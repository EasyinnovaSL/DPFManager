/**
 * <h1>PdfReport.java</h1> <p> This program is free software: you can redistribute it
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

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.PDFParams;

import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.model.types.abstractTiffType;

import org.apache.commons.lang.StringUtils;
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
import java.util.Hashtable;
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
    if (check || (forcecheck && nerrors+nwarnings == 0)) {
      if (nerrors > 0) {
        pdfParams = writeText(pdfParams, "This file does NOT conform to " + key, pos_x + image_width + 10, font, font_size, Color.red);
      } else if (nwarnings > 0) {
        pdfParams = writeText(pdfParams, "This file conforms to " + key + ", BUT it has some warnings", pos_x + image_width + 1, font, font_size, Color.orange);
      } else {
        pdfParams = writeText(pdfParams, "This file conforms to " + key, pos_x + image_width + 1, font, font_size, Color.green);
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
      int epErr = ir.getNEpErr(), epWar = ir.getNEpWar();
      int blErr = ir.getNBlErr(), blWar = ir.getNBlWar();
      int it0Err = ir.getNItErr(0), it0War = ir.getNItWar(0);
      int it1Err = ir.getNItErr(1), it2War = ir.getNItWar(1);
      int it2Err = ir.getNItErr(2), it1War = ir.getNItWar(2);
      int pcErr = ir.getPCErrors().size(), pcWar = ir.getPCWarnings().size();

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
      pdfParams = writeText(pdfParams, ir.getFilePath(), pos_x + image_width + 10, font, font_size);

      // Image alert
      pdfParams.y -= 30;
      font_size = 9;
      pdfParams = makeConformSection(blErr, blWar, "TIFF Baseline", pdfParams, ir.checkEP, true, pos_x, image_width, font_size, font);
      pdfParams = makeConformSection(epErr, epWar, "TIFF/EP", pdfParams, ir.checkEP, true, pos_x, image_width, font_size, font);
      pdfParams = makeConformSection(it0Err, it0War, "TIFF/IT", pdfParams, ir.checkIT0, true, pos_x, image_width, font_size, font);
      pdfParams = makeConformSection(it1Err, it1War, "TIFF/IT1", pdfParams, ir.checkIT1, true, pos_x, image_width, font_size, font);
      pdfParams = makeConformSection(it2Err, it2War, "TIFF/IT2", pdfParams, ir.checkIT2, true, pos_x, image_width, font_size, font);
      pdfParams = makeConformSection(pcErr, pcWar, "Policy checker", pdfParams, ir.checkPC, false, pos_x, image_width, font_size, font);

      // Summary table
      font_size = 8;
      pdfParams = writeText(pdfParams, "Errors", pos_x + image_width + 140, font, font_size);
      pdfParams = writeText(pdfParams, "Warnings", pos_x + image_width + 180, font, font_size);
      String dif = "";

      if (ir.hasBlValidation()) {
        pdfParams.y -= 20;
        pdfParams.getContentStream().drawLine(pos_x + image_width + 10, pdfParams.y + 15, pos_x + image_width + 230, pdfParams.y + 15);
        pdfParams = writeText(pdfParams, "Baseline", pos_x + image_width + 10, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlErr(), blErr) : "";
        pdfParams = writeText(pdfParams, blErr + dif, pos_x + image_width + 150, font, font_size, blErr > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNBlWar(), blWar) : "";
        pdfParams = writeText(pdfParams, blWar + dif, pos_x + image_width + 200, font, font_size, blWar > 0 ? Color.orange : Color.black);
      }
      if (ir.checkEP && ir.hasEpValidation()) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Tiff/Ep", pos_x + image_width + 10, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpErr(), epErr) : "";
        pdfParams = writeText(pdfParams, epErr + dif, pos_x + image_width + 150, font, font_size, epErr > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNEpWar(), epWar) : "";
        pdfParams = writeText(pdfParams, epWar + dif, pos_x + image_width + 200, font, font_size, epWar > 0 ? Color.orange : Color.black);
      }
      if (ir.checkIT0 && ir.hasItValidation(0)) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Tiff/It", pos_x + image_width + 10, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(0), it0Err) : "";
        pdfParams = writeText(pdfParams, it0Err + dif, pos_x + image_width + 150, font, font_size, it0Err > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(0), it0War) : "";
        pdfParams = writeText(pdfParams, it0War + dif, pos_x + image_width + 200, font, font_size, it0War > 0 ? Color.orange : Color.black);
      }
      if (ir.checkIT1 && ir.hasItValidation(0)) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Tiff/It-P1", pos_x + image_width + 10, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(1), it1Err) : "";
        pdfParams = writeText(pdfParams, it1Err + dif, pos_x + image_width + 150, font, font_size, it1Err > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(1), it1War) : "";
        pdfParams = writeText(pdfParams, it1War + dif, pos_x + image_width + 200, font, font_size, it1War > 0 ? Color.orange : Color.black);
      }
      if (ir.checkIT2 && ir.hasItValidation(0)) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Tiff/It-P2", pos_x + image_width + 10, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItErr(2), it2Err) : "";
        pdfParams = writeText(pdfParams, it2Err + dif, pos_x + image_width + 150, font, font_size, it2Err > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNItWar(2), it2War) : "";
        pdfParams = writeText(pdfParams, it2War + dif, pos_x + image_width + 200, font, font_size, it2War > 0 ? Color.orange : Color.black);
      }
      if (ir.checkPC) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Policy checker", pos_x + image_width + 10, font, font_size);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCErrors().size(), pcErr) : "";
        pdfParams = writeText(pdfParams, pcErr + dif, pos_x + image_width + 150, font, font_size, pcErr > 0 ? Color.red : Color.black);
        dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getPCWarnings().size(), pcWar) : "";
        pdfParams = writeText(pdfParams, pcWar + dif, pos_x + image_width + 200, font, font_size, pcWar > 0 ? Color.orange : Color.black);
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
      pdfParams.y -= 20;
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
      if (ir.checkBL) {
        pdfParams = writeErrorsWarnings(pdfParams, font, ir.getBaselineErrors(), ir.getBaselineWarnings(), pos_x, "Baseline");
      }
      if (ir.checkEP) {
        pdfParams = writeErrorsWarnings(pdfParams, font, ir.getEPErrors(), ir.getEPWarnings(), pos_x, "Tiff/EP");
      }
      if (ir.checkIT0) {
        pdfParams = writeErrorsWarnings(pdfParams, font, ir.getITErrors(0), ir.getITWarnings(0), pos_x, "Tiff/IT");
      }
      if (ir.checkIT1) {
        pdfParams = writeErrorsWarnings(pdfParams, font, ir.getITErrors(1), ir.getITWarnings(1), pos_x, "Tiff/IT-1");
      }
      if (ir.checkIT2) {
        pdfParams = writeErrorsWarnings(pdfParams, font, ir.getITErrors(2), ir.getITWarnings(2), pos_x, "Tiff/IT-2");
      }
      if (ir.checkPC) {
        pdfParams = writeErrorsWarnings2(pdfParams, font, ir.getPCErrors(), ir.getPCWarnings(), pos_x, "Policy Checker");
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
  private PDFParams writeErrorsWarnings(PDFParams pdfParams, PDFont font, List<RuleResult> errors, List<RuleResult> warnings, int pos_x, String type) throws Exception {
    int total = 0;
    int font_size = 10;
    pdfParams.y -= 20;

    pdfParams = writeText(pdfParams, type + " Conformance", pos_x, font, font_size);
    font_size = 8;
    int marg1 = 30;
    int marg2 = 75;
    int marg3 = 240;
    if ((errors != null && errors.size() > 0) || (warnings != null && warnings.size() > 0)) {
      pdfParams.y -= 20;
      pdfParams = writeText(pdfParams, "Type", pos_x, font, font_size);
      pdfParams = writeText(pdfParams, "Location", pos_x + marg1, font, font_size);
      pdfParams = writeText(pdfParams, "Reference", pos_x + marg2, font, font_size);
      pdfParams = writeText(pdfParams, "Description", pos_x + marg3, font, font_size);
    }
    if (errors != null) {
      for (RuleResult val : errors) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Error", pos_x, font, font_size, Color.red);
        pdfParams = writeText(pdfParams, val.getLocation(), pos_x + marg1, font, font_size);
        if (val.getReference() != null) pdfParams = writeText(pdfParams, val.getReference(), pos_x + marg2, font, font_size);
        pdfParams = writeText(pdfParams, val.getDescription(), pos_x + marg3, font, font_size);
        total++;
      }
    }
    if (warnings != null) {
      for (RuleResult val : warnings) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Warning", pos_x, font, font_size, Color.orange);
        pdfParams = writeText(pdfParams, val.getLocation(), pos_x + marg1, font, font_size);
        if (val.getReference() != null) pdfParams = writeText(pdfParams, val.getReference(), pos_x + marg2, font, font_size);
        pdfParams = writeText(pdfParams, val.getDescription(), pos_x + marg3, font, font_size);
        total++;
      }
    }
    if (total == 0) {
      pdfParams.y -= 20;
      PDXObjectImage ximage = new PDJpeg(pdfParams.getDocument(), getFileStreamFromResources("images/ok.jpg"));
      pdfParams.getContentStream().drawXObject(ximage, pos_x + 8, pdfParams.y, 5, 5);
      pdfParams = writeText(pdfParams, "This file conforms to " + type, pos_x + 15, font, font_size, Color.green);
    }
    return pdfParams;
  }

  private PDFParams writeErrorsWarnings2(PDFParams pdfParams, PDFont font, List<RuleResult> errors, List<RuleResult> warnings, int pos_x, String type) throws Exception {
    int total = 0;
    int font_size = 10;
    pdfParams.y -= 20;
    pdfParams = writeText(pdfParams, type + " Conformance", pos_x, font, font_size);

    font_size = 8;
    if ((errors != null && errors.size() > 0) || (warnings != null && warnings.size() > 0)) {
      pdfParams.y -= 20;
      pdfParams = writeText(pdfParams, "Type", pos_x, font, font_size);
      pdfParams = writeText(pdfParams, "Location", pos_x + 50, font, font_size);
      pdfParams = writeText(pdfParams, "Description", pos_x + 100, font, font_size);
    }
    if (errors != null) {
      for (RuleResult val : errors) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Error", pos_x, font, font_size, Color.red);
        pdfParams = writeText(pdfParams, val.getLocation(), pos_x + 50, font, font_size);
        pdfParams = writeText(pdfParams, val.getDescription(), pos_x + 100, font, font_size);
        total++;
      }
    }
    if (warnings != null) {
      for (RuleResult val : warnings) {
        pdfParams.y -= 20;
        pdfParams = writeText(pdfParams, "Warning", pos_x, font, font_size, Color.orange);
        pdfParams = writeText(pdfParams, val.getLocation(), pos_x + 50, font, font_size);
        pdfParams = writeText(pdfParams, val.getDescription(), pos_x + 100, font, font_size);
        total++;
      }
    }
    if (total == 0) {
      pdfParams.y -= 20;
      PDXObjectImage ximage = new PDJpeg(pdfParams.getDocument(), getFileStreamFromResources("images/ok.jpg"));
      pdfParams.getContentStream().drawXObject(ximage, pos_x + 8, pdfParams.y, 5, 5);
      pdfParams = writeText(pdfParams, "This file conforms to " + type, pos_x + 15, font, font_size, Color.green);
    }
    return pdfParams;
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

