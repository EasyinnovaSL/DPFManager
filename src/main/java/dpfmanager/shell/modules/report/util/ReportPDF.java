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
 * (c) statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> (c)
 * 2015 Easy Innova, SL </p>
 *
 * @author Victor Munoz Sola
 * @version 1.0
 * @since 16/10/2015
 */


package dpfmanager.shell.modules.report.util;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.ReportGeneric;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.color.PDGamma;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * The class Report PDF
 */
public class ReportPDF extends ReportGeneric {

  /**
   * The Init posy.
   */
  static int init_posy = 800;

  /**
   * Parse a global report to PDF format.
   *
   * @param pdffile the file name.
   * @param gr      the global report.
   */
  public void parseGlobal(String pdffile, GlobalReport gr, java.util.List<SmallIndividualReport> reports) {
    try {
      PDFParams pdfParams = new PDFParams();
      pdfParams.init(PDRectangle.A4);

      PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
      int pos_x = 200;
      pdfParams.y = 700;
      int font_size = 18;
      // Logo
      InputStream imageInputStream = getFileStreamFromResources("images/logo.jpg");
      PDImageXObject ximage = PDImageXObject.createFromByteArray(pdfParams.getDocument(), IOUtils.toByteArray(imageInputStream), "images/logo.jpg");
      float scale = 3;
      pdfParams.getContentStream().drawImage(ximage, pos_x, pdfParams.y, 645 / scale, 300 / scale);

      // Report Title
      pdfParams.y -= 30;
      pdfParams = writeText(pdfParams, "MULTIPLE REPORT", pos_x, font, font_size);
      pdfParams.y -= 30;
      font_size = 15;
      pdfParams = writeText(pdfParams, "Processed files: " + gr.getReportsCount(), pos_x, font, font_size, Color.cyan);

      // Conforms table
      pos_x = 100;
      pdfParams.y -= 15;
      font_size = 8;
      Color color;
      for (String iso : gr.getCheckedIsos()) {
        if (gr.getSelectedIsos().contains(iso) || gr.getReportsOk(iso) == gr.getReportsCount()) {
          String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
          String policy = "";
          if (gr.hasModificationIso(iso)) {
            policy = gr.getReportsOk(iso) == gr.getReportsOkPolicy(iso) ? "" : " (with custom policy)";
          }
          pdfParams.y -= 15;
          color = (gr.hasModificationIso(iso) ? gr.getReportsOkPolicy(iso) : gr.getReportsOk(iso)) == gr.getReportsCount() ? Color.green : Color.red;
          pdfParams = writeText(pdfParams, (gr.hasModificationIso(iso) ? gr.getReportsOkPolicy(iso) : gr.getReportsOk(iso)) + " files conforms to " + name + policy, pos_x, font, font_size, color);
        }
      }

      // Pie chart
      pdfParams.y += 10;
      if (pdfParams.y > 565) pdfParams.y = 565;
      pos_x += 200;
      int graph_size = 40;
      BufferedImage image = new BufferedImage(graph_size * 10, graph_size * 10, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();
      Double doub = (double) gr.getAllReportsOk() / gr.getReportsCount();
      double extent = 360d * doub;
      g2d.setColor(Color.green);
      g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360, Arc2D.PIE));
      g2d.setColor(Color.red);
      g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360 - extent, Arc2D.PIE));
      ximage = JPEGFactory.createFromImage(pdfParams.getDocument(), image);
      pdfParams.getContentStream().drawImage(ximage, pos_x, pdfParams.y, graph_size, graph_size);
      pdfParams.y += graph_size - 10;
      font_size = 7;
      pdfParams = writeText(pdfParams, gr.getAllReportsOk() + " passed", pos_x + 50, font, font_size, Color.green);
      pdfParams.y -= 10;
      pdfParams = writeText(pdfParams, gr.getAllReportsKo() + " failed", pos_x + 50, font, font_size, Color.red);
      pdfParams.y -= 10;
      pdfParams = writeText(pdfParams, "Global score " + (int) (doub * 100) + "%", pos_x + 50, font, font_size, Color.black);

      /**
       * Individual Tiff images list
       */
      pos_x = 100;
      pdfParams.y -= 50;
      for (SmallIndividualReport ir : reports) {
        int image_height = 65;
        int image_width = 100;

        // Draw image
        String imgPath = ir.getInternalReportFodler() + "/html/" + ir.getImagePath();
        BufferedImage bimg;
        FileInputStream fis = null;
        if (!new File(imgPath).exists()) {
          bimg = ImageIO.read(getFileStreamFromResources("html/img/not-found.jpg"));
        } else {
          fis = new FileInputStream(imgPath);
          bimg = ImageIO.read(fis);
        }
        image_width = image_height * bimg.getWidth() / bimg.getHeight();
        if (image_width > 100) {
          image_width = 100;
          image_height = image_width * bimg.getHeight() / bimg.getWidth();
        }

        // Check if we need new page before draw image
        int maxHeight = getMaxHeight(ir, gr, image_height);
        if (newPageNeeded(pdfParams.y - maxHeight)) {
          PDPage page = newPage(pdfParams.getContentStream(), pdfParams.getDocument());
          pdfParams.setPage(page);
          pdfParams.setContentStream( new PDPageContentStream(pdfParams.getDocument(), pdfParams.getPage()));
          pdfParams.y = init_posy;
        }

        int initialy = pdfParams.y;
        int initialx = 100;

        pdfParams.y -= image_height;
        int maxy = pdfParams.y;

        ximage = JPEGFactory.createFromImage(pdfParams.getDocument(), bimg);
        pdfParams.getContentStream().drawImage(ximage, pos_x, pdfParams.y, image_width, image_height);
        if (fis != null) fis.close();

        // Values
        image_width = initialx;
        pdfParams.y = initialy - 5;
        if (maxHeight == 75) {
          pdfParams.y -= 5;
        }
        pdfParams = writeText(pdfParams, ir.getFileName(), pos_x + image_width + 10, font, font_size, Color.gray);
        font_size = 6;
        pdfParams.y -= 10;
        pdfParams = writeText(pdfParams, "Conformance Checker", pos_x + image_width + 10, font, font_size, Color.black);
        pdfParams.getContentStream().moveTo(pos_x + image_width + 10, pdfParams.y - 5);
        pdfParams.getContentStream().lineTo(pos_x + image_width + 170, pdfParams.y - 5);
        pdfParams.getContentStream().stroke();
        int preChart = pdfParams.y;

        // Isos table
        pdfParams.y = preChart;
        int mode = (gr.getModifiedIsos().size() != 0) ? 2 : 1;
        int col1 = 100, col2 = 140;
        if (!ir.isQuick() && mode == 2) {
          pdfParams = writeText(pdfParams, "Standard", pos_x + image_width + col1, font, font_size);
          pdfParams = writeText(pdfParams, "Policy", pos_x + image_width + col2, font, font_size);
        } else if (ir.isQuick()) {
          pdfParams = writeText(pdfParams, "Result", pos_x + image_width + col2, font, font_size);
        }
        pdfParams.y -= 2;
        int totalWarnings = 0;
        for (String iso : gr.getCheckedIsos()) {
          if (gr.hasValidation(iso) || ir.getNErrors(iso) == 0) {
            String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
            pdfParams.y -= 5;
            if (!ir.isQuick() && mode == 1) {
              pdfParams.y -= 10;
              pdfParams = writeText(pdfParams, name, pos_x + image_width + 10, font, font_size, Color.black);
              pdfParams = writeText(pdfParams, ir.getNErrors(iso) + " errors", pos_x + image_width + col1, font, font_size, ir.getNErrors(iso) > 0 ? Color.red : Color.black);
              pdfParams = writeText(pdfParams, ir.getNWarnings(iso) + " warnings", pos_x + image_width + col2, font, font_size, ir.getNWarnings(iso) > 0 ? Color.orange : Color.black);
              if (ir.getNWarnings(iso) > 0) {
                totalWarnings++;
              }
            } else if (!ir.isQuick() && mode == 2) {
              pdfParams.y -= 15;
              pdfParams = writeText(pdfParams, name, pos_x + image_width + 10, font, font_size, Color.black);
              pdfParams.y += 5;
              // Errors
              pdfParams = writeText(pdfParams, ir.getNErrors(iso) + " errors", pos_x + image_width + col1, font, font_size, ir.getNErrors(iso) > 0 ? Color.red : Color.black);
              if (gr.hasModifiedIso(iso)) {
                pdfParams = writeText(pdfParams, ir.getNErrorsPolicy(iso) + " errors", pos_x + image_width + col2, font, font_size, ir.getNErrorsPolicy(iso) > 0 ? Color.red : ir.getNErrors(iso) > 0 ? Color.green : Color.black);
              }
              // Warnings
              pdfParams.y -= 8;
              pdfParams = writeText(pdfParams, ir.getNWarnings(iso) + " warnings", pos_x + image_width + col1, font, font_size, ir.getNWarnings(iso) > 0 ? Color.orange : Color.black);
              if (gr.hasModifiedIso(iso)) {
                pdfParams = writeText(pdfParams, ir.getNWarningsPolicy(iso) + " warnings", pos_x + image_width + col2, font, font_size, ir.getNWarningsPolicy(iso) > 0 ? Color.orange : ir.getNWarnings(iso) > 0 ? Color.green : Color.black);
              }
              if (ir.getNWarnings(iso) > 0) {
                totalWarnings++;
              }
            } else if (ir.isQuick()){
              int modeSpecific = (gr.hasModificationIso(iso)) ? 2 : 1;
              int errorsCount = (modeSpecific == 1) ? ir.getNErrors(iso) : ir.getNErrorsPolicy(iso);
              String resultName = (errorsCount == 0) ? "Passed" : "Failed";
              pdfParams.y -= 10;
              pdfParams = writeText(pdfParams, name, pos_x + image_width + 10, font, font_size, Color.black);

              if (modeSpecific == 2) {
                pdfParams.y -= 8;
                pdfParams = writeText(pdfParams, "with custom policy", pos_x + image_width + 10, font, font_size, Color.black);
                pdfParams.y += 4;
              }

              pdfParams = writeText(pdfParams, resultName, pos_x + image_width + col2, font, font_size,  (errorsCount == 0) ? Color.green : Color.red);
              if (modeSpecific == 2) {
                pdfParams.y -= 4;
              }
            }
          }
        }
        if (pdfParams.y < maxy) maxy = pdfParams.y;

        // Chart
        pdfParams.y = initialy;
        pdfParams.y -= 10;
        pdfParams.y -= 10;
        graph_size = 25;
        image = new BufferedImage(graph_size * 10, graph_size * 10, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        doub = (double) ir.getPercent();
        extent = 360d * doub / 100.0;
        g2d.setColor(Color.gray);
        g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360, Arc2D.PIE));
        g2d.setColor(Color.red);
        g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360 - extent, Arc2D.PIE));
        ximage = JPEGFactory.createFromImage(pdfParams.getDocument(), image);
        pdfParams.getContentStream().drawImage(ximage, pos_x + image_width + 180, pdfParams.y - graph_size, graph_size, graph_size);
        pdfParams.y += graph_size - 10;
        if (doub < 100) {
          pdfParams.y = pdfParams.y - 10 - graph_size / 2;
          pdfParams = writeText(pdfParams, "Failed", pos_x + image_width + 180 + graph_size + 10, font, font_size, Color.red);
        } else {
          pdfParams.y = pdfParams.y - 10 - graph_size / 2;
          pdfParams = writeText(pdfParams, "Passed", pos_x + image_width + 180 + graph_size + 10, font, font_size, Color.green);
          if (totalWarnings > 0 ) {
            int size = (int) getSize(font_size, font, "Passed");
            pdfParams = writeText(pdfParams, " with warnings", pos_x + image_width + 180 + graph_size + 10 + size, font, font_size, Color.orange);
          }
        }
        pdfParams.y = pdfParams.y - 10;
        pdfParams = writeText(pdfParams, "Score " + doub + "%", pos_x + image_width + 180 + graph_size + 10, font, font_size, Color.gray);
        if (pdfParams.y < maxy) maxy = pdfParams.y;

        // Link to individual PDF
        pdfParams.y = initialy - image_height - 10;
        pdfParams = writeLink(pdfParams, "View the full individual report.", ir.getReportPath() + ".pdf", pos_x, font, font_size);

        if (pdfParams.y > maxy) pdfParams.y = maxy;
        pdfParams.y =  pdfParams.y - 15;
      }

      pdfParams.getContentStream().close();

      pdfParams.getDocument().save(pdffile);
      pdfParams.getDocument().close();

    } catch (Exception tfe) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in ReportPDF", tfe));
    }
  }

  private int getMaxHeight(SmallIndividualReport ir, GlobalReport gr, int image_height) {
    int height = 22;
    int link = 10;
    for (String iso : gr.getCheckedIsos()) {
      if (gr.hasValidation(iso) || ir.getNErrors(iso) == 0) {
        height += 15;
      }
    }
    if (image_height + link > height) {
      height = image_height + link;
    }
    return height;
  }

  /**
   * New page needed boolean.
   *
   * @param pos_y the pos y
   * @return the boolean
   */
  boolean newPageNeeded(int pos_y) {
    return pos_y < 100;
  }

  /**
   * New page pd page content stream.
   *
   * @param contentStream the content stream
   * @param document      the document
   * @return the pd page content stream
   * @throws Exception the exception
   */
  PDPage newPage(PDPageContentStream contentStream, PDDocument document) throws Exception {
    contentStream.close();
    PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);
    return page;
  }

  /**
   * Write text.
   *
   * @param pdfParams the content stream and document
   * @param text      the text
   * @param x         the x
   * @param font      the font
   * @param font_size the font size
   * @throws Exception the exception
   */
  private PDFParams writeText(PDFParams pdfParams, String text, int x, PDFont font, int font_size) throws Exception {
    return writeText(pdfParams, text, x, font, font_size, Color.black);
  }

  /**
   * Write text.
   *
   * @param pdfParams the content stream and document
   * @param text      the text
   * @param x         the x
   * @param font      the font
   * @param font_size the font size
   * @param color     the color
   * @throws Exception the exception
   */
  private PDFParams writeText(PDFParams pdfParams, String text, int x, PDFont font, int font_size, Color color) throws Exception {
    PDPageContentStream contentStream = pdfParams.getContentStream();
    try {
      if (newPageNeeded(pdfParams.y)) {
        PDPage page = newPage(contentStream, pdfParams.getDocument());
        pdfParams.setPage(page);
        contentStream = new PDPageContentStream(pdfParams.getDocument(), pdfParams.getPage());
        pdfParams.y = init_posy;
      }

      contentStream.beginText();
      contentStream.setFont(font, font_size);
      contentStream.setNonStrokingColor(color);
      contentStream.newLineAtOffset(x, pdfParams.y);
      contentStream.showText(text);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      contentStream.endText();
      pdfParams.setContentStream(contentStream);
      return pdfParams;
    }
  }

  private PDFParams writeLink(PDFParams pdfParams, String text, String link, int pos_x, PDFont font, int font_size) throws Exception {
    PDBorderStyleDictionary borderULine = new PDBorderStyleDictionary();
    borderULine.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
    PDAnnotationLink txtLink = new PDAnnotationLink();
    txtLink.setBorderStyle(borderULine);
    float[] rgbBlue = new float[] {0.192156f, 0.290196f, 0.592157f};
    PDColor blueColor = new PDColor(rgbBlue, PDDeviceRGB.INSTANCE);
    txtLink.setColor(blueColor);

    // add an action
    PDActionURI action = new PDActionURI();
    action.setURI("file:///"+link);
    txtLink.setAction(action);

    // print it
    pdfParams = writeText(pdfParams, text, pos_x, font, font_size, Color.blue);
    PDRectangle position = new PDRectangle();
    position.setLowerLeftX(pos_x);
    position.setLowerLeftY(pdfParams.y + 8);
    position.setUpperRightX(pos_x + getSize(font_size, font, text));
    position.setUpperRightY(pdfParams.y-2);
    txtLink.setRectangle(position);
    pdfParams.getPage().getAnnotations().add(txtLink);

    return pdfParams;
  }

  private float getSize(int font_size, PDFont font, String text) throws IOException {
    return font_size * font.getStringWidth(text) / 1000;
  }
}
