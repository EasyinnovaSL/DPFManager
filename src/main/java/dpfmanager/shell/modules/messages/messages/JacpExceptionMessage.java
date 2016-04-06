package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 06/04/2016.
 */
public class JacpExceptionMessage extends DpfMessage {

  private String title;
  private String header;
  private String content;
  private Throwable throwable;

  public JacpExceptionMessage(Throwable t){
    title = "Gui Exception";
    header = "A gui error has occurred!";
    content = t.getMessage();
    throwable = t;
  }

  public String getTitle(){
    return title;
  }

  public String getHeader(){
    return header;
  }

  public String getContent(){
    return content;
  }

  public Throwable getThrowable(){
    return throwable;
  }
}
