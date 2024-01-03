/**
 * <h1>XmlReport.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Victor Muñoz Sola
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.policy_checker.PolicyConstants;
import com.easyinnova.policy_checker.model.Rule;
import com.easyinnova.policy_checker.model.Rules;
import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.Rational;
import com.easyinnova.tiff.model.types.abstractTiffType;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.modules.report.core.IndividualReport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by easy on 02/05/2016.
 */
public class XmlReport extends Report {
  /**
   * Creates the ifd node.
   *
   * @param doc   the doc
   * @param ir    the ir
   * @param ifd   the ifd
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
    if (tagv != null && tagv.getCardinality() > 0) {
      el.setTextContent(tagv.getFirstNumericValue() + "");
    } else {
      el.setTextContent("null");
    }
    ifdNode.appendChild(el);

    // SubImage
    el = doc.createElement("hasSubIfd");
    if (ifd.getsubIFD() != null) {
      typ = "Thumbnail";
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
//    for (TagValue t : ifdTags.getTags()) {
    for (ReportTag tag : ir.getTags(false, true)) {
      elchild = doc.createElement("tag");

      elchild2 = doc.createElement("name");
      elchild2.setTextContent((tag.tv.getName().equals(tag.tv.getId()) ? "Private tag" : tag.tv.getName()));
      elchild.appendChild(elchild2);

      elchild2 = doc.createElement("id");
      elchild2.setTextContent(tag.tv.getId() + "");
      elchild.appendChild(elchild2);

      elchild2 = doc.createElement("value");
      String val = (tag.isDefault) ? tag.defaultValue : tag.tv.getFirstTextReadValue();
      elchild2.setTextContent(val);
      elchild.appendChild(elchild2);

      if (tag.isDefault) {
        elchild2 = doc.createElement("default");
        elchild2.setTextContent("true");
        elchild.appendChild(elchild2);
      }

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
    if (ir.getCompareReport() != null && !ir.getIsOriginal()) {
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
        if (!ir.showTag(tv)) tag.expert = true;
        list.add(tag);
      }
      if (ifdcomp != null) {
        for (TagValue tv : ifdcomp.getMetadata().getTags()) {
          if (!meta.containsTagId(tv.getId())) {
            ReportTag tag = new ReportTag();
            tag.index = index;
            tag.tv = tv;
            tag.dif = -1;
            if (!ir.showTag(tv)) tag.expert = true;
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
   * Adds the errors warnings.
   *
   * @param doc      the doc
   * @param results  the results
   * @param errors   the errors
   * @param warnings the warnings
   */
  private void addErrorsWarnings(Document doc, Element results,
                                 List<RuleResult> errors, List<RuleResult> warnings, boolean policyValue) {
    // errors
    for (int i = 0; i < errors.size(); i++) {
      RuleResult value = errors.get(i);
      addResult(value, doc, results, true, policyValue);
    }

    // warnings
    for (int i = 0; i < warnings.size(); i++) {
      RuleResult warning = warnings.get(i);
      if (!warning.getWarning()) continue;
      addResult(warning, doc, results, false, policyValue);
    }

    // infos
    for (int i = 0; i < warnings.size(); i++) {
      RuleResult warning = warnings.get(i);
      if (!warning.getInfo()) continue;
      addResult(warning, doc, results, false, policyValue);
    }
  }

  private void addResult(RuleResult value, Document doc, Element results, boolean error, boolean policyValue) {
    Element warning = doc.createElement("rule_result");

    // level
    Element level = doc.createElement("level");
    String levelStr = "";
    if (policyValue && value.isRelaxed()) {
      levelStr += "omitted ";
    }
    if (error) {
      levelStr += "error";
    } else if (value.getWarning()) {
      levelStr += "warning";
    } else {
      levelStr += "info";
    }
    level.setTextContent(levelStr);
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
      msg.setTextContent(value.getRule().getId());
      warning.appendChild(msg);

      msg = doc.createElement("ruleTest");
      msg.setTextContent(value.getRule().getAssert().getTest());
      warning.appendChild(msg);

      msg = doc.createElement("ruleValue");
      msg.setTextContent(value.getRule().getAssert().getValue());
      warning.appendChild(msg);

      if (value.getReference() != null) {
        // ISO reference
        msg = doc.createElement("iso_reference");
        msg.setTextContent(value.getReference());
        warning.appendChild(msg);
      }
    }

    results.appendChild(warning);
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
   * @param ir  the individual report.
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
    Element sizeElement = doc.createElement("filesize");
    sizeElement.setTextContent(new File(ir.getFilePath()).length() + "");
    fileInfoStructure.appendChild(nameElement);
    fileInfoStructure.appendChild(pathElement);
    fileInfoStructure.appendChild(sizeElement);

    report.appendChild(fileInfoStructure);

    if (ir.containsData()) {
      /**
       * tiff structure
       */
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

      /**
       * Tags
       */
      Element infoElement;
      infoElement = doc.createElement("ByteOrder");
      String endianess = "none";
      if (ir.getTiffModel().getEndianess() != null) {
        endianess = ir.getTiffModel().getEndianess().toString();
      }
      infoElement.setTextContent(endianess);
      infoElement.setAttribute("ByteOrder", endianess);
      report.appendChild(infoElement);

      if (ir.getTiffModel() != null && ir.getTiffModel().getIccProfile() != null) {
        infoElement = doc.createElement("IccProfileClass");
        infoElement.setTextContent(ir.getTiffModel().getIccProfile().getProfileClass() + "");
        infoElement.setAttribute("IccProfileClass", "" + ir.getTiffModel().getIccProfile().getProfileClass());
        report.appendChild(infoElement);

        infoElement = doc.createElement("IccProfileDescription");
        infoElement.setTextContent(ir.getTiffModel().getIccProfile().getDescription());
        infoElement.setAttribute("IccProfileDescription", "" + ir.getTiffModel().getIccProfile().getDescription());
        report.appendChild(infoElement);

        if (ir.getTiffModel().getIccProfile().getCreator() != null) {
          infoElement = doc.createElement("IccProfileCreator");
          infoElement.setTextContent(ir.getTiffModel().getIccProfile().getCreator().getCreator());
          infoElement.setAttribute("IccProfileCreator", "" + ir.getTiffModel().getIccProfile().getCreator().getCreator());
          report.appendChild(infoElement);
        }

        infoElement = doc.createElement("IccProfileVersion");
        infoElement.setTextContent(ir.getTiffModel().getIccProfile().getVersion());
        infoElement.setAttribute("IccProfileVersion", "" + ir.getTiffModel().getIccProfile().getVersion());
        report.appendChild(infoElement);
      }

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
      checkBlankPages = false;

      ifd = ir.getTiffModel().getFirstIFD();
      int numBlankPages = 0;
      ImageReader reader = null;
      ImageInputStream iis = null;
      if (checkBlankPages) {
        try {
          File fi = new File(ir.getFilePath());
          iis = javax.imageio.ImageIO.createImageInputStream(fi);
          Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
          reader = iterator.next();
          reader.setInput(iis);
        } catch (Exception e) {
          reader = null;
        }
      }
      int imageIndex = 0;
      while (ifd != null) {
        // For each image
        if (ifd.getMetadata() != null && ifd.getMetadata().get("BitsPerSample") != null) {
          String bps = ifd.getMetadata().get("BitsPerSample").toString();
          infoElement = doc.createElement("bitspersample");
          infoElement.setTextContent(bps);
          report.appendChild(infoElement);

          infoElement = doc.createElement("BitDepth");
          infoElement.setTextContent(bps);
          infoElement.setAttribute("BitDepth", bps);
          report.appendChild(infoElement);
        }

        String eqxy = "True";
        if (ifd.getTags().containsTagId(TiffTags.getTagId("XResolution")) && ifd.getTags().containsTagId(TiffTags.getTagId("YResolution"))) {
          if (!ifd.getTag("XResolution").toString().equals(ifd.getTag("YResolution").toString()))
            eqxy = "False";
        }
        infoElement = doc.createElement("EqualXYResolution");
        infoElement.setTextContent(eqxy);
        infoElement.setAttribute("EqualXYResolution", eqxy);
        report.appendChild(infoElement);

        String evenness = "";
        if (ifd.getTags().containsTagId(TiffTags.getTagId("XResolution")) && ifd.getTags().containsTagId(TiffTags.getTagId("YResolution"))
            && ifd.getTag("XResolution").getValue() != null && ifd.getTag("YResolution").getValue() != null
            && ifd.getTag("XResolution").getValue().size() > 0 && ifd.getTag("YResolution").getValue().size() > 0) {
          try {
            abstractTiffType rx = ifd.getTag("XResolution").getValue().get(0);
            abstractTiffType ry = ifd.getTag("YResolution").getValue().get(0);
            if (rx instanceof Rational && ry instanceof Rational) {
              Rational ratx = (Rational) rx;
              Rational raty = (Rational) ry;
              int xres = (int) ratx.getFloatValue();
              int yres = (int) raty.getFloatValue();
              if (xres % 2 != 0 || yres % 2 != 0)
                evenness = "Uneven";
              else
                evenness = "Even";
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        infoElement = doc.createElement("Evenness");
        infoElement.setTextContent(evenness);
        infoElement.setAttribute("Evenness", evenness);
        report.appendChild(infoElement);

        infoElement = doc.createElement("FileSize");
        long size = ir.getTiffModel().getSize();
        infoElement.setTextContent(size + "");
        infoElement.setAttribute("FileSize", size + "");
        report.appendChild(infoElement);

        String extra = "0";
        if (ifd.getTags().containsTagId(TiffTags.getTagId("ExtraSamples")))
          extra = ifd.getTag("ExtraSamples").getCardinality() + "";
        infoElement = doc.createElement("ExtraChannels");
        infoElement.setTextContent(extra);
        infoElement.setAttribute("ExtraChannels", extra);
        report.appendChild(infoElement);

        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("Compression"))) {
          infoElement = doc.createElement("Compression");
          String value = "Unknown";
          try {
            int comp = Integer.parseInt(ifd.getMetadata().get("Compression").toString());
            value = comp > 0 ? PolicyConstants.compressionName(comp) : "Unknown";
          } catch (Exception ex) {

          }
          infoElement.setTextContent(value);
          infoElement.setAttribute("Compression", value);
          report.appendChild(infoElement);
        }

        String value = "Unknown";
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("PhotometricInterpretation"))) {
          try {
            int photo = Integer.parseInt(ifd.getMetadata().get("PhotometricInterpretation").toString());
            value = PolicyConstants.photometricName(photo);
          } catch (Exception ex) {
            value = "Unknown";
          }
        }
        infoElement = doc.createElement("Photometric");
        infoElement.setTextContent(value);
        infoElement.setAttribute("Photometric", value);
        report.appendChild(infoElement);

        value = "Unknown";
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("PlanarConfiguration"))) {
          try {
            String val = ifd.getMetadata().get("PlanarConfiguration").toString();
            int planar = Integer.parseInt(val);
            value = PolicyConstants.planarName(planar);
          } catch (Exception ex) {
            value = "Unknown";
          }
        }
        infoElement = doc.createElement("Planar");
        infoElement.setTextContent(value);
        infoElement.setAttribute("Planar", value);
        report.appendChild(infoElement);

        String pixeldensity = "0";
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("ResolutionUnit")) && ifd.getMetadata().containsTagId(TiffTags.getTagId("XResolution"))) {
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
            pixeldensity = ppi + "";
          } catch (Exception ex) {
            pixeldensity = "";
          }
        }
        infoElement = doc.createElement("PixelDensity");
        infoElement.setTextContent(pixeldensity);
        try {
          infoElement.setAttribute("PixelDensity", "" + (int) Double.parseDouble(pixeldensity));
        } catch (Exception ex) {
          infoElement.setAttribute("PixelDensity", "0");
        }
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
              boolean isBlank = (double) nblanks / (ww * hh) > percent_blank_pixels;
              value = "False";
              if (isBlank) {
                value = "True";
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
      if (checkBlankPages) {
        if (iis != null) {
          try {
            iis.close();
          } catch (Exception e) {

          }
        }
      }

      infoElement = doc.createElement("NumberBlankImages");
      infoElement.setTextContent(numBlankPages + "");
      infoElement.setAttribute("NumberBlankImages", "" + numBlankPages);
      report.appendChild(infoElement);

      // tags
      for (ReportTag tag : ir.getTags(false, true)) {
        try {
          if (tag.tv.getName().equals("Compression")) continue;

          /*if (tag.tv.getId() == 700) {
            // XMP
            String tagname = tag.tv.getFileName().replace(" ", "");
            if (tagname.equals(tag.tv.getId() + "")) tagname = "Undefined" + tagname;
            infoElement = doc.createElement(tagname);
            infoElement.setAttribute("id", tag.tv.getId() + "");
            infoElement.setAttribute("type", tag.dif + "");

            for (abstractTiffType to : tag.tv.getValue()) {
              XMP xmp = (XMP)to;
              try {
                Metadata metadata = xmp.createMetadata();
                for (String key : metadata.keySet()) {
                  Element childElement = doc.createElement(key);
                  String val = metadata.get(key).toString().trim().toString().replaceAll("\\p{C}", "?");;

                  childElement.setTextContent(val);
                  childElement.setAttribute(tagname, val);
                  infoElement.appendChild(childElement);
                }
                Element historyElement = doc.createElement("xmpHistory");
                for (Hashtable<String, String> kv : xmp.getHistory()) {
                  for (String key : kv.keySet()) {
                    Element childElement = doc.createElement(key);
                    String val = kv.get(key).toString().trim().replaceAll("\\p{C}", "?");;

                    childElement.setTextContent(val);
                    childElement.setAttribute(tagname, val);
                    historyElement.appendChild(childElement);
                  }
                }
                infoElement.appendChild(historyElement);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
            report.appendChild(infoElement);

            continue;
          }
          if (tag.tv.getId() == 33723) {
            // IPTC
            String tagname = tag.tv.getFileName().replace(" ", "");
            if (tagname.equals(tag.tv.getId() + "")) tagname = "Undefined" + tagname;
            infoElement = doc.createElement(tagname);
            infoElement.setAttribute("id", tag.tv.getId() + "");
            infoElement.setAttribute("type", tag.dif + "");

            for (abstractTiffType to : tag.tv.getValue()) {
              IPTC iptc = (IPTC)to;
              try {
                Metadata metadata = iptc.createMetadata();
                for (String key : metadata.keySet()) {
                  Element childElement = doc.createElement(key);
                  String val = metadata.get(key).toString().trim().toString().replaceAll("\\p{C}", "?");;

                  childElement.setTextContent(val);
                  childElement.setAttribute(tagname, val);
                  infoElement.appendChild(childElement);
                }
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
            report.appendChild(infoElement);

            continue;
          }
          if (tag.tv.getId() == 34665) {
            // EXIF
            String tagname = tag.tv.getFileName().replace(" ", "");
            if (tagname.equals(tag.tv.getId() + "")) tagname = "Undefined" + tagname;
            infoElement = doc.createElement(tagname);
            infoElement.setAttribute("id", tag.tv.getId() + "");
            infoElement.setAttribute("type", tag.dif + "");

            for (abstractTiffType to : tag.tv.getValue()) {
              IFD exif = (IFD)to;
              try {
                for (TagValue tv : exif.getTags().getTags()) {
                  String name = tv.getFileName().replace(" ", "");
                  if (name.equals(tv.getId() + "")) name = "Undefined" + name;
                  Element childElement = doc.createElement(name);
                  String val = tv.getDescriptiveValue().toString().replaceAll("\\p{C}", "?");;

                  childElement.setTextContent(val);
                  childElement.setAttribute(tagname, val);
                  childElement.setAttribute("id", tv.getId() + "");
                  infoElement.appendChild(childElement);
                }
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
            report.appendChild(infoElement);

            continue;
          }*/

          String tagname = tag.tv.getName().replace(" ", "");
          if (tagname.equals(tag.tv.getId() + "")) tagname = "Undefined" + tagname;
          infoElement = doc.createElement(tagname);
          String val = tag.tv.getFirstTextReadValue().replaceAll("\\p{C}", "?");
          if (val.length() > 500) val = val.substring(0, 500) + "...";

          infoElement.setTextContent(val);
          infoElement.setAttribute(tagname, val);
          infoElement.setAttribute("id", tag.tv.getId() + "");
          infoElement.setAttribute("type", tag.dif + "");
          report.appendChild(infoElement);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      /**
       * Implementation Checker
       */
      List<RuleResult> errorsTotal = ir.getAllErrors();
      List<RuleResult> warningsTotal = ir.getAllWarnings();
      Element implementationCheckerElement = doc.createElement("implementation_checker");
      implementationCheckerElement.setAttribute("version", DPFManagerProperties.getVersion());
      implementationCheckerElement.setAttribute("ref", "DPF Manager");
      if (!ir.isQuick()){
        implementationCheckerElement.setAttribute("totalErrors", errorsTotal.size() + "");
        implementationCheckerElement.setAttribute("totalWarnings", warningsTotal.size() + "");
        for (String path : ImplementationCheckerLoader.getPathsList()) {
          String name = ImplementationCheckerLoader.getFileName(path);
          implementationCheckerElement.setAttribute(name, (ir.getErrors(name).size() == 0) + "");
        }
        for (String iso : ir.getCheckedIsos()) {
          String title = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
          List<RuleResult> errors = ir.getErrors(iso);
          List<RuleResult> warnings = ir.getWarnings(iso);
          Element implementationCheck = doc.createElement("implementation_check");
          Element name = doc.createElement("name");
          name.setTextContent(title);
          implementationCheck.appendChild(name);
          addErrorsWarnings(doc, implementationCheck, errors, warnings, false);
          implementationCheckerElement.appendChild(implementationCheck);
        }
      } else {
        for (String iso : ir.getCheckedIsos()) {
          if (!ir.hasModifiedIso(iso) && !iso.equals(TiffConformanceChecker.POLICY_ISO)) {
            int errorsCount = ir.getNErrors(iso);
            String title = ImplementationCheckerLoader.getIsoName(iso);
            String resultName = (errorsCount == 0) ? "Passed" : "Failed";
            Element implementationCheck = doc.createElement("implementation_check");
            Element name = doc.createElement("name");
            name.setTextContent(title);
            implementationCheck.appendChild(name);
            Element result = doc.createElement("result");
            result.setTextContent(resultName);
            implementationCheck.appendChild(result);
            implementationCheckerElement.appendChild(implementationCheck);
          }
        }
      }
      report.appendChild(implementationCheckerElement);

      /**
       * Policy checker
       */
      Element policyCheckerElement = doc.createElement("policy_checkers");
      if (!ir.isQuick()){
        for (String iso : ir.getCheckedIsos()) {
          if (!ir.hasModifiedIso(iso)) continue;
          String title = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
          List<RuleResult> errors = ir.getErrorsPolicy(iso);
          List<RuleResult> warnings = ir.getWarningsPolicy(iso);
          Element implementationCheck = doc.createElement("implementation_check");
          Element name = doc.createElement("name");
          name.setTextContent(title);
          implementationCheck.appendChild(name);
          addErrorsWarnings(doc, implementationCheck, errors, warnings, true);
          policyCheckerElement.appendChild(implementationCheck);
        }
        // Policy rules
        Element policyRules = doc.createElement("policy_rules");
        if (rules != null) {
          for (RuleResult rr : ir.getErrors(TiffConformanceChecker.POLICY_ISO)) {
            Element error = doc.createElement("error");
            Element test = doc.createElement("test");
            test.setTextContent(rr.getRule().getDescription().getValue());
            Element message = doc.createElement("message");
            message.setTextContent(rr.getMessage());
            error.appendChild(test);
            error.appendChild(message);
            policyRules.appendChild(error);
          }
          for (RuleResult rr : ir.getWarnings(TiffConformanceChecker.POLICY_ISO)) {
            Element warning = doc.createElement("warning");
            Element test = doc.createElement("test");
            test.setTextContent(rr.getRule().getDescription().getValue());
            Element message = doc.createElement("message");
            message.setTextContent(rr.getMessage());
            warning.appendChild(test);
            warning.appendChild(message);
            policyRules.appendChild(warning);
          }
          policyCheckerElement.appendChild(policyRules);
        }
      } else {
        for (String iso : ir.getCheckedIsos()) {
          if (ir.hasModifiedIso(iso) || iso.equals(TiffConformanceChecker.POLICY_ISO)) {
            int errorsCount = (ir.hasModifiedIso(iso)) ? ir.getNErrorsPolicy(iso) : ir.getNErrors(iso);
            String title = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
            String resultName = (errorsCount == 0) ? "Passed" : "Failed";
            Element implementationCheck = doc.createElement("implementation_check");
            Element name = doc.createElement("name");
            name.setTextContent(title);
            implementationCheck.appendChild(name);
            Element result = doc.createElement("result");
            result.setTextContent(resultName);
            implementationCheck.appendChild(result);
            policyCheckerElement.appendChild(implementationCheck);
          }
        }
      }
      report.appendChild(policyCheckerElement);

      // Total
      if (!ir.isQuick()){
        Element results = doc.createElement("results");
        addErrorsWarnings(doc, results, errorsTotal, warningsTotal, true);
        implementationCheckerElement.appendChild(results);
      }
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
   * @param ir    the individual report.
   * @param rules the policy checker.
   * @return the XML string generated.
   */
  public String parseIndividual(IndividualReport ir, Rules rules) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element report = buildReportIndividual(doc, ir, rules);
      if (report != null) doc.appendChild(report);

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      // To String
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      return writer.getBuffer().toString();
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
    return "";
  }
}
