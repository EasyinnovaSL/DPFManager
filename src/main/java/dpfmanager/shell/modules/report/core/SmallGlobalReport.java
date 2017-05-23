///**
// * <h1>ReportGenerator.java</h1> <p> This program is free software: you can redistribute it and/or
// * modify it under the terms of the GNU General Public License as published by the Free Software
// * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
// * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
// * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
// * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
// * have received a copy of the GNU General Public License and the Mozilla Public License along with
// * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
// * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
// * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
// * 2015 Easy Innova, SL </p>
// *
// * @author Adria Llorens Martinez
// * @version 1.0
// * @since 23/6/2015
// */
//
//package dpfmanager.shell.modules.report.core;
//
//import java.io.FileOutputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
///**
// * The Class GlobalReport.
// */
//public class SmallGlobalReport implements Serializable {
//
//  /**
//   * Do not modify!
//   */
//  private static final long serialVersionUID = 7845L;
//
//  /**
//   * The isos to check
//   */
//  private Long duration;
//
//  /**
//   * Number of reports ok
//   */
//  private Map<String, Integer> nReportsOk;
//
//  /**
//   * Number of reports ok with policy
//   */
//  private Map<String, Integer> nReportsOkPolicy;
//
//  /**
//   * The isos to check
//   */
//  private List<String> isos;
//
//  /**
//   * The isos checked
//   */
//  private List<String> isosChecked;
//
//  /**
//   * List of modified ISOs
//   */
//  private List<String> isosModified;
//
//  /**
//   * Count reports
//   */
//  private int allReportsCount;
//
//  /**
//   * Count reports OK
//   */
//  private int allReportsOk;
//
//  /**
//   * Count of reports with some warning.
//   */
//  private int allReportsWarnings;
//
//  /**
//   * Average Errors Value
//   */
//  private double errVal;
//
//  /**
//   * Instantiates a new global report.
//   */
//  public SmallGlobalReport() {
//    nReportsOk = new HashMap<>();
//    nReportsOkPolicy = new HashMap<>();
//    isos = new ArrayList<>();
//    isosChecked = new ArrayList<>();
//  }
//
//  /**
//   * Gets isos.
//   *
//   * @return the isos
//   */
//  public List<String> getIsos() {
//    return isos;
//  }
//
//  /**
//   * Gets isos.
//   *
//   * @return the isos
//   */
//  public List<String> getCheckedIsos() {
//    return isosChecked;
//  }
//
//
//  /**
//   * Generate the full report information.
//   */
//  public void generate(GlobalReport gr) {
//    allReportsOk = gr.getAllReportsOk();
//    allReportsCount = gr.getReportsCount();
//    allReportsWarnings = gr.getAllReportsWarnings();
//    isosModified = gr.getModifiedIsos();
//    errVal = gr.computeAverageErrors();
//    duration = gr.getDuration();
//  }
//
//  /**
//   * Get the reports count.
//   *
//   * @return nreportsok + nreportsko
//   */
//  public int getReportsCount() {
//    return allReportsCount;
//  }
//
//  /**
//   * Get the count of correct reports.
//   *
//   * @return nreportsok reports ok
//   */
//  public int getAllReportsOk() {
//    return allReportsOk;
//  }
//
//  /**
//   * Get the count of reports with some warning.
//   *
//   * @return nreports warning
//   */
//  public int getAllReportsWarnings() {
//    return allReportsWarnings;
//  }
//
//  /**
//   * Get the count of reports with some error.
//   *
//   * @return nreportsko reports ko
//   */
//  public int getAllReportsKo() {
//    return getReportsCount() - getAllReportsOk();
//  }
//
//  public int getReportsOk(String iso) {
//    return nReportsOk.containsKey(iso) ? nReportsOk.get(iso) : 0;
//  }
//
//  public int getReportsOkPolicy(String iso) {
//    return nReportsOkPolicy.containsKey(iso) ? nReportsOkPolicy.get(iso) : 0;
//  }
//
//  public boolean hasModificationIso(String iso) {
//    return isosModified.contains(iso);
//  }
//
//  public double getAverageErrors() {
//    return errVal;
//  }
//
//  public Long getDurationHours(){
//    return TimeUnit.MILLISECONDS.toHours(duration);
//  }
//
//  public Long getDurationMinutes(){
//    return TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
//  }
//
//  public Long getDurationSeconds(){
//    return TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
//  }
//
//  public Long getDurationMillis(){
//    return TimeUnit.MILLISECONDS.toMillis(duration)- TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));
//  }
//
//  /**
//   * Serialize to file
//   */
//  public void write(String internal){
//    try{
//      FileOutputStream fout = new FileOutputStream(internal+"/summary.ser");
//      ObjectOutputStream oos = new ObjectOutputStream(fout);
//      oos.writeObject(this);
//      oos.close();
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//  }
//}
