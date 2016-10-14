package dpfmanager.commandline;

import static junit.framework.TestCase.assertEquals;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.app.MainConsoleApp;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by easy on 08/02/2016.
 */
public class ConformanceCheckersManagerTest extends CommandLineTest {

  @Test
  public void testBasicOptions() throws Exception {
    String[] args;
    String expectedStr, currentStr;
    File expected, current;

    /*
     * Add
     */
    args = new String[10];
    args[0] = "modules";
    args[1] = "--add";
    args[2] = "RandomName456";
    args[3] = "src/test/resources/Small/Bilevel.tif"; // Simulate executable
    args[4] = "--configure";
    args[5] = "src/test/resources/ConfigFiles/SimpleHtml.dpf";
    args[6] = "--parameters";
    args[7] = "-configuration %config% %input%";
    args[8] = "--extensions";
    args[9] = "tif,tiff";
    MainConsoleApp.main(args);

    /*
     * Edit
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "--edit";
    args[2] = "RandomName456";
    args[3] = "src/test/resources/Small/Grey.tif"; // Simulate executable
    MainConsoleApp.main(args);

    /*
     * Disable built-in
     */
    args = new String[3];
    args[0] = "modules";
    args[1] = "--disable";
    args[2] = "Tiff";
    MainConsoleApp.main(args);

    /*
     * Compare Step 1
     */
    expected = new File("src/test/resources/ConformanceConfiguration/step1.xml");
    current = new File(DPFManagerProperties.getConformancesConfig());
    compareFiles(expected, current, 1);

    /*
     * Remove
     */
    args = new String[3];
    args[0] = "modules";
    args[1] = "--remove";
    args[2] = "RandomName456";
    MainConsoleApp.main(args);

    /*
     * Add
     */
    args = new String[10];
    args[0] = "modules";
    args[1] = "--add";
    args[2] = "RandomName789";
    args[3] = "src/test/resources/Small/RGB.tif"; // Simulate executable
    args[4] = "--configure";
    args[5] = "src/test/resources/ConfigFiles/SimpleXml.dpf";
    args[6] = "--parameters";
    args[7] = "-configuration %config% %input%";
    args[8] = "--extensions";
    args[9] = "tif";
    MainConsoleApp.main(args);

    /*
     * Configure
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "--configure";
    args[2] = "RandomName789";
    args[3] = "src/test/resources/ConfigFiles/SimpleJson.dpf";
    MainConsoleApp.main(args);

    /*
     * Parameters
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "--parameters";
    args[2] = "RandomName789";
    args[3] = "%input% -configuration %config%";
    MainConsoleApp.main(args);

    /*
     * Bad Parameters
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "--parameters";
    args[2] = "RandomName789";
    args[3] = "%inpu% -configuration %config%";
    MainConsoleApp.main(args);

    /*
     * Extensions
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "--extensions";
    args[2] = "RandomName789";
    args[3] = "jpg,gif";
    MainConsoleApp.main(args);

    /*
     * Compare Step 2
     */
    expected = new File("src/test/resources/ConformanceConfiguration/step2.xml");
    current = new File(DPFManagerProperties.getConformancesConfig());
    compareFiles(expected, current, 1);

    /*
     * Bad try remove built-in
     */
    args = new String[3];
    args[0] = "modules";
    args[1] = "-r";
    args[2] = "Tiff";
    MainConsoleApp.main(args);

    /*
     * Bad try edit built-in
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "-e";
    args[2] = "Tiff";
    args[3] = "src/test/resources/Small/RGB.tif"; // Simulate executable
    MainConsoleApp.main(args);

    /*
     * Bad try extensions built-in
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "--extensions";
    args[2] = "Tiff";
    args[3] = "pdf";
    MainConsoleApp.main(args);

    /*
     * Configure built-in
     */
    args = new String[4];
    args[0] = "modules";
    args[1] = "--configure";
    args[2] = "Tiff";
    args[3] = "src/test/resources/ConfigFiles/config.dpf";
    MainConsoleApp.main(args);

    /*
     * Enable built-in
     */
    args = new String[3];
    args[0] = "modules";
    args[1] = "--enable";
    args[2] = "Tiff";
    MainConsoleApp.main(args);

    /*
     * Compare Step 3
     */
    expected = new File("src/test/resources/ConformanceConfiguration/step3.xml");
    current = new File(DPFManagerProperties.getConformancesConfig());
    compareFiles(expected, current, 1);
  }

  private void compareFiles(File expected, File current, int maxDifferLines) throws IOException {
    List<String> expectedLines = FileUtils.readLines(expected);
    List<String> currentLines = FileUtils.readLines(current);
    if (expectedLines.size() == currentLines.size()) {
      int count = 0;
      for (int i = 0; i< expectedLines.size(); i++){
        String expectedLine = expectedLines.get(i);
        String currentLine = expectedLines.get(i);
        if (!expectedLine.equals(currentLine)){
          count++;
        }
        if (count > maxDifferLines){
          Assert.assertEquals("Config files differ!", true, false);
        }
      }
    } else {
      Assert.assertEquals("Config files differ!", true, false);
    }
  }

}
