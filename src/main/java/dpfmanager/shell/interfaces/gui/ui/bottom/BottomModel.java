package dpfmanager.shell.interfaces.gui.ui.bottom;

import org.jrebirth.af.core.ui.fxml.DefaultFXMLModel;

/**
 * Created by Adrià Llorens on 18/02/2016.
 */
public class BottomModel extends DefaultFXMLModel<BottomModel> {

  private BottomController controller;

  private double dividerPos = 0.7;

  @Override
  protected void initModel() {

  }

  public void setController(BottomController bc){
    controller = bc;
  }

  public BottomController getController(){
    return controller;
  }

  public void setDividerPos(double dividerPos) {
    this.dividerPos = dividerPos;
  }

  public double getDividerPos() {
    return dividerPos;
  }
}