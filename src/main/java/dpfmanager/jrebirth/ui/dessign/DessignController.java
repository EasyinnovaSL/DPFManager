package dpfmanager.jrebirth.ui.dessign;

import dpfmanager.RebirthApp;
import dpfmanager.jrebirth.ui.config.ConfigModel;
import dpfmanager.jrebirth.ui.main.MainModel;
import dpfmanager.jrebirth.ui.report.ReportsModel;
import dpfmanager.shell.modules.classes.ProcessInput;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Adrià Llorens on 05/02/2016.
 */
public class DessignController extends DefaultController<DessignModel, DessignView> {

  public DessignController(final DessignView view) throws CoreException {
    super(view);
  }

  @Override
  protected void initEventHandlers() throws CoreException {
    initBottomButtons();
    initInfoButtons();

    // Select Input
    getView().getSelectButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String txtFile = null;
        ComboBox c = getView().getComboInput();
        String configDir = getModel().getModel(MainModel.class).getConfigDir();
        if (c.getValue() == "File") {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open File");
          fileChooser.setInitialDirectory(new File(configDir));
          List<File> files = fileChooser.showOpenMultipleDialog(RebirthApp.getMyStage());
          if (files != null) {
            String sfiles = "";
            for (File file : files) {
              if (sfiles.length() > 0) sfiles += ";";
              sfiles += file.getPath();
            }
            txtFile = sfiles;
            if (new File(sfiles).exists() && new File(sfiles).getParent() != null && new File(new File(sfiles).getParent()).exists() && new File(new File(sfiles).getParent()).isDirectory()) {
              getModel().getModel(MainModel.class).setConfigDir(new File(sfiles).getParent());
            }
          }
        } else {
          DirectoryChooser folderChooser = new DirectoryChooser();
          folderChooser.setTitle("Open Folder");
          folderChooser.setInitialDirectory(new File(configDir));
          File directory = folderChooser.showDialog(RebirthApp.getMyStage());
          if (directory != null) {
            txtFile = directory.getPath();
            getModel().getModel(MainModel.class).setConfigDir(directory.getPath());
          }
        }
        if (txtFile != null){
          getView().getInputText().setText(txtFile);
        }
      }
    });

    // Check Files
    getView().getCheckFilesButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ArrayList<String> files = new ArrayList<>();
        ArrayList<String> extensions = getModel().getExtensions();

        if (getView().getInputText().getText().equals("Select a file")) {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Alert");
          alert.setHeaderText("Please select a file");
          alert.showAndWait();
          return;
        }

        RadioButton radio = getView().getSelectedConfig();
        if (radio == null) {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Alert");
          alert.setHeaderText("Please select a configuration file");
          alert.initOwner(RebirthApp.getMyStage());
          alert.showAndWait();
          return;
        }
        if (!getModel().readConfig(getFileByPath(radio.getText()).getAbsolutePath())) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Error reading configuration file");
          alert.initOwner(RebirthApp.getMyStage());
          alert.showAndWait();
          return;
        } else {
          // Everything OK!
          getView().showLoading();
          Label lblLoading = getView().getLabelLoading();

          // Create a background task, because otherwise the loading message is not shown
          Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
              try {
                TextField txtFile = getView().getInputText();
                if (new File(txtFile.getText()).isDirectory()) {
                  File[] listOfFiles = new File(txtFile.getText()).listFiles();
                  for (int j = 0; j < listOfFiles.length; j++) {
                    if (listOfFiles[j].isFile()) {
                      files.add(listOfFiles[j].getPath());
                    }
                  }
                } else {
                  for (String sfile : txtFile.getText().split(";")) {
                    files.add(sfile);
                  }
                }

                ProcessInput pi = new ProcessInput(extensions);
                pi.setLabelLoading(lblLoading);
                ArrayList<String> formats = getModel().getConfig().getFormats();

                String filefolder = pi.ProcessFiles(files, getModel().getConfig(), true);
                if (pi.outOfmemory) {
                  Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occured");
                    alert.setContentText("Out of memory");
                    alert.showAndWait();
                  });
                }

                // When finish, show report
                String type = "";
                String path = "";
                if (formats.contains("HTML")) {
                  type = "html";
                  path = filefolder + "report.html";
                } else if (formats.contains("XML")) {
                  type = "xml";
                  path = filefolder + "summary.xml";
                } else if (formats.contains("JSON")) {
                  type = "json";
                  path = filefolder + "summary.json";
                } else if (formats.contains("PDF")) {
                  type = "pdf";
                  path = filefolder + "report.pdf";
                }

                // Show reports
                getView().hideLoading();
                getModel().getModel(ReportsModel.class).setFinishReport(type, path);
                getModel().getModel(MainModel.class).showReports();
              } catch (Exception ex) {
                Platform.runLater(() -> {
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("An error occured");
                  alert.setContentText(ex.toString());
                  alert.showAndWait();
                });
              } catch (OutOfMemoryError er) {
                Platform.runLater(() -> {
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  alert.setHeaderText("An error occured");
                  alert.setContentText("Out of memory");
                  alert.showAndWait();
                });
              }
              return 0;
            }
          };

          //start the background task
          Thread th = new Thread(task);
          th.setDaemon(true);
          th.start();
        }
      }
    });
  }

  public void initComboBox(){
    // Combo box
    getView().getChoiceType().valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue ov, String oldValue, String newValue) {
        getView().getSelectButton().fire();
      }
    });
  }

  private void initBottomButtons(){
    // New config
    getView().getNewButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        getModel().getModel(ConfigModel.class).initNewConfig();
        getModel().getModel(MainModel.class).showConfig();
      }
    });

    // Edit config
    getView().getEditButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (getView().getSelectedConfig() != null) {
          String path = getView().getSelectedConfig().getText();
          getModel().getModel(ConfigModel.class).initEditConfig(getFileByPath(path).getAbsolutePath());
          getModel().getModel(MainModel.class).showConfig();
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Alert");
          alert.setHeaderText("Please select a configuration file");
          alert.initOwner(RebirthApp.getMyStage());
          alert.showAndWait();
        }
      }
    });

    // Import config
    getView().getImportButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        File file;
        String value = MainModel.getTestParams("import");
        if (value != null) {
          //Test mode
          file = new File(value);
        } else {
          //Ask for file
          String configDir = getModel().getModel(MainModel.class).getConfigDir();
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Config");
          fileChooser.setInitialDirectory(new File(configDir));
          fileChooser.setInitialFileName("config.dpf");
          file = fileChooser.showOpenDialog(RebirthApp.getMyStage());
        }

        if (file != null) {
          getView().addConfigFile(file.getAbsolutePath());
        }
      }
    });

    // Delete config
    getView().getDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        RadioButton radio = getView().getSelectedConfig();
        if (radio != null) {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Delete configuration file");
          alert.setHeaderText("Are you sure to delete the configuration file '" + radio.getText() + "'?");
          alert.setContentText("The physical file in disk will be also removed");
          ButtonType buttonNo = new ButtonType("No");
          ButtonType buttonYes = new ButtonType("Yes");
          alert.getButtonTypes().setAll(buttonNo, buttonYes);
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == buttonYes) {
            File file = getFileByPath(radio.getText());
            if (file.delete()) {
              getView().deleteSelectedConfig();
            } else {
              Alert alert2 = new Alert(Alert.AlertType.ERROR);
              alert2.setTitle("Error");
              alert2.setHeaderText("There was an error deleting the configuration file");
              alert2.initOwner(RebirthApp.getMyStage());
              alert2.showAndWait();
            }
          }
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Alert");
          alert.setHeaderText("Please select a configuration file");
          alert.initOwner(RebirthApp.getMyStage());
          alert.showAndWait();
        }
      }
    });
  }

  private void initInfoButtons(){
    // File info
    getView().getFileInfoBut().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("The path to the files to check");
        alert.setContentText("This can be either a single file or a folder. Only the files with a valid TIF file extension will be processed.");
        alert.initOwner(RebirthApp.getMyStage());
        alert.showAndWait();
      }
    });

    // Config info
    getView().getConfigInfoBut().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Configuration files define the options to check the files (ISO, report format and policy rules)");
        alert.setContentText("You can either create a new configuration file, import a new one from disk, or edit/delete one from the list");
        alert.initOwner(RebirthApp.getMyStage());
        alert.showAndWait();
      }
    });
  }

  public File getFileByPath(String path){
    File file = new File(path);
    if (!file.exists()) {
      String configDir = getModel().getModel(MainModel.class).getConfigDir();
      file = new File(configDir + "/" + path);
    }
    return file;
  }

  @Override
  protected void initEventAdapters() throws CoreException {
  }

}