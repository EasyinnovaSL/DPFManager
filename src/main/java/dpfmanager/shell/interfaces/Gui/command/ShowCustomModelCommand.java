package dpfmanager.shell.interfaces.Gui.command;

import org.jrebirth.af.api.command.Command;
import org.jrebirth.af.api.key.UniqueKey;
import org.jrebirth.af.core.command.basic.showmodel.DetachModelCommand;
import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.command.basic.showmodel.ShowModelCommand;
import org.jrebirth.af.core.command.multi.DefaultMultiBeanCommand;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
public class ShowCustomModelCommand extends DefaultMultiBeanCommand<DisplayModelWaveBean> {
  public ShowCustomModelCommand() {
  }

  protected List<UniqueKey<? extends Command>> defineSubCommand() {
    return Arrays.asList(new UniqueKey[]{
        getCommandKey(ShowModelCommand.class, new Object[0]),
        getCommandKey(FadeTransitionCommand.class, new Object[0]),
        getCommandKey(DetachModelCommand.class, new Object[0])
    });
  }
}
