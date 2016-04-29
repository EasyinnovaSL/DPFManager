package dpfmanager.shell.interfaces.gui.component.show;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Adrià Llorens on 17/03/2016.
 */
public class ShowController extends DpfController<ShowModel, ShowView> {

  public ShowController(){

  }

  public void showSingleReport(String type, String path) {
//    getContext().send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Showing report..."));
    switch (type) {
      case "html":
        getView().showWebView(path);
        break;
      case "xml":
        getView().showTextArea(getContent(path));
        break;
      case "json":
        String content = getContent(path);
        content = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(content));
        getView().showTextArea(content);
        break;
      case "pdf":
        try {
          getView().getContext().send(GuiConfig.PERSPECTIVE_REPORTS,new UiMessage());
          Desktop.getDesktop().open(new File(path));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      default:
        break;
    }
  }

  private String getContent(String path) {
    String content = "";
    try {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return content;
  }

}
