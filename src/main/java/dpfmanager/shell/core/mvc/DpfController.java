package dpfmanager.shell.core.mvc;

import org.jacpfx.rcp.context.Context;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class DpfController<M extends ModelInterface, V extends ViewInterface<M, ?>> implements ControllerInterface<M , V> {

  private V view;

  private Context context;

  public DpfController(){
  }

  public void setView(V v){
    view = v;
  }

  public void setContext(Context cont){
    context = cont;
  }

  @Override
  public M getModel() {
    return getView().getModel();
  }

  @Override
  public V getView() {
    return view;
  }

  @Override
  public Context getContext() {
    return context;
  }

}
