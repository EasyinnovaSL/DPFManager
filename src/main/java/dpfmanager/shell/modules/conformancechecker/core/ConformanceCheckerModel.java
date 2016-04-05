package dpfmanager.shell.modules.conformancechecker.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.interfaces.gui.component.dessign.DessignController;
import dpfmanager.shell.interfaces.gui.component.dessign.DessignView;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class ConformanceCheckerModel extends DpfModel<DessignView, DessignController> {
  private String txtFile;

  // User Interface
  private ArrayList<String> extensions;
  private ArrayList<String> isos;
  private ArrayList<Field> fields;

  // Check files configuration
  private Configuration config;
  private TiffConformanceChecker conformance;

  public ConformanceCheckerModel() {
    // init vars
    txtFile = "";
    LoadConformanceChecker();
  }

  public void LoadConformanceChecker() {
    conformance = new TiffConformanceChecker();
    fields = conformance.getConformanceCheckerFields();
    extensions = conformance.getConformanceCheckerExtensions();
    isos = conformance.getConformanceCheckerStandards();
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
