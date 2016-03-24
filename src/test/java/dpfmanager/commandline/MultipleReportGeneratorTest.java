package dpfmanager.commandline;

import dpfmanager.shell.interfaces.console.commandline.CommandLineApp;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import javafx.application.Application;

import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Before;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Easy on 20/07/2015.
 */
public class MultipleReportGeneratorTest extends TestCase {
  TiffReader tr;

  /**
   * Pre test.
   */
  @Before
  public void PreTest() {
    DPFManagerProperties.setFeedback(false);

    boolean ok = true;
    try {
      tr = new TiffReader();
    } catch (Exception e) {
      ok = false;
    }
    assertEquals(ok, true);
  }

  public void testReportsXML() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "src/test/resources/Small/";
    args[1] = "-reportformat";
    args[2] = "xml";

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLineApp cl = new CommandLineApp(params);
    cl.launch();

    String path = getPath();
    File directori = new File(path);
    assertEquals(7, directori.list().length);
  }

  public void testReportsKoPdf() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "src/test/resources/Block/Bad alignment Big E.tif";
    args[1] = "-reportformat";
    args[2] = "pdf";

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLineApp cl = new CommandLineApp(params);
    cl.launch();

    String path = getPath();
    File directori = new File(path);
    assertEquals(2, directori.list().length);

    PDDocument doc = PDDocument.load(path + "/report.pdf");
    List<PDPage> l = doc.getDocumentCatalog().getAllPages();
    assertEquals(2, l.size());
    doc.close();
  }

  public void testReportsPDF() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[3];
    args[0] = "src/test/resources/Small/";
    args[1] = "-reportformat";
    args[2] = "pdf";

    Application.Parameters params = new Application.Parameters() {
      @Override
      public List<String> getRaw() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public List<String> getUnnamed() {
        ArrayList<String> listRaw = new ArrayList<String>();
        listRaw.add(args[0]);
        listRaw.add(args[1]);
        listRaw.add(args[2]);
        return listRaw;
      }

      @Override
      public Map<String, String> getNamed() {
        return null;
      }
    };

    CommandLineApp cl = new CommandLineApp(params);
    cl.launch();

    String path = getPath();
    File directori = new File(path);
    assertEquals(7, directori.list().length);

    PDDocument doc = PDDocument.load(path + "/report.pdf");
    List<PDPage> l = doc.getDocumentCatalog().getAllPages();
    assertEquals(13, l.size());
    doc.close();
  }

  private String getPath() {
    String path = ReportGenerator.createReportPath(true);
    return path;
  }
}
