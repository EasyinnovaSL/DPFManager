/*
 * Copyright (c) TAKAHASHI,Toru 2015
 */
package pdfviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * PDFæ–‡æ›¸ã?®ãƒšãƒ¼ã‚¸ã‚’åˆ†å‰²ã?—ã?Ÿã‚Šã€?çµ?å?ˆã?—ã?Ÿã‚Šã?¨ã?„ã?£ã?Ÿç°¡æ˜“ã?ªæ“?ä½œã‚’è¡Œã?†ãƒ„ãƒ¼ãƒ«ã€‚
 * 
 * @author toru
 */
public class PdfViewer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/PdfView.fxml"));
//        Parent content = loader.load();

        Parent root = FXMLLoader.load(getClass().getResource("/PdfView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("PDF simple viewer by PDFBox");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
