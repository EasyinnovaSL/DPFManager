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

package dpfmanager.shell.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GlobalReport.
 */
public class GlobalReport {

  /** The list of all individual reports. */
  private List<IndividualReport> reports;

  /**
   * Number of EP ok
   */
  int nreportsEpOk;

  /**
   * Number of Baseline ok
   */
  int nreportsBlOk;

  /**
   * Number of IT ok
   */
  int nreportsItOk;

  /**
   * Number of PC ok
   */
  int nreportsPcOk;

  /**
   * The Has ep.
   */
  boolean hasEp;
  /**
   * The Has it.
   */
  boolean hasIt;
  /**
   * The Has bl.
   */
  boolean hasBl;

  /**
   * Instantiates a new global report.
   */
  public GlobalReport() {
    reports = new ArrayList<IndividualReport>();
    nreportsEpOk = 0;
    nreportsItOk = 0;
    nreportsBlOk = 0;
    nreportsPcOk = 0;
    hasEp = false;
    hasIt = false;
    hasBl = false;
  }

  /**
   * Gets has ep.
   *
   * @return the has ep
   */
  public boolean getHasEp() {
    return hasEp;
  }

  /**
   * Gets has it.
   *
   * @return the has it
   */
  public boolean getHasIt() {
    return hasIt;
  }

  /**
   * Gets has bl.
   *
   * @return the has bl
   */
  public boolean getHasBl() {
    return hasBl;
  }

  /**
   * Add an individual report.
   *
   * @param ir the individual report.
   */
  public void addIndividual(IndividualReport ir) {
    reports.add(ir);
  }

  /**
   * Generate the full report information.
   */
  public void generate() {
    for (IndividualReport ir : reports) {
      if (ir.hasEpValidation()) {
        if (ir.getEPErrors().size()==0) nreportsEpOk++;
        hasEp = true;
      }
      if (ir.hasItValidation()) {
        if (ir.getITErrors().size()==0) nreportsItOk++;
        hasIt = true;
      }
      if (ir.hasBlValidation()) {
        if (ir.getBaselineErrors().size()==0) nreportsBlOk++;
        hasBl = true;
      }
      if (ir.getPCErrors().size() == 0) nreportsPcOk++;
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
  public int getReportsOk() {
    int n=0;
    for (IndividualReport ir : reports) {
      boolean ok = true;
      if (ir.hasEpValidation()) {
        if (ir.getEPErrors().size() > 0) ok = false;
      }
      if (ir.hasItValidation()) {
        if (ir.getITErrors().size() > 0) ok = false;
      }
      if (ir.hasBlValidation()) {
        if (ir.getBaselineErrors().size() > 0) ok = false;
      }
      if (ok) n++;
    }
    return n;
  }

  /**
   * Get the count of reports with some error.
   *
   * @return nreportsko reports ko
   */
  public int getReportsKo() {
    return getReportsCount() - getReportsOk();
  }

  /**
   * Has bl boolean.
   *
   * @return the boolean
   */
  public boolean hasBl() {
    return hasBl;
  }

  /**
   * Has ep boolean.
   *
   * @return the boolean
   */
  public boolean hasEp() {
    return hasEp;
  }

  /**
   * Has it boolean.
   *
   * @return the boolean
   */
  public boolean hasIt() {
    return hasIt;
  }

  /**
   * Get the count of correct reports.
   *
   * @return nReportsOnlyBl reports bl
   */
  public int getReportsBl() {
    return nreportsBlOk;
  }

  /**
   * Get the count of reports with some error.
   *
   * @return nReportsOnlyEp reports ep
   */
  public int getReportsEp() {
    return nreportsEpOk;
  }

  /**
   * Get the count of reports with some error.
   *
   * @return nReportsOnlyIt reports it
   */
  public int getReportsIt() {
    return nreportsItOk;
  }

  /**
   * Get the count of reports with some error.
   *
   * @return nReportsOnlyIt reports pc
   */
  public int getReportsPc() {
    return nreportsPcOk;
  }

  /**
   * Get the list of the individual reports.
   *
   * @return the individual reports
   */
  public List<IndividualReport> getIndividualReports() {
    return reports;
  }

}
