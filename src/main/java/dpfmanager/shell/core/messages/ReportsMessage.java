package dpfmanager.shell.core.messages;

import javafx.scene.control.ScrollPane;

/**
 * Created by Adrià Llorens on 04/03/2016.
 */
public class ReportsMessage extends DpfMessage {
  public enum Type {
    TABLE,
    REPORT,
    READ,
    SCROLL
  }

  private Type type;
  private String rType;
  private String path;
  private ScrollPane scrollPane;

  public ReportsMessage(Type t){
    type = t;
  }

  public ReportsMessage(Type t, String reportType, String reportPath){
    type = t;
    rType = reportType;
    path = reportPath;
  }

  public ReportsMessage(Type t, ScrollPane scroll){
    type = t;
    scrollPane = scroll;
  }

  public ScrollPane getScrollPane(){
    return scrollPane;
  }

  public Type getType(){
    return type;
  }

  public String getPath() {
    return path;
  }

  public String getReportType() {
    return rType;
  }

  public boolean isTable(){
    return type.equals(Type.TABLE);
  }

  public boolean isReport(){
    return type.equals(Type.REPORT);
  }

  public boolean isRead(){
    return type.equals(Type.READ);
  }

  public boolean isScroll(){
    return type.equals(Type.SCROLL);
  }

}