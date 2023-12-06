/**
 * <h1>PDFParams.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.report.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.image.ImagingOpException;
import java.io.IOException;

/**
 * Created by Adria Llorens on 15/04/2016.
 */
public class PDFParams {
  /**
   * The Content stream.
   */
  private PDPageContentStream contentStream;

  /**
   * The Pdf Document
   */
  private PDDocument document;

  /**
   * The Pdf current page
   */
  private PDPage page;

  /**
   * Actual Y position
   */
  public Integer y;

  public PDFParams(){

  }

  public void init(PDRectangle pageType) throws IOException{
    document = new PDDocument();
    page = new PDPage(pageType);
    document.addPage(page);
    contentStream = new PDPageContentStream(document, page);
  }

  /**
   * Set content stream
   *
   * @param contentStream the content stream
   */
  public void setContentStream(PDPageContentStream contentStream) {
    this.contentStream = contentStream;
  }

  /**
   * Gets content stream.
   *
   * @return the content stream
   */
  public PDPageContentStream getContentStream() {
    return contentStream;
  }

  /**
   * Gets pdf document.
   *
   * @return the document
   */
  public PDDocument getDocument() {
    return document;
  }

  public void setPage(PDPage page) {
    this.page = page;
  }

  public PDPage getPage() {
    return page;
  }

  public PDPageContentStream checkNewPage() throws Exception {
    if (newPageNeeded(y)) {
      contentStream = newPage(getContentStream(), getDocument());
      y = 800;
    }
    return contentStream;
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
    PDPage page = new PDPage(PDRectangle.A4);
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
    return pos_y < 80;
  }
}
