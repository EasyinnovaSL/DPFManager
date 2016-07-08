package dpfmanager.shell.modules.periodic.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.periodic.messages.PeriodicMessage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_PERIODIC)
@Scope("singleton")
public class PeriodicService extends DpfService {

  private Controller controller;

  @PostConstruct
  public void init() {
    // No context yet
  }

  @Override
  protected void handleContext(DpfContext context) {
    if (System.getProperty("os.name").startsWith("Windows")) {
      controller = new ControllerWindows(context, DPFManagerProperties.getBundle());
    } else {
      controller = new ControllerLinux(context, DPFManagerProperties.getBundle());
    }
  }

  public void savePeriocicalCheck(PeriodicCheck check) {
    boolean result = controller.savePeriodicalCheck(check);
    context.send(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.SAVE, check.getUuid(), result));
  }

  public void editPeriocicalCheck(PeriodicCheck check) {
    boolean result = controller.editPeriodicalCheck(check);
    context.send(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.EDIT, check.getUuid(), result));
  }

  public void deletePeriocicalCheck(String uuid) {
    boolean result = controller.deletePeriodicalCheck(uuid);
    context.send(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.DELETE, uuid, result));
  }

  public void readPeriodicalChecks() {
    List<PeriodicCheck> list = controller.readPeriodicalChecks();
    context.send(GuiConfig.COMPONENT_PERIODICAL, new PeriodicMessage(PeriodicMessage.Type.LIST, list));
  }

  @PreDestroy
  public void finish() {
  }

}
