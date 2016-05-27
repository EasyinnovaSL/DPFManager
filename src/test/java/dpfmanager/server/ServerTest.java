package dpfmanager.server;

import dpfmanager.commandline.CommandLineTest;
import dpfmanager.shell.core.app.MainConsoleApp;

import org.junit.Test;

/**
 * Created by Adri on 27/05/2016.
 */
public class ServerTest extends CommandLineTest {

  @Test
  public void testAddRemoveTag() throws Exception {
    String[] args = new String[3];
    args[0] = "-server";
    args[1] = "-p";
    args[2] = "9000";

    MainConsoleApp.main(args);
  }

}
