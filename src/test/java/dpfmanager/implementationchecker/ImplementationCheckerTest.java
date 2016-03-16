package dpfmanager.implementationchecker;

import dpfmanager.shell.conformancechecker.ImplementationChecker.TiffImplementationChecker;
import dpfmanager.shell.conformancechecker.ImplementationChecker.Validator;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffValidationObject;
import dpfmanager.shell.conformancechecker.ImplementationChecker.rules.RuleResult;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import java.io.File;
import java.util.List;

/**
 * Created by easy on 08/03/2016.
 */
public class ImplementationCheckerTest extends TestCase {
  public void testImplemantationCheckOk() throws Exception {
    String filename = "file.xml";

    TiffReader tr = new TiffReader();
    tr.readFile("src/test/resources/Small/Bilevel.tif");
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    tiffValidation.writeXml(filename);

    Validator v = new Validator();
    v.validate(filename);
    List<RuleResult> results = v.getErrors();

    ValidationResult validation = tr.getBaselineValidation();
    assertEquals(validation.getErrors().size(), results.size());

    new File(filename).delete();
  }

  public void testImplemantationCheckKo() throws Exception {
    String filename = "file.xml";

    TiffReader tr = new TiffReader();
    tr.readFile("src/test/resources/Block/Bad alignment Classic E.tif");
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    tiffValidation.writeXml(filename);

    Validator v = new Validator();
    v.validate(filename);
    List<RuleResult> results = v.getErrors();

    ValidationResult validation = tr.getBaselineValidation();
    assertEquals(validation.getErrors().size(), results.size());

    new File(filename).delete();
  }
}
