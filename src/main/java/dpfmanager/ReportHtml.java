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

package dpfmanager;

import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.types.IFD;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Class ReportHtml.
 */
public class ReportHtml {

  /**
   * Insert html folder.
   *
   * @param file the file
   * @return the string
   */
  private static String insertHtmlFolder(String file) {
    String name = file.substring(file.lastIndexOf("/") + 1, file.length());
    return file.replace(name, "html/" + name);
  }

  /**
   * Parse an individual report to HTML.
   *
   * @param outputfile the outputfile
   * @param ir the individual report.
   */
  public static void parseIndividual(String outputfile, IndividualReport ir) {
    String templatePath = "./src/main/resources/templates/individual.html";
    outputfile = insertHtmlFolder(outputfile);
    String newHtmlFolder = outputfile.substring(0, outputfile.lastIndexOf("/"));

    String htmlBody = ReportGenerator.readFile(templatePath);

    // Image
    String imgPath = "img/" + ir.getFileName() + ".jpg";
    boolean check = tiff2Jpg(ir.getFilePath(), newHtmlFolder + "/" + imgPath);
    if (!check) {
      imgPath = "img/noise.jpg";
    }
    htmlBody = htmlBody.replace("##IMG_PATH##", imgPath);

    // Basic info
    htmlBody = htmlBody.replace("##IMG_NAME##", ir.getFileName());
    int epErr = ir.getEPErrors().size();
    int epWar = ir.getEPWarnings().size();
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

    // Errors
    String clas = "success";
    if (epErr > 0) {
      clas = "error";
    }
    htmlBody = htmlBody.replaceAll("##U_EP_ERR_N##", "" + epErr);
    htmlBody = htmlBody.replaceAll("##U_EP_ERR_CLASS##", clas);

    // Warnings
    clas = "success";
    if (epWar > 0) {
      clas = "warning";
    }
    htmlBody = htmlBody.replaceAll("##U_EP_WAR##", "" + epWar);
    htmlBody = htmlBody.replaceAll("##U_EP_WAR_CLASS##", clas);

    // TO-DO, actually never fix nothing || no policy checker
    htmlBody = htmlBody.replaceAll("##U_PC_CLASS##", "success");
    htmlBody = htmlBody.replaceAll("##U_PCR##", "" + 0);
    htmlBody = htmlBody.replaceAll("##CP_OK##", "none");
    htmlBody = htmlBody.replaceAll("##CP_ERR##", "none");
    htmlBody = htmlBody.replaceAll("##F_EP_ERR_CLASS##", "info");
    htmlBody = htmlBody.replaceAll("##F_EP_WAR_CLASS##", "info");
    htmlBody = htmlBody.replaceAll("##F_PC_CLASS##", "info");
    htmlBody = htmlBody.replaceAll("##F_EP_ERR##", "0");
    htmlBody = htmlBody.replaceAll("##F_EP_WAR##", "0");
    htmlBody = htmlBody.replaceAll("##F_PC##", "0");
    // End TO-DO

    // Full Description
    // Errors and warnings
    String row;
    String rows = "";
    for (ValidationEvent val : ir.getEPErrors()) {
      row = "<tr><td class=\"bold error\">Error</td><td>##TEXT##</td></tr>";
      row = row.replace("##TEXT##", val.getDescription());
      rows += row;
    }

    for (ValidationEvent val : ir.getEPWarnings()) {
      row = "<tr><td class=\"bold warning\">Warning</td><td>##TEXT##</td></tr>";
      row = row.replace("##TEXT##", val.getDescription());
      rows += row;
    }
    htmlBody = htmlBody.replaceAll("##ROWS_EP##", rows);

    // Tags list
    rows = "";
    Metadata meta = ir.getTiffModel().getMetadata();
    for (String key : meta.keySet()) {
      row = "<tr><td>##KEY##</td><td>##VALUE##</td></tr>";
      row = row.replace("##KEY##", key);
      row = row.replace("##VALUE##", meta.get(key).toString());
      rows += row;
    }
    htmlBody = htmlBody.replaceAll("##ROWS_TAGS##", rows);

    // File Structure
    String ul = "<ul>";
    TiffDocument td = ir.getTiffModel();
    for (TiffObject object : td.getImageIfds()) {
      IFD ifd = (IFD) object;
      ul += "<li><i class=\"fa fa-file-o\"></i> " + ifd.toString();
      if (ifd.getsubIFD() != null) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> " + ifd.getsubIFD().toString() + "</li></ul>";
      }
      ul += "</li>";
    }
    ul += "</ul>";
    htmlBody = htmlBody.replaceAll("##UL##", ul);

    // Finish, write to html file
    htmlBody = htmlBody.replaceAll("\\.\\./html/", "");
    ReportGenerator.writeToFile(outputfile, htmlBody);
  }

  /**
   * Tiff2 jpg.
   *
   * @param inputfile the inputfile
   * @param outputfile the outputfile
   * @return true, if successful
   */
  private static boolean tiff2Jpg(String inputfile, String outputfile) {
    File outfile = new File(outputfile);
    if (outfile.exists()) {
      return true;
    }
    BufferedImage image = null;
    try {
      File input = new File(inputfile);
      image = ImageIO.read(input);

      double factor = 1.0;
      int width = image.getWidth();
      if (width > 500) {
        factor = 500.0 / width;
      }
      int height = (int) (image.getHeight() * factor);
      width = (int) (width * factor);

      BufferedImage convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D graphic = convertedImage.createGraphics();
      graphic.drawImage(image, 0, 0, width, height, null);
      graphic.dispose();

      ImageIO.write(convertedImage, "jpg", new File(outputfile));
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  /**
   * Calculate percent.
   *
   * @param ir the ir
   * @return the int
   */
  private static int calculatePercent(IndividualReport ir) {
    Double rest = 100.0 - ir.getEPErrors().size() * 12.5;
    if (rest < 0.0) {
      rest = 0.0;
    }
    return rest.intValue();
  }

  /**
   * Parse a global report to XML format.
   *
   * @param outputfile the output file.
   * @param gr the global report.
   */
  public static void parseGlobal(String outputfile, GlobalReport gr) {
    String templatePath = "./src/main/resources/templates/global.html";
    String imagePath = "./src/main/resources/templates/image.html";
    String newHtmlFolder = outputfile.substring(0, outputfile.lastIndexOf("/"));


    String imagesBody = "";
    String pieFunctions = "";
    // Parse individual Reports
    int index = 0;
    for (IndividualReport ir : gr.getIndividualReports()) {
      String imageBody = ReportGenerator.readFile(imagePath);

      // Image
      String imgPath = "html/img/" + ir.getFileName() + ".jpg";
      boolean check = tiff2Jpg(ir.getFilePath(), newHtmlFolder + "/" + imgPath);
      if (!check) {
        imgPath = "html/img/noise.jpg";
      }
      imageBody = imageBody.replace("##IMG_PATH##", imgPath);

      // Basic
      int percent = calculatePercent(ir);
      imageBody = imageBody.replace("##PERCENT##", "" + percent);
      imageBody = imageBody.replace("##INDEX##", "" + index);
      imageBody = imageBody.replace("##IMG_NAME##", "" + ir.getFileName());
      imageBody = imageBody.replace("##ERR_N##", "" + ir.getEPErrors().size());
      imageBody = imageBody.replace("##WAR_N##", "" + ir.getEPWarnings().size());
      imageBody = imageBody.replace("##HREF##", "html/" + ir.getFileName() + ".html");
      if (ir.getEPErrors().size() > 0) {
        imageBody = imageBody.replace("##ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##ERR_C##", "success");
      }
      if (ir.getEPWarnings().size() > 0) {
        imageBody = imageBody.replace("##WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##WAR_C##", "success");
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
      if (ir.getEPErrors().size() > 0) {
        functionPie += ", '#CCCCCC', 'red'); ";
      } else {
        functionPie += ", '#66CC66', '#66CC66'); ";
      }
      pieFunctions += functionPie;

      // TO-DO
      imageBody = imageBody.replace("##CP_N##", "0");
      imageBody = imageBody.replace("##CP_C##", "success");
      // END TO-DO

      imagesBody += imageBody;
      index++;
    }

    // Parse the sumary report
    // numbers
    String htmlBody = ReportGenerator.readFile(templatePath);
    Double doub = 1.0 * gr.getReportsOk() / gr.getReportsCount() * 100.0;
    int globalPercent = doub.intValue();
    htmlBody = htmlBody.replace("##IMAGES_LIST##", imagesBody);
    htmlBody = htmlBody.replace("##PERCENT##", "" + globalPercent);
    htmlBody = htmlBody.replace("##COUNT##", "" + gr.getReportsCount());
    htmlBody = htmlBody.replaceAll("##OK##", "" + gr.getReportsOk());
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
    String functionPie = "plotPie('pie-global', " + reverseAngleG + ", " + angleG;
    if (gr.getReportsOk() >= gr.getReportsKo()) {
      functionPie += ", '#F2F2F2', '#66CC66'); ";
    } else {
      functionPie += ", 'red', '#F2F2F2'); ";
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
