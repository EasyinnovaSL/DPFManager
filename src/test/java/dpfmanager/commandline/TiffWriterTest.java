package dpfmanager.commandline;

import static java.io.File.separator;
import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import com.easyinnova.tiff.io.TiffInputStream;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.reader.TiffReader;
import com.easyinnova.tiff.writer.TiffWriter;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Easy on 20/07/2015.
 */
public class TiffWriterTest extends CommandLineTest {
  @Test
  public void testReports1() throws Exception {
    DPFManagerProperties.setFeedback(false);

    String[] args = new String[2];
    args[0] = "src/test/resources/TestWriter";
    args[1] = "-s";

    TiffReader tr = new TiffReader();
    tr.readFile("src" + separator + "test" + separator + "resources" + separator + "TestWriter" + separator + "Bilevel1.tif");
    TiffInputStream ti = new TiffInputStream(new File("src" + separator + "test" + separator + "resources" + separator + "TestWriter" + separator + "Bilevel1.tif"));
    TiffDocument td = tr.getModel();

    TiffWriter tw = new TiffWriter(ti);
    tw.SetModel(td);
    tw.write("src" + separator + "test" + separator + "resources" + separator + "TestWriter" + separator + "Bilevel2.tif");

    MainConsoleApp.main(args);

    String path = getPath();
    String xmlFile = path + "/1-Bilevel1.tif.xml";
    String xmlFile2 = path + "/2-Bilevel2.tif.xml";
    if (!new File(xmlFile).exists()) xmlFile = path + "/2-Bilevel1.tif.xml";
    if (!new File(xmlFile2).exists()) xmlFile2 = path + "/1-Bilevel2.tif.xml";

    assertXML(xmlFile, xmlFile2);
  }

  private void assertXML(String xmlFile, String xmlFile2) throws Exception {
    File fXmlFile = new File(xmlFile);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(fXmlFile);
    doc.getDocumentElement().normalize();

    File fXmlFil2e = new File(xmlFile2);
    DocumentBuilderFactory dbFactory2 = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder2 = dbFactory.newDocumentBuilder();
    Document doc2 = dBuilder.parse(fXmlFil2e);
    doc2.getDocumentElement().normalize();


    Node node = doc.getFirstChild().getFirstChild();
    Node node2 = doc2.getFirstChild().getFirstChild();

    while (node.getNextSibling() != null && node2.getNextSibling() != null) {

      assertEquals(node.getNodeName(), node2.getNodeName());
      assertEquals(node.getNodeValue(), node2.getNodeValue());
      if (node.getFirstChild() != null && node2.getFirstChild() != null) {
        getChilds(node.getFirstChild(), node2.getFirstChild());
      }
      node = node.getNextSibling();
      node2 = node2.getNextSibling();
    }
  }

  public void getChilds(Node node, Node node2) {
    while (node.getNextSibling() != null && node2.getNextSibling() != null) {
      assertEquals(node.getNodeName(), node2.getNodeName());
      assertEquals(node.getNodeValue(), node2.getNodeValue());
      if (node.getFirstChild() != null && node2.getFirstChild() != null) {
        getChilds(node.getFirstChild(), node2.getFirstChild());
      }
      node = node.getNextSibling();
      node2 = node2.getNextSibling();
    }
  }

  private String getPath() {
    String path = ReportGenerator.createReportPath(true);
    return path;
  }
}
