package dpfmanager.shell.modules.report.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

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

  public PDFParams(){
    document = new PDDocument();
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
