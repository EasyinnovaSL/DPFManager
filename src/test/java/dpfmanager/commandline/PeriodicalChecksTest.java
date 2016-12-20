package dpfmanager.commandline;

import dpfmanager.shell.core.app.MainConsoleApp;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by easy on 13/10/2015.
 */
public class PeriodicalChecksTest extends CommandLineTest {

  private PrintStream old;
  private ByteArrayOutputStream baos;

  @Test
  public void fullTestPeriodicalCheck() throws Exception {
    // Custom system out
    old = System.out;
    baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);

    String[] argsAdd = new String[9];
    argsAdd[0] = "periodic";
    argsAdd[1] = "--add";
    argsAdd[2] = "--periodicity";
    argsAdd[3] = "D";
    argsAdd[4] = "--time";
    argsAdd[5] = "09:30";
    argsAdd[6] = "--configure";
    argsAdd[7] = "src/test/resources/ConfigFiles/SimpleHtml.dpf";
    argsAdd[8] = "src/test/resources/Small/Bilevel.tif";

    String[] argsList = new String[2];
    argsList[0] = "periodic";
    argsList[1] = "-l";

    // Test Add periodical check
    Integer beforeCount = getPeriodicalChecksCount(argsList);
    Integer expectedCount = beforeCount + 1;
    String pcId = getPeriodicalCheckId(argsAdd);
    Assert.assertNotNull(pcId);
    Integer afterCount = getPeriodicalChecksCount(argsList);
    Assert.assertEquals(expectedCount, afterCount);

    // Test remove periodical check
    String[] argsRemove = new String[3];
    argsRemove[0] = "periodic";
    argsRemove[1] = "-r";
    argsRemove[2] = pcId;
    removePeriodicalCheck(argsRemove);
    Integer lastCount = getPeriodicalChecksCount(argsList);
    Assert.assertEquals(beforeCount, lastCount);

    // Put things back
    System.out.flush();
    System.setOut(old);
  }

  private Integer getPeriodicalChecksCount(String[] args) throws Exception {
    String output = getExecutionOutput(args);
    Integer count = 0;
    String[] lines = output.split("\n");
    for (String line : lines) {
      if (line.startsWith("ID:")){
        count++;
      }
    }
    return count;
  }

  private void removePeriodicalCheck(String[] args) throws Exception {
    getExecutionOutput(args);
  }

  private String getPeriodicalCheckId(String[] args) throws Exception {
    String output = getExecutionOutput(args);
    String[] lines = output.split("\n");
    for (String line : lines) {
      if (line.startsWith("ID:")){
        return line.substring(4);
      }
    }
    return null;
  }

  private String getExecutionOutput(String[] args){
    MainConsoleApp.main(args);
    String output = baos.toString();
    baos.reset();
    return output;
  }
}
