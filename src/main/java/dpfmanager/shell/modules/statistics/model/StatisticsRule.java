package dpfmanager.shell.modules.statistics.model;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class StatisticsRule {

  public enum Type {
    ERROR, WARNING
  }

  public String name;
  public Integer total;
  public Integer count;
  public Type type;

  public StatisticsRule(String n, boolean isWarning){
    name = n;
    count = 0;
    total = 0;
    type = (isWarning) ? Type.WARNING : Type.ERROR;
  }

  public Double computePercentageOne(){
    return (count * 1.0) / (total * 1.0);
  }

}
