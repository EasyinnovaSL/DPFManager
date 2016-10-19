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

import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
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

      /**
       * Errors / warnings resume
       */
      String rowTmpl =  "<tr>\n" +
          "\t\t\t\t\t\t        <td class=\"c1\">##NAME##</td>\n" +
          "\t\t\t\t\t\t        <td class=\"c2 ##ERR_C##\">##ERR_N## errors</td>\n" +
          "\t\t\t\t\t\t        <td class=\"c2 ##WAR_C##\">##WAR_N## warnings</td>\n" +
          "\t\t\t\t\t\t        <td></td>\n" +
          "\t\t\t\t\t\t    </tr>";
      String rows = "";
      for (String iso : ir.getIsosCheck()){
        String name = ImplementationCheckerLoader.getIsoName(iso);
        String row = rowTmpl;
        if (ir.hasValidation(iso)) {
          int errorsCount = ir.getNErrors(iso);
          int warningsCount = ir.getNWarnings(iso);
          row = StringUtils.replace(row, "##NAME##", name);
          row = StringUtils.replace(row, "##ERR_N##", "" + errorsCount);
          row = StringUtils.replace(row, "##WAR_N##", "" + warningsCount);
          if (errorsCount > 0){
            row = StringUtils.replace(row, "##ERR_C##", "error");
          } else {
            row = StringUtils.replace(row, "##ERR_C##", "");
          }
          if (warningsCount > 0){
            row = StringUtils.replace(row, "##WAR_C##", "warning");
          } else {
            row = StringUtils.replace(row, "##WAR_C##", "");
          }
        }
        rows += row;
      }
      imageBody = StringUtils.replace(imageBody, "##TABLE_RESUME_IMAGE##", rows);
      imageBody = StringUtils.replace(imageBody, "##HREF##", "html/" + encodeUrl(new File(ir.getReportPath()).getName() + ".html"));

      /**
       * Percent info
       */
      if (percent == 100) {
        imageBody = StringUtils.replace(imageBody, "##CLASS##", "success");
        imageBody = StringUtils.replace(imageBody, "##RESULT##", "Passed");
        if (ir.getAllWarnings().size() > 0) {
          imageBody = StringUtils.replace(imageBody, "##DISPLAY_WAR##", "inline-block");
        } else {
          imageBody = StringUtils.replace(imageBody, "##DISPLAY_WAR##", "none");
        }
      } else {
        imageBody = StringUtils.replace(imageBody, "##CLASS##", "error");
        imageBody = StringUtils.replace(imageBody, "##RESULT##", "Failed");
        imageBody = StringUtils.replace(imageBody, "##DISPLAY_WAR##", "none");
      }

      /**
       * Percent chart
       */
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
    Double doub = 1.0 * gr.getAllReportsOk() / gr.getReportsCount() * 100.0;
    int globalPercent = doub.intValue();
    htmlBody = StringUtils.replace(htmlBody, "##IMAGES_LIST##", imagesBody);
    htmlBody = StringUtils.replace(htmlBody, "##PERCENT##", "" + globalPercent);
    String scount = gr.getReportsCount() + " ";
    if (gr.getReportsCount() == 1) scount += "file";
    else scount += "files";
    htmlBody = StringUtils.replace(htmlBody, "##COUNT##", "" + scount);
    htmlBody = StringUtils.replace(htmlBody, "##OK##", "" + gr.getAllReportsOk());

    /**
     * Conforms table
     */
    String rowTmpl =  "<tr><td class=\"##TYPE## border-bot\">##OK##</td><td class=\"##TYPE## border-bot\">conforms to ##NAME##</td></tr>";
    String rows = "";
    for (String iso : gr.getIsos()){
      String row = rowTmpl;
      row = StringUtils.replace(row, "##OK##", "" + gr.getReportsOk(iso));
      row = StringUtils.replace(row, "##NAME##", ImplementationCheckerLoader.getIsoName(iso));
      row = StringUtils.replace(row, "##TYPE##", gr.getReportsOk(iso) == gr.getReportsCount() ? "success" : "error");
      rows += row;
    }
    htmlBody = StringUtils.replace(htmlBody, "##TABLE_RESUME##", rows);

    htmlBody = StringUtils.replace(htmlBody, "##KO##", "" + gr.getAllReportsKo());
    if (gr.getAllReportsOk() >= gr.getAllReportsKo()) {
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
      if (gr.getAllReportsOk() >= gr.getAllReportsKo()) {
        functionPie += ", '#66CC66', '#F2F2F2'); ";
      } else {
        functionPie += ", '#F2F2F2', 'red'); ";
      }
    } else {
      functionPie = "plotPie('pie-global', " + reverseAngleG + ", " + angleG;
      if (gr.getAllReportsOk() >= gr.getAllReportsKo()) {
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
