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

  /**
   * Gets fields.
   *
   * @return the fields
   */
  public ArrayList<Field> getFields() {
    return fields;
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

  public void setConfig(Configuration c){
    config = c;
  }

  public Configuration getConfig() {
    return config;
  }
}
