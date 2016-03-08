package dpfmanager.shell.jacp.core.mvc;

import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public interface ModelInterface<V extends ViewInterface<?, C>, C extends ControllerInterface> {

  public V getView();

  public C getController();

  public Context getContext();

}
