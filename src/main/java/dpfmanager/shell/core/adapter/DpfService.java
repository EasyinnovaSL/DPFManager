package dpfmanager.shell.core.adapter;

import dpfmanager.shell.core.context.DpfContext;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class DpfService {

  protected DpfContext context;

  public DpfContext getContext(){
    return context;
  }

  public void setContext(DpfContext c){
    context = c;
  }

}
