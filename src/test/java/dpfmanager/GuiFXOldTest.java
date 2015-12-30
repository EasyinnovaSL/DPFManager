//package dpfmanager;
//
//
//import org.junit.AfterClass;
//import org.loadui.testfx.controls.Commons;
//
//import javafx.application.Platform;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//
//import com.athaydes.automaton.FXApp;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.loadui.testfx.Assertions;
//import org.loadui.testfx.controls.Commons;
//import org.loadui.testfx.GuiTest;
//import org.loadui.testfx.utils.FXTestUtils;
//
//import java.awt.*;
//import java.io.IOException;
//
///**
// * Created by Adria Llorens on 05/10/2015.
// */
//public class GuiFXOldTest {
//
//  //Set properties for headless mode
//  static {
////    System.setProperty("testfx.robot", "glass");
////    System.setProperty("glass.platform", "Monocle");
////    System.setProperty("monocle.platform", "Headless");
//
////    System.setProperty("java.awt.headless", "true");
////    System.setProperty("testfx.robot", "glass");
////    System.setProperty("testfx.headless", "true");
////    System.setProperty("prism.order", "sw");
////    System.setProperty("prism.text", "t2k");
//  }
//
//  public static GuiTest controller;
//
//  @BeforeClass
//  public static void setUpClass() throws Exception {
//    FXTestUtils.launchApp(MainApp.class);
//    Thread.sleep(5000);
//    controller = new GuiTest() {
//      @Override
//      protected Parent getRootNode() {
//        return MainApp.getStage().getScene().getRoot();
//      }
//    };
//  }
//
//  @AfterClass
//  public static void shutdownAll() throws InterruptedException {
//    Platform.runLater(() -> MainApp.getStage().close());
//    Platform.exit();
//  }
//
//  @Test
//  public void testFirstScreen() throws Exception {
//    //Wait
//    FXTestUtils.awaitEvents();
//
//    //Check
//    Assertions.verifyThat("#txtBox1", Commons.hasText("Select a file"));
//  }
//
//  @Test
//  public void testAbout() throws Exception {
//    //Wait
//    FXTestUtils.awaitEvents();
//
//    //Actions
//    controller.click("#butAbout");
//
//    //Check
//    Assertions.verifyThat("#aboutTitle", Commons.hasText("About DPF Manager"));
//  }
//
//  @Test
//  public void testFail() throws Exception {
//    //Wait
//    FXTestUtils.awaitEvents();
//
//    //Actions
//    controller.click("#butReport");
//
//    //Check
//    Assertions.verifyThat("#txtBox1", Commons.hasText("Select a file"));
//  }
//}
