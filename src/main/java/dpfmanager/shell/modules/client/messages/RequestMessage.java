/**
 * <h1>RequestMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.client.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;

import java.util.List;

/**
 * Created by Adria Llorens on 10/05/2016.
 */
public class RequestMessage extends DpfMessage {

  public enum Type {
    ASK, CHECK
  }

  private Type type;
  private List<String> files;
  private List<String> tmpFiles;
  private Configuration config;
  private String str;

  public RequestMessage(Type t, String i) {
    // ASK
    type = t;
    str = i;
  }

  public RequestMessage(Type t, List<String> f, List<String> tmp, Configuration c) {
    // CHECK
    type = t;
    files = f;
    tmpFiles = tmp;
    config = c;
  }

  public boolean isAsk() {
    return type.equals(Type.ASK);
  }

  public boolean isCheck() {
    return type.equals(Type.CHECK);
  }

  public List<String> getFiles() {
    return files;
  }

  public List<String> getTmpFiles() {
    return tmpFiles;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getString() {
    return str;
  }
}
