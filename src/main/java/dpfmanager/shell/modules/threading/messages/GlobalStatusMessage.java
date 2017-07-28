/**
 * <h1>GlobalStatusMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.threading.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.util.Map;

/**
 * Created by Adria Llorens on 08/04/2016.
 */
public class GlobalStatusMessage extends DpfMessage {

  public enum Type {
    NEW,
    INIT,
    FINISH,
    RELOAD,
    CANCEL
  }

  private Type type;
  private long uuid;
  private int size;
  private Configuration config;
  private String internal;
  private String input;
  private DpfRunnable run;
  private Map<String,String> zipsPaths;

  public GlobalStatusMessage(Type t, Long u, DpfRunnable r, String i) {
    // New
    type = t;
    uuid = u;
    run = r;
    input = i;
  }

  public GlobalStatusMessage(Type t, long u, int n, Configuration c, String i, String ri, Map<String,String> z) {
    // Init
    type = t;
    uuid = u;
    size = n;
    config = c;
    internal = i;
    input = ri;
    zipsPaths = z;
  }

  public GlobalStatusMessage(Type t, long u) {
    // Finish
    type = t;
    uuid = u;
  }

  public GlobalStatusMessage(Type t, long u, String i) {
    // Cancel
    type = t;
    uuid = u;
    internal = i;
  }

  public GlobalStatusMessage(Type t) {
    // Asking for reload
    type = t;
  }

  public boolean isNew() {
    return type.equals(Type.NEW);
  }

  public boolean isInit() {
    return type.equals(Type.INIT);
  }

  public boolean isFinish() {
    return type.equals(Type.FINISH);
  }

  public boolean isCancel() {
    return type.equals(Type.CANCEL);
  }

  public boolean isReload() {
    return type.equals(Type.RELOAD);
  }

  public long getUuid() {
    return uuid;
  }

  public int getSize() {
    return size;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getInternal() {
    return internal;
  }

  public String getInput() {
    return input;
  }

  public DpfRunnable getRunnable() {
    return run;
  }

  public Map<String, String> getZipsPaths() {
    return zipsPaths;
  }
}
