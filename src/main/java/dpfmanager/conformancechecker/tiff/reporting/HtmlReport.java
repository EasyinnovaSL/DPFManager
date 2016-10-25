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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.model.types.abstractTiffType;

import org.apache.commons.lang.StringUtils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

/**
 * Created by easy on 05/05/2016.
 */
public class HtmlReport extends Report {
  String makeConformDiv(int nerrors, int nwarnings, String key, String html, boolean check, boolean forcecheck) {
    String htmlBody = html;
    if (check || (forcecheck && nerrors + nwarnings == 0)) {
      if (nerrors > 0) {
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_OK##", "none");
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_WAR##", "none");
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_ERR##", "block");
      } else if (nwarnings > 0) {
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_OK##", "none");
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_WAR##", "block");
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_ERR##", "none");
      } else {
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_OK##", "block");
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_WAR##", "none");
        htmlBody = StringUtils.replace(htmlBody, "##" + key + "_ERR##", "none");
      }
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##" + key + "_OK##", "none");
      htmlBody = StringUtils.replace(htmlBody, "##" + key + "_WAR##", "none");
      htmlBody = StringUtils.replace(htmlBody, "##" + key + "_ERR##", "none");
    }
    return htmlBody;
  }

  /**
   * Parse an individual report to HTML.
   *
   * @param ir   the individual report.
   * @param mode the mode (1, 2).
   */
  public String parseIndividual(IndividualReport ir, int mode, int id) {
    String templatePath = "templates/individual.html";

    String htmlBody = readFilefromResources(templatePath);

    // Image
    String fileName = getReportName("", ir.getFilePath(), id);
    String imgPath = "img/" + fileName + ".jpg";
    BufferedImage thumb = tiff2Jpg(ir.getFilePath());
    if (thumb == null) {
      imgPath = "img/noise.jpg";
    }
    htmlBody = StringUtils.replace(htmlBody, "##IMG_PATH##", encodeUrl(imgPath));

    /**
     * Basic info / divs conforms
     */
    htmlBody = StringUtils.replace(htmlBody, "##IMG_NAME##", ir.getFileName());
    htmlBody = StringUtils.replace(htmlBody, "##IMG_FILEPATH##", ir.getFilePath());
    String divs = "";
    for (String iso : ir.getCheckedIsos()) {
      if (ir.hasValidation(iso) || ir.getErrors(iso).isEmpty()) {
        divs += makeConformsText(ir, iso);
      }
    }
    htmlBody = StringUtils.replace(htmlBody, "##DIVS_CONFORMS##", divs);

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
    String rowTmpl = "<tr>\n" +
        "    <td>##TITLE##</td>\n" +
        "    <td class=\"##ERR_CLASS##\">##ERR##</td>\n" +
        "    <td class=\"##WAR_CLASS##\">##WAR##</td>\n" +
        "    </tr>";
    String rows = "";
    for (String iso : ir.getCheckedIsos()) {
      if (ir.hasValidation(iso) || ir.getErrors(iso).isEmpty()) {
        String name = ImplementationCheckerLoader.getIsoName(iso);
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
        rows += row;
      }
    }
    htmlBody = StringUtils.replace(htmlBody, "##TABLE_RESUME_ERRORS##", rows);

    /**
     * Errors / Warnings resume
     */

    String fullTmpl = "<div class=\"row top30 bot50 fullw\">\n" +
        "\t\t\t\t<div class=\"clexpert\"><input type=\"checkbox\" name=\"info\" id=\"checkInfo##COUNT##\" onclick=\"onChangeInfo(##COUNT##)\"> Show infos</div>\n" +
        "\t\t\t\t<div>\n" +
        "\t\t\t\t\t<h4 class=\"left15\"><i class=\"fa fa-check-square-o\"></i>  ##TITLE##</h4>\n" +
        "\t\t\t\t\t##CONTENT##\n" +
        "\t\t\t\t</div>\n" +
        "\t\t\t</div>";
//    String conformTmpl = "<span style=\"margin-left: 20px;\" class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to ##TITLE##</span>";
    String errorsTmpl = "<table class=\"CustomTable3 left15\">\n" +
        "\t\t\t\t        <tr>\n" +
        "\t\t\t\t            <th class=\"bold\" style='width: 60px;'>Type</th>\n" +
        "\t\t\t\t            <th class=\"bold\" style='width: 120px;'>ID</th>\n" +
        "\t\t\t\t            <th class=\"bold\" style='width: 110px;'>Location</th>\n" +
        "\t\t\t\t            <th class=\"bold\">Description</th>\n" +
        "\t\t\t\t        </tr>\n" +
        "\t\t\t\t        ##ROWS##\n" +
        "\t\t\t\t\t</table>";
    String tdTmpl = "<tr ##CLASS## ##DISPLAY## ##POPOVER##><td class=\"bold\"><i style=\"font-size: 18px;\" class=\"fa fa-##FA_CLASS##-circle iconStyle\"/></td><td>##ID##</td><td>##LOC##</td><td>##DESC##</td></tr>";
    rows = "";
    int count = 0;
    for (String iso : ir.getIsosCheck()) {
      if (ir.hasValidation(iso)) {
        ImplementationCheckerObjectType rules = ImplementationCheckerLoader.getRules(iso);
        String name = rules.getTitle();
        String row = fullTmpl;
        int errorsCount = ir.getNErrors(iso);
        int warningsCount = ir.getNWarnings(iso);
        int infosCount = ir.getNInfos(iso);
        String content = "";
//        if (errorsCount + warningsCount == 0) {
//          content += conformTmpl;
//        }
        if (errorsCount + warningsCount + infosCount > 0) {
          content += errorsTmpl;
          String allRows = "";
          // Errors, Warnings and Infos
          for (RuleResult val : ir.getAllRuleResults(iso)) {
            String tdRow = tdTmpl;
            String display = "";
            String clasz = "";
            if (val.getRule().isError() || val.getRule().isCritical()) {
              tdRow = tdRow.replace("##FA_CLASS##", "times");
            } else if (val.getRule().isWarning()) {
              tdRow = tdRow.replace("##FA_CLASS##", "exclamation");
            } else if (val.getRule().isInfo()) {
              tdRow = tdRow.replace("##FA_CLASS##", "info");
              display = "style='display: none;'";
              clasz = "class='info##COUNT##'";
            }
            tdRow = tdRow.replace("##ID##", val.getRule().getId());
            tdRow = tdRow.replace("##LOC##", val.getLocation());
            tdRow = tdRow.replace("##DESC##", val.getDescription());
            tdRow = tdRow.replace("##POPOVER##", makePopoverAttributes(val));
            tdRow = tdRow.replace("##DISPLAY##", display);
            tdRow = tdRow.replace("##CLASS##", clasz);
            allRows += tdRow;
          }
          content = StringUtils.replace(content, "##ROWS##", allRows);
        }
        row = StringUtils.replace(row, "##CONTENT##", content);
        row = StringUtils.replace(row, "##COUNT##", (++count) + "");
        row = StringUtils.replace(row, "##TITLE##", name);
        rows += row;
      }
    }
    htmlBody = StringUtils.replace(htmlBody, "##DIVS_ERRORS##", rows);

    /**
     * Tags list
     */
    String row;
    rows = "";
    for (ReportTag tag : getTags(ir)) {
      if (tag.tv.getId() == 700) {
        // XMP
        for (abstractTiffType to : tag.tv.getValue()) {
          XMP xmp = (XMP) to;
          try {
            Metadata metadata = xmp.createMetadata();
            for (String key : metadata.keySet()) {
              row = "<tr class='xmp xmp" + (tag.index + 1) + "'><td>##ICON##</td><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
              row = row.replace("##ICON##", "<i class=\"icon-" + key.toLowerCase() + "\"></i>");
              row = row.replace("##ID##", "");
              row = row.replace("##KEY##", key);
              row = row.replace("##VALUE##", metadata.get(key).toString().trim());
              rows += row;
            }
            int nh = 1;
            if (xmp.getHistory() != null) {
              for (Hashtable<String, String> kv : xmp.getHistory()) {
                for (String key : kv.keySet()) {
                  row = "<tr class='xmp xmp" + (tag.index + 1) + "'><td>##ICON##</td><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
                  row = row.replace("##ICON##", "<i class=\"icon-xmphist\"></i>");
                  row = row.replace("##ID##", nh + "");
                  row = row.replace("##KEY##", key);
                  row = row.replace("##VALUE##", kv.get(key).toString().trim());
                  rows += row;
                }
                nh++;
              }
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        continue;
      }
      if (tag.tv.getId() == 33723) {
        // IPTC
        for (abstractTiffType to : tag.tv.getValue()) {
          IPTC iptc = (IPTC) to;
          try {
            Metadata metadata = iptc.createMetadata();
            for (String key : metadata.keySet()) {
              row = "<tr class='iptc iptc" + (tag.index + 1) + "'><td>##ICON##</td><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
              row = row.replace("##ICON##", "<i class=\"icon-" + key.toLowerCase() + "\"></i>");
              row = row.replace("##ID##", "");
              row = row.replace("##KEY##", key);
              row = row.replace("##VALUE##", metadata.get(key).toString().trim());
              rows += row;
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        continue;
      }
      if (tag.tv.getId() == 34665) {
        // EXIF
        for (abstractTiffType to : tag.tv.getValue()) {
          IFD exif = (IFD) to;
          try {
            for (TagValue tv : exif.getTags().getTags()) {
              row = "<tr class='exif exif" + (tag.index + 1) + "'><td>##ICON##</td><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
              row = row.replace("##ICON##", "<i class=\"icon-" + tv.getName().toLowerCase() + "\"></i>");
              row = row.replace("##ID##", "");
              row = row.replace("##KEY##", tv.getName());
              row = row.replace("##VALUE##", tv.getDescriptiveValue());
              rows += row;
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        continue;
      }
      String seeTr = "";
      if (tag.index > 0) seeTr = " hide";
      String expert = "";
      if (tag.expert) expert = " expert";
      row = "<tr class='ifd ifd" + tag.index + seeTr + expert + "'><td>##ICON##</td><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
      String sDif = "";
      if (tag.dif < 0) sDif = "<i class=\"fa fa-times\"></i>";
      else if (tag.dif > 0) sDif = "<i class=\"fa fa-plus\"></i>";
      row = row.replace("##ICON##", "<i class=\"icon-" + tag.tv.getName().toLowerCase() + "\"></i>");
      row = row.replace("##ID##", tag.tv.getId() + sDif);
      row = row.replace("##KEY##", tag.tv.getName());
      row = row.replace("##VALUE##", tag.tv.getDescriptiveValue());
      rows += row;
    }
    htmlBody = StringUtils.replace(htmlBody, "##ROWS_TAGS##", rows);

    // File Structure
    String ul = "<ul>";
    int index = 0;
    TiffDocument td = ir.getTiffModel();
    IFD ifd = td.getFirstIFD();
    boolean hasIFDList = false;
    if (ifd != null && ifd.hasNextIFD()) {
      hasIFDList = ifd.hasNextIFD();
    }
    while (ifd != null) {
      String typ = " - Main image";
      if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()) {
        typ = " - Thumbnail";
      }
      String aIni = "";
      String aBody = " " + ifd.toString() + typ;
      String aEnd = "";
      String bold = "";
      if (index == 0) {
        bold = "bold";
      }
      aIni = "<a id='liifd" + index + "' href='javascript:void(0)' onclick='showIfd(" + index + ")' class='liifdlist " + bold + "'>";
      aEnd = "</a>";
      ul += "<li><i class=\"fa fa-file-o\"></i>" + aIni + aBody + aEnd;
      index++;
      if (ifd.getsubIFD() != null) {
        typ = "";
        if (ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = " - Main image";
        else typ = " - Thumbnail";
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> SubIFD" + typ + "</li></ul>";
      }
      if (ifd.containsTagId(34665)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick='showExif(" + index + ")' class='lixmplist'>EXIF</a></li></ul>";
      }
      if (ifd.containsTagId(700)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick='showXmp(" + index + ")' class='liexiflist'>XMP</a></li></ul>";
      }
      if (ifd.containsTagId(33723)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick='showIptc(" + index + ")' class='liiptclist'>IPTC</a></li></ul>";
      }
      if (index == 1) {
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
    }
    ul += "</ul>";
    htmlBody = StringUtils.replace(htmlBody, "##UL##", ul);

    // Metadata incoherencies
    IFD tdifd = td.getFirstIFD();
    int nifd = 1;
    while (tdifd != null) {
      XMP xmp = null;
      IPTC iptc = null;
      if (tdifd.containsTagId(TiffTags.getTagId("XMP")))
        xmp = (XMP) tdifd.getTag("XMP").getValue().get(0);
      if (tdifd.containsTagId(TiffTags.getTagId("IPTC")))
        iptc = (IPTC) tdifd.getTag("IPTC").getValue().get(0);

      // Author
      String authorTag = null;
      if (tdifd.containsTagId(TiffTags.getTagId("Artist")))
        authorTag = tdifd.getTag("Artist").toString();
      String authorIptc = null;
      if (iptc != null) authorIptc = iptc.getCreator();
      String authorXmp = null;
      if (xmp != null) authorXmp = xmp.getCreator();
      htmlBody = DetectIncoherency(authorTag, authorIptc, authorXmp, "Author", nifd, htmlBody);

      tdifd = tdifd.getNextIFD();
      nifd++;
    }

    // Finish, write to html file
    htmlBody = StringUtils.replace(htmlBody, "\\.\\./html/", "");
    return htmlBody;
  }

  private String DetectIncoherency(String valueTag, String valueIptc, String valueXmp, String name, int nifd, String htmlBody) {
    String htmlBodyMod = htmlBody;
    String incoherencies = "";
    if (valueTag != null && valueIptc != null && !valueTag.equals(valueIptc)) {
      incoherencies += "<li>" + name + " on TAG and IPTC in IFD " + nifd + " (" + valueTag + ", " + valueIptc + ")</li>";
    }
    if (valueTag != null && valueXmp != null && !valueTag.equals(valueXmp)) {
      incoherencies += "<li>" + name + " on TAG and XMP in IFD " + nifd + " (" + valueTag + ", " + valueXmp + ")</li>";
    }
    if (valueIptc != null && valueXmp != null && !valueIptc.equals(valueXmp)) {
      incoherencies += "<li>" + name + " on IPTC and XMP in IFD " + nifd + " (" + valueIptc + ", " + valueXmp + ")</li>";
    }
    htmlBodyMod = htmlBodyMod.replace("##INCOHERENCIES##", incoherencies);
    return htmlBodyMod;
  }

  private String makePopoverAttributes(RuleResult val) {
    RuleType rule = val.getRule();
    String description = rule.getDescription().getValue();
    if (val.getReference() != null) {
      description += "<br><i>" + val.getReference() + "</i>";
    }
    return "data-toggle=\"popover\" title=\"" + rule.getTitle().getValue() + "\" data-content=\"" + description + "\" data-placement=\"auto bottom\" data-trigger=\"hover\"";
  }

  private String makeConformsText(IndividualReport ir, String iso) {
    String tmplPassed = "<div class=\"success\"><i class=\"fa fa-check-circle\"></i> This file conforms to ##TITLE##</div>";
    String tmplWarn = "<div class=\"warning\"><i class=\"fa fa-exclamation-triangle\"></i> This file conforms to ##TITLE##, BUT it has some warnings</div>";
    String tmplError = "<div class=\"error\"><i class=\"fa fa-exclamation-triangle\"></i> This file does NOT conform to ##TITLE##</div>";
    String name = ImplementationCheckerLoader.getIsoName(iso);
    int err = ir.getNErrors(iso);
    int war = ir.getNWarnings(iso);
    if (err == 0 && war == 0) {
      return StringUtils.replace(tmplPassed, "##TITLE##", name);
    } else if (err == 0 && war > 0) {
      return StringUtils.replace(tmplWarn, "##TITLE##", name);
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
