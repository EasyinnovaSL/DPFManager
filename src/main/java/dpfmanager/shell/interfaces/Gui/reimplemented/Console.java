package dpfmanager.shell.interfaces.Gui.reimplemented;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import dpfmanager.shell.interfaces.DPFManagerProperties;
import javafx.scene.control.TextArea;

/**
 * Created by Adrià Llorens on 17/02/2016.
 */

public class Console extends OutputStream {

  private TextArea output;
  private BufferedWriter outputWriter = null;

  public Console(TextArea ta) {
    this.output = ta;
    createLogFile();
  }

  private void createLogFile() {
    // Create logs folder
    File folder = new File(DPFManagerProperties.getConfigDir() + "/logs");
    if (!folder.exists()) {
      folder.mkdir();
    }

    // Create log file
    try {
      File logFile = new File(folder.getAbsolutePath() + "/dpfmanager.log");
      if (!logFile.exists()) {
        logFile.createNewFile();
      }
      outputWriter = new BufferedWriter(new FileWriter(logFile.getAbsolutePath(), true));
    } catch (IOException e) {
      System.out.println("Cannot create logs file!");
      e.printStackTrace();
    }
  }

  @Override
  public void write(int i) throws IOException {
    output.appendText(String.valueOf((char) i));
    if (outputWriter != null) {
      outputWriter.write(i);
      outputWriter.flush();
    }
  }

}