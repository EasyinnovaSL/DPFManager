/**
 * <h1>VersionUtil.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.core.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by Adria Llorens on 05/04/2016.
 */
public class VersionUtil {

  // Write version into build files
  public static void main(String[] args) {
    String version = args[0];
    String baseDir = args[1];

    String issPath = baseDir + "/package/windows/DPF Manager.iss";
    String rpmPath = baseDir + "/package/linux/DPFManager.old.spec";
    String propOutput = baseDir + "/target/classes/version.properties";

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
