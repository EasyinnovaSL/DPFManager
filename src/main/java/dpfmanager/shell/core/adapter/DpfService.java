package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.context.DpfContext;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public abstract class DpfService {

  protected DpfContext context;

  protected abstract void handleContext(DpfContext context);

  public DpfContext getContext(){
    return context;
  }

  public void setContext(DpfContext c){
    context = c;
    handleContext(context);
  }

}
