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

import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class IndividualReport.
 */
public class IndividualReport {

  /** The file name. */
  private String filename;

  /** The file path. */
  private String filepath;

  /** The real file name. */
  private String reportFilename;

  /** The file path. */
  private String reportpath;

  /** The ifdNode count. */
  private int ifdCount;

  /** The isimg list. */
  private List<Boolean> listIsimg;

  /** The hasSubIfd list. */
  private List<Boolean> listHasSubIfd;

  /** The Tiff width. */
  private String width;

  /** The Tiff height. */
  private String height;

  /** The Tiff bits per sample. */
  private String bps;

  /** The Endianess. */
  private String endianess;

  /** The compression. */
  private String compression;

  /** The pixel density. */
  private String pixeldensity;

  /** The pixel numberimages. */
  private String numberimages;

  /** The Tiff photometric. */
  private String photo;

  /** The baseline errors list. */
  private List<RuleResult> errorsBl;

  /** The baseline warning list. */
  private List<RuleResult> warningsBl;

  /** The Tiff EP errors list. */
  private List<RuleResult> errorsEp;

  /** The Tiff EP warning list. */
  private List<RuleResult> warningsEp;

  /** The Tiff IT errors list. */
  private List<RuleResult> errorsIt0;

  /** The Tiff IT warning list. */
  private List<RuleResult> warningsIt0;

  /** The Tiff IT errors list. */
  private List<RuleResult> errorsIt1;

  /** The Tiff IT warning list. */
  private List<RuleResult> warningsIt1;

  /** The Tiff IT errors list. */
  private List<RuleResult> errorsIt2;

  /** The Tiff IT warning list. */
  private List<RuleResult> warningsIt2;

  /** The Tiff PC errors list. */
  private List<RuleResult> errorsPc;

  /** The Tiff PC warning list. */
  private List<RuleResult> warningsPc;

  /** The Tiff Document object. */
  private TiffDocument tiffModel;

  /**
   * Check Tiff/IT conformance.
   */
  public boolean checkIT0;

  /**
   * Check Tiff/IT conformance.
   */
  public boolean checkIT1;

  /**
   * Check Tiff/IT conformance.
   */
  public boolean checkIT2;

  /**
   * Check Tiff/EP conformance.
   */
  public boolean checkEP;

  /**
   * Check Baseline conformance.
   */
  public boolean checkBL;

  private ArrayList<RuleResult> pcValidation;

  /**
   * Check Policy.
   */
  public boolean checkPC;

  private IndividualReport compareIr;

  private String document;

  private boolean containsData;

  private String conformanceCheckerReport = null;

  /**
   * Constructor + generate.
   *
   * @param name               the name
   * @param path               the path
   * @param reportFilename               the path
   */
  public IndividualReport(String name, String path, String reportFilename) {
    filename = name;
    filepath = path;
    this.reportFilename = reportFilename;
    containsData = false;
  }

  public void setConformanceCheckerReport(String report) {
    conformanceCheckerReport = report;
  }

  public String getConformanceCheckerReport() {
    return conformanceCheckerReport;
  }

  public boolean containsData() {
    return containsData;
  }

  /**
   * Constructor + generate.
   *
   * @param name               the name
   * @param path               the path
   * @param tiffModel          the TIFF model
   * @param baselineValidation the baseline validation
   * @param epValidation       the EP validation
   * @param itValidation       the IT validation
   */
  public IndividualReport(String name, String path, String rFilename, TiffDocument tiffModel,
                          Validator baselineValidation, Validator epValidation, Validator itValidation, Validator it1Validation, Validator it2Validation) {
    filename = name;
    filepath = path;
    ifdCount = 0;
    listIsimg = new ArrayList<>();
    listHasSubIfd = new ArrayList<>();
    containsData = true;
    reportFilename = rFilename;
    generate(tiffModel, baselineValidation, epValidation, itValidation, it1Validation, it2Validation);
  }

  /**
   * Sets pc validation.
   *
   * @param pcValidation the pc validation
   */
  public void setPcValidation(ArrayList<RuleResult> pcValidation) {
    this.pcValidation = pcValidation;
    processPcValidation();
  }

  /**
   * Gets pc validation.
   *
   * @return the pc validation
   */
  public List<RuleResult> getPcValidation() {
    return pcValidation;
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
   * Saves the ifd node.
   *
   * @param ifd the ifd
   */
  private void saveIfdNode(IFD ifd) {
    listIsimg.add(ifd.isImage());
    listHasSubIfd.add(ifd.getsubIFD() != null);
    ifdCount++;
  }

  /**
   * Calculate percent.
   *
   * @return the int
   */
  public int calculatePercent() {
    Double rest = 100.0;
    IndividualReport ir = this;
    if (ir.hasEpValidation()) rest -= ir.getEPErrors().size() * 12.5;
    if (ir.hasItValidation()) rest -= (ir.getITErrors(0).size() + ir.getITErrors(1).size() + ir.getITErrors(2).size()) * 12.5;
    if (ir.hasBlValidation()) rest -= ir.getBaselineErrors().size() * 12.5;
    if (ir.hasPcValidation()) rest -= ir.getPCErrors().size() * 12.5;
    if (rest < 0.0) {
      rest = 0.0;
    }
    return rest.intValue();
  }

  /**
   * Generate the report information.
   *
   * @param tiffModel    the tiff model
   * @param validation   the baseline validation
   * @param epValidation the EP validation
   * @param it0Validation the IT validation
   * @param it1Validation the IT-1 validation
   * @param it2Validation the IT-2 validation
   */
  public void generate(TiffDocument tiffModel, Validator validation,
                       Validator epValidation, Validator it0Validation, Validator it1Validation, Validator it2Validation) {
    this.tiffModel = tiffModel;
    // tiff structure
    IFD ifd = tiffModel.getFirstIFD();
    if (ifd != null) {
      saveIfdNode(ifd);
      while (ifd.hasNextIFD()) {
        ifd = ifd.getNextIFD();
        saveIfdNode(ifd);
      }
    }

    // basic info
    width = tiffModel.getMetadataSingleString("ImageWidth");
    height = tiffModel.getMetadataSingleString("ImageLength");
    bps = tiffModel.getMetadataSingleString("BitsPerSample");
    compression = tiffModel.getMetadataSingleString("Compression");
    endianess = tiffModel.getEndianess().toString();
    pixeldensity = "0";
    numberimages = "0";
    if (tiffModel.getMetadata().contains("ResolutionUnit") && tiffModel.getMetadata().contains("XResolution"))
    {
      double pd = 0;
      try {
        double ru = Double.parseDouble(tiffModel.getMetadata().get("ResolutionUnit").toString());
        String xres = tiffModel.getMetadata().get("XResolution").toString();
        double num = Double.parseDouble(xres.substring(0, xres.indexOf("/")));
        double den = Double.parseDouble(xres.substring(xres.indexOf("/") + 1));
        double xr = num / den;
        double ppi;
        if (ru == 2) {
          ppi = xr;
        } else {
          ppi = xr / 0.3937;
        }
        pixeldensity = ppi+"";
      } catch (Exception ex) {
        pixeldensity = "";
      }
    }
    numberimages = tiffModel.getImageIfds().size() + "";
    photo = tiffModel.getMetadataSingleString("PhotometricRepresentation");

    // errors & warnings
    if (validation != null) {
      errorsBl = validation.getErrors();
      warningsBl = validation.getWarnings();
    }
    if (epValidation != null) {
      errorsEp = epValidation.getErrors();
      warningsEp = epValidation.getWarnings();
    }
    if (it0Validation != null) {
      errorsIt0 = it0Validation.getErrors();
      warningsIt0 = it0Validation.getWarnings();
    }
    if (it1Validation != null) {
      errorsIt1 = it1Validation.getErrors();
      warningsIt1 = it1Validation.getWarnings();
    }
    if (it2Validation != null) {
      errorsIt2 = it2Validation.getErrors();
      warningsIt2 = it2Validation.getWarnings();
    }
    if (pcValidation != null) {
      processPcValidation();
    }
  }

  void processPcValidation() {
    errorsPc = new ArrayList<>();
    warningsPc = new ArrayList<>();
    for (RuleResult rr : pcValidation) if (!rr.getWarning()) errorsPc.add(rr);
    for (RuleResult rr : pcValidation) if (rr.getWarning()) warningsPc.add(rr);
  }

  /**
   * Get ifd count.
   *
   * @return the numbr of ifd's
   */
  public int getIfdCount() {
    return ifdCount;
  }

  /**
   * Get isimg at index.
   *
   * @param index the index
   * @return the isimg list
   */
  public Boolean getIsimgAt(int index) {
    return listIsimg.get(index);
  }

  /**
   * Get hasSubIfd at index.
   *
   * @param index the index
   * @return the list of hasSubIfd
   */
  public Boolean getHasSubIfdAt(int index) {
    return listHasSubIfd.get(index);
  }

  /**
   * Get width.
   *
   * @return the width
   */
  public String getWidth() {
    return width;
  }

  /**
   * Get height.
   *
   * @return the height
   */
  public String getHeight() {
    return height;
  }

  /**
   * Get Photometric.
   *
   * @return the Photometric
   */
  public String getPhotometric() {
    return photo;
  }

  /**
   * Get bits per sample.
   *
   * @return the height
   */
  public String getBitsPerSample() {
    return bps;
  }

  /**
   * Gets endianess.
   *
   * @return the endianess
   */
  public String getEndianess() {
    return endianess;
  }

  /**
   * Gets compression.
   *
   * @return the compression
   */
  public String getCompression() {
    return compression;
  }

  /**
   * Has pc validation boolean.
   *
   * @return the boolean
   */
  public boolean hasPcValidation(){
    return errorsPc != null;
  }

  /**
   * Has bl validation boolean.
   *
   * @return the boolean
   */
  public boolean hasBlValidation(){
    return errorsBl != null;
  }

  /**
   * Has ep validation boolean.
   *
   * @return the boolean
   */
  public boolean hasEpValidation(){
    return errorsEp != null;
  }

  /**
   * Has it validation boolean.
   *
   * @return the boolean
   */
  public boolean hasItValidation(){
    return errorsIt0 != null || errorsIt1 != null || errorsIt2 != null;
  }

  /**
   * Has it validation boolean.
   *
   * @return the boolean
   */
  public boolean hasItValidation(int profile){
    if (profile == 0) return errorsIt0 != null;
    if (profile == 1) return errorsIt1 != null;
    return errorsIt2 != null;
  }

  /**
   * Gets number images.
   *
   * @return the number of images
   */
  public String getNumberImages() {
    return "" + numberimages;
  }

  /**
   * Gets pixels density.
   *
   * @return the pixels density
   */
  public String getPixelsDensity() {
    return "" + pixeldensity;
  }

  /**
   * Get errors list.
   *
   * @return the errors
   */
  public List<RuleResult> getBaselineErrors() {
    if (errorsBl == null) return new ArrayList<RuleResult>();
    return errorsBl;
  }

  /**
   * Get warnings list.
   *
   * @return the warnings
   */
  public List<RuleResult> getBaselineWarnings() {
    if (warningsBl == null) return new ArrayList<RuleResult>();
    return warningsBl;
  }

  /**
   * Get EP errors list.
   *
   * @return the errors
   */
  public List<RuleResult> getEPErrors() {
    if (errorsEp == null) return new ArrayList<RuleResult>();
    return errorsEp;
  }

  /**
   * Get EP warnings list.
   *
   * @return the warnings
   */
  public List<RuleResult> getEPWarnings() {
    if (warningsEp == null) return new ArrayList<RuleResult>();
    return warningsEp;
  }

  /**
   * Get IT errors list.
   *
   * @return the errors
   */
  public List<RuleResult> getITErrors(int profile) {
    if (profile == 0) {
      if (errorsIt0 == null) return new ArrayList<RuleResult>();
      return errorsIt0;
    } else if (profile == 1) {
      if (errorsIt1 == null) return new ArrayList<RuleResult>();
      return errorsIt1;
    } else {
      if (errorsIt2 == null) return new ArrayList<RuleResult>();
      return errorsIt2;
    }
  }

  /**
   * Get IT warnings list.
   *
   * @return the warnings
   */
  public List<RuleResult> getITWarnings(int profile) {
    if (profile == 0) {
      if (warningsIt0 == null) return new ArrayList<RuleResult>();
      return warningsIt0;
    } else if (profile == 1) {
      if (warningsIt1 == null) return new ArrayList<RuleResult>();
      return warningsIt1;
    } else {
      if (warningsIt2 == null) return new ArrayList<RuleResult>();
      return warningsIt2;
    }
  }

  /**
   * Get PC errors list.
   *
   * @return the errors
   */
  public List<RuleResult> getPCErrors() {
    if (errorsPc == null) return new ArrayList<RuleResult>();
    return errorsPc;
  }

  /**
   * Get PC warnings list.
   *
   * @return the warnings
   */
  public List<RuleResult> getPCWarnings() {
    if (warningsPc == null) return new ArrayList<RuleResult>();
    return warningsPc;
  }

  /**
   * Sets pc errors.
   *
   * @param errors the errors
   */
  public void setPCErrors(List<RuleResult> errors) {
    errorsPc = errors;
  }

  /**
   * Sets pc warnings.
   *
   * @param warnings the warnings
   */
  public void setPCWarnings(List<RuleResult> warnings) {
    warningsPc = warnings;
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

  /**
   * Gets n ep err.
   *
   * @return the n ep err
   */
  public int getNEpErr() {
    return getEPErrors() == null ? 0 : getEPErrors().size();
  }

  /**
   * Gets n bl err.
   *
   * @return the n bl err
   */
  public int getNBlErr() {
    return getBaselineErrors() == null ? 0 : getBaselineErrors().size();
  }

  /**
   * Gets n it err.
   *
   * @return the n it err
   */
  public int getNItErr(int profile) {
    return getITErrors(profile) == null ? 0 : getITErrors(profile).size();
  }

  /**
   * Gets n ep war.
   *
   * @return the n ep war
   */
  public int getNEpWar() {
    return getEPWarnings() == null ? 0 : getEPWarnings().size();
  }

  /**
   * Gets n bl war.
   *
   * @return the n bl war
   */
  public int getNBlWar() {
    return getBaselineWarnings() == null ? 0 : getBaselineWarnings().size();
  }

  /**
   * Gets n it war.
   *
   * @return the n it war
   */
  public int getNItWar(int profile) {
    return getITWarnings(profile) == null ? 0 : getITWarnings(profile).size();
  }
}
