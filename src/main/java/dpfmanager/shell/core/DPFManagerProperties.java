package dpfmanager.shell.core;

import dpfmanager.shell.core.app.MainGuiApp;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
public class DPFManagerProperties {

  private static boolean finished = false;

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

  public static int getDatabaseVersion() {
    String strVal = getPropertiesValue("database", "0");
    return Integer.parseInt(strVal);
  }

  public static void setDatabaseVersion(int value) {
    String strVal = String.valueOf(value);
    setPropertiesValue("database", strVal);
  }

  public static boolean getFirstTime() {
    String strVal = getPropertiesValue("firstTime", "true");
    return Boolean.valueOf(strVal);
  }

  public static void setFirstTime(boolean value) {
    String strVal = String.valueOf(value);
    setPropertiesValue("firstTime", strVal);
  }

  public static String getDefaultDirFile() {
    String dir = getPropertiesValue("browse_dir_file", getConfigDir());
    if (!new File(dir).exists()) {
      return getConfigDir();
    }
    return dir;
  }

  public static void setDefaultDirFile(String path) {
    setPropertiesValue("browse_dir_file", path);
  }

  public static String getDefaultDirConfig() {
    String dir = getPropertiesValue("browse_dir_config", getConfigDir());
    if (!new File(dir).exists()) {
      return getConfigDir();
    }
    return dir;
  }

  public static void setDefaultDirConfig(String path) {
    setPropertiesValue("browse_dir_config", path);
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
      File configFileNew = new File(getDataDir()+"/dpfmanager.properties");
      // Config file
      if (!configFileNew.exists()){
        // No new file
        if (!configFile.exists()) {
          // No old file, create it
          configFileNew.createNewFile();
        } else {
          // Move old to new
          FileUtils.moveFile(configFile, configFileNew);
        }
      }

      InputStream is = new FileInputStream(configFileNew);
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
      File configFile = new File(getDataDir()+"/dpfmanager.properties");
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

  public static String getReportsDir() {
    String dir = getConfigDir() + "/reports";
    File file = new File(dir);
    if (!file.exists()){
      file.mkdirs();
    }
    return dir;
  }

  public static String getDataDir() {
    String dataDir = getConfigDir() + "/data";
    File dataFile = new File(dataDir);
    if (!dataFile.exists()){
      dataFile.mkdirs();
    }
    return dataDir;
  }

  public static String getServerDir() {
    String dataDir = getConfigDir() + "/server";
    File dataFile = new File(dataDir);
    if (!dataFile.exists()){
      dataFile.mkdirs();
    }
    return dataDir;
  }

  public static String getLanguage() {
    return getPropertiesValue("language", Locale.ENGLISH.toString());
  }

  public static void setLanguage(String lang) {
    setPropertiesValue("language", lang);
  }

  public static ResourceBundle getBundle(){
    return ResourceBundle.getBundle("bundles.language");
  }

  /**
   * Finish control for test
   */
  public static boolean isFinished() {
    return finished;
  }

  public static void setFinished(boolean f) {
    finished = f;
  }
}
