package dpfmanager.shell.core.mvc;

import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public interface ViewInterface<M extends ModelInterface, C extends ControllerInterface> {

  public M getModel();
  public C getController();
  public Context getContext();

  public void setModel(M m);
  public void setController(C c);

}
