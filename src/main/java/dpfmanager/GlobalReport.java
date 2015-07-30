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

package dpfmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GlobalReport.
 */
public class GlobalReport {

  /** The list of all individual reports. */
  private List<IndividualReport> reports;

  /** Number of valid EP. */
  private int nReportsOnlyBl;

  /** Number of valid Baseline. */
  private int nReportsOnlyEp;

  /** Number of valid reports. */
  private int nreportsok;

  /** Number of failed reports. */
  private int nreportsko;

  /**
   * Instantiates a new global report.
   */
  public GlobalReport() {
    reports = new ArrayList<IndividualReport>();
    nreportsok = 0;
    nreportsko = 0;
    nReportsOnlyBl = 0;
    nReportsOnlyEp = 0;
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
   *
   */
  public void generate() {
    for (final IndividualReport ir : reports) {
      if (ir.getBaselineErrors().size() == 0 && ir.getEPErrors().size() == 0) {
        nreportsok++;
      } else if (ir.getBaselineErrors().size() > 0 && ir.getEPErrors().size() > 0) {
        nreportsko++;
      } else if (ir.getEPErrors().size() > 0) {
        nReportsOnlyEp++;
      } else {
        nReportsOnlyBl++;
      }
    }
  }

  /**
   * Get the reports count.
   *
   * @return nreportsok + nreportsko
   */
  public int getReportsCount() {
    return nreportsok + nreportsko;
  }

  /**
   * Get the count of correct reports.
   *
   * @return nreportsok
   */
  public int getReportsOk() {
    return nreportsok;
  }

  /**
   * Get the count of reports with some error.
   *
   * @return nreportsko
   */
  public int getReportsKo() {
    return nreportsko;
  }

  /**
   * Get the count of correct reports.
   *
   * @return nReportsOnlyBl
   */
  public int getReportsBl() {
    return nReportsOnlyBl;
  }

  /**
   * Get the count of reports with some error.
   *
   * @return nReportsOnlyEp
   */
  public int getReportsEp() {
    return nReportsOnlyEp;
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
