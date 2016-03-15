package dpfmanager.commandline;

import dpfmanager.shell.conformancechecker.ImplementationChecker.TiffImplementationChecker;
import dpfmanager.shell.conformancechecker.ImplementationChecker.Validator;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

/**
 * Created by easy on 08/03/2016.
 */
public class ImplementationCheckerTest extends TestCase {
  public void testImplemantationCheck() throws Exception {
    TiffReader tr = new TiffReader();
    tr.readFile("src/test/resources/Small/Bilevel.tif");
    TiffDocument td = tr.getModel();
    TiffImplementationChecker tic = new TiffImplementationChecker();
    tic.CreateValidationObject(td);

    Validator v = new Validator();
    v.validate("file.xml");
  }
}
