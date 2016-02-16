package dpfmanager.shell.interfaces.Gui.command;

import dpfmanager.shell.interfaces.Gui.ui.report.ReportsModel;

import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.WaveBean;
import org.jrebirth.af.core.command.single.ui.DefaultUIBeanCommand;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
public final class ParseReportsCommand extends DefaultUIBeanCommand<WaveBean> {

  @Override
  public void initCommand() {
    // You must put your initialization code here (if any)
  }

  @Override
  protected void perform(final Wave wave) {
    getModel(ReportsModel.class).getView().addData();
  }

}
