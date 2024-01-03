/**
 * <h1>Report.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Victor Muñoz Sola
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by easy on 06/05/2016.
 */
public class Report {
  BufferedImage buffer;

  /**
   * Tiff 2 jpg.
   *
   * @param inputfile  the inputfile
   * @return true, if successful
   */
  protected BufferedImage tiff2Jpg(String inputfile) {
    try {
      buffer = ImageIO.read(new File(inputfile));

      double factor = 1.0;

      // Scale width
      int width = buffer.getWidth();
      int height = buffer.getHeight();
      if (width > 500) {
        factor = 500.0 / width;
      }
      height = (int) (height * factor);
      width = (int) (width * factor);

      // Scale height
      if (height > 500) {
        factor = 500.0 / height;
        height = (int) (height * factor);
        width = (int) (width * factor);
      }

      BufferedImage img = scale(buffer, width, height);
      return img;
    } catch (OutOfMemoryError error) {
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  BufferedImage scale(BufferedImage src, int w, int h) {
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    int x, y;
    int ww = src.getWidth();
    int hh = src.getHeight();
    for (x = 0; x < w; x++) {
      for (y = 0; y < h; y++) {
        int col = src.getRGB(x * ww / w, y * hh / h);
        img.setRGB(x, y, col);
      }
    }
    return img;
  }


  /**
   * Gets dif.
   *
   * @param n1 the n 1
   * @param n2 the n 2
   * @return the dif
   */
  protected String getDif(int n1, int n2) {
    String dif = "";
    if (n2 != n1) {
      dif = " (" + (n2 > n1 ? "+" : "-") + Math.abs(n2 - n1) + ")";
    } else {
      dif = " (=)";
    }
    return dif;
  }

  /**
   * Read filefrom resources string.
   *
   * @param pathStr the path str
   * @return the string
   */
  public InputStream getFileStreamFromResources(String pathStr) {
    InputStream fis = null;
    Path path = Paths.get(pathStr);
    try {
      if (Files.exists(path)) {
        // Look in current dir
        fis = new FileInputStream(pathStr);
      } else {
        // Look in JAR
        Class cls = ReportGenerator.class;
        ClassLoader cLoader = cls.getClassLoader();
        fis = cLoader.getResourceAsStream(pathStr);
      }
    } catch (FileNotFoundException e) {
      //printOut("File " + pathStr + " not found in dir.");
    }

    return fis;
  }

  public byte[] getByteArrayFileStreamFromResources(String pathStr) {
    try {
      return IOUtils.toByteArray(getFileStreamFromResources(pathStr));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected Map<String, List<ReportTag>> parseTags(IndividualReport ir) {
    /**
     * Parse Tags
     */
    Map<String, List<ReportTag>> tags = new HashMap<>();
    for (ReportTag tag : ir.getTags(false, true)) {
      String mapId;
      List<ReportTag> list = null;
      if (tag.tv.getId() == 700) {
        mapId = "xmp" + tag.index;
        list = tags.containsKey(mapId) ? tags.get(mapId) : new ArrayList<>();
      } else if (tag.tv.getId() == 33723) {
        mapId = "ipt" + tag.index;
        list = tags.containsKey(mapId) ? tags.get(mapId) : new ArrayList<>();
      } else if (tag.tv.getId() == 34665) {
        mapId = "exi" + tag.index;
        list = tags.containsKey(mapId) ? tags.get(mapId) : new ArrayList<>();
      } else if (tag.tv.getId() == 330) {
        mapId = "sub" + tag.index;
        list = tags.containsKey(mapId) ? tags.get(mapId) : new ArrayList<>();
      } else if (tag.isDefault) {
        mapId = "ifd" + tag.index + "d";
        list = tags.containsKey(mapId) ? tags.get(mapId) : new ArrayList<>();
      } else if (tag.expert) {
        mapId = "ifd" + tag.index + "e";
        list = tags.containsKey(mapId) ? tags.get(mapId) : new ArrayList<>();
      } else {
        mapId = "ifd" + tag.index;
        list = tags.containsKey(mapId) ? tags.get(mapId) : new ArrayList<>();
      }
      if (list != null) {
        list.add(tag);
        tags.put(mapId, list);
      }
    }
    return tags;
  }

  public String getThumbnailPath(String internalReportFolder, String fileName, IndividualReport ir){
    String imgPath = "img/" + fileName + ".gif";
    File outputFile = new File(internalReportFolder + "/html/" + imgPath);
    outputFile.getParentFile().mkdirs();
    if (outputFile.exists()) return imgPath;
    if (!new File(ir.getFilePath()).exists()) return "img/not-found.jpg";
    if (!ir.getTiffModel().getFatalError()) {
      try {
        // Make thumbnail
        BufferedImage thumb = tiff2Jpg(ir.getFilePath());
        if (thumb == null) {
          copyFile("img/error.jpg", internalReportFolder, imgPath);
          return imgPath;
        } else {
          // Save thumbnail
          ImageIO.write(thumb, "gif", outputFile);
          buffer.flush();
          buffer = null;
          thumb.flush();
          thumb = null;
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        try {
          copyFile("img/error.jpg", internalReportFolder, imgPath);
          return imgPath;
        } catch (Exception e){
          imgPath = "img/error.jpg";
        }
      } catch (Error err) {
        err.printStackTrace();
        try {
          copyFile("img/error.jpg", internalReportFolder, imgPath);
          return imgPath;
        } catch (Exception e){
          imgPath = "img/error.jpg";
        }
      }
    } else {
      try {
        copyFile("img/error.jpg", internalReportFolder, imgPath);
        return imgPath;
      } catch (Exception e){
        imgPath = "img/error.jpg";
      }
    }
    return imgPath;
  }

  public void copyFile(String filePath, String internalReportFolder, String targetPath) {
    String pathStr = "./src/main/resources/html/" + filePath;
    File targetFile = new File(internalReportFolder + "/html/" + targetPath);
    Path path = Paths.get(pathStr);
    if (Files.exists(path)) {
      // Look in current dir
      File srcFile = new File(pathStr);
      if (srcFile.exists() && srcFile.isFile()) {
        try {
          org.apache.commons.io.FileUtils.copyFile(srcFile, targetFile);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      // Look in JAR
      CodeSource src = ReportGenerator.class.getProtectionDomain().getCodeSource();
      if (src != null) {
        try {
          // if originals folders does not exist
          targetFile.getParentFile().mkdirs();

          // copy file
          Class cls = ReportGenerator.class;
          ClassLoader cLoader = cls.getClassLoader();
          InputStream in = cLoader.getResourceAsStream("html/"+filePath);
          if(in != null) {
            int readBytes;
            byte[] buffer = new byte[4096];
            OutputStream resStreamOut = new FileOutputStream(targetFile);
            while ((readBytes = in.read(buffer)) > 0) {
              resStreamOut.write(buffer, 0, readBytes);
            }
            resStreamOut.close();
            in.close();
          }
        } catch (Exception e) {
        }
      }
    }
  }

}
