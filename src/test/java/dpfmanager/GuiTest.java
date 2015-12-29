package dpfmanager;

import dpfmanager.shell.modules.interfaces.CommandLine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
  public void testAutomaton() throws Exception{
    /*System.out.printf("About to launch FX App\n");
    String[] params = new String[1];
    params[0]="-gui";
    //FXApp.initialize(params);
    FXApp.startApp(new MainApp(),params);


    FXer fxer = FXer.getUserWith(FXApp.getScene().getRoot());

    Thread.sleep(2000);
    fxer.clickOn( "#myButton" ).waitForFxEvents();
    Thread.sleep(2000);*/

  }

  @Test
  public void testGui() throws Exception {
    /*System.out.printf("About to launch FX App\n");
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
      Thread.sleep(20000);*/
  }

  void StartRobot() {
    int initx = 485;
    int inity = 65;
    Thread robot = new Thread("Robot") {
      public void run() {
        try {
          Robot robot = new Robot();
          robot.delay(2000);
          MoveClickAndWait(robot, initx + 587, inity + 20, 1000);
          MoveClickAndWait(robot, initx + 515, inity + 20, 1000);
          MoveClickAndWait(robot, initx + 378, inity + 20, 1000);
          MoveClickAndWait(robot, initx + 500, inity + 825, 1000);
          MoveClickAndWait(robot, initx + 621, inity + 372, 1000);
          MoveClickAndWait(robot, initx + 260, inity + 387, 1000);
          Write(robot);
          MoveClickAndWait(robot, initx + 500, inity + 825, 2000);
        } catch (Exception e) {

        }
      }
    };
    robot.setDaemon(true);
    robot.start();
  }

  void MoveClickAndWait(Robot robot, int posx, int posy, int delay) {
    robot.mouseMove(posx, posy);
    robot.mousePress(InputEvent.BUTTON1_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_MASK );
    robot.delay(delay);
  }

  void alt(Robot bot, int event1, int event2, int event3, int event4) throws Exception {
    bot.keyPress(KeyEvent.VK_ALT);

    bot.keyPress(event1);
    bot.keyRelease(event1);

    bot.keyPress(event2);
    bot.keyRelease(event2);

    bot.keyPress(event3);
    bot.keyRelease(event3);

    bot.keyPress(event4);
    bot.keyRelease(event4);

    bot.keyRelease(KeyEvent.VK_ALT);

  }

  void Write(Robot robot) throws Exception {
    /*for (int i=0;i<15;i++) {
      robot.keyPress(KeyCode.DELETE.impl_getCode());
      robot.delay(100);
    }
    robot.keyPress(KeyCode.D.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyEvent.VK_SHIFT);
    robot.keyPress(KeyEvent.VK_SEMICOLON);
    robot.keyRelease(KeyEvent.VK_SEMICOLON);
    robot.keyRelease(KeyEvent.VK_SHIFT);
    robot.delay(100);
    alt(robot, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD9, KeyEvent.VK_NUMPAD2);
    robot.delay(100);
    robot.keyPress(KeyCode.E.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.S.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.C.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.R.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.I.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.T.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.O.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.R.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.I.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.O.impl_getCode());
    robot.delay(100);
    alt(robot, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD9, KeyEvent.VK_NUMPAD2);
    robot.delay(100);
    robot.keyPress(KeyCode.B.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.I.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.L.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.E.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.V.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.E.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.L.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.STOP.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.T.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.I.impl_getCode());
    robot.delay(100);
    robot.keyPress(KeyCode.F.impl_getCode());
    robot.delay(100);*/
  }
}
