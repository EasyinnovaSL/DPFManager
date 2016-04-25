package dpfmanager.shell.modules.threading.core;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.modules.report.core.IndividualReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 14/04/2016.
 */
public class FileCheck {

  private long uuid;
  private int total;
  private int errors;
  private Configuration config;
  private String internal;
  private String input;
  private List<IndividualReport> individuals;

  public FileCheck(){
    uuid = 77;
  }

  public FileCheck(long u, int n, Configuration c, String i, String ri){
    uuid = u;
    total = n;
    config = c;
    internal = i;
    individuals = new ArrayList<>();
    errors = 0;
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

  public int getTotal() {
    return total;
  }
}
