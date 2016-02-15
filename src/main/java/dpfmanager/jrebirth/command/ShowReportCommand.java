package dpfmanager.jrebirth.command;

import dpfmanager.jrebirth.ui.main.MainModel;
import dpfmanager.jrebirth.ui.reports.ReportsModel;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.WaveBean;
import org.jrebirth.af.core.command.single.ui.DefaultUIBeanCommand;
import org.jrebirth.af.core.wave.JRebirthItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Adrià Llorens on 03/02/2016.
 */
public final class ShowReportCommand extends DefaultUIBeanCommand<WaveBean> {

  /**
   * The class logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ShowReportCommand.class);

  @Override
  public void initCommand() {
    // You must put your initialization code here (if any)
  }

  @Override
  protected void perform(final Wave wave) {
    // First go to reports page
    getModel(MainModel.class).showReports();

    String data = wave.get(JRebirthItems.stringItem);
    JsonElement rootJson = new JsonParser().parse(data);
    JsonObject rootObject = rootJson.getAsJsonObject();
    String type = rootObject.get("type").getAsString();
    String path = rootObject.get("path").getAsString();

    System.out.println("Showing report...");

    String content = "";
    switch (type) {
      case "xml":
        content = getContent(path);
        getModel(ReportsModel.class).getView().showTextArea(content);
        break;
      case "json":
        content = getContent(path);
        content = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(content));
        getModel(ReportsModel.class).getView().showTextArea(content);
        break;
      case "html":
        getModel(ReportsModel.class).getView().showWebView(path);
        break;
      case "pdf":
        try {
          Desktop.getDesktop().open(new File(path));
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      default:
        break;
    }
  }

  private String getContent(String path){
    String content = "";
    try {
      BufferedReader input = new BufferedReader(new FileReader(path));
      try {
        String line;
        while ((line = input.readLine()) != null) {
          if (!content.equals("")) {
            content += "\n";
          }
          content += line;
        }
      } finally {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return content;
  }

}
