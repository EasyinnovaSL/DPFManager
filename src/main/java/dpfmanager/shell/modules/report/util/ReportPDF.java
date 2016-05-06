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
 * NB: for the (c) statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * (c) 2015 Easy Innova, SL
 * </p>
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
    try {
      PDDocument document = new PDDocument();

      PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
      document.addPage( page );

      PDFont font = PDType1Font.HELVETICA_BOLD;
      PDPageContentStream contentStream = new PDPageContentStream(document, page);

      int pos_x = 200;
      int pos_y = 700;
      int font_size = 18;
      // Logo
      PDXObjectImage ximage = new PDJpeg(document, getFileStreamFromResources("images/logo.jpg"));
      float scale = 3;
      contentStream.drawXObject( ximage, pos_x, pos_y, 645/scale, 300/scale );

      // Report Title
      pos_y -= 30;
      writeText(contentStream, "MULTIPLE REPORT", pos_x, pos_y, font, font_size);
      pos_y -= 30;
      font_size = 15;
      writeText(contentStream, "Processed files: " + gr.getIndividualReports().size(), pos_x, pos_y, font, font_size, Color.cyan);

      // Summary table
      pos_x = 100;
      pos_y -= 30;
      font_size = 8;
      Color col = gr.getReportsPc() == gr.getReportsCount() ? Color.green : Color.red;
      writeText(contentStream, gr.getReportsPc() + "", pos_x, pos_y, font, font_size, col);
      writeText(contentStream, "conforms to Policy checker", pos_x + 30, pos_y, font, font_size, col);
      if (gr.getHasBl()){
        pos_y -= 15;
        col = gr.getReportsBl() == gr.getReportsCount() ? Color.green : Color.red;
        writeText(contentStream, gr.getReportsBl() + "", pos_x, pos_y, font, font_size, col);
        writeText(contentStream, "conforms to Baseline Profile", pos_x + 30, pos_y, font, font_size, col);
      }
      if (gr.getHasEp()){
        pos_y -= 15;
        col = gr.getReportsEp() == gr.getReportsCount() ? Color.green : Color.red;
        writeText(contentStream, gr.getReportsEp() + "", pos_x, pos_y, font, font_size, col);
        writeText(contentStream, "conforms to Tiff/EP Profile", pos_x + 30, pos_y, font, font_size, col);
      }
      if (gr.getHasIt0()){
        pos_y -= 15;
        col = gr.getReportsIt0() == gr.getReportsCount() ? Color.green : Color.red;
        writeText(contentStream, gr.getReportsIt0() + "", pos_x, pos_y, font, font_size, col);
        writeText(contentStream, "conforms to Tiff/IT Profile", pos_x + 30, pos_y, font, font_size, col);
      }
      if (gr.getHasIt1()){
        pos_y -= 15;
        col = gr.getReportsIt1() == gr.getReportsCount() ? Color.green : Color.red;
        writeText(contentStream, gr.getReportsIt2() + "", pos_x, pos_y, font, font_size, col);
        writeText(contentStream, "conforms to Tiff/IT P1 Profile", pos_x + 30, pos_y, font, font_size, col);
      }
      if (gr.getHasIt2()){
        pos_y -= 15;
        col = gr.getReportsIt2() == gr.getReportsCount() ? Color.green : Color.red;
        writeText(contentStream, gr.getReportsIt2() + "", pos_x, pos_y, font, font_size, col);
        writeText(contentStream, "conforms to Tiff/IT P2 Profile", pos_x + 30, pos_y, font, font_size, col);
      }

      // Pie chart
      pos_y += 10;
      if (pos_y > 565) pos_y = 565;
      pos_x += 200;
      int graph_size = 40;
      BufferedImage image = new BufferedImage(graph_size*10, graph_size*10, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();
      Double doub = (double)gr.getReportsOk() / gr.getReportsCount();
      double extent = 360d * doub;
      g2d.setColor(Color.green);
      g2d.fill(new Arc2D.Double(0, 0, graph_size*10, graph_size*10, 90, 360, Arc2D.PIE));
      g2d.setColor(Color.red);
      g2d.fill(new Arc2D.Double(0, 0, graph_size*10, graph_size*10, 90, 360 - extent, Arc2D.PIE));
      ximage = new PDJpeg(document, image);
      contentStream.drawXObject(ximage, pos_x, pos_y, graph_size, graph_size);
      pos_y += graph_size - 10;
      font_size = 7;
      writeText(contentStream, gr.getReportsOk() + " passed", pos_x + 50, pos_y, font, font_size, Color.green);
      pos_y -= 10;
      writeText(contentStream, gr.getReportsKo() + " failed", pos_x + 50, pos_y, font, font_size, Color.red);
      pos_y -= 10;
      writeText(contentStream, "Global score " + (doub*100) + "%", pos_x + 50, pos_y, font, font_size, Color.black);

      // Individual Tiff images list
      pos_x = 100;
      pos_y -= 50;
      for (IndividualReport ir : gr.getIndividualReports()) {

        if (newPageNeeded(pos_y)) {
          contentStream = newPage(contentStream, document);
          pos_y = init_posy;
        }

        int image_height = 65;
        int image_width = 100;
        pos_y -= 10;

        int initialy = pos_y;
        pos_y -= image_height;
        int maxy = pos_y;

        // Draw image
        String imgPath = pdffile + "img.jpg";
        int ids = 0;
        while (new File(imgPath).exists()) imgPath = pdffile + "img" + ids++ +".jpg";
        boolean check = tiff2Jpg(ir.getFilePath(), imgPath);
        BufferedImage bimg;
        if (!check) {
          bimg = ImageIO.read(getFileStreamFromResources("html/img/noise.jpg"));
        } else {
          bimg = ImageIO.read(new File(imgPath));
        }
        image_width = image_height * bimg.getWidth() / bimg.getHeight();
        ximage = new PDJpeg(document, bimg );
        contentStream.drawXObject( ximage, pos_x, pos_y, image_width, image_height );
        if (check) new File(imgPath).delete();

        // Values
        pos_y = initialy;
        pos_y -= 10;
        writeText(contentStream, ir.getFileName(), pos_x + image_width + 10, pos_y, font, font_size, Color.gray);
        font_size = 6;
        pos_y -= 10;
        writeText(contentStream, "Conformance Checker", pos_x + image_width + 10, pos_y, font, font_size, Color.black);
        contentStream.drawLine(pos_x + image_width + 10,pos_y-5,image_width + 150,pos_y-5);
        pos_y -= 2;

        if (ir.hasBlValidation()) {
          pos_y -= 10;
          writeText(contentStream, "Baseline", pos_x + image_width + 10, pos_y, font, font_size, Color.black);
          writeText(contentStream, ir.getBaselineErrors().size() + " errors", pos_x + image_width + 70, pos_y, font, font_size, ir.getBaselineErrors().size() > 0 ? Color.red : Color.black);
          writeText(contentStream, ir.getBaselineWarnings().size() + " warnings", pos_x + image_width + 120, pos_y, font, font_size, ir.getBaselineWarnings().size() > 0 ? Color.red : Color.black);
        }
        if (ir.hasEpValidation()) {
          pos_y -= 10;
          writeText(contentStream, "Tiff/EP", pos_x + image_width + 10, pos_y, font, font_size, Color.black);
          writeText(contentStream, ir.getEPErrors().size() + " errors", pos_x + image_width + 70, pos_y, font, font_size, ir.getEPErrors().size() > 0 ? Color.red : Color.black);
          writeText(contentStream, ir.getEPWarnings().size() + " warnings", pos_x + image_width + 120, pos_y, font, font_size, ir.getEPWarnings().size() > 0 ? Color.red : Color.black);
        }
        if (ir.hasItValidation(0)) {
          pos_y -= 10;
          writeText(contentStream, "Tiff/IT", pos_x + image_width + 10, pos_y, font, font_size, Color.black);
          writeText(contentStream, ir.getITErrors(0).size() + " errors", pos_x + image_width + 70, pos_y, font, font_size, ir.getITErrors(0).size() > 0 ? Color.red : Color.black);
          writeText(contentStream, ir.getITWarnings(0).size() + " warnings", pos_x + image_width + 120, pos_y, font, font_size, ir.getITWarnings(0).size() > 0 ? Color.red : Color.black);
        }
        if (ir.hasItValidation(1)) {
          pos_y -= 10;
          writeText(contentStream, "Tiff/IT-1", pos_x + image_width + 10, pos_y, font, font_size, Color.black);
          writeText(contentStream, ir.getITErrors(1).size() + " errors", pos_x + image_width + 70, pos_y, font, font_size, ir.getITErrors(1).size() > 0 ? Color.red : Color.black);
          writeText(contentStream, ir.getITWarnings(1).size() + " warnings", pos_x + image_width + 120, pos_y, font, font_size, ir.getITWarnings(1).size() > 0 ? Color.red : Color.black);
        }
        if (ir.hasItValidation(2)) {
          pos_y -= 10;
          writeText(contentStream, "Tiff/IT-2", pos_x + image_width + 10, pos_y, font, font_size, Color.black);
          writeText(contentStream, ir.getITErrors(2).size() + " errors", pos_x + image_width + 70, pos_y, font, font_size, ir.getITErrors(2).size() > 0 ? Color.red : Color.black);
          writeText(contentStream, ir.getITWarnings(2).size() + " warnings", pos_x + image_width + 120, pos_y, font, font_size, ir.getITWarnings(2).size() > 0 ? Color.red : Color.black);
        }
        if (ir.checkPC) {
          pos_y -= 10;
          writeText(contentStream, "Policy checker", pos_x + image_width + 10, pos_y, font, font_size, Color.black);
          writeText(contentStream, ir.getPCErrors().size() + " errors", pos_x + image_width + 70, pos_y, font, font_size, ir.getPCErrors().size() > 0 ? Color.red : Color.black);
          writeText(contentStream, ir.getPCWarnings().size() + " warnings", pos_x + image_width + 120, pos_y, font, font_size, ir.getPCWarnings().size() > 0 ? Color.red : Color.black);
        }
        if (pos_y < maxy) maxy = pos_y;

        // Chart
        pos_y = initialy;
        pos_y -= 10;
        pos_y -= 10;
        graph_size = 25;
        image = new BufferedImage(graph_size*10, graph_size*10, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        doub = (double)ir.calculatePercent();
        extent = 360d * doub / 100.0;
        g2d.setColor(Color.gray);
        g2d.fill(new Arc2D.Double(0, 0, graph_size*10, graph_size*10, 90, 360, Arc2D.PIE));
        g2d.setColor(Color.red);
        g2d.fill(new Arc2D.Double(0, 0, graph_size*10, graph_size*10, 90, 360 - extent, Arc2D.PIE));
        ximage = new PDJpeg(document, image);
        contentStream.drawXObject(ximage, pos_x + image_width + 180, pos_y - graph_size, graph_size, graph_size);
        pos_y += graph_size - 10;
        if (doub < 100) {
          pos_y -= 10;
          writeText(contentStream, "Failed", pos_x + image_width + 180 + graph_size + 10, pos_y - graph_size / 2, font, font_size, Color.red);
        }
        pos_y -= 10;
        writeText(contentStream, "Score " + doub + "%", pos_x + image_width + 180 + graph_size + 10, pos_y - graph_size / 2, font, font_size, Color.gray);
        if (pos_y < maxy) maxy = pos_y;

        pos_y = maxy;
      }

      // Full individual reports
      ArrayList<PDDocument> toClose = new ArrayList<PDDocument>();
      for (IndividualReport ir : gr.getIndividualReports()) {
        if (!ir.containsData()) continue;
        PDDocument doc;
        if (ir.getPDF() != null)
          doc = ir.getPDF();
        else
          doc = PDDocument.load(ir.getPDFDocument());
        List<PDPage> l = doc.getDocumentCatalog().getAllPages();
        for (PDPage pag : l) {
          document.addPage(pag);
        }
        toClose.add(doc);
      }

      contentStream.close();

      document.save(pdffile);
      document.close();

      for (PDDocument doc : toClose) {
        doc.close();
      }
    } catch (Exception tfe) {
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in ReportPDF", tfe));
    }
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
    document.addPage( page );
    return new PDPageContentStream(document, page);
  }

  /**
   * Write text.
   *
   * @param contentStream the content stream
   * @param text          the text
   * @param x             the x
   * @param y             the y
   * @param font          the font
   * @param font_size     the font size
   * @throws Exception the exception
   */
  void writeText(PDPageContentStream contentStream, String text, int x, int y, PDFont font, int font_size) throws Exception {
    writeText(contentStream, text, x, y, font, font_size, Color.black);
  }

  /**
   * Write text.
   *
   * @param contentStream the content stream
   * @param text          the text
   * @param x             the x
   * @param y             the y
   * @param font          the font
   * @param font_size     the font size
   * @param color         the color
   * @throws Exception the exception
   */
  void writeText(PDPageContentStream contentStream, String text, int x, int y, PDFont font, int font_size, Color color) throws Exception {
    contentStream.beginText();
    contentStream.setFont( font, font_size );
    contentStream.setNonStrokingColor(color);
    contentStream.moveTextPositionByAmount( x, y );
    contentStream.drawString( text );
    contentStream.endText();
  }
}
