package dpfmanager.shell.interfaces.Gui.reimplemented;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

/**
 * Created by Adrià Llorens on 17/02/2016.
 */

public class Console extends OutputStream {
  private TextArea output;

  public Console(TextArea ta) {
    this.output = ta;
  }

  @Override
  public void write(int i) throws IOException {
    output.appendText(String.valueOf((char) i));
  }

}