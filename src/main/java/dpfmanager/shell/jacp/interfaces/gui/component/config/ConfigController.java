package dpfmanager.shell.jacp.interfaces.gui.component.config;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.interfaces.GuiApp;
import dpfmanager.shell.interfaces.gui.ui.main.MainModel;
import dpfmanager.shell.jacp.core.config.GuiConfig;
import dpfmanager.shell.jacp.core.messages.UiMessage;
import dpfmanager.shell.jacp.core.mvc.DpfController;
import dpfmanager.shell.jacp.core.workbench.GuiWorkbench;
import dpfmanager.shell.jacp.interfaces.gui.fragment.wizard.Wizard1Fragment;
import dpfmanager.shell.jacp.interfaces.gui.fragment.wizard.Wizard2Fragment;
import dpfmanager.shell.jacp.interfaces.gui.fragment.wizard.Wizard3Fragment;
import dpfmanager.shell.jacp.interfaces.gui.fragment.wizard.Wizard4Fragment;
import dpfmanager.shell.jacp.interfaces.gui.fragment.wizard.Wizard5Fragment;
import dpfmanager.shell.jacp.interfaces.gui.fragment.wizard.Wizard6Fragment;
import javafx.stage.FileChooser;

import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;

import java.io.File;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class ConfigController extends DpfController<ConfigModel, ConfigView> {

  private ManagedFragmentHandler<Wizard1Fragment> fragment1;
  private ManagedFragmentHandler<Wizard2Fragment> fragment2;
  private ManagedFragmentHandler<Wizard3Fragment> fragment3;
  private ManagedFragmentHandler<Wizard4Fragment> fragment4;
  private ManagedFragmentHandler<Wizard5Fragment> fragment5;
  private ManagedFragmentHandler<Wizard6Fragment> fragment6;

  public void clearAllSteps() {
    fragment1.getController().clear();
    fragment2.getController().clear();
    fragment3.getController().clear();
    fragment4.getController().clear();
    fragment5.getController().clear();
    fragment6.getController().clear();
  }

  public void saveConfig() {
    File file;
    String value = GuiWorkbench.getTestParams("saveConfig");
    if (value != null) {
      //Save in specified output
      file = new File(value);
    } else {
      //Open save dialog
      FileChooser fileChooser = new FileChooser();
      fileChooser.setInitialDirectory(new File(DPFManagerProperties.getConfigDir()));
      fileChooser.setInitialFileName("config.dpf");
      fileChooser.setTitle("Save Config");
      file = fileChooser.showSaveDialog(GuiWorkbench.getMyStage());
    }

    if (file != null) {
      try {
        getModel().saveConfig(file.getAbsolutePath());
        getContext().send(GuiConfig.PRESPECTIVE_DESSIGN, new UiMessage(UiMessage.Type.SHOW));
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  public void saveSettings(int step) {
    switch (step) {
      case 1:
        fragment1.getController().saveIsos(getModel().getConfiguration());
        break;
      case 2:
        fragment2.getController().saveRules(getModel().getConfiguration());
        break;
      case 3:
        fragment3.getController().saveFormats(getModel().getConfiguration());
        fragment3.getController().saveOutput(getModel().getConfiguration());
        break;
      case 4:
        fragment4.getController().saveFixes(getModel().getConfiguration());
        break;
    }
  }

  public void loadSettings(int step) {
    switch (step) {
      case 1:
        fragment1.getController().loadIsos(getModel().getConfiguration());
        break;
      case 2:
        fragment2.getController().loadRules(getModel().getConfiguration());
        break;
      case 3:
        fragment3.getController().loadFormats(getModel().getConfiguration());
        fragment3.getController().loadOutput(getModel().getConfiguration());
        break;
      case 4:
        fragment4.getController().loadFixes(getModel().getConfiguration());
        break;
      case 6:
        fragment6.getController().loadSummary(getModel().getConfiguration());
        break;
    }
  }

  /** Setters */

  public void setFragment1(ManagedFragmentHandler<Wizard1Fragment> fragment1) {
    this.fragment1 = fragment1;
  }

  public void setFragment2(ManagedFragmentHandler<Wizard2Fragment> fragment2) {
    this.fragment2 = fragment2;
  }

  public void setFragment3(ManagedFragmentHandler<Wizard3Fragment> fragment3) {
    this.fragment3 = fragment3;
  }

  public void setFragment4(ManagedFragmentHandler<Wizard4Fragment> fragment4) {
    this.fragment4 = fragment4;
  }

  public void setFragment5(ManagedFragmentHandler<Wizard5Fragment> fragment5) {
    this.fragment5 = fragment5;
  }

  public void setFragment6(ManagedFragmentHandler<Wizard6Fragment> fragment6) {
    this.fragment6 = fragment6;
  }

}
