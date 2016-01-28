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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    //Properties prop = new Properties();
    //InputStream input = null;

    //try {
      //String ver = UserInterface.class.getPackage().getImplementationVersion();

      //String filename = ".properties";
      //input = MainApp.class.getClassLoader().getResourceAsStream(filename);
      //if(input==null){
      //  return "";
      //}

      // load a properties file
      //prop.load(input);

      // get the property value and print it out
      //return prop.getProperty("version");
    //} catch (IOException ex) {
    //  ex.printStackTrace();
    //} finally {
    //  if (input != null) {
    //    try {
    //      input.close();
    //    } catch (IOException e) {
    //      e.printStackTrace();
    //    }
    //  }
    //}
    return "1.4";
  }

  public static boolean getFeedback() {
    String strVal = getPropertiesValue("feedback", "0");
    return Boolean.valueOf(strVal);
  }

  public static void setFeedback(boolean value) {
    String strVal = String.valueOf(value);
    setPropertiesValue("feedback", strVal);
  }

  public static boolean getFirstTime() {
    String strVal = getPropertiesValue("firstTime", "true");
    return Boolean.valueOf(strVal);
  }

  public static void setFirstTime(boolean value) {
    String strVal = String.valueOf(value);
    setPropertiesValue("firstTime", strVal);
  }

  public static String getDefaultDir() {
    return getPropertiesValue("browse_dir", getConfigDir());
  }

  public static void setDefaultDir(String path) {
    setPropertiesValue("browse_dir", path);
  }

  public static String getPropertiesValue(String key, String def) {
    Properties properties = getPropertiesConfig();
    String value = (String) properties.get(key);
    if (value == null) {
      value = def;
    }
    return value;
  }

  public static void setPropertiesValue(String key, String value) {
    Properties properties = getPropertiesConfig();
    properties.setProperty(key, value);
    setPropertiesConfig(properties);
  }

  public static Properties getPropertiesConfig() {
    Properties properties = null;
    try {
      File configFile = new File(getConfigDir()+"/dpfmanager.properties");
      if (!configFile.exists()) {
        configFile.createNewFile();
      }
      InputStream is = new FileInputStream(configFile);
      properties = new Properties();
      properties.load(is);
      is.close();
    }
    catch(IOException ex){
      ex.printStackTrace();
    }
    return properties;
  }

  public static void setPropertiesConfig(Properties properties){
    try {
      File configFile = new File(getConfigDir()+"/dpfmanager.properties");
      if (configFile.exists()) {
        OutputStream os = new FileOutputStream(configFile);
        properties.store(os, null);
        os.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static String getConfigDir() {
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

