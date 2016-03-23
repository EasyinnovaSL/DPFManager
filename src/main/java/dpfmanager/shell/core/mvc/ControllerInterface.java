package dpfmanager.shell.core.mvc;

import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public interface ControllerInterface<M extends ModelInterface, V extends ViewInterface<M, ?>> {

  public M getModel();

  public V getView();

  public Context getContext();

}
