package dpfmanager.shell.jacp.core.mvc;

import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public abstract class DpfView<M extends DpfModel, C extends DpfController> implements ViewInterface<M, C> {

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
