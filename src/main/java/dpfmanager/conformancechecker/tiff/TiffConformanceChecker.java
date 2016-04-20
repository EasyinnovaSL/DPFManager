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
import dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes.clearPrivateData;
import dpfmanager.shell.application.launcher.noui.ConsoleLauncher;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.reader.TiffReader;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * The Class TiffConformanceChecker.
 */
public class TiffConformanceChecker extends ConformanceChecker {
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
      addElement(doc, field, "description", "Pixel Density in pixels per centimeter");
      addElement(doc, field, "operators", ">,<,=");
      // Number of images
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "NumberImages");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Number of images");
      addElement(doc, field, "operators", ">,<,=");
      // BitDepth
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "BitDepth");
      addElement(doc, field, "type", "integer");
      addElement(doc, field, "description", "Bit Depth");
      addElement(doc, field, "operators", ">,<,=");
      addElement(doc, field, "values", "1,2,4,8,16,32,64");
      // Compression
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "Compression");
      addElement(doc, field, "type", "string");
      addElement(doc, field, "description", "Compression");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", compressionName(1) + "," + compressionName(2) + "," + compressionName(32773) + "," + compressionName(3) + "," + compressionName(4) + "," + compressionName(5) + "," + compressionName(6) + "," + compressionName(7) + "," + compressionName(8) + "," + compressionName(9) + "," + compressionName(10) + "");
      // Byteorder
      field = doc.createElement("field");
      fields.appendChild(field);
      addElement(doc, field, "name", "ByteOrder");
      addElement(doc, field, "type", "string");
      addElement(doc, field, "description", "Byte Order (BigEndian, LittleEndian)");
      addElement(doc, field, "operators", "=");
      addElement(doc, field, "values", ByteOrder.BIG_ENDIAN.toString() + "," + ByteOrder.LITTLE_ENDIAN.toString());

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      output = writer.getBuffer().toString().replaceAll("\n|\r", "");
    } catch (Exception ex) {
      output = null;
    }
    return output;
  }

  public ArrayList<String> getConformanceCheckerExtensions() {
    String xml = getConformanceCheckerOptions();
    Document doc = convertStringToDocument(xml);

    ArrayList<String> extensions = new ArrayList<String>();
    NodeList nodelist = doc.getElementsByTagName("extension");
    for (int i = 0; i < nodelist.getLength(); i++) {
      Node node = nodelist.item(i);
      extensions.add(node.getFirstChild().getNodeValue());
    }
    return extensions;
  }

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
        Reflections reflections = new Reflections(TiffConformanceChecker.getAutofixesClassPath(), new SubTypesScanner(false));
        Set<Class<? extends Object>> classesSet = reflections.getSubTypesOf(Object.class);

        classes = new ArrayList<String>();
        for (Class<?> cl : classesSet) {
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
    return tiffValidation.writeString();
  }

  public static Validator getBaselineValidation(String content) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = new Validator(Logger);
    validation.validateBaseline(content);
    return validation;
  }

  public static Validator getEPValidation(String content) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = new Validator(Logger);
    validation.validateTiffEP(content);
    return validation;
  }

  public static Validator getITValidation(int profile, String content) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = new Validator(Logger);
    if (profile == 0) validation.validateTiffIT(content);
    else if (profile == 1) validation.validateTiffITP1(content);
    else validation.validateTiffITP2(content);
    return validation;
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
  public IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder, Configuration config) throws ReadTagsIOException, ReadIccConfigIOException {
    try {
//      Logger.println("Reading Tiff file");
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
          Logger.println("Validating Tiff");
          boolean checkBL = config.getIsos().contains("Baseline");
          boolean checkEP = config.getIsos().contains("Tiff/EP");
          boolean checkIT = config.getIsos().contains("Tiff/IT");
          boolean checkIT1 = config.getIsos().contains("Tiff/IT-1");
          boolean checkIT2 = config.getIsos().contains("Tiff/IT-2");
          boolean checkPC = false;
          if (config.getRules() != null) {
            checkPC = config.getRules().getRules().size() > 0;
          }

          Logger.println("Validating Tiff");
          String content = getValidationXmlString(tr);
          Validator baselineVal = null;
          if (checkBL) {
            baselineVal = getBaselineValidation(content);
          }
          Validator epValidation = null;
          if (checkEP) {
            epValidation = getEPValidation(content);
          }
          Validator it0Validation = null;
          Validator it1Validation = null;
          Validator it2Validation = null;
          if (checkIT) {
            it0Validation = getITValidation(0, content);
          }
          if (checkIT1) {
            it1Validation = getITValidation(1, content);
          }
          if (checkIT2) {
            it2Validation = getITValidation(2, content);
          }
          Logger.println("Creating report");

          String pathNorm = reportFilename.replaceAll("\\\\", "/");
          String name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
          IndividualReport ir = new IndividualReport(name, pathToFile, reportFilename, tr.getModel(), baselineVal, epValidation, it0Validation, it1Validation, it2Validation);
          ir.checkBL = checkBL;
          ir.checkEP = checkEP;
          ir.checkIT0 = checkIT;
          ir.checkIT1 = checkIT1;
          ir.checkIT2 = checkIT2;
          ir.checkPC = checkPC;
          Logger.println("Internal report created");

          tr = null;
          //System.gc();
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
    }
    return null;
  }

  /**
   * Checks if is tiff.
   *
   * @param filename the filename
   * @return true, if is tiff
   */
  public boolean acceptsFile(String filename) {
    boolean isTiff = false;
    for (String extension : getConformanceCheckerExtensions()) {
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
      String[] args = new String[4];
      args[0] = "-configuration";
      args[1] = config;
      args[2] = "-s";
      args[3] = input;

      // Run console app
      MainConsoleApp.main(args);

      // Wait for finish (timeout 60s)
      int timeout = 0;
      while (!ConsoleLauncher.isFinished() && timeout < 60) {
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

