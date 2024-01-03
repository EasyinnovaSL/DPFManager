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

package dpfmanager.shell.modules.report.util;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.ReportGeneric;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import org.apache.commons.lang3.StringUtils;


import java.io.File;
import java.util.Collections;
import java.util.List;

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
  public void parseGlobal(String outputfile, GlobalReport gr, List<SmallIndividualReport> reports, ReportGenerator generator) {
    Collections.sort(reports);
    String templatePath = "templates/global.html";
    String imagePath = "templates/image.html";

    String imagesBody = "";
    String pieFunctions = "";

    // Parse individual Reports
    int index = 0;
    for (SmallIndividualReport ir : reports) {
      if (!ir.getContainsData()) continue;

      int percent = ir.getPercent();
      String imageBody;
      imageBody = generator.readFilefromResources(imagePath);
      // Image
      String imgPath = "html/" + ir.getImagePath();

      imageBody = StringUtils.replace(imageBody, "##IMG_PATH##", encodeUrl(imgPath));

      // Basic
      imageBody = StringUtils.replace(imageBody, "##PERCENT##", "" + percent);
      imageBody = StringUtils.replace(imageBody, "##INDEX##", "" + index);
      imageBody = StringUtils.replace(imageBody, "##IMG_NAME##", "" + ir.getFileName());

      /**
       * Errors / warnings resume (individual)
       */
      int totalWarnings = 0;
      if (!ir.isQuick()) {
        /**
         * Full Check
         */
        String thTmpl = "<tr><th colspan=\"3\" style=\"width: 100%;\">Conformance checker</th></tr>";
        String rowTmpl = "<tr>\n" +
            "\t\t\t\t\t\t        <td class=\"c1\">##NAME##</td>\n" +
            "\t\t\t\t\t\t        <td class=\"c2 ##ERR_C##\">##ERR_N## errors</td>\n" +
            "\t\t\t\t\t\t        <td class=\"c2 ##WAR_C##\">##WAR_N## warnings</td>\n" +
            "\t\t\t\t\t\t        <td></td>\n" +
            "\t\t\t\t\t\t    </tr>";
        int mode = 1;
        if (gr.getModifiedIsos().keySet().size() > 0) {
          thTmpl = "<tr><th style=\"width: 100%;\">Conformance checker</th><th class=\"nowrap\">Standard</th><th class=\"nowrap\">Policy</th></tr>";
          rowTmpl = "<tr>\n" +
              "\t\t\t\t\t\t        <td class=\"c1\">##NAME##</td>\n" +
              "\t\t\t\t\t\t        <td class=\"c2\">" +
              "\t\t\t\t\t\t\t        <p><span class=\"##ERR_C##\">##ERR_N## errors</span></p>" +
              "\t\t\t\t\t\t\t        <p><span class=\"##WAR_C##\">##WAR_N## warnings</span></p>" +
              "\t\t\t\t\t\t        </td>\n" +
              "\t\t\t\t\t\t        <td class=\"c2\">" +
              "\t\t\t\t\t\t\t        <p><span class=\"##ERR_C_P##\">##ERR_N_P## errors</span></p>" +
              "\t\t\t\t\t\t\t        <p><span class=\"##WAR_C_P##\">##WAR_N_P## warnings</span></p>" +
              "\t\t\t\t\t\t        </td>\n" +
              "\t\t\t\t\t\t    </tr>";
          mode = 2;
        }
        String rows = "";
        for (String iso : gr.getCheckedIsos()) {
          if (gr.hasValidation(iso)) {
            String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
            String row = rowTmpl;

            // Standard IC
            int errorsCount = ir.getNErrors(iso);
            int warningsCount = ir.getNWarnings(iso);
            totalWarnings += warningsCount;
            row = StringUtils.replace(row, "##NAME##", name);
            row = StringUtils.replace(row, "##ERR_N##", "" + errorsCount);
            row = StringUtils.replace(row, "##WAR_N##", "" + warningsCount);
            if (errorsCount > 0) {
              row = StringUtils.replace(row, "##ERR_C##", "error");
            } else {
              row = StringUtils.replace(row, "##ERR_C##", "");
            }
            if (warningsCount > 0) {
              row = StringUtils.replace(row, "##WAR_C##", "warning");
            } else {
              row = StringUtils.replace(row, "##WAR_C##", "");
            }

            // Standard PC
            if (mode == 2) {
              if (!gr.hasModifiedIso(iso)) {
                // Empty
                row = StringUtils.replace(row, "##ERR_C_P##", "hide");
                row = StringUtils.replace(row, "##WAR_C_P##", "hide");
              } else {
                int errorsCountPolicy = ir.getNErrorsPolicy(iso);
                int warningsCountPolicy = ir.getNWarningsPolicy(iso);
                row = StringUtils.replace(row, "##ERR_N_P##", "" + errorsCountPolicy);
                row = StringUtils.replace(row, "##WAR_N_P##", "" + warningsCountPolicy);
                if (errorsCountPolicy > 0) {
                  row = StringUtils.replace(row, "##ERR_C_P##", "error");
                } else if (errorsCount > 0) {
                  row = StringUtils.replace(row, "##ERR_C_P##", "success");
                } else {
                  row = StringUtils.replace(row, "##ERR_C_P##", "");
                }
                if (warningsCountPolicy > 0) {
                  row = StringUtils.replace(row, "##WAR_C_P##", "warning");
                } else if (warningsCount > 0) {
                  row = StringUtils.replace(row, "##WAR_C_P##", "success");
                } else {
                  row = StringUtils.replace(row, "##WAR_C_P##", "");
                }
              }
            }
            rows += row;
          }
        }
        imageBody = StringUtils.replace(imageBody, "##TABLE_RESUME_IMAGE##", thTmpl + rows);
      } else {
        /**
         * Quick check
         */
        String thTmpl = "<tr><th style=\"width: 70%;\">Conformance checker</th><th class=\"nowrap\" style=\"width: 30%;\">Result</th></tr>";
        String rowTmpl = "<tr>\n" +
            "\t\t\t\t\t\t        <td class=\"c1\">##NAME##</td>\n" +
            "\t\t\t\t\t\t        <td class=\"c2 ##RESULT_COLOR##\">##RESULT_NAME##</td>\n" +
            "\t\t\t\t\t\t    </tr>";
        String policySpan = "<span class=\"no-bold\">with custom policy</span>";
        String rows = "";
        for (String iso : gr.getCheckedIsos()) {
          if (gr.hasValidation(iso)) {
            int mode = (gr.getModifiedIsos().containsKey(iso)) ? 2 : 1;
            int errorsCount = (mode == 1) ? ir.getNErrors(iso) : ir.getNErrorsPolicy(iso);
            String row = rowTmpl;
            String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
            String resultName = (errorsCount == 0) ? "Passed" : "Failed";
            String resultColor = (errorsCount == 0) ? "success" : "error";
            String nameTd = (mode == 1) ? name : name + "<br>" + policySpan;
            row = StringUtils.replace(row, "##NAME##", nameTd);
            row = StringUtils.replace(row, "##RESULT_NAME##", resultName);
            row = StringUtils.replace(row, "##RESULT_COLOR##", resultColor);
            rows += row;
          }
        }
        imageBody = StringUtils.replace(imageBody, "##TABLE_RESUME_IMAGE##", thTmpl + rows);
      }
      imageBody = StringUtils.replace(imageBody, "##HREF##", "html/" + encodeUrl(new File(ir.getReportPath()).getName() + ".html"));

      /**
       * Percent info
       */
      if (percent == 100) {
        imageBody = StringUtils.replace(imageBody, "##CLASS##", "success");
        imageBody = StringUtils.replace(imageBody, "##RESULT##", "Passed");
        if (totalWarnings > 0) {
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

      /** Bug Java 1.8_91*/
      if (percent > 50){
        int angle = percent * 360 / 100;
        int reverseAngle = 360 - angle;
        String functionPie = "plotPie('pie-" + index + "', " + angle + ", " + reverseAngle;
        if (percent < 100) {
          functionPie += ", '#CCCCCC', 'red'); ";
        } else {
          functionPie += ", '#66CC66', '#66CC66'); ";
        }
        pieFunctions += functionPie;
      } else {
        int reverseAngle = percent * 360 / 100;
        int angle = 360 - reverseAngle;
        String functionPie = "plotPie('pie-" + index + "', " + angle + ", " + reverseAngle;
        if (percent < 100) {
          functionPie += ", 'red', '#CCCCCC'); ";
        } else {
          functionPie += ", '#66CC66', '#66CC66'); ";
        }
        pieFunctions += functionPie;
      }

      /** Java 1.8_121*/
//      int reverseAngle = percent * 360 / 100;
//      int angle = 360 - reverseAngle;
//      String functionPie = "plotPie('pie-" + index + "', " + angle + ", " + reverseAngle;
//      if (percent < 100) {
//        functionPie += ", 'red', '#CCCCCC'); ";
//      } else {
//        functionPie += ", '#66CC66', '#66CC66'); ";
//      }
//      pieFunctions += functionPie;

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
     * Conforms table (all)
     */
    String rows = "";
    for (String iso : gr.getCheckedIsos()) {
      if (gr.getSelectedIsos().contains(iso) || gr.getReportsOk(iso) == gr.getReportsCount()) {
        rows += makeConformsRow(gr, iso, true);
      }
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

  private String makeConformsRow(GlobalReport gr, String iso, boolean force) {
    String row = "<tr><td class=\"##TYPE## border-bot\">##OK##</td><td class=\"##TYPE## border-bot\">Conforms to ##NAME## ##POLICY##</td></tr>";
    String policy = "";
    int n = 0;
    if (gr.hasModificationIso(iso)) {
      policy = gr.getReportsOk(iso) == gr.getReportsOkPolicy(iso) ? "" : " (with custom policy)";
    }
    String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
    row = StringUtils.replace(row, "##OK##", "" + (gr.hasModificationIso(iso) ? gr.getReportsOkPolicy(iso) : gr.getReportsOk(iso)));
    row = StringUtils.replace(row, "##NAME##", name);
    row = StringUtils.replace(row, "##POLICY##", policy);
    row = StringUtils.replace(row, "##TYPE##", (gr.hasModificationIso(iso) ? gr.getReportsOkPolicy(iso) : gr.getReportsOk(iso)) == gr.getReportsCount() ? "success" : "error");
    return row;
  }

  private String encodeUrl(String str) {
    return str.replaceAll("#", "%23");
  }
}
