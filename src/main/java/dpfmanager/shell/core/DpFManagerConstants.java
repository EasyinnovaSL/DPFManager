package dpfmanager.shell.core;

/**
 * Created by Adrià Llorens on 05/05/2016.
 */
public class DpFManagerConstants {

  /**
   * The minimum width
   */
  public static final int MIN_WIDTH = 400;

  /**
   * The initial width
   */
  public static final int WIDTH = 970;

  /**
   * The initial height
   */
  public static final int HEIGHT = 950;

  /**
   * The maximum number of simultaneous checks
   */
  public static final int MAX_CHECKS = 3;

  /**
   * Database update interval in milliseconds
   */
  public static final Integer UPDATE_INTERVAL = 1000;

  /**
   * Hours that a job is in database
   */
  public static final Integer JOB_LIFE_HOURS = 48;

  /**
   * Minutes that a job is running without any change in DB
   */
  public static final Integer JOB_LAST_UPDATE_MINUTES = 30;

  /**
   * The database version, incress it for force delete tables and recreate
   */
  public static final int DATABASE_VERSION = 1;

}
