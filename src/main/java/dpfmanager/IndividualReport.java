package dpfmanager;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class IndividualReport.
 */
public class IndividualReport {
  
  /* The file name*/
  private String filename;
  
  /* The ifdNode count*/
  private int ifdCount;
  
  /* The isimg list*/
  private List<Boolean> listIsimg;
  
  /* The hasSubIfd list*/
  private List<Boolean> listHasSubIfd;
  
  /* The Tiff width*/
  private String width;
  
  /* The Tiff height*/
  private String height;
  
  /* The errors list*/
  private List<ValidationEvent> errors;
  
  /* The warning list*/
  private List<ValidationEvent> warnings;
  
  /* The Tiff Document object */
  private TiffDocument tiffModel;
  
  /**
   * Default constructor.
   *
   */
  public IndividualReport() {
    filename = "";
    ifdCount = 0;
    listIsimg = new ArrayList<Boolean>();
    listHasSubIfd = new ArrayList<Boolean>();
  }
  
  /**
   * Constructor + generate.
   *
   */
  public IndividualReport(String name, TiffDocument tiffModel, ValidationResult validation) {
    filename = name;
    ifdCount = 0;
    listIsimg = new ArrayList<Boolean>();
    listHasSubIfd = new ArrayList<Boolean>();
    generate(tiffModel, validation);
  }
  
  /**
   * Set file name.
   *
   */
  public void setFileName(String name) {
    filename = name;
  }
  
  /**
   * Get file name.
   *
   * @return filename
   */
  public String getFileName() {
    return filename;
  }
  
  /**
   * Saves the ifd node.
   *
   * @param ifd the ifd
   */
  private void saveIfdNode(IFD ifd) {
    listIsimg.add(ifd.isImage());
    listHasSubIfd.add(ifd.getsubIFD() != null);
    ifdCount++;
  }

  /**
   * Generate the report information.
   *
   */
  public void generate(TiffDocument tiffModel, ValidationResult validation) {
    this.tiffModel = tiffModel;
    // tiff structure
    IFD ifd = tiffModel.getFirstIFD();
    if (ifd != null) {
      saveIfdNode(ifd);
      while (ifd.hasNextIFD()) {
        ifd = ifd.getNextIFD();
        saveIfdNode(ifd);
      }
    }

    // basic info
    width = tiffModel.getMetadataSingleString("ImageWidth");
    height = tiffModel.getMetadataSingleString("ImageLength");

    // errors & warnings
    errors = validation.getErrors();
    warnings = validation.getWarnings();
  }
  
  /**
   * Get ifd count.
   *
   * @return the numbr of ifd's
   */
  public int getIfdCount() {
    return ifdCount;
  }
  
  /**
   * Get isimg at index.
   *
   * @param index the index
   * @return the isimg list
   */
  public Boolean getIsimgAt(int index) {
    return listIsimg.get(index);
  }
  
  /**
   * Get hasSubIfd at index.
   *
   * @param index the index
   * @return the list of hasSubIfd
   */
  public Boolean getHasSubIfdAt(int index) {
    return listHasSubIfd.get(index);
  }
  
  /**
   * Get width.
   *
   * @return the width
   */
  public String getWidth() {
    return width;
  }
  
  /**
   * Get height.
   *
   * @return the height
   */
  public String getHeight() {
    return height;
  }
  
  /**
   * Get errors list.
   *
   * @return the errors
   */
  public List<ValidationEvent> getErrors() {
    return errors;
  }
  
  /**
   * Get warnings list.
   *
   * @return the warnings
   */
  public List<ValidationEvent> getWarnings() {
    return warnings;
  }
  
  /**
   * Get Tiff Model.
   *
   * @return the tiffModel
   */
  public TiffDocument getTiffModel() {
    return tiffModel;
  }
  
}
