package dpfmanager.implementationchecker;

import static java.io.File.separator;

import dpfmanager.conformancechecker.tiff.implementation_checker.TiffImplementationChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;

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

    TiffReader tr = new TiffReader();
    tr.readFile("src/test/resources/Small/Bilevel.tif");
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String content = tiffValidation.writeString();

    Validator v = new Validator();
    v.validateBaseline(content);
    List<RuleResult> results = v.getErrors();

    ValidationResult validation = tr.getBaselineValidation();
    assertEquals(validation.getErrors().size(), results.size());
  }

  public void testImplemantationCheckKo() throws Exception {
    TiffReader tr = new TiffReader();
    tr.readFile("src/test/resources/Block/Bad alignment Classic E.tif");
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String content = tiffValidation.writeString();

    Validator v = new Validator();
    v.validateBaseline(content);
    List<RuleResult> results = v.getErrors();

    ValidationResult validation = tr.getBaselineValidation();
    assertEquals(validation.getErrors().size(), results.size());
  }

  void assertNumberOfErrors(String filename, int errors) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);

      assertEquals(0, result);
      if (errors > -1) {
        assertEquals(errors, tr.getBaselineValidation().getErrors().size());
      } else {
        assertEquals(true, tr.getBaselineValidation().getErrors().size() > 0);
      }

      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      String content = tiffValidation.writeString();

      Validator v = new Validator();
      v.validateBaseline(content);

      if (errors > -1) {
        assertEquals(v.getErrors().size(), tr.getBaselineValidation().getErrors().size());
      } else {
        assertEquals(true, v.getErrors().size() > 0);
      }
    } catch (Exception ex) {
      assertEquals(0, 1);
    }
  }

  /**
   * Valid examples set.
   */
  public void testValidExamples() {
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Small" + separator + "Bilevel.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Classic Intel.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Classic Motorola.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Colorspace" + separator + "F32.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD tree" + separator + "Recommended list.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD tree" + separator + "Old school E.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky multistrip.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky singlestrip.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky tile.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar multistrip.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar singlestrip.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar tile.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Motorola nopred nocomp.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Motorola pred nocomp.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Intel nopred nocomp.tif", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Intel pred nocomp.tif", 0);
  }

  /**
   * Invalid examples set.
   */
  public void testInvalidExamples() {
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Nonsense byteorder E.tif", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Block" + separator + "Bad alignment Classic E.tif", 3);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD struct" + separator + "Insane tag count E.tif", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD struct" + separator + "Circular E.tif", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD struct" + separator + "Circular short E.tif", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD struct" + separator + "Beyond EOF E.tif", 1);
    //assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD struct" + separator + "Premature EOF E.tif", -1);
  }
}
