package dpfmanager.shell.core.mvc;

import org.jacpfx.rcp.context.Context;

import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class DpfModel<V extends ViewInterface<?, C>, C extends ControllerInterface> implements ModelInterface<V, C> {

  private V view;

  private Context context;

  private ResourceBundle bundle;

  public DpfModel(){
  }

  public void setView(V v){
    view = v;
  }

  public void setContext(Context cont){
    context = cont;
  }

  @Override
  public V getView() {
    return view;
  }

  @Override
  public C getController() {
    return getView().getController();
  }

  @Override
  public Context getContext() {
    return context;
  }

  public void setResourcebundle(ResourceBundle bundle){
    this.bundle = bundle;
  }

  public ResourceBundle getBundle() {
    return bundle;
  }

}
