package dpfmanager.shell.interfaces.gui.component.show;

import dpfmanager.shell.modules.report.runnable.MakeReportRunnable;

/**
 * Created by Adrià Llorens on 15/06/2017.
 */
public class ShowReport {

  public Long uuid;
  public Integer count = 0;
  public Integer max = 0;
  public Integer globalValue = 0;
  public MakeReportRunnable mrr = null;
  public boolean finished = false;
  public boolean onlyGlobal = false;

  public ShowReport(Long u){
    uuid = u;
  }

}
