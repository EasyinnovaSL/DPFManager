package dpfmanager.shell.modules;

import dpfmanager.shell.core.modules.DpfModule;

import org.jrebirth.af.api.wave.Wave;
import org.jrebirth.af.api.wave.annotation.OnWave;
import org.jrebirth.af.api.wave.contract.WaveType;
import org.jrebirth.af.core.service.DefaultService;
import org.jrebirth.af.core.wave.Builders;
import org.jrebirth.af.core.wave.WaveItemBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Adrià Llorens on 22/02/2016.
 */
public class SampleService extends DefaultService {

  /**
   * The WaveType name.
   */
  public final static String SOMETHING = "SOMETHING";

  /**
   * The WaveType return action name.
   */
  public final static String SOMETHING_DONE = "SOMETHING_DONE";

  /**
   * The input wave items
   */
  public static WaveItemBase<String> IN1 = new WaveItemBase<String>() {
  };
  public static WaveItemBase<String> IN2 = new WaveItemBase<String>() {
  };

  /**
   * The output wave item
   */
  public static WaveItemBase<Integer> OUT = new WaveItemBase<Integer>() {
  };

  /**
   * The WaveType
   */
  public static WaveType DO_SOMETHING = Builders
      .waveType(SOMETHING).items(IN1, IN2)
      .returnAction(SOMETHING_DONE).returnItem(OUT);

  /**
   * The class logger.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SampleService.class);
  private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SampleService.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public void initService() {
    // Define the service method
  }

  @OnWave(SOMETHING)
  public Integer doSomething(String in1, String in2, final Wave wave) {
    System.out.println("Do something:");
    System.out.println("   Input 1: " + in1);
    System.out.println("   Input 2: " + in2);
    if (LOG.isInfoEnabled()) {
      System.out.println("enable");
    } else {
      System.out.println("disable");
    }
    LOG.debug("DEBUG");
    LOG.warn("WARNING");
    LOG.info("INFO");
    LOG.error("ERROR");
    LOG.trace("TRACE");

    try {
      ArrayList<String> str = null;
      str.add("HOL-A");
      str.get(1);
    }
    catch (Exception e){
      LOG.error(e, e);
    }

    return 93;
  }

}