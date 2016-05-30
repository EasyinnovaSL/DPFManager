package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.conformancechecker.tiff.policy_checker.Rule;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;
import dpfmanager.conformancechecker.tiff.policy_checker.Schematron;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.ReportHtml;
import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.ImageStrips;
import com.easyinnova.tiff.model.Strip;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sun.awt.image.ImageDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by easy on 02/05/2016.
 */
public class XmlReport {
  /**
   * Creates the ifd node.
   *
   * @param doc the doc
   * @param ir the ir
   * @param ifd the ifd
   * @param index the index
   * @return the element
   */
  private Element createIfdNode(Document doc, IndividualReport ir, IFD ifd, int index) {
    Element ifdNode = doc.createElement("ifdNode");
    Element el, elchild, elchild2;

    // Number
    el = doc.createElement("number");
    el.setTextContent("" + index);
    ifdNode.appendChild(el);

    // Image
    el = doc.createElement("isimg");
    if (ifd.isImage()) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // Thumbnail or main
    String typ = "Main image";
    if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = "Thumbnail";
    el = doc.createElement("imagetype");
    el.setAttribute("check_ifd0", "typ");
    el.setTextContent(typ);
    ifdNode.appendChild(el);

    // Strips or Tiles
    el = doc.createElement("image_representation");
    if (ifd.hasStrips()) {
      el.setTextContent("strips[" + ifd.getImageStrips().getStrips().size() + "]");
    } else if (ifd.hasTiles()) {
      el.setTextContent("tiles[" + ifd.getImageTiles().getTiles().size() + "]");
    } else {
      el.setTextContent("none");
    }
    ifdNode.appendChild(el);

    // Photometric
    el = doc.createElement("photometric");
    TagValue tagv = ifd.getMetadata().get("PhotometricInterpretation");
    if (tagv != null) {
      el.setTextContent(tagv.getFirstNumericValue() + "");
    } else {
      el.setTextContent("null");
    }
    ifdNode.appendChild(el);

    // SubImage
    el = doc.createElement("hasSubIfd");
    if (ifd.getsubIFD() != null) {
      typ="Thumbnail";
      if (ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = "Main image";
      el.setTextContent(typ);
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // Exif
    el = doc.createElement("hasExif");
    if (ifd.containsTagId(34665)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // XMP
    el = doc.createElement("hasXMP");
    if (ifd.containsTagId(700)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // IPTC
    el = doc.createElement("hasIPTC");
    if (ifd.containsTagId(33723)) {
      el.setTextContent("yes");
    } else {
      el.setTextContent("no");
    }
    ifdNode.appendChild(el);

    // Tags
    el = doc.createElement("tags");
    IfdTags ifdTags = ifd.getMetadata();
    for (TagValue t : ifdTags.getTags()) {
      elchild = doc.createElement("tag");

      elchild2 = doc.createElement("name");
      elchild2.setTextContent(t.getName());
      elchild.appendChild(elchild2);

      elchild2 = doc.createElement("id");
      elchild2.setTextContent(t.getId()+"");
      elchild.appendChild(elchild2);

      elchild2 = doc.createElement("value");
      if (t.getCardinality() == 1 || t.toString().length() < 100)
        elchild2.setTextContent(t.toString());
      else
        elchild2.setTextContent("Array[" + t.getCardinality() + "]");
      elchild.appendChild(elchild2);

      el.appendChild(elchild);
    }
    ifdNode.appendChild(el);

    return ifdNode;
  }

  /**
   * Gets tags.
   *
   * @param ir the ir
   * @return the tags
   */
  protected ArrayList<ReportTag> getTags(IndividualReport ir) {
    ArrayList<ReportTag> list = new ArrayList<ReportTag>();
    TiffDocument td = ir.getTiffModel();
    IFD ifd = td.getFirstIFD();
    IFD ifdcomp = null;
    if (ir.getCompareReport() != null) {
      ifdcomp = ir.getCompareReport().getTiffModel().getFirstIFD();
    }
    td.getFirstIFD();
    int index = 0;
    while (ifd != null) {
      IfdTags meta = ifd.getMetadata();
      for (TagValue tv : meta.getTags()) {
        ReportTag tag = new ReportTag();
        tag.index = index;
        tag.tv = tv;
        if (ifdcomp != null) {
          if (!ifdcomp.getMetadata().containsTagId(tv.getId()))
            tag.dif = 1;
        }
        if (!showTag(tv)) tag.expert = true;
        list.add(tag);
      }
      if (ifdcomp != null) {
        for (TagValue tv : ifdcomp.getMetadata().getTags()) {
          if (!meta.containsTagId(tv.getId())) {
            ReportTag tag = new ReportTag();
            tag.index = index;
            tag.tv = tv;
            tag.dif = -1;
            if (!showTag(tv)) tag.expert = true;
            list.add(tag);
          }
        }
      }
      ifd = ifd.getNextIFD();
      if (ifdcomp != null) ifdcomp = ifdcomp.getNextIFD();
      index++;
    }
    return list;
  }

  /**
   * Read showable tags file.
   *
   * @return hashset of tags
   */
  protected HashSet<String> readShowableTags() {
    HashSet<String> hs = new HashSet<String>();
    try {
      Path path = Paths.get("./src/main/resources/");
      if (Files.exists(path)) {
        // Look in current dir
        FileReader fr = new FileReader("./src/main/resources/htmltags.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
          String[] fields = line.split("\t");
          if (fields.length == 1) {
            hs.add(fields[0]);
          }
          line = br.readLine();
        }
        br.close();
        fr.close();
      } else {
        // Look in JAR
        String resource = "htmltags.txt";
        Class cls = ReportHtml.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(resource);
        //CodeSource src = ReportHtml.class.getProtectionDomain().getCodeSource();
        if (in != null) {
          try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            while (line != null) {
              String[] fields = line.split("\t");
              if (fields.length == 1) {
                hs.add(fields[0]);
              }
              line = br.readLine();
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        } else {
          throw new Exception("InputStream is null");
        }
      }
    } catch (Exception ex) {
    }
    return hs;
  }

  /**
   * Show Tag.
   *
   * @param tv The tag value
   * @return true, if successful
   */
  protected boolean showTag(TagValue tv) {
    HashSet<String> showableTags = readShowableTags();
    /*showableTags.add("ImageWidth");
    showableTags.add("ImageLength");
    showableTags.add("BitsPerSample");
    showableTags.add("Compression");
    showableTags.add("PhotometricInterpretation");
    showableTags.add("ImageDescription");
    showableTags.add("Make");
    showableTags.add("Model");
    showableTags.add("Orientation");
    showableTags.add("SamplesPerPixel");
    showableTags.add("XResolution");
    showableTags.add("YResolution");
    showableTags.add("ResolutionUnit");
    showableTags.add("PlanarConfiguration");
    showableTags.add("Software");
    showableTags.add("DateTime");
    showableTags.add("Artist");
    showableTags.add("Copyright");
    showableTags.add("DateTimeOriginal");
    showableTags.add("Flash");
    showableTags.add("TIFFEPStandardID");*/
    //if (tv.getName().equals(""+tv.getId())) return false;
    return showableTags.contains(tv.getName());
  }

  /**
   * Adds the errors warnings.
   *
   * @param doc the doc
   * @param results the results
   * @param errors the errors
   * @param warnings the warnings
   */
  private void addErrorsWarnings(Document doc, Element results,
                                 List<RuleResult> errors, List<RuleResult> warnings) {
    // errors
    for (int i = 0; i < errors.size(); i++) {
      RuleResult value = errors.get(i);
      Element error = doc.createElement("rule_result");

      // level
      Element level = doc.createElement("level");
      level.setTextContent("critical");
      error.appendChild(level);

      // msg
      Element msg = doc.createElement("message");
      msg.setTextContent(value.getDescription());
      error.appendChild(msg);

      // context
      msg = doc.createElement("context");
      msg.setTextContent(value.getContext());
      error.appendChild(msg);

      // location
      msg = doc.createElement("location");
      msg.setTextContent(value.getLocation());
      error.appendChild(msg);

      // rule
      if (value.getRule() != null) {
        msg = doc.createElement("ruleId");
        msg.setTextContent(value.getRule().getReference());
        error.appendChild(msg);

        msg = doc.createElement("ruleTest");
        msg.setTextContent(value.getRule().getAssertionField().getTest());
        error.appendChild(msg);

        msg = doc.createElement("ruleValue");
        msg.setTextContent(value.getRule().getAssertionField().getValue());
        error.appendChild(msg);
      }

      results.appendChild(error);
    }

    // warnings
    for (int i = 0; i < warnings.size(); i++) {
      RuleResult value = warnings.get(i);
      Element warning = doc.createElement("rule_result");

      // level
      Element level = doc.createElement("level");
      level.setTextContent("warning");
      warning.appendChild(level);

      // msg
      Element msg = doc.createElement("message");
      msg.setTextContent(value.getDescription());
      warning.appendChild(msg);

      // context
      msg = doc.createElement("context");
      msg.setTextContent(value.getContext());
      warning.appendChild(msg);

      // location
      msg = doc.createElement("location");
      msg.setTextContent(value.getLocation());
      warning.appendChild(msg);

      // rule
      if (value.getRule() != null) {
        msg = doc.createElement("ruleId");
        msg.setTextContent(value.getRule().getReference());
        warning.appendChild(msg);

        msg = doc.createElement("ruleTest");
        msg.setTextContent(value.getRule().getAssertionField().getTest());
        warning.appendChild(msg);

        msg = doc.createElement("ruleValue");
        msg.setTextContent(value.getRule().getAssertionField().getValue());
        warning.appendChild(msg);
      }

      results.appendChild(warning);
    }
  }

  private ImageReader getTiffImageReader() {
    Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByFormatName("TIFF");
    if (!imageReaders.hasNext()) {
      throw new UnsupportedOperationException("No TIFF Reader found!");
    }
    return imageReaders.next();
  }

  private ImageInputStream openImageInputStream(String filename) throws IOException {
    return ImageIO.createImageInputStream(filename);
  }

  /**
   * Parse an individual report to XML format.
   *
   * @param doc the doc
   * @param ir the individual report.
   * @return the element
   */
  private Element buildReportIndividual(Document doc, IndividualReport ir, Rules rules) {
    Element report = doc.createElement("report");

    // file info
    Element fileInfoStructure = doc.createElement("file_info");
    Element nameElement = doc.createElement("name");
    nameElement.setTextContent(ir.getFileName());
    Element pathElement = doc.createElement("fullpath");
    pathElement.setTextContent(ir.getFilePath());
    fileInfoStructure.appendChild(nameElement);
    fileInfoStructure.appendChild(pathElement);
    report.appendChild(fileInfoStructure);

    if (ir.containsData()) {
      // tiff structure
      Element tiffStructureElement = doc.createElement("tiff_structure");
      Element ifdTree = doc.createElement("ifdTree");
      int index = 0;
      IFD ifd = ir.getTiffModel().getFirstIFD();
      while (ifd != null) {
        Element ifdNode = createIfdNode(doc, ir, ifd, index++);
        ifdTree.appendChild(ifdNode);
        ifd = ifd.getNextIFD();
      }

      tiffStructureElement.appendChild(ifdTree);
      report.appendChild(tiffStructureElement);

      // basic info
      Element infoElement;
      infoElement = doc.createElement("ByteOrder");
      String endianess = "none";
      if (ir.getTiffModel().getEndianess() != null){
        endianess = ir.getTiffModel().getEndianess().toString();
      }
      infoElement.setTextContent(endianess);
      infoElement.setAttribute("ByteOrder", endianess);
      report.appendChild(infoElement);

      infoElement = doc.createElement("NumberImages");
      String numberimages = ir.getTiffModel().getImageIfds().size() + "";
      infoElement.setTextContent(numberimages);
      infoElement.setAttribute("NumberImages", "" + (int) Double.parseDouble(numberimages));
      report.appendChild(infoElement);

      boolean checkBlankPages = false;
      if (rules != null) {
        for (Rule rule : rules.getRules()) {
          if (rule.getTag().equals("BlankPage")) {
            checkBlankPages = true;
            break;
          }
        }
      }

      ifd = ir.getTiffModel().getFirstIFD();
      int numBlankPages = 0;
      ImageReader reader = null;
      try {
        File fi = new File(ir.getFilePath());
        ImageInputStream iis = javax.imageio.ImageIO.createImageInputStream(fi);
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
        reader = iterator.next();
        reader.setInput(iis);
      } catch (Exception e) {
        reader = null;
      }
      int imageIndex = 0;
      while (ifd != null) {
        // For each image
        String bps = ifd.getMetadata().get("BitsPerSample").toString();
        infoElement = doc.createElement("bitspersample");
        infoElement.setTextContent(bps);
        report.appendChild(infoElement);

        infoElement = doc.createElement("BitDepth");
        infoElement.setTextContent(bps);
        infoElement.setAttribute("BitDepth", bps);
        report.appendChild(infoElement);

        if (ifd.getMetadata().containsTagId(TiffTags.getTagId("Compression"))) {
          infoElement = doc.createElement("Compression");
          int comp = Integer.parseInt(ifd.getMetadata().get("Compression").toString());
          String value = comp > 0 ? TiffConformanceChecker.compressionName(comp) : "Unknown";
          infoElement.setTextContent(value);
          infoElement.setAttribute("Compression", value);
          report.appendChild(infoElement);
        }

        String value = "Unknown";
        if (ifd.getMetadata().containsTagId(TiffTags.getTagId("PhotometricInterpretation"))) {
          int photo = Integer.parseInt(ifd.getMetadata().get("PhotometricInterpretation").toString());
          value = TiffConformanceChecker.photometricName(photo);
        }
        infoElement = doc.createElement("Photometric");
        infoElement.setTextContent(value);
        infoElement.setAttribute("Photometric", value);
        report.appendChild(infoElement);

        value = "Unknown";
        if (ifd.getMetadata().containsTagId(TiffTags.getTagId("PlanarConfiguration"))) {
          int planar = Integer.parseInt(ifd.getMetadata().get("PlanarConfiguration").toString());
          value = TiffConformanceChecker.planarName(planar);
        }
        infoElement = doc.createElement("Planar");
        infoElement.setTextContent(value);
        infoElement.setAttribute("Planar", value);
        report.appendChild(infoElement);

        String pixeldensity = "0";
        if (ifd.getMetadata().containsTagId(TiffTags.getTagId("ResolutionUnit")) && ifd.getMetadata().containsTagId(TiffTags.getTagId("XResolution")))
        {
          try {
            double ru = Double.parseDouble(ifd.getMetadata().get("ResolutionUnit").toString());
            String xres = ifd.getMetadata().get("XResolution").toString();
            double num = Double.parseDouble(xres.substring(0, xres.indexOf("/")));
            double den = Double.parseDouble(xres.substring(xres.indexOf("/") + 1));
            double xr = num / den;
            double ppi;
            if (ru == 2) {
              ppi = xr;
            } else {
              ppi = xr / 0.3937;
            }
            pixeldensity = ppi+"";
          } catch (Exception ex) {
            pixeldensity = "";
          }
        }
        infoElement = doc.createElement("PixelDensity");
        infoElement.setTextContent(pixeldensity);
        infoElement.setAttribute("PixelDensity", "" + (int) Double.parseDouble(pixeldensity));
        report.appendChild(infoElement);

        double percent_blank_pixels = 0.95;
        double tolerance = 0.95;
        if (checkBlankPages) {
          try {
            if (reader != null) {
              BufferedImage bufferedImage = reader.read(imageIndex);
              int x, y;
              int ww = bufferedImage.getWidth();
              int hh = bufferedImage.getHeight();
              int nblanks = 0;
              for (x = 0; x < ww; x++) {
                for (y = 0; y < hh; y++) {
                  boolean isBlank = true;
                  int col = bufferedImage.getRGB(x, y);
                  Color color = new Color(col);
                  if (color.getRed() < 255 * tolerance) isBlank = false;
                  if (color.getGreen() < 255 * tolerance) isBlank = false;
                  if (color.getBlue() < 255 * tolerance) isBlank = false;
                  if (isBlank) nblanks++;
                }
              }
              boolean isBlank = (double)nblanks / (ww*hh) > percent_blank_pixels;
              value = "0";
              if (isBlank) {
                value = "1";
                numBlankPages++;
              }
              infoElement = doc.createElement("BlankPage");
              infoElement.setTextContent(value);
              infoElement.setAttribute("BlankPage", value);
              report.appendChild(infoElement);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        ifd = ifd.getNextIFD();
      }

      infoElement = doc.createElement("NumberBlankImages");
      infoElement.setTextContent(numBlankPages + "");
      infoElement.setAttribute("NumberBlankImages", "" + numBlankPages);
      report.appendChild(infoElement);

      // tags
      for (ReportTag tag : getTags(ir)) {
        try {
          if (tag.tv.getName().equals("Compression")) continue;
          String tagname = tag.tv.getName();
          if (tagname.equals(tag.tv.getId() + "")) tagname = "Undefined" + tagname;
          infoElement = doc.createElement(tagname);
          infoElement.setTextContent(tag.tv.toString());
          infoElement.setAttribute(tagname, tag.tv.toString());
          infoElement.setAttribute("id", tag.tv.getId() + "");
          infoElement.setAttribute("type", tag.dif + "");
          report.appendChild(infoElement);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      // implementation checker
      List<RuleResult> errorsTotal = new ArrayList<RuleResult>();
      List<RuleResult> warningsTotal = new ArrayList<RuleResult>();
      if (ir.getBaselineErrors() != null) errorsTotal.addAll(ir.getBaselineErrors());
      if (ir.getEPErrors() != null) errorsTotal.addAll(ir.getEPErrors());
      if (ir.getITErrors(0) != null) errorsTotal.addAll(ir.getITErrors(0));
      if (ir.getITErrors(1) != null) errorsTotal.addAll(ir.getITErrors(1));
      if (ir.getITErrors(2) != null) errorsTotal.addAll(ir.getITErrors(2));
      if (ir.getBaselineWarnings() != null) warningsTotal.addAll(ir.getBaselineWarnings());
      if (ir.getEPWarnings() != null) warningsTotal.addAll(ir.getEPWarnings());
      if (ir.getITWarnings(0) != null) warningsTotal.addAll(ir.getITWarnings(0));
      if (ir.getITWarnings(1) != null) warningsTotal.addAll(ir.getITWarnings(1));
      if (ir.getITWarnings(2) != null) warningsTotal.addAll(ir.getITWarnings(2));

      Element implementationCheckerElement = doc.createElement("implementation_checker");
      implementationCheckerElement.setAttribute("version", "2.1");
      implementationCheckerElement.setAttribute("ref", "DPF Manager");
      implementationCheckerElement.setAttribute("totalErrors", errorsTotal.size() + "");
      implementationCheckerElement.setAttribute("totalWarnings", warningsTotal.size() + "");
      report.appendChild(implementationCheckerElement);

      // Baseline
      List<RuleResult> errors = ir.getBaselineErrors();
      List<RuleResult> warnings = ir.getBaselineWarnings();
      if (errors != null && warnings != null) {
        Element results = doc.createElement("implementation_check");
        Element name = doc.createElement("name");
        name.setTextContent("Baseline 6.0");
        results.appendChild(name);
        addErrorsWarnings(doc, results, errors, warnings);
        implementationCheckerElement.appendChild(results);
      }

      // TiffEP
      errors = ir.getEPErrors();
      warnings = ir.getEPWarnings();
      if (errors != null && warnings != null) {
        Element results = doc.createElement("implementation_check");
        Element name = doc.createElement("name");
        name.setTextContent("Tiff/EP");
        results.appendChild(name);
        addErrorsWarnings(doc, results, errors, warnings);
        implementationCheckerElement.appendChild(results);
      }

      // TiffIT
      errors = ir.getITErrors(0);
      warnings = ir.getITWarnings(0);
      if (errors != null && warnings != null) {
        Element results = doc.createElement("implementation_check");
        Element name = doc.createElement("name");
        name.setTextContent("Tiff-IT");
        results.appendChild(name);
        addErrorsWarnings(doc, results, errors, warnings);
        implementationCheckerElement.appendChild(results);
      }

      // TiffIT-1
      errors = ir.getITErrors(1);
      warnings = ir.getITWarnings(1);
      if (errors != null && warnings != null) {
        Element results = doc.createElement("implementation_check");
        Element name = doc.createElement("name");
        name.setTextContent("Tiff-IT P1");
        results.appendChild(name);
        addErrorsWarnings(doc, results, errors, warnings);
        implementationCheckerElement.appendChild(results);
      }

      // TiffIT-2
      errors = ir.getITErrors(2);
      warnings = ir.getITWarnings(2);
      if (errors != null && warnings != null) {
        Element results = doc.createElement("implementation_check");
        Element name = doc.createElement("name");
        name.setTextContent("Tiff-IT P2");
        results.appendChild(name);
        addErrorsWarnings(doc, results, errors, warnings);
        implementationCheckerElement.appendChild(results);
      }

      // Total
      Element results = doc.createElement("results");
      addErrorsWarnings(doc, results, errorsTotal, warningsTotal);
      implementationCheckerElement.appendChild(results);
    } else {
      try {
        // External results
        Element implementationCheckerElement = doc.createElement("implementation_checker");
        report.appendChild(implementationCheckerElement);

        DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc2 = dBuilder.parse(new ByteArrayInputStream(ir.getConformanceCheckerReport().getBytes()));
        Node node = doc.importNode(doc2.getDocumentElement(), true);
        implementationCheckerElement.appendChild(doc.importNode(node, true));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    return report;
  }

  /**
   * Parse an individual report to XML format.
   *
   * @param ir      the individual report.
   * @param rules   the policy checker.
   * @return the XML string generated.
   */
  public String parseIndividual(IndividualReport ir, Rules rules) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element report = buildReportIndividual(doc, ir, rules);
      doc.appendChild(report);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      // To String
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      String output = writer.getBuffer().toString();

      // Schematron
      Schematron sch = new Schematron();
      try {
        String resultsch = sch.testXML(output, rules);
        String presch = output.substring(0, output.indexOf("</report>"));
        String postsch = output.substring(output.indexOf("</report>"));
        if (resultsch.indexOf("<svrl:schematron-output") > -1) {
          resultsch = resultsch.substring(resultsch.indexOf("<svrl:schematron-output"));
        }
        output = presch + resultsch + postsch;
      } catch (Exception e) {
        e.printStackTrace();
      }

      return output;

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
    return "";
  }
}