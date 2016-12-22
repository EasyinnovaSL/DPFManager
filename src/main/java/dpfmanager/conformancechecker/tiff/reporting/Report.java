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
 * @author Victor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.util.ReportHtml;
import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.google.common.util.concurrent.SimpleTimeLimiter;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

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

  /**
   * Read showable tags file.
   *
   * @return hashset of tags
   */
  protected HashSet<String> readShowableTags() {
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
        String resource = "htmltags.txt";
        Class cls = ReportHtml.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(resource);
        //CodeSource src = ReportHtml.class.getProtectionDomain().getCodeSource();
        if (in != null) {
          try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            while (line != null) {
              String[] fields = line.split("\t");
              if (fields.length == 1) {
                hs.add(fields[0]);
              }
              line = br.readLine();
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        } else {
          throw new Exception("InputStream is null");
        }
      }
    } catch (Exception ex) {
    }
    return hs;
  }

  /**
   * Show Tag.
   *
   * @param tv The tag value
   * @return true, if successful
   */
  protected boolean showTag(TagValue tv) {
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
    //if (tv.getFileName().equals(""+tv.getId())) return false;
    return showableTags.contains(tv.getName());
  }

  /**
   * Gets tags.
   *
   * @param ir the ir
   * @return the tags
   */
  protected ArrayList<ReportTag> getTags(IndividualReport ir) {
    ArrayList<ReportTag> list = new ArrayList<ReportTag>();
    TiffDocument td = ir.getTiffModel();
    IFD ifd = td.getFirstIFD();
    IFD ifdcomp = null;
    if (ir.getCompareReport() != null) {
      ifdcomp = ir.getCompareReport().getTiffModel().getFirstIFD();
    }
    td.getFirstIFD();
    int index = 0;
    while (ifd != null) {
      IfdTags meta = ifd.getMetadata();
      for (TagValue tv : meta.getTags()) {
        ReportTag tag = new ReportTag();
        tag.index = index;
        tag.tv = tv;
        if (ifdcomp != null) {
          if (!ifdcomp.getMetadata().containsTagId(tv.getId()))
            tag.dif = 1;
        }
        if (!showTag(tv)) tag.expert = true;
        list.add(tag);
      }
      if (ifdcomp != null) {
        for (TagValue tv : ifdcomp.getMetadata().getTags()) {
          if (!meta.containsTagId(tv.getId())) {
            ReportTag tag = new ReportTag();
            tag.index = index;
            tag.tv = tv;
            tag.dif = -1;
            if (!showTag(tv)) tag.expert = true;
            list.add(tag);
          }
        }
      }
      ifd = ifd.getNextIFD();
      if (ifdcomp != null) ifdcomp = ifdcomp.getNextIFD();
      index++;
    }
    return list;
  }

  protected Map<String, List<ReportTag>> parseTags(IndividualReport ir) {
    /**
     * Parse Tags
     */
    Map<String, List<ReportTag>> tags = new HashMap<>();
    for (ReportTag tag : getTags(ir)) {
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
        IFD sub = (IFD) tag.tv.getValue().get(0);
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
}
