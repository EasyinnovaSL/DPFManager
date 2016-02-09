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

package dpfmanager.shell.conformancechecker;

import dpfmanager.shell.conformancechecker.MetadataFixer.autofixes.clearPrivateData;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.nio.ByteOrder;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * The Class TiffConformanceChecker.
 */
public class TiffConformanceChecker {
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

  public static ArrayList<String> getConformanceCheckerExtensions() {
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

  public static ArrayList<String> getConformanceCheckerStandards() {
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

  public static ArrayList<Field> getConformanceCheckerFields() {
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
      System.out.println("Loading autofixes from JAR");
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
        System.out.println("Jar not found");
      }
    } catch (Exception ex) {
      System.out.println("Error " + ex.toString());
    }

    if (classes == null) {
      System.out.println("Loading autofixes through reflection");
      try {
        Reflections reflections = new Reflections("dpfmanager.shell.conformancechecker.MetadataFixer.autofixes", new SubTypesScanner(false));
        Set<Class<? extends Object>> classesSet = reflections.getSubTypesOf(Object.class);

        classes = new ArrayList<String>();
        for (Class<?> cl : classesSet) {
          if (!cl.toString().endsWith(".autofix")) {
            classes.add(cl.toString().substring(cl.toString().lastIndexOf(".") + 1));
          }
        }
      } catch (Exception ex) {
        System.out.println("Exception getting classes");
      }
    }

    if (classes == null) {
      System.out.println("Autofixes loaded manually");
      classes = new ArrayList<String>();
      classes.add(clearPrivateData.class.toString());
    }

    System.out.println("Found " + classes.size() + " classes:");
    for (String cl : classes) {
      System.out.println(cl);
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
      ProcessInput pi = new ProcessInput(getConformanceCheckerExtensions());
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
}

