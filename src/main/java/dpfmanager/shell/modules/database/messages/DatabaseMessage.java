package dpfmanager.shell.modules.database.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
public class DatabaseMessage extends DpfMessage {

  public enum Type {
    NEW, INIT, UPDATE, FINISH, GET, RESUME, CANCEL
  }

  private Type type;
  private Long uuid;
  private int total;
  private String input;
  private String output;
  private boolean pending;

  public DatabaseMessage(Type type, Long uuid, String input, boolean pending) {
    // New
    this.type = type;
    this.uuid = uuid;
    this.input = input;
    this.pending = pending;
  }

  public DatabaseMessage(Type type, Long uuid, int total, String output ) {
    // Init
    this.type = type;
    this.uuid = uuid;
    this.total = total;
    this.output = output;
  }

  public DatabaseMessage(Type type, Long uuid) {
    // Update && Finish && Resume && Cancel
    this.type = type;
    this.uuid = uuid;
  }

  public DatabaseMessage(Type type) {
    // Get
    this.type = type;
  }

  public boolean isInit(){
    return type.equals(Type.INIT);
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

  public boolean isResume(){
    return type.equals(Type.RESUME);
  }

  public boolean isCancel(){
    return type.equals(Type.CANCEL);
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

  public boolean getPending() {
    return pending;
  }
}
