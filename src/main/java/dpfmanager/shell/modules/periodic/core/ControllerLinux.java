package dpfmanager.shell.modules.periodic.core;

import java.util.List;

/**
 * Created by Adrià Llorens on 07/07/2016.
 */
public class ControllerLinux extends Controller {
  @Override
  public boolean savePeriodicalCheck(PeriodicCheck check) {
    return false;
  }

  @Override
  public boolean deletePeriodicalCheck(String uuid) {
    return false;
  }

  @Override
  public List<PeriodicCheck> readPeriodicalChecks() {
    return null;
  }
}
