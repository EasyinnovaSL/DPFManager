package dpfmanager.shell.modules.threading.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.threading.runnable.DpfRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Adrià Llorens on 14/04/2016.
 */
public class FileCheck {

  /** Check information */
  private long uuid;
  private int total;
  private int errors;
  private Configuration config;
  private String internal;
  private String input;
  private List<IndividualReport> individuals;

  /** Runnable tasks of this check */
  private List<DpfRunnable> runnables;

  /** Initial task */
  private DpfRunnable initialTask;

  public FileCheck(long u){
    uuid = u;
    individuals = new ArrayList<>();
    runnables = new ArrayList<>();
    errors = 0;
  }

  public void init(int n, Configuration c, String i, String ri){
    total = n;
    config = c;
    internal = i;
    input = ri;
  }

  public void addIndividual(IndividualReport ir){
    individuals.add(ir);
  }

  public List<IndividualReport> getIndividuals() {
    return individuals;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getInternal() {
    return internal;
  }

  public boolean allFinished(){
    return total == getFinished();
  }

  public void addError(){
    errors++;
  }

  public long getUuid() {
    return uuid;
  }

  public int getFinished() {
    return individuals.size() + errors;
  }

  public String getInput() {
    String ret = input;
    if (ret.length() > 50) {
      ret = ret.substring(0, 47) + "...";
    }
    return ret;
  }

  public void setInitialTask(DpfRunnable initialTask) {
    this.initialTask = initialTask;
  }

  public DpfRunnable getInitialTask() {
    return initialTask;
  }

  public int getTotal() {
    return total;
  }
}
