/**
 * <h1>ProcessInputMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.conformancechecker.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Adria Llorens on 09/05/2016.
 */
public class ProcessInputMessage extends DpfMessage {

  private Type type;
  private Long uuid;
  private List<String> files;
  private Configuration config;
  private String internalReportFolder;
  private String inputStr;
  private Integer toWait;
  private Map<String,String> zipsPath;

  public enum Type {
    WAIT, FILE
  }

  public ProcessInputMessage(Type type, Long uuid, List<String> files, Configuration config, String internalReportFolder, String inputStr, Integer toWait){
    // Wait
    this.type = type;
    this.uuid = uuid;
    this.files = files;
    this.config = config;
    this.internalReportFolder = internalReportFolder;
    this.inputStr = inputStr;
    this.toWait = toWait;
  }

  public ProcessInputMessage(Type type, Long uuid, List<String> files, Map<String,String> zipsPath){
    // File
    this.type = type;
    this.uuid = uuid;
    this.files = files;
    this.zipsPath = zipsPath;
  }

  public boolean isWait(){
    return type.equals(Type.WAIT);
  }

  public boolean isFile(){
    return type.equals(Type.FILE);
  }

  public Long getUuid() {
    return uuid;
  }

  public List<String> getFiles() {
    return files;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getInternalReportFolder() {
    return internalReportFolder;
  }

  public String getInputStr() {
    return inputStr;
  }

  public Integer getToWait() {
    return toWait;
  }

  public Map<String, String> getZipsPath() {
    return zipsPath;
  }
}
