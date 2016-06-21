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

package dpfmanager.shell.modules.report.util;

import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.ReportGeneric;

import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * The Class ReportHtml.
 */
public class ReportHtml extends ReportGeneric {
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
      imageBody = StringUtils.replace(imageBody, "##IMG_PATH##", encodeUrl(imgPath));

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

      imageBody = StringUtils.replace(imageBody, "##HREF##", "html/" + encodeUrl(new File(ir.getReportPath()).getName() + ".html"));
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

    if (gr.getHasBl()) {
      htmlBody = StringUtils.replace(htmlBody, "##BL_OK##", "" + gr.getReportsBl());
      htmlBody = StringUtils.replace(htmlBody, "##BL_TYP##", gr.getReportsBl() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_BL##", "hide");
    }

    if (gr.getHasEp()) {
      htmlBody = StringUtils.replace(htmlBody, "##EP_OK##", "" + gr.getReportsEp());
      htmlBody = StringUtils.replace(htmlBody, "##EP_TYP##", gr.getReportsEp() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_EP##", "hide");
    }

    if (gr.getHasIt0()) {
      htmlBody = StringUtils.replace(htmlBody, "##IT_OK##", "" + gr.getReportsIt0());
      htmlBody = StringUtils.replace(htmlBody, "##IT_TYP##", gr.getReportsIt0() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT##", "hide");
    }

    if (gr.getHasIt1()) {
      htmlBody = StringUtils.replace(htmlBody, "##IT1_OK##", "" + gr.getReportsIt1());
      htmlBody = StringUtils.replace(htmlBody, "##IT1_TYP##", gr.getReportsIt1() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT1##", "hide");
    }

    if (gr.getHasIt2()) {
      htmlBody = StringUtils.replace(htmlBody, "##IT2_OK##", "" + gr.getReportsIt2());
      htmlBody = StringUtils.replace(htmlBody, "##IT2_TYP##", gr.getReportsIt2() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = StringUtils.replace(htmlBody, "##ROW_IT2##", "hide");
    }

    if (gr.getHasPc()) {
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
    String functionPie = "";
    if (angleG > reverseAngleG) {
      functionPie = "plotPie('pie-global', " + angleG + ", " + reverseAngleG;
      if (gr.getReportsOk() >= gr.getReportsKo()) {
        functionPie += ", '#66CC66', '#F2F2F2'); ";
      } else {
        functionPie += ", '#F2F2F2', 'red'); ";
      }
    } else {
      functionPie = "plotPie('pie-global', " + reverseAngleG + ", " + angleG;
      if (gr.getReportsOk() >= gr.getReportsKo()) {
        functionPie += ", '#F2F2F2', '#66CC66'); ";
      } else {
        functionPie += ", 'red', '#F2F2F2'); ";
      }
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

  private String encodeUrl(String str) {
    return str.replaceAll("#", "%23");
  }
}
