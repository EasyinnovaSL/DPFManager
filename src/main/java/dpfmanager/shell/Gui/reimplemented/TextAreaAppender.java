package dpfmanager.shell.gui.reimplemented;

import org.apache.log4j.WriterAppender;

/**
 * Created by Adrià Llorens on 23/02/2016.
 */
public class TextAreaAppender extends WriterAppender {

  @Override
  public void activateOptions() {
    setWriter(createWriter(new SystemTextAreaStream()));
  }

}