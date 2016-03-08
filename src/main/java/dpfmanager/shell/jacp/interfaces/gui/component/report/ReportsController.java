package dpfmanager.shell.jacp.interfaces.gui.component.report;

import dpfmanager.shell.jacp.core.mvc.DpfController;
import dpfmanager.shell.jacp.core.util.NodeUtil;
import dpfmanager.shell.jacp.interfaces.gui.component.dessign.DessignModel;
import dpfmanager.shell.jacp.interfaces.gui.component.dessign.DessignView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ReportsController extends DpfController<ReportsModel, ReportsView> {

  public void initButtonsHandlers(){
    // Load More button
    getView().getLoadMore().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          getModel().readReports();
          if (getModel().isAllReportsLoaded()) {
            NodeUtil.hideNode(getView().getLoadMore());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

}
