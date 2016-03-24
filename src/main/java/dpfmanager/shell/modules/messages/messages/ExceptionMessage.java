package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 24/03/2016.
 */
public class ExceptionMessage extends DpfMessage {

  private String title;
  private String header;
  private String content;
  private Exception exception;

  public ExceptionMessage(String h, Exception e){
    title = "Exception";
    header = h;
    content = e.getMessage();
    exception = e;
  }

  public ExceptionMessage(String t, String h, Exception e){
    title = t;
    header = h;
    content = e.getMessage();
    exception = e;
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

  public Exception getException(){
    return exception;
  }
}
