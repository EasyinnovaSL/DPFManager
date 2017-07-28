/**
 * <h1>ReportGenerator.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.report.core;

/**
 * The Class GlobalReport.
 */
public class SmallGlobalReport extends ReportSerializable {

  /**
   * Do not modify!
   */
  private static final long serialVersionUID = 7845L;

  /**
   * The path to global report serialized file
   */
  private String serGlobalReportPath;

  private String input;
  private boolean quick;
  private int nFiles;
  private int nErrors;
  private int nWarnings;
  private int nPassed;


  /**
   * Instantiates a new small global report.
   */
  public SmallGlobalReport(GlobalReport gr, String path) {
    this.serGlobalReportPath = path;
    this.input = gr.getInputString();
    this.quick = gr.getConfig().isQuick();
    this.nFiles = gr.getReportsCount();
    this.nErrors = gr.getAllReportsKo();
    this.nWarnings = gr.getAllReportsWarnings();
    this.nPassed = gr.getAllReportsOk();
  }

  /**
   * Getters
   */

  public String getSerGlobalReportPath() {
    return serGlobalReportPath;
  }

  public String getInput() {
    return input;
  }

  public boolean isQuick() {
    return quick;
  }

  public int getnFiles() {
    return nFiles;
  }

  public int getnErrors() {
    return nErrors;
  }

  public int getnWarnings() {
    return nWarnings;
  }

  public int getnPassed() {
    return nPassed;
  }
}
