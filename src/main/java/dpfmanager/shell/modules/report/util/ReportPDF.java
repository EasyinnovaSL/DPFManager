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

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.core.ReportGeneric;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
  public void parseGlobal(String pdffile, GlobalReport gr) {
//    try {
//      PDFParams pdfParams = new PDFParams();
//      pdfParams.init(PDPage.PAGE_SIZE_A4);
//
//      PDFont font = PDType1Font.HELVETICA_BOLD;
//      int pos_x = 200;
//      pdfParams.y = 700;
//      int font_size = 18;
//      // Logo
//      PDXObjectImage ximage = new PDJpeg(pdfParams.getDocument(), getFileStreamFromResources("images/logo.jpg"));
//      float scale = 3;
//      pdfParams.getContentStream().drawXObject(ximage, pos_x, pdfParams.y, 645 / scale, 300 / scale);
//
//      // Report Title
//      pdfParams.y -= 30;
//      pdfParams = writeText(pdfParams, "MULTIPLE REPORT", pos_x, font, font_size);
//      pdfParams.y -= 30;
//      font_size = 15;
//      pdfParams = writeText(pdfParams, "Processed files: " + gr.getIndividualReports().size(), pos_x, font, font_size, Color.cyan);
//
//      // Summary table
//      pos_x = 100;
//      pdfParams.y -= 15;
//      font_size = 8;
//      Color col = gr.getReportsPc() == gr.getReportsCount() ? Color.green : Color.red;
//      if (gr.getHasBl()) {
//        pdfParams.y -= 15;
//        col = gr.getReportsBl() == gr.getReportsCount() ? Color.green : Color.red;
//        pdfParams = writeText(pdfParams, gr.getReportsBl() + "", pos_x, font, font_size, col);
//        pdfParams = writeText(pdfParams, "conforms to Baseline Profile", pos_x + 30, font, font_size, col);
//      }
//      if (gr.getHasEp()) {
//        pdfParams.y -= 15;
//        col = gr.getReportsEp() == gr.getReportsCount() ? Color.green : Color.red;
//        pdfParams = writeText(pdfParams, gr.getReportsEp() + "", pos_x, font, font_size, col);
//        pdfParams = writeText(pdfParams, "conforms to Tiff/EP Profile", pos_x + 30, font, font_size, col);
//      }
//      if (gr.getHasIt0()) {
//        pdfParams.y -= 15;
//        col = gr.getReportsIt0() == gr.getReportsCount() ? Color.green : Color.red;
//        pdfParams = writeText(pdfParams, gr.getReportsIt0() + "", pos_x, font, font_size, col);
//        pdfParams = writeText(pdfParams, "conforms to Tiff/IT Profile", pos_x + 30, font, font_size, col);
//      }
//      if (gr.getHasIt1()) {
//        pdfParams.y -= 15;
//        col = gr.getReportsIt1() == gr.getReportsCount() ? Color.green : Color.red;
//        pdfParams = writeText(pdfParams, gr.getReportsIt2() + "", pos_x, font, font_size, col);
//        pdfParams = writeText(pdfParams, "conforms to Tiff/IT P1 Profile", pos_x + 30, font, font_size, col);
//      }
//      if (gr.getHasIt2()) {
//        pdfParams.y -= 15;
//        col = gr.getReportsIt2() == gr.getReportsCount() ? Color.green : Color.red;
//        pdfParams = writeText(pdfParams, gr.getReportsIt2() + "", pos_x, font, font_size, col);
//        pdfParams = writeText(pdfParams, "conforms to Tiff/IT P2 Profile", pos_x + 30, font, font_size, col);
//      }
//      if (gr.getHasPc()) {
//        pdfParams.y -= 15;
//        pdfParams = writeText(pdfParams, gr.getReportsPc() + "", pos_x, font, font_size, col);
//        pdfParams = writeText(pdfParams, "conforms to Policy checker", pos_x + 30, font, font_size, col);
//      }
//
//      // Pie chart
//      pdfParams.y += 10;
//      if (pdfParams.y > 565) pdfParams.y = 565;
//      pos_x += 200;
//      int graph_size = 40;
//      BufferedImage image = new BufferedImage(graph_size * 10, graph_size * 10, BufferedImage.TYPE_INT_ARGB);
//      Graphics2D g2d = image.createGraphics();
//      Double doub = (double) gr.getReportsOk() / gr.getReportsCount();
//      double extent = 360d * doub;
//      g2d.setColor(Color.green);
//      g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360, Arc2D.PIE));
//      g2d.setColor(Color.red);
//      g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360 - extent, Arc2D.PIE));
//      ximage = new PDJpeg(pdfParams.getDocument(), image);
//      pdfParams.getContentStream().drawXObject(ximage, pos_x, pdfParams.y, graph_size, graph_size);
//      pdfParams.y += graph_size - 10;
//      font_size = 7;
//      pdfParams = writeText(pdfParams, gr.getReportsOk() + " passed", pos_x + 50, font, font_size, Color.green);
//      pdfParams.y -= 10;
//      pdfParams = writeText(pdfParams, gr.getReportsKo() + " failed", pos_x + 50, font, font_size, Color.red);
//      pdfParams.y -= 10;
//      pdfParams = writeText(pdfParams, "Global score " + (doub * 100) + "%", pos_x + 50, font, font_size, Color.black);
//
//      // Individual Tiff images list
//      pos_x = 100;
//      pdfParams.y -= 50;
//      for (IndividualReport ir : gr.getIndividualReports()) {
//          int image_height = 65;
//          int image_width = 100;
//
//          // Draw image
//          String imgPath = pdffile + "img.jpg";
//          int ids = 0;
//          while (new File(imgPath).exists()) imgPath = pdffile + "img" + ids++ + ".jpg";
//          boolean check = tiff2Jpg(ir.getFilePath(), imgPath);
//          BufferedImage bimg;
//          if (!check) {
//            bimg = ImageIO.read(getFileStreamFromResources("html/img/noise.jpg"));
//          } else {
//            bimg = ImageIO.read(new File(imgPath));
//          }
//          image_width = image_height * bimg.getWidth() / bimg.getHeight();
//          if (image_width > 100) {
//            image_width = 100;
//            image_height = image_width * bimg.getHeight() / bimg.getWidth();
//          }
//
//          // Check if we need new page before draw image
//          int maxHeight = getMaxHeight(ir, image_height);
//          if (newPageNeeded(pdfParams.y - maxHeight)) {
//            pdfParams.setContentStream(newPage(pdfParams.getContentStream(), pdfParams.getDocument()));
//            pdfParams.y = init_posy;
//          }
//
//          int initialy = pdfParams.y;
//          int initialx = 100;
//
//          pdfParams.y -= maxHeight;
//          int maxy = pdfParams.y;
//
//          ximage = new PDJpeg(pdfParams.getDocument(), bimg);
//          pdfParams.getContentStream().drawXObject(ximage, pos_x, pdfParams.y, image_width, image_height);
//          if (check) new File(imgPath).delete();
//
//          // Values
//          image_width = initialx;
//          pdfParams.y = initialy;
//          if (maxHeight == 65) {
//            pdfParams.y -= 10;
//          }
//          pdfParams = writeText(pdfParams, ir.getFileName(), pos_x + image_width + 10, font, font_size, Color.gray);
//          font_size = 6;
//          pdfParams.y -= 10;
//          pdfParams = writeText(pdfParams, "Conformance Checker", pos_x + image_width + 10, font, font_size, Color.black);
//          pdfParams.getContentStream().drawLine(pos_x + image_width + 10, pdfParams.y - 5, image_width + 150, pdfParams.y - 5);
//          pdfParams.y -= 2;
//
//          if (ir.hasBlValidation()) {
//            pdfParams.y -= 10;
//            pdfParams = writeText(pdfParams, "Baseline", pos_x + image_width + 10, font, font_size, Color.black);
//            pdfParams = writeText(pdfParams, ir.getBaselineErrors().size() + " errors", pos_x + image_width + 70, font, font_size, ir.getBaselineErrors().size() > 0 ? Color.red : Color.black);
//            pdfParams = writeText(pdfParams, ir.getNBlErr() + " warnings", pos_x + image_width + 120, font, font_size, ir.getNBlErr() > 0 ? Color.red : Color.black);
//          }
//          if (ir.checkEP && ir.hasEpValidation()) {
//            pdfParams.y -= 10;
//            pdfParams = writeText(pdfParams, "Tiff/EP", pos_x + image_width + 10, font, font_size, Color.black);
//            pdfParams = writeText(pdfParams, ir.getEPErrors().size() + " errors", pos_x + image_width + 70, font, font_size, ir.getEPErrors().size() > 0 ? Color.red : Color.black);
//            pdfParams = writeText(pdfParams, ir.getNEpWar() + " warnings", pos_x + image_width + 120, font, font_size, ir.getNEpWar() > 0 ? Color.red : Color.black);
//          }
//          if (ir.checkIT0 && ir.hasItValidation(0)) {
//            pdfParams.y -= 10;
//            pdfParams = writeText(pdfParams, "Tiff/IT", pos_x + image_width + 10, font, font_size, Color.black);
//            pdfParams = writeText(pdfParams, ir.getITErrors(0).size() + " errors", pos_x + image_width + 70, font, font_size, ir.getITErrors(0).size() > 0 ? Color.red : Color.black);
//            pdfParams = writeText(pdfParams, ir.getNItWar(0) + " warnings", pos_x + image_width + 120, font, font_size, ir.getNItWar(0) > 0 ? Color.red : Color.black);
//          }
//          if (ir.checkIT1 && ir.hasItValidation(1)) {
//            pdfParams.y -= 10;
//            pdfParams = writeText(pdfParams, "Tiff/IT-1", pos_x + image_width + 10, font, font_size, Color.black);
//            pdfParams = writeText(pdfParams, ir.getITErrors(1).size() + " errors", pos_x + image_width + 70, font, font_size, ir.getITErrors(1).size() > 0 ? Color.red : Color.black);
//            pdfParams = writeText(pdfParams, ir.getNItWar(1) + " warnings", pos_x + image_width + 120, font, font_size, ir.getNItWar(1) > 0 ? Color.red : Color.black);
//          }
//          if (ir.checkIT2 && ir.hasItValidation(2)) {
//            pdfParams.y -= 10;
//            pdfParams = writeText(pdfParams, "Tiff/IT-2", pos_x + image_width + 10, font, font_size, Color.black);
//            pdfParams = writeText(pdfParams, ir.getITErrors(2).size() + " errors", pos_x + image_width + 70, font, font_size, ir.getITErrors(2).size() > 0 ? Color.red : Color.black);
//            pdfParams = writeText(pdfParams, ir.getNItWar(2) + " warnings", pos_x + image_width + 120, font, font_size, ir.getNItWar(2) > 0 ? Color.red : Color.black);
//          }
//          if (ir.checkPC) {
//            pdfParams.y -= 10;
//            pdfParams = writeText(pdfParams, "Policy checker", pos_x + image_width + 10, font, font_size, Color.black);
//            pdfParams = writeText(pdfParams, ir.getPCErrors().size() + " errors", pos_x + image_width + 70, font, font_size, ir.getPCErrors().size() > 0 ? Color.red : Color.black);
//            pdfParams = writeText(pdfParams, ir.getPCWarnings().size() + " warnings", pos_x + image_width + 120, font, font_size, ir.getPCWarnings().size() > 0 ? Color.red : Color.black);
//          }
//          if (pdfParams.y < maxy) maxy = pdfParams.y;
//
//          // Chart
//          pdfParams.y = initialy;
//          pdfParams.y -= 10;
//          pdfParams.y -= 10;
//          graph_size = 25;
//          image = new BufferedImage(graph_size * 10, graph_size * 10, BufferedImage.TYPE_INT_ARGB);
//          g2d = image.createGraphics();
//          doub = (double) ir.calculatePercent();
//          extent = 360d * doub / 100.0;
//          g2d.setColor(Color.gray);
//          g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360, Arc2D.PIE));
//          g2d.setColor(Color.red);
//          g2d.fill(new Arc2D.Double(0, 0, graph_size * 10, graph_size * 10, 90, 360 - extent, Arc2D.PIE));
//          ximage = new PDJpeg(pdfParams.getDocument(), image);
//          pdfParams.getContentStream().drawXObject(ximage, pos_x + image_width + 180, pdfParams.y - graph_size, graph_size, graph_size);
//          pdfParams.y += graph_size - 10;
//          if (doub < 100) {
//            pdfParams.y = pdfParams.y - 10 - graph_size / 2;
//            pdfParams = writeText(pdfParams, "Failed", pos_x + image_width + 180 + graph_size + 10, font, font_size, Color.red);
//          }
//          pdfParams.y = pdfParams.y - 10 - graph_size / 2;
//          pdfParams = writeText(pdfParams, "Score " + doub + "%", pos_x + image_width + 180 + graph_size + 10, font, font_size, Color.gray);
//          if (pdfParams.y < maxy) maxy = pdfParams.y;
//
//          pdfParams.y = maxy - 10;
//      }
//
//      // Full individual reports
//      ArrayList<PDDocument> toClose = new ArrayList<PDDocument>();
//      for (IndividualReport ir : gr.getIndividualReports()) {
//
//          if (!ir.containsData()) continue;
//          PDDocument doc = null;
//          if (ir.getPDF() != null)
//            doc = ir.getPDF();
//          else if (ir.getPDFDocument() != null)
//            doc = PDDocument.load(ir.getPDFDocument());
//          if (doc != null) {
//            List<PDPage> l = doc.getDocumentCatalog().getAllPages();
//            for (PDPage pag : l) {
//              pdfParams.getDocument().addPage(pag);
//            }
//            toClose.add(doc);
//          }
//      }
//
//      pdfParams.getContentStream().close();
//
//      pdfParams.getDocument().save(pdffile);
//      pdfParams.getDocument().close();
//
//      for (PDDocument doc : toClose) {
//        doc.close();
//      }
//    } catch (Exception tfe) {
//      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in ReportPDF", tfe));
//    }
  }

  private int getMaxHeight(IndividualReport ir, int image_height) {
    int height = 15;
//    if (ir.hasBlValidation()) {
//      height += 10;
//    }
//    if (ir.hasEpValidation()) {
//      height += 10;
//    }
//    if (ir.hasItValidation(0)) {
//      height += 10;
//    }
//    if (ir.hasItValidation(1)) {
//      height += 10;
//    }
//    if (ir.hasItValidation(2)) {
//      height += 10;
//    }
//    if (ir.checkPC) {
//      height += 10;
//    }
//    if (image_height > height) {
//      height = image_height;
//    }
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
  PDPageContentStream newPage(PDPageContentStream contentStream, PDDocument document) throws Exception {
    contentStream.close();
    PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
    document.addPage(page);
    return new PDPageContentStream(document, page);
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
        contentStream = newPage(contentStream, pdfParams.getDocument());
        pdfParams.y = init_posy;
      }

      contentStream.beginText();
      contentStream.setFont(font, font_size);
      contentStream.setNonStrokingColor(color);
      contentStream.moveTextPositionByAmount(x, pdfParams.y);
      contentStream.drawString(text);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      contentStream.endText();
      pdfParams.setContentStream(contentStream);
      return pdfParams;
    }
  }
}
