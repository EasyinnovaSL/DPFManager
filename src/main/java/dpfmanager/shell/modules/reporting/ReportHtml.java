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

package dpfmanager.shell.modules.reporting;

import dpfmanager.ReportGenerator;
import dpfmanager.shell.modules.classes.Fixes;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
  public static void parseIndividual(String outputfile, IndividualReport ir, ValidationResult pcValidation, int mode) {
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
    int epErr = ir.getNEpErr();
    int epWar = ir.getNEpWar();
    int blErr = ir.getNBlErr();
    int blWar = ir.getNBlWar();
    int itErr = ir.getNItErr();
    int itWar = ir.getNItWar();
    int pcErr = pcValidation.getErrors().size();
    int pcWar = pcValidation.getWarnings().size();
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
    if (itErr > 0) {
      htmlBody = htmlBody.replaceAll("##IT_OK##", "none");
      htmlBody = htmlBody.replaceAll("##IT_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##IT_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##IT_ERR-WAR##", "");
    } else if (itWar > 0) {
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
      htmlBody = htmlBody.replaceAll("##LINK2##", ir.getFileName() + "_fixed.html");
    }
    if (mode == 2) {
      htmlBody = htmlBody.replaceAll("##CL_LINKR1##", "show");
      htmlBody = htmlBody.replaceAll("##LINK1##", ir.getCompareReport().getFileName() + ".html");
    }

    String dif;

    // Policy checker
    if (pcValidation.getErrors().size() > 0) {
      htmlBody = htmlBody.replaceAll("##F_PC_ERR_CLASS##", "error");
    } else {
      htmlBody = htmlBody.replaceAll("##F_PC_ERR_CLASS##", "info");
    }
    if (pcValidation.getWarnings().size() > 0) {
      htmlBody = htmlBody.replaceAll("##F_PC_WAR_CLASS##", "warning");
    } else {
      htmlBody = htmlBody.replaceAll("##F_PC_WAR_CLASS##", "info");
    }
    htmlBody = htmlBody.replaceAll("##F_PC_ERR##", "" + pcValidation.getErrors().size());
    htmlBody = htmlBody.replaceAll("##F_PC_WAR##", "" + pcValidation.getWarnings().size());

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

    if (ir.hasItValidation()) {
      htmlBody = htmlBody.replaceAll("##F_IT_ERR_CLASS##", ir.getITErrors().size() > 0 ? "error" : "info");
      htmlBody = htmlBody.replaceAll("##F_IT_WAR_CLASS##", ir.getITWarnings().size() > 0 ? "warning" : "info");
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(), itErr) : "";
      htmlBody = htmlBody.replaceAll("##F_IT_ERR##", "" + ir.getITErrors().size() + dif);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(), itWar) : "";
      htmlBody = htmlBody.replaceAll("##F_IT_WAR##", "" + ir.getITWarnings().size() + dif);
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT##", "hide");
    }

    // Full Description
    // Errors and warnings

    // EP
    String row;
    String rows = "";

    if (ir.checkEP) {
      if (ir.getEPErrors() != null) {
        for (ValidationEvent val : ir.getEPErrors()) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getEPWarnings() != null) {
        for (ValidationEvent val : ir.getEPWarnings()) {
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
        for (ValidationEvent val : ir.getBaselineErrors()) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getBaselineWarnings() != null) {
        for (ValidationEvent val : ir.getBaselineWarnings()) {
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
    if (ir.checkIT >= 0) {
      if (ir.getITErrors() != null) {
        for (ValidationEvent val : ir.getITErrors()) {
          row = "<tr><td class=\"bold error\">Error</td><td>##LOC##</td><td>##TEXT##</td></tr>";
          row = row.replace("##LOC##", val.getLocation());
          row = row.replace("##TEXT##", val.getDescription());
          rows += row;
        }
      }

      if (ir.getITWarnings() != null) {
        for (ValidationEvent val : ir.getITWarnings()) {
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
    TiffDocument td = ir.getTiffModel();
    IFD ifd = td.getFirstIFD();
    IFD ifdcomp = null;
    if (ir.getCompareReport() != null) {
      ifdcomp = ir.getCompareReport().getTiffModel().getFirstIFD();
    }
    td.getFirstIFD();
    int index = 0;
    boolean expertMode = false;
    while (ifd != null) {
      IfdTags meta = ifd.getMetadata();
      for (TagValue tv : meta.getTags()) {
        if (showTag(tv) || expertMode) {
          String seetr = "";
          if (index > 0) seetr = " hide";
          row = "<tr class='ifd ifd" + index + seetr + "'><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
          dif = "";
          if (ifdcomp != null) {
            if (!ifdcomp.getMetadata().containsTagId(tv.getId()))
              dif += "+";
          }
          row = row.replace("##ID##", tv.getId() + dif);
          row = row.replace("##KEY##", tv.getName());
          row = row.replace("##VALUE##", tv.toString());
          rows += row;
        }
      }
      if (ifdcomp != null) {
        for (TagValue tv : ifdcomp.getMetadata().getTags()) {
          if (showTag(tv) || expertMode) {
            if (tv.getName().equals("Copyright"))
              tv.toString();
            if (!meta.containsTagId(tv.getId())) {
              String seetr = "";
              if (index > 0) seetr = " hide";
              row = "<tr class='ifd ifd" + index + seetr + "'><td>##ID##</td><td>##KEY##</td><td>##VALUE##</td></tr>";
              dif = "-";
              row = row.replace("##ID##", tv.getId() + dif);
              row = row.replace("##KEY##", tv.getName());
              row = row.replace("##VALUE##", tv.toString());
              rows += row;
            }
          }
        }
      }
      ifd = ifd.getNextIFD();
      if (ifdcomp != null) ifdcomp = ifdcomp.getNextIFD();
      index++;
    }
    htmlBody = htmlBody.replaceAll("##ROWS_TAGS##", rows);

    // File Structure
    String ul = "<ul>";
    index = 0;
    ifd = td.getFirstIFD();
    while (ifd != null) {
      String typ = " - Main image";
      if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = " - Thumbnail";
      ul += "<li><i class=\"fa fa-file-o\"></i> <a href='javascript:void(0)' onclick='showIfd("+index+++")'>" + ifd.toString() + typ + "</a>";
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

  private static String getDif(int n1, int n2) {
    String dif = "";
    if (n2 != n1) {
      dif = " (" + (n2 > n1 ? "+" : "-") + Math.abs(n2-n1) + ")";
    } else {
      dif = " (=)";
    }
    return dif;
  }

  /**
   * Read showable tags file.
   *
   * @return hashset of tags
   */
  private static HashSet<String> readShowableTags() {
    HashSet<String> hs = new HashSet<String>();
    try {
      Path path = Paths.get("./src/main/resources/");
      if (Files.exists(path)) {
        // Look in current dir
        FileReader fr = new FileReader("./src/main/resources/htmltags.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
          String[] fields = line.split("\t");
          if (fields.length == 1) {
            hs.add(fields[0]);
          }
          line = br.readLine();
        }
        br.close();
        fr.close();
      } else {
        // Look in JAR
        CodeSource src = ReportHtml.class.getProtectionDomain().getCodeSource();
        if (src != null) {
          URL jar = src.getLocation();
          ZipInputStream zip = new ZipInputStream(jar.openStream());
          ZipEntry zipFile;
          while ((zipFile = zip.getNextEntry()) != null) {
            String name = zipFile.getName();
            if (name.equals("htmltags.txt")) {
              try {
                BufferedReader br = new BufferedReader(new InputStreamReader(zip));
                String line = br.readLine();
                while (line != null) {
                  String[] fields = line.split("\t");
                  if (fields.length == 1) {
                    hs.add(fields[0]);
                  }
                  line = br.readLine();
                }
              } catch (Exception ex) {
                throw new Exception("");
              }
            }
          }
        } else {
          throw new Exception("");
        }
      }
    } catch (Exception ex) {
    }
    return hs;
  }

  /**
   * Show Tag.
   *
   * @return true, if successful
   */
  private static boolean showTag(TagValue tv) {
    HashSet<String> showableTags = readShowableTags();
    /*showableTags.add("ImageWidth");
    showableTags.add("ImageLength");
    showableTags.add("BitsPerSample");
    showableTags.add("Compression");
    showableTags.add("PhotometricInterpretation");
    showableTags.add("ImageDescription");
    showableTags.add("Make");
    showableTags.add("Model");
    showableTags.add("Orientation");
    showableTags.add("SamplesPerPixel");
    showableTags.add("XResolution");
    showableTags.add("YResolution");
    showableTags.add("ResolutionUnit");
    showableTags.add("PlanarConfiguration");
    showableTags.add("Software");
    showableTags.add("DateTime");
    showableTags.add("Artist");
    showableTags.add("Copyright");
    showableTags.add("DateTimeOriginal");
    showableTags.add("Flash");
    showableTags.add("TIFFEPStandardID");*/
    //if (tv.getName().equals(""+tv.getId())) return false;
    return showableTags.contains(tv.getName());
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
    Double rest = 100.0;
    if (ir.hasEpValidation()) rest -= ir.getEPErrors().size() * 12.5;
    if (ir.hasItValidation()) rest -= ir.getITErrors().size() * 12.5;
    if (ir.hasBlValidation()) rest -= ir.getBaselineErrors().size() * 12.5;
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

      if (ir.hasItValidation()) {
        imageBody = imageBody.replace("##IT_ERR_N##", "" + ir.getITErrors().size());
        imageBody = imageBody.replace("##IT_WAR_N##", "" + ir.getITWarnings().size());
      } else {
        imageBody = imageBody.replace("##IT_CLASS##", "hide");
      }

      if (ir.checkPC) {
        imageBody = imageBody.replace("##PC_ERR_N##", "" + ir.getPCErrors().size());
        imageBody = imageBody.replace("##PC_WAR_N##", "" + ir.getPCWarnings().size());
      } else {
        imageBody = imageBody.replace("##PC_CLASS##", "hide");
      }

      imageBody = imageBody.replace("##HREF##", "html/" + ir.getFileName() + ".html");
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
      if (ir.getITErrors().size() > 0) {
        imageBody = imageBody.replace("##IT_ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##IT_ERR_C##", "");
      }
      if (ir.getITWarnings().size() > 0) {
        imageBody = imageBody.replace("##IT_WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##IT_WAR_C##", "");
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

    // Parse the sumary report
    // numbers
    String htmlBody = ReportGenerator.readFile(templatePath);
    Double doub = 1.0 * gr.getReportsOk() / gr.getReportsCount() * 100.0;
    int globalPercent = doub.intValue();
    htmlBody = htmlBody.replace("##IMAGES_LIST##", imagesBody);
    htmlBody = htmlBody.replace("##PERCENT##", "" + globalPercent);
    htmlBody = htmlBody.replace("##COUNT##", "" + gr.getReportsCount());
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

    if (gr.getHasIt()){
      htmlBody = htmlBody.replaceAll("##IT_OK##", "" + gr.getReportsIt());
      htmlBody = htmlBody.replaceAll("##IT_TYP##", gr.getReportsIt() == gr.getReportsCount() ? "success" : "error");
    } else {
      htmlBody = htmlBody.replaceAll("##ROW_IT##", "hide");
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
