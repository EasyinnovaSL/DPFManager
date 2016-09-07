package dpfmanager.shell.application.launcher.noui;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.interfaces.console.AppContext;
import dpfmanager.shell.interfaces.console.ConsoleController;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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

  /**
   * The resource bundle
   */
  private ResourceBundle bundle;

  /**
   * The parsed args
   */
  private Map<String, String> parameters;

  public ConsoleLauncher(String[] args) {
    DPFManagerProperties.setFinished(false);
    // Program input
    params = new ArrayList(Arrays.asList(args));
    // Update locale
    updateLanguage();
    // Load spring context
    AppContext.loadContext("DpfSpringConsole.xml");
    parameters = (Map<String, String>) AppContext.getApplicationContext().getBean("parameters");
    parameters.put("mode", "CMD");
    //Load DpfContext
    context = new ConsoleContext(AppContext.getApplicationContext());
    // The main controller
    controller = new ConsoleController(context,bundle);
  }

  /**
   * Update the app language.
   */
  private void updateLanguage(){
    // Check if language is specified
    if (params.contains("-l")){
      int langIndex = params.indexOf("-l") +1;
      String language = params.get(langIndex);
      Locale newLocale = new Locale(language);
      if (newLocale != null){
        DPFManagerProperties.setLanguage(language);
      }
    }
    Locale.setDefault(new Locale(DPFManagerProperties.getLanguage()));
    bundle = DPFManagerProperties.getBundle();
  }

  /**
   * Launch.
   */
  public void launch() {
    // Reads the parameters
    int idx = 0;
    boolean argsError = false;
    ArrayList<String> files = new ArrayList<>();
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      if (arg.equals("-o") || arg.equals("--output")) {
        if (idx + 1 < params.size()) {
          String outputFolder = params.get(++idx);
          File tmp = new File(outputFolder);
          if (!tmp.exists()) {
            if (!tmp.mkdirs()) {
              printOut(bundle.getString("cannotOutput"));
              outputFolder = null;
            }
          } else if (!tmp.isDirectory()) {
            printOut(bundle.getString("outputMustDirectory"));
            outputFolder = null;
          } else if (tmp.listFiles().length > 0) {
            printOut(bundle.getString("outputMustEmpty"));
            outputFolder = null;
          }
          if (outputFolder != null) {
            controller.setOutputFolder(outputFolder);
            controller.setExplicitOutput(true);
            parameters.put("-o",outputFolder);
          } else {
            argsError = true;
          }
        } else {
          printOut(bundle.getString("outputSpecify"));
          argsError = true;
        }
      } else if (arg.equals("-s")) {
        parameters.put("-s", "true");
      } else if (arg.equals("-w")) {
        parameters.put("-w", "true");
      } else if (arg.equals("-r")) {
        Integer max = Integer.MAX_VALUE;
        parameters.put("-r", max.toString());
      } else if (arg.startsWith("-r") && isNumeric(arg.substring(2))) {
        parameters.put("-r", arg.substring(2));
      } else if (arg.equals("-configuration")) {
        if (idx + 1 < params.size()) {
          String xmlConfig = params.get(++idx);
          Configuration config = new Configuration();
          try {
            config.ReadFile(xmlConfig);
            if (config.getFormats().size() == 0) {
              printOut(bundle.getString("missingReportFormat"));
              argsError = true;
            } else {
              controller.setConfig(config);
              parameters.put("-configuration",xmlConfig);
            }
          } catch (Exception ex) {
            printOut(bundle.getString("incorrectConfigFile").replace("%1",xmlConfig));
            argsError = true;
          }
        } else {
          printOut(bundle.getString("specifyConfig"));
          argsError = true;
        }
      } else if (arg.equals("-h") || arg.equals("--help")) {
        controller.displayHelp();
        exit();
      } else if (arg.equals("-v") || arg.equals("--version")) {
        controller.displayVersion();
        exit();
      } else if (arg.equals("-f") || arg.equals("--reportformat")) {
        if (idx + 1 < params.size()) {
          String formats = params.get(++idx);
          controller.setXml(formats.contains("xml"));
          controller.setJson(formats.contains("json"));
          controller.setHtml(formats.contains("html"));
          controller.setPdf(formats.contains("pdf"));
          String result = formats.replace("xml", "").replace("json", "").replace("html", "").replace("pdf", "").replace(",", "").replace("'", "");
          if (result.length() > 0) {
            printOut(bundle.getString("incorrectReportFormat"));
            argsError = true;
          } else {
            controller.setExplicitFormats(true);
          }
        } else {
          printOut(bundle.getString("specifyFormat"));
          argsError = true;
        }
      } else if (arg.equals("-job")) {
        if (idx + 1 < params.size()) {
          String jobId = params.get(++idx);
          parameters.put("-job",jobId);
        } else {
          printOut(bundle.getString("specifyJob"));
          argsError = true;
        }
      } else if (arg.equals("-url")) {
        if (idx + 1 < params.size()) {
          String url = params.get(++idx);
          if (!url.startsWith("http://")){
            url = "http://" + url;
          }
          parameters.put("-url",url);
        } else {
          printOut(bundle.getString("specifyUrl"));
          argsError = true;
        }
      } else if (arg.equals("-l") || arg.equals("--language")) {
        // nothing, checked before
      } else if (arg.equals("-listperiodic")) {
        if (parameters.containsKey("-addperiodic") || parameters.containsKey("-editperiodic") || parameters.containsKey("-removeperiodic")){
          printOut(bundle.getString("onlyOnePeriodicAction"));
          argsError = true;
        } else {
          parameters.put("-listperiodic", "");
        }
      } else if (arg.equals("-addperiodic")) {
        if (parameters.containsKey("-listperiodic") || parameters.containsKey("-editperiodic") || parameters.containsKey("-removeperiodic")){
          printOut(bundle.getString("onlyOnePeriodicAction"));
          argsError = true;
        } else {
          parameters.put("-addperiodic", "");
        }
      } else if (arg.equals("-editperiodic")) {
        if (parameters.containsKey("-listperiodic") || parameters.containsKey("-addperiodic") || parameters.containsKey("-removeperiodic")){
          printOut(bundle.getString("onlyOnePeriodicAction"));
          argsError = true;
        } else {
          if (idx + 1 < params.size()) {
            String uuid = params.get(++idx);
            parameters.put("-editperiodic", uuid);
          } else {
            printOut(bundle.getString("specifyPeriodicId"));
            argsError = true;
          }
        }
      } else if (arg.equals("-removeperiodic")) {
        if (parameters.containsKey("-listperiodic") || parameters.containsKey("-editperiodic") || parameters.containsKey("-addperiodic")){
          printOut(bundle.getString("onlyOnePeriodicAction"));
          argsError = true;
        } else {
          if (idx + 1 < params.size()) {
            String uuid = params.get(++idx);
            parameters.put("-removeperiodic", uuid);
          } else {
            printOut(bundle.getString("specifyPeriodicId"));
            argsError = true;
          }
        }
      } else if (arg.equals("-time")) {
        if (idx + 1 < params.size()) {
          String time = params.get(++idx);
          parameters.put("-time", time);
        } else {
          printOut(bundle.getString("specifyTime"));
          argsError = true;
        }
      } else if (arg.equals("-periodicity")) {
        // mode
        String mode = "";
        if (idx + 1 < params.size()) {
          mode = params.get(++idx);
          parameters.put("-periodicity", mode);
        } else {
          printOut(bundle.getString("specifyPeriodicityMode"));
          argsError = true;
        }
        // extra info (weekly & monthly)
        if (mode.equals("weekly") || mode.equals("monthly")){
          if (idx + 1 < params.size()) {
            String extra = params.get(++idx);
            parameters.put("-extra", extra);
          } else {
            printOut(bundle.getString("specifyPeriodicityExtra"));
            argsError = true;
          }
        }
      } else if (arg.startsWith("-")) {
        // unknown option
        printOut(bundle.getString("unknownOption").replace("%1",arg));
        argsError = true;
      } else {
        // File or directory to process
        String arg_mod = arg;
        if (!new File(arg_mod).isAbsolute() && !new File(arg_mod).exists() && new File("../" + arg_mod).exists()) {
          arg_mod = "../" + arg;
        }
        files.add(arg_mod);
      }
      idx++;
    }

    // Job needs url
    if (parameters.containsKey("-job") && !parameters.containsKey("-url")){
      printOut(bundle.getString("jobNeedUrl"));
      argsError = true;
    }

    if (files.size() == 0 && !parameters.containsKey("-job") && !parameters.containsKey("-listperiodic") && !parameters.containsKey("-removeperiodic")) {
      printOut(bundle.getString("noFilesSpecified"));
      argsError = true;
    }
    if (argsError || params.size() == 0) {
      controller.displayHelp();
    } else {
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

  /**
   * Custom print lines
   */
  private void printOut(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  private void printErr(String message) {
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  private void printException(Exception ex) {
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("exception"), ex));
  }

  /**
   * Exit application
   */
  public void exit() {
    AppContext.close();
    System.exit(0);
  }

}
