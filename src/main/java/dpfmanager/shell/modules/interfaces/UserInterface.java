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

import dpfmanager.MainApp;
import dpfmanager.shell.modules.classes.Field;
import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.prefs.Preferences;

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

  public static String getVersion() {
    Properties prop = new Properties();
    InputStream input = null;

    try {

      String filename = ".properties";
      input = MainApp.class.getClassLoader().getResourceAsStream(filename);
      if(input==null){
        return "";
      }

      // load a properties file
      prop.load(input);

      // get the property value and print it out
      return prop.getProperty("version");

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return "";
  }

  public static boolean getFeedback() {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String defaultValue = "0";
    String propertyValue = prefs.get(PREF_NAME, defaultValue);
    return propertyValue.equals("1");
  }

  public static void setFeedback(boolean value) {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "feedback";
    String newValue = value ? "1" : "0";
    prefs.put(PREF_NAME, newValue);
  }

  public static boolean getFirstTime() {
    String sfile = UserInterface.getConfigDir() + "/setts.txt";
    File file = new File(sfile);
    try {
      if (file.exists()) {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, "UTF-8");
        if (str.contains("first_time=0"))
          return false;
      }
    } catch (Exception ex) {

    }
    return true;
    //Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    //final String PREF_NAME = "first_time";
    //String defaultValue = "1";
    //String propertyValue = prefs.get(PREF_NAME, defaultValue);
    //return propertyValue.equals("1");
  }

  public static void setFirstTime(boolean value) {
    String sfile = UserInterface.getConfigDir() + "/setts.txt";
    File file = new File(sfile);
    String txt = "first_time=" + (value ? "1" : "0");
    try {
      if (file.exists()) {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, "UTF-8");
        if (str.contains("first_time=1")) str.replace("first_time=1", txt);
        else if (str.contains("first_time=0")) str.replace("first_time=0", txt);
        else txt = str + txt;
        file.delete();
      }
      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      writer.write(txt);
      writer.close();
    } catch (Exception ex) {

    }

    //Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    //final String PREF_NAME = "first_time";
    //String newValue = value ? "1" : "0";
    //prefs.put(PREF_NAME, newValue);
  }

  public static String getDefaultDir() {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "browse_dir";
    String defaultValue = ".";
    String propertyValue = prefs.get(PREF_NAME, defaultValue);
    if (new File(propertyValue).exists() && new File(propertyValue).isDirectory())
      return propertyValue;
    else
      return ".";
  }

  public static void setDefaultDir(String path) {
    Preferences prefs = Preferences.userNodeForPackage(dpfmanager.MainApp.class);
    final String PREF_NAME = "browse_dir";
    prefs.put(PREF_NAME, path);
  }

  public static String getConfigDir() {
    //return ".";
    if (!new File(System.getProperty("user.home") + "/DPF Manager").exists()) {
      new File(System.getProperty("user.home") + "/DPF Manager").mkdir();
    }
    if (!new File(System.getProperty("user.home") + "/DPF Manager").exists()) {
      System.out.println("Could not create user dir. Switching to app dir");
      return ".";
    }
    return System.getProperty("user.home") + "/DPF Manager";
  }
}

