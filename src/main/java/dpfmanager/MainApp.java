package dpfmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * The Class MainApp.
 */
public class MainApp extends Application {
    
    /*
     * Javadoc asdf asd
     * 
     *  asdfasdf
     * asdf asd fasd fa
     */
    private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws Exception the exception
   */
    public static void main(final String[] args) throws Exception {
	launch(args);
    }

    /*
     * Javadoc asdf asd
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage) asdfasdf
     * asdf asd fasd fa
     * 
     */
   
    public final void start(final Stage stage) throws Exception {

	LOG.info("Starting Hello JavaFX and Maven demonstration application");

	String fxmlFile = "/fxml/hellov.fxml";
	LOG.debug("Loading FXML for main view from: {}", fxmlFile);
	FXMLLoader loader = new FXMLLoader();
	Parent rootNode =
		(Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

	LOG.debug("Showing JFX scene");
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	
	final int width = 1439;
	final int height = gd.getDisplayMode().getHeight()-75;
	
	Scene scene = new Scene(rootNode, width, height);
	scene.getStylesheets().add("/styles/stylesv.css");
		
	stage.setTitle("Hello JavaFX and Maven");
	stage.setScene(scene);
	stage.show();
    }
}
