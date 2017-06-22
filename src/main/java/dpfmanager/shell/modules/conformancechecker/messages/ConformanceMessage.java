/**
 * <h1>ConformanceMessage.java</h1> <p> This program is free software: you can redistribute it
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

import java.util.List;

/**
 * Created by Adria Llorens on 24/03/2016.
 */
public class ConformanceMessage extends DpfMessage {

  private Long uuid;
  private String input;
  private String path;
  private List<String> files;
  private Configuration config;
  private int recursive;
  private boolean askOverwrite;
  private boolean quick;

  private boolean gui = false;
  private boolean console = false;
  private boolean server = false;

  public ConformanceMessage(String i, String p, int r, boolean ao, boolean q) {
    // Gui
    input = i;
    path = p;
    recursive = r;
    gui = true;
    askOverwrite = ao;
    quick = q;
  }

  public ConformanceMessage(Long u, String i, String p) {
    // Server
    input = i;
    path = p;
    uuid = u;
    server = true;
    quick = false;
  }

  public ConformanceMessage(List<String> f, Configuration c) {
    // Console
    files = f;
    config = c;
    input = null;
    path = null;
    console = true;
    quick = false;
  }

  public boolean hasPaths() {
    return input != null && path != null;
  }

  public Long getUuid() {
    return uuid;
  }

  public String getPath() {
    return path;
  }

  public String getInput() {
    return input;
  }

  public List<String> getFiles() {
    return files;
  }

  public Configuration getConfig() {
    return config;
  }

  public int getRecursive() {
    return recursive;
  }

  public boolean askOverwrite() {
    return askOverwrite;
  }

  public void setAskOverwrite(boolean ow){
    askOverwrite = ow;
  }

  public boolean isQuick() {
    return quick;
  }

  public boolean isGui() {
    return gui;
  }

  public boolean isConsole() {
    return console;
  }

  public boolean isServer() {
    return server;
  }
}
