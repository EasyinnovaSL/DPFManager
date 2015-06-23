package dpfmanager.shell.modules.interfaces;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.reader.BaselineProfile;
import com.easyinnova.tiff.reader.TiffEPProfile;
import com.easyinnova.tiff.reader.TiffReader;

import java.io.File;
import java.util.ArrayList;

/**
 * The Class CommandLine.
 */
public class CommandLine implements UserInterface {

  /** The args. */
  String[] args;

  /**
   * Instantiates a new command line.
   *
   * @param args the args
   */
  public CommandLine(String[] args) {
    this.args = args;
  }

  /**
   * Launch.
   */
  public void launch() {
    ArrayList<String> files = new ArrayList<String>();
    String outputFile = null;

    // Reads the parameters
    int idx = 0;
    boolean argsError = false;
    while (idx < args.length && !argsError) {
      String arg = args[idx];
      if (arg == "-o") {
        if (idx + 1 < args.length) {
          outputFile = args[++idx];
        } else {
          argsError = true;
        }
      } else if (arg == "-help") {
        displayHelp();
        break;
      } else if (arg.startsWith("-")) {
        // unknown option
        argsError = true;
      } else {
        // File or directory to process
        File file = new File(arg);
        if (file.isFile()) {
          files.add(arg);
        } else if (file.isDirectory()) {
          File[] listOfFiles = file.listFiles();
          for (int j = 0; j < listOfFiles.length; j++) {
            if (listOfFiles[j].isFile()) {
              files.add(listOfFiles[j].getPath());
            }
          }
        }
      }
      idx++;
    }
    if (argsError) {
      // Shows the program usage
      displayHelp();
    } else {
      // Process files
      for (final String filename : files) {
        System.out.println("Processing file " + filename);
        TiffReader tr = new TiffReader();
        int result = tr.readFile(filename);
        if (outputFile == null)
          reportResults(tr, result);
        else
          reportResultsXML(tr, result, outputFile);
      }
    }
    // System.out.println("Bye");
  }

  /**
   * Report the results of the reading process to the console.
   *
   * @param tiffReader the tiff reader
   * @param result the result
   */
  private static void reportResults(TiffReader tiffReader, int result) {
    String filename = tiffReader.getFilename();
    TiffDocument to = tiffReader.getModel();

    // Display results human readable
    switch (result) {
      case -1:
        System.out.println("File '" + filename + "' does not exist");
        break;
      case -2:
        System.out.println("IO Exception in file '" + filename + "'");
        break;
      case 0:
        if (tiffReader.getValidation().correct) {
          // The file is correct
          System.out.println("Everything ok in file '" + filename + "'");
          System.out.println("IFDs: " + to.getIfdCount());
          System.out.println("SubIFDs: " + to.getSubIfdCount());

          to.printMetadata();
          BaselineProfile bp = new BaselineProfile(to);
          bp.validate();
          bp.getValidation().printErrors();
          TiffEPProfile bpep = new TiffEPProfile(to);
          bpep.validate();
          bpep.getValidation().printErrors();

          int nifd = 1;
          for (TiffObject o : to.getImageIfds()) {
            IFD ifd = (IFD) o;
            if (ifd != null) {
              System.out.println("IFD " + nifd++ + " TAGS:");
              ifd.printTags();
            }
          }
          nifd = 1;
          for (TiffObject o : to.getSubIfds()) {
            IFD ifd = (IFD) o;
            if (ifd != null) {
              System.out.println("SubIFD " + nifd++ + " TAGS:");
              ifd.printTags();
            }
          }
        } else {
          // The file is not correct
          System.out.println("Errors in file '" + filename + "'");
          if (to != null) {
            System.out.println("IFDs: " + to.getIfdCount());
            System.out.println("SubIFDs: " + to.getSubIfdCount());

            // int index = 0;
            to.printMetadata();
            BaselineProfile bp = new BaselineProfile(to);
            bp.validate();
          }
          tiffReader.getValidation().printErrors();
        }
        tiffReader.getValidation().printWarnings();
        break;
      default:
        System.out.println("Unknown result (" + result + ") in file '" + filename + "'");
        break;
    }
  }

  /**
   * Report the results of the reading process to the console.
   *
   * @param tiffreader the tiff reader
   * @param result the result
   * @param xmlfile the xml file
   */
  private static void reportResultsXML(TiffReader tiffreader, int result, String xmlfile) {
    String filename = tiffreader.getFilename();
    TiffDocument to = tiffreader.getModel();

    // Display results human readable
    switch (result) {
      case -1:
        System.out.println("File '" + filename + "' does not exist");
        break;
      case -2:
        System.out.println("IO Exception in file '" + filename + "'");
        break;
      case 0:
        if (tiffreader.getValidation().correct) {
          // The file is correct
          System.out.println("Everything ok in file '" + filename + "'");
          System.out.println("IFDs: " + to.getIfdCount());
          System.out.println("SubIFDs: " + to.getSubIfdCount());

          to.printMetadata();
          BaselineProfile bp = new BaselineProfile(to);
          bp.validate();
          bp.getValidation().printErrors();
          TiffEPProfile bpep = new TiffEPProfile(to);
          bpep.validate();
          bpep.getValidation().printErrors();

          int nifd = 1;
          for (TiffObject o : to.getImageIfds()) {
            IFD ifd = (IFD) o;
            if (ifd != null) {
              System.out.println("IFD " + nifd++ + " TAGS:");
              ifd.printTags();
            }
          }
          nifd = 1;
          for (TiffObject o : to.getSubIfds()) {
            IFD ifd = (IFD) o;
            if (ifd != null) {
              System.out.println("SubIFD " + nifd++ + " TAGS:");
              ifd.printTags();
            }
          }
        } else {
          // The file is not correct
          System.out.println("Errors in file '" + filename + "'");
          if (to != null) {
            System.out.println("IFDs: " + to.getIfdCount());
            System.out.println("SubIFDs: " + to.getSubIfdCount());

            // int index = 0;
            to.printMetadata();
            BaselineProfile bp = new BaselineProfile(to);
            bp.validate();
          }
          tiffreader.getValidation().printErrors();
        }
        tiffreader.getValidation().printWarnings();
        break;
      default:
        System.out.println("Unknown result (" + result + ") in file '" + filename + "'");
        break;
    }
  }

  /**
   * Shows program usage.
   */
  static void displayHelp() {
    System.out.println("Usage: dpfmanager [options] <file1> <file2> ... <fileN>");
    System.out.println("Options: -help displays help");
  }
}
