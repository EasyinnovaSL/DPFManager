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

package dpfmanager.shell.modules.report.util;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.ReportGeneric;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
  public void parseIndividual(String outputfile, IndividualReport ir, int mode, ReportGenerator generator) {
    //String templatePath = "./src/main/resources/templates/individual.html";
    String templatePath = "templates/individual.html";
    String name = outputfile.substring(outputfile.lastIndexOf("/") + 1, outputfile.length());
    outputfile = outputfile.replace(name, "html/" + name);
    String newHtmlFolder = outputfile.substring(0, outputfile.lastIndexOf("/"));

    //String htmlBody = ReportGenerator.readFile(templatePath);
    String htmlBody = generator.readFilefromResources(templatePath);

    // Image
    String imgPath = "img/" + new File(ir.getReportPath()).getName() + ".jpg";
    boolean check = tiff2Jpg(ir.getFilePath(), newHtmlFolder + "/" + imgPath);
    if (!check) {
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
      htmlBody = StringUtils.replace(htmlBody, "##LINK2##", new File(ir.getReportPath()).getName() + "_fixed.html");
    }
    if (mode == 2) {
      htmlBody = StringUtils.replace(htmlBody, "##CL_LINKR1##", "show");
      htmlBody = StringUtils.replace(htmlBody, "##LINK1##", new File(ir.getCompareReport().getReportPath()).getName() + ".html");
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
    generator.writeToFile(outputfile, htmlBody);
  }

  /**
   * Parse a global report to XML format.
   *
   * @param outputfile the output file.
   * @param gr         the global report.
   */
  public void parseGlobal(String outputfile, GlobalReport gr, ReportGenerator generator) {
    String templatePath = "templates/global.html";
    String imagePath = "templates/image.html";
    String newHtmlFolder = outputfile.substring(0, outputfile.lastIndexOf("/"));

    String imagesBody = "";
    String pieFunctions = "";

    // Parse individual Reports
    int index = 0;
    gr.computePcChecks();
    for (IndividualReport ir : gr.getIndividualReports()) {
      if (!ir.containsData()) continue;
      String imageBody;
      imageBody = generator.readFilefromResources(imagePath);
      // Image
      String imgPath = "html/img/" + new File(ir.getReportPath()).getName() + ".jpg";
      boolean check = tiff2Jpg(ir.getFilePath(), newHtmlFolder + "/" + imgPath);
      if (!check) {
        imgPath = "html/img/noise.jpg";
      }
      imageBody = StringUtils.replace(imageBody, "##IMG_PATH##", imgPath);

      // Basic
      int percent = ir.calculatePercent();
      imageBody = StringUtils.replace(imageBody, "##PERCENT##", "" + percent);
      imageBody = StringUtils.replace(imageBody, "##INDEX##", "" + index);
      imageBody = StringUtils.replace(imageBody, "##IMG_NAME##", "" + ir.getFileName());

      if (ir.hasEpValidation()) {
        imageBody = StringUtils.replace(imageBody, "##EP_ERR_N##", "" + ir.getEPErrors().size());
        imageBody = StringUtils.replace(imageBody, "##EP_WAR_N##", "" + ir.getEPWarnings().size());
      } else {
        imageBody = StringUtils.replace(imageBody, "##EP_CLASS##", "hide");
      }

      if (ir.hasBlValidation()) {
        imageBody = StringUtils.replace(imageBody, "##BL_ERR_N##", "" + ir.getBaselineErrors().size());
        imageBody = StringUtils.replace(imageBody, "##BL_WAR_N##", "" + ir.getBaselineWarnings().size());
      } else {
        imageBody = StringUtils.replace(imageBody, "##BL_CLASS##", "hide");
      }

      if (ir.hasItValidation(0)) {
        imageBody = StringUtils.replace(imageBody, "##IT_ERR_N##", "" + ir.getITErrors(0).size());
        imageBody = StringUtils.replace(imageBody, "##IT_WAR_N##", "" + ir.getITWarnings(0).size());
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT_CLASS##", "hide");
      }

      if (ir.hasItValidation(1)) {
        imageBody = StringUtils.replace(imageBody, "##IT1_ERR_N##", "" + ir.getITErrors(1).size());
        imageBody = StringUtils.replace(imageBody, "##IT1_WAR_N##", "" + ir.getITWarnings(1).size());
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT1_CLASS##", "hide");
      }

      if (ir.hasItValidation(2)) {
        imageBody = StringUtils.replace(imageBody, "##IT2_ERR_N##", "" + ir.getITErrors(2).size());
        imageBody = StringUtils.replace(imageBody, "##IT2_WAR_N##", "" + ir.getITWarnings(2).size());
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT2_CLASS##", "hide");
      }

      if (ir.checkPC) {
        imageBody = StringUtils.replace(imageBody, "##PC_ERR_N##", "" + ir.getPCErrors().size());
        imageBody = StringUtils.replace(imageBody, "##PC_WAR_N##", "" + ir.getPCWarnings().size());
      } else {
        imageBody = StringUtils.replace(imageBody, "##PC_CLASS##", "hide");
      }

      imageBody = StringUtils.replace(imageBody, "##HREF##", "html/" + new File(ir.getReportPath()).getName() + ".html");
      if (ir.getBaselineErrors().size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##BL_ERR_C##", "error");
      } else {
        imageBody = StringUtils.replace(imageBody, "##BL_ERR_C##", "");
      }
      if (ir.getBaselineWarnings().size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##BL_WAR_C##", "warning");
      } else {
        imageBody = StringUtils.replace(imageBody, "##BL_WAR_C##", "");
      }
      if (ir.getEPErrors().size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##EP_ERR_C##", "error");
      } else {
        imageBody = StringUtils.replace(imageBody, "##EP_ERR_C##", "");
      }
      if (ir.getEPWarnings().size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##EP_WAR_C##", "warning");
      } else {
        imageBody = StringUtils.replace(imageBody, "##EP_WAR_C##", "");
      }
      if (ir.getITErrors(0).size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##IT_ERR_C##", "error");
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT_ERR_C##", "");
      }
      if (ir.getITWarnings(0).size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##IT_WAR_C##", "warning");
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT_WAR_C##", "");
      }
      if (ir.getITErrors(1).size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##IT1_ERR_C##", "error");
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT1_ERR_C##", "");
      }
      if (ir.getITWarnings(1).size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##IT1_WAR_C##", "warning");
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT1_WAR_C##", "");
      }
      if (ir.getITErrors(2).size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##IT2_ERR_C##", "error");
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT2_ERR_C##", "");
      }
      if (ir.getITWarnings(2).size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##IT2_WAR_C##", "warning");
      } else {
        imageBody = StringUtils.replace(imageBody, "##IT2_WAR_C##", "");
      }

      if (ir.getPCErrors().size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##PC_ERR_C##", "error");
      } else {
        imageBody = StringUtils.replace(imageBody, "##PC_ERR_C##", "");
      }
      if (ir.getPCWarnings().size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##PC_WAR_C##", "warning");
      } else {
        imageBody = StringUtils.replace(imageBody, "##PC_WAR_C##", "");
      }

      // Percent Info
      if (percent == 100) {
        imageBody = StringUtils.replace(imageBody, "##CLASS##", "success");
        imageBody = StringUtils.replace(imageBody, "##RESULT##", "Passed");
      } else {
        imageBody = StringUtils.replace(imageBody, "##CLASS##", "error");
        imageBody = StringUtils.replace(imageBody, "##RESULT##", "Failed");
      }
      if (ir.getEPWarnings().size() > 0) {
        imageBody = StringUtils.replace(imageBody, "##DISPLAY_WAR##", "inline-block");
      } else {
        imageBody = StringUtils.replace(imageBody, "##DISPLAY_WAR##", "none");
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
    htmlBody = generator.readFilefromResources(templatePath);
    Double doub = 1.0 * gr.getReportsOk() / gr.getReportsCount() * 100.0;
    int globalPercent = doub.intValue();
    htmlBody = StringUtils.replace(htmlBody, "##IMAGES_LIST##", imagesBody);
    htmlBody = StringUtils.replace(htmlBody, "##PERCENT##", "" + globalPercent);
    String scount = gr.getReportsCount() + " ";
    if (gr.getReportsCount() == 1) scount += "file";
    else scount += "files";
    htmlBody = StringUtils.replace(htmlBody, "##COUNT##", "" + scount);
    htmlBody = StringUtils.replace(htmlBody, "##OK##", "" + gr.getReportsOk());

    if (gr.getHasBl()){
      htmlBody = StringUtils.replace(htmlBody, "##BL_OK##", "" + gr.getReportsBl());
      htmlBody = StringUtils.replace(htmlBody, "##BL_TYP##", gr.getReportsBl() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_BL##", "hide");
    }

    if (gr.getHasEp()){
      htmlBody = StringUtils.replace(htmlBody, "##EP_OK##", "" + gr.getReportsEp());
      htmlBody = StringUtils.replace(htmlBody, "##EP_TYP##", gr.getReportsEp() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_EP##", "hide");
    }

    if (gr.getHasIt0()){
      htmlBody = StringUtils.replace(htmlBody, "##IT_OK##", "" + gr.getReportsIt0());
      htmlBody = StringUtils.replace(htmlBody, "##IT_TYP##", gr.getReportsIt0() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT##", "hide");
    }

    if (gr.getHasIt1()){
      htmlBody = StringUtils.replace(htmlBody, "##IT1_OK##", "" + gr.getReportsIt1());
      htmlBody = StringUtils.replace(htmlBody, "##IT1_TYP##", gr.getReportsIt1() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT1##", "hide");
    }

    if (gr.getHasIt2()){
      htmlBody = StringUtils.replace(htmlBody, "##IT2_OK##", "" + gr.getReportsIt2());
      htmlBody = StringUtils.replace(htmlBody, "##IT2_TYP##", gr.getReportsIt2() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT2##", "hide");
    }

    if (gr.getHasPc()){
      htmlBody = StringUtils.replace(htmlBody, "##PC_OK##", "" + gr.getReportsPc());
      htmlBody = StringUtils.replace(htmlBody, "##PC_TYP##", gr.getReportsPc() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_PC##", "hide");
    }

    htmlBody = StringUtils.replace(htmlBody, "##PC_OK##", "" + gr.getReportsPc());
    htmlBody = StringUtils.replace(htmlBody, "##PC_TYP##", gr.getReportsPc() == gr.getReportsCount() ? "success" : "error");

    htmlBody = StringUtils.replace(htmlBody, "##KO##", "" + gr.getReportsKo());
    if (gr.getReportsOk() >= gr.getReportsKo()) {
      htmlBody = StringUtils.replace(htmlBody, "##OK_C##", "success");
      htmlBody = StringUtils.replace(htmlBody, "##KO_C##", "info-white");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##OK_C##", "info-white");
      htmlBody = StringUtils.replace(htmlBody, "##KO_C##", "error");
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
    htmlBody = StringUtils.replace(htmlBody, "##PLOT##", pieFunctions);

    // TO-DO
    htmlBody = StringUtils.replace(htmlBody, "##OK_PC##", "0");
    htmlBody = StringUtils.replace(htmlBody, "##OK_EP##", "0");
    // END TO-DO

    htmlBody = htmlBody.replaceAll("\\.\\./", "");
    generator.writeToFile(outputfile, htmlBody);
  }
}
