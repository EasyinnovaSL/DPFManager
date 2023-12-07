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
 * @author Victor Muñoz Sola
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.model.types.abstractTiffType;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.PDFParams;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;

/**
 * Created by easy on 06/05/2016.
 */
public class PdfReport extends Report {
  /**
   * The Init posy.
   */
  int init_posy = 800;

  PDFParams makeConformSection(int nerrors, int nerrorspolicy, String key, PDFParams pdfParams0, int pos_x, int image_width, int font_size, PDFont font) throws Exception {
    PDFParams pdfParams = pdfParams0;
    String imgPath = "images/pdf/check.png";
    if (nerrorspolicy == -1){
      if (nerrors > 0) {
        imgPath = "images/pdf/error.png";
        pdfParams = writeTitle(pdfParams, key, imgPath, Color.red, pos_x + image_width + 10, font, font_size, 7);
      } else {
        pdfParams = writeTitle(pdfParams, key, imgPath, Color.green, pos_x + image_width + 10, font, font_size, 7);
      }
    } else {
      if (nerrorspolicy > 0) {
        imgPath = "images/pdf/error.png";
        pdfParams = writeTitle(pdfParams, key, imgPath, Color.red, pos_x + image_width + 10, font, font_size, 7);
      } else {
        if (nerrorspolicy != nerrors){
          key += " (with custom policy)";
        }
        pdfParams = writeTitle(pdfParams, key, imgPath, Color.green, pos_x + image_width + 10, font, font_size, 7);
      }
    }
    pdfParams.y -= 10;
    return pdfParams;
  }

  private List<String> detectIncoherency(String valueTag, String valueIptc, String valueXmp, String name, int nifd) {
    List<String> list = new ArrayList<>();
    if (valueTag != null && valueIptc != null && !valueTag.equals(valueIptc)) {
      if (valueTag.trim().length() == 0)
        list.add(name + " on IPTC (" + valueIptc + ") does not match with " + name + " on TAG, which is empty, in IFD " + nifd);
      else if (valueIptc.trim().length() == 0)
        list.add(name + " on TAG (" + valueTag + ") does not match with " + name + " on IPTC, which is empty, in IFD " + nifd);
      else
        list.add(name + " on TAG (" + valueTag + ") does not match with " + name + " on IPTC (" + valueIptc + ") in IFD " + nifd);
    }
    if (valueTag != null && valueXmp != null && !valueTag.equals(valueXmp)) {
      if (valueTag.trim().length() == 0)
        list.add(name + " on XMP field dc:creator (" + valueXmp + ") does not match with " + name + " on TAG, which is empty, in IFD " + nifd);
      else if (valueXmp.trim().length() == 0)
        list.add(name + " on TAG (" + valueTag + ") does not match with " + name + " on XMP field dc:creator, which is empty, in IFD " + nifd);
      else
        list.add(name + " on TAG (" + valueTag + ") does not match with " + name + " on XMP field dc:creator (" + valueXmp + ") in IFD " + nifd);
    }
    if (valueIptc != null && valueXmp != null && !valueIptc.equals(valueXmp)) {
      if (valueIptc.trim().length() == 0)
        list.add(name + " on XMP field dc:creator (" + valueXmp + ") does not match with " + name + " on IPTC, which is empty, in IFD " + nifd);
      else if (valueXmp.trim().length() == 0)
        list.add(name + " on IPTC (" + valueIptc + ") does not match with " + name + " on XMP field dc:creator, which is empty, in IFD " + nifd);
      else
        list.add(name + " on IPTC (" + valueIptc + ") does not match with " + name + " on XMP field dc:creator (" + valueXmp + ") in IFD " + nifd);
    }
    return list;
  }

  static String getFileType(String path) {
    String fileType = null;
    fileType = path.substring(path.lastIndexOf('.') + 1).toUpperCase();
    return fileType;
  }

  public static String getReportName(String internalReportFolder, String realFilename, int idReport) {
    String reportName = internalReportFolder + idReport + "-" + new File(realFilename).getName();
    File file = new File(reportName);
    int index = 0;
    while (file.exists()) {
      index++;
      String ext = getFileType(reportName);
      reportName =
          internalReportFolder + idReport + "-"
              + new File(realFilename.substring(0, realFilename.lastIndexOf(".")) + index + "." + ext)
              .getName();
      file = new File(reportName);
    }
    return reportName;
  }

  /**
   * Parse an individual report to PDF.
   *
   * @param ir the individual report.
   */
  public PDDocument parseIndividual(IndividualReport ir, int id, String internalReportFolder) {
    try {
      PDFParams pdfParams = new PDFParams();
      pdfParams.init(PDRectangle.A4);
      PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

      int pos_x = 200;
      pdfParams.y = 700;
      int font_size = 18;

      // Logo
      PDImageXObject ximage;
      float scale = 3;
      synchronized (this) {
        byte[] logo = getByteArrayFileStreamFromResources("images/logo.jpg");
        ximage = null;
        try {
          ximage = PDImageXObject.createFromByteArray(pdfParams.getDocument(), logo, "images/logo.jpg");
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        if (ximage != null)
          pdfParams.getContentStream().drawImage(ximage, pos_x, pdfParams.y, 645 / scale, 300 / scale);
      }

      // Report Title
      pdfParams.y -= 30;
      pdfParams = writeText(pdfParams, "INDIVIDUAL REPORT", pos_x, font, font_size);

      // Image
      pos_x = 50;
      int max_image_height = 130;
      int max_image_width = 200;
      pdfParams.y -= (max_image_height + 30);
      int image_pos_y = pdfParams.y;
      BufferedImage thumb;
      FileInputStream fis = null;

      String fileName = getReportName("", ir.getFilePath(), id);
      String imgPath = getThumbnailPath(internalReportFolder, fileName, ir);
      if (new File(internalReportFolder + "/html/" + imgPath).exists()) {
        fis = new FileInputStream(internalReportFolder + "/html/" + imgPath);
        thumb = ImageIO.read(fis);
      } else {
        thumb = ImageIO.read(getFileStreamFromResources("html/img/not-found.jpg"));
      }
      if (thumb == null) {
        thumb = ImageIO.read(getFileStreamFromResources("html/img/error.jpg"));
      }
      int image_height = thumb.getHeight();
      int image_width = thumb.getWidth();
      if (image_height > max_image_height) {
        image_width = max_image_height * image_width / image_height;
        image_height = max_image_height;
      }
      if (image_width > max_image_width) {
        image_height = max_image_width * image_height / image_width;
        image_width = max_image_width;
      }
      ximage = JPEGFactory.createFromImage(pdfParams.getDocument(), thumb);
      pdfParams.getContentStream().drawImage(ximage, pos_x, pdfParams.y, image_width, image_height);
      image_width = max_image_width;
      if (fis != null) fis.close();

      /**
       * Image name and path
       */
      font_size = 12;
      pdfParams.y += image_height - 12;
      pdfParams = writeText(pdfParams, ir.getFileName(), pos_x + image_width + 10, font, font_size);
      font_size = 11;
      pdfParams.y -= 32;
      List<String> linesPath = splitInLines(font_size, font, ir.getFilePath().replaceAll("\\\\", "/"), 490 - image_width, "/");
      for (String line : linesPath) {
        pdfParams = writeText(pdfParams, line, pos_x + image_width + 10, font, font_size);
        pdfParams.y -= 10;
      }

      /**
       * Conformance section
       */
      pdfParams.y -= 30;
      font_size = 9;
      for (String iso : ir.getCheckedIsos()) {
        if (ir.hasValidation(iso) || ir.getNErrors(iso) == 0) {
          String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
          pdfParams = makeConformSection(ir.getNErrors(iso), ir.hasModifiedIso(iso) ? ir.getNErrorsPolicy(iso) : -1, name, pdfParams, pos_x, image_width, font_size, font);
        }
      }
      pdfParams.y -= 10;

      if (!ir.isQuick()) {
        /**
         * Summary table
         */
        font_size = 8;
        String dif = "";
        int mode = 1, col1 = 160, col2 = 210;
        if (ir.getModifiedIsos().size() == 0) {
          pdfParams = writeText(pdfParams, "Errors", pos_x + image_width + col1, font, font_size);
          pdfParams = writeText(pdfParams, "Warnings", pos_x + image_width + col2, font, font_size);
        } else {
          pdfParams = writeText(pdfParams, "Standard", pos_x + image_width + col1, font, font_size);
          pdfParams = writeText(pdfParams, "Policy", pos_x + image_width + col2, font, font_size);
          mode = 2;
        }

        for (String iso : ir.getCheckedIsos()) {
          if (ir.hasValidation(iso) || ir.getNErrors(iso) == 0) {
            String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
            pdfParams.y -= 15;
            pdfParams.getContentStream().moveTo(pos_x + image_width + 10, pdfParams.y + 10);
            pdfParams.getContentStream().lineTo(pos_x + image_width + 260, pdfParams.y + 10);
            pdfParams.getContentStream().stroke();
            if (mode == 1) {
              // Standard
              pdfParams = writeText(pdfParams, name, pos_x + image_width + 10, font, font_size);
              dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNErrors(iso), ir.getNErrors(iso)) : "";
              pdfParams = writeText(pdfParams, ir.getNErrors(iso) + dif, pos_x + image_width + 170, font, font_size, ir.getNErrors(iso) > 0 ? Color.red : Color.black);
              dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNWarnings(iso), ir.getNWarnings(iso)) : "";
              pdfParams = writeText(pdfParams, ir.getNWarnings(iso) + dif, pos_x + image_width + 230, font, font_size, ir.getNWarnings(iso) > 0 ? Color.orange : Color.black);
            } else {
              // With policy
              pdfParams.y -= 5;
              pdfParams = writeText(pdfParams, name, pos_x + image_width + 10, font, font_size);
              pdfParams.y += 5;
              // Errors
              dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNErrors(iso), ir.getNErrors(iso)) : "";
              pdfParams = writeText(pdfParams, ir.getNErrors(iso) + dif + " errors", pos_x + image_width + col1, font, font_size, ir.getNErrors(iso) > 0 ? Color.red : Color.black);
              if (ir.hasModifiedIso(iso)) {
                dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNErrorsPolicy(iso), ir.getNErrorsPolicy(iso)) : "";
                pdfParams = writeText(pdfParams, ir.getNErrorsPolicy(iso) + dif + " errors", pos_x + image_width + col2, font, font_size, ir.getNErrorsPolicy(iso) > 0 ? Color.red : ir.getNErrors(iso) > 0 ? Color.green : Color.black);
              }
              // Warnings
              pdfParams.y -= 10;
              dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNWarnings(iso), ir.getNWarnings(iso)) : "";
              pdfParams = writeText(pdfParams, ir.getNWarnings(iso) + dif + " warnings", pos_x + image_width + col1, font, font_size, ir.getNWarnings(iso) > 0 ? Color.orange : Color.black);
              if (ir.hasModifiedIso(iso)) {
                dif = ir.getCompareReport() != null ? getDif(ir.getCompareReport().getNWarningsPolicy(iso), ir.getNWarningsPolicy(iso)) : "";
                pdfParams = writeText(pdfParams, ir.getNWarningsPolicy(iso) + dif + " warnings", pos_x + image_width + col2, font, font_size, ir.getNWarningsPolicy(iso) > 0 ? Color.orange : ir.getNWarningsPolicy(iso) > 0 ? Color.green : Color.black);
              }
            }
          }
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
        ximage = PDImageXObject.createFromByteArray(pdfParams.getDocument(), getByteArrayFileStreamFromResources("images/doc.jpg"), "images/doc.jpg");
        pdfParams.getContentStream().drawImage(ximage, pos_x, pdfParams.y, 5, 7);
        pdfParams = writeText(pdfParams, ifd + typ, pos_x + 7, font, font_size);
        if (ifd.getsubIFD() != null) {
          pdfParams.y -= 18;
          if (ifd.getImageSize() < ifd.getsubIFD().getImageSize()) typ = " - Main image";
          else typ = " - Thumbnail";
          pdfParams.getContentStream().drawImage(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "SubIFD" + typ, pos_x + 15 + 7, font, font_size);
        }
        if (ifd.containsTagId(34665)) {
          pdfParams.y -= 18;
          pdfParams.getContentStream().drawImage(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "EXIF", pos_x + 15 + 7, font, font_size);
        }
        if (ifd.containsTagId(700)) {
          pdfParams.y -= 18;
          pdfParams.getContentStream().drawImage(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "XMP", pos_x + 15 + 7, font, font_size);
        }
        if (ifd.containsTagId(33723)) {
          pdfParams.y -= 18;
          pdfParams.getContentStream().drawImage(ximage, pos_x + 15, pdfParams.y, 5, 7);
          pdfParams = writeText(pdfParams, "IPTC", pos_x + 15 + 7, font, font_size);
        }
        ifd = ifd.getNextIFD();
      }

      /**
       * Tags
       */
      font_size = 7;
      Map<String, List<ReportTag>> tags = parseTags(ir);
      for (String key : sortByTag(tags.keySet())) {
        /**
         * IFD
         */
        if (key.startsWith("ifd") && !key.endsWith("e") && !key.endsWith("d")) {
          pdfParams.y -= 40;
          pdfParams = writeTitle(pdfParams, "IFD Tags", "images/pdf/tag.png", pos_x, font, 10);
          pdfParams.y -= 20;
          Integer[] margins = {2, 40, 180};
          pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("ID", "Name", "Value"), margins);
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 15;
            String sDif = "";
            if (tag.dif < 0) sDif = " (-)";
            else if (tag.dif > 0) sDif = " (+)";
            pdfParams = writeText(pdfParams, tag.tv.getId() + sDif, pos_x + margins[0], font, font_size);
            pdfParams = writeText(pdfParams, (tag.tv.getName().equals(tag.tv.getId() + "") ? "Private tag" : tag.tv.getName()), pos_x + margins[1], font, font_size);
            pdfParams = writeText(pdfParams, tag.tv.getFirstTextReadValue(), pos_x + margins[2], font, font_size);
          }
        }

        /**
         * IFD  expert
         */
        else if (key.startsWith("ifd") && !key.endsWith("d")) {
          pdfParams.y -= 40;
          pdfParams = writeTitle(pdfParams, "IFD Expert Tags", "images/pdf/tag.png", pos_x, font, 10);
          pdfParams.y -= 20;
          Integer[] margins = {2, 40, 180};
          pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("ID", "Name", "Value"), margins);
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 15;
            String sDif = "";
            if (tag.dif < 0) sDif = " (-)";
            else if (tag.dif > 0) sDif = " (+)";
            pdfParams = writeText(pdfParams, tag.tv.getId() + sDif, pos_x + margins[0], font, font_size);
            pdfParams = writeText(pdfParams, (tag.tv.getName().equals(tag.tv.getId() + "") ? "Private tag" : tag.tv.getName()), pos_x + margins[1], font, font_size);
            pdfParams = writeText(pdfParams, tag.tv.getFirstTextReadValue(), pos_x + margins[2], font, font_size);
          }
        }

        /**
         * IFD default
         */
        else if (key.startsWith("ifd") && key.endsWith("d")) {
          pdfParams.y -= 40;
          pdfParams = writeTitle(pdfParams, "IFD Default Tags", "images/pdf/tag.png", pos_x, font, 10);
          pdfParams.y -= 20;
          Integer[] margins = {2, 40, 180};
          pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("ID", "Name", "Default value"), margins);
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 15;
            String sDif = "";
            if (tag.dif < 0) sDif = " (-)";
            else if (tag.dif > 0) sDif = " (+)";
            pdfParams = writeText(pdfParams, tag.tv.getId() + sDif, pos_x + margins[0], font, font_size);
            pdfParams = writeText(pdfParams, (tag.tv.getName().equals(tag.tv.getId() + "") ? "Private tag" : tag.tv.getName()), pos_x + margins[1], font, font_size);
            pdfParams = writeText(pdfParams, (tag.isDefault) ? tag.defaultValue : tag.tv.getFirstTextReadValue(), pos_x + margins[2], font, font_size);
          }
        }

        /**
         * SUB
         */
        if (key.startsWith("sub")) {
          for (ReportTag tag : tags.get(key)) {
            pdfParams.y -= 40;
            pdfParams = writeTitle(pdfParams, "Sub IFD Tags", "images/pdf/tag.png", pos_x, font, 10);
            pdfParams.y -= 20;
            Integer[] margins = {2, 40, 180};
            pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("ID", "Name", "Value"), margins);
            IFD sub = (IFD) tag.tv.getValue().get(0);
            for (TagValue tv : sub.getTags().getTags()) {
              pdfParams.y -= 15;
              pdfParams = writeText(pdfParams, tv.getId() + "", pos_x + margins[0], font, font_size);
              pdfParams = writeText(pdfParams, (tv.getName().equals(tv.getId() + "") ? "Private tag" : tv.getName()), pos_x + margins[1], font, font_size);
              pdfParams = writeText(pdfParams, tv.getFirstTextReadValue(), pos_x + margins[2], font, font_size);
            }
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
            if (tag.tv.getValue().size() > 0) {
              abstractTiffType obj = tag.tv.getValue().get(0);
              if (obj instanceof IFD) {
                IFD exif = (IFD) obj;
                for (TagValue tv : exif.getTags().getTags()) {
                  pdfParams.y -= 15;
                  pdfParams = writeText(pdfParams, tv.getId() + "", pos_x + margins[0], font, font_size);
                  pdfParams = writeText(pdfParams, (tv.getName().equals(tv.getId() + "") ? "Private tag" : tv.getName()), pos_x + margins[1], font, font_size);
                  pdfParams = writeText(pdfParams, tv.getFirstTextReadValue(), pos_x + margins[2], font, font_size);
                }
              }
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
            if (tag.tv.getReadValue().size() > 0) {
              abstractTiffType obj = tag.tv.getReadValue().get(0);
              if (obj instanceof IPTC) {
                IPTC iptc = (IPTC) obj;
                Metadata metadata = iptc.createMetadata();
                for (String mKey : iptc.createMetadata().keySet()) {
                  pdfParams.y -= 15;
                  pdfParams = writeText(pdfParams, mKey, pos_x + margins[0], font, font_size);
                  pdfParams = writeText(pdfParams, metadata.get(mKey).toString().trim(), pos_x + margins[1], font, font_size);
                }
              }
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
            if (tag.tv.getReadValue().size() > 0) {
              XMP xmp = (XMP) tag.tv.getReadValue().get(0);
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
                    pdfParams.getContentStream().moveTo(pos_x, pdfParams.y - 5);
                    pdfParams.getContentStream().lineTo(pos_x + 490, pdfParams.y - 5);
                    pdfParams.getContentStream().stroke();                  }
                  pdfParams.y -= 15;
                  pdfParams = writeText(pdfParams, hKey, pos_x + margins[0], font, font_size);
                  pdfParams = writeText(pdfParams, kv.get(hKey).toString().trim(), pos_x + margins[1], font, font_size);
                  nh++;
                }
              }
            }
          }
        }
      }

      if (!ir.isQuick()) {
        /**
         * Metadata incoherencies
         */
        pdfParams.y -= 40;
        pdfParams = writeTitle(pdfParams, "Metadata analysis", "images/pdf/metadata.png", pos_x, font, 10);
        pdfParams.y -= 20;
        Integer[] margins = {2, 30};
        pdfParams = writeTableHeaders(pdfParams, pos_x, font_size, font, Arrays.asList("", "Description"), margins);

        int nifd = 1;
        List<String> rows = new ArrayList<>();
        while (ir.getAuthorTag().containsKey(nifd) || ir.getAuthorIptc().containsKey(nifd) || ir.getAuthorXmp().containsKey(nifd)) {
          rows.addAll(detectIncoherency(ir.getAuthorTag().get(nifd), ir.getAuthorIptc().get(nifd), ir.getAuthorXmp().get(nifd), "Author", nifd));
          nifd++;
        }
        if (rows.isEmpty()) {
          pdfParams.y -= 15;
          PDImageXObject titleImage = PDImageXObject.createFromByteArray(pdfParams.getDocument(), getByteArrayFileStreamFromResources("images/pdf/check.png"), "images/pdf/check.png");
          pdfParams.getContentStream().drawImage(titleImage, pos_x + 5, pdfParams.y - 1, 9, 9);
          pdfParams = writeText(pdfParams, "No metadata incoherencies found", pos_x + margins[1], font, font_size);
        }
        for (String row : rows) {
          pdfParams.y -= 15;
          PDImageXObject titleImage = PDImageXObject.createFromByteArray(pdfParams.getDocument(), getByteArrayFileStreamFromResources("images/pdf/error.png"), "images/pdf/error.png");
          pdfParams.getContentStream().drawImage(titleImage, pos_x + 5, pdfParams.y - 1, 9, 9);
          pdfParams = writeText(pdfParams, row, pos_x + margins[1], font, font_size);
        }
      }

      if (!ir.isQuick()) {
        /**
         * Conformance checkers
         */
        pdfParams.y -= 40;
        font_size = 10;
        pdfParams = writeTitle(pdfParams, "Conformance checkers", "images/pdf/thumbs.png", pos_x, font, font_size);
        for (String iso : ir.getCheckedIsos()) {
          if (ir.hasValidation(iso) && !iso.equals(TiffConformanceChecker.POLICY_ISO)) {
            pdfParams = writeErrorsWarnings(pdfParams, font, ir, iso, pos_x, false);
          }
        }

        /**
         * Policy checkers
         */
        pdfParams.y -= 40;
        font_size = 10;
        pdfParams = writeTitle(pdfParams, "Policy checkers", "images/pdf/thumbs.png", pos_x, font, font_size);
        for (String iso : ir.getCheckedIsos()) {
          if (ir.hasValidation(iso) && ir.hasModifiedIso(iso)) {
            pdfParams = writeErrorsWarnings(pdfParams, font, ir, iso, pos_x, true);
          }
        }
        if (ir.hasValidation(TiffConformanceChecker.POLICY_ISO)) {
          String iso = TiffConformanceChecker.POLICY_ISO;
          pdfParams = writeErrorsWarnings(pdfParams, font, ir, iso, pos_x, false);
        }
      }

      pdfParams.getContentStream().close();

      return pdfParams.getDocument();
    } catch (Exception tfe) {
      tfe.printStackTrace();
      return null;
      //context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception in ReportPDF", tfe));
    }
  }

  /**
   * Write errors warnings int.
   *
   * @param pdfParams the pdf params
   * @param font      the font
   * @param pos_x     the pos x
   * @return the int
   * @throws Exception the exception
   */
  private PDFParams writeErrorsWarnings(PDFParams pdfParams, PDFont font, IndividualReport ir, String iso, int pos_x, boolean relaxed) throws Exception {
    String name = iso.equals(TiffConformanceChecker.POLICY_ISO) ? TiffConformanceChecker.POLICY_ISO_NAME : ImplementationCheckerLoader.getIsoName(iso);
    boolean isPolicy = iso.equals(TiffConformanceChecker.POLICY_ISO);
    List<RuleResult> errors, warnings, infos;
    if (!relaxed) {
      errors = ir.getErrors(iso);
      warnings = ir.getOnlyWarnings(iso);
      infos = ir.getOnlyInfos(iso);
    } else {
      errors = ir.getErrorsPolicy(iso);
      warnings = ir.getOnlyWarningsPolicy(iso);
      infos = ir.getOnlyInfosPolicy(iso);
    }

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
    if (!relaxed) {
      if (errors.size() > 0) {
        imgPath = "images/pdf/error.png";
      } else if (warnings.size() > 0) {
        imgPath = "images/pdf/warning.png";
      }
    } else {
      if (ir.getNErrorsPolicy(iso) > 0){
        imgPath = "images/pdf/error.png";
      } else if (ir.getNWarningsPolicy(iso) > 0){
        imgPath = "images/pdf/warning.png";
      }
    }
    pdfParams = writeTitle(pdfParams, name, imgPath, pos_x, font, font_size);
    font_size = 8;

    // Errors, warnings and infos list
    if (errors.size() + warnings.size() + infos.size() > 0) {
      pdfParams.y -= 20;
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
      pdfParams = writeResult(pdfParams, val, !relaxed ? "images/pdf/error.png" : "images/pdf/minus.png", pos_x, font, margins, isPolicy);
    }
    for (RuleResult val : warnings) {
      pdfParams = writeResult(pdfParams, val, !relaxed ? "images/pdf/warning.png" : "images/pdf/minus.png", pos_x, font, margins, isPolicy);
    }
    for (RuleResult val : infos) {
      pdfParams = writeResult(pdfParams, val, !relaxed ? "images/pdf/info.png" : "images/pdf/minus.png", pos_x, font, margins, isPolicy);
    }
    return pdfParams;
  }

  private PDFParams writeResult(PDFParams pdfParams, RuleResult val, String imgPath, int pos_x, PDFont font, Integer[] margins, boolean isPolicy) throws Exception {
    int font_size = 8;
    pdfParams.y -= 10;

    pdfParams.checkNewPage();

    //Type
    PDImageXObject titleImage = PDImageXObject.createFromByteArray(pdfParams.getDocument(), getByteArrayFileStreamFromResources(imgPath), "images/pdf/check.png");
    pdfParams.getContentStream().drawImage(titleImage, pos_x + 5, pdfParams.y - 1, 9, 9);

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

  private List<String> sortByTag(Set<String> keysSet) {
    List<String> keys = new ArrayList<>(keysSet);
    keys.sort(new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        String sub1 = o1.substring(0, 3);
        String sub2 = o2.substring(0, 3);
        if (sub1.equals(sub2)) {
          return o1.compareTo(o2);
        } else if (sub1.equals("ifd") || sub2.equals("ifd")) {
          return sub1.equals("ifd") ? -1 : 1;
        } else if (sub1.equals("sub") || sub2.equals("sub")) {
          return sub1.equals("sub") ? -1 : 1;
        } else if (sub1.equals("exi") || sub2.equals("exi")) {
          return sub1.equals("exi") ? -1 : 1;
        } else if (sub1.equals("xmp") || sub2.equals("xmp")) {
          return sub1.equals("xmp") ? -1 : 1;
        } else if (sub1.equals("ipt") || sub2.equals("ipt")) {
          return sub1.equals("ipt") ? -1 : 1;
        }
        return 0;
      }
    });
    return keys;
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
    return writeText(pdfParams, text, x, 99999, font, font_size, Color.black, null, 0);
  }

  private PDFParams writeText(PDFParams pdfParams, String text, int x, int max_x, PDFont font, int font_size) throws Exception {
    return writeText(pdfParams, text, x, max_x, font, font_size, Color.black, null, 0);
  }

  private PDFParams writeText(PDFParams pdfParams, String text, int x, PDFont font, int font_size, Color color) throws Exception {
    return writeText(pdfParams, text, x, 99999, font, font_size, color, null, 0);
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
  private PDFParams writeText(PDFParams pdfParams, String text, int x, int max_x, PDFont font, int font_size, Color color, PDImageXObject image, int imgSize) throws Exception {
    PDPageContentStream contentStream = pdfParams.getContentStream();

    try {
      contentStream = pdfParams.checkNewPage();

      if (image != null) {
        contentStream.drawImage(image, x - imgSize - 3, pdfParams.y - 1, imgSize, imgSize);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      String stext = text.replace("\n", " ").replaceAll(" +", " ").trim();
      contentStream.beginText();
      contentStream.setFont(font, font_size);
      contentStream.setNonStrokingColor(color);
      contentStream.newLineAtOffset(x, pdfParams.y);
      contentStream.showText(stext);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      contentStream.endText();
      pdfParams.setContentStream(contentStream);
      return pdfParams;
    }
  }

  private PDFParams writeTitle(PDFParams pdfParams, String text, String imgPath, int x, PDFont font, int font_size) throws Exception {
    return writeTitle(pdfParams, text, imgPath, Color.black, x, font, font_size, 9);
  }

  private PDFParams writeTitle(PDFParams pdfParams, String text, String imgPath, Color color, int x, PDFont font, int font_size, int imgSize) throws Exception {
    pdfParams.checkNewPage();
    PDImageXObject titleImage = PDImageXObject.createFromByteArray(pdfParams.getDocument(), getByteArrayFileStreamFromResources("images/pdf/check.png"), "images/pdf/check.png");
    return writeText(pdfParams, text, x + imgSize + 3, 99999, font, font_size, color, titleImage, imgSize);
  }

  private PDFParams writeTableHeaders(PDFParams pdfParams, int pos_x, int font_size, PDFont font, List<String> headers, Integer[] margins) throws Exception {
    pdfParams.checkNewPage();
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
      contentStream.addRect(x, pdfParams.y - 3, max, font_size + 5);
      contentStream.fill();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      pdfParams.setContentStream(contentStream);
      return pdfParams;
    }
  }
}

