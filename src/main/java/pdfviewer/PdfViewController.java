/*
 * Copyright (c) TAKAHASHI,Toru 2015
 */
package pdfviewer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 *
 * @author toru
 */
public class PdfViewController implements Initializable {
    
    @FXML
    private Pagination pagination;
    
    private PdfModel model;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new PdfModel(Paths.get("D:/report.pdf"));
        pagination.setPageCount(model.numPages());
        pagination.setPageFactory(index -> new ImageView(model.getImage(index)));
        
    }    
    
}
