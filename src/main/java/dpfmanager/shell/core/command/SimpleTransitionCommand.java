package dpfmanager.shell.core.command;

import javafx.collections.ObservableList;
import javafx.scene.Node;

import org.jrebirth.af.api.concurrent.RunInto;
import org.jrebirth.af.api.concurrent.RunType;
import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.command.single.AbstractSingleCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FadeTransitionCommand.
 *
 * Add a fading transition to switch 2 model node hold into a stackpane (or equivalent).
 */
@RunInto(RunType.JAT)
public class SimpleTransitionCommand extends AbstractSingleCommand<DisplayModelWaveBean> {

  /**
   * The Constant LOGGER.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTransitionCommand.class);

  @Override
  protected void initCommand() {
    // Nothing to do yet
  }

  @Override
  protected void perform(final Wave wave) {

    // The old node is the one that exists into the parent container (or null if none)
    Node oldNode = getWaveBean(wave).hideModel() == null ? null : getWaveBean(wave).hideModel().getRootNode();

    if (oldNode == null) {
      final ObservableList<Node> parentContainer = getWaveBean(wave).childrenPlaceHolder();
      oldNode = parentContainer.size() > 1 ? parentContainer.get(getWaveBean(wave).childrenPlaceHolder().size() - 1) : null;
    }

    // The new node is the one create by PrepareModelCommand
    Node newNode = getWaveBean(wave).showModel() == null ? null : getWaveBean(wave).showModel().getRootNode();

    if (oldNode != null || newNode != null) {
      getWaveBean(wave).childrenPlaceHolder().remove(oldNode);
      getWaveBean(wave).showModel().doShowView(wave);
    }
  }

  @Override
  protected void initInnerComponents() {
    // Nothing to do
  }

}
