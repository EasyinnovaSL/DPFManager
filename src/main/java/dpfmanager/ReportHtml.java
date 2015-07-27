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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * The Class ReportHtml.
 */
public class ReportHtml {

  /**
   * Copy a file or directory from source to target.
   *
   * @param sourceLocation the source path.
   * @param targetLocation the target path.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static void copy(File sourceLocation, File targetLocation) throws IOException {
    if (sourceLocation.isDirectory()) {
      copyDirectory(sourceLocation, targetLocation);
    } else {
      copyFile(sourceLocation, targetLocation);
    }
  }

  /**
   * Copy a directory from source to target.
   *
   * @param source the source path.
   * @param target the target path.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static void copyDirectory(File source, File target) throws IOException {
    if (!target.exists()) {
      target.mkdir();
    }
    for (String f : source.list()) {
      copy(new File(source, f), new File(target, f));
    }
  }


  /**
   * Copy file.
   *
   * @param source the source
   * @param target the target
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static void copyFile(File source, File target) throws IOException {
    try (InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(target)) {
      byte[] buf = new byte[1024];
      int length;
      while ((length = in.read(buf)) > 0) {
        out.write(buf, 0, length);
      }
    }
  }

  /**
   * Copy folder.
   *
   * @param path the path
   * @param name the name
   */
  private static void copyFolder(String path, String name) {
    File nameFile = new File(name);
    String absolutePath = nameFile.getAbsolutePath();
    String srcDir = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));

    File source = new File(path);
    File target = new File(srcDir + "/html");
    if (!target.exists()) {
      target.mkdir();
    }

    try {
      copyDirectory(source, target);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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
    String templatePath = "resources/templates/individual.html";
    String htmlFolder = "resources/html/";
    copyFolder(htmlFolder, outputfile);
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
    int epErr = ir.getErrors().size();
    int epWar = ir.getWarnings().size();
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
    for (ValidationEvent val : ir.getErrors()) {
      row = "<tr><td class=\"bold error\">Error</td><td>##TEXT##</td></tr>";
      row = row.replace("##TEXT##", val.getDescription());
      rows += row;
    }

    for (ValidationEvent val : ir.getWarnings()) {
      row = "<tr><td class=\"bold warning\">Warning</td><td>##TEXT##</td></tr>";
      row = row.replace("##TEXT##", val.getDescription());
      rows += row;
    }
    htmlBody = htmlBody.replaceAll("##ROWS_EP##", rows);

    // Taggs list
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
    for (TiffObject object : td.getIfds()) {
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

  private static boolean tiff2Jpg(String inputfile, String outputfile) {
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
    Double rest = 100.0 - ir.getErrors().size() * 12.5;
    if (rest < 0.0) {
      rest = 0.0;
    }
    return rest.intValue();
  }

  /**
   * Generate css color.
   *
   * @param index the index
   * @param ir the ir
   * @param path the path
   * @return the string
   */
  private static String generateCssColor(int index, IndividualReport ir, String path) {
    String css = ReportGenerator.readFile(path);
    css = css.replace("##INDEX##", "" + index);
    if (ir.getErrors().size() > 0) {
      css = css.replace("##FIRST##", "#CCCCCC"); // Light Grey
      css = css.replace("##SECOND##", "red");
    } else {
      css = css.replace("##FIRST##", "#66CC66"); // Green
      css = css.replace("##SECOND##", "#66CC66"); // Green
    }
    return css;
  }

  /**
   * Generate css rotation.
   *
   * @param path the path
   * @param start the start
   * @param val the val
   * @return the string
   */
  private static String generateCssRotation(String path, int start, int val) {
    String css = ReportGenerator.readFile(path);
    css = css.replaceAll("##START##", "" + start);
    css = css.replaceAll("##VAL##", "" + val);
    return css;
  }

  /**
   * Replaces chart.
   *
   * @param body the body
   * @param angle the angle
   * @param reverseAngle the reverse angle
   * @return the string
   */
  private static String replacesChart(String body, int angle, int reverseAngle) {
    body = body.replace("##VAL1##", "" + angle);
    body = body.replace("##START2##", "" + angle);
    body = body.replace("##VAL2##", "" + reverseAngle);
    if (angle > 180) {
      body = body.replace("##BIG1##", "big");
      body = body.replace("##BIG2##", "");
    } else {
      body = body.replace("##BIG1##", "");
      body = body.replace("##BIG2##", "big");
    }
    return body;
  }

  /**
   * Parse a global report to XML format.
   *
   * @param outputfile the output file.
   * @param gr the global report.
   */
  public static void parseGlobal(String outputfile, GlobalReport gr) {
    String templatePath = "resources/templates/global.html";
    String imagePath = "resources/templates/image.html";
    String pcPath = "resources/templates/pie-color.css";
    String prPath = "resources/templates/pie-rotation.css";
    String htmlFolder = "resources/html/";
    copyFolder(htmlFolder, outputfile);
    String newHtmlFolder = outputfile.substring(0, outputfile.lastIndexOf("/"));


    String imagesBody = "";
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
      imageBody = imageBody.replace("##ERR_N##", "" + ir.getErrors().size());
      imageBody = imageBody.replace("##WAR_N##", "" + ir.getWarnings().size());
      imageBody = imageBody.replace("##HREF##", "html/" + ir.getFileName() + ".html");
      if (ir.getErrors().size() > 0) {
        imageBody = imageBody.replace("##ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##ERR_C##", "success");
      }
      if (ir.getWarnings().size() > 0) {
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
      if (ir.getWarnings().size() > 0) {
        imageBody = imageBody.replace("##DISPLAY_WAR##", "inline-block");
      } else {
        imageBody = imageBody.replace("##DISPLAY_WAR##", "none");
      }

      // Percent Chart
      int angle = percent * 360 / 100;
      int reverseAngle = 360 - angle;
      imageBody = replacesChart(imageBody, angle, reverseAngle);
      String css = generateCssColor(index, ir, pcPath);
      css += generateCssRotation(prPath, 0, angle);
      css += generateCssRotation(prPath, angle, reverseAngle);
      imageBody = imageBody.replace("##CSS##", css);

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
    htmlBody = htmlBody.replace("##PERCENT##", "" + globalPercent);
    htmlBody = htmlBody.replace("##IMAGES_LIST##", imagesBody);
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
    String cssG = ReportGenerator.readFile(pcPath);
    cssG = cssG.replace("##INDEX##", "global");
    if (gr.getReportsOk() >= gr.getReportsKo()) {
      cssG = cssG.replace("##FIRST##", "#F2F2F2"); // Draker White
      cssG = cssG.replace("##SECOND##", "#66CC66"); // Green
    } else {
      cssG = cssG.replace("##FIRST##", "red"); // Red
      cssG = cssG.replace("##SECOND##", "#F2F2F2"); // Draker White
    }
    int angleG = globalPercent * 360 / 100;
    int reverseAngleG = 360 - angleG;
    cssG +=
        " " + generateCssRotation(prPath, 0, reverseAngleG) + " "
            + generateCssRotation(prPath, reverseAngleG, angleG);
    htmlBody = htmlBody.replaceAll("##ANGLE##", "" + angleG);
    htmlBody = htmlBody.replaceAll("##RANGLE##", "" + reverseAngleG);
    htmlBody = htmlBody.replace("##CSS##", "" + cssG);
    if (reverseAngleG > 180) {
      htmlBody = htmlBody.replaceAll("##BIG1##", "big");
      htmlBody = htmlBody.replaceAll("##BIG2##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##BIG1##", "");
      htmlBody = htmlBody.replaceAll("##BIG2##", "big");
    }

    // TO-DO
    htmlBody = htmlBody.replace("##OK_PC##", "0");
    htmlBody = htmlBody.replace("##OK_EP##", "0");
    // END TO-DO

    htmlBody = htmlBody.replaceAll("\\.\\./", "");
    ReportGenerator.writeToFile(outputfile, htmlBody);
  }
}
