package dpfmanager.implementationchecker;

import dpfmanager.conformancechecker.tiff.implementation_checker.TiffImplementationChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import static java.io.File.separator;

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
    v.validateBaseline(filename);
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
    v.validateBaseline(filename);
    List<RuleResult> results = v.getErrors();

    ValidationResult validation = tr.getBaselineValidation();
    assertEquals(validation.getErrors().size(), results.size());

    new File(filename).delete();
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

      String xml = "file.xml";
      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      tiffValidation.writeXml(xml);

      Validator v = new Validator();
      v.validateBaseline(xml);

      if (errors > -1) {
        assertEquals(v.getErrors().size(), tr.getBaselineValidation().getErrors().size());
      } else {
        assertEquals(true, v.getErrors().size() > 0);
      }

      new File(xml).delete();
    } catch (Exception ex) {
      assertEquals(0, 1);
    }
  }

  /**
   * Valid examples set.
   */
  public void testValidExamples() {
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Small" + separator + "Bilevel.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Classic Intel.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Classic Motorola.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Colorspace" + separator + "F32.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD tree" + separator + "Recommended list.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD tree" + separator + "Old school E.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky multistrip.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky singlestrip.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Chunky tile.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar multistrip.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar singlestrip.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Organization" + separator + "Planar tile.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Motorola nopred nocomp.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Motorola pred nocomp.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Intel nopred nocomp.TIF", 0);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Compression" + separator + "Intel pred nocomp.TIF", 0);
  }

  /**
   * Invalid examples set.
   */
  public void testInvalidExamples() {
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Header" + separator + "Nonsense byteorder E.TIF", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "Block" + separator + "Bad alignment Classic E.tif", 3);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Insane tag count E.TIF", -1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Circular E.TIF", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Circular Short E.TIF", 1);
    assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Beyond EOF E.TIF", 1);
    //assertNumberOfErrors("src" + separator + "test" + separator + "resources" + separator + "IFD Struct" + separator + "Premature EOF E.TIF", -1);
  }
}
