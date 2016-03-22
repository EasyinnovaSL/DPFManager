package dpfmanager.shell.interfaces.gui.component.report;

import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.messages.LogMessage;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.core.util.NodeUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.logging.log4j.Level;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ReportsController extends DpfController<ReportsModel, ReportsView> {

  public ReportsController(){

  }

  public void loadMoreReports(){
    getContext().send(BasicConfig.MODULE_LOGS, new LogMessage(this.getClass(), Level.INFO, "Count: "));
//    try {
//      getModel().readReports();
//      if (getModel().isAllReportsLoaded()) {
//        getView().hideLoadMore();
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
  }

}
