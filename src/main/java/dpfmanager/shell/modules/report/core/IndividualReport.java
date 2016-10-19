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
 * @author Adrià Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.report.core;

import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;

import com.easyinnova.tiff.model.TiffDocument;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class IndividualReport.
 */
public class IndividualReport implements Comparable {

  /**
   * The file name.
   */
  private String filename;

  /**
   * The file path.
   */
  private String filepath;

  /**
   * The real file name.
   */
  private String reportFilename;

  /**
   * The file path.
   */
  private String reportpath;

  /**
   * The errors list.
   */
  private Map<String, List<RuleResult>> errors;

  /**
   * The warnings list.
   */
  private Map<String, List<RuleResult>> warnings;

  /**
   * The isos to check.
   */
  private List<String> isosCheck;

  /**
   * The Tiff Document object.
   */
  private TiffDocument tiffModel;

  private IndividualReport compareIr;

  private String document;

  private PDDocument pdf;

  private boolean containsData;

  private String conformanceCheckerReport = null;

  private String conformanceCheckerReportHtml = null;

  private String conformanceCheckerReportMets = null;

  /**
   * Extra check information
   */
  private String internalReportFodler;

  private int idReport;

  private long uuid;

  private boolean error;

  /**
   * Error constructor
   */
  public IndividualReport(boolean e) {
    error = e;
  }

  /**
   * Constructor + generate.
   *
   * @param name           the name
   * @param path           the path
   * @param reportFilename the path
   */
  public IndividualReport(String name, String path, String reportFilename) {
    filename = name;
    filepath = path;
    this.reportFilename = reportFilename;
    containsData = false;
    errors = new HashMap<>();
    warnings = new HashMap<>();
  }

  /**
   * Constructor + generate.
   *
   * @param name      the name
   * @param path      the path
   * @param tiffModel the TIFF model
   */
  public IndividualReport(String name, String path, String rFilename, TiffDocument tiffModel, Map<String, Validator> validators) {
    filename = name;
    filepath = path;
    containsData = true;
    reportFilename = rFilename;
    errors = new HashMap<>();
    warnings = new HashMap<>();
    generate(tiffModel, validators);
  }

  public boolean isError() {
    return error;
  }

  public void setInternalReportFolder(String internal) {
    internalReportFodler = internal;
  }

  public String getInternalReportFodler() {
    return internalReportFodler;
  }

  public int getIdReport() {
    return idReport;
  }

  public void setIdReport(int idReport) {
    this.idReport = idReport;
  }

  public long getUuid() {
    return uuid;
  }

  public void setUuid(long uuid) {
    this.uuid = uuid;
  }

  public void setIsosCheck(List<String> isosCheck) {
    this.isosCheck = isosCheck;
  }

  public List<String> getIsosCheck() {
    return isosCheck;
  }

  public void setConformanceCheckerReport(String report) {
    conformanceCheckerReport = report;
  }

  public void setConformanceCheckerReportHtml(String report) {
    conformanceCheckerReportHtml = report;
  }

  public void setConformanceCheckerReportMets(String report) {
    conformanceCheckerReportMets = report;
  }

  public String getConformanceCheckerReport() {
    return conformanceCheckerReport;
  }

  public String getConformanceCheckerReportHtml() {
    return conformanceCheckerReportHtml;
  }

  public String getConformanceCheckerReportMets() {
    return conformanceCheckerReportMets;
  }

  public boolean containsData() {
    return containsData;
  }

  /**
   * Sets pc validation.
   *
   * @param validation the validation
   */
  public void addValidation(String key, ArrayList<RuleResult> validation) {
    List<RuleResult> errorsPc = new ArrayList<>();
    List<RuleResult> warningsPc = new ArrayList<>();
    for (RuleResult rr : validation) if (!rr.getWarning()) errorsPc.add(rr);
    for (RuleResult rr : validation) if (rr.getWarning()) warningsPc.add(rr);
    errors.put(key, errorsPc);
    warnings.put(key, warningsPc);
  }

  /**
   * Sets report path.
   *
   * @param path the path
   */
  public void setReportPath(String path) {
    reportpath = path;
  }

  /**
   * Gets report path.
   *
   * @return the report path
   */
  public String getReportPath() {
    return reportpath;
  }

  /**
   * Set file name.
   *
   * @param name the new file name
   */
  public void setFileName(String name) {
    filename = name;
  }

  /**
   * Get report file name.
   *
   * @return reportFilename
   */
  public String getReportFileName() {
    return reportFilename;
  }

  /**
   * Get file name.
   *
   * @return filename file name
   */
  public String getFileName() {
    return filename;
  }

  /**
   * Sets compare report.
   *
   * @param ir the ir
   */
  public void setCompareReport(IndividualReport ir) {
    compareIr = ir;
  }

  /**
   * Gets compare report.
   *
   * @return the compare report
   */
  public IndividualReport getCompareReport() {
    return compareIr;
  }

  /**
   * Sets pdf document.
   *
   * @param document the document
   */
  public void setPDFDocument(String document) {
    this.document = document;
  }

  public void setPDF(PDDocument doc) {
    this.pdf = doc;
  }

  public PDDocument getPDF() {
    return pdf;
  }

  /**
   * Gets pdf document.
   *
   * @return the pdf document
   */
  public String getPDFDocument() {
    return document;
  }

  /**
   * Get file path.
   *
   * @return filepath file path
   */
  public String getFilePath() {
    return filepath;
  }

  /**
   * Set file path.
   *
   * @param path the new file name
   */
  public void setFilePath(String path) {
    filepath = path;
  }

  /**
   * Calculate percent.
   *
   * @return the int
   */
  public int calculatePercent() {
    Double rest = 100.0;
    IndividualReport ir = this;

    if (isosCheck != null) {
      for (String key : isosCheck) {
        rest -= ir.getErrors(key).size() * 12.5;
      }
    }

    if (rest < 0.0) {
      rest = 0.0;
    }
    return rest.intValue();
  }

  /**
   * Generate the report information.
   *
   * @param tiffModel   the tiff model
   * @param validations the validations
   */
  public void generate(TiffDocument tiffModel, Map<String, Validator> validations) {
    this.tiffModel = tiffModel;

    for (String key : validations.keySet()) {
      errors.put(key, validations.get(key).getErrors());
      warnings.put(key, validations.get(key).getWarningsAndInfos());
    }
  }

  /**
   * Has pc validation boolean.
   *
   * @return the boolean
   */
  public boolean hasValidation(String key) {
    return errors.containsKey(key);
  }

  /**
   * Get warnings list.
   *
   * @return the errors
   */
  public List<RuleResult> getWarnings(String key) {
    if (!warnings.containsKey(key)) {
      return new ArrayList<>();
    }
    return warnings.get(key);
  }

  /**
   * Get errors list.
   *
   * @return the errors
   */
  public List<RuleResult> getErrors(String key) {
    if (!errors.containsKey(key)) {
      return new ArrayList<>();
    }
    return errors.get(key);
  }

  /**
   * Gets warnings count.
   *
   * @return the warnings count
   */
  public int getNWarnings(String key) {
    int n = 0;
    for (RuleResult rule :getWarnings(key)){
      if (rule.getWarning()) n++;
    }
    return n;
  }

  /**
   * Gets errors count.
   *
   * @return the errors count
   */
  public int getNErrors(String key) {
    return getErrors(key).size();
  }

  public List<RuleResult> getAllErrors() {
    List<RuleResult> allErrors = new ArrayList<>();
    for (String key : errors.keySet()){
      if (getIsosCheck().contains(key)){
        allErrors.addAll(errors.get(key));
      }
    }
    return allErrors;
  }

  public List<RuleResult> getAllWarnings() {
    List<RuleResult> allWarnings = new ArrayList<>();
    for (String key : warnings.keySet()){
      if (getIsosCheck().contains(key)){
        allWarnings.addAll(warnings.get(key));
      }
    }
    return allWarnings;
  }

  /**
   * Get Tiff Model.
   *
   * @return the tiffModel
   */
  public TiffDocument getTiffModel() {
    return tiffModel;
  }

  /**
   * Sets tiff model.
   *
   * @param model the model
   */
  public void setTiffModel(TiffDocument model) {
    tiffModel = model;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof IndividualReport) {
      IndividualReport other = (IndividualReport) o;
      Integer thisPercent = calculatePercent();
      Integer otherPercent = other.calculatePercent();
      return thisPercent.compareTo(otherPercent);
    } else {
      return -1;
    }
  }
}
