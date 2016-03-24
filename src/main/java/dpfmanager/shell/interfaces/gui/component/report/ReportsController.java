package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.mvc.DpfController;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ReportsController extends DpfController<ReportsModel, ReportsView> {

  public ReportsController(){

  }

  public void loadMoreReports(){
    try {
      getModel().readReports();
      if (getModel().isAllReportsLoaded()) {
        getView().hideLoadMore();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
