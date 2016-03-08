package dpfmanager.shell.interfaces.gui.ui.stack;

import javafx.scene.layout.StackPane;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.api.ui.annotation.RootNodeClass;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.ui.DefaultView;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
@RootNodeClass("StackPanel")
public class StackView extends DefaultView<StackModel, StackPane, DefaultController<StackModel, StackView>> {

  /** The Constant LOGGER. */
  // private static final Logger LOGGER = LoggerFactory.getLogger(StackView.class);

  /**
   * Instantiates a new Stack View.
   *
   * @param model the model
   *
   * @throws CoreException the core exception
   */
  public StackView(final StackModel model) throws CoreException {
    super(model);
  }

}

