package dpfmanager.shell.modules.report.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import java.awt.image.ImagingOpException;
import java.io.IOException;

/**
 * Created by Adrià Llorens on 15/04/2016.
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
   * Actual Y position
   */
  public Integer y;

  public PDFParams(){

  }

  public void init(PDRectangle pageType) throws IOException{
    document = new PDDocument();
    PDPage page = new PDPage(pageType);
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

}
