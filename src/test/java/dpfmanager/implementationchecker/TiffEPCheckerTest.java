package dpfmanager.implementationchecker;

import dpfmanager.conformancechecker.tiff.implementation_checker.TiffImplementationChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.reader.TiffReader;

import java.util.List;
import static java.io.File.separator;

import junit.framework.TestCase;

/**
 * Created by easy on 18/03/2016.
 */
public class TiffEPCheckerTest extends TestCase {
  public void testValidTest() throws Exception {
    TiffReader tr = new TiffReader();
    int result = tr.readFile("src" + separator + "test" + separator + "resources" + separator
        + "TIFF_EP Samples" + separator + "IMG_0887_EP.tif");
    assertEquals(0, result);

    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String content = tiffValidation.getXml();

    Validator v = new Validator();
    v.validate(content, "implementationcheckers/TIFF_EP.xml", false);
    List<RuleResult> results = v.getErrors();

    ValidationResult validation = tr.getTiffEPValidation();
    assertEquals(0, validation.getErrors().size());
    //assertEquals(4, results.size());
    assertEquals(true, results.size() > 0);
  }

  void assertNumberOfErrors(String filename, int errors) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);

      assertEquals(0, result);
      if (errors > -1) {
        assertEquals(errors, tr.getTiffEPValidation().getErrors().size());
      } else {
        assertEquals(true, tr.getTiffEPValidation().getErrors().size() > 0);
      }

      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      String content =  tiffValidation.getXml();

      Validator v = new Validator();
      v.validate(content, "implementationcheckers/TIFF_EP.xml", false);

      if (errors > -1) {
        assertEquals(v.getErrors().size(), tr.getTiffEPValidation().getErrors().size());
      } else {
        assertEquals(true, v.getErrors().size() > 0);
      }
    } catch (Exception ex) {
      assertEquals(0, 1);
    }
  }

  /**
   * Invalid examples set.
   */
  public void testInvalidExamples() {
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Small" + separator + "Grey_stripped.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "tiffep-sample.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "tiffep-sample-EP.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "tiffep-sample-EP-jpeg.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "tiffep-sample-EP-jpeg-thumb.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "tiffep-sample-EP-thumb.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "DSC_1501_EP.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "DSC_1502_EP.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "IMG_0887.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "TIFF_EP Samples" + separator + "tiffep-sample.dng.tif", -1);
  }
}
