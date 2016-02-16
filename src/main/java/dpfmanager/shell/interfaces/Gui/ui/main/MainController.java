package dpfmanager.shell.interfaces.Gui.ui.main;

import javafx.event.ActionEvent;

import org.jrebirth.af.api.exception.CoreException;
import org.jrebirth.af.component.ui.stack.StackWaves;
import org.jrebirth.af.core.ui.DefaultController;
import org.jrebirth.af.core.wave.Builders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public final class MainController extends DefaultController<MainModel, MainView> {

  /** The class logger. */
  private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

  /**
   * Default Constructor.
   *
   * @param view the view to control
   *
   * @throws CoreException if an error occurred while creating event handlers
   */
  public MainController (final MainView view) throws CoreException {
    super(view);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void initEventAdapters() throws CoreException {
    // Manage Ui Command Button
    linkWave(getView().getShowDessign(), ActionEvent.ACTION, StackWaves.SHOW_PAGE_ENUM,
        Builders.waveData(StackWaves.PAGE_ENUM, MainPage.Dessign));

    linkWave(getView().getShowReports(), ActionEvent.ACTION, StackWaves.SHOW_PAGE_ENUM,
        Builders.waveData(StackWaves.PAGE_ENUM, MainPage.Reports));

    linkWave(getView().getShowAbout(), ActionEvent.ACTION, StackWaves.SHOW_PAGE_ENUM,
        Builders.waveData(StackWaves.PAGE_ENUM, MainPage.About));

    linkWave(getView().getShowConfig(), ActionEvent.ACTION, StackWaves.SHOW_PAGE_ENUM,
        Builders.waveData(StackWaves.PAGE_ENUM, MainPage.Config));

    linkWave(getView().getShowFirstTime(), ActionEvent.ACTION, StackWaves.SHOW_PAGE_ENUM,
        Builders.waveData(StackWaves.PAGE_ENUM, MainPage.FirstTime));
  }
}
