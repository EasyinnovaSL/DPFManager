package dpfmanager.shell.core.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Adrià Llorens on 05/04/2016.
 */
public class VersionUtil {

  // Write version into build files
  public static void main(String[] args) {
    String version = args[0];
    String baseDir = args[1];

    String issPath = baseDir + "/package/windows/DPF Manager.iss";
    String rpmPath = baseDir + "/package/linux/DPFManager.spec";
    String propOutput = baseDir + "/target/classes/version.properties";

    // Windows iss
    try {
      // Windows iss
      File issFile = new File(issPath);
      String issContent = FileUtils.readFileToString(issFile);
      String newIssContent = replaceLine( StringUtils.split(issContent, '\n'),"AppVersion=",version);
      if (!newIssContent.isEmpty()){
        FileUtils.writeStringToFile(issFile, newIssContent);
        System.out.println("New version information updated! (iss)");
      }

      // RPM spec
      File rpmFile = new File(rpmPath);
      String rpmContent = FileUtils.readFileToString(rpmFile);
      String newRpmContent = replaceLine(StringUtils.split(rpmContent, '\n'),"Version: ",version);
      if (!newRpmContent.isEmpty()){
        FileUtils.writeStringToFile(rpmFile, newRpmContent);
        System.out.println("New version information updated! (spec)");
      }

      // Java properties file
      OutputStream output = new FileOutputStream(propOutput);
      Properties prop = new Properties();
      prop.setProperty("version", version);
      prop.store(output,"Version autoupdated");
      output.close();
      System.out.println("New version information updated! (properties)");

    }
    catch(Exception e){
      System.out.println("Exception ocurred, no version changed.");
      e.printStackTrace();
    }

  }

  private static String replaceLine(String[] lines, String search, String version){
    int i=0;
    while (i<lines.length){
      String line = lines[i];
      if (line.contains(search)){
        if (line.contains(version)){
          System.out.println("No need to change version.");
          return "";
        }
        lines[i] = search+version;
        return StringUtils.join(lines,'\n');
      }
      i++;
    }
    return "";
  }

}
