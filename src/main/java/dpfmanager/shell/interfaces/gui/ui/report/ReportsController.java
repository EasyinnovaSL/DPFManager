package dpfmanager.shell.interfaces.gui.ui.report;

import dpfmanager.shell.core.command.ShowReportCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.Builders;
import org.jrebirth.af.core.wave.JRebirthItems;

/**
 * Created by Adrià Llorens on 02/02/2016.
 */
public class ReportsController extends DefaultController<ReportsModel, ReportsView> {

  public ReportsController(final ReportsView view) throws CoreException {
    super(view);
  }

  @Override
  protected void initEventHandlers() throws CoreException {
    // Load More button
    getView().getLoadButton().setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          getModel().readReports();
          if (getModel().isAllReportsLoaded()) {
            getView().getLoadButton().setVisible(false);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  protected void initEventAdapters() throws CoreException {
  }

  public void shoReportIfNeeded(){
    if (getModel().needShowReport()) {
      String waveData = getModel().getShowReportData();
      getModel().callCommand(ShowReportCommand.class, Builders.waveData(JRebirthItems.stringItem, waveData));
    }
  }

  public void linkFormatIcon(ImageView icon, String type, String path) {
    String data = "{ \"type\": \"" + type + "\", \"path\": \"" + path.replaceAll("\\\\","/") + "\"}";
    linkCommand(icon, MouseEvent.MOUSE_CLICKED, ShowReportCommand.class, Builders.waveData(JRebirthItems.stringItem, data));
  }

}