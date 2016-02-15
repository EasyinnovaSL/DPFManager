package dpfmanager.jrebirth.ui.about;

import org.jrebirth.af.core.ui.DefaultModel;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public class AboutModel extends DefaultModel<AboutModel, AboutView> {

  @Override
  protected void initModel() {
    super.initModel();
    // Nothing to do yet
  }

  public String getVersion() {
    //Properties prop = new Properties();
    //InputStream input = null;

    //try {
    //String ver = DPFManagerProperties.class.getPackage().getImplementationVersion();

    //String filename = ".properties";
    //input = MainApp.class.getClassLoader().getResourceAsStream(filename);
    //if(input==null){
    //  return "";
    //}

    // load a properties file
    //prop.load(input);

    // get the property value and print it out
    //return prop.getProperty("version");
    //} catch (IOException ex) {
    //  ex.printStackTrace();
    //} finally {
    //  if (input != null) {
    //    try {
    //      input.close();
    //    } catch (IOException e) {
    //      e.printStackTrace();
    //    }
    //  }
    //}
    return "1.4";
  }

}
