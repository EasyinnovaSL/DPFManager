/**
 * <h1>TiffConformanceChecker.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.conformancechecker.tiff.implementation_checker.TiffImplementationChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.conformancechecker.tiff.metadata_fixer.Fix;
import dpfmanager.conformancechecker.tiff.metadata_fixer.Fixes;
import dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes.autofix;
import dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes.clearPrivateData;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;
import dpfmanager.conformancechecker.tiff.reporting.HtmlReport;
import dpfmanager.conformancechecker.tiff.reporting.MetsReport;
import dpfmanager.conformancechecker.tiff.reporting.PdfReport;
import dpfmanager.conformancechecker.tiff.reporting.XmlReport;
import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.interoperability.core.ConformanceConfig;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import com.google.common.reflect.ClassPath;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IccProfile;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.apache.logging.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;
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
 * The Class TiffConformanceChecker.
 */
public class TiffConformanceChecker extends ConformanceChecker {

  private Configuration checkConfig;

  public TiffConformanceChecker(ConformanceConfig config, Configuration checkConfig){
    this.checkConfig = checkConfig;
    setConfig(config);
  }

  /**
   * Gets the conformance checker options.
   *
   * @return the conformance checker options
   */
  public static String getConformanceCheckerOptions() {
    String output;
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element conformenceCheckerElement = doc.createElement("conformanceCheckerOptions");
      doc.appendChild(conformenceCheckerElement);

      addElement(doc, conformenceCheckerElement, "name", "TIFF conformance checker");
      addElement(doc, conformenceCheckerElement, "author", "Víctor Muñoz Solà");
      addElement(doc, conformenceCheckerElement, "version", "0.1");
      addElement(doc, conformenceCheckerElement, "company", "Easy Innova");
      addElement(doc, conformenceCheckerElement, "media_type", "image/tiff");

      Element extensions = doc.createElement("extensions");
      conformenceCheckerElement.appendChild(extensions);
      addElement(doc, extensions, "extension", "tiff");
      addElement(doc, extensions, "extension", "tif");

      Element magics = doc.createElement("magicNumbers");
      conformenceCheckerElement.appendChild(magics);

      Element magicNumber = doc.createElement("magicNumber");
      magics.appendChild(magicNumber);
      addElement(doc, magicNumber, "offset", "0");
      addElement(doc, magicNumber, "signature", "\\x49\\x49\\x2A\\x00");
      magicNumber = doc.createElement("magicNumber");
      magics.appendChild(magicNumber);
      addElement(doc, magicNumber, "offset", "0");
      addElement(doc, magicNumber, "signature", "\\x4D\\x4D\\x00\\x2A");

      // ISOS
      Element implementationChecker = doc.createElement("implementationCheckerOptions");
      conformenceCheckerElement.appendChild(implementationChecker);
      Element standards = doc.createElement("standards");
      implementationChecker.appendChild(standards);

      // Baseline 6
      Element standard = doc.createElement("standard");
      standards.appendChild(standard);
      addElement(doc, standard, "name", "TIFF");
      addElement(doc, standard, "description", "TIFF Baseline 6.0");
      // Tiff/EP
      standard = doc.createElement("standard");
      standards.appendChild(standard);
      addElement(doc, standard, "name", "TIFF/EP");
      addElement(doc, standard, "description", "TIFF extension for Electronic Photography");
      // Tiff/IT
      standard = doc.createElement("standard");
      standards.appendChild(standard);
      addElement(doc, standard, "name", "TIFF/IT");
      addElement(doc, standard, "description", "TIFF extension for Image Technology");

      // Policy checker
      Element policyChecker = doc.createElement("policyCheckerOptions");
      conformenceCheckerElement.appendChild(policyChecker);
      Element fields = doc.createElement("fields");
      policyChecker.appendChild(fields);
      // Image Width
      Element field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "ImageWidth");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Image Width in pixels");
      addElement(doc, field, "operators", ">,<,=");
      // Image Height
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "ImageHeight");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Image Height in pixels");
      addElement(doc, field, "operators", ">,<,=");
      // Pixel Density
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "PixelDensity");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Pixels per centimeter");
      addElement(doc, field, "operators", ">,<,=");
      // Number of images
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "NumberImages");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Number of images in the TIFF");
      addElement(doc, field, "operators", ">,<,=");
      // BitDepth
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "BitDepth");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Number of bits per pixel component");
      addElement(doc, field, "operators", ">,<,=");
      addElement(doc, field, "values", "1,2,4,8,16,32,64");
      // DPI
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "DPI");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Dots per Inch");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", "Even,Uneven");
      // Extra Channels
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "ExtraChannels");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Extra pixel components");
      addElement(doc, field, "operators", ">,<,=");
      // XY Resolution
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "EqualXYResolution");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "XResolution equal to YResolution");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", "False,True");
      // BlankPage
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "BlankPage");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Page devoid of content (completely white)");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", "False,True");
      // NumberBlankPage
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "NumberBlankImages");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Number of Blank Pages");
      addElement(doc, field, "operators", ">,<,=");
      // Compression
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "Compression");
      addElement(doc, field, "type", "string");
      addElement(doc, field, "description", "Compression scheme");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", compressionName(1) + "," + compressionName(2) + "," + compressionName(32773) + "," + compressionName(3) + "," + compressionName(4) + "," + compressionName(5) + "," + compressionName(6) + "," + compressionName(7) + "," + compressionName(8) + "," + compressionName(9) + "," + compressionName(10) + "");
      // Photometric
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "Photometric");
      addElement(doc, field, "type", "string");
      addElement(doc, field, "description", "Color space of the image data");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", photometricName(1) + "," + photometricName(2) + "," + photometricName(3) + "," + photometricName(4) + "," + photometricName(5) + "," + photometricName(6) + "," + photometricName(10) + "");
      // Planar
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "Planar");
      addElement(doc, field, "type", "string");
      addElement(doc, field, "description", "How the pixels components are stored");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", planarName(1) + "," + planarName(2));
      // Byteorder
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "ByteOrder");
      addElement(doc, field, "type", "string");
      addElement(doc, field, "description", "Byte Order (BigEndian, LittleEndian)");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", ByteOrder.BIG_ENDIAN.toString() + "," + ByteOrder.LITTLE_ENDIAN.toString());
      // IccProfileClass
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "IccProfileClass");
      addElement(doc, field, "type", "string");
      addElement(doc, field, "description", "Class of the device ICC Profile");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", IccProfile.ProfileClass.Abstract + "," + IccProfile.ProfileClass.Input + "," + IccProfile.ProfileClass.Display + "," + IccProfile.ProfileClass.Output + "," + IccProfile.ProfileClass.DeviceLink + "," + IccProfile.ProfileClass.ColorSpace + "," + IccProfile.ProfileClass.NamedColor + "," + IccProfile.ProfileClass.Unknown);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      output = writer.getBuffer().toString().replaceAll("\n|\r", "");
    } catch (Exception ex) {
      ex.printStackTrace();
      output = null;
    }
    return output;
  }

//  public ArrayList<String> getConformanceCheckerExtensions() {
//    String xml = getConformanceCheckerOptions();
//    Document doc = convertStringToDocument(xml);
//
//    ArrayList<String> extensions = new ArrayList<String>();
//    NodeList nodelist = doc.getElementsByTagName("extension");
//    for (int i = 0; i < nodelist.getLength(); i++) {
//      Node node = nodelist.item(i);
//      extensions.add(node.getFirstChild().getNodeValue());
//    }
//    return extensions;
//  }

  public ArrayList<String> getConformanceCheckerStandards() {
    String xml = getConformanceCheckerOptions();
    Document doc = convertStringToDocument(xml);

    ArrayList<String> standards = new ArrayList<String>();
    NodeList nodelist = doc.getElementsByTagName("standard");
    for (int i = 0; i < nodelist.getLength(); i++) {
      Node node = nodelist.item(i);
      standards.add(node.getFirstChild().getNodeValue());
    }
    return standards;
  }

  public ArrayList<Field> getConformanceCheckerFields() {
    String xml = getConformanceCheckerOptions();
    Document doc = convertStringToDocument(xml);

    ArrayList<Field> fields = new ArrayList<Field>();
    NodeList nodelist = doc.getElementsByTagName("field");
    for (int i = 0; i < nodelist.getLength(); i++) {
      Node node = nodelist.item(i);
      NodeList childs = node.getChildNodes();
      Field field = new Field(childs);
      fields.add(field);
    }
    return fields;
  }

  public static String compressionName(int code) {
    switch (code) {
      case 1: return "None";
      case 2: return "CCITT";
      case 3: return "CCITT GR3";
      case 4: return "CCITT GR4";
      case 5: return "LZW";
      case 6: return "OJPEG";
      case 7: return "JPEG";
      case 8: return "DEFLATE Adobe";
      case 9: return "JBIG BW";
      case 10: return "JBIG C";
      case 32773: return "PackBits";
    }
    return "Unknown";
  }

  public static String photometricName(int code) {
    switch (code) {
      case 0:
      case 1: return "Bilevel";
      case 2: return "RGB";
      case 3: return "Palette";
      case 4: return "Transparency Mask";
      case 5: return "CMYK";
      case 6: return "YCbCr";
      case 8:
      case 9:
      case 10: return "CIELAB";
    }
    return "Unknown";
  }

  public static String planarName(int code) {
    switch (code) {
      case 1: return "Chunky";
      case 2: return "Planar";
    }
    return "Unknown";
  }

  private static Document convertStringToDocument(String xmlStr) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
      return doc;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getAutofixesClassPath() {
    return "dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes";
  }

  /**
   * Adds a element to the XML object.
   *
   * @param doc                       the Document (XML object)
   * @param conformenceCheckerElement the Main element in the XML object
   * @param name                      the element name
   * @param content                   the content of the element
   */
  static void addElement(Document doc, Element conformenceCheckerElement, String name,
                         String content) {
    Element element = doc.createElement(name);
    element.setTextContent(content);
    conformenceCheckerElement.appendChild(element);
  }

  /**
   * Gets autofixes.
   *
   * @return the autofixes
   */
  public static ArrayList<String> getAutofixes() {
    ArrayList<String> classes = null;

    try {
      Logger.println("Loading autofixes from JAR");
      String path = "DPF Manager-jfx.jar";
      if (new File(path).exists()) {
        ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
          if (!entry.isDirectory() && entry.getName().endsWith(".class") && entry.getName().contains("autofixes") && !entry.getName().contains("autofix.class")) {
            if (classes == null) {
              classes = new ArrayList<String>();
            }
            classes.add(entry.getName().substring(entry.getName().lastIndexOf("/") + 1).replace(".class", ""));
          }
        }
      } else {
        Logger.println("Jar not found");
      }
    } catch (Exception ex) {
      Logger.println("Error " + ex.toString());
    }

    if (classes == null) {
      Logger.println("Loading autofixes through reflection");
      try {
        //Reflections reflections = new Reflections(TiffConformanceChecker.getAutofixesClassPath(), new SubTypesScanner(false));
        //Set<Class<? extends Object>> classesSet = reflections.getSubTypesOf(Object.class);

        Class cls = Class.forName("dpfmanager.conformancechecker.tiff.TiffConformanceChecker");
        ClassLoader cll = cls.getClassLoader();
        Set<ClassPath.ClassInfo> classesInPackage = ClassPath.from(cll).getTopLevelClassesRecursive(TiffConformanceChecker.getAutofixesClassPath());

        classes = new ArrayList<String>();
        for (ClassPath.ClassInfo cl : classesInPackage) {
          if (!cl.toString().endsWith(".autofix")) {
            classes.add(cl.toString().substring(cl.toString().lastIndexOf(".") + 1));
          }
        }
      } catch (Exception ex) {
        Logger.println("Exception getting classes");
      }
    }

    if (classes == null) {
      Logger.println("Autofixes loaded manually");
      classes = new ArrayList<String>();
      classes.add(clearPrivateData.class.toString());
    }

    Logger.println("Found " + classes.size() + " classes:");
    for (String cl : classes) {
      Logger.println(cl);
    }

    return classes;
  }

  public static String getValidationXmlString(TiffReader tr) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    return tiffValidation.getXml();
  }

  public static Validator getBaselineValidation(String content) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = new Validator(Logger);
    validation.validateBaseline(content);
    return validation;
  }

  public static Validator getEPValidation(String content, boolean fastBreak) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = new Validator(Logger);
    validation.validateTiffEP(content, fastBreak);
    return validation;
  }

  public static Validator getITValidation(int profile, String content, boolean fastBreak) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = new Validator(Logger);
    if (profile == 0) validation.validateTiffIT(content, fastBreak);
    else if (profile == 1) validation.validateTiffITP1(content, fastBreak);
    else validation.validateTiffITP2(content, fastBreak);
    return validation;
  }

  private String addXmlReportToPremisSection (String xmlReport, String metsReport){

      //FIXME revisar el procediment, cal afegir el contingut del report xml com un node al mets report
    //FIXME el node del mets és el premis:eventOutcomeDetail, aquí dins s'ha de colocar el report
//    try {
//      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
//      domFactory.setNamespaceAware(true);
//      DocumentBuilder builder = domFactory.newDocumentBuilder();
//      Document docMets = builder.parse(new InputSource(new StringReader(metsReport)));
//      Document docXML = builder.parse(new InputSource(new StringReader(xmlReport)));
//
//      Element outcomeDetail = (Element)docMets.getElementsByTagName("premis:eventOutcomeDetail").item(0);
//      outcomeDetail.appendChild(docXML);
//      //http://stackoverflow.com/questions/4613140/xml-to-append-xml-document-into-the-node-of-another-document
//    } catch (ParserConfigurationException e) {
//      e.printStackTrace();
//    } catch (SAXException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }


    return "";
  }

  /**
   * Process tiff file.
   *
   * @param pathToFile     the path in local disk to the file
   * @param reportFilename the file name that will be displayed in the report
   * @return the individual report
   * @throws ReadTagsIOException      the read tags io exception
   * @throws ReadIccConfigIOException the read icc config io exception
   */
  public IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder, Configuration config, int id) throws ReadTagsIOException, ReadIccConfigIOException {
    try {
//      Logger.println("Reading Tiff file");
      if (config == null){
        config = getDefaultConfiguration();
      }
      TiffReader tr = new TiffReader();
      int result = tr.readFile(pathToFile);
      switch (result) {
        case -1:
          Logger.println("File '" + pathToFile + "' does not exist");
          break;
        case -2:
          Logger.println("IO Exception in file '" + pathToFile + "'");
          break;
        case 0:
          //Logger.println("Validating Tiff");
          boolean checkBL = config.getIsos().contains("Baseline");
          boolean checkEP = config.getIsos().contains("Tiff/EP");
          boolean checkIT = config.getIsos().contains("Tiff/IT");
          boolean checkIT1 = config.getIsos().contains("Tiff/IT-1");
          boolean checkIT2 = config.getIsos().contains("Tiff/IT-2");
          boolean checkPC = false;
          if (config.getRules() != null) {
            checkPC = config.getRules().getRules().size() > 0;
          }

          //Logger.println("Validating Tiff");
          String content = getValidationXmlString(tr);
          Validator baselineVal = null;
          //if (checkBL)
            baselineVal = getBaselineValidation(content);
          Validator epValidation = null;
          //if (checkEP)
            epValidation = getEPValidation(content, !checkEP);
          Validator it0Validation = null;
          Validator it1Validation = null;
          Validator it2Validation = null;
          //if (checkIT)
            it0Validation = getITValidation(0, content, !checkIT);
          //if (checkIT1)
            it1Validation = getITValidation(1, content, !checkIT1);
          //if (checkIT2)
            it2Validation = getITValidation(2, content, !checkIT2);
          //Logger.println("Creating report");

          String pathNorm = reportFilename.replaceAll("\\\\", "/");
          String name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
          IndividualReport ir = new IndividualReport(name, pathToFile, reportFilename, tr.getModel(), baselineVal, epValidation, it0Validation, it1Validation, it2Validation);
          ir.checkBL = checkBL;
          ir.checkEP = checkEP;
          ir.checkIT0 = checkIT;
          ir.checkIT1 = checkIT1;
          ir.checkIT2 = checkIT2;
          ir.checkPC = checkPC;
          //Logger.println("Internal report created");

          tr = null;
          //System.gc();

          Rules rules = config.getRules();
          XmlReport xmlReport = new XmlReport();
          String output = xmlReport.parseIndividual(ir, config.getRules());
          ir.setConformanceCheckerReport(output);
          if (config.getRules() != null && config.getRules().getRules() != null && config.getRules().getRules().size() > 0) {
            ir.setPcValidation(getPcValidation(output));
          }

          //Mets report
          MetsReport metsReport = new MetsReport();
          String xmlOutput = output;
          output = metsReport.parseIndividual(ir, config);
          addXmlReportToPremisSection(xmlOutput,output);
          ir.setConformanceCheckerReportMets(output);

          Fixes fixes = config.getFixes();
          if (config.getFormats().contains("HTML")) {
            int htmlMode = 0;
            if (fixes != null && fixes.getFixes().size() > 0) htmlMode = 1;
            HtmlReport htmlReport = new HtmlReport();
            output = htmlReport.parseIndividual(ir, htmlMode, id);
            ir.setConformanceCheckerReportHtml(output);
          }

          if (config.getFormats().contains("PDF")) {
            PdfReport pdfReport = new PdfReport();
            pdfReport.parseIndividual(ir);
          }

          if (fixes != null && fixes.getFixes().size() > 0) {
            TiffDocument td = ir.getTiffModel();
            String nameOriginalTif = ir.getFilePath();

            tr = new TiffReader();
            tr.readFile(nameOriginalTif);
            ir.setTiffModel(tr.getModel());

            for (Fix fix : fixes.getFixes()) {
              if (fix.getOperator() != null) {
                if (fix.getOperator().equals("Add Tag")) {
                  td.addTag(fix.getTag(), fix.getValue());
                } else if (fix.getOperator().equals("Remove Tag")) {
                  td.removeTag(fix.getTag());
                }
              } else {
                String className = fix.getTag();
                autofix autofix = (autofix) Class.forName(TiffConformanceChecker.getAutofixesClassPath() + "." + className).newInstance();
                autofix.run(td);
              }
            }

            String outputFolder = config.getOutput();
            if (outputFolder == null) outputFolder = internalReportFolder;
            File dir = new File(outputFolder + "/fixed/");
            if (!dir.exists()) dir.mkdir();
            String pathFixed = outputFolder + "/fixed/" + new File(reportFilename).getName();
            if (new File(Paths.get(pathFixed).toString()).exists()) new File(Paths.get(pathFixed).toString()).delete();

            TiffInputStream ti = new TiffInputStream(new File(nameOriginalTif));
            TiffWriter tw = new TiffWriter(ti);
            tw.SetModel(td);
            tw.write(pathFixed);
            ti.close();

            tr = new TiffReader();
            tr.readFile(pathFixed);
            TiffDocument to = tr.getModel();

            String contentfixed = TiffConformanceChecker.getValidationXmlString(tr);
            Validator baselineValfixed = null;
            //if (ir.checkBL)
              baselineValfixed = TiffConformanceChecker.getBaselineValidation(contentfixed);
            Validator epValidationfixed = null;
            //if (ir.checkEP)
              epValidationfixed = TiffConformanceChecker.getEPValidation(contentfixed, !ir.checkEP);
            Validator it0Validationfixed = null;
            //if (ir.checkIT0)
              it0Validationfixed = TiffConformanceChecker.getITValidation(0, contentfixed, !ir.checkIT0);
            Validator it1Validationfixed = null;
            //if (ir.checkIT1)
              it1Validationfixed = TiffConformanceChecker.getITValidation(1, contentfixed, !ir.checkIT1);
            Validator it2Validationfixed = null;
            //if (ir.checkIT2)
              it2Validationfixed = TiffConformanceChecker.getITValidation(2, contentfixed, !ir.checkIT2);

            pathNorm = pathFixed.replaceAll("\\\\", "/");
            name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
            IndividualReport ir2 = new IndividualReport(name, pathFixed, pathFixed, to, baselineValfixed, epValidationfixed, it0Validationfixed, it1Validationfixed, it2Validationfixed);
            int ind = reportFilename.lastIndexOf(".tif");
            ir2.setReportPath(reportFilename.substring(0, ind) + "_fixed.tif");
            ir2.checkPC = ir.checkPC;
            ir2.checkBL = ir.checkBL;
            ir2.checkEP = ir.checkEP;
            ir2.checkIT0 = ir.checkIT0;
            ir2.checkIT1 = ir.checkIT1;
            ir2.checkIT2 = ir.checkIT2;

            ir2.setFilePath(pathFixed);
            //context.sendConsole(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Fixed file " + pathFixed + " created"));
            ir2.setFileName(new File(nameOriginalTif).getName() + " Fixed");

            //Make report
            output = xmlReport.parseIndividual(ir2, rules);
            ir2.setConformanceCheckerReport(output);
            ir2.setPcValidation(getPcValidation(output));
            ir.setCompareReport(ir2);
            ir2.setCompareReport(ir);

            //Make due report in METS
            //FIXME cal revisar xq aixo dóna null
            MetsReport metsReportFixed = new MetsReport();
            output = metsReport.parseIndividual(ir2, config);
            ir2.setConformanceCheckerReportMets(output);

            if (config.getFormats().contains("HTML")) {
              HtmlReport htmlReport = new HtmlReport();
              output = htmlReport.parseIndividual(ir2, 2, id);
              ir2.setConformanceCheckerReportHtml(output);
            }

            if (config.getFormats().contains("PDF")) {
              PdfReport pdfReport = new PdfReport();
              pdfReport.parseIndividual(ir2);
            }
          }

          return ir;
        default:
          Logger.println("Unknown result (" + result + ") in file '" + pathToFile + "'");
          break;
      }
    } catch (ReadTagsIOException e) {
      Logger.println("Error loading Tiff library dependencies (tags)");
    } catch (ReadIccConfigIOException e) {
      Logger.println("Error loading Tiff library dependencies (icc)");
    } catch (OutOfMemoryError error) {
      Logger.println("Out of memory");
    } catch (ParserConfigurationException e) {
      Logger.println("Error in Tiff file (1)");
    } catch (IOException e) {
      Logger.println("Error in Tiff file (2)");
    } catch (SAXException e) {
      Logger.println("Error in Tiff file (3)");
    } catch (JAXBException e) {
      Logger.println("Error in Tiff file (4)");
    } catch (ClassNotFoundException e) {
      Logger.println("Error in Fix");
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Configuration getDefaultConfiguration(){
    return checkConfig;
  }

  /**
   * Gets pc validation.
   *
   * @param output the output
   * @return the pc validation
   */
  static ArrayList<RuleResult> getPcValidation(String output) {
    ArrayList<RuleResult> valid = new ArrayList<>();
    int index = output.indexOf("<svrl:failed-assert");
    while (index > -1) {
      String text = output.substring(output.indexOf("text>", index));
      text = text.substring(text.indexOf(">") + 1);
      text = text.substring(0, text.indexOf("</"));
      index = output.indexOf("<svrl:failed-assert", index + 1);
      RuleResult val = new RuleResult();
      val.setWarning(false);
      val.setMessage(text);
      val.setLocation("Policy checker");
      valid.add(val);
    }
    index = output.indexOf("<svrl:successful-report");
    while (index > -1) {
      String text = output.substring(output.indexOf("text>", index));
      text = text.substring(text.indexOf(">") + 1);
      text = text.substring(0, text.indexOf("</"));
      index = output.indexOf("<svrl:successful-report", index + 1);
      RuleResult val = new RuleResult();
      val.setWarning(true);
      val.setMessage(text);
      val.setLocation("Policy checker");
      valid.add(val);
    }
    return valid;
  }

  /**
   * Checks if is tiff.
   *
   * @param filename the filename
   * @return true, if is tiff
   */
  public boolean acceptsFile(String filename) {
    boolean isTiff = false;
    for (String extension : getConfig().getExtensions()) {
      if (filename.toLowerCase().endsWith(extension.toLowerCase())) {
        isTiff = true;
      }
    }
    return isTiff;
  }

  /**
   * Executes the tiff conformance checker and returns an XML string with the result. A feedback
   * report is transparently sent to the DPF Manager development team (only for testing purposes).
   *
   * @param input the path to the TIFF file
   */
  public static String RunTiffConformanceCheckerAndSendReport(String input) {
    String report_xml = null;
    try {
      // Get tmp output folder and config file
      String config = "config.dpf";

      // Create config file
      File configFile = new File(config);
      if (configFile.exists()) {
        configFile.delete();
      }
      PrintWriter bw = new PrintWriter(configFile);
      bw.write("ISO\tBaseline\n" +
          "ISO\tTiff/EP\n" +
          "ISO\tTiff/IT\n" +
          "FORMAT\tXML\n");
      bw.close();

      // Console parameters
      String[] args = new String[5];
      args[0] = "check";
      args[1] = "--configuration";
      args[2] = config;
      args[3] = "-s";
      args[4] = input;

      // Run console app
      MainConsoleApp.main(args);

      // Wait for finish (timeout 60s)
      int timeout = 0;
      while (!DPFManagerProperties.isFinished() && timeout < 60) {
        Thread.sleep(1000);
        timeout++;
      }

      // Get the xml summary report
      String lastReport = ReportGenerator.getLastReportPath();
      String summary_report = lastReport + "summary.xml";
      if (new File(summary_report).exists()) {
        byte[] encoded = Files.readAllBytes(Paths.get(summary_report));
        report_xml = new String(encoded);
      }

      // Delete temp files
      configFile.delete();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return report_xml;
  }

  @Override
  public String toString() {
    return "TiffConformanceChecker";
  }
}

