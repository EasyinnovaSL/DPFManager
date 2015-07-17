package dpfmanager.shell.modules.interfaces;


import dpfmanager.ReportGenerator;
import javafx.application.Application.Parameters;

import com.easyinnova.conformancechecker.TiffConformanceChecker;
import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.profiles.BaselineProfile;
import com.easyinnova.tiff.profiles.TiffEPProfile;
import com.easyinnova.tiff.reader.TiffReader;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The Class CommandLine.
 */
public class CommandLine implements UserInterface {

  /**
   * The args.
   */
  List<String> args;

  /**
   * Instantiates a new command line.
   *
   * @param args the args
   */
  public CommandLine(Parameters args) {
    this.args = args.getRaw();
  }

  /**
   * Launch.
   */
  public void launch() {
    ArrayList<String> files = new ArrayList<String>();
    String outputFile = null;

    readConformanceChecker();

    // Reads the parameters
    int idx = 0;
    boolean argsError = false;
    while (idx < args.size() && !argsError) {
      String arg = args.get(idx);
      if (arg.equals("-o")) {
        if (idx + 1 < args.size()) {
          outputFile = args.get(++idx);
        } else {
          argsError = true;
        }
      } else if (arg.equals("-help")) {
        displayHelp();
        break;
      } else if (arg.startsWith("-")) {
        // unknown option
        argsError = true;
      } else {
        // File or directory to process
        File file = new File(arg);
        if (file.isDirectory()) {
          File[] listOfFiles = file.listFiles();
          for (int j = 0; j < listOfFiles.length; j++) {
            if (listOfFiles[j].isFile()) {
              files.add(listOfFiles[j].getPath());
            }
          }
        } else {
          files.add(arg);
        }
      }
      idx++;
    }
    if (argsError) {
      // Shows the program usage
      displayHelp();
    } else {
      // Process files
      String internalReportFolder = ReportGenerator.createReportPath();
      for (final String filename : files) {
        System.out.println("Processing file " + filename);
        processFile(filename, outputFile, internalReportFolder);
      }
      ReportGenerator.makeSummaryReport(internalReportFolder);
    }
  }

  /**
   * Load xml from string.
   *
   * @param xml the xml
   * @return the document
   * @throws Exception the exception
   */
  private Document loadXmlFromString(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(xml));
    return builder.parse(is);
  }

  /**
   * Read conformance checker.
   */
  private void readConformanceChecker() {
    String xml = TiffConformanceChecker.getConformanceCheckerOptions();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setNamespaceAware(true);
    try {
      Document doc = loadXmlFromString(xml);

      NodeList name = doc.getElementsByTagName("name");
      if (name != null && name.getLength() > 0) {
        NodeList subList = name.item(0).getChildNodes();
        if (subList != null && subList.getLength() > 0) {
          System.out.println("Conformance checker: " + subList.item(0).getNodeValue());
        }
      }

      System.out.print("Extensions: ");
      NodeList extensions = doc.getElementsByTagName("extension");
      if (extensions != null && extensions.getLength() > 0) {
        for (int i = 0; i < extensions.getLength(); i++) {
          NodeList subList = extensions.item(i).getChildNodes();
          if (subList != null && subList.getLength() > 0) {
            if (i > 0) {
              System.out.print(", ");
            }
            System.out.print(subList.item(0).getNodeValue());
          }
        }
      }
      System.out.println();

      NodeList standards = doc.getElementsByTagName("standard");
      if (standards != null && standards.getLength() > 0) {
        for (int i = 0; i < standards.getLength(); i++) {
          NodeList nodes = standards.item(i).getChildNodes();
          String stdName = "";
          String desc = "";
          for (int j = 0; j < nodes.getLength(); j++) {
            if (nodes.item(j).getNodeName().equals("name")) {
              stdName = nodes.item(j).getTextContent();
            } else if (nodes.item(j).getNodeName().equals("description")) {
              desc = nodes.item(j).getTextContent();
            }
          }
          System.out.println("Standard: " + stdName + " (" + desc + ")");
        }
      }

    } catch (Exception e) {
      System.out.println("Failed communication with conformance checker: " + e.getMessage());
    }
  }

  /**
   * Process a Tiff file.
   *
   * @param filename             the filename
   * @param outputFile           the output file
   * @param internalReportFolder the internal report folder
   */
  private void processFile(String filename, String outputFile, String internalReportFolder) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);
      if (outputFile == null) {
        reportResults(tr, result);
      } else {
        reportResultsXml(tr, result, outputFile);
      }
      internalReport(tr, result, internalReportFolder);
    } catch (ReadTagsIOException e) {
      System.out.println("Error loading Tiff library dependencies");
    } catch (ReadIccConfigIOException e) {
      System.out.println("Error loading Tiff library dependencies");
    }
  }

  /**
   * Report the results of the reading process to the console.
   *
   * @param tiffReader the tiff reader
   * @param result     the result
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
   * @param result     the result
   * @param xmlfile    the xml file
   */
  private static void reportResultsXml(TiffReader tiffreader, int result, String xmlfile) {
    String filename = tiffreader.getFilename();
    TiffDocument to = tiffreader.getModel();

    // Save results into XML file
    switch (result) {
      case -1:
        System.out.println("File '" + filename + "' does not exist");
        break;
      case -2:
        System.out.println("IO Exception in file '" + filename + "'");
        break;
      case 0:
        ValidationResult val = tiffreader.getValidation();
        ReportGenerator rg = new ReportGenerator(to, val);
        rg.generateXml(xmlfile);
        System.out.println("Report '" + xmlfile + "' created");
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
   * @param result     the result
   * @param folder     the internal report folder
   */
  private static void internalReport(TiffReader tiffreader, int result, String folder) {
    String filename = tiffreader.getFilename();
    TiffDocument to = tiffreader.getModel();

    // Save results into XML file
    switch (result) {
      case -1:
        System.out.println("File '" + filename + "' does not exist");
        break;
      case -2:
        System.out.println("IO Exception in file '" + filename + "'");
        break;
      case 0:
        ValidationResult val = tiffreader.getValidation();
        ReportGenerator rg = new ReportGenerator(to, val);
        String reportName = ReportGenerator.getReportName(folder, tiffreader);
        rg.generateXml(reportName);
        System.out.println("Internal report '" + reportName + "' created");
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
