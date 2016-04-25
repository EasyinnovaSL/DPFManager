package dpfmanager.shell.modules.database.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
public class DatabaseMessage extends DpfMessage {

  public enum Type {
    NEW, UPDATE, FINISH, GET
  }

  private Type type;
  private Long uuid;
  private int total;
  private String input;
  private String output;

  public DatabaseMessage(Type type, Long uuid, int total, String input, String output) {
    // New
    this.type = type;
    this.uuid = uuid;
    this.total = total;
    this.input = input;
    this.output = output;
  }

  public DatabaseMessage(Type type, Long uuid) {
    // Update && Finish
    this.type = type;
    this.uuid = uuid;
  }

  public DatabaseMessage(Type type) {
    // Get
    this.type = type;
  }

  public boolean isNew(){
    return type.equals(Type.NEW);
  }

  public boolean isUpdate(){
    return type.equals(Type.UPDATE);
  }

  public boolean isFinish(){
    return type.equals(Type.FINISH);
  }

  public boolean isGet(){
    return type.equals(Type.GET);
  }

  public Long getUuid() {
    return uuid;
  }

  public int getTotal() {
    return total;
  }

  public String getInput() {
    return input;
  }

  public String getOutput() {
    return output;
  }
}
