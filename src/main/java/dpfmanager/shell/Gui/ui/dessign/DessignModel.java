package dpfmanager.shell.gui.ui.dessign;

import dpfmanager.conformancechecker.tiff.Configuration;
import dpfmanager.conformancechecker.tiff.Field;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.modules.SampleService;

import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.Builders;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrià Llorens on 05/02/2016.
 */
public class DessignModel extends DefaultModel<DessignModel, DessignView> {

  private String txtFile;

  // User Interface
  private ArrayList<String> extensions;
  private ArrayList<String> isos;
  private ArrayList<Field> fields;

  // Check files configuration
  private Configuration config;

  @Override
  protected void initModel() {
    // listen waves
    getService(SampleService.class);
    listen(SampleService.DO_SOMETHING.returnWaveType());

    // init vars
    txtFile = "";
    LoadConformanceChecker();
  }

  public void doSomethingDone(Integer returned, final Wave wave) {
    System.out.println("Returned: " + returned);
  }

  public void callServiceTest() {
    returnData(SampleService.class,
        SampleService.DO_SOMETHING,
        Builders.waveData(SampleService.IN1, "HOL-A"),
        Builders.waveData(SampleService.IN2, "A-DEU")
    );
  }

  public void LoadConformanceChecker() {
    TiffConformanceChecker cc = new TiffConformanceChecker();
    fields = cc.getConformanceCheckerFields();
    extensions = cc.getConformanceCheckerExtensions();
    isos = cc.getConformanceCheckerStandards();

//    extensions = new ArrayList<>();
//    isos = new ArrayList<>();
//    fields = new ArrayList<>();
//
//    String xml = TiffConformanceChecker.getConformanceCheckerOptions();
//    Document doc = convertStringToDocument(xml);
//
//    NodeList nodelist = doc.getElementsByTagName("extension");
//    for (int i=0;i<nodelist.getLength();i++) {
//      Node node = nodelist.item(i);
//      extensions.add(node.getFirstChild().getNodeValue());
//    }
//
//    nodelist = doc.getElementsByTagName("standard");
//    for (int i=0;i<nodelist.getLength();i++) {
//      Node node = nodelist.item(i);
//      isos.add(node.getFirstChild().getNodeValue());
//    }
//
//    nodelist = doc.getElementsByTagName("field");
//    for (int i=0;i<nodelist.getLength();i++) {
//      Node node = nodelist.item(i);
//      NodeList childs = node.getChildNodes();
//      Field field = new Field(childs);
//      fields.add(field);
//    }
  }

  private Document convertStringToDocument(String xmlStr) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
      return doc;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void setTxtFile(String txt) {
    txtFile = txt;
  }

  public String getTxtFile() {
    return txtFile;
  }

  /**
   * Gets extensions.
   *
   * @return the extensions
   */
  public ArrayList<String> getExtensions() {
    return extensions;
  }

  /**
   * Gets fields.
   *
   * @return the fields
   */
  public ArrayList<Field> getFields() {
    return fields;
  }

  /**
   * Gets fixes.
   *
   * @return the fixes
   */
  public ArrayList<String> getFixes() {
    ArrayList<String> fixes = new ArrayList<>();
    fixes.add("Remove Tag");
    fixes.add("Add Tag");
    return fixes;
  }

  /**
   * Gets fix fields.
   *
   * @return the fix fields
   */
  public ArrayList<String> getFixFields() {
    ArrayList<String> fields = new ArrayList<>();
    fields.add("ImageDescription");
    fields.add("Copyright");
    fields.add("Artist");
    return fields;
  }

  public ArrayList<String> getOperators(String name) {
    for (Field field : fields) {
      if (field.getName().equals(name)) {
        return field.getOperators();
      }
    }
    return new ArrayList<>();
  }

  public ArrayList<String> getValues(String name) {
    for (Field field : fields) {
      if (field.getName().equals(name)) {
        return field.getValues();
      }
    }
    return new ArrayList<>();
  }

  public boolean readConfig(String path) {
    try {
      config = new Configuration();
      config.ReadFile(path);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Configuration getConfig() {
    return config;
  }
}
