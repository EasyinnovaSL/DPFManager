package dpfmanager.shell.application.launcher.noui;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.interfaces.console.ConsoleController;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class ConsoleLauncher {

  /**
   * Static param for tests wait for finish
   */
  private static boolean finished;

  /**
   * The args.
   */
  private List<String> params;

  /**
   * The main controller.
   */
  private ConsoleController controller;

  /**
   * The Dpf Context
   */
  private ConsoleContext context;

  public ConsoleLauncher(String[] args) {
    ConsoleLauncher.setFinished(false);
    // Load spring context
    AppContext.loadContext("DpfSpringConsole.xml");
    //Load DpfContext
    context = new ConsoleContext(AppContext.getApplicationContext());
    // Program input
    params = new ArrayList(Arrays.asList(args));
    // The main controller
    controller = new ConsoleController(context);
  }

  /**
   * Launch.
   */
  public void launch() {
    // Reads the parameters
    int idx = 0;
    boolean argsError = false;
    ArrayList<String> files = new ArrayList<>();
    ApplicationParameters parameters = new ApplicationParameters();
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      if (arg.equals("-o")) {
        if (idx + 1 < params.size()) {
          String outputFolder = params.get(++idx);
          File tmp = new File(outputFolder);
          if (!tmp.exists()) {
            if (!tmp.mkdirs()) {
              printOut("Cannot create the output folder.");
              outputFolder = null;
            }
          } else if (!tmp.isDirectory()) {
            printOut("The output path must be a directory.");
            outputFolder = null;
          } else if (tmp.listFiles().length > 0) {
            printOut("The output folder must be empty.");
            outputFolder = null;
          }
          if (outputFolder != null) {
            controller.setOutputFolder(outputFolder);
            controller.setExplicitOutput(true);
          } else{
            argsError = true;
          }
        } else {
          printOut("You must specify the output folder after '-o' option.");
          argsError = true;
        }
      } else if (arg.equals("-s")) {
        parameters.setSilence(true);
      } else if (arg.equals("-r")) {
        parameters.setRecursive(Integer.MAX_VALUE);
      } else if (arg.startsWith("-r") && isNumeric(arg.substring(2))) {
        parameters.setRecursive(Integer.parseInt(arg.substring(2)));
      } else if (arg.equals("-configuration")) {
        if (idx + 1 < params.size()) {
          String xmlConfig = params.get(++idx);
          Configuration config = new Configuration();
          try {
            config.ReadFile(xmlConfig);
            if (config.getFormats().size() == 0) {
              printOut("No report format was specified in the config file.");
              argsError = true;
            } else{
              controller.setConfig(config);
            }
          } catch (Exception ex) {
            printOut("Incorrect configuration file '" + xmlConfig + "'");
            argsError = true;
          }
        } else {
          printOut("You must specify the configuration file after '-configuration' option.");
          argsError = true;
        }
      } else if (arg.equals("-help")) {
        controller.displayHelp();
        exit();
      } else if (arg.equals("-v")) {
        controller.displayVersion();
        exit();
      } else if (arg.equals("-reportformat")) {
        if (idx + 1 < params.size()) {
          String formats = params.get(++idx);
          controller.setXml(formats.contains("xml"));
          controller.setJson(formats.contains("json"));
          controller.setHtml(formats.contains("html"));
          controller.setPdf(formats.contains("pdf"));
          String result = formats.replace("xml", "").replace("json", "").replace("html", "").replace("pdf", "").replace(",", "").replace("'", "");
          if (result.length() > 0) {
            printOut("Incorrect report formats.");
            argsError = true;
          } else{
            controller.setExplicitFormats(true);
          }
        } else {
          printOut("You must specify the formats after '-reportformat' option.");
          argsError = true;
        }
      } else if (arg.startsWith("-")) {
        // unknown option
        printOut("Unknown option: " + arg);
        argsError = true;
      } else {
        // File or directory to process
        String arg_mod = arg;
        if (!new File(arg_mod).isAbsolute() && !new File(arg_mod).exists() && new File("../" + arg_mod).exists()){
          arg_mod = "../" + arg;
        }
        files.add(arg_mod);
      }
      idx++;
    }

    if (files.size() == 0) {
      printOut("No files specified.");
      argsError = true;
    }
    if (argsError || params.size() == 0){
      controller.displayHelp();
    } else{
      // Everything OK!
      controller.setParameters(parameters);
      controller.setFiles(files);
      controller.run();
    }
  }

  private static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private void addDirectoryToFiles(ArrayList<String> files, File directory, int recursive, int currentlevel) {
    File[] listOfFiles = directory.listFiles();
    for (int j = 0; j < listOfFiles.length; j++) {
      if (listOfFiles[j].isFile()) {
        files.add(listOfFiles[j].getPath());
      } else if (listOfFiles[j].isDirectory() && currentlevel < recursive) {
        addDirectoryToFiles(files, listOfFiles[j], recursive, currentlevel + 1);
      }
    }
  }

  /**
   * Custom print lines
   */
  private void printOut(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private void printErr(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  private void printException(Exception ex){
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("An exception has occurred!", ex));
  }

  /**
   * Exit application
   */
  public void exit(){
    AppContext.close();
    System.exit(0);
  }

  /**
   * Finish control for test
   */
  public static boolean isFinished() {
    return finished;
  }
  public static void setFinished(boolean f) {
    finished = f;
  }

}
