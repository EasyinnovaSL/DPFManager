package dpfmanager.shell.core.modules;

import dpfmanager.shell.modules.messages.core.MessageModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 24/02/2016.
 */
public class ModulesManager {

  List<DpfModule> modules;

  public ModulesManager(){
    modules = new ArrayList<>();
  }

  public void initAllModules(){
    MessageModule mm = new MessageModule();
    mm.start();
    modules.add(new MessageModule());
  }

  public List<DpfModule> getAllModules(){
    return modules;
  }

}
