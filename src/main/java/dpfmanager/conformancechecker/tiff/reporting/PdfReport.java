/**
 * <h1>PdfReport.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Victor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.PDFParams;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

  PDFParams makeConformSection(int nerrors, int nwarnings, String key, PDFParams pdfParams0, boolean check, boolean forcecheck, int pos_x, int image_width, int font_size, PDFont font) throws Exception {
    PDFParams pdfParams = pdfParams0;
    if (check || (forcecheck && nerrors + nwarnings == 0)) {
      if (nerrors > 0) {
        pdfParams = writeText(pdfParams, "This file does NOT conform to " + key, pos_x + image_width + 10, font, font_size, Color.red);
      } else if (nwarnings > 0) {
        pdfParams = writeText(pdfParams, "This file conforms to " + key + ", BUT it has some warnings", pos_x + image_width + 10, font, font_size, Color.orange);
      } else {
        pdfParams = writeText(pdfParams, "This file conforms to " + key, pos_x + image_width + 10, font, font_size, Color.green);
      }
      pdfParams.y -= 20;
    } else {

    }
    return pdfParams;
  }

  /**
   * Parse an individual report to PDF.
   *
   * @param ir the individual report.
   */
  public void parseIndividual(IndividualReport ir) {
    try {
//      int epErr = ir.getNEpErr(), epWar = ir.getNEpWar();
//      int blErr = ir.getNBlErr(), blWar = ir.getNBlWar();
//      int it0Err = ir.getNItErr(0), it0War = ir.getNItWar(0);
//      int it1Err = ir.getNItErr(1), it2War = ir.getNItWar(1);
//      int it2Err = ir.getNItErr(2), it1War = ir.getNItWar(2);
//      int pcErr = ir.getPCErrors().size(), pcWar = ir.getPCWarnings().size();

      PDFParams pdfParams = new PDFParams();
      pdfParams.init(PDPage.PAGE_SIZE_A4);
      PDFont font = PDType1Font.HELVETICA_BOLD;

      int pos_x = 200;
      pdfParams.y = 700;
      int font_size = 18;

      // Logo
      PDXObjectImage ximage;
      float scale = 3;
      synchronized (this) {
        InputStream inputStream = getFileStreamFromResources("images/logo.jpg");
        ximage = new PDJpeg(pdfParams.getDocument(), inputStream);
        pdfParams.getContentStream().drawXObject(ximage, pos_x, pdfParams.y, 645 / scale, 300 / scale);
      }

      // Report Title
      pdfParams.y -= 30;
      pdfParams = writeText(pdfParams, "INDIVIDUAL REPORT", pos_x, font, font_size);

      // Image
      pos_x = 50;
      int image_height = 130;
      int image_width = 200;
      pdfParams.y -= (image_height + 30);
      int image_pos_y = pdfParams.y;
      BufferedImage thumb = tiff2Jpg(ir.getFilePath());
      if (thumb == null) {
        thumb = ImageIO.read(getFileStreamFromResources("html/img/noise.jpg"));
      }
      image_width = image_height * thumb.getWidth() / thumb.getHeight();
      ximage = new PDJpeg(pdfParams.getDocument(), thumb);
      pdfParams.getContentStream().drawXObject(ximage, pos_x, pdfParams.y, image_width, image_height);

      // Image name & path
      font_size = 12;
      pdfParams.y += image_height - 12;
      pdfParams = writeText(pdfParams, ir.getFileName(), pos_x + image_width + 10, font, font_size);
      font_size = 11;
      pdfParams.y -= 32;
      List<String> linesPath = splitInLines(font_size, font, ir.getFilePath().replaceAll("\\\\", "/"), 500, "/");
      for (String line : linesPath) {
        pdfParams = writeText(pdfParams, line, pos_x + image_width + 10, font, font_size);
        pdfParams.y -= 10;
      }

      // Image alert
      pdfParams.y -= 30;
      font_size = 9;
      for (String iso : ir.getIsosCheck()) {
        String name = ImplementationCheckerLoader.getIsoName(iso);
        pdfParams = makeConformSection(ir.getNErrors(iso), ir.getNWarnings(iso), name, pdfParams, true, true, pos_x, image_width, font_size, font);
      }

      // Summary table
      font_size = 8;
      pdfParams = writeText(pdfParams, "Errors", pos_x + image_width + 140, font, font_size);
      pdfParams = writeText(pdfParams, "Warnings", pos_x + image_width + 180, font, font_size);
      String dif = "";

      for (String iso : ir.getIsosCheck()) {
        String name = ImplementationCheckerLoader.getIsoName(iso);
        pdfParams.y -= 20;
        pdfParams.getContentStream().drawLine(pos_x + image_width + 10, pdfParams.y + 15, pos_x + image_width + 230, pdfParams.y + 15);
        pdfParams = writeText(pdfParams, name, pos_x + image_width + 10, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNErrors(iso), ir.getNErrors(iso)) : "";
        pdfParams = writeText(pdfParams, ir.getNErrors(iso) + dif, pos_x + image_width + 150, font, font_size, ir.getNErrors(iso) > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNWarnings(iso), ir.getNWarnings(iso)) : "";
        pdfParams = writeText(pdfParams, ir.getNWarnings(iso) + dif, pos_x + image_width + 200, font, font_size, ir.getNWarnings(iso) > 0 ? Color.orange : Color.black);
      }

      // Tags
      font_size = 10;
      if (pdfParams.y > image_pos_y) pdfParams.y = image_pos_y;
      pdfParams.y -= 20;
      pdfParams = writeText(pdfParams, "Tags", pos_x, font, font_size);
      font_size = 7;
      pdfParams.y -= 20;
      pdfParams = writeText(pdfParams, "IFD", pos_x, font, font_size);
      pdfParams = writeText(pdfParams, "Tag ID", pos_x + 40, font, font_size);
      pdfParams = writeText(pdfParams, "Tag Name", pos_x + 80, font, font_size);
      pdfParams = writeText(pdfParams, "Value", pos_x + 200, font, font_size);
      for (ReportTag tag : getTags(ir)) {
        if (tag.expert) continue;
        /*if (tag.tv.getId() == 700) {
          // XMP
          for (abstractTiffType to : tag.tv.getValue()) {
            XMP xmp = (XMP)to;
            try {
              Metadata metadata = xmp.createMetadata();
              for (String key : metadata.keySet()) {
                pdfParams.y -= 18;
                pdfParams = writeText(pdfParams, (tag.index+1) + "", pos_x, font, font_size);
                pdfParams = writeText(pdfParams, "", pos_x + 40, font, font_size);
                pdfParams = writeText(pdfParams, key, pos_x + 80, font, font_size);
                pdfParams = writeText(pdfParams, metadata.get(key).toString().trim(), pos_x + 200, font, font_size);
              }
              int nh = 1;
              for (Hashtable<String, String> kv : xmp.getHistory()) {
                for (String key : kv.keySet()) {
                  pdfParams.y -= 18;
                  pdfParams = writeText(pdfParams, (tag.index+1) + "", pos_x, font, font_size);
                  pdfParams = writeText(pdfParams, nh + "", pos_x + 40, font, font_size);
                  pdfParams = writeText(pdfParams, key, pos_x + 80, font, font_size);
                  pdfParams = writeText(pdfParams, kv.get(key).toString().trim(), pos_x + 200, font, font_size);
                }
                nh++;
              }
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
          continue;
        }
        if (tag.tv.getId() == 33723) {
          // IPTC
          for (abstractTiffType to : tag.tv.getValue()) {
            IPTC iptc = (IPTC)to;
            try {
              Metadata metadata = iptc.createMetadata();
              for (String key : metadata.keySet()) {
                pdfParams.y -= 18;
                pdfParams = writeText(pdfParams, (tag.index+1) + "", pos_x, font, font_size);
                pdfParams = writeText(pdfParams, "", pos_x + 40, font, font_size);
                pdfParams = writeText(pdfParams, key, pos_x + 80, font, font_size);
                pdfParams = writeText(pdfParams, metadata.get(key).toString().trim(), pos_x + 200, font, font_size);
              }
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
          continue;
        }
        if (tag.tv.getId() == 34665) {
          // EXIF
          for (abstractTiffType to : tag.tv.getValue()) {
            IFD exif = (IFD)to;
            try {
              for (TagValue tv : exif.getTags().getTags()) {
                pdfParams.y -= 18;
                pdfParams = writeText(pdfParams, (tag.index+1) + "", pos_x, font, font_size);
                pdfParams = writeText(pdfParams, "", pos_x + 40, font, font_size);
                pdfParams = writeText(pdfParams, tv.getName(), pos_x + 80, font, font_size);
                pdfParams = writeText(pdfParams, tv.getDescriptiveValue(), pos_x + 200, font, font_size);
              }
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
          continue;
        }*/
        String sDif = "";
        if (tag.dif < 0) sDif = "(-)";
        else if (tag.dif > 0) sDif = "(+)";
        pdfParams.y -= 18;
        pdfParams = writeText(pdfParams, tag.index + "", pos_x, font, font_size);
        pdfParams = writeText(pdfParams, tag.tv.getId() + sDif, pos_x + 40, font, font_size);
        pdfParams = writeText(pdfParams, tag.tv.getName(), pos_x + 80, font, font_size);
        pdfParams = writeText(pdfParams, tag.tv.getDescriptiveValue(), pos_x + 200, font, font_size);
      }

      // File structure
      font_size = 10;
      pdfParams.y -= 40;
      pdfParams = writeText(pdfParams, "File Structure", pos_x, font, font_size);
      TiffDocument td = ir.getTiffModel();
      IFD ifd = td.getFirstIFD();
      font_size = 7;
      while (ifd != null) {
        pdfParams.y -= 20;
        String typ = " - Main image";
        if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize())
          typ = " - Thumbnail";
        ximage = new PDJpeg(pdfParams.getDocument(), getFileStreamFromResources("images/doc.jpg"));
        pdfParams.getContentStream().drawXObject(ximage, pos_x, pdfParams.y, 5, 7);
        pdfParams = writeText(pdfParams, ifd.toString() + typ, pos_x + 7, font, font_size);
        if (ifd.getsubIFD() != null) {
          pdfParams.y -= 18;
          if (ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = " - Main image";
          else typ = " - Thumbnail";
          pdfParams.getContentStream().drawXObject(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "SubIFD" + typ, pos_x + 15 + 7, font, font_size);
        }
        if (ifd.containsTagId(34665)) {
          pdfParams.y -= 18;
          pdfParams.getContentStream().drawXObject(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "EXIF", pos_x + 15 + 7, font, font_size);
        }
        if (ifd.containsTagId(700)) {
          pdfParams.y -= 18;
          pdfParams.getContentStream().drawXObject(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "XMP", pos_x + 15 + 7, font, font_size);
        }
        if (ifd.containsTagId(33723)) {
          pdfParams.y -= 18;
          pdfParams.getContentStream().drawXObject(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "IPTC", pos_x + 15 + 7, font, font_size);
        }
        ifd = ifd.getNextIFD();
      }

      // Conformance
      for (String iso : ir.getIsosCheck()) {
        pdfParams.y -= 20;
        String name = ImplementationCheckerLoader.getIsoName(iso);
        pdfParams = writeErrorsWarnings(pdfParams, font, ir.getErrors(iso), ir.getOnlyWarnings(iso), ir.getOnlyInfos(iso), pos_x, name);
      }

      pdfParams.getContentStream().close();

      ir.setPDF(pdfParams.getDocument());
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
    document.addPage(page);
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
   * @param font      the font
   * @param errors    the errors
   * @param warnings  the warnings
   * @param pos_x     the pos x
   * @param type      the type
   * @return the int
   * @throws Exception the exception
   */
  private PDFParams writeErrorsWarnings(PDFParams pdfParams, PDFont font, List<RuleResult> errors, List<RuleResult> warnings, List<RuleResult> infos, int pos_x, String type) throws Exception {
    int font_size = 10;
    int marg1 = 30;
    int marg2 = 75;
    int marg3 = 240;
    pdfParams.y -= 20;

    pdfParams = writeText(pdfParams, type + " Conformance", pos_x, font, font_size);
    font_size = 8;

    // Conforms message
    if (errors.size() + warnings.size() == 0) {
      pdfParams.y -= 20;
      PDXObjectImage ximage = new PDJpeg(pdfParams.getDocument(), getFileStreamFromResources("images/ok.jpg"));
      pdfParams.getContentStream().drawXObject(ximage, pos_x + 8, pdfParams.y, 5, 5);
      pdfParams = writeText(pdfParams, "This file conforms to " + type, pos_x + 15, font, font_size, Color.green);
    }

    // Errors, warnings and infos list
    if (errors.size() + warnings.size() + infos.size() > 0) {
      pdfParams.y -= 20;
      pdfParams = writeText(pdfParams, "Type", pos_x, font, font_size);
      pdfParams = writeText(pdfParams, "Location", pos_x + marg1, font, font_size);
      pdfParams = writeText(pdfParams, "Reference", pos_x + marg2, font, font_size);
      pdfParams = writeText(pdfParams, "Description", pos_x + marg3, font, font_size);
    }
    for (RuleResult val : errors) {
      pdfParams = writeResult(pdfParams, val, "Error", pos_x, font);
    }
    for (RuleResult val : warnings) {
      pdfParams = writeResult(pdfParams, val, "Warning", pos_x, font);
    }
    for (RuleResult val : infos) {
      pdfParams = writeResult(pdfParams, val, "Info", pos_x, font);
    }
    return pdfParams;
  }

  private PDFParams writeResult(PDFParams pdfParams, RuleResult val, String type, int pos_x, PDFont font) throws Exception {
    int marg1 = 30;
    int marg2 = 75;
    int marg3 = 240;
    int marg4 = 500;
    int font_size = 8;
    pdfParams.y -= 20;

    //Type
    Color color = Color.red;
    if (type.equals("Info")) {
      color = Color.gray;
    } else if (type.equals("Warning")) {
      color = Color.orange;
    }
    pdfParams = writeText(pdfParams, type, pos_x, font, font_size, color);

    // Location
    pdfParams = writeText(pdfParams, val.getLocation(), pos_x + marg1, font, font_size);

    // Reference
    List<String> linesRef = splitInLines(font_size, font, val.getReference(), marg3 - marg2 - 10, " ");
    for (String line : linesRef) {
      pdfParams = writeText(pdfParams, line, pos_x + marg2, font, font_size);
      pdfParams.y -= 10;
    }
    pdfParams.y = pdfParams.y + 10 * linesRef.size();

    // Description
    List<String> linesDes = splitInLines(font_size, font, val.getDescription(), marg4 - marg3 - 10, " ");
    for (String line : linesDes) {
      pdfParams = writeText(pdfParams, line, pos_x + marg3, font, font_size);
      pdfParams.y -= 10;
    }
    pdfParams.y += 10 * linesDes.size();

    pdfParams.y -= linesDes.size() > linesRef.size() ? linesDes.size() * 10 : linesRef.size() * 10;
    return pdfParams;
  }

  private List<String> splitInLines(int font_size, PDFont font, String text, int max, String regex) {
    boolean pathMode = regex.equals("/");
    if (text == null) {
      return new ArrayList<>();
    }
    try {
      List<String> lines = new ArrayList<>();
      float size = getSize(font_size, font, text);
      if (size > max) {
        String[] words = text.split(regex);
        String line = words[0];
        if (pathMode){
          line += "/";
        }
        for (int i = 1; i < words.length; i++) {
          String word = words[i];
          String aux = line + regex + word;
          if (pathMode){
            aux = line + word + regex;
          }
          if (getSize(font_size, font, aux) > max) {
            lines.add(line);
            line = word;
            if (pathMode){
              line += "/";
            }
          } else {
            line = aux;
          }
        }
        if (pathMode){
          line = line.substring(0, line.length()-1);
        }
        lines.add(line);
        return lines;
      }
    } catch (Exception e) {
    }
    return Arrays.asList(text);
  }

  private float getSize(int font_size, PDFont font, String text) throws IOException {
    return font_size * font.getStringWidth(text) / 1000;
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
    return writeText(pdfParams, text, x, 99999, font, font_size, Color.black);
  }

  private PDFParams writeText(PDFParams pdfParams, String text, int x, int max_x, PDFont font, int font_size) throws Exception {
    return writeText(pdfParams, text, x, max_x, font, font_size, Color.black);
  }

  private PDFParams writeText(PDFParams pdfParams, String text, int x, PDFont font, int font_size, Color color) throws Exception {
    return writeText(pdfParams, text, x, 99999, font, font_size, color);
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
  private PDFParams writeText(PDFParams pdfParams, String text, int x, int max_x, PDFont font, int font_size, Color color) throws Exception {
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

