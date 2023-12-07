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

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.reporting.ReportTag;
import dpfmanager.shell.modules.report.util.ReportHtml;

import com.easyinnova.implementation_checker.ValidationResult;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.Tag;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.model.types.abstractTiffType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The Class IndividualReport.
 */
public class IndividualReport extends ReportSerializable {

  /**
   * Do not modify!
   */
  private static final long serialVersionUID = 2946L;

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
   * The passed rules list (only for policy).
   */
  private Map<String, List<RuleResult>> passed;

  /**
   * The isos to check.
   */
  private List<String> checkedIsos;

  /**
   * The Tiff Document object.
   */
  private TiffDocument tiffModel;

  private IndividualReport compareIr;

  private String document;

  private boolean containsData;

  /**
   * Data precomputed for write external
   */
  private transient String conformanceCheckerReport = null;

  private boolean precomputedOutput = false;

  /**
   * Author tag
   */
  private Map<Integer, String> authorTag = new HashMap<>();
  private Map<Integer, String> authorIptc = new HashMap<>();
  private Map<Integer, String> authorXmp = new HashMap<>();

  /**
   * Extra check information
   */
  private String internalReportFodler;

  private int idReport;

  private long uuid;

  private boolean error;

  private boolean quick;

  private Map<String, ArrayList<String>> modifiedIsos;

  private String imagePath = null;

  private int reportId;

  private boolean isOriginal = true;

  private String inputStr;

  private String serPath;

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
  public IndividualReport(String name, String path, String reportFilename, boolean q, int id) {
    filename = name;
    filepath = path;
    this.reportFilename = reportFilename;
    containsData = false;
    errors = new HashMap<>();
    warnings = new HashMap<>();
    quick = q;
    reportId = id;
  }

  public boolean getIsOriginal() {
    return isOriginal;
  }

  public void setisOriginal(boolean isOriginal) {
    this.isOriginal = isOriginal;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String path) {
    imagePath = path;
  }

  /**
   * Constructor + generate.
   *
   * @param name      the name
   * @param path      the path
   * @param tiffModel the TIFF model
   */
  public IndividualReport(String name, String path, String rFilename, TiffDocument tiffModel, Map<String, ValidationResult> validators, Map<String, ArrayList<String>> modifiedIsosList, boolean q, int id) {
    filename = name;
    filepath = path;
    containsData = true;
    reportFilename = rFilename;
    errors = new HashMap<>();
    warnings = new HashMap<>();
    passed = new HashMap<>();
    modifiedIsos = modifiedIsosList;
    quick = q;
    reportId = id;
    generate(tiffModel, validators);
  }

  public int getReportId() {
    return reportId;
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
    this.checkedIsos = isosCheck;
  }

  public String getInputStr() {
    return inputStr;
  }

  public void setInputStr(String inputStr) {
    this.inputStr = inputStr;
  }

  public List<String> getSelectedIsos() {
    if (checkedIsos == null) return new ArrayList<>();
    return checkedIsos;
  }

  public List<String> getCheckedIsos() {
    List<String> checked = new ArrayList<>();
    if (errors != null) {
      checked.addAll(errors.keySet());
      Collections.sort(checked, Collator.getInstance());
    }
    return checked;
  }

  public void setConformanceCheckerReport(String report) {
    conformanceCheckerReport = report;
    precomputedOutput = true;
  }

  public String getSerPath() {
    return serPath;
  }

  public void setSerPath(String serPath) {
    this.serPath = serPath;
  }

  public boolean hasPrecomputedOutput() {
    return precomputedOutput;
  }

  public String getConformanceCheckerReport() {
    return conformanceCheckerReport;
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

  public void addIsosCheck(String iso) {
    if (!checkedIsos.contains(iso)) {
      checkedIsos.add(iso);
    }
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
   * Generate the report information.
   *
   * @param tiffModel   the tiff model
   * @param validations the validations
   */
  public void generate(TiffDocument tiffModel, Map<String, ValidationResult> validations) {
    this.tiffModel = tiffModel;

    for (String key : validations.keySet()) {
      errors.put(key, validations.get(key).getErrors());
      warnings.put(key, validations.get(key).getWarnings(true));
      if (key.equals(TiffConformanceChecker.POLICY_ISO)){
        passed.put(key, validations.get(key).getPassed());
      }
    }

    /**
     * Metadata extra info
     */
    int nifd = 1;
    IFD tdifd = getTiffModel().getFirstIFD();
    while (tdifd != null) {
      XMP xmp = null;
      IPTC iptc = null;
      if (tdifd.containsTagId(TiffTags.getTagId("XMP"))) {
        try {
          xmp = (XMP) tdifd.getTag("XMP").getReadValue().get(0);
        } catch (Exception ex) {
          xmp = null;
        }
      }
      if (tdifd.containsTagId(TiffTags.getTagId("IPTC"))) {
        try {
          iptc = (IPTC) tdifd.getTag("IPTC").getReadValue().get(0);
        } catch (Exception ex) {
          iptc = null;
        }
      }

      // Author
      if (tdifd.containsTagId(TiffTags.getTagId("Artist"))) authorTag.put(nifd, tdifd.getTag("Artist").toString());
      if (iptc != null) authorIptc.put(nifd, iptc.getCreator());
      if (xmp != null) authorXmp.put(nifd, xmp.getCreator());
      tdifd = tdifd.getNextIFD();
      nifd++;
    }

    /**
     * SmallMetadata
     */

  }

  /**
   * Has pc validation boolean.
   *
   * @return the boolean
   */
  public boolean hasValidation(String key) {
    return checkedIsos.contains(key);
  }

  public List<RuleResult> getKORuleResults(String key) {
    List<RuleResult> all = new ArrayList<>();
    all.addAll(getErrors(key));
    all.addAll(getWarnings(key));
    return all;
  }

  public List<RuleResult> getAllRuleResults(String key) {
    List<RuleResult> all = new ArrayList<>();
    all.addAll(getErrors(key));
    all.addAll(getWarnings(key));
    all.addAll(getPassed(key));
    return all;
  }

  /**
   * Get warnings and infos list.
   *
   * @return the warnings and infos
   */
  public List<RuleResult> getWarnings(String key) {
    if (!warnings.containsKey(key)) {
      return new ArrayList<>();
    }
    return warnings.get(key);
  }

  public List<RuleResult> getWarningsPolicy(String key) {
    List<RuleResult> filtered = new ArrayList<>();
    for (RuleResult rr : warnings.get(key)) {
      if (modifiedIsos.get(key).contains(rr.getRule().getId())) {
        filtered.add(rr);
      }
    }
    return filtered;
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

  public List<RuleResult> getErrorsPolicy(String key) {
    List<RuleResult> filtered = new ArrayList<>();
    for (RuleResult rr : errors.get(key)) {
      if (rr.isRelaxed()) {
        filtered.add(rr);
      }
    }
    return filtered;
  }

  /**
   * Get warnings list.
   *
   * @return the warnings
   */
  public List<RuleResult> getOnlyWarnings(String key) {
    List<RuleResult> result = new ArrayList<>();
    for (RuleResult rule : getWarnings(key)) {
      if (rule.getWarning()) {
        result.add(rule);
      }
    }
    return result;
  }

  public List<RuleResult> getOnlyWarningsPolicy(String key) {
    List<RuleResult> result = new ArrayList<>();
    for (RuleResult rule : getWarnings(key)) {
      if (rule.getWarning() && rule.isRelaxed()) {
        result.add(rule);
      }
    }
    return result;
  }

  /**
   * Get passed list.
   *
   * @return the warnings
   */
  public List<RuleResult> getPassed(String key) {
    if (!passed.containsKey(key)) {
      return new ArrayList<>();
    }
    return passed.get(key);
  }

  /**
   * Get infos list.
   *
   * @return the infos
   */
  public List<RuleResult> getOnlyInfos(String key) {
    List<RuleResult> result = new ArrayList<>();
    for (RuleResult rule : getWarnings(key)) {
      if (rule.getInfo()) {
        result.add(rule);
      }
    }
    return result;
  }

  public List<RuleResult> getOnlyInfosPolicy(String key) {
    List<RuleResult> result = new ArrayList<>();
    for (RuleResult rule : getWarnings(key)) {
      if (rule.getInfo() && rule.isRelaxed()) {
        result.add(rule);
      }
    }
    return result;
  }

  /**
   * Gets warnings count.
   *
   * @return the warnings count
   */
  public int getNWarnings(String key) {
    int n = 0;
    for (RuleResult rule : getWarnings(key)) {
      if (rule.getWarning()) n++;
    }
    return n;
  }

  public int getNWarningsPolicy(String key) {
    int n = 0;
    if (modifiedIsos.containsKey(key)) {
      for (RuleResult rule : getWarnings(key)) {
        if (rule.getWarning() && !modifiedIsos.get(key).contains(rule.getRule().getId())) {
          n++;
        }
      }
    }
    return n;
  }

  /**
   * Gets warnings count.
   *
   * @return the warnings count
   */
  public int getNInfos(String key) {
    int n = 0;
    for (RuleResult rule : getOnlyInfos(key)) {
      if (rule.getInfo()) n++;
    }
    return n;
  }

  public int getNInfosPolicy(String key) {
    int n = 0;
    if (modifiedIsos.containsKey(key)) {
      for (RuleResult rule : getWarnings(key)) {
        if (rule.getInfo() && !modifiedIsos.get(key).contains(rule.getRule().getId())) {
          n++;
        }
      }
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

  public int getNErrorsPolicy(String key) {
    int n = 0;
    if (modifiedIsos.containsKey(key)) {
      for (RuleResult rule : getErrors(key)) {
        if (!modifiedIsos.get(key).contains(rule.getRule().getId())) {
          n++;
        }
      }
    }
    return n;
  }

  public List<RuleResult> getAllErrors() {
    List<RuleResult> allErrors = new ArrayList<>();
    for (String key : errors.keySet()) {
      if (getCheckedIsos().contains(key)) {
        allErrors.addAll(errors.get(key));
      }
    }
    return allErrors;
  }

  public int getAllNErrorsPolicy() {
    int n = 0;
    for (String key : errors.keySet()) {
      if (getCheckedIsos().contains(key)) {
        n += hasModifiedIso(key) ? getNErrorsPolicy(key) : getNErrors(key);
      }
    }
    return n;
  }

  public List<RuleResult> getAllWarnings() {
    List<RuleResult> allWarnings = new ArrayList<>();
    for (String key : warnings.keySet()) {
      if (getCheckedIsos().contains(key)) {
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

  public Map<String, ArrayList<String>> getModifiedIsos() {
    return modifiedIsos;
  }

  public boolean hasModifiedIso(String iso) {
    return modifiedIsos.containsKey(iso);
  }

  public ArrayList<ReportTag> getTags(boolean subTags) {
    return getTags(subTags, false);
  }

  /**
   * Gets tags.
   *
   * @return the tags
   */
  public ArrayList<ReportTag> getTags(boolean subTags, boolean defaultTags) {
    ArrayList<ReportTag> list = new ArrayList<>();
    List<Integer> currentTagsIds = new ArrayList<>();
    TiffDocument td = getTiffModel();
    IFD ifd = td.getFirstIFD();
    IFD ifdcomp = null;
    if (getCompareReport() != null && !isOriginal) {
      ifdcomp = getCompareReport().getTiffModel().getFirstIFD();
    }
    td.getFirstIFD();
    int index = 0;
    while (ifd != null) {
      IfdTags meta = ifd.getMetadata();
      boolean isThumbnail = ifd.isThumbnail();
      for (TagValue tv : meta.getTags()) {
        ReportTag tag = new ReportTag();
        tag.index = index;
        tag.tv = tv;
        tag.thumbnail = isThumbnail;
        if (ifdcomp != null) {
          if (!ifdcomp.getMetadata().containsTagId(tv.getId()))
            tag.dif = 1;
        }
        if (!showTag(tv)) tag.expert = true;
        if (subTags) list.addAll(getSubTags(tag, index, isThumbnail, defaultTags));
        list.add(tag);
        currentTagsIds.add(tag.tv.getId());
      }

      ifd = ifd.getNextIFD();
      if (ifdcomp != null) ifdcomp = ifdcomp.getNextIFD();

      // Get missing default tags values for each main IFD
      if (defaultTags) {
        TagValue tv = meta.get(262);
        int intPhotometric = (tv != null) ? getPhotometricIntValue(tv) : -1;
        List<ReportTag> defaultTagsList = getMissingDefaultTags(currentTagsIds, index, isThumbnail, intPhotometric);
        list.addAll(defaultTagsList);
      }

      index++;
    }

    // Sort list by tag id
    Collections.sort(list, new Comparator<ReportTag>() {
      @Override
      public int compare(ReportTag o1, ReportTag o2) {
        Integer i1 = o1.index;
        int comp = i1.compareTo(o2.index);
        if (comp == 0){
          Integer id1 = o1.tv.getId();
          return id1.compareTo(o2.tv.getId());
        }
        return comp;
      }
    });

    return list;
  }

  private int getPhotometricIntValue(TagValue tv){
    if (tv.getValue() != null){
      if (tv.getValue().size() > 0) {
        return tv.getValue().get(0).toInt();
      }
      return -1;
    } else {
      HashMap<String, String> map = TiffTags.getTag(262).getValues();
      for (String key : map.keySet()){
        String value = map.get(key);
        if (value.equals(tv.getFirstTextReadValue())) {
          return Integer.parseInt(key);
        }
      }
      return -1;
    }
  }

  private ArrayList<ReportTag> getSubTags(ReportTag tag, int index, boolean isThumbnail, boolean defaultTags){
    ArrayList<ReportTag> subTagsList = new ArrayList<>();
    List<Integer> currentTagsIds = new ArrayList<>();
    TagValue tvPhotometric = null;
    if (tag.tv.getId() == 34665) {
      // EXIF
      try {
        abstractTiffType obj = tag.tv.getReadValue().get(0);
        if (obj instanceof IFD) {
          IFD exif = (IFD) obj;
          tvPhotometric = exif.getTags().get(262);
          for (TagValue tv : exif.getTags().getTags()) {
            ReportTag rTag = new ReportTag();
            rTag.index = index;
            rTag.tv = tv;
            rTag.thumbnail = isThumbnail;
            if (!showTag(tv)) rTag.expert = true;
            subTagsList.add(rTag);
            currentTagsIds.add(rTag.tv.getId());
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else if (tag.tv.getId() == 330) {
      // Sub IFD
      IFD sub = (IFD) tag.tv.getReadValue().get(0);
      tvPhotometric = sub.getTags().get(262);
      isThumbnail = sub.isThumbnail();
      for (TagValue tv : sub.getTags().getTags()) {
        ReportTag rTag = new ReportTag();
        rTag.index = index;
        rTag.tv = tv;
        rTag.thumbnail = isThumbnail;
        if (!showTag(tv)) rTag.expert = true;
        subTagsList.add(rTag);
        currentTagsIds.add(rTag.tv.getId());
      }
    }

    // Get missing default tags values for each main IFD
    if (defaultTags && tvPhotometric != null) {
      int intPhotometric = getPhotometricIntValue(tvPhotometric);
      List<ReportTag> defaultTagsList = getMissingDefaultTags(currentTagsIds, index, isThumbnail,intPhotometric);
      subTagsList.addAll(defaultTagsList);
    }

    return subTagsList;
  }

  public static List<String> defaultTagsNames = Arrays.asList("BitsPerSample","Compression","DotRange","MaxSampleValue","MinSampleValue","NewSubfileType"
      ,"Orientation","PlanarConfiguration","Predictor","ResolutionUnit","RowsPerStrip","SampleFormat"
      ,"SamplesPerPixel","SMaxSampleValue","SMinSampleValue","Threshholding","TransferRange","WhiteLevel"
      ,"YCbCrCoefficients","YCbCrSubSampling","YCbCrPositioning","GrayResponseUnit","InkSet","NumberOfInks","Indexed","ReferenceBlackWhite");

  public List<ReportTag> getMissingDefaultTags(List<Integer> currentTagsIds, Integer index, Boolean isThumbnail, Integer photometric){
    List<ReportTag> defaultTags = new ArrayList<>();
    try{
      TiffTags tiffTags = TiffTags.getTiffTags();
      for (Integer key : tiffTags.tagMap.keySet()) {
        if (currentTagsIds.contains(key)) continue;
        Tag tag = tiffTags.tagMap.get(key);
        if (!tag.getDefaultValue().isEmpty() && defaultTagsNames.contains(tag.getName())) {
          if (tag.getName().equals("GrayResponseUnit") && photometric != 0 && photometric != 1) continue;
          if ((tag.getName().equals("YCbCrCoefficients") || tag.getName().equals("YCbCrSubSampling") || tag.getName().equals("YCbCrPositioning")) && photometric != 6) continue;
          if ((tag.getName().equals("InkSet") || tag.getName().equals("NumberOfInks")) && photometric != 5) continue;
          if (tag.getName().equals("Indexed") && photometric != 3) continue;
          if (tag.getName().equals("ReferenceBlackWhite") && photometric != 2 && photometric != 6) continue;
          ReportTag rTag = new ReportTag();
          rTag.index = index;
          rTag.tv = new TagValue(tag.getId(), -1);
          rTag.thumbnail = isThumbnail;
          rTag.isDefault = true;
          String desc = tag.getTextDescription(tag.getDefaultValue());
          rTag.defaultValue = (desc == null) ? tag.getDefaultValue() : desc;
          if (!showTag(tag.getName())) rTag.expert = true;
          defaultTags.add(rTag);
        }
      }
    } catch (Exception e) {
      /* unable to read tiff tags */
    }
    return defaultTags;
  }

  public String getDescriptiveTextValue(Tag tag) {
    if (tag.hasReadableDescription()){
      String desc = tag.getDefaultValue();
      String tagDescription = tag.getTextDescription(toString());
      if (tagDescription != null){
        desc = tagDescription;
      }
      return desc;
    } else {
      return tag.getDefaultValue();
    }
  }


  /**
   * Show Tag.
   *
   * @param tv The tag value
   * @return true, if successful
   */
  public boolean showTag(TagValue tv) {
    return showTag(tv.getName());
  }

  public boolean showTag(String name) {
    if (IndividualReport.showableTags == null) {
      IndividualReport.showableTags = readShowableTags();
    }
    return IndividualReport.showableTags.contains(name);
  }

  public static HashSet<String> showableTags = null;

  /**
   * Read showable tags file.
   *
   * @return hashset of tags
   */
  protected synchronized HashSet<String> readShowableTags() {
    HashSet<String> hs = new HashSet<>();
    try {
      Path path = Paths.get("./src/main/resources/");
      if (Files.exists(path)) {
        // Look in current dir
        FileReader fr = new FileReader("./src/main/resources/htmltags.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
          String[] fields = line.split("\t");
          if (fields.length == 1) {
            hs.add(fields[0]);
          }
          line = br.readLine();
        }
        br.close();
        fr.close();
      } else {
        // Look in JAR
        String resource = "htmltags.txt";
        Class cls = ReportHtml.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(resource);
        //CodeSource src = ReportHtml.class.getProtectionDomain().getCodeSource();
        if (in != null) {
          try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            while (line != null) {
              String[] fields = line.split("\t");
              if (fields.length == 1) {
                hs.add(fields[0]);
              }
              line = br.readLine();
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        } else {
          throw new Exception("InputStream is null");
        }
      }
    } catch (Exception ex) {
    }
    return hs;
  }

  public boolean isQuick() {
    return quick;
  }

  public Map<Integer, String> getAuthorTag() {
    return authorTag;
  }

  public Map<Integer, String> getAuthorIptc() {
    return authorIptc;
  }

  public Map<Integer, String> getAuthorXmp() {
    return authorXmp;
  }
}
