package dpfmanager.shell.modules.periodic.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.periodic.core.PeriodicCheck;

import java.util.List;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class PeriodicMessage extends DpfMessage {

  public enum Type {
    SAVE, EDIT, DELETE, READ, LIST
  }

  private Type type;
  private String uuid;
  private boolean result;
  private PeriodicCheck check;
  private List<PeriodicCheck> periodicChecks;

  public PeriodicMessage(Type type) {
    // Read
    this.type = type;
  }

  public PeriodicMessage(Type type, PeriodicCheck check) {
    // Save & Edit
    this.type = type;
    this.check = check;
  }

  public PeriodicMessage(Type type, String uuid) {
    // Delete
    this.type = type;
    this.uuid = uuid;
  }

  public PeriodicMessage(Type type, String uuid, boolean result) {
    // Delete response
    this.type = type;
    this.uuid = uuid;
    this.result = result;
  }

  public PeriodicMessage(Type type, List<PeriodicCheck> periodicChecks) {
    // List
    this.type = type;
    this.periodicChecks = periodicChecks;
  }

  public boolean isSave(){
    return type.equals(Type.SAVE);
  }

  public boolean isEdit(){
    return type.equals(Type.EDIT);
  }

  public boolean isDelete(){
    return type.equals(Type.DELETE);
  }

  public boolean isRead(){
    return type.equals(Type.READ);
  }

  public boolean isList(){
    return type.equals(Type.LIST);
  }

  public String getUuid() {
    return uuid;
  }

  public boolean getResult() {
    return result;
  }

  public PeriodicCheck getPeriodicCheck() {
    return check;
  }

  public List<PeriodicCheck> getPeriodicChecks() {
    return periodicChecks;
  }
}
