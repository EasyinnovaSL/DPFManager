package dpfmanager.jrebirth.ui.main;

import dpfmanager.jrebirth.ui.about.AboutModel;
import dpfmanager.jrebirth.ui.config.ConfigModel;
import dpfmanager.jrebirth.ui.dessign.DessignModel;
import dpfmanager.jrebirth.ui.firsttime.FirstTimeModel;
import dpfmanager.jrebirth.ui.report.ReportsModel;

import org.jrebirth.af.api.key.UniqueKey;
import org.jrebirth.af.api.ui.Model;
import org.jrebirth.af.component.ui.stack.PageEnum;
import org.jrebirth.af.core.key.Key;

/**
 * Created by Adrià Llorens on 01/02/2016.
 */
public enum MainPage implements PageEnum {

  /** Conformance Checker pane */
  Dessign,

  /** Reports pane */
  Reports,

  /** About pane */
  About,

  /** Config pane */
  Config,

  /** First time pane*/
  FirstTime

  ;

  /**
   * {@inheritDoc}
   */
  @Override
  public UniqueKey<? extends Model> getModelKey() {
    UniqueKey<? extends Model> modelKey;

    switch (this) {
      default:
      case Dessign:
        modelKey = Key.create(DessignModel.class);
        break;
      case Reports:
        modelKey = Key.create(ReportsModel.class);
        break;
      case About:
        modelKey = Key.create(AboutModel.class);
        break;
      case Config:
        modelKey = Key.create(ConfigModel.class);
        break;
      case FirstTime:
        modelKey = Key.create(FirstTimeModel.class);
        break;
    }

    return modelKey;
  }

}
