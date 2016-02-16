package dpfmanager.shell.interfaces.Gui.ui.about;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.core.ui.DefaultController;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public class AboutController extends DefaultController<AboutModel, AboutView> {

  public AboutController(final AboutView view) throws CoreException {
    super(view);
  }

}