/**
 * <h1>ProcessInputParameters.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.configuration.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adria Llorens on 10/05/2016.
 */
public class ProcessInputParameters {

  private String internalReportFolder;
  private String inputStr;
  private Configuration config;
  private int toWait;
  private List<String> files;
  private Map<String,String> zipsPath;

  public ProcessInputParameters(String internalReportFolder, String inputStr, Configuration config, int toWait, List<String> files) {
    this.internalReportFolder = internalReportFolder;
    this.inputStr = inputStr;
    this.config = config;
    this.toWait = toWait;
    this.files = files;
    this.zipsPath = new HashMap<>();
  }

  public String getInternalReportFolder() {
    return internalReportFolder;
  }

  public String getInputStr() {
    return inputStr;
  }

  public Configuration getConfig() {
    return config;
  }

  public List<String> getFiles() {
    return files;
  }

  public int getToWait() {
    return toWait;
  }

  public void add(List<String> files2){
    files.addAll(files2);
    toWait--;
  }

  public void addZipsPath(Map<String, String> zipsPath2) {
    for (String key : zipsPath2.keySet()) {
      if (!zipsPath.containsKey(key)) {
        zipsPath.put(key, zipsPath2.get(key));
      }
    }
  }

  public Map<String, String> getZipsPath() {
    return zipsPath;
  }

}
