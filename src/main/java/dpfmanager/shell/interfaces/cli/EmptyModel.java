package dpfmanager.shell.interfaces.cli;


import dpfmanager.shell.core.modules.ModulesService;
import dpfmanager.shell.modules.messages.MessageService;
import dpfmanager.shell.modules.messages.core.MessageModule;
import javafx.application.Platform;
import javafx.scene.Node;

import org.jrebirth.af.core.ui.simple.DefaultSimpleModel;
import org.jrebirth.af.core.wave.Builders;

/**
 * Created by Adrià Llorens on 24/02/2016.
 */
public class EmptyModel extends DefaultSimpleModel<Node> {

  private ModulesService manager;

  @Override
  protected void initModel() {
    try {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          getLocalFacade().getGlobalFacade().getApplication().getStage().hide();
        }
      });

      manager = getService(ModulesService.class);

      sendWave(MessageService.DO_LOG_MESSAGE,
          Builders.waveData(MessageService.CLASS, getClass()),
          Builders.waveData(MessageService.TYPE, MessageModule.LogType.INFO),
          Builders.waveData(MessageService.MESSAGE, "Test Message log Command Line")
      );

//      for (int i=0 ; i<5 ; i++){
//        sendWave(MessageModule.DO_LOG_MESSAGE,
//            Builders.waveData(MessageModule.CLASS, getClass()),
//            Builders.waveData(MessageModule.TYPE, MessageModule.LogType.INFO),
//            Builders.waveData(MessageModule.MESSAGE, "Sleep "+i)
//        );
//        Thread.sleep(1000);
//      }

//      sendWave(MessageModule.DO_LOG_MESSAGE,
//          Builders.waveData(MessageModule.CLASS, getClass()),
//          Builders.waveData(MessageModule.TYPE, MessageModule.LogType.INFO),
//          Builders.waveData(MessageModule.MESSAGE, "Exit!")
//      );
//
//      Platform.exit();
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

}
