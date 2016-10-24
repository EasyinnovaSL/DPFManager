/**
 * <h1>InteroperabilityMessage.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.interoperability.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.interoperability.core.ConformanceConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
public class InteroperabilityMessage extends DpfMessage {

  public enum Type {
    ADD, EDIT, LIST, INFO, REMOVE, ENABLE, DISABLE, PARAMETERS, CONFIGURE, EXTENSIONS, OBJECTS, GUIEDIT
  }

  private Type type;

  private ConformanceConfig config;

  private String name;

  private String extra;

  private String configure = "";

  private String parameters = "";

  private boolean enabled = true;

  private List<String> extensions = new ArrayList<>();

  public InteroperabilityMessage(Type type) {
    this.type = type;
  }

  public InteroperabilityMessage(Type type, ConformanceConfig config) {
    this.type = type;
    this.config = config;
  }

  public InteroperabilityMessage(Type type, String name) {
    this.type = type;
    this.name = name;
  }

  public InteroperabilityMessage(Type type, String name, String extra) {
    this.type = type;
    this.name = name;
    this.extra = extra;
  }

  public InteroperabilityMessage(Type type, String name, String extra, String parameters, String configure, List<String> extensions, boolean enabled) {
    this.type = type;
    this.name = name;
    this.extra = extra;
    this.parameters = parameters;
    this.configure = configure;
    this.extensions = extensions;
    this.enabled = enabled;
  }

  public Type getType() {
    return type;
  }

  public boolean isAdd(){
    return type.equals(Type.ADD);
  }

  public boolean isEdit(){
    return type.equals(Type.EDIT);
  }

  public boolean isRemove(){
    return type.equals(Type.REMOVE);
  }

  public boolean isInfo(){
    return type.equals(Type.INFO);
  }

  public boolean isList(){
    return type.equals(Type.LIST);
  }

  public boolean isEnable(){
    return type.equals(Type.ENABLE);
  }

  public boolean isDisable(){
    return type.equals(Type.DISABLE);
  }

  public boolean isObjects(){
    return type.equals(Type.OBJECTS);
  }

  public boolean isGuiEdit(){
    return type.equals(Type.GUIEDIT);
  }

  public boolean isConfigure(){
    return type.equals(Type.CONFIGURE);
  }

  public boolean isExtensions(){
    return type.equals(Type.EXTENSIONS);
  }

  public boolean isParameters(){
    return type.equals(Type.PARAMETERS);
  }

  public String getName() {
    return name;
  }

  public String getExtra() {
    return extra;
  }

  public String getConfigure() {
    return configure;
  }

  public void setConfigure(String configure) {
    this.configure = configure;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public List<String> getExtensions() {
    return extensions;
  }

  public void setExtensions(List<String> extensions) {
    this.extensions = extensions;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public ConformanceConfig getConfig() {
    return config;
  }
}
