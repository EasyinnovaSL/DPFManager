package dpfmanager.shell.modules.messages;

import dpfmanager.shell.modules.messages.core.MessageModule;

import org.apache.log4j.Logger;
import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.annotation.OnWave;
import org.jrebirth.af.api.wave.contract.WaveType;
import org.jrebirth.af.core.service.DefaultService;
import org.jrebirth.af.core.wave.Builders;
import org.jrebirth.af.core.wave.WaveItemBase;

/**
 * Created by Adrià Llorens on 23/02/2016.
 */
public class MessageService extends DefaultService {

  /**
   * The WaveType name.
   */
  public final static String LOG_MESSAGE = "LOG_MESSAGE";

  /**
   * The input wave items
   */
  public static WaveItemBase<Class> CLASS = new WaveItemBase<Class>() {
  };
  public static WaveItemBase<MessageModule.LogType> TYPE = new WaveItemBase<MessageModule.LogType>() {
  };
  public static WaveItemBase<String> MESSAGE = new WaveItemBase<String>() {
  };

  /**
   * The WaveType
   */
  public static WaveType DO_LOG_MESSAGE = Builders
      .waveType(LOG_MESSAGE).items(TYPE, MESSAGE);


  /**
   * The associated module
   */
  private MessageModule module;

  public void setModule(MessageModule module) {
    this.module = module;
  }

  @Override
  public void initService() {
    Logger.getLogger(MessageService.class).info("Starting messages module");
  }

  @OnWave(MessageService.LOG_MESSAGE)
  public void doLogMessage(Class clas, MessageModule.LogType type, String message, final Wave wave) {
    module.logMessage(clas, type, message);
  }
}
