package dpfmanager.shell.modules.database.messages;

import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.interfaces.gui.component.periodical.Periodicity;
import dpfmanager.shell.modules.database.tables.Crons;

import java.util.List;

/**
 * Created by Adrià Llorens on 20/04/2016.
 */
public class CronMessage extends DpfMessage {

  public enum Type {
    SAVE, DELETE, GET, RESPONSE, OK
  }

  private Type type;
  private String uuid;
  private String input;
  private String configuration;
  private Periodicity periodicity;
  private List<Crons> crons;

  public CronMessage(Type type, String uuid, String input, String configuration, Periodicity periodicity) {
    // Save
    this.type = type;
    this.uuid = uuid;
    this.input = input;
    this.configuration = configuration;
    this.periodicity = periodicity;
  }

  public CronMessage(Type type, String uuid) {
    // Delete
    this.type = type;
    this.uuid = uuid;
  }

  public CronMessage(Type type) {
    // Get & Ok
    this.type = type;
  }

  public CronMessage(Type type, List<Crons> crons) {
    // Response
    this.type = type;
    this.crons = crons;
  }

  public boolean isSave(){
    return type.equals(Type.SAVE);
  }

  public boolean isDelete(){
    return type.equals(Type.DELETE);
  }

  public boolean isGet(){
    return type.equals(Type.GET);
  }

  public boolean isResponse(){
    return type.equals(Type.RESPONSE);
  }

  public String getUuid() {
    return uuid;
  }

  public String getInput() {
    return input;
  }

  public String getConfiguration() {
    return configuration;
  }

  public Periodicity getPeriodicity() {
    return periodicity;
  }

  public List<Crons> getCrons() {
    return crons;
  }
}
