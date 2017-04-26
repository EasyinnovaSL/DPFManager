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

import java.util.List;

/**
 * Created by easy on 26/10/2016.
 */
public class TIATest extends TestCase {
  public void testValidTest() throws Exception {
    TiffReader tr = new TiffReader();
    int result = tr.readFile("src" + separator + "test" + separator + "resources" + separator
        + "Small" + separator + "RGB.tif");
    assertEquals(0, result);

    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String content = tiffValidation.getXml();

    Validator v = new Validator();
    v.validate(content, "implementationcheckers/TIAProfileChecker.xml", false);
    List<RuleResult> results = v.getErrors();

    assertEquals(0, results.size());
  }

  public void testInvalidTest() throws Exception {
    TiffReader tr = new TiffReader();
    int result = tr.readFile("src" + separator + "test" + separator + "resources" + separator
        + "Small" + separator + "Bilevel.tif");
    assertEquals(0, result);

    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String content = tiffValidation.getXml();

    Validator v = new Validator();
    v.validate(content, "implementationcheckers/TIAProfileChecker.xml", false);
    List<RuleResult> results = v.getErrors();

    assertEquals(1, results.size());
  }
}
