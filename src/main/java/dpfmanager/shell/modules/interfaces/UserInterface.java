/**
 * <h1>UserInterface.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 22/6/2015
 */

package dpfmanager.shell.modules.interfaces;

import dpfmanager.shell.modules.classes.Field;
import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The Class UserInterface.
 */
public class UserInterface {
  private ArrayList<String> extensions;
  private ArrayList<String> isos;
  private ArrayList<Field> fields;
  private String selectedFile;

  /**
   * Gets selected file.
   *
   * @return the selected file
   */
  public String getSelectedFile() {
    return selectedFile;
  }

  /**
   * Set selected file.
   *
   * @param filename the filename
   */
  public void setSelectedFile(String filename){
    selectedFile = filename;
  }

  /**
   * Gets extensions.
   *
   * @return the extensions
   */
  public ArrayList<String> getExtensions() {
    return extensions;
  }

  /**
   * Gets isos.
   *
   * @return the isos
   */
  public ArrayList<String> getIsos() {
    return isos;
  }

  /**
   * Gets fields.
   *
   * @return the fields
   */
  public ArrayList<Field> getFields() {
    return fields;
  }

  /**
   * Gets fixes.
   *
   * @return the fixes
   */
  public ArrayList<String> getFixes() {
    ArrayList<String> fixes = new ArrayList<String>();
    fixes.add("Remove Tag");
    fixes.add("Add Tag");
    return fixes;
  }

  /**
   * Gets fix fields.
   *
   * @return the fix fields
   */
  public ArrayList<String> getFixFields() {
    ArrayList<String> fields = new ArrayList<String>();
    fields.add("ImageDescription");
    fields.add("Copyright");
    fields.add("Artist");
    return fields;
  }

  /**
   * Load conformance checker.
   */
  public void LoadConformanceChecker() {
    extensions = new ArrayList<String>();
    isos = new ArrayList<String>();
    fields = new ArrayList<Field>();

    String xml = TiffConformanceChecker.getConformanceCheckerOptions();
    Document doc = convertStringToDocument(xml);

    NodeList nodelist = doc.getElementsByTagName("extension");
    for (int i=0;i<nodelist.getLength();i++) {
      Node node = nodelist.item(i);
      extensions.add(node.getFirstChild().getNodeValue());
    }

    nodelist = doc.getElementsByTagName("standard");
    for (int i=0;i<nodelist.getLength();i++) {
      Node node = nodelist.item(i);
      isos.add(node.getFirstChild().getNodeValue());
    }

    nodelist = doc.getElementsByTagName("field");
    for (int i=0;i<nodelist.getLength();i++) {
      Node node = nodelist.item(i);
      NodeList childs = node.getChildNodes();
      Field field = new Field(childs);
      fields.add(field);
    }
  }

  private Document convertStringToDocument(String xmlStr) {
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
}

