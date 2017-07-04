/**
 * <h1>ShowMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.core.messages;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.runnable.MakeReportRunnable;

/**
 * Created by Adria Llorens on 04/03/2016.
 */
public class ShowMessage extends DpfMessage {

  public enum Type {
    SHOW, GENERATE, UPDATE, INIT, INDIVIDUAL
  }

  private Type internalType;
  private String type;
  private String path;
  private GlobalReport globalReport;
  private int number;
  private int value;
  private MakeReportRunnable makeReportRunnable;
  private Long uuid;
  private String internal;
  private boolean onlyGlobal;
  private Configuration config;

  // Show
  public ShowMessage(Long u, String t, String p) {
    type = t;
    uuid = u;
    path = p;
    internalType = Type.SHOW;
  }

  // Show
  public ShowMessage(String t, String p) {
    type = t;
    uuid = null;
    path = p;
    internalType = Type.SHOW;
  }

  // Generate
  public ShowMessage(Long u, String t, GlobalReport gr, String i, boolean o) {
    type = t;
    uuid = u;
    internal = i;
    globalReport = gr;
    onlyGlobal = o;
    internalType = Type.GENERATE;
  }

  // Individual
  public ShowMessage(String t, String p, Configuration c) {
    type = t;
    path = p;
    config = c;
    internalType = Type.INDIVIDUAL;
  }

  // Init
  public ShowMessage(Long u, int m, int v, MakeReportRunnable mrr) {
    uuid = u;
    internalType = Type.INIT;
    number = m;
    value = v;
    makeReportRunnable = mrr;
  }

  // Update
  public ShowMessage(Long u, int m) {
    uuid = u;
    internalType = Type.UPDATE;
    number = m;
  }

  public String getPath() {
    return path;
  }

  public String getType() {
    return type;
  }

  public GlobalReport getGlobalReport() {
    return globalReport;
  }

  public int getNumber() {
    return number;
  }

  public int getValue() {
    return value;
  }

  public MakeReportRunnable getMakeReportRunnable() {
    return makeReportRunnable;
  }

  public boolean isGenerate() {
    return internalType.equals(Type.GENERATE);
  }

  public boolean isShow() {
    return internalType.equals(Type.SHOW);
  }

  public boolean isIndividual() {
    return internalType.equals(Type.INDIVIDUAL);
  }

  public boolean isUpdate() {
    return internalType.equals(Type.UPDATE);
  }

  public boolean isInit() {
    return internalType.equals(Type.INIT);
  }

  public Long getUuid() {
    return uuid;
  }

  public String getInternal() {
    return internal;
  }

  public boolean isOnlyGlobal() {
    return onlyGlobal;
  }

  public Configuration getConfig() {
    return config;
  }
}