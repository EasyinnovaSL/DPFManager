package dpfmanager.shell.core.command;

import dpfmanager.shell.core.modules.ModulesService;

import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.core.command.single.internal.DefaultCommand;

/**
 * Created by Adrià Llorens on 24/02/2016.
 */
public class TestCommand extends DefaultCommand {

  private ModulesService manager;

  @Override
  public void initCommand() {
    // You must put your initialization code here (if any)
  }

  @Override
  protected void perform(final Wave wave) {
    manager = getService(ModulesService.class);
    String omg = "";
  }

  public ModulesService getManager(){
    return manager;
  }
}
