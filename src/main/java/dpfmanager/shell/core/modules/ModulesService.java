package dpfmanager.shell.core.modules;

import dpfmanager.shell.modules.messages.MessageService;
import dpfmanager.shell.modules.messages.core.MessageModule;

import org.jrebirth.af.api.service.Service;
import org.jrebirth.af.core.service.DefaultService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 23/02/2016.
 *
 * ONLY for gui application.
 */
public class ModulesService extends DefaultService {

  ModulesManager manager;
  List<Service> services;

  @Override
  public void initService() {
    services = new ArrayList<>();
    manager = new ModulesManager();
    initAllModules();
    initAllServices();
  }

  public void initAllModules() {
    manager.initAllModules();
  }

  public void initAllServices() {
    for (DpfModule module : manager.getAllModules()){
      // Search matching service
      MessageService mm = getService(MessageService.class);
      mm.setModule((MessageModule) module);
      services.add(mm);
    }
  }

}
