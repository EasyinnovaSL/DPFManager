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

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.PDFParams;

import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
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
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Created by easy on 06/05/2016.
 */
public class PdfReport extends Report {
  /**
   * The Init posy.
   */
  int init_posy = 800;

  PDFParams makeConformSection(int nerrors, int nwarnings, String key, PDFParams pdfParams0, int pos_x, int image_width, int font_size, PDFont font) throws Exception {
    PDFParams pdfParams = pdfParams0;
    if (nerrors > 0) {
      pdfParams = writeText(pdfParams, "NOT conform to " + key, pos_x + image_width + 10, font, font_size, Color.red);
    } else if (nwarnings > 0) {
//      pdfParams = writeText(pdfParams, "This file conforms to " + key + ", BUT it has some warnings", pos_x + image_width + 10, font, font_size, Color.orange);
      pdfParams = writeText(pdfParams, "Conforms to " + key, pos_x + image_width + 10, font, font_size, Color.green);
    } else {
      pdfParams = writeText(pdfParams, "Conforms to " + key, pos_x + image_width + 10, font, font_size, Color.green);
    }
    pdfParams.y -= 10;
    return pdfParams;
  }

  private List<String> detectIncoherency(String valueTag, String valueIptc, String valueXmp, String name, int nifd) {
    List<String> list = new ArrayList<>();
    if (valueTag != null && valueIptc != null && !valueTag.equals(valueIptc)) {
      list.add(name + " on TAG and IPTC in IFD " + nifd + " (" + valueTag + ", " + valueIptc + ")");
    }
    if (valueTag != null && valueXmp != null && !valueTag.equals(valueXmp)) {
      list.add(name + " on TAG and XMP in IFD " + nifd + " (" + valueTag + ", " + valueXmp + ")");
    }
    if (valueIptc != null && valueXmp != null && !valueIptc.equals(valueXmp)) {
      list.add(name + " on IPTC and XMP in IFD " + nifd + " (" + valueIptc + ", " + valueXmp + ")");
    }
    return list;
  }

  /**
   * Parse an individual report to PDF.
   *
   * @param ir the individual report.
   */
  public void parseIndividual(IndividualReport ir) {
    try {
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

      /**
       * Image name and path
       */
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
      for (String iso : ir.getCheckedIsos()) {
        if (ir.hasValidation(iso) || ir.getNErrors(iso) == 0) {
          String name = ImplementationCheckerLoader.getIsoName(iso);
          pdfParams = makeConformSection(ir.getNErrors(iso), ir.getNWarnings(iso), name, pdfParams, pos_x, image_width, font_size, font);
        }
      }
      pdfParams.y -= 10;

      /**
       * Summary table
       */
      font_size = 8;
      pdfParams = writeText(pdfParams, "Errors", pos_x + image_width + 170, font, font_size);
      pdfParams = writeText(pdfParams, "Warnings", pos_x + image_width + 210, font, font_size);
      String dif = "";

      for (String iso : ir.getCheckedIsos()) {
        if (ir.hasValidation(iso) || ir.getNErrors(iso) == 0) {
          String name = ImplementationCheckerLoader.getIsoName(iso);
          pdfParams.y -= 20;
          pdfParams.getContentStream().drawLine(pos_x + image_width + 10, pdfParams.y + 15, pos_x + image_width + 260, pdfParams.y + 15);
          pdfParams = writeText(pdfParams, name, pos_x + image_width + 10, font, font_size);
          dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNErrors(iso), ir.getNErrors(iso)) : "";
          pdfParams = writeText(pdfParams, ir.getNErrors(iso) + dif, pos_x + image_width + 180, font, font_size, ir.getNErrors(iso) > 0 ? Color.red : Color.black);
          dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNWarnings(iso), ir.getNWarnings(iso)) : "";
          pdfParams = writeText(pdfParams, ir.getNWarnings(iso) + dif, pos_x + image_width + 230, font, font_size, ir.getNWarnings(iso) > 0 ? Color.orange : Color.black);
        }
      }

      if (pdfParams.y > image_pos_y) pdfParams.y = image_pos_y;

      /**
       * File structure
       */
      font_size = 10;
      pdfParams.y -= 40;
      pdfParams = writeTitle(pdfParams, "File Structure", "images/pdf/site.png", pos_x, font, font_size);
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

      /**
       * Tags
       */
      font_size = 7;
      Map<String, List<ReportTag>> tags = parseTags(ir);
      for (String key : tags.keySet()) {
        /**
         * IFD
         */
        if (key.startsWith("ifd") && !key.endsWith("e")) {
          pdfParams.y -= 40;
          pdfParams = writeTitle(pdfParams, "IFD Tags", "images/pdf/tag.png", pos_x, font, 10);
          pdfParams.y -= 20;
          Integer[] margins = {2, 40, 180};
          pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("ID", "Name", "Value"), margins);
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 15;
            String sDif = "";
            if (tag.dif < 0) sDif = "(-)";
            else if (tag.dif > 0) sDif = "(+)";
            pdfParams = writeText(pdfParams, tag.tv.getId() + sDif, pos_x + margins[0], font, font_size);
            pdfParams = writeText(pdfParams, (tag.tv.getName().equals(tag.tv.getId()) ? "Private tag" : tag.tv.getName()), pos_x + margins[1], font, font_size);
            pdfParams = writeText(pdfParams, tag.tv.getDescriptiveValue(), pos_x + margins[2], font, font_size);
          }
        }
        /**
         * IFD  expert
         */
        else if (key.startsWith("ifd")) {
          pdfParams.y -= 40;
          pdfParams = writeTitle(pdfParams, "IFD Expert Tags", "images/pdf/tag.png", pos_x, font, 10);
          pdfParams.y -= 20;
          Integer[] margins = {2, 40, 180};
          pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("ID", "Name", "Value"), margins);
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 15;
            String sDif = "";
            if (tag.dif < 0) sDif = "(-)";
            else if (tag.dif > 0) sDif = "(+)";
            pdfParams = writeText(pdfParams, tag.tv.getId() + sDif, pos_x + margins[0], font, font_size);
            pdfParams = writeText(pdfParams, (tag.tv.getName().equals(tag.tv.getId()) ? "Private tag" : tag.tv.getName()), pos_x + margins[1], font, font_size);
            pdfParams = writeText(pdfParams, tag.tv.getDescriptiveValue(), pos_x + margins[2], font, font_size);
          }
        }
        /**
         * EXIF
         */
        if (key.startsWith("exi")) {
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 40;
            String index = (tag.index > 0) ? " (IFD " + tag.index + ")" : "";
            pdfParams = writeTitle(pdfParams, "EXIF Tags" + index, "images/pdf/tag.png", pos_x, font, 10);
            pdfParams.y -= 20;
            Integer[] margins = {2, 40, 180};
            pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("ID", "Name", "Value"), margins);
            IFD exif = (IFD) tag.tv.getValue().get(0);
            for (TagValue tv : exif.getTags().getTags()) {
              pdfParams.y -= 15;
              pdfParams = writeText(pdfParams, tv.getId() + "", pos_x + margins[0], font, font_size);
              pdfParams = writeText(pdfParams, (tv.getName().equals(tv.getId()) ? "Private tag" : tv.getName()), pos_x + margins[1], font, font_size);
              pdfParams = writeText(pdfParams, tv.getDescriptiveValue(), pos_x + margins[2], font, font_size);
            }
          }
        }
        /**
         * IPTC
         */
        if (key.startsWith("ipt")) {
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 40;
            String index = (tag.index > 0) ? " (IFD " + tag.index + ")" : "";
            pdfParams = writeTitle(pdfParams, "IPTC Tags" + index, "images/pdf/tag.png", pos_x, font, 10);
            pdfParams.y -= 20;
            Integer[] margins = {2, 180};
            pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("Name", "Value"), margins);
            IPTC iptc = (IPTC) tag.tv.getValue().get(0);
            Metadata metadata = iptc.createMetadata();
            for (String mKey : iptc.createMetadata().keySet()) {
              pdfParams.y -= 15;
              pdfParams = writeText(pdfParams, mKey, pos_x + margins[0], font, font_size);
              pdfParams = writeText(pdfParams, metadata.get(mKey).toString().trim(), pos_x + margins[1], font, font_size);
            }
          }
        }
        /**
         * XMP
         */
        if (key.startsWith("xmp")) {
          for (ReportTag tag : tags.get(key)) {
            // Tags
            pdfParams.y -= 40;
            String index = (tag.index > 0) ? " (IFD " + tag.index + ")" : "";
            pdfParams = writeTitle(pdfParams, "XMP Tags" + index, "images/pdf/tag.png", pos_x, font, 10);
            pdfParams.y -= 20;
            Integer[] margins = {2, 180};
            pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("Name", "Value"), margins);
            XMP xmp = (XMP) tag.tv.getValue().get(0);
            Metadata metadata = xmp.createMetadata();
            for (String xKey : metadata.keySet()) {
              pdfParams.y -= 15;
              pdfParams = writeText(pdfParams, xKey, pos_x + margins[0], font, font_size);
              pdfParams = writeText(pdfParams, metadata.get(xKey).toString().trim(), pos_x + margins[1], font, font_size);
            }
            // History
            if (xmp.getHistory() != null) {
              pdfParams.y -= 40;
              pdfParams = writeTitle(pdfParams, "History" + index, "images/pdf/tag.png", pos_x, font, 10);
              pdfParams.y -= 20;
              pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("Attribute", "Value"), margins);
              int nh = 0;
              for (Hashtable<String, String> kv : xmp.getHistory()) {
                // TODO WORKARROUND
                String hKey = kv.keySet().iterator().next();
                if (hKey.equals("action") && nh != 0) {
                  pdfParams.getContentStream().drawLine(pos_x, pdfParams.y-5, pos_x + 490, pdfParams.y-5);
                }
                pdfParams.y -= 15;
                pdfParams = writeText(pdfParams, hKey, pos_x + margins[0], font, font_size);
                pdfParams = writeText(pdfParams, kv.get(hKey).toString().trim(), pos_x + margins[1], font, font_size);
                nh++;
              }
            }
          }
        }
      }

      /**
       * Metadata incoherencies
       */
      pdfParams.y -= 40;
      pdfParams = writeTitle(pdfParams, "Metadata analysis", "images/pdf/metadata.png", pos_x, font, 10);
      pdfParams.y -= 20;
      Integer[] margins = {2, 30};
      pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("", "Description"), margins);
      IFD tdifd = td.getFirstIFD();
      int nifd = 1;
      List<String> rows = new ArrayList<>();
      while (tdifd != null) {
        XMP xmp = null;
        IPTC iptc = null;
        if (tdifd.containsTagId(TiffTags.getTagId("XMP")))
          xmp = (XMP) tdifd.getTag("XMP").getValue().get(0);
        if (tdifd.containsTagId(TiffTags.getTagId("IPTC")))
          iptc = (IPTC) tdifd.getTag("IPTC").getValue().get(0);

        // Author
        String authorTag = null;
        if (tdifd.containsTagId(TiffTags.getTagId("Artist")))
          authorTag = tdifd.getTag("Artist").toString();
        String authorIptc = null;
        if (iptc != null) authorIptc = iptc.getCreator();
        String authorXmp = null;
        if (xmp != null) authorXmp = xmp.getCreator();

        rows.addAll(detectIncoherency(authorTag, authorIptc, authorXmp, "Author", nifd));
        tdifd = tdifd.getNextIFD();
        nifd++;
      }
      if (rows.isEmpty()){
        pdfParams.y -= 15;
        PDPixelMap titleImage = new PDPixelMap(pdfParams.getDocument(), ImageIO.read(getFileStreamFromResources("images/pdf/check.png")));
        pdfParams.getContentStream().drawXObject(titleImage, pos_x + 5, pdfParams.y-1, 9, 9);
        pdfParams = writeText(pdfParams, "No metadata incoherencies found", pos_x + margins[1], font, font_size);
      }
      for (String row : rows){
        pdfParams.y -= 15;
        PDPixelMap titleImage = new PDPixelMap(pdfParams.getDocument(), ImageIO.read(getFileStreamFromResources("images/pdf/error.png")));
        pdfParams.getContentStream().drawXObject(titleImage, pos_x + 5, pdfParams.y-1, 9, 9);
        pdfParams = writeText(pdfParams, row, pos_x + margins[1], font, font_size);
      }

      /**
       * Conformance checkers
       */
      pdfParams.y -= 40;
      font_size = 10;
      pdfParams = writeTitle(pdfParams, "Conformance checkers", "images/pdf/thumbs.png", pos_x, font, font_size);
      for (String iso : ir.getIsosCheck()) {
        if (ir.hasValidation(iso)) {
          String name = ImplementationCheckerLoader.getIsoName(iso);
          pdfParams = writeErrorsWarnings(pdfParams, font, ir.getErrors(iso), ir.getOnlyWarnings(iso), ir.getOnlyInfos(iso), pos_x, name, iso.equals(TiffConformanceChecker.POLICY_ISO));
        }
      }

      pdfParams.getContentStream().close();

      ir.setPDF(pdfParams.getDocument());
    } catch (Exception tfe) {
      tfe.printStackTrace();
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
  private PDFParams writeErrorsWarnings(PDFParams pdfParams, PDFont font, List<RuleResult> errors, List<RuleResult> warnings, List<RuleResult> infos, int pos_x, String type, boolean isPolicy) throws Exception {
    int font_size = 10;
    Integer[] margins;
    if (!isPolicy) {
      // Conformance margins
      margins = new Integer[]{2, 30, 90, 150, 480};
    } else {
      // Policy margins
      margins = new Integer[]{2, 30, 200, 480};
    }
    pdfParams.y -= 20;

    String imgPath = "images/pdf/check.png";
    if (errors.size() > 0) {
      imgPath = "images/pdf/error.png";
    } else if (warnings.size() > 0) {
      imgPath = "images/pdf/warning.png";
    }
    pdfParams = writeTitle(pdfParams, type, imgPath, pos_x, font, font_size);
    font_size = 8;

    // Errors, warnings and infos list
    if (errors.size() + warnings.size() + infos.size() > 0) {
      pdfParams.y -= 20;
      pdfParams = writeRectangle(pdfParams, pos_x, font_size, 490, Color.decode("#333333"));
      List<String> headers;
      if (!isPolicy) {
        headers = Arrays.asList("Type", "ID", "Location", "Description");
      } else {
        headers = Arrays.asList("Type", "Rule", "Description");
      }
      pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, headers, margins);
    }
    pdfParams.y -= 10;
    for (RuleResult val : errors) {
      pdfParams = writeResult(pdfParams, val, "images/pdf/error.png", pos_x, font, margins, isPolicy);
    }
    for (RuleResult val : warnings) {
      pdfParams = writeResult(pdfParams, val, "images/pdf/warning.png", pos_x, font, margins, isPolicy);
    }
    for (RuleResult val : infos) {
      pdfParams = writeResult(pdfParams, val, "images/pdf/info.png", pos_x, font, margins, isPolicy);
    }
    return pdfParams;
  }

  private PDFParams writeResult(PDFParams pdfParams, RuleResult val, String imgPath, int pos_x, PDFont font, Integer[] margins, boolean isPolicy) throws Exception {
    int font_size = 8;
    pdfParams.y -= 10;

    //Type
    PDPixelMap titleImage = new PDPixelMap(pdfParams.getDocument(), ImageIO.read(getFileStreamFromResources(imgPath)));
    pdfParams.getContentStream().drawXObject(titleImage, pos_x + 5, pdfParams.y-1, 9, 9);

    if (!isPolicy) {
      // ID
      pdfParams = writeText(pdfParams, val.getRule() != null ? val.getRule().getId() : "", pos_x + margins[1], font, font_size);

      // Location
      List<String> linesLoc = splitInLines(font_size, font, val.getLocation(), margins[3] - margins[2] - 10, " ");
      for (String line : linesLoc) {
        pdfParams = writeText(pdfParams, line, pos_x + margins[2], font, font_size);
        pdfParams.y -= 10;
      }
      pdfParams.y = pdfParams.y + 10 * linesLoc.size();

      // Description
      List<String> linesDes = splitInLines(font_size, font, val.getDescription(), margins[4] - margins[3] - 10, " ");
      for (String line : linesDes) {
        pdfParams = writeText(pdfParams, line, pos_x + margins[3], font, font_size);
        pdfParams.y -= 10;
      }
      pdfParams.y += 10 * linesDes.size();

      pdfParams.y -= linesDes.size() > linesLoc.size() ? linesDes.size() * 10 : linesLoc.size() * 10;
    } else {
      // Rule
      List<String> linesRul = splitInLines(font_size, font, val.getRuleDescription(), margins[2] - margins[1] - 10, " ");
      for (String line : linesRul) {
        pdfParams = writeText(pdfParams, line, pos_x + margins[1], font, font_size);
        pdfParams.y -= 10;
      }
      pdfParams.y += 10 * linesRul.size();

      // Description
      List<String> linesDes = splitInLines(font_size, font, val.getDescription(), margins[3] - margins[2] - 10, " ");
      for (String line : linesDes) {
        pdfParams = writeText(pdfParams, line, pos_x + margins[2], font, font_size);
        pdfParams.y -= 10;
      }
      pdfParams.y += 10 * linesDes.size();

      pdfParams.y -= linesDes.size() > linesRul.size() ? linesDes.size() * 10 : linesRul.size() * 10;
    }

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
        if (pathMode) {
          line += "/";
        }
        for (int i = 1; i < words.length; i++) {
          String word = words[i];
          String aux = line + regex + word;
          if (pathMode) {
            aux = line + word + regex;
          }
          if (getSize(font_size, font, aux) > max) {
            lines.add(line);
            line = word;
            if (pathMode) {
              line += "/";
            }
          } else {
            line = aux;
          }
        }
        if (pathMode) {
          line = line.substring(0, line.length() - 1);
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
    return writeText(pdfParams, text, x, 99999, font, font_size, Color.black, null);
  }

  private PDFParams writeText(PDFParams pdfParams, String text, int x, int max_x, PDFont font, int font_size) throws Exception {
    return writeText(pdfParams, text, x, max_x, font, font_size, Color.black, null);
  }

  private PDFParams writeText(PDFParams pdfParams, String text, int x, PDFont font, int font_size, Color color) throws Exception {
    return writeText(pdfParams, text, x, 99999, font, font_size, color, null);
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
  private PDFParams writeText(PDFParams pdfParams, String text, int x, int max_x, PDFont font, int font_size, Color color, PDPixelMap image) throws Exception {
    PDPageContentStream contentStream = pdfParams.getContentStream();
    try {
      if (newPageNeeded(pdfParams.y)) {
        contentStream = newPage(contentStream, pdfParams.getDocument());
        pdfParams.y = init_posy;
      }

      if (image != null){
        pdfParams.getContentStream().drawXObject(image, x-12, pdfParams.y - 1, 9, 9);
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

  private PDFParams writeTitle(PDFParams pdfParams, String text, String imgPath, int x, PDFont font, int font_size) throws Exception {
    PDPixelMap titleImage = new PDPixelMap(pdfParams.getDocument(), ImageIO.read(getFileStreamFromResources(imgPath)));
    return writeText(pdfParams, text, x + 12, 99999, font, font_size, Color.black, titleImage);
  }

  private PDFParams writeTableHeaders(PDFParams pdfParams, int pos_x, int font_size, PDFont font, List<String> headers, Integer[] margins) throws Exception {
    pdfParams = writeRectangle(pdfParams, pos_x, font_size, 490, Color.decode("#333333"));
    int i = 0;
    for (String header : headers) {
      pdfParams = writeText(pdfParams, header, pos_x + margins[i], font, font_size, Color.lightGray);
      i++;
    }
    return pdfParams;
  }

  private PDFParams writeRectangle(PDFParams pdfParams, int x, int font_size, int max, Color color) throws Exception {
    PDPageContentStream contentStream = pdfParams.getContentStream();
    try {
      //draw rectangle
      contentStream.setNonStrokingColor(color);
      contentStream.fillRect(x, pdfParams.y - 3, max, font_size + 5);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      pdfParams.setContentStream(contentStream);
      return pdfParams;
    }
  }
}

