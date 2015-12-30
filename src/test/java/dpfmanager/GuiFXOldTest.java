//package dpfmanager;
//
//
//import org.loadui.testfx.controls.Commons;
//
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
//  private static Scene scene;
//
//  @BeforeClass
//  public static void setUpClass() throws InterruptedException, IOException {
//    FXTestUtils.launchApp(MainApp.class);
//    Thread.sleep(1000);
//    controller = new GuiTest() {
//      @Override
//      protected Parent getRootNode() {
//        return MainApp.getStage().getScene().getRoot();
//      }
//    };
//    scene = MainApp.getStage().getScene();
//  }
//
//  @Test
//  public void testFirstScreen() throws Exception {
//    //Actions
//    Thread.sleep(500);
//
//    //Check
//    Assertions.verifyThat("#txtBox1", Commons.hasText("Select a file"));
//  }
//
//  @Test
//  public void testAbout() throws Exception {
//    //Actions
//    Node button = controller.find("#butAbout");
//    controller.click(button);
//    Thread.sleep(500);
//
//    //Check
//    Assertions.verifyThat("#aboutTitle", Commons.hasText("About DPF Manager"));
//  }
//
//  @Test
//  public void testFail() throws Exception {
//    //Actions
//    Node button = controller.find("#butReport");
//    controller.click(button);
//    Thread.sleep(500);
//
//    //Check
//    Assertions.verifyThat("#txtBox1", Commons.hasText("Select a file"));
//  }
//}
