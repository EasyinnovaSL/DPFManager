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

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The Class GlobalReport.
 */
public class GlobalReport extends ReportSerializable {

  /**
   * Do not modify!
   */
  private static final long serialVersionUID = 7845L;

  /**
   * The isos to check
   */
  private Long duration;

  /**
   * The list of all individual reports.
   */
  private List<SmallIndividualReport> reports;

  /**
   * Number of reports ok
   */
  private Map<String, Integer> nReportsOk;

  /**
   * Number of reports ok with policy
   */
  private Map<String, Integer> nReportsOkPolicy;

  /**
   * The isos checked
   */
  private List<String> checkedIsos;

  /**
   * The isos selected by configuration
   */
  private List<String> selectedIsos;

  /**
   * The invalidated rules for isos
   */
  private Map<String, ArrayList<String>> modifiedIsos;

  /**
   * The average error value
   */
  private double errVal = 12.5;

  /**
   * The configuration
   */
  private Configuration config;

  /**
   * The input string
   */
  private String inputStr;

  /**
   * The paths where each zip was unzipped
   */
  private Map<String, String> zipsPaths;

  /**
   * Instantiates a new global report.
   */
  public GlobalReport() {
    reports = new ArrayList<>();
    nReportsOk = new HashMap<>();
    nReportsOkPolicy = new HashMap<>();
    selectedIsos = new ArrayList<>();
    checkedIsos = new ArrayList<>();
    errVal = 0;
  }

  public void init(Configuration c, List<String> ci, Map<String, String> z) {
    this.modifiedIsos = c.getModifiedIsos();
    this.selectedIsos = c.getIsos();
    if (c.hasRules()) {
      selectedIsos.add(TiffConformanceChecker.POLICY_ISO);
    }
    this.checkedIsos = ci;
    this.config = c;
    this.zipsPaths = z;
    if (zipsPaths == null) {
      zipsPaths = new HashMap<>();
    }
  }

  /**
   * Gets isos.
   *
   * @return the isos
   */
  public List<String> getCheckedIsos() {
    return checkedIsos;
  }

  /**
   * Gets checked isos.
   *
   * @return the isos
   */
  public List<String> getSelectedIsos() {
    return selectedIsos;
  }

  public boolean hasValidation(String key) {
    return selectedIsos.contains(key);
  }

  public Map<String, ArrayList<String>> getModifiedIsos() {
    return modifiedIsos;
  }

  public boolean hasModifiedIso(String iso) {
    return modifiedIsos.containsKey(iso);
  }

  /**
   * Add an individual report.
   *
   * @param ir the individual report.
   */
  public void addIndividual(SmallIndividualReport ir) {
    reports.add(ir);
  }

  /**
   * Generate the full report information.
   */
  public void generate(Date start) {
    duration = (new Date()).getTime() - start.getTime();
    List<SmallIndividualReport> toDelete = new ArrayList<>();
    Collections.sort(reports);
    for (SmallIndividualReport ir : reports) {
      if (inputStr == null) inputStr = ir.getInputStr();
      if (ir.isError()) {
        toDelete.add(ir);
      } else {
        for (String iso : getCheckedIsos()) {
          if (ir.getNErrors(iso) == 0) {
            if (nReportsOk.containsKey(iso)) {
              nReportsOk.put(iso, nReportsOk.get(iso) + 1);
            } else {
              nReportsOk.put(iso, 1);
            }
          }
          if (ir.getNErrorsPolicy(iso) == 0) {
            if (nReportsOkPolicy.containsKey(iso)) {
              nReportsOkPolicy.put(iso, nReportsOkPolicy.get(iso) + 1);
            } else {
              nReportsOkPolicy.put(iso, 1);
            }
          }
        }
      }
    }
    // Delete reports with error
    reports.removeAll(toDelete);
    // Error value and percent
    errVal = computeAverageErrors();
    for (SmallIndividualReport ir : reports) {
      ir.computePercent(this);
    }
  }

  /**
   * Get the reports count.
   *
   * @return nreportsok + nreportsko
   */
  public int getReportsCount() {
    return reports.size();
  }

  /**
   * Get the count of correct reports.
   *
   * @return nreportsok reports ok
   */
  public int getAllReportsOk() {
    int n = 0;
    for (SmallIndividualReport ir : reports) {
      boolean ok = true;
      for (String iso : getSelectedIsos()) {
        int size = hasModifiedIso(iso) ? ir.getNErrorsPolicy(iso) : ir.getNErrors(iso);
        if (hasValidation(iso) && size > 0) {
          ok = false;
        }
      }
      if (ok) n++;
    }
    return n;
  }

  /**
   * Get the count of reports with some warning.
   *
   * @return nreports warning
   */
  public int getAllReportsWarnings() {
    int n = 0;
    for (SmallIndividualReport rep : reports) {
      boolean warn = false;
      for (String iso : getSelectedIsos()) {
        if (rep.getNErrors(iso) == 0){
          if (rep.getNWarnings(iso) > 0) {
            warn = true;
          }
        } else {
          warn = false;
          break;
        }
      }
      if (warn) n++;
    }
    return n;
  }

  /**
   * Get the count of reports with some error.
   *
   * @return nreportsko reports ko
   */
  public int getAllReportsKo() {
    return getReportsCount() - getAllReportsOk();
  }

  public int getReportsOk(String iso) {
    return nReportsOk.containsKey(iso) ? nReportsOk.get(iso) : 0;
  }

  public int getReportsOkPolicy(String iso) {
    return nReportsOkPolicy.containsKey(iso) ? nReportsOkPolicy.get(iso) : 0;
  }

  public boolean hasModificationIso(String iso) {
    return hasModifiedIso(iso);
  }

  /**
   * Get the list of the individual reports.
   *
   * @return the individual reports
   */
  public List<SmallIndividualReport> getIndividualReports() {
    return reports;
  }

  public double computeAverageErrors() {
    int maxErrs = 0;
    for (SmallIndividualReport ir : getIndividualReports()) {
      int currentErrs = ir.getAllNErrorsPolicy();
      if (currentErrs > maxErrs) {
        maxErrs = currentErrs;
      }
    }
    if (maxErrs == 0) {
      errVal = 12.5; // Default
    } else {
      errVal = 100.0 / maxErrs;
    }
    return errVal;
  }

  public double getErrorValue(){
    return errVal;
  }

  public String prettyPrintDuration() {
    return String.format("%02d:%02d:%02d.%02d",
        getDurationHours(),
        getDurationMinutes(),
        getDurationSeconds(),
        getDurationMillis()
    );
  }

  public Long getDurationHours() {
    return TimeUnit.MILLISECONDS.toHours(duration);
  }

  public Long getDurationMinutes() {
    return TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
  }

  public Long getDurationSeconds() {
    return TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
  }

  public Long getDurationMillis() {
    return TimeUnit.MILLISECONDS.toMillis(duration) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));
  }

  public String getInputString(){
    if (inputStr != null && !inputStr.isEmpty()) return inputStr;
    String name = "";
    int index = 0;
    Iterator<SmallIndividualReport> it = reports.iterator();
    while (it.hasNext() && index < 10){
      SmallIndividualReport individual = it.next();
      name = (name.length() > 0) ? name + ", " + individual.getFileName() : individual.getFileName();
    }
    inputStr = name;
    return inputStr;
  }

  public Map<String, String> getZipsPaths() {
    return zipsPaths;
  }

  public Configuration getConfig() {
    return config;
  }
}
