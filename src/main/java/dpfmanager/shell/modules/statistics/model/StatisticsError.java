package dpfmanager.shell.modules.statistics.model;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class StatisticsError {

  public enum Type {
    ERROR, WARNING
  }

  public String id;
  public String description;
  public Type type;
  public Integer count;

  public StatisticsError(String i, String d, boolean isWarning){
    id = i;
    description = d;
    count = 0;
    type = (isWarning) ? Type.WARNING : Type.ERROR;
  }

}
