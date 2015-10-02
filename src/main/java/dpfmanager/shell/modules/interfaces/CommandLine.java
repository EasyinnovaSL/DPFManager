/**
 * <h1>CommandLine.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Easy Innova
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.interfaces;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.profiles.BaselineProfile;
import com.easyinnova.tiff.profiles.TiffEPProfile;

import dpfmanager.ReportGenerator;
import dpfmanager.shell.modules.ProcessInput;
import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application.Parameters;

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
   * The allowed extensions.
   */
  List<String> allowedExtensions;

  /**
   * The Reports Generator.
   */
  ReportGenerator reportGenerator;

  /**
   * Instantiates a new command line.
   *
   * @param args the args
   */
  public CommandLine(Parameters args) {
    this.args = args.getRaw();
    allowedExtensions = new ArrayList<String>();
  }

  /**
   * Launch.
   */
  public void launch() {
    ArrayList<String> files = new ArrayList<String>();
    String outputFolder = null;

    boolean xml = true;
    boolean json = true;
    boolean html = true;
    boolean silence = false;

    List<String> params = new ArrayList<String>();
    for (String s : args) {
      params.add(s);
    }
    if (params.size() == 0) {
      params.add(".");
    }

    // Reads the parameters
    int idx = 0;
    boolean argsError = false;
    while (idx < params.size() && !argsError) {
      String arg = params.get(idx);
      if (arg.equals("-o")) {
        if (idx + 1 < params.size()) {
          outputFolder = params.get(++idx);
          File tmp = new File(outputFolder);
          if (!tmp.exists()) {
            if (!tmp.mkdir()) {
              System.out.println("Cannot create the output folder. Ignoring ouput folder.");
              outputFolder = null;
            }
          } else if (!tmp.isDirectory()) {
            System.out.println("The output path must be a directory. Ignoring output folder.");
            outputFolder = null;
          } else if (tmp.listFiles().length > 0) {
            System.out.println("The output folder must be empty. Ignoring ouput folder.");
            outputFolder = null;
          }
        } else {
          argsError = true;
        }
      } else if (arg.equals("-s")) {
        silence = true;
      } else if (arg.equals("-help")) {
        argsError = true;
        break;
      } else if (arg.equals("-reportformat")) {
        if (idx + 1 < params.size()) {
          String formats = params.get(++idx);
          xml = formats.contains("xml");
          json = formats.contains("json");
          html = formats.contains("html");
          String result =
              formats.replace("xml", "").replace("json", "").replace("html", "").replace(",", "");
          if (result.length() > 0) {
            System.out.println("Incorrect report formats");
            argsError = true;
          }
        } else {
          argsError = true;
        }
      } else if (arg.startsWith("-")) {
        // unknown option
        argsError = true;
      } else {
        // File or directory to process
        String arg_mod = arg;
        if (!new File(arg_mod).isAbsolute() && !new File(arg_mod).exists() && new File("../" + arg_mod).exists()) arg_mod = "../" + arg;
        File file = new File(arg_mod);
        if (file.isDirectory()) {
          File[] listOfFiles = file.listFiles();
          for (int j = 0; j < listOfFiles.length; j++) {
            if (listOfFiles[j].isFile()) {
              files.add(listOfFiles[j].getPath());
            }
          }
        } else {
          files.add(arg_mod);
        }
      }
      idx++;
    }

    if (argsError) {
      // Shows the program usage
      displayHelp();
    } else if (files.size() == 0) {
      System.out.println("No files specified.");
    } else {
      readConformanceChecker();

      ProcessInput pi = new ProcessInput(allowedExtensions, true, true, true);
      pi.ProcessFiles(files, xml, json, html, outputFolder, silence);
      System.out.println("Report generated successfully.");
    }
  }

  /**
   * Load XML from string.
   *
   * @param xml the XML
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
            allowedExtensions.add(subList.item(0).getNodeValue());
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
      System.err.println("Failed communication with conformance checker: " + e.getMessage());
    }
  }

  /**
   * Report the results of the reading process to the console.
   *
   * @param filename the filename
   * @param to the to
   * @param val the val
   */
  private static void reportResults(String filename, TiffDocument to, ValidationResult val) {
    // Display results human readable
    if (val.correct) {
      // The file is correct
      System.out.println("Everything ok in file '" + filename + "'");
      System.out.println("IFDs: " + to.getIfdCount());
      System.out.println("SubIFDs: " + to.getSubIfdCount());

      // to.printMetadata();
      BaselineProfile bp = new BaselineProfile(to);
      bp.validate();
      TiffEPProfile bpep = new TiffEPProfile(to);
      bpep.validate();

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
        // to.printMetadata();
        BaselineProfile bp = new BaselineProfile(to);
        bp.validate();
        TiffEPProfile bpep = new TiffEPProfile(to);
        bpep.validate();
      }
      val.printErrors();
    }
    val.printWarnings();
  }

  /**
   * Shows program usage.
   */
  static void displayHelp() {
    System.out.println("Usage: dpfmanager [options] <file1> <file2> ... <fileN>");
    System.out.println("Options: -help displays help");
    System.out.println("         -o path: Specifies the report output folder.");
    // System.out.println("         -gui: Launches graphical interface");
    System.out.println("         -reportformat (xml, json or html): "
        + "Specifies the report format. Default set to all reports.");
  }
}
