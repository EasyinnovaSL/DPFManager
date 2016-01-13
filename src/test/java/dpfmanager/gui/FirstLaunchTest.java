//package dpfmanager.gui;
//
//import dpfmanager.MainApp;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.stage.Stage;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.testfx.api.FxAssert;
//import org.testfx.api.FxToolkit;
//import org.testfx.matcher.base.NodeMatchers;
//import org.testfx.util.WaitForAsyncUtils;
//
///**
// * Created by Adrià Llorens on 30/12/2015.
// */
//public class FirstLaunchTest extends ApplicationTest {
//
//  Stage stage = null;
//
//  @Override
//  public void init() throws Exception{
//    System.out.println("Init");
//    stage = launch(MainApp.class);
//    scene = stage.getScene();
//  }
//
//  @Test
//  public void testFirstScreen() throws Exception {
//    WaitForAsyncUtils.waitForFxEvents();
//    FxAssert.verifyThat("#welcomeText", NodeMatchers.hasText("Welcome to DPF Manager!"));
//  }
//
//}
