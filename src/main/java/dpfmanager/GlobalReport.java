package dpfmanager;

import java.util.ArrayList;
import java.util.List;

public class GlobalReport {

  /** The list of all individual reports. */
  private List<IndividualReport> reports;

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
      if (ir.getErrors().size() > 0) {
        nreportsko++;
      } else {
        nreportsok++;
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
   * Get the list of the individual reports.
   *
   */
  public List<IndividualReport> getIndividualReports() {
    return reports;
  }
  
}
