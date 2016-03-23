package dpfmanager.shell.core.mvc;

import dpfmanager.shell.core.adapter.DpfSimpleView;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import javafx.event.Event;
import javafx.scene.Node;

import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.PerspectiveLayout;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public abstract class DpfView<M extends DpfModel, C extends DpfController> extends DpfSimpleView implements ViewInterface<M, C> {

  private M model = null;
  private C controller = null;

  public DpfView(){
  }

  @Override
  public M getModel() {
    return model;
  }

  @Override
  public C getController() {
    return controller;
  }

  @Override
  public void setModel(M m) {
    model = m;
    model.setView(this);
    model.setContext(getContext());
  }

  @Override
  public void setController(C c) {
    controller = c;
    controller.setView(this);
    controller.setContext(getContext());
  }

}
