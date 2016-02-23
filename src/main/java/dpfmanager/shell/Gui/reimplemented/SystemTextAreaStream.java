package dpfmanager.shell.gui.reimplemented;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Adrià Llorens on 23/02/2016.
 */
public class SystemTextAreaStream extends OutputStream {

  private static TextArea textArea = null;

  public SystemTextAreaStream() {
  }

  public static void setTextArea(TextArea ta) {
    textArea = ta;
  }

  public static TextArea getTextArea() {
    return textArea;
  }

  public void close() {
  }

  public void flush() {
  }

  public void write(byte[] bytes) throws IOException {
    write(new String(bytes, StandardCharsets.UTF_8));
  }

  public void write(byte[] bytes, int off, int len) throws IOException {
    String str = new String(bytes, StandardCharsets.UTF_8);
    write(str.substring(off, off + len));
  }

  public void write(int i) throws IOException {
    write(String.valueOf((char) i));
  }

  private void write(String str) {
    if (getTextArea() != null) {
      getTextArea().appendText(str);
    }
  }
}
