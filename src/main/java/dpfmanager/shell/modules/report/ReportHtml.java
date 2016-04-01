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

package dpfmanager.shell.modules.report;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;

import java.io.File;

/**
 * The Class ReportHtml.
 */
public class ReportHtml extends ReportGeneric {

  /**
   * Parse an individual report to HTML.
   *
   * @param outputfile the outputfile
   * @param ir         the individual report.
   * @param mode       the mode (1, 2).
   */
  public static void parseIndividual(String outputfile, IndividualReport ir, int mode) {
    //String templatePath = "./src/main/resources/templates/individual.html";
    String templatePath = "templates/individual.html";
    String name = outputfile.substring(outputfile.lastIndexOf("/") + 1, outputfile.length());
    outputfile = outputfile.replace(name, "html/" + name);
    String newHtmlFolder = outputfile.substring(0, outputfile.lastIndexOf("/"));

    //String htmlBody = ReportGenerator.readFile(templatePath);
    String htmlBody = ReportGenerator.readFilefromResources(templatePath);

    // Image
    String imgPath = "img/" + new File(ir.getReportPath()).getName() + ".jpg";
    boolean check = tiff2Jpg(ir.getFilePath(), newHtmlFolder + "/" + imgPath);
    if (!check) {
      imgPath = "img/noise.jpg";
    }
    htmlBody = htmlBody.replace("##IMG_PATH##", imgPath);

    // Basic info
    htmlBody = htmlBody.replace("##IMG_NAME##", ir.getFileName());
    htmlBody = htmlBody.replace("##IMG_FILEPATH##", ir.getFilePath());
    int epErr = ir.getNEpErr(), epWar = ir.getNEpWar();
    int blErr = ir.getNBlErr(), blWar = ir.getNBlWar();
    int it0Err = ir.getNItErr(0), it0War = ir.getNItWar(0);
    int it1Err = ir.getNItErr(1), it1War = ir.getNItWar(1);
    int it2Err = ir.getNItErr(2), it2War = ir.getNItWar(2);
    ValidationResult pcValidation = ir.getPcValidation();
    int pcErr = pcValidation.getErrors().size(), pcWar = pcValidation.getWarnings().size();

    // Global result
    if (blErr + epErr + it0Err + it1Err + it2Err + pcErr > 0) {
      htmlBody = htmlBody.replaceAll("##ALL_OK##", "none");
      htmlBody = htmlBody.replaceAll("##ALL_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##ALL_ERR##", "block");
    } else if (blWar + epWar + it0War + it1War + it2War + pcWar > 0) {
      htmlBody = htmlBody.replaceAll("##ALL_OK##", "none");
      htmlBody = htmlBody.replaceAll("##ALL_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##ALL_ERR##", "none");
    } else {
      htmlBody = htmlBody.replaceAll("##ALL_OK##", "block");
      htmlBody = htmlBody.replaceAll("##ALL_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##ALL_ERR##", "none");
    }

    if (blErr > 0) {
      htmlBody = htmlBody.replaceAll("##BL_OK##", "none");
      htmlBody = htmlBody.replaceAll("##BL_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##BL_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##BL_ERR-WAR##", "");
    } else if (blWar > 0) {
      htmlBody = htmlBody.replaceAll("##BL_OK##", "none");
      htmlBody = htmlBody.replaceAll("##BL_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##BL_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##BL_ERR-WAR##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##BL_OK##", "block");
      htmlBody = htmlBody.replaceAll("##BL_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##BL_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##BL_ERR-WAR##", "display: none;");
    }
    if (epErr > 0) {
      htmlBody = htmlBody.replaceAll("##EP_OK##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##EP_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR-WAR##", "");
    } else if (epWar > 0) {
      htmlBody = htmlBody.replaceAll("##EP_OK##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##EP_ERR-WAR##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##EP_OK##", "block");
      htmlBody = htmlBody.replaceAll("##EP_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR-WAR##", "display: none;");
    }
    if (it0Err > 0) {
      htmlBody = htmlBody.replaceAll("##IT_OK##", "none");
      htmlBody = htmlBody.replaceAll("##IT_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##IT_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##IT_ERR-WAR##", "");
    } else if (it0War > 0) {
      htmlBody = htmlBody.replaceAll("##IT_OK##", "none");
      htmlBody = htmlBody.replaceAll("##IT_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##IT_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##IT_ERR-WAR##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##IT_OK##", "block");
      htmlBody = htmlBody.replaceAll("##IT_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##IT_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##IT_ERR-WAR##", "display: none;");
    }
    if (it1Err > 0) {
      htmlBody = htmlBody.replaceAll("##IT1_OK##", "none");
      htmlBody = htmlBody.replaceAll("##IT1_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##IT1_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##IT1_ERR-WAR##", "");
    } else if (it1War > 0) {
      htmlBody = htmlBody.replaceAll("##IT1_OK##", "none");
      htmlBody = htmlBody.replaceAll("##IT1_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##IT1_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##IT1_ERR-WAR##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##IT1_OK##", "block");
      htmlBody = htmlBody.replaceAll("##IT1_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##IT1_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##IT1_ERR-WAR##", "display: none;");
    }
    if (it2Err > 0) {
      htmlBody = htmlBody.replaceAll("##IT2_OK##", "none");
      htmlBody = htmlBody.replaceAll("##IT2_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##IT2_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##IT2_ERR-WAR##", "");
    } else if (it2War > 0) {
      htmlBody = htmlBody.replaceAll("##IT2_OK##", "none");
      htmlBody = htmlBody.replaceAll("##IT2_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##IT2_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##IT2_ERR-WAR##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##IT2_OK##", "block");
      htmlBody = htmlBody.replaceAll("##IT2_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##IT2_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##IT2_ERR-WAR##", "display: none;");
    }

    if (pcErr > 0) {
      htmlBody = htmlBody.replaceAll("##PC_OK##", "none");
      htmlBody = htmlBody.replaceAll("##PC_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##PC_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##PC_ERR-WAR##", "");
    } else if (pcWar > 0) {
      htmlBody = htmlBody.replaceAll("##PC_OK##", "none");
      htmlBody = htmlBody.replaceAll("##PC_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##PC_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##PC_ERR-WAR##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##PC_OK##", "block");
      htmlBody = htmlBody.replaceAll("##PC_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##PC_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##PC_ERR-WAR##", "display: none;");
    }

    if (mode == 1) {
      htmlBody = htmlBody.replaceAll("##CL_LINKR2##", "show");
      htmlBody = htmlBody.replaceAll("##LINK2##", new File(ir.getReportPath()).getName() + "_fixed.html");
    }
    if (mode == 2) {
      htmlBody = htmlBody.replaceAll("##CL_LINKR1##", "show");
      htmlBody = htmlBody.replaceAll("##LINK1##", new File(ir.getCompareReport().getReportPath()).getName() + ".html");
    }

    String dif;

    // Policy checker
    if (pcErr > 0) {
      htmlBody = htmlBody.replaceAll("##F_PC_ERR_CLASS##", "error");
    } else {
      htmlBody = htmlBody.replaceAll("##F_PC_ERR_CLASS##", "info");
    }
    if (pcWar > 0) {
      htmlBody = htmlBody.replaceAll("##F_PC_WAR_CLASS##", "warning");
    } else {
      htmlBody = htmlBody.replaceAll("##F_PC_WAR_CLASS##", "info");
    }

    dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPcValidation().getErrors().size(), pcValidation.getErrors().size()) : "";
    htmlBody = htmlBody.replaceAll("##F_PC_ERR##", "" + pcValidation.getErrors().size() + dif);
    dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPcValidation().getWarnings().size(), pcValidation.getWarnings().size()) : "";
    htmlBody = htmlBody.replaceAll("##F_PC_WAR##", "" + pcValidation.getWarnings().size() + dif);

    if (ir.hasBlValidation()) {
      htmlBody = htmlBody.replaceAll("##F_BL_ERR_CLASS##", ir.getBaselineErrors().size() > 0 ? "error" : "info");
      htmlBody = htmlBody.replaceAll("##F_BL_WAR_CLASS##", ir.getBaselineWarnings().size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlErr(), blErr) : "";
      htmlBody = htmlBody.replaceAll("##F_BL_ERR##", "" + ir.getBaselineErrors().size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlWar(), blWar) : "";
      htmlBody = htmlBody.replaceAll("##F_BL_WAR##", "" + ir.getBaselineWarnings().size() + dif);
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_BL##", "hide");
    }

    if (ir.hasEpValidation()) {
      htmlBody = htmlBody.replaceAll("##F_EP_ERR_CLASS##", ir.getEPErrors().size() > 0 ? "error" : "info");
      htmlBody = htmlBody.replaceAll("##F_EP_WAR_CLASS##", ir.getEPWarnings().size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpErr(), epErr) : "";
      htmlBody = htmlBody.replaceAll("##F_EP_ERR##", "" + ir.getEPErrors().size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpWar(), epWar) : "";
      htmlBody = htmlBody.replaceAll("##F_EP_WAR##", "" + ir.getEPWarnings().size() + dif);
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_EP##", "hide");
    }

    if (ir.hasItValidation(0)) {
      htmlBody = htmlBody.replaceAll("##F_IT_ERR_CLASS##", ir.getITErrors(0).size() > 0 ? "error" : "info");
      htmlBody = htmlBody.replaceAll("##F_IT_WAR_CLASS##", ir.getITWarnings(0).size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(0), it0Err) : "";
      htmlBody = htmlBody.replaceAll("##F_IT_ERR##", "" + ir.getITErrors(0).size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(0), it0War) : "";
      htmlBody = htmlBody.replaceAll("##F_IT_WAR##", "" + ir.getITWarnings(0).size() + dif);
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT##", "hide");
    }

    if (ir.hasItValidation(1)) {
      htmlBody = htmlBody.replaceAll("##F_IT1_ERR_CLASS##", ir.getITErrors(1).size() > 0 ? "error" : "info");
      htmlBody = htmlBody.replaceAll("##F_IT1_WAR_CLASS##", ir.getITWarnings(1).size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(1), it1Err) : "";
      htmlBody = htmlBody.replaceAll("##F_IT1_ERR##", "" + ir.getITErrors(1).size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(1), it1War) : "";
      htmlBody = htmlBody.replaceAll("##F_IT1_WAR##", "" + ir.getITWarnings(1).size() + dif);
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT1##", "hide");
    }

    if (ir.hasItValidation(2)) {
      htmlBody = htmlBody.replaceAll("##F_IT2_ERR_CLASS##", ir.getITErrors(2).size() > 0 ? "error" : "info");
      htmlBody = htmlBody.replaceAll("##F_IT2_WAR_CLASS##", ir.getITWarnings(2).size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(2), it2Err) : "";
      htmlBody = htmlBody.replaceAll("##F_IT2_ERR##", "" + ir.getITErrors(2).size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(2), it2War) : "";
      htmlBody = htmlBody.replaceAll("##F_IT2_WAR##", "" + ir.getITWarnings(2).size() + dif);
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT2##", "hide");
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
      htmlBody = htmlBody.replaceAll("##ROWS_EP##", rows);
      htmlBody = htmlBody.replaceAll("##EP_VISIBLE##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##EP_VISIBLE##", "hidden");
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
      htmlBody = htmlBody.replaceAll("##ROWS_BL##", rows);
      htmlBody = htmlBody.replaceAll("##BL_VISIBLE##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##BL_VISIBLE##", "hidden");
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
      htmlBody = htmlBody.replaceAll("##ROWS_IT##", rows);
      htmlBody = htmlBody.replaceAll("##IT_VISIBLE##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##IT_VISIBLE##", "hidden");
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
      htmlBody = htmlBody.replaceAll("##ROWS_IT1##", rows);
      htmlBody = htmlBody.replaceAll("##IT1_VISIBLE##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##IT1_VISIBLE##", "hidden");
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
      htmlBody = htmlBody.replaceAll("##ROWS_IT2##", rows);
      htmlBody = htmlBody.replaceAll("##IT2_VISIBLE##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##IT2_VISIBLE##", "hidden");
    }

    // PC
    rows = "";
    if (ir.checkPC) {
      for (ValidationEvent val : pcValidation.getErrors()) {
        row = "<tr><td class=\"bold error\">Error</td><td>##TEXT##</td></tr>";
        row = row.replace("##TEXT##", val.getDescription());
        rows += row;
      }

      for (ValidationEvent val : pcValidation.getWarnings()) {
        row = "<tr><td class=\"bold warning\">Warning</td><td>##TEXT##</td></tr>";
        row = row.replace("##TEXT##", val.getDescription());
        rows += row;
      }
      htmlBody = htmlBody.replaceAll("##ROWS_PC##", rows);
      htmlBody = htmlBody.replaceAll("##PC_VISIBLE##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##PC_VISIBLE##", "hidden");
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
    htmlBody = htmlBody.replaceAll("##ROWS_TAGS##", rows);

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
    htmlBody = htmlBody.replaceAll("##UL##", ul);

    // Finish, write to html file
    htmlBody = htmlBody.replaceAll("\\.\\./html/", "");
    ReportGenerator.writeToFile(outputfile, htmlBody);
  }

  /**
   * Parse a global report to XML format.
   *
   * @param outputfile the output file.
   * @param gr         the global report.
   */
  public static void parseGlobal(String outputfile, GlobalReport gr) {
    String templatePath = "templates/global.html";
    String imagePath = "templates/image.html";
    String newHtmlFolder = outputfile.substring(0, outputfile.lastIndexOf("/"));

    String imagesBody = "";
    String pieFunctions = "";

    // Parse individual Reports
    int index = 0;
    for (IndividualReport ir : gr.getIndividualReports()) {
      if (!ir.containsData()) continue;
      String imageBody;
      imageBody = ReportGenerator.readFilefromResources(imagePath);
      // Image
      String imgPath = "html/img/" + new File(ir.getReportPath()).getName() + ".jpg";
      boolean check = tiff2Jpg(ir.getFilePath(), newHtmlFolder + "/" + imgPath);
      if (!check) {
        imgPath = "html/img/noise.jpg";
      }
      imageBody = imageBody.replace("##IMG_PATH##", imgPath);

      // Basic
      int percent = ir.calculatePercent();
      imageBody = imageBody.replace("##PERCENT##", "" + percent);
      imageBody = imageBody.replace("##INDEX##", "" + index);
      imageBody = imageBody.replace("##IMG_NAME##", "" + ir.getFileName());

      if (ir.hasEpValidation()) {
        imageBody = imageBody.replace("##EP_ERR_N##", "" + ir.getEPErrors().size());
        imageBody = imageBody.replace("##EP_WAR_N##", "" + ir.getEPWarnings().size());
      } else {
        imageBody = imageBody.replace("##EP_CLASS##", "hide");
      }

      if (ir.hasBlValidation()) {
        imageBody = imageBody.replace("##BL_ERR_N##", "" + ir.getBaselineErrors().size());
        imageBody = imageBody.replace("##BL_WAR_N##", "" + ir.getBaselineWarnings().size());
      } else {
        imageBody = imageBody.replace("##BL_CLASS##", "hide");
      }

      if (ir.hasItValidation(0)) {
        imageBody = imageBody.replace("##IT_ERR_N##", "" + ir.getITErrors(0).size());
        imageBody = imageBody.replace("##IT_WAR_N##", "" + ir.getITWarnings(0).size());
      } else {
        imageBody = imageBody.replace("##IT_CLASS##", "hide");
      }

      if (ir.hasItValidation(1)) {
        imageBody = imageBody.replace("##IT1_ERR_N##", "" + ir.getITErrors(1).size());
        imageBody = imageBody.replace("##IT1_WAR_N##", "" + ir.getITWarnings(1).size());
      } else {
        imageBody = imageBody.replace("##IT1_CLASS##", "hide");
      }

      if (ir.hasItValidation(2)) {
        imageBody = imageBody.replace("##IT2_ERR_N##", "" + ir.getITErrors(2).size());
        imageBody = imageBody.replace("##IT2_WAR_N##", "" + ir.getITWarnings(2).size());
      } else {
        imageBody = imageBody.replace("##IT2_CLASS##", "hide");
      }

      if (ir.checkPC) {
        imageBody = imageBody.replace("##PC_ERR_N##", "" + ir.getPCErrors().size());
        imageBody = imageBody.replace("##PC_WAR_N##", "" + ir.getPCWarnings().size());
      } else {
        imageBody = imageBody.replace("##PC_CLASS##", "hide");
      }

      imageBody = imageBody.replace("##HREF##", "html/" + new File(ir.getReportPath()).getName() + ".html");
      if (ir.getBaselineErrors().size() > 0) {
        imageBody = imageBody.replace("##BL_ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##BL_ERR_C##", "");
      }
      if (ir.getBaselineWarnings().size() > 0) {
        imageBody = imageBody.replace("##BL_WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##BL_WAR_C##", "");
      }
      if (ir.getEPErrors().size() > 0) {
        imageBody = imageBody.replace("##EP_ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##EP_ERR_C##", "");
      }
      if (ir.getEPWarnings().size() > 0) {
        imageBody = imageBody.replace("##EP_WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##EP_WAR_C##", "");
      }
      if (ir.getITErrors(0).size() > 0) {
        imageBody = imageBody.replace("##IT_ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##IT_ERR_C##", "");
      }
      if (ir.getITWarnings(0).size() > 0) {
        imageBody = imageBody.replace("##IT_WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##IT_WAR_C##", "");
      }
      if (ir.getITErrors(1).size() > 0) {
        imageBody = imageBody.replace("##IT1_ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##IT1_ERR_C##", "");
      }
      if (ir.getITWarnings(1).size() > 0) {
        imageBody = imageBody.replace("##IT1_WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##IT1_WAR_C##", "");
      }
      if (ir.getITErrors(2).size() > 0) {
        imageBody = imageBody.replace("##IT2_ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##IT2_ERR_C##", "");
      }
      if (ir.getITWarnings(2).size() > 0) {
        imageBody = imageBody.replace("##IT2_WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##IT2_WAR_C##", "");
      }

      if (ir.getPCErrors().size() > 0) {
        imageBody = imageBody.replace("##PC_ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##PC_ERR_C##", "");
      }
      if (ir.getPCWarnings().size() > 0) {
        imageBody = imageBody.replace("##PC_WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##PC_WAR_C##", "");
      }

      // Percent Info
      if (percent == 100) {
        imageBody = imageBody.replace("##CLASS##", "success");
        imageBody = imageBody.replace("##RESULT##", "Passed");
      } else {
        imageBody = imageBody.replace("##CLASS##", "error");
        imageBody = imageBody.replace("##RESULT##", "Failed");
      }
      if (ir.getEPWarnings().size() > 0) {
        imageBody = imageBody.replace("##DISPLAY_WAR##", "inline-block");
      } else {
        imageBody = imageBody.replace("##DISPLAY_WAR##", "none");
      }

      // Percent Chart
      int angle = percent * 360 / 100;
      int reverseAngle = 360 - angle;
      String functionPie = "plotPie('pie-" + index + "', " + angle + ", " + reverseAngle;
      if (percent < 100) {
        functionPie += ", '#CCCCCC', 'red'); ";
      } else {
        functionPie += ", '#66CC66', '#66CC66'); ";
      }
      pieFunctions += functionPie;

      imagesBody += imageBody;
      index++;
    }

    // Parse the sumary report numbers
    String htmlBody;
    htmlBody = ReportGenerator.readFilefromResources(templatePath);
    Double doub = 1.0 * gr.getReportsOk() / gr.getReportsCount() * 100.0;
    int globalPercent = doub.intValue();
    htmlBody = htmlBody.replace("##IMAGES_LIST##", imagesBody);
    htmlBody = htmlBody.replace("##PERCENT##", "" + globalPercent);
    String scount = gr.getReportsCount() + " ";
    if (gr.getReportsCount() == 1) scount += "file";
    else scount += "files";
    htmlBody = htmlBody.replace("##COUNT##", "" + scount);
    htmlBody = htmlBody.replaceAll("##OK##", "" + gr.getReportsOk());

    if (gr.getHasBl()){
      htmlBody = htmlBody.replaceAll("##BL_OK##", "" + gr.getReportsBl());
      htmlBody = htmlBody.replaceAll("##BL_TYP##", gr.getReportsBl() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_BL##", "hide");
    }

    if (gr.getHasEp()){
      htmlBody = htmlBody.replaceAll("##EP_OK##", "" + gr.getReportsEp());
      htmlBody = htmlBody.replaceAll("##EP_TYP##", gr.getReportsEp() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_EP##", "hide");
    }

    if (gr.getHasIt0()){
      htmlBody = htmlBody.replaceAll("##IT_OK##", "" + gr.getReportsIt0());
      htmlBody = htmlBody.replaceAll("##IT_TYP##", gr.getReportsIt0() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT##", "hide");
    }

    if (gr.getHasIt1()){
      htmlBody = htmlBody.replaceAll("##IT1_OK##", "" + gr.getReportsIt1());
      htmlBody = htmlBody.replaceAll("##IT1_TYP##", gr.getReportsIt1() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT1##", "hide");
    }

    if (gr.getHasIt2()){
      htmlBody = htmlBody.replaceAll("##IT2_OK##", "" + gr.getReportsIt2());
      htmlBody = htmlBody.replaceAll("##IT2_TYP##", gr.getReportsIt2() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT2##", "hide");
    }

    htmlBody = htmlBody.replaceAll("##PC_OK##", "" + gr.getReportsPc());
    htmlBody = htmlBody.replaceAll("##PC_TYP##", gr.getReportsPc() == gr.getReportsCount() ? "success" : "error");

    htmlBody = htmlBody.replace("##KO##", "" + gr.getReportsKo());
    if (gr.getReportsOk() >= gr.getReportsKo()) {
      htmlBody = htmlBody.replace("##OK_C##", "success");
      htmlBody = htmlBody.replace("##KO_C##", "info-white");
    } else {
      htmlBody = htmlBody.replace("##OK_C##", "info-white");
      htmlBody = htmlBody.replace("##KO_C##", "error");
    }

    // Chart
    int angleG = globalPercent * 360 / 100;
    int reverseAngleG = 360 - angleG;
    String functionPie = "plotPie('pie-global', " + angleG + ", " + reverseAngleG;
    if (gr.getReportsOk() >= gr.getReportsKo()) {
      functionPie += ", '#66CC66', '#F2F2F2'); ";
    } else {
      functionPie += ", '#F2F2F2', 'red'); ";
    }
    pieFunctions += functionPie;

    // All charts calls
    htmlBody = htmlBody.replaceAll("##PLOT##", pieFunctions);

    // TO-DO
    htmlBody = htmlBody.replace("##OK_PC##", "0");
    htmlBody = htmlBody.replace("##OK_EP##", "0");
    // END TO-DO

    htmlBody = htmlBody.replaceAll("\\.\\./", "");
    ReportGenerator.writeToFile(outputfile, htmlBody);
  }
}
