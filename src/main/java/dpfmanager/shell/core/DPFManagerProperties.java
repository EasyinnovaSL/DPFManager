package dpfmanager.shell.core;

import dpfmanager.shell.core.app.MainGuiApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
public class DPFManagerProperties {

  public static String getVersion(){
    String filename = "version.properties";
    InputStream input = MainGuiApp.class.getClassLoader().getResourceAsStream(filename);

    try {
      // load a properties file
      Properties prop = new Properties();
      prop.load(input);
      return prop.getProperty("version");
    }
    catch (Exception e){
      return "";
    }
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
//      System.out.println("Could not create user dir. Switching to app dir"); TODO
      return ".";
    }
    return System.getProperty("user.home") + "/DPF Manager";
  }

  public static String getDataDir() {
    String dataDir = getConfigDir() + "/data";
    File dataFile = new File(dataDir);
    if (!dataFile.exists()){
      dataFile.mkdirs();
    }
    return dataDir;
  }
}
