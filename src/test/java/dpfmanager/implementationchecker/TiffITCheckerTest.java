package dpfmanager.implementationchecker;

import static java.io.File.separator;

import com.easyinnova.implementation_checker.TiffImplementationChecker;
import com.easyinnova.implementation_checker.Validator;
import com.easyinnova.implementation_checker.model.TiffValidationObject;
import com.easyinnova.implementation_checker.rules.RuleResult;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import java.io.File;
import java.util.List;

/**
 * Created by easy on 23/03/2016.
 */
public class TiffITCheckerTest extends TestCase {
  void testValid(String filename, int profile) throws Exception {
    TiffReader tr = new TiffReader();
    int result = tr.readFile(filename);
    assertEquals(0, result);

    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    tic.setITFields(true);
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String content = tiffValidation.getXml();

    Validator v = new Validator();
    if (profile == 0)
      v.validate(content, "implementationcheckers/TiffITProfileChecker.xml", false);
    else if (profile == 1)
      v.validate(content, "implementationcheckers/TiffITP1ProfileChecker.xml", false);
    else
      v.validate(content, "implementationcheckers/TiffITP2ProfileChecker.xml", false);
    List<RuleResult> results = v.getErrors();

    ValidationResult validation = tr.getTiffITValidation(profile);
    assertEquals(0, results.size());
    assertEquals(validation.getErrors().size(), results.size());
  }

  void testInvalid(String filename, int profile, int errors) throws Exception {
    TiffReader tr = new TiffReader();
    int result = tr.readFile(filename);
    assertEquals(0, result);

    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    tic.setITFields(true);
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String content = tiffValidation.getXml();

    Validator v = new Validator();
    if (profile == 0)
      v.validate(content, "implementationcheckers/TiffITProfileChecker.xml", false);
    else if (profile == 1)
      v.validate(content, "implementationcheckers/TiffITP1ProfileChecker.xml", false);
    else
      v.validate(content, "implementationcheckers/TiffITP2ProfileChecker.xml", false);
    List<RuleResult> results = v.getErrors();

    if (errors > 0)
      assertEquals(errors, results.size());
    else
      assertEquals(true, results.size() > 0);
  }

  public void testValidTestP0Valid() throws Exception {
    testValid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "sample-IT.tif", 0);
  }

  public void testValidTestP0Invalid() throws Exception {
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "sample-cmyk-IT.tif", 0, -1);
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "IMG_0887_EP.tif", 0, -1);
  }

  public void testValidTestP1Valid() throws Exception {
  }

  public void testValidTestP1Invalid() throws Exception {
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "sample-IT.tif", 1, -1);
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "IMG_0887_EP.tif", 1, -1);
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "sample-cmyk-IT.tif", 1, -1);
  }

  public void testValidTestP2Valid() throws Exception {
  }

  public void testValidTestP2Invalid() throws Exception {
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "sample-IT.tif", 2, -1);
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "IMG_0887_EP.tif", 2, -1);
    testInvalid("src" + separator + "test" + separator + "resources" + separator
        + "IT Samples" + separator + "sample-cmyk-IT.tif", 2, -1);
  }
}
