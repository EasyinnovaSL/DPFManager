package dpfmanager;

import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by easy on 05/10/2015.
 */
public class GuiTest extends TestCase {

  @Test
  public void testGui() throws Exception {
    System.out.printf("About to launch FX App\n");
    boolean robotize = false;

    if (robotize)
      StartRobot();

    Thread t = new Thread("JavaFX Init Thread") {
      public void run() {
        if (!robotize)
          Application.launch(MainApp.AsNonApp.class, new String[0]);
        else
          Application.launch(MainApp.class, new String[0]);
      }
    };
    t.setDaemon(true);
    t.start();
    System.out.printf("FX App thread started\n");
    if (robotize)
      Thread.sleep(20000);
  }

  void StartRobot() {
    Thread robot = new Thread("Robot") {
      public void run() {
        try {
          Robot robot = new Robot();
          robot.delay(2000);
          robot.mouseMove(1000, 85);
          robot.mousePress(InputEvent.BUTTON1_MASK);
          robot.mouseRelease(InputEvent.BUTTON1_MASK );
          robot.mouseMove(0, 0);
        } catch (Exception e) {

        }
      }
    };
    robot.setDaemon(true);
    robot.start();
  }
}
