/**
 * <h1>ReportGenerator.java</h1>
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Adrià Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.report.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class GlobalReport.
 */
public class GlobalReport {

  /**
   * The list of all individual reports.
   */
  private List<SmallIndividualReport> reports;

  /**
   * Number of reports ok
   */
  private Map<String, Integer> nReportsOk;

  /**
   * The isos to check
   */
  private List<String> isos;

  /**
   * The isos checked
   */
  private List<String> isosChecked;

  private double errVal;

  /**
   * Instantiates a new global report.
   */
  public GlobalReport() {
    reports = new ArrayList<>();
    nReportsOk = new HashMap<>();
    isos = new ArrayList<>();
    isosChecked = new ArrayList<>();
    errVal = 0;
  }

  /**
   * Gets isos.
   *
   * @return the isos
   */
  public List<String> getIsos() {
    return isos;
  }

  /**
   * Gets isos.
   *
   * @return the isos
   */
  public List<String> getCheckedIsos() {
    return isosChecked;
  }


  /**
   * Add an individual report.
   *
   * @param ir the individual report.
   */
  public void addIndividual(IndividualReport ir) {
    SmallIndividualReport small = new SmallIndividualReport(ir);
    reports.add(small);
  }

  /**
   * Generate the full report information.
   */
  public void generate() {
    List<SmallIndividualReport> toDelete = new ArrayList<>();
    Collections.sort(reports);
    for (SmallIndividualReport ir : reports) {
      if (ir.isError()){
        toDelete.add(ir);
      } else {
        for (String iso : ir.getCheckedIsos()){
          if (ir.hasValidation(iso)){
            if (ir.getNErrors(iso) == 0){
              if (nReportsOk.containsKey(iso)){
                nReportsOk.put(iso, nReportsOk.get(iso)+1);
              } else {
                nReportsOk.put(iso, 1);
              }
            }
            if (!isos.contains(iso)) isos.add(iso);
          }
          if (!isosChecked.contains(iso)) isosChecked.add(iso);
        }
      }
    }
    // Delete reports with error
    reports.removeAll(toDelete);
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
      for (String iso : ir.getIsosCheck()) {
        int size = ir.hasModifiedIso(iso) ? ir.getNErrorsPolicy(iso) : ir.getNErrors(iso);
        if (ir.hasValidation(iso) && size > 0) {
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
      for (String iso : rep.getIsosCheck()) {
        if (rep.getNWarnings(iso) > 0) {
          n++;
          break;
        }
      }
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
    int n = 0;
    for (SmallIndividualReport ir : reports) {
      int size = ir.hasModifiedIso(iso) ? ir.getNErrorsPolicy(iso) : ir.getNErrors(iso);
      if (size == 0){
        n++;
      }
    }
    return n;
  }

  public boolean hasModificationIso(String iso) {
    for (SmallIndividualReport ir : reports) {
      return ir.hasModifiedIso(iso);
    }
    return false;
  }

  /**
   * Get the list of the individual reports.
   *
   * @return the individual reports
   */
  public List<SmallIndividualReport> getIndividualReports() {
    return reports;
  }

  public double computeAverageErrors(){
    if (errVal != 0) return errVal;
    int maxErrs = 0;
    for (SmallIndividualReport ir : getIndividualReports()) {
      int currentErrs = ir.getAllNErrorsPolicy();
      if (currentErrs > maxErrs) {
        maxErrs = currentErrs;
      }
    }
    if (maxErrs == 0){
      errVal = 12.5; // Default
    } else {
      errVal = 100.0 / maxErrs;
    }
    return errVal;
  }

}
