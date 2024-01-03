/**
 * <h1>HtmlReport.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
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
 * @author Víctor Muñoz Sola
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.implementation_checker.rules.model.RuleType;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.model.types.abstractTiffType;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by easy on 05/05/2016.
 */
public class HtmlReport extends Report {

  /**
   * Parse an individual report to HTML.
   *
   * @param ir   the individual report.
   */
  public String parseIndividual(IndividualReport ir, int id, String internalReportFolder) {
    String templatePath = "templates/individual.html";

    String htmlBody = readFilefromResources(templatePath);

    // Thumbnail image
    String fileName = getReportName("", ir.getFilePath(), id);
    String imgPath = getThumbnailPath(internalReportFolder, fileName, ir);
    ir.setImagePath(imgPath);
    htmlBody = StringUtils.replace(htmlBody, "##IMG_PATH##", encodeUrl(imgPath));

    /**
     * Basic info / divs conforms
     */
    htmlBody = StringUtils.replace(htmlBody, "##IMG_NAME##", ir.getFileName());
    htmlBody = StringUtils.replace(htmlBody, "##IMG_FILEPATH##", ir.getFilePath());
    try {
      long isize = new File(ir.getFilePath()).length();
      String ssize = isize + " bytes";
      if (isize > 1024) ssize = (isize / 1024) + " Kb";
      if (isize > 1024 * 1024) ssize = (isize / (1024 * 1024)) + " Mb";

      htmlBody = StringUtils.replace(htmlBody, "##IMG_FILESIZE##", "Size: " + ssize);
    } catch (Exception e) {
      htmlBody = StringUtils.replace(htmlBody, "##IMG_FILESIZE##", "");
    }
    String divs = "";
    for (String iso : ir.getCheckedIsos()) {
      if (ir.hasValidation(iso) || ir.getErrors(iso).isEmpty()) {
        divs += makeConformsText(ir, iso);
      }
    }
    htmlBody = StringUtils.replace(htmlBody, "##DIVS_CONFORMS##", divs);

    int mode = 0;
    if (ir.getCompareReport() != null) {
      if (ir.getIsOriginal()) mode = 1;
      else mode = 2;
    }
    if (mode == 1) {
      htmlBody = StringUtils.replace(htmlBody, "##CL_LINKR2##", "show");
      htmlBody = StringUtils.replace(htmlBody, "##LINK2##", encodeUrl(new File(fileName).getName() + "_fixed.html"));
    }
    if (mode == 2) {
      htmlBody = StringUtils.replace(htmlBody, "##CL_LINKR1##", "show");
      htmlBody = StringUtils.replace(htmlBody, "##LINK1##", encodeUrl(new File(fileName).getName() + ".html"));
    }

    /**
     * Table errors / warnings count
     */
    if (!ir.isQuick()){
      int modeTh = 1;
      String thTmpl = "<tr><td></td><td>Errors</td><td>Warnings</td></tr>";
      String rowTmpl = "<tr>\n" +
          "    <td>##TITLE##</td>\n" +
          "    <td class=\"##ERR_CLASS##\">##ERR##</td>\n" +
          "    <td class=\"##WAR_CLASS##\">##WAR##</td>\n" +
          "    </tr>";
      if (ir.getModifiedIsos().keySet().size() > 0) {
        thTmpl = "<tr><td></td><td>Standard</td><td>Policy</td></tr>";
        rowTmpl = "<tr>\n" +
            "\t\t\t\t\t\t        <td>##TITLE##</td>\n" +
            "\t\t\t\t\t\t        <td>" +
            "\t\t\t\t\t\t\t        <p><span class=\"##ERR_CLASS##\">##ERR## errors</span></p>" +
            "\t\t\t\t\t\t\t        <p><span class=\"##WAR_CLASS##\">##WAR## warnings</span></p>" +
            "\t\t\t\t\t\t        </td>\n" +
            "\t\t\t\t\t\t        <td>" +
            "\t\t\t\t\t\t\t        <p><span class=\"##ERR_CLASS_P##\">##ERR_P## errors</span></p>" +
            "\t\t\t\t\t\t\t        <p><span class=\"##WAR_CLASS_P##\">##WAR_P## warnings</span></p>" +
            "\t\t\t\t\t\t        </td>\n" +
            "\t\t\t\t\t\t    </tr>";
        modeTh = 2;
      }
      String rows = "";
      for (String iso : ir.getCheckedIsos()) {
        if (ir.hasValidation(iso)) {
          String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
          String row = rowTmpl;
          int errorsCount = ir.getNErrors(iso);
          int warningsCount = ir.getNWarnings(iso);
          row = StringUtils.replace(row, "##TITLE##", name);
          String difErr = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNErrors(iso), ir.getNErrors(iso)) : "";
          String difWar = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNWarnings(iso), ir.getNWarnings(iso)) : "";
          row = StringUtils.replace(row, "##ERR##", "" + errorsCount + difErr);
          row = StringUtils.replace(row, "##WAR##", "" + warningsCount + difWar);
          row = StringUtils.replace(row, "##ERR_CLASS##", errorsCount > 0 ? "error" : "info");
          row = StringUtils.replace(row, "##WAR_CLASS##", warningsCount > 0 ? "warning" : "info");
          if (modeTh == 2) {
            if (!ir.hasModifiedIso(iso)) {
              // Empty
              row = StringUtils.replace(row, "##ERR_CLASS_P##", "hide");
              row = StringUtils.replace(row, "##WAR_CLASS_P##", "hide");
            } else {
              int errorsCountPolicy = ir.getNErrorsPolicy(iso);
              int warningsCountPolicy = ir.getNWarningsPolicy(iso);
              row = StringUtils.replace(row, "##ERR_P##", "" + errorsCountPolicy);
              row = StringUtils.replace(row, "##WAR_P##", "" + warningsCountPolicy);
              row = StringUtils.replace(row, "##ERR_CLASS_P##", errorsCountPolicy > 0 ? "error" : (errorsCount > 0 ? "success" : "info"));
              row = StringUtils.replace(row, "##WAR_CLASS_P##", warningsCountPolicy > 0 ? "warning" : (warningsCount > 0 ? "success" : "info"));
            }
          }
          rows += row;
        }
      }
      htmlBody = StringUtils.replace(htmlBody, "##TABLE_RESUME_ERRORS##", thTmpl + rows);

      /**
       * Errors / Warnings resume
       */
      String fullTmpl = "<div class=\"row bot20 fullw\">\n" +
          "\t\t\t\t##CHECK##\n" +
          "\t\t\t\t<div>\n" +
          "\t\t\t\t\t<h5 class=\"bold left15\"><i class=\"fa ##ICON##\"></i>  ##TITLE##</h4>\n" +
          "\t\t\t\t\t##CONTENT##\n" +
          "\t\t\t\t</div>\n" +
          "\t\t\t</div>";
      String checkInfos = "<div class=\"clexpert\"><input type=\"checkbox\" id=\"checkInfo##COUNT##\" onchange=\"onChangeInfo(##COUNT##)\"><label for=\"checkInfo##COUNT##\"><span></span> Show infos</label></div>";
      String errorsTmpl = "<table class=\"CustomTable3 left15\">\n" +
          "\t\t\t\t        <tr>\n" +
          "\t\t\t\t            <th class=\"bold tcenter\" style='width: 50px;'>Type</th>\n" +
          "\t\t\t\t            <th class=\"bold\" style='width: 120px;'>ID</th>\n" +
          "\t\t\t\t            <th class=\"bold\" style='width: 80px;'>Location</th>\n" +
          "\t\t\t\t            <th class=\"bold\">Description</th>\n" +
          "\t\t\t\t        </tr>\n" +
          "\t\t\t\t        ##ROWS##\n" +
          "\t\t\t\t\t</table>";
      String tdTmpl = "<tr ##CLASS## ##DISPLAY## ##POPOVER##><td class=\"bold tcenter\"><i style=\"font-size: 18px;\" class=\"fa fa-##FA_CLASS##-circle iconStyle\"/></td><td>##ID##</td><td>##LOC##</td><td>##DESC##</td></tr>";
      rows = "";
      int count = 0;
      for (String iso : ir.getCheckedIsos()) {
        if (ir.hasValidation(iso) && !iso.equals(TiffConformanceChecker.POLICY_ISO)) {
          String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
          String row = fullTmpl, icon;
          int errorsCount = ir.getNErrors(iso);
          int warningsCount = ir.getNWarnings(iso);
          int infosCount = ir.getNInfos(iso);
          int addedRows = 0, addedInfos = 0;
          if (errorsCount > 0) {
            icon = "fa-times-circle";
          } else if (warningsCount > 0) {
            icon = "fa-exclamation-circle";
          } else {
            icon = "fa-check-circle";
          }
          String content = "";
          if (errorsCount + warningsCount + infosCount > 0) {
            content += errorsTmpl;
            String allRows = "";
            // Errors, Warnings and Infos
            for (RuleResult val : ir.getKORuleResults(iso)) {
              String tdRow = tdTmpl, display = "", clasz = "", location = "";
              if (val.getRule().isError() || val.getRule().isCritical()) {
                tdRow = tdRow.replace("##FA_CLASS##", "times");
              } else if (val.getRule().isWarning()) {
                tdRow = tdRow.replace("##FA_CLASS##", "exclamation");
              } else if (val.getRule().isInfo()) {
                tdRow = tdRow.replace("##FA_CLASS##", "info");
                display = "style='display: none;'";
                clasz = "class='info##COUNT##'";
                addedInfos++;
              }
              tdRow = tdRow.replace("##ID##", val.getRule() != null ? val.getRule().getId() : "");
              tdRow = tdRow.replace("##LOC##", val.getLocation());
              tdRow = tdRow.replace("##DESC##", val.getDescription());
              tdRow = tdRow.replace("##POPOVER##", makePopoverAttributes(val));
              tdRow = tdRow.replace("##DISPLAY##", display);
              tdRow = tdRow.replace("##CLASS##", clasz);
              addedRows++;
              allRows += tdRow;
            }
            content = StringUtils.replace(content, "##ROWS##", allRows);
          }
          if (addedRows == 0) {
            content = "";
          }
          if (addedInfos == 0) {
            row = StringUtils.replace(row, "##CHECK##", "");
          }
          row = StringUtils.replace(row, "##CHECK##", checkInfos);
          row = StringUtils.replace(row, "##CONTENT##", content);
          row = StringUtils.replace(row, "##COUNT##", (++count) + "");
          row = StringUtils.replace(row, "##TITLE##", name);
          row = StringUtils.replace(row, "##ICON##", icon);
          rows += row;
        }
      }
      htmlBody = StringUtils.replace(htmlBody, "##DIVS_CONFORMANCE##", rows);

      /**
       * Policy checker custom rules
       */
      String fullTmplPC = "<div class=\"row bot20 fullw\">\n" +
          "\t\t\t\t<div>\n" +
          "\t\t\t\t\t<h5 class=\"bold left15\"><i class=\"fa ##ICON##\"></i>  ##TITLE##</h4>\n" +
          "\t\t\t\t\t##CONTENT##\n" +
          "\t\t\t\t</div>\n" +
          "\t\t\t</div>";
      String policyTmpl = "<table class=\"CustomTable3 left15\">\n" +
          "\t\t\t\t        <tr>\n" +
          "\t\t\t\t            <th class=\"bold tcenter\" style='width: 50px;'>Type</th>\n" +
          "\t\t\t\t            <th class=\"bold\">Rule</th>\n" +
          "\t\t\t\t            <th class=\"bold\">Description</th>\n" +
          "\t\t\t\t        </tr>\n" +
          "\t\t\t\t        ##ROWS##\n" +
          "\t\t\t\t\t</table>";
      String pcTmpl = "<tr ##CLASS## ##DISPLAY##><td class=\"bold tcenter\"><i style=\"font-size: 18px;\" class=\"fa fa-##FA_CLASS##-circle iconStyle\"/><td>##RULE##</td><td>##DESC##</td></tr>";
      rows = "";
      if (ir.hasValidation(TiffConformanceChecker.POLICY_ISO)) {
        String name = TiffConformanceChecker.POLICY_ISO_NAME;
        String row = fullTmplPC, icon, content = "", tdRow, location;
        String iso = TiffConformanceChecker.POLICY_ISO;
        int errorsCount = ir.getNErrors(iso);
        int warningsCount = ir.getNWarnings(iso);
        int infosCount = ir.getNInfos(iso);
        if (errorsCount > 0) {
          icon = "fa-times-circle";
        } else if (warningsCount > 0) {
          icon = "fa-exclamation-circle";
        } else {
          icon = "fa-check-circle";
        }
        int addedRows = 0;
        content += policyTmpl;
        if (errorsCount + warningsCount + infosCount > 0) {
          String allRows = "";
          // Errors, Warnings and Infos
          for (RuleResult val : ir.getKORuleResults(iso)) {
            // Policy value
            tdRow = pcTmpl;
            if (!val.ok() && !val.getWarning()) {
              tdRow = tdRow.replace("##FA_CLASS##", "times");
            } else if (!val.ok()) {
              tdRow = tdRow.replace("##FA_CLASS##", "exclamation");
            }
            tdRow = tdRow.replace("##RULE##", val.getRule().getDescription().getValue());
            tdRow = tdRow.replace("##DESC##", val.getDescription());
            addedRows++;
            allRows += tdRow;
          }
          content = StringUtils.replace(content, "##ROWS##", allRows);
        }
        if (addedRows == 0) {
          content = "";
        }
        row = StringUtils.replace(row, "##CONTENT##", content);
        row = StringUtils.replace(row, "##TITLE##", name);
        row = StringUtils.replace(row, "##ICON##", icon);
        rows += row;
      }

      /**
       * Policy checker custom ISO
       */
      String errorsTmplPC = "<table class=\"CustomTable3 left15\">\n" +
          "\t\t\t\t        <tr>\n" +
          "\t\t\t\t            <th class=\"bold tcenter\" style='width: 50px;'></th>\n" +
          "\t\t\t\t            <th class=\"bold\" style='width: 120px;'>ID</th>\n" +
          "\t\t\t\t            <th class=\"bold\" style='width: 80px;'>Location</th>\n" +
          "\t\t\t\t            <th class=\"bold\">Description</th>\n" +
          "\t\t\t\t        </tr>\n" +
          "\t\t\t\t        ##ROWS##\n" +
          "\t\t\t\t\t</table>";
      String tdTmplPC = "<tr ##POPOVER##><td class=\"bold tcenter\"><i style=\"font-size: 18px;\" class=\"fa fa-minus-circle iconStyle\"/></td><td>##ID##</td><td>##LOC##</td><td>##DESC##</td></tr>";
      for (String iso : ir.getModifiedIsos().keySet()) {
        if (ir.hasValidation(iso) && !iso.equals(TiffConformanceChecker.POLICY_ISO)) {
          String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
          String row = fullTmplPC, icon, content = "", tdRow, location;
          int errorsCount = ir.getNErrorsPolicy(iso);
          int warningsCount = ir.getNWarningsPolicy(iso);
          int infosCount = ir.getNInfosPolicy(iso);
          if (errorsCount > 0) {
            icon = "fa-times-circle";
          } else if (warningsCount > 0) {
            icon = "fa-exclamation-circle";
          } else {
            icon = "fa-check-circle";
          }
          int addedRows = 0;
          content += errorsTmplPC;
          if (errorsCount + warningsCount + infosCount > 0) {
            String allRows = "";
            // Errors, Warnings and Infos
            for (RuleResult val : ir.getKORuleResults(iso)) {
              // Skip not invalidated rules
              if (!ir.getModifiedIsos().get(iso).contains(val.getRule().getId())) continue;
              tdRow = tdTmplPC;
              location = val.getLocation();
              tdRow = tdRow.replace("##ID##", val.getRule() != null ? val.getRule().getId() : "");
              tdRow = tdRow.replace("##LOC##", location);
              tdRow = tdRow.replace("##DESC##", val.getDescription());
              tdRow = tdRow.replace("##POPOVER##", makePopoverAttributes(val));
              addedRows++;
              allRows += tdRow;
            }
            content = StringUtils.replace(content, "##ROWS##", allRows);
          }
          if (addedRows == 0) {
            content = "";
          }
          row = StringUtils.replace(row, "##CONTENT##", content);
          row = StringUtils.replace(row, "##TITLE##", name);
          row = StringUtils.replace(row, "##ICON##", icon);
          rows += row;
        }
      }

      /**
       * Show / Hide all Conformance & Policy Block
       */
      String displayPolicy = "none";
      if (!rows.equals("")) {
        displayPolicy = "block";
      }
      htmlBody = StringUtils.replace(htmlBody, "##SHOW_POLICY##", displayPolicy);
      htmlBody = StringUtils.replace(htmlBody, "##SHOW_CONFORMANCE##", "block");
      htmlBody = StringUtils.replace(htmlBody, "##DIVS_POLICY##", rows);
    } else {
      /**
       * Hide all Conformance & Policy Block
       */
      htmlBody = StringUtils.replace(htmlBody, "##SHOW_POLICY##", "none");
      htmlBody = StringUtils.replace(htmlBody, "##SHOW_CONFORMANCE##", "none");
    }

    /**
     * Tags divs
     */
    htmlBody = StringUtils.replace(htmlBody, "##TAGS_DIVS##", generateTagsDivs(ir));

    /**
     * File Structure
     */
    String ul = "<ul id='structure'>";
    int index = 0;
    TiffDocument td = ir.getTiffModel();
    IFD ifd = td.getFirstIFD();
    while (ifd != null) {
      String typ = ifd.isThumbnail() ? " - Thumbnail" : " - Main image";
      String aIni = "";
      String aBody = " " + ifd.toString() + (index + 1) + typ;
      String aEnd = "";
      String bold = "";
      if (index == 0) {
        bold = "bold";
      }
      aIni = "<a id='liifd" + index + "' href='javascript:void(0)' onclick=\"showTagsDiv('ifd" + index + "')\" class='" + bold + "'>";
      aEnd = "</a>";
      ul += "<li><i class=\"fa fa-file-image-o\"></i>" + aIni + aBody + aEnd;
      if (ifd.getsubIFD() != null) {
        typ = ifd.getsubIFD().isThumbnail() ? " - Thumbnail" : " - Main image";
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick=\"showTagsDiv('sub" + index + "')\" id='lisub" + index + "'>SubIFD" + typ + "</a></li></ul>";
      }
      if (ifd.containsTagId(34665)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick=\"showTagsDiv('exi" + index + "')\" id='liexi" + index + "'>EXIF</a></li></ul>";
      }
      if (ifd.containsTagId(700)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick=\"showTagsDiv('xmp" + index + "')\" id='lixmp" + index + "'>XMP</a></li></ul>";
      }
      if (ifd.containsTagId(33723)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick=\"showTagsDiv('ipt" + index + "')\" id='liipt" + index + "'>IPTC</a></li></ul>";
      }
      if (index == 0) {
        if (ir.getTiffModel().getIccProfile() != null) {
          String creat = "";
          if (ir.getTiffModel().getIccProfile().getCreator() != null) {
            creat = "<li>Creator: " + ir.getTiffModel().getIccProfile().getCreator().getCreator() + "</li>";
          }
          ul += "<ul><li><i class=\"fa fa-file-o\"></i> ICC<ul>" +
              "<li>Description: " + ir.getTiffModel().getIccProfile().getDescription() + "</li>" +
              creat +
              "<li>Version: " + ir.getTiffModel().getIccProfile().getVersion() + "</li>" +
              "<li>Class: " + ir.getTiffModel().getIccProfile().getProfileClass().toString() + "</li></ul>" +
              "</li></ul>";
        }
      }
      ul += "</li>";
      ifd = ifd.getNextIFD();
      index++;
    }
    ul += "</ul>";
    htmlBody = StringUtils.replace(htmlBody, "##UL##", ul);

    if (!ir.isQuick()) {
      /**
       * Metadata incoherencies
       */
      int nifd = 1;
      String rows = "";
      while (ir.getAuthorTag().containsKey(nifd) || ir.getAuthorIptc().containsKey(nifd) || ir.getAuthorXmp().containsKey(nifd)) {
        rows += detectIncoherency(ir.getAuthorTag().get(nifd), ir.getAuthorIptc().get(nifd), ir.getAuthorXmp().get(nifd), "Author", nifd);
        nifd++;
      }
      if (rows.isEmpty()) {
        rows = "<tr><td class='tcenter'><i style='font-size: 18px;' class=\"fa fa-check-circle\"></i></td><td>No metadata incoherencies found</td></tr>";
      }
      htmlBody = StringUtils.replace(htmlBody, "##SHOW_METADATA##", "block");
      htmlBody = StringUtils.replace(htmlBody, "##META_ROWS##", rows);
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##SHOW_METADATA##", "none");
    }

    /**
     * Finish, write to html file
     */
    htmlBody = StringUtils.replace(htmlBody, "\\.\\./html/", "");
    return htmlBody;
  }

  private String generateTagsDivs(IndividualReport ir) {
    Map<String, Boolean> hasExpert = new HashMap<>();
    Map<String, Boolean> hasDefault = new HashMap<>();
    Map<String, String> tagsMap = new HashMap<>();
    Map<String, String> templates = new HashMap<>();
    String row;

    /**
     * Parse TAGs
     */
    for (ReportTag tag : ir.getTags(false, true)) {
      if (tag.tv.getId() == 700 && !tag.isDefault) {
        String mapId = "xmp" + tag.index;
        String mapIdH = "xmp" + tag.index + "h";
        // XMP
        for (abstractTiffType to : tag.tv.getValue()) {
          XMP xmp = (XMP) to;
          try {
            Metadata metadata = xmp.createMetadata();
            for (String key : metadata.keySet()) {
              row = "<tr class='xmp" + tag.index + "'><td>" + key + "</td><td>" + metadata.getMetadataObject(key).getSchema() + "</td><td>" + metadata.get(key).toString().trim() + "</td></tr>";
              String rows = tagsMap.containsKey(mapId) ? tagsMap.get(mapId) : "";
              tagsMap.put(mapId, rows + row);
            }
            if (xmp.getHistory() != null) {
              for (Hashtable<String, String> kv : xmp.getHistory()) {
                // TODO WORKARROUND
                String key = kv.keySet().iterator().next();
                String value = kv.get(key);
                row = "<tr class='##LINE## xmp" + tag.index + "'><td>##ATTR##</td><td>##VALUE##</td></tr>";
                if (key.equals("action")) {
                  row = row.replace("##LINE##", "line-top");
                } else {
                  row = row.replace("##LINE##", "");
                }
                row = row.replace("##ATTR##", key);
                row = row.replace("##VALUE##", value.replaceAll("<!--", "").replaceAll("-->", ""));
                String rows = tagsMap.containsKey(mapIdH) ? tagsMap.get(mapIdH) : "";
                tagsMap.put(mapIdH, rows + row);
              }
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        continue;
      }
      if (tag.tv.getId() == 34665 && !tag.isDefault) {
        // EXIF
        String mapId = "exi" + tag.index;
        try {
          abstractTiffType obj = tag.tv.getValue().get(0);
          if (obj instanceof IFD) {
            IFD exif = (IFD) obj;
            for (TagValue tv : exif.getTags().getTags()) {
              row = "<tr class='exi" + tag.index + "'><td>##ICON##</td><td class='tcenter'>" + tv.getId() + "</td><td>" + (tv.getName().equals(tv.getId() + "") ? "Private tag" : tv.getName()) + "</td><td>" + tv.getFirstTextReadValue() + "</td></tr>";
              row = row.replace("##ICON##", "<i class=\"image-default icon-" + tv.getName().toLowerCase() + "\"></i>");
              String rows = tagsMap.containsKey(mapId) ? tagsMap.get(mapId) : "";
              tagsMap.put(mapId, rows + row);
            }
          }
        } catch (Exception ex) {
          ex.toString();
          ex.printStackTrace();
        }
        continue;
      }
      if (tag.tv.getId() == 330 && !tag.isDefault) {
        // Sub IFD
        String mapId = "sub" + tag.index;
        if (tag.tv.getValue().size() == 0) continue;
        IFD sub = (IFD) tag.tv.getValue().get(0);
        for (TagValue tv : sub.getTags().getTags()) {
          String expert = "";
          if (!ir.showTag(tv)) {
            expert = "expert";
            hasExpert.put(mapId, true);
          }
          row = "<tr class='sub" + tag.index + " " + expert + "'><td>##ICON##</td><td class='tcenter'>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
          row = row.replace("##ICON##", "<i class=\"image-default icon-" + tv.getName().toLowerCase() + "\"></i>");
          row = row.replace("##ID##", tv.getId() + "");
          row = row.replace("##KEY##", (tv.getName().equals(tv.getId() + "") ? "Private tag" : tv.getName()));
          row = row.replace("##VALUE##", tv.getFirstTextReadValue().replaceAll("<!--", "").replaceAll("-->", ""));
          String rows = tagsMap.containsKey(mapId) ? tagsMap.get(mapId) : "";
          tagsMap.put(mapId, rows + row);
        }
        continue;
      }
      if (tag.tv.getId() == 33723 && !tag.isDefault) {
        String mapId = "ipt" + tag.index;
        // IPTC
        for (abstractTiffType to : tag.tv.getValue()) {
          try {
            IPTC iptc = (IPTC) to;
            Metadata metadata = iptc.createMetadata();
            for (String key : metadata.keySet()) {
              row = "<tr class='ipt" + tag.index + "'><td>" + key + "</td><td>" + metadata.get(key).toString().trim() + "</td></tr>";
              String rows = tagsMap.containsKey(mapId) ? tagsMap.get(mapId) : "";
              tagsMap.put(mapId, rows + row);
            }
          } catch (Exception ex) {
            ex.toString();
            ex.printStackTrace();
          }
        }
        continue;
      }
      // IFD
      String mapId = "ifd" + tag.index;
      String expert = "";
      if (tag.expert) {
        expert = " expert";
        hasExpert.put(mapId, true);
      }
      String defaultVisible = "";
      if (tag.isDefault){
        defaultVisible = " default-value";
        hasDefault.put(mapId, true);
      }
      row = "<tr class='ifd" + tag.index + expert + defaultVisible + " '><td>##ICON##</td><td class='tcenter'>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
      String sDif = "";
      if (tag.dif < 0) sDif = "<i class=\"fa fa-times\"></i>";
      else if (tag.dif > 0) {
        sDif = "<i class=\"fa fa-plus\"></i>";
      }
      row = row.replace("##ICON##", "<i class=\"image-default icon-" + tag.tv.getName().toLowerCase() + "\"></i>");
      row = row.replace("##ID##", tag.tv.getId() + sDif);
      row = row.replace("##KEY##", (tag.tv.getName().equals(tag.tv.getId()) ? "Private tag" : tag.tv.getName()));
      String val = (tag.isDefault) ? tag.defaultValue : tag.tv.getFirstTextReadValue();
      if (val.length() > 200)
        val = val.substring(0, 200) + "...";
      row = row.replace("##VALUE##", val.replaceAll("<!--", "").replaceAll("-->", ""));
      String rows = tagsMap.containsKey(mapId) ? tagsMap.get(mapId) : "";
      tagsMap.put(mapId, rows + row);
    }

    /**
     * Generate divs
     */
    String finalResult = "";
    String genTmpl = "<div id=\"div##INDEX##\" class=\"tags-divs\" style='display: ##DISPLAY##'>\n" +
        "\t\t\t\t\t<h4 class='bold'><i class=\"fa fa-tags\"></i>  ##TITLE##</h4>\n" +
        "\t\t\t\t\t<table class=\"CustomTable3\">\n" +
        "\t\t\t\t        <tr>\n" +
        "\t\t\t\t            <th style=\"width: 40px;\"></th>\n" +
        "\t\t\t\t            <th style=\"width: 70px;\" class=\"bold tcenter\">Tag Id</th>\n" +
        "\t\t\t\t            <th class=\"bold\">Tag Name</th>\n" +
        "\t\t\t\t            <th class=\"bold\">Value</th>\n" +
        "\t\t\t\t        </tr>\n" +
        "\t\t\t\t        ##ROWS##\n" +
        "\t\t\t\t\t</table>\n" +
        "\t\t\t\t</div>";
    String subTmpl = StringUtils.replace(genTmpl, "##TITLE##", "Sub IFD Tags");
    String ifdTmpl = StringUtils.replace(genTmpl, "##TITLE##", "IFD Tags");
    String exifTmpl = StringUtils.replace(genTmpl, "##TITLE##", "EXIF");
    String iptcTmpl = "<div id=\"div##INDEX##\" class=\"tags-divs\" style='display: ##DISPLAY##'>\n" +
        "\t\t\t\t\t<h4 class='bold'><i class=\"fa fa-tags\"></i>  IPTC</h4>\n" +
        "\t\t\t\t\t<table class=\"CustomTable3\">\n" +
        "\t\t\t\t        <tr>\n" +
        "\t\t\t\t            <th class=\"bold\">Name</th>\n" +
        "\t\t\t\t            <th class=\"bold\">Value</th>\n" +
        "\t\t\t\t        </tr>\n" +
        "\t\t\t\t        ##ROWS##\n" +
        "\t\t\t\t\t</table>\n" +
        "\t\t\t\t</div>";
    String xmpTmpl = "<div id=\"div##INDEX##\" class=\"tags-divs\" style='display: ##DISPLAY##'>\n" +
        "\t\t\t\t\t<h4 class='bold'><i class=\"fa fa-tags\"></i>  XMP</h4>\n" +
        "\t\t\t\t\t<table class=\"CustomTable3\">\n" +
        "\t\t\t\t        <tr>\n" +
        "\t\t\t\t            <th class=\"bold\">Name</th>\n" +
        "\t\t\t\t            <th class=\"bold\">Schema</th>\n" +
        "\t\t\t\t            <th class=\"bold\">Value</th>\n" +
        "\t\t\t\t        </tr>\n" +
        "\t\t\t\t        ##ROWS##\n" +
        "\t\t\t\t\t</table>\n" +
        "\t\t\t\t\t<div style='display: ##DISH##;' class='top20'>\n" +
        "\t\t\t\t\t<h4 class='bold'><i class=\"fa fa-history\"></i>  History</h4>\n" +
        "\t\t\t\t\t<table class=\"CustomTable3\">\n" +
        "\t\t\t\t        <tr>\n" +
        "\t\t\t\t            <th class=\"bold\">Attribute</th>\n" +
        "\t\t\t\t            <th class=\"bold\">Value</th>\n" +
        "\t\t\t\t        </tr>\n" +
        "\t\t\t\t        ##ROWSH##\n" +
        "\t\t\t\t\t</table>\n" +
        "\t\t\t\t\t</div>\n" +
        "\t\t\t\t</div>";
    templates.put("ifd", ifdTmpl);
    templates.put("sub", subTmpl);
    templates.put("ipt", iptcTmpl);
    templates.put("xmp", xmpTmpl);
    templates.put("exi", exifTmpl);

    /**
     * Generate HTMLs
     */
    for (String key : tagsMap.keySet()) {
      if (key.endsWith("h")) continue;
      String type = key.substring(0, 3);
      String display = "none;";
      if (key.equals("ifd0")) display = "block;";
      String tmpl = templates.get(type);
      tmpl = StringUtils.replace(tmpl, "##INDEX##", key);
      tmpl = StringUtils.replace(tmpl, "##DISPLAY##", display);
      if (key.startsWith("ipt")) {
        try {
          tmpl = StringUtils.replace(tmpl, "##ROWS##", new String(tagsMap.get(key).getBytes(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
          tmpl = StringUtils.replace(tmpl, "##ROWS##", tagsMap.get(key));
        }
      } else {
        tmpl = StringUtils.replace(tmpl, "##ROWS##", tagsMap.get(key));
      }
      if (type.equals("xmp") && tagsMap.containsKey(key + "h")) {
        tmpl = StringUtils.replace(tmpl, "##ROWSH##", tagsMap.get(key + "h"));
        tmpl = StringUtils.replace(tmpl, "##DISH##", "block");
      } else {
        tmpl = StringUtils.replace(tmpl, "##DISH##", "none");
      }
      finalResult += tmpl;
    }
    return finalResult;
  }

  private String detectIncoherency(String valueTag, String valueIptc, String valueXmp, String name, int nifd) {
    String tmpl = "<tr><td class='tcenter'><i style='font-size: 18px;' class=\"fa fa-times-circle\"></i></td><td>##TEXT##</td></tr>";
    String incoherencies = "";
    if (valueTag != null && valueIptc != null && !valueTag.equals(valueIptc)) {
      if (valueTag.trim().length() == 0)
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on IPTC (" + valueIptc + ") does not match with " + name + " on TAG, which is empty, in IFD " + nifd);
      else if (valueIptc.trim().length() == 0)
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on TAG (" + valueTag + ") does not match with " + name + " on IPTC, which is empty, in IFD " + nifd);
      else
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on TAG (" + valueTag + ") does not match with " + name + " on IPTC (" + valueIptc + ") in IFD " + nifd);
    }
    if (valueTag != null && valueXmp != null && !valueTag.equals(valueXmp)) {
      if (valueTag.trim().length() == 0)
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on XMP field dc:creator (" + valueXmp + ") does not match with " + name + " on TAG, which is empty, in IFD " + nifd);
      else if (valueXmp.trim().length() == 0)
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on TAG (" + valueTag + ") does not match with " + name + " on XMP field dc:creator, which is empty, in IFD " + nifd);
      else
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on TAG (" + valueTag + ") does not match with " + name + " on XMP field dc:creator (" + valueXmp + ") in IFD " + nifd);
    }
    if (valueIptc != null && valueXmp != null && !valueIptc.equals(valueXmp)) {
      if (valueIptc.trim().length() == 0)
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on XMP field dc:creator (" + valueXmp + ") does not match with " + name + " on IPTC, which is empty, in IFD " + nifd);
      else if (valueXmp.trim().length() == 0)
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on IPTC (" + valueIptc + ") does not match with " + name + " on XMP field dc:creator, which is empty, in IFD " + nifd);
      else
        incoherencies += StringUtils.replace(tmpl, "##TEXT##", name + " on IPTC (" + valueIptc + ") does not match with " + name + " on XMP field dc:creator (" + valueXmp + ") in IFD " + nifd);
    }
    return incoherencies;
  }

  private String makePopoverAttributes(RuleResult val) {
    RuleType rule = val.getRule();
    if (rule != null && !rule.getTitle().getValue().isEmpty() && !rule.getDescription().getValue().isEmpty()) {
      String description = rule.getDescription().getValue();
      if (val.getReference() != null) {
        description += "<br><i>" + val.getReference() + "</i>";
      }
      if (rule.getId().equals("RECOMMENDED-TAG-270")) {
        val.toString();
      }
      description = description.replaceAll("\"", "'");
      return "data-toggle=\"popover\" title=\"" + rule.getTitle().getValue() + "\" data-content=\"" + description + "\" data-placement=\"auto bottom\" data-trigger=\"hover\"";
    }
    return "";
  }

  private String makeConformsText(IndividualReport ir, String iso) {
    String tmplPassed = "<div class=\"success\"><i class=\"fa fa-check-circle\"></i> ##TITLE##</div>";
    String tmplError = "<div class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> ##TITLE##</div>";
    String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
    if (ir.hasModifiedIso(iso) && ir.getNErrors(iso) != ir.getNErrorsPolicy(iso)) {
      name += " (with custom policy)";
    }
    int err = ir.hasModifiedIso(iso) ? ir.getNErrorsPolicy(iso) : ir.getNErrors(iso);
    int war = ir.hasModifiedIso(iso) ? ir.getNWarningsPolicy(iso) : ir.getNWarnings(iso);
    if (err == 0 && war == 0) {
      return StringUtils.replace(tmplPassed, "##TITLE##", name);
    } else if (err == 0 && war > 0) {
      return StringUtils.replace(tmplPassed, "##TITLE##", name);
    } else {
      return StringUtils.replace(tmplError, "##TITLE##", name);
    }
  }

  /**
   * Gets the report name of a given tiff file.
   *
   * @param internalReportFolder the internal report folder
   * @param realFilename         the real file name
   * @param idReport             the report id
   * @return the report name
   */
  public static String getReportName(String internalReportFolder, String realFilename, int idReport) {
    String reportName = internalReportFolder + idReport + "-" + new File(realFilename).getName();
    File file = new File(reportName);
    int index = 0;
    while (file.exists()) {
      index++;
      String ext = getFileType(reportName);
      reportName =
          internalReportFolder + idReport + "-"
              + new File(realFilename.substring(0, realFilename.lastIndexOf(".")) + index + "." + ext)
              .getName();
      file = new File(reportName);
    }
    return reportName;
  }

  /**
   * Gets the file type.
   *
   * @param path the path
   * @return the file type
   */
  static String getFileType(String path) {
    String fileType = null;
    fileType = path.substring(path.lastIndexOf('.') + 1).toUpperCase();
    return fileType;
  }

  /**
   * Read filefrom resources string.
   *
   * @param pathStr the path str
   * @return the string
   */
  public String readFilefromResources(String pathStr) {
    String text = "";
    Path path = Paths.get(pathStr);
    try {
      if (Files.exists(path)) {
        // Look in current dir
        BufferedReader br = new BufferedReader(new FileReader(pathStr));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
          sb.append(line);
          sb.append(System.lineSeparator());
          line = br.readLine();
        }
        text = sb.toString();
        br.close();
      } else {
        // Look in JAR
        Class cls = ReportGenerator.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(pathStr);
        if (in != null) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          StringBuilder out = new StringBuilder();
          String newLine = System.getProperty("line.separator");
          String line;
          while ((line = reader.readLine()) != null) {
            out.append(line);
            out.append(newLine);
          }
          text = out.toString();
        }
      }
    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    }

    return text;
  }

  private String encodeUrl(String str) {
    return str.replaceAll("#", "%23");
  }
}
