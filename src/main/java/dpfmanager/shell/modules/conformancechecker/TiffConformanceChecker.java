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

package dpfmanager.shell.modules.conformancechecker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

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

      Element implementationChecker = doc.createElement("implementationCheckerOptions");
      conformenceCheckerElement.appendChild(implementationChecker);
      Element standards = doc.createElement("standards");
      implementationChecker.appendChild(standards);
      Element standard = doc.createElement("standard");
      standards.appendChild(standard);
      addElement(doc, standard, "name", "TIFF");
      addElement(doc, standard, "description", "TIFF Baseline 6.0");
      standard = doc.createElement("standard");
      standards.appendChild(standard);
      addElement(doc, standard, "name", "TIFF/EP");
      addElement(doc, standard, "description", "TIFF extension for Electronic Photography");

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

  /**
   * Adds a element to the XML object.
   *
   * @param doc the Document (XML object)
   * @param conformenceCheckerElement the Main element in the XML object
   * @param name the element name
   * @param content the content of the element
   */
  static void addElement(Document doc, Element conformenceCheckerElement, String name,
      String content) {
    Element element = doc.createElement(name);
    element.setTextContent(content);
    conformenceCheckerElement.appendChild(element);
  }
}

