package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.PDFParams;
import dpfmanager.shell.modules.report.util.ReportTag;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Created by easy on 06/05/2016.
 */
public class PdfReport extends Report {
  /**
   * The Init posy.
   */
  int init_posy = 800;

  /**
   * Parse an individual report to PDF.
   *
   * @param ir         the individual report.
   */
  public void parseIndividual(IndividualReport ir) {
    try {
      int epErr = ir.getNEpErr(), epWar = ir.getNEpWar();
      int blErr = ir.getNBlErr(), blWar = ir.getNBlWar();
      int it0Err = ir.getNItErr(0), it0War = ir.getNItWar(0);
      int it1Err = ir.getNItErr(1), it2War = ir.getNItWar(1);
      int it2Err = ir.getNItErr(2), it1War = ir.getNItWar(2);
      int pcErr = ir.getPCErrors().size(), pcWar = ir.getPCWarnings().size();

      PDFParams pdfParams = new PDFParams();
      PDDocument document = pdfParams.getDocument();

      PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
      document.addPage( page );

      PDFont font = PDType1Font.HELVETICA_BOLD;
      PDPageContentStream contentStream = new PDPageContentStream(document, page);

      int pos_x = 200;
      int pos_y = 700;
      int font_size = 18;

      // Logo
      float scale = 3;
      InputStream inputStream = getFileStreamFromResources("images/logo.jpg");
      PDXObjectImage ximage = new PDJpeg(document, inputStream);
      contentStream.drawXObject( ximage, pos_x, pos_y, 645/scale, 300/scale );

      // Report Title
      pos_y -= 30;
      writeText(contentStream, "INDIVIDUAL REPORT", pos_x, pos_y, font, font_size);

      // Image
      pos_x = 50;
      int image_height = 130;
      int image_width = 200;
      pos_y -= (image_height + 30);
      int image_pos_y = pos_y;
      BufferedImage thumb = tiff2Jpg(ir.getFilePath());
      if (thumb == null) {
        thumb = ImageIO.read(getFileStreamFromResources("html/img/noise.jpg"));
      }
      image_width = image_height * thumb.getWidth() / thumb.getHeight();
      ximage = new PDJpeg(document, thumb);
      contentStream.drawXObject( ximage, pos_x, pos_y, image_width, image_height );

      // Image name & path
      font_size = 12;
      pos_y += image_height;
      writeText(contentStream, ir.getFileName(), pos_x + image_width + 10, pos_y - 12, font, font_size);
      font_size = 11;
      pos_y -= 20;
      writeText(contentStream, ir.getFilePath(), pos_x + image_width + 10, pos_y - 12, font, font_size);

      // Image alert
      pos_y -= 30;
      font_size = 9;
      if (blErr + epErr + it0Err + it1Err + it2Err + pcErr > 0) {
        writeText(contentStream, "This file does NOT conform to conformance checker", pos_x + image_width + 10, pos_y, font, font_size, Color.red);
      } else if (blWar + epWar + it0War + it1War + it2War + pcWar > 0) {
        writeText(contentStream, "This file conforms to conformance checker, BUT it has some warnings", pos_x + image_width + 1, pos_y, font, font_size, Color.orange);
      } else {
        writeText(contentStream, "This file conforms to conformance checker", pos_x + image_width + 1, pos_y, font, font_size, Color.green);
      }

      // Summary table
      pos_y -= 20;
      font_size = 8;
      writeText(contentStream, "Errors", pos_x + image_width + 140, pos_y, font, font_size);
      writeText(contentStream, "Warnings", pos_x + image_width + 180, pos_y, font, font_size);
      String dif = "";

      if (ir.hasBlValidation()) {
        pos_y -= 20;
        contentStream.drawLine(pos_x + image_width + 10,pos_y+15,pos_x + image_width + 230,pos_y+15);
        writeText(contentStream, "Baseline", pos_x + image_width + 10, pos_y, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlErr(), blErr) : "";
        writeText(contentStream, blErr + dif, pos_x + image_width + 150, pos_y, font, font_size, blErr > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlWar(), blWar) : "";
        writeText(contentStream, blWar + dif, pos_x + image_width + 200, pos_y, font, font_size, blWar > 0 ? Color.orange : Color.black);
      }
      if (ir.hasEpValidation()) {
        pos_y -= 20;
        writeText(contentStream, "Tiff/Ep", pos_x + image_width + 10, pos_y, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpErr(), epErr) : "";
        writeText(contentStream, epErr + dif, pos_x + image_width + 150, pos_y, font, font_size, epErr > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpWar(), epWar) : "";
        writeText(contentStream, epWar + dif, pos_x + image_width + 200, pos_y, font, font_size, epWar > 0 ? Color.orange : Color.black);
      }
      if (ir.hasItValidation(0)) {
        pos_y -= 20;
        writeText(contentStream, "Tiff/It", pos_x + image_width + 10, pos_y, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(0), it0Err) : "";
        writeText(contentStream, it0Err + dif, pos_x + image_width + 150, pos_y, font, font_size, it0Err > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(0), it0War) : "";
        writeText(contentStream, it0War + dif, pos_x + image_width + 200, pos_y, font, font_size, it0War > 0 ? Color.orange : Color.black);
      }
      if (ir.hasItValidation(0)) {
        pos_y -= 20;
        writeText(contentStream, "Tiff/It-P1", pos_x + image_width + 10, pos_y, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(1), it1Err) : "";
        writeText(contentStream, it1Err + dif, pos_x + image_width + 150, pos_y, font, font_size, it1Err > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(1), it1War) : "";
        writeText(contentStream, it1War + dif, pos_x + image_width + 200, pos_y, font, font_size, it1War > 0 ? Color.orange : Color.black);
      }
      if (ir.hasItValidation(0)) {
        pos_y -= 20;
        writeText(contentStream, "Tiff/It-P2", pos_x + image_width + 10, pos_y, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(2), it2Err) : "";
        writeText(contentStream, it2Err + dif, pos_x + image_width + 150, pos_y, font, font_size, it2Err > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(2), it2War) : "";
        writeText(contentStream, it2War + dif, pos_x + image_width + 200, pos_y, font, font_size, it2War > 0 ? Color.orange : Color.black);
      }
      pos_y -= 20;
      writeText(contentStream, "Policy checker", pos_x + image_width + 10, pos_y, font, font_size);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCErrors().size(), pcErr) : "";
      writeText(contentStream, pcErr + dif, pos_x + image_width + 150, pos_y, font, font_size, pcErr > 0 ? Color.red : Color.black);
      dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCWarnings().size(), pcWar) : "";
      writeText(contentStream, pcWar + dif, pos_x + image_width + 200, pos_y, font, font_size, pcWar > 0 ? Color.orange : Color.black);

      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      // Tags
      font_size = 10;
      if (pos_y > image_pos_y) pos_y = image_pos_y;
      pos_y -= 20;
      writeText(contentStream, "Tags", pos_x, pos_y, font, font_size);
      font_size = 7;
      pos_y -= 20;
      writeText(contentStream, "IFD", pos_x, pos_y, font, font_size);
      writeText(contentStream, "Tag ID", pos_x + 40, pos_y, font, font_size);
      writeText(contentStream, "Tag Name", pos_x + 80, pos_y, font, font_size);
      writeText(contentStream, "Value", pos_x + 200, pos_y, font, font_size);
      for (ReportTag tag : getTags(ir)) {
        if (tag.expert) continue;
        String sDif = "";
        if (tag.dif < 0) sDif = "(-)";
        else if (tag.dif > 0) sDif = "(+)";
        pos_y -= 18;
        if (newPageNeeded(pos_y)) {
          contentStream = newPage(contentStream, document);
          pos_y = init_posy;
        }
        writeText(contentStream, tag.index + "", pos_x, pos_y, font, font_size);
        writeText(contentStream, tag.tv.getId() + sDif, pos_x + 40, pos_y, font, font_size);
        writeText(contentStream, tag.tv.getName(), pos_x + 80, pos_y, font, font_size);
        writeText(contentStream, tag.tv.getDescriptiveValue(), pos_x + 200, pos_y, font, font_size);
      }

      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      // File structure
      font_size = 10;
      pos_y -= 20;
      writeText(contentStream, "File Structure", pos_x, pos_y, font, font_size);
      TiffDocument td = ir.getTiffModel();
      IFD ifd = td.getFirstIFD();
      font_size = 7;
      while (ifd != null) {
        pos_y -= 20;
        String typ = " - Main image";
        if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = " - Thumbnail";
        ximage = new PDJpeg(document, getFileStreamFromResources("images/doc.jpg"));
        contentStream.drawXObject( ximage, pos_x, pos_y, 5, 7 );
        writeText(contentStream, ifd.toString() + typ, pos_x + 7, pos_y, font, font_size);
        if (ifd.getsubIFD() != null) {
          pos_y -= 18;
          if (ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = " - Main image";
          else typ = " - Thumbnail";
          contentStream.drawXObject( ximage, pos_x + 15, pos_y, 5, 7 );
          writeText(contentStream, "SubIFD"+typ, pos_x + 15 + 7, pos_y, font, font_size);
        }
        if (ifd.containsTagId(34665)) {
          pos_y -= 18;
          contentStream.drawXObject( ximage, pos_x + 15, pos_y, 5, 7 );
          writeText(contentStream, "EXIF", pos_x + 15 + 7, pos_y, font, font_size);
        }
        if (ifd.containsTagId(700)) {
          pos_y -= 18;
          contentStream.drawXObject( ximage, pos_x + 15, pos_y, 5, 7 );
          writeText(contentStream, "XMP", pos_x + 15 + 7, pos_y, font, font_size);
        }
        if (ifd.containsTagId(33723)) {
          pos_y -= 18;
          contentStream.drawXObject( ximage, pos_x + 15, pos_y, 5, 7 );
          writeText(contentStream, "IPTC", pos_x + 15 + 7, pos_y, font, font_size);
        }
        ifd = ifd.getNextIFD();
      }

      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      // Conformance
      pdfParams.setContentStream(contentStream);
      if (ir.checkBL) {
        pos_y = writeErrorsWarnings(pdfParams, font, ir.getBaselineErrors(), ir.getBaselineWarnings(), pos_x, pos_y, "Baseline");
        contentStream = pdfParams.getContentStream();
      }
      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      if (ir.checkEP) {
        pos_y = writeErrorsWarnings(pdfParams, font, ir.getEPErrors(), ir.getEPWarnings(), pos_x, pos_y, "Tiff/EP");
        contentStream = pdfParams.getContentStream();
      }
      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      if (ir.checkIT0) {
        pos_y = writeErrorsWarnings(pdfParams, font, ir.getITErrors(0), ir.getITWarnings(0), pos_x, pos_y, "Tiff/IT");
        contentStream = pdfParams.getContentStream();
      }
      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      if (ir.checkIT1) {
        pos_y = writeErrorsWarnings(pdfParams, font, ir.getITErrors(1), ir.getITWarnings(1), pos_x, pos_y, "Tiff/IT");
        contentStream = pdfParams.getContentStream();
      }
      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      if (ir.checkIT2) {
        pos_y = writeErrorsWarnings(pdfParams, font, ir.getITErrors(2), ir.getITWarnings(2), pos_x, pos_y, "Tiff/IT");
        contentStream = pdfParams.getContentStream();
      }
      if (newPageNeeded(pos_y)) {
        contentStream = newPage(contentStream, document);
        pos_y = init_posy;
      }

      if (ir.checkPC) {
        pos_y = writeErrorsWarnings2(pdfParams, font, ir.getPCErrors(), ir.getPCWarnings(), pos_x, pos_y, "Policy Checker");
        contentStream = pdfParams.getContentStream();
      }

      contentStream.close();

      ir.setPDF(document);
    } catch (Exception tfe) {
      ir.setPDF(null);
      //context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in ReportPDF", tfe));
    }
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
   * New page needed boolean.
   *
   * @param pos_y the pos y
   * @return the boolean
   */
  boolean newPageNeeded(int pos_y) {
    return pos_y < 100;
  }

  /**
   * Write errors warnings int.
   *
   * @param pdfParams the pdf params
   * @param font     the font
   * @param errors   the errors
   * @param warnings the warnings
   * @param pos_x    the pos x
   * @param posy     the posy
   * @param type     the type
   * @return the int
   * @throws Exception the exception
   */
  int writeErrorsWarnings(PDFParams pdfParams, PDFont font, List<RuleResult> errors, List<RuleResult> warnings, int pos_x, int posy, String type) throws Exception{
    int total = 0;
    int font_size = 10;
    int pos_y = posy;
    pos_y -= 20;
    PDDocument document = pdfParams.getDocument();
    PDPageContentStream contentStream = pdfParams.getContentStream();
    writeText(contentStream, type + " Conformance", pos_x, pos_y, font, font_size);

    font_size = 8;
    if ((errors != null && errors.size() > 0) || (warnings != null && warnings.size() > 0)) {
      pos_y -= 20;
      writeText(contentStream, "Type", pos_x, pos_y, font, font_size);
      writeText(contentStream, "Location", pos_x + 50, pos_y, font, font_size);
      writeText(contentStream, "Description", pos_x + 100, pos_y, font, font_size);
    }
    if (errors != null) {
      for (RuleResult val : errors) {
        pos_y -= 20;
        if (newPageNeeded(pos_y)) {
          contentStream = newPage(contentStream, document);
          pos_y = init_posy;
        }
        writeText(contentStream, "Error", pos_x, pos_y, font, font_size, Color.red);
        writeText(contentStream, val.getLocation(), pos_x + 50, pos_y, font, font_size);
        writeText(contentStream, val.getDescription(), pos_x + 100, pos_y, font, font_size);
        total++;
      }
    }
    if (warnings != null) {
      for (RuleResult val : warnings) {
        pos_y -= 20;
        if (newPageNeeded(pos_y)) {
          contentStream = newPage(contentStream, document);
          pos_y = init_posy;
        }
        writeText(contentStream, "Warning", pos_x, pos_y, font, font_size, Color.orange);
        writeText(contentStream, val.getLocation(), pos_x + 50, pos_y, font, font_size);
        writeText(contentStream, val.getDescription(), pos_x + 100, pos_y, font, font_size);
        total++;
      }
    }
    if (total == 0) {
      pos_y -= 20;
      PDXObjectImage ximage = new PDJpeg(document, getFileStreamFromResources("images/ok.jpg"));
      contentStream.drawXObject( ximage, pos_x + 8, pos_y, 5, 5 );
      writeText(contentStream, "This file conforms to " + type, pos_x + 15, pos_y, font, font_size, Color.green);
    }
    pdfParams.setContentStream(contentStream);
    return pos_y;
  }

  int writeErrorsWarnings2(PDFParams pdfParams, PDFont font, List<RuleResult> errors, List<RuleResult> warnings, int pos_x, int posy, String type) throws Exception{
    int total = 0;
    int font_size = 10;
    int pos_y = posy;
    pos_y -= 20;
    PDDocument document = pdfParams.getDocument();
    PDPageContentStream contentStream = pdfParams.getContentStream();
    writeText(contentStream, type + " Conformance", pos_x, pos_y, font, font_size);

    font_size = 8;
    if ((errors != null && errors.size() > 0) || (warnings != null && warnings.size() > 0)) {
      pos_y -= 20;
      writeText(contentStream, "Type", pos_x, pos_y, font, font_size);
      writeText(contentStream, "Location", pos_x + 50, pos_y, font, font_size);
      writeText(contentStream, "Description", pos_x + 100, pos_y, font, font_size);
    }
    if (errors != null) {
      for (RuleResult val : errors) {
        pos_y -= 20;
        if (newPageNeeded(pos_y)) {
          contentStream = newPage(contentStream, document);
          pos_y = init_posy;
        }
        writeText(contentStream, "Error", pos_x, pos_y, font, font_size, Color.red);
        writeText(contentStream, val.getLocation(), pos_x + 50, pos_y, font, font_size);
        writeText(contentStream, val.getDescription(), pos_x + 100, pos_y, font, font_size);
        total++;
      }
    }
    if (warnings != null) {
      for (RuleResult val : warnings) {
        pos_y -= 20;
        if (newPageNeeded(pos_y)) {
          contentStream = newPage(contentStream, document);
          pos_y = init_posy;
        }
        writeText(contentStream, "Warning", pos_x, pos_y, font, font_size, Color.orange);
        writeText(contentStream, val.getLocation(), pos_x + 50, pos_y, font, font_size);
        writeText(contentStream, val.getDescription(), pos_x + 100, pos_y, font, font_size);
        total++;
      }
    }
    if (total == 0) {
      pos_y -= 20;
      PDXObjectImage ximage = new PDJpeg(document, getFileStreamFromResources("images/ok.jpg"));
      contentStream.drawXObject( ximage, pos_x + 8, pos_y, 5, 5 );
      writeText(contentStream, "This file conforms to " + type, pos_x + 15, pos_y, font, font_size, Color.green);
    }
    pdfParams.setContentStream(contentStream);
    return pos_y;
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
