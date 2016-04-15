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
  private Configuration config;
  private String internal;
  private List<IndividualReport> individuals;

  public FileCheck(long u, int n, Configuration c, String i){
    uuid = u;
    total = n;
    config = c;
    internal = i;
    individuals = new ArrayList<>();
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
    return total == individuals.size();
  }

}
