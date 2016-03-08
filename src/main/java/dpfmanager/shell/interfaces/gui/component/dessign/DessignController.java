package dpfmanager.shell.interfaces.gui.component.dessign;

import dpfmanager.conformancechecker.tiff.ProcessInput;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.GuiApp;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ConfigMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.jacp.core.workbench.GuiWorkbench;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class DessignController extends DpfController<DessignModel, DessignView> {

  public void initEventHandlers() {
    initBottomButtons();
    initInfoButtons();

    // Select Input
    getView().getSelectButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String txtFile = null;
        ComboBox c = getView().getComboChoice();
        String configDir = DPFManagerProperties.getConfigDir();
        if (c.getValue() == "File") {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open File");
          fileChooser.setInitialDirectory(new File(configDir));
          List<File> files = fileChooser.showOpenMultipleDialog(GuiApp.getMyStage());
          if (files != null) {
            String sfiles = "";
            for (File file : files) {
              if (sfiles.length() > 0) sfiles += ";";
              sfiles += file.getPath();
            }
            txtFile = sfiles;
            if (new File(sfiles).exists() && new File(sfiles).getParent() != null && new File(new File(sfiles).getParent()).exists() && new File(new File(sfiles).getParent()).isDirectory()) {
              String path = new File(sfiles).getParent();
              DPFManagerProperties.setDefaultDir(path);
            }
          }
        } else {
          DirectoryChooser folderChooser = new DirectoryChooser();
          folderChooser.setTitle("Open Folder");
          folderChooser.setInitialDirectory(new File(configDir));
          File directory = folderChooser.showDialog(GuiApp.getMyStage());
          if (directory != null) {
            txtFile = directory.getPath();
            DPFManagerProperties.setDefaultDir(directory.getPath());
          }
        }
        if (txtFile != null) {
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
          alert.initOwner(GuiApp.getMyStage());
          alert.showAndWait();
          return;
        }
        if (!getModel().readConfig(getFileByPath(radio.getText()).getAbsolutePath())) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Error reading configuration file");
          alert.initOwner(GuiApp.getMyStage());
          alert.showAndWait();
          return;
        } else {
          // Everything OK!
          getView().showLoading();
          Label lblLoading = getView().getLblLoading();

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

                ProcessInput pi = new ProcessInput();
                pi.setLabelLoading(lblLoading);
                ArrayList<String> formats = getModel().getConfig().getFormats();

                String filefolder = pi.ProcessFiles(files, getModel().getConfig(), true);
                if (pi.isOutOfmemory()) {
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
                getContext().send(GuiConfig.PRESPECTIVE_REPORTS, new UiMessage(UiMessage.Type.SHOW, GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.REPORT, type, path)));
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

  private void initBottomButtons() {
    // New config
    getView().getNewButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        getContext().send(GuiConfig.PRESPECTIVE_CONFIG, new UiMessage(UiMessage.Type.SHOW, GuiConfig.COMPONENT_CONFIG, new ConfigMessage(ConfigMessage.Type.NEW)));
//        getModel().getModel(ConfigModel.class).initNewConfig();TODO
//        getModel().getModel(MainModel.class).showConfig();
      }
    });

    // Edit config
    getView().getEditButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (getView().getSelectedConfig() != null) {
          String path = getView().getSelectedConfig().getText();
          getContext().send(GuiConfig.PRESPECTIVE_CONFIG, new UiMessage(UiMessage.Type.SHOW, GuiConfig.COMPONENT_CONFIG, new ConfigMessage(ConfigMessage.Type.EDIT,getFileByPath(path).getAbsolutePath())));
//          getModel().getModel(ConfigModel.class).initEditConfig(getFileByPath(path).getAbsolutePath());TODO
//          getModel().getModel(MainModel.class).showConfig();
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Alert");
          alert.setHeaderText("Please select a configuration file");
          alert.initOwner(GuiApp.getMyStage());
          alert.showAndWait();
        }
      }
    });

    // Import config
    getView().getImportButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        File file;
        String value = GuiWorkbench.getTestParams("import");
        if (value != null) {
          //Test mode
          file = new File(value);
        } else {
          //Ask for file
          String configDir = DPFManagerProperties.getConfigDir();
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Config");
          fileChooser.setInitialDirectory(new File(configDir));
          fileChooser.setInitialFileName("config.dpf");
          file = fileChooser.showOpenDialog(GuiWorkbench.getMyStage());
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
              alert2.initOwner(GuiApp.getMyStage());
              alert2.showAndWait();
            }
          }
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Alert");
          alert.setHeaderText("Please select a configuration file");
          alert.initOwner(GuiApp.getMyStage());
          alert.showAndWait();
        }
      }
    });
  }

  private void initInfoButtons() {
    // File info
    getView().getFileInfoBut().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("The path to the files to check");
        alert.setContentText("This can be either a single file or a folder. Only the files with a valid TIF file extension will be processed.");
        alert.initOwner(GuiApp.getMyStage());
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
        alert.initOwner(GuiApp.getMyStage());
        alert.showAndWait();
      }
    });
  }

  private File getFileByPath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      String configDir = DPFManagerProperties.getConfigDir();
      file = new File(configDir + "/" + path);
    }
    return file;
  }
}
