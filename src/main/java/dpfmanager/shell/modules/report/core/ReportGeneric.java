/**
 * <h1>ReportGeneric.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.report.core;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/**
 * Created by easy on 19/10/2015.
 */
public class ReportGeneric {

  protected DpfContext context;

  public DpfContext getContext() {
    return context;
  }

  public void setContext(DpfContext context) {
    this.context = context;
  }

  /**
   * Tiff 2 jpg.
   *
   * @param inputfile  the inputfile
   * @param outputfile the outputfile
   * @return true, if successful
   */
  protected boolean tiff2Jpg(String inputfile, String outputfile) {
    File outfile = new File(outputfile);
    if (outfile.exists()) {
      return true;
    }
    try {
      BufferedImage image = ImageIO.read(new File(inputfile));
      //BufferedImage image = loadImageCrazyFast(new File(inputfile).toURI().toURL());

      double factor = 1.0;

      // Scale width
      int width = image.getWidth();
      int height = image.getHeight();
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

      BufferedImage img = scale(image, width, height);

      File outputFile = new File(outputfile);
      outputFile.getParentFile().mkdirs();
      ImageIO.write(img, "jpg", outputFile);
      img.flush();
      img = null;
      image.flush();
      image = null;
      //System.gc();
    } catch (Exception e) {
      return false;
    }
    return true;
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
      printOut("File " + pathStr + " not found in dir.");
    }

    return fis;
  }

  public void printOut(String content){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(this.getClass(), Level.DEBUG, content));
  }
}
