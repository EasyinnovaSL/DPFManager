/**
 * <h1>TiffConformanceChecker.java</h1> 
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at
 * <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 *
 */

package dpfmanager.conformancechecker.tiff;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.shell.modules.conformancechecker.core.ProcessInput;
import dpfmanager.conformancechecker.tiff.implementation_checker.TiffImplementationChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes.clearPrivateData;
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
import java.io.StringReader;
import java.nio.ByteOrder;

import java.io.StringWriter;
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
    for (int i=0;i<nodelist.getLength();i++) {
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
    for (int i=0;i<nodelist.getLength();i++) {
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
    for (int i=0;i<nodelist.getLength();i++) {
      Node node = nodelist.item(i);
      NodeList childs = node.getChildNodes();
      Field field = new Field(childs);
      fields.add(field);
    }
    return fields;
  }

  private static Document convertStringToDocument(String xmlStr) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try
    {
      builder = factory.newDocumentBuilder();
      Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
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

  /**
   * Executes the tiff conformance checker and returns an XML string with the result.
   * A feedback report is transparently sent to the DPF Manager development team (only for testing purposes).
   *
   * @param path the path to the TIFF file
   */
  public static String RunTiffConformanceCheckerAndSendReport(String path) {
    String report_xml = null;
    try {
      ProcessInput pi = new ProcessInput();
      ArrayList<String> files = new ArrayList<String>();
      files.add(path);
      Configuration config = new Configuration();
      config = new Configuration(null, null, new ArrayList<>());
      config.getFormats().add("XML");
      config.getIsos().add("Baseline");
      config.getIsos().add("Tiff/EP");
      config.getIsos().add("Tiff/IT");
      String reportFolder = pi.ProcessFiles(files, config, false);
      String summary_report = reportFolder + "/summary.xml";
      try {
        if (new File(summary_report).exists()) {
          byte[] encoded = Files.readAllBytes(Paths.get(summary_report));
          report_xml = new String(encoded);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return report_xml;
  }

  public static Validator getBaselineValidation(TiffReader tr) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = null;
    String baseDir = ReportGenerator.getReportsFolder();
    String xml = baseDir + "/file.xml";
    int idx = 1;
    while (new File(xml).exists()) {
      xml = baseDir + "/file" + idx + ".xml";
      idx++;
    }
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    tiffValidation.writeXml(xml);

    validation = new Validator();
    validation.validateBaseline(xml);

    if (new File(xml).exists()) {
      new File(xml).delete();
    }
    return validation;
  }

  public static Validator getEPValidation(TiffReader tr) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = null;
    String baseDir = ReportGenerator.getReportsFolder();
    String xml = baseDir + "/file.xml";
    int idx = 1;
    while (new File(xml).exists()) {
      xml = baseDir + "/file" + idx + ".xml";
      idx++;
    }
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    tiffValidation.writeXml(xml);

    validation = new Validator();
    validation.validateTiffEP(xml);

    if (new File(xml).exists()) {
      new File(xml).delete();
    }
    return validation;
  }

  public static Validator getITValidation(int profile, TiffReader tr) throws ParserConfigurationException, IOException, SAXException, JAXBException {
    Validator validation = null;
    String baseDir = ReportGenerator.getReportsFolder();
    String xml = baseDir + "/file.xml";
    int idx = 1;
    while (new File(xml).exists()) {
      xml = baseDir + "/file" + idx + ".xml";
      idx++;
    }
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    tiffValidation.writeXml(xml);

    validation = new Validator();
    if (profile == 0) validation.validateTiffIT(xml);
    else if (profile == 1) validation.validateTiffITP1(xml);
    else validation.validateTiffITP2(xml);

    if (new File(xml).exists()) {
      new File(xml).delete();
    }
    return validation;
  }

  /**
   * Process tiff file.
   *
   * @param pathToFile the path in local disk to the file
   * @param reportFilename the file name that will be displayed in the report
   * @param internalReportFolder the internal report folder
   * @return the individual report
   * @throws ReadTagsIOException the read tags io exception
   * @throws ReadIccConfigIOException the read icc config io exception
   */
  public IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder, Configuration config,
                                          int idReport) throws ReadTagsIOException, ReadIccConfigIOException {
    try {
      Logger.println("Reading Tiff file");
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
          boolean checkBL = config.getIsos().contains("Baseline");
          boolean checkEP = config.getIsos().contains("Tiff/EP");
          boolean checkIT = config.getIsos().contains("Tiff/IT");
          boolean checkIT1 = config.getIsos().contains("Tiff/IT-1");
          boolean checkIT2 = config.getIsos().contains("Tiff/IT-2");
          boolean checkPC = false;
          if (config.getRules() != null){
            checkPC = config.getRules().getRules().size() > 0;
          }

          Logger.println("Validating Tiff");
          Validator baselineVal = null;
          if (checkBL) {
            baselineVal = getBaselineValidation(tr);
          }
          Validator epValidation = null;
          if (checkEP) {
            epValidation = getEPValidation(tr);
          }
          Validator it0Validation = null;
          Validator it1Validation = null;
          Validator it2Validation = null;
          if (checkIT) {
            it0Validation = getITValidation(0, tr);
          }
          if (checkIT1) {
            it1Validation = getITValidation(1, tr);
          }
          if (checkIT2) {
            it2Validation = getITValidation(2, tr);
          }

          Logger.println("Creating report");
          String pathNorm = reportFilename.replaceAll("\\\\", "/");
          String name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
          IndividualReport ir = new IndividualReport(name, pathToFile, tr.getModel(), baselineVal, epValidation, it0Validation, it1Validation, it2Validation);
          ir.checkBL = checkBL;
          ir.checkEP = checkEP;
          ir.checkIT0 = checkIT;
          ir.checkIT1 = checkIT1;
          ir.checkIT2 = checkIT2;
          ir.checkPC = checkPC;

          // Generate individual report
          String outputfile = ReportGenerator.getReportName(internalReportFolder, reportFilename, idReport);
          ReportGenerator.generateIndividualReport(outputfile, ir, config);
          Logger.println("Internal report '" + outputfile + "' created");

          tr=null;
          System.gc();
          return ir;
        default:
          Logger.println("Unknown result (" + result + ") in file '" + pathToFile + "'");
          break;
      }
    } catch (ReadTagsIOException e) {
      Logger.println("Error loading Tiff library dependencies (tags)");
    } catch (ReadIccConfigIOException e) {
      Logger.println("Error loading Tiff library dependencies (icc)");
    } catch (OutOfMemoryError error){
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

  @Override
  public String toString() {
    return "TiffConformanceChecker";
  }
}

