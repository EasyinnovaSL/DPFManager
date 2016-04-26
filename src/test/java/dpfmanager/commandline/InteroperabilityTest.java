package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.TiffImplementationChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by easy on 08/02/2016.
 */
public class InteroperabilityTest extends CommandLineTest {
  @Test
  public void testDpfManager() {
    try {
      // Get the Tiff file model
      TiffReader tr = new TiffReader();
      tr.readFile("src/test/resources/Small/Bilevel.tif");
      TiffDocument td = tr.getModel();

      // Get the first image (a Tiff file could contain more than one image)
      IFD ifd = td.getFirstIFD();

      // Get the tags of the image
      IfdTags tags = ifd.getTags();

      // Get a concrete tag (either by the tag id or the tag name)
      TagValue tv = tags.get("ImageWidth");

      // Get the tag value (parse the string to the desired type)
      String value = tv.toString();
      int width = Integer.parseInt(value);
      assertEquals(999, width);

      // Get the report object
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);

      Validator v = new Validator();
      v.validateBaseline(tiffValidation);
      int numberOfErrors = v.getErrors().size();
      assertEquals(0, numberOfErrors);
    } catch (ReadTagsIOException e) {
      assertEquals(1, 0);
    } catch (ReadIccConfigIOException e) {
      assertEquals(1, 0);
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFunctionCall() throws Exception {
    String path = "src/test/resources/Small/Bilevel.tif";
    String report = TiffConformanceChecker.RunTiffConformanceCheckerAndSendReport(path);
    assertEquals(true, report != null);
  }

  @Test
  public void testValidation() throws Exception {
    String path = "src/test/resources/Small/Bilevel.tif";

    TiffReader tr = new TiffReader();
    tr.readFile(path);
    TiffDocument td = tr.getModel();

    TiffImplementationChecker tic = new TiffImplementationChecker();
    TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
    String xml = tiffValidation.getXml();
    assertEquals(true, xml != null);
    assertEquals(true, xml.contains("<tiffValidationObject>"));
    assertEquals(true, xml.contains("<correctStrips>1</correctStrips>"));
    assertEquals(true, xml.contains("<tag id=\"257\" name=\"ImageLength\">"));
    assertEquals(true, xml.contains("<size>115396</size>"));
    assertEquals(true, xml.contains("</tiffValidationObject>"));
  }

  @Test
  public void testExternalCheckerMov() throws Exception {
    if (!SystemUtils.IS_OS_WINDOWS) return;
    String pathmov = "src/test/resources/interop/test.mov";

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String configfile = "temp/xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tXML\n");
    bw.close();

    String path = "temp/output";
    idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String[] args = new String[6];
    args[0] = pathmov;
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml = null;
    for (String file : directori.list()) {
      if (file.equals("1-test.mov.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml = new String(encoded);
      }
    }
    assertEquals(xml != null, true);
    assertEquals(xml.contains("mediaconch"), true);
    assertEquals(xml.contains("implementationChecks"), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }

  @Test
  public void testExternalCheckerPdf() throws Exception {
    if (!SystemUtils.IS_OS_WINDOWS) return;
    String pathmov = "src/test/resources/interop/test.pdf";

    if (!new File("temp").exists()) {
      new File("temp").mkdir();
    }

    String configfile = "temp/xx.cfg";
    int idx = 0;
    while (new File(configfile).exists()) configfile = "temp/xx" + idx++ + ".cfg";

    PrintWriter bw = new PrintWriter(configfile);
    bw.write("ISO\tBaseline\n" +
        "FORMAT\tXML\n");
    bw.close();

    String path = "temp/output";
    idx = 1;
    while (new File(path).exists()) path = "temp/output" + idx++;

    String[] args = new String[6];
    args[0] = pathmov;
    args[1] = "-s";
    args[2] = "-o";
    args[3] = path;
    args[4] = "-configuration";
    args[5] = configfile;

    MainConsoleApp.main(args);

    // Wait for finish
    waitForFinishMultiThred(30);

    File directori = new File(path);
    assertEquals(directori.exists(), true);

    String xml = null;
    for (String file : directori.list()) {
      if (file.equals("1-test.pdf.xml")) {
        byte[] encoded = Files.readAllBytes(Paths.get(path + "/" + file));
        xml = new String(encoded);
      }
    }
    assertEquals(xml != null, true);
    assertEquals(xml.contains("validationResult"), true);
    assertEquals(xml.contains("ruleId specification=\"ISO_19005_1\""), true);

    FileUtils.deleteDirectory(new File(path));

    new File(configfile).delete();
    FileUtils.deleteDirectory(new File("temp"));
  }
}
