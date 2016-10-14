/**
 * <h1>AlertMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import javafx.scene.control.Alert;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 23/03/2016.
 */
public class AlertMessage extends DpfMessage {

  public enum Type{
    ALERT,
    INFO,
    WARNING,
    ERROR,
    CONFIRMATION
  }

  private Type type;
  private String title;
  private String header;
  private String content;
  private Boolean result = null;
  private DpfMessage next;
  private String target;

  public AlertMessage(Type t, String h){
    type = t;
    title = getDefaultTitle(type);
    header = h;
    content = null;
    next = null;
  }

  public AlertMessage(Type t, String h, DpfMessage n, String ta){
    type = t;
    title = getDefaultTitle(type);
    header = h;
    content = null;
    next = n;
    target = ta;
  }

  public AlertMessage(Type t, String h, String c){
    type = t;
    title = getDefaultTitle(type);
    header = h;
    content = c;
    next = null;
  }

  public void setTitle(String t){
    title = t;
  }

  public void setResult(boolean bol){
    result = bol;
  }

  public boolean getResult(){
    return result;
  }

  public boolean hasResult(){
    return result != null;
  }

  private String getDefaultTitle(Type type){
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.language");
    if (type.equals(Type.ERROR)){
      return bundle.getString("error");
    } else if (type.equals(Type.WARNING)){
      return bundle.getString("warning");
    } else if (type.equals(Type.INFO)){
      return bundle.getString("help");
    } else if (type.equals(Type.ALERT)){
      return bundle.getString("alert");
    }
    return "";
  }

  public Type getType(){
    return type;
  }

  public String getTitle(){
    return title;
  }

  public String getHeader(){
    return header;
  }

  public String getContent(){
    return content;
  }

  public DpfMessage getNext() {
    return next;
  }

  public String getTarget() {
    return target;
  }
}
