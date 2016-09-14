package dpfmanager.implementationchecker;

import static java.io.File.separator;

import dpfmanager.conformancechecker.tiff.implementation_checker.TiffImplementationChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

/**
 * Created by easy on 20/06/2016.
 */
public class ClassesTest extends TestCase {
  void assertNumberOfErrors(String filename, int errors) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);

      assertEquals(0, result);

      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      String content = tiffValidation.getXml();

      Validator v = new Validator();
      v.validateBaseline(content);

      if (errors > -1) {
        assertEquals(errors, v.getErrors().size());
      } else {
        assertEquals(true, v.getErrors().size() > 0);
      }
    } catch (Exception ex) {
      assertEquals(0, 1);
    }
  }

  void assertErrors(String filename, String error, int nerrors) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);

      assertEquals(0, result);

      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      String content = tiffValidation.getXml();

      Validator v = new Validator();
      v.validateBaseline(content);

      assertEquals(nerrors, v.getErrors().size());
      assertEquals(true, v.getErrors().get(0).getMessage().contains(error));
    } catch (Exception ex) {
      assertEquals(0, 1);
    }
  }

  void assertNumberOfWarnings(String filename, int errors) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);

      assertEquals(0, result);

      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      String content = tiffValidation.getXml();

      Validator v = new Validator();
      v.validateBaseline(content);

      if (errors > -1) {
        assertEquals(errors, v.getWarnings().size());
      } else {
        assertEquals(true, v.getWarnings().size() > 0);
      }
    } catch (Exception ex) {
      assertEquals(0, 1);
    }
  }

  /**
   * Valid examples set.
   */
  public void testValidExamples() {
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_OK.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_OK_2.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_OK_3.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_OK_channels.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_OK_sampleformat.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_OK_sampleformat2.tif", 0);

    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_resolution.tif", 0);
    assertNumberOfWarnings("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_resolution.tif", 1);
  }

  /**
   * Invalid examples set.
   */
  public void testInvalidExamples() {
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_byteorder.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_channels.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_direntries.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_magic.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_overlap.tif", -1);
    //assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_overlap2.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_pointzero.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_reuse.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_width.tif", -1);

    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO date.tif", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO date2.tif", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO date3.tif", 2);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO date4.tif", 3);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO date5.tif", 1);

    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO ascii7.tif", 1);
  }

  /**
   * Invalid examples set.
   */
  public void testInvalidExamples2() {
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_byteorder.tif", "ByteOrder", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_magic.tif", "Magic Number", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO alignment.tif", "Bad alignment", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO offset out.tif", "corrupted file", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_reuse.tif", "Offset already used", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO_overlap.tif", "Overlap", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO ntags.tif", "entry", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO no ifds.tif", "least", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "Circular E.tif", "Circular", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO order.tif", "ascending", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO repeat.tif", "duplicated", 2);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO photo.tif", "Photometric", 1);
    assertErrors("src" + separator + "test" + separator + "resources" + separator + "classes" + separator + "IMG_KO photoval.tif", "Photometric Interpretation value", 1);
  }
}
