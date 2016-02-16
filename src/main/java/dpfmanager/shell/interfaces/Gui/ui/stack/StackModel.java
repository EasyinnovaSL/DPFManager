package dpfmanager.shell.interfaces.Gui.ui.stack;

import dpfmanager.shell.interfaces.Gui.command.ShowCustomModelCommand;

import org.jrebirth.af.api.key.UniqueKey;
import org.jrebirth.af.api.ui.Model;
import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.component.ui.stack.*;
import org.jrebirth.af.core.command.basic.showmodel.DisplayModelWaveBean;
import org.jrebirth.af.core.ui.DefaultModel;
import org.jrebirth.af.core.wave.checker.ClassWaveChecker;
import org.jrebirth.af.core.wave.checker.DefaultWaveChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Adrià Llorens on 15/02/2016.
 */
public class StackModel extends DefaultModel<StackModel, StackView> {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(StackModel.class);

  /** Hold the current model displayed as a page. */
  private UniqueKey<? extends Model> currentModelKey;

  /** The default page model key to display first. */
  private UniqueKey<? extends Model> defaultPageModelKey;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void initModel() {

    // Listen StackWaves to be aware of any changes
    listen(new DefaultWaveChecker<>(StackWaves.STACK_NAME, getStackName()), StackWaves.SHOW_PAGE_MODEL);
    listen(new ClassWaveChecker<>(StackWaves.PAGE_ENUM, getPageEnumClass()), StackWaves.SHOW_PAGE_ENUM);
  }

  /**
   * Show page.
   *
   * Called when model received a SHOW_PAGE wave type.
   *
   * @param pageModelKey the modelKey for the page to show
   * @param stackName the unique string tha t identify the stack
   *
   * @param wave the wave
   */
  public void doShowPageModel(final UniqueKey<? extends Model> pageModelKey, final String stackName, final Wave wave) {

    if (getStackName() != null && getStackName().equals(stackName)) {
      showPage(pageModelKey);
    }

  }

  /**
   * Show page.
   *
   * Called when model received a SHOW_PAGE wave type.
   *
   * @param pageEnum the page enum for the model to show
   * @param wave the wave
   */
  public void doShowPageEnum(final PageEnum pageEnum, final Wave wave) {

    LOGGER.info("Show Page Enum: " + pageEnum.toString());
    if (getPageEnumClass() != null && getPageEnumClass().equals(pageEnum.getClass())) {
      showPage(pageEnum.getModelKey());
    }
  }

  /**
   * Returns the page enum class associated to this model.
   *
   * Checks the modelObject and return it only if it extends {@link PageEnum}
   *
   * @return the page enum class
   */
  @SuppressWarnings("unchecked")
  private Class<PageEnum> getPageEnumClass() {
    Class<PageEnum> res = null;
    if (getFirstKeyPart() instanceof Class && PageEnum.class.isAssignableFrom((Class<?>) getFirstKeyPart())) {
      res = (Class<PageEnum>) getFirstKeyPart();
    }
    return res;
  }

  /**
   * Returns the current stack name associated to this model.
   *
   * Checks the modelObject and return it only if it is a String
   *
   * @return the stack name
   */
  private String getStackName() {
    String res = null;
    if (getFirstKeyPart() instanceof String) {
      res = (String) getFirstKeyPart();
    }
    return res;
  }

  /**
   * Private method used to show another page.
   *
   * @param pageModelKey the mdoelKey for the page to show
   */
  private void showPage(final UniqueKey<? extends Model> pageModelKey) {
    LOGGER.info("Show Page Model: " + pageModelKey.toString());

    // Create the Wave Bean that will hold all data processed by chained commands
    final DisplayModelWaveBean waveBean = DisplayModelWaveBean.create()
        // Define the placeholder that will receive the content
        .childrenPlaceHolder(getView().getRootNode().getChildren())
            // Allow to add element behind the stack to allow transition
        .appendChild(false)
        .showModelKey(pageModelKey)
        .hideModelKey(this.currentModelKey);

    this.currentModelKey = waveBean.showModelKey();

    callCommand(ShowCustomModelCommand.class, waveBean);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void showView() {
    // On redisplay show the start page only if no page is displayed
    if (this.currentModelKey == null) {
      // Manage default page for Page Enum
      if (getPageEnumClass() != null && getPageEnumClass().isEnum() && getPageEnumClass().getEnumConstants().length > 0) {
        doShowPageEnum(getPageEnumClass().getEnumConstants()[0], null);
      }
      // Manage default page for pageModelKey
      if (getDefaultPageModelKey() != null) {
        doShowPageModel(getDefaultPageModelKey(), getStackName(), null);
      }
    }
  }

  /**
   * @return the default PageModel Key
   */
  public UniqueKey<? extends Model> getDefaultPageModelKey() {
    return this.defaultPageModelKey;
  }

}
