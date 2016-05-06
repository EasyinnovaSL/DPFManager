package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.util.ReportTag;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;

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
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Created by easy on 05/05/2016.
 */
public class HtmlReport extends Report {
  /**
   * Parse an individual report to HTML.
   *
   * @param ir         the individual report.
   * @param mode       the mode (1, 2).
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
    htmlBody = StringUtils.replace(htmlBody, "##IMG_PATH##", imgPath);

    // Basic info
    htmlBody = StringUtils.replace(htmlBody, "##IMG_NAME##", ir.getFileName());
    htmlBody = StringUtils.replace(htmlBody, "##IMG_FILEPATH##", ir.getFilePath());
    int epErr = ir.getNEpErr(), epWar = ir.getNEpWar();
    int blErr = ir.getNBlErr(), blWar = ir.getNBlWar();
    int it0Err = ir.getNItErr(0), it0War = ir.getNItWar(0);
    int it1Err = ir.getNItErr(1), it1War = ir.getNItWar(1);
    int it2Err = ir.getNItErr(2), it2War = ir.getNItWar(2);
    List<RuleResult> pcValidation = ir.getPcValidation();
    int pcErr = ir.getPCErrors().size(), pcWar = ir.getPCWarnings().size();

    // Global result
    if (blErr + epErr + it0Err + it1Err + it2Err + pcErr > 0) {
      htmlBody = StringUtils.replace(htmlBody, "##ALL_OK##", "none");
      htmlBody = StringUtils.replace(htmlBody, "##ALL_WAR##", "none");
      htmlBody = StringUtils.replace(htmlBody, "##ALL_ERR##", "block");
    } else if (blWar + epWar + it0War + it1War + it2War + pcWar > 0) {
      htmlBody = StringUtils.replace(htmlBody, "##ALL_OK##", "none");
      htmlBody = StringUtils.replace(htmlBody, "##ALL_WAR##", "block");
      htmlBody = StringUtils.replace(htmlBody, "##ALL_ERR##", "none");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ALL_OK##", "block");
      htmlBody = StringUtils.replace(htmlBody, "##ALL_WAR##", "none");
      htmlBody = StringUtils.replace(htmlBody, "##ALL_ERR##", "none");
    }

    if (mode == 1) {
      htmlBody = StringUtils.replace(htmlBody, "##CL_LINKR2##", "show");
      htmlBody = StringUtils.replace(htmlBody, "##LINK2##", new File(fileName).getName() + "_fixed.html");
    }
    if (mode == 2) {
      htmlBody = StringUtils.replace(htmlBody, "##CL_LINKR1##", "show");
      htmlBody = StringUtils.replace(htmlBody, "##LINK1##", new File(fileName).getName() + ".html");
    }

    String dif;

    // Policy checker
    if (pcErr > 0) {
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_ERR_CLASS##", "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_ERR_CLASS##", "info");
    }
    if (pcWar > 0) {
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_WAR_CLASS##", "warning");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_WAR_CLASS##", "info");
    }

    dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCErrors().size(), ir.getPCErrors().size()) : "";
    htmlBody = StringUtils.replace(htmlBody, "##F_PC_ERR##", "" + ir.getPCErrors().size() + dif);
    dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCWarnings().size(), ir.getPCWarnings().size()) : "";
    htmlBody = StringUtils.replace(htmlBody, "##F_PC_WAR##", "" + ir.getPCWarnings().size() + dif);

    if (ir.hasBlValidation()) {
      htmlBody = StringUtils.replace(htmlBody, "##F_BL_ERR_CLASS##", ir.getBaselineErrors().size() > 0 ? "error" : "info");
      htmlBody = StringUtils.replace(htmlBody, "##F_BL_WAR_CLASS##", ir.getBaselineWarnings().size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlErr(), blErr) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_BL_ERR##", "" + ir.getBaselineErrors().size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlWar(), blWar) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_BL_WAR##", "" + ir.getBaselineWarnings().size() + dif);
      htmlBody = StringUtils.replace(htmlBody, "##BL_OK##", ir.getBaselineErrors().size() > 0 ? "none" : "block");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_BL##", "hide");
    }

    if (ir.hasEpValidation()) {
      htmlBody = StringUtils.replace(htmlBody, "##F_EP_ERR_CLASS##", ir.getEPErrors().size() > 0 ? "error" : "info");
      htmlBody = StringUtils.replace(htmlBody, "##F_EP_WAR_CLASS##", ir.getEPWarnings().size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpErr(), epErr) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_EP_ERR##", "" + ir.getEPErrors().size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpWar(), epWar) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_EP_WAR##", "" + ir.getEPWarnings().size() + dif);
      htmlBody = StringUtils.replace(htmlBody, "##EP_OK##", ir.getEPErrors().size() > 0 ? "none" : "block");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_EP##", "hide");
    }

    if (ir.hasItValidation(0)) {
      htmlBody = StringUtils.replace(htmlBody, "##F_IT_ERR_CLASS##", ir.getITErrors(0).size() > 0 ? "error" : "info");
      htmlBody = StringUtils.replace(htmlBody, "##F_IT_WAR_CLASS##", ir.getITWarnings(0).size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(0), it0Err) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_IT_ERR##", "" + ir.getITErrors(0).size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(0), it0War) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_IT_WAR##", "" + ir.getITWarnings(0).size() + dif);
      htmlBody = StringUtils.replace(htmlBody, "##IT_OK##", ir.getITErrors(0).size() > 0 ? "none" : "block");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT##", "hide");
    }

    if (ir.hasItValidation(1)) {
      htmlBody = StringUtils.replace(htmlBody, "##F_IT1_ERR_CLASS##", ir.getITErrors(1).size() > 0 ? "error" : "info");
      htmlBody = StringUtils.replace(htmlBody, "##F_IT1_WAR_CLASS##", ir.getITWarnings(1).size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(1), it1Err) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_IT1_ERR##", "" + ir.getITErrors(1).size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(1), it1War) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_IT1_WAR##", "" + ir.getITWarnings(1).size() + dif);
      htmlBody = StringUtils.replace(htmlBody, "##IT1_OK##", ir.getITErrors(1).size() > 0 ? "none" : "block");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT1##", "hide");
    }

    if (ir.hasItValidation(2)) {
      htmlBody = StringUtils.replace(htmlBody, "##F_IT2_ERR_CLASS##", ir.getITErrors(2).size() > 0 ? "error" : "info");
      htmlBody = StringUtils.replace(htmlBody, "##F_IT2_WAR_CLASS##", ir.getITWarnings(2).size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(2), it2Err) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_IT2_ERR##", "" + ir.getITErrors(2).size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(2), it2War) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_IT2_WAR##", "" + ir.getITWarnings(2).size() + dif);
      htmlBody = StringUtils.replace(htmlBody, "##IT2_OK##", ir.getITErrors(2).size() > 0 ? "none" : "block");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT2##", "hide");
    }

    if (ir.hasPcValidation()) {
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_ERR_CLASS##", ir.getPCErrors().size() > 0 ? "error" : "info");
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_WAR_CLASS##", ir.getPCWarnings().size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCErrors().size(), pcErr) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_ERR##", "" + ir.getPCErrors().size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCWarnings().size(), pcWar) : "";
      htmlBody = StringUtils.replace(htmlBody, "##F_PC_WAR##", "" + ir.getPCWarnings().size() + dif);
      htmlBody = StringUtils.replace(htmlBody, "##PC_OK##", ir.getPCErrors().size() > 0 ? "none" : "block");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_PC##", "hide");
    }


    // Errors and warnings

    // EP
    String row;
    String rows = "";

    if (ir.checkEP) {
      if (ir.getEPErrors() != null) {
        for (RuleResult val : ir.getEPErrors()) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getEPWarnings() != null) {
        for (RuleResult val : ir.getEPWarnings()) {
          row = "<tr><td class=\"bold warning\">Warning</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }
      htmlBody = StringUtils.replace(htmlBody, "##ROWS_EP##", rows);
      htmlBody = StringUtils.replace(htmlBody, "##EP_VISIBLE##", "");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##EP_VISIBLE##", "hidden");
    }

    // Baseline
    rows = "";
    if (ir.checkBL) {
      if (ir.getBaselineErrors() != null) {
        for (RuleResult val : ir.getBaselineErrors()) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getBaselineWarnings() != null) {
        for (RuleResult val : ir.getBaselineWarnings()) {
          row = "<tr><td class=\"bold warning\">Warning</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }
      htmlBody = StringUtils.replace(htmlBody, "##ROWS_BL##", rows);
      htmlBody = StringUtils.replace(htmlBody, "##BL_VISIBLE##", "");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##BL_VISIBLE##", "hidden");
    }

    // IT
    rows = "";
    if (ir.checkIT0) {
      if (ir.getITErrors(0) != null) {
        for (RuleResult val : ir.getITErrors(0)) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getITWarnings(0) != null) {
        for (RuleResult val : ir.getITWarnings(0)) {
          row = "<tr><td class=\"bold warning\">Warning</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }
      htmlBody = StringUtils.replace(htmlBody, "##ROWS_IT##", rows);
      htmlBody = StringUtils.replace(htmlBody, "##IT_VISIBLE##", "");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##IT_VISIBLE##", "hidden");
    }

    // IT-1
    rows = "";
    if (ir.checkIT1) {
      if (ir.getITErrors(1) != null) {
        for (RuleResult val : ir.getITErrors(1)) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getITWarnings(1) != null) {
        for (RuleResult val : ir.getITWarnings(1)) {
          row = "<tr><td class=\"bold warning\">Warning</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }
      htmlBody = StringUtils.replace(htmlBody, "##ROWS_IT1##", rows);
      htmlBody = StringUtils.replace(htmlBody, "##IT1_VISIBLE##", "");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##IT1_VISIBLE##", "hidden");
    }

    // IT-2
    rows = "";
    if (ir.checkIT2) {
      if (ir.getITErrors(2) != null) {
        for (RuleResult val : ir.getITErrors(2)) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getITWarnings(2) != null) {
        for (RuleResult val : ir.getITWarnings(2)) {
          row = "<tr><td class=\"bold warning\">Warning</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }
      htmlBody = StringUtils.replace(htmlBody, "##ROWS_IT2##", rows);
      htmlBody = StringUtils.replace(htmlBody, "##IT2_VISIBLE##", "");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##IT2_VISIBLE##", "hidden");
    }

    // PC
    rows = "";
    if (ir.checkPC) {
      for (RuleResult val : ir.getPCErrors()) {
        row = "<tr><td class=\"bold error\">Error</td><td>##TEXT##</td></tr>";
        row = row.replace("##TEXT##", val.getDescription());
        rows += row;
      }

      for (RuleResult val : ir.getPCWarnings()) {
        row = "<tr><td class=\"bold warning\">Warning</td><td>##TEXT##</td></tr>";
        row = row.replace("##TEXT##", val.getDescription());
        rows += row;
      }
      htmlBody = StringUtils.replace(htmlBody, "##ROWS_PC##", rows);
      htmlBody = StringUtils.replace(htmlBody, "##PC_VISIBLE##", "");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##PC_VISIBLE##", "hidden");
    }

    // Tags list
    rows = "";
    for (ReportTag tag : getTags(ir)) {
      String seeTr = "";
      if (tag.index > 0) seeTr = " hide";
      String expert = "";
      if (tag.expert) expert = " expert";
      row = "<tr class='ifd ifd" + tag.index + seeTr + expert + "'><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
      String sDif = "";
      if (tag.dif < 0) sDif = "<i class=\"fa fa-times\"></i>";
      else if (tag.dif > 0) sDif = "<i class=\"fa fa-plus\"></i>";
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
      if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()){
        typ = " - Thumbnail";
      }
      String aIni = "";
      String aBody = " " + ifd.toString() + typ;
      String aEnd = "";
      if (hasIFDList){
        String bold = "";
        if (index == 0){
          bold = "bold";
        }
        aIni = "<a id='liifd" + index + "' href='javascript:void(0)' onclick='showIfd(" + index + ")' class='liifdlist " + bold + "'>";
        aEnd = "</a>";
      }
      ul += "<li><i class=\"fa fa-file-o\"></i>" + aIni + aBody + aEnd;
      index++;
      if (ifd.getsubIFD() != null) {
        typ="";
        if (ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = " - Main image";
        else typ = " - Thumbnail";
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> SubIFD"+typ+"</li></ul>";
      }
      if (ifd.containsTagId(34665)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> EXIF</li></ul>";
      }
      if (ifd.containsTagId(700)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> XMP</li></ul>";
      }
      if (ifd.containsTagId(33723)) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> IPTC</li></ul>";
      }
      ul += "</li>";
      ifd = ifd.getNextIFD();
    }
    ul += "</ul>";
    htmlBody = StringUtils.replace(htmlBody, "##UL##", ul);

    // Finish, write to html file
    htmlBody = StringUtils.replace(htmlBody, "\\.\\./html/", "");
    return htmlBody;
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
}
