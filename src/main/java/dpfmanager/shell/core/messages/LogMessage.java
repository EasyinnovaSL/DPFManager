package dpfmanager.shell.core.messages;

import javafx.scene.control.TextArea;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 21/03/2016.
 */
public class LogMessage extends DpfMessage {

  private String message;
  private Level level;
  private Class clas;
  private TextArea textArea;

  public LogMessage(Class c, Level l, String m) {
    message = m;
    level = l;
    clas = c;
    textArea = null;
  }

  public LogMessage(TextArea ta) {
    textArea = ta;
  }

  public String getMessage() {
    return message;
  }

  public Level getLevel() {
    return level;
  }

  public Class getMyClass() {
    return clas;
  }

  public TextArea getTextArea() {
    return textArea;
  }

  public boolean hasTextArea() {
    return textArea != null;
  }
}
