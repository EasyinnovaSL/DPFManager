/**
 * <h1>ReportGenerator.java</h1>
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Easy Innova
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.interfaces;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.ValidationResult;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.profiles.BaselineProfile;
import com.easyinnova.tiff.profiles.TiffEPProfile;
import com.easyinnova.tiff.reader.TiffReader;

import dpfmanager.IndividualReport;
import dpfmanager.ReportGenerator;
import dpfmanager.shell.modules.conformancechecker.TiffConformanceChecker;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

  /** The allowed extensions. */
  List<String> allowedExtensions;

  /** The Reports Generator. */
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
    String outputFile = null;

    readConformanceChecker();
    boolean xml = true;
    boolean json = false;
    boolean html = false;

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
      } else if (arg.equals("-reportformat")) {
        if (idx + 1 < args.size()) {
          String formats = args.get(++idx);
          xml = formats.contains("xml");
          json = formats.contains("json");
          html = formats.contains("html");
        } else {
          argsError = true;
        }
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
      reportGenerator = new ReportGenerator();
      reportGenerator.setReportsFormats(xml, json, html);
      // Process files
      ArrayList<IndividualReport> individuals = new ArrayList<IndividualReport>();
      String internalReportFolder = ReportGenerator.createReportPath();
      for (final String filename : files) {
        System.out.println("");
        System.out.println("Processing file " + filename);
        List<IndividualReport> indReports = processFile(filename, outputFile, internalReportFolder);
        if (indReports.size() > 0) {
          individuals.addAll(indReports);
        }
      }
      // Global report
      String summaryXmlFile = reportGenerator.makeSummaryReport(internalReportFolder, individuals);

      // Send report over FTP (only for alpha testing)
      try {
        //sendFtp(reportGenerator, summaryXmlFile);
        //startFTP(summaryXmlFile, "summary.xml");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Send ftp.
   *
   * @param reportGenerator the report generator
   * @param summaryXmlFile the summary xml file
   */
  private void sendFtp(ReportGenerator reportGenerator, String summaryXmlFile) throws NoSuchAlgorithmException {
    String sFTP = "84.88.145.109";
    String sUser = "preformaapp";
    String sPassword = "2.eX#lh>";
    FTPSClient client = new FTPSClient();

    FileInputStream fis = null;

    try {
      client.connect(sFTP);
      client.login(sUser, sPassword);
      fis = new FileInputStream(summaryXmlFile);
      client.storeFile(summaryXmlFile, fis);
      client.logout();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
        client.disconnect();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public boolean startFTP(String summaryXmlFile, String fileToFTP){
    StandardFileSystemManager manager = new StandardFileSystemManager();

    try {

      String serverAddress = "84.88.145.109";
      String userId = "preformaapp";
      String password = "2.eX#lh>";
      String remoteDirectory = "";

      //check if the file exists
      String filepath = summaryXmlFile;
      File file = new File(filepath);
      if (!file.exists())
        throw new RuntimeException("Error. Local file not found");

      //Initializes the file manager
      manager.init();

      //Setup our SFTP configuration
      FileSystemOptions opts = new FileSystemOptions();
      SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
          opts, "no");
      SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
      SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

      //Create the SFTP URI using the host name, userid, password,  remote path and file name
      String sftpUri = "sftp://" + userId + ":" + password +  "@" + serverAddress + "/" +
          remoteDirectory + fileToFTP;

      // Create local file object
      FileObject localFile = manager.resolveFile(file.getAbsolutePath());

      // Create remote file object
      FileObject remoteFile = manager.resolveFile(sftpUri, opts);

      // Copy local file to sftp server
      remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
      System.out.println("File upload successful");

    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    finally {
      manager.close();
    }

    return true;
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
      System.out.println("Failed communication with conformance checker: " + e.getMessage());
    }
  }

  /**
   * Process a Tiff file.
   *
   * @param filename the filename
   * @param outputFile the output file
   * @param internalReportFolder the internal report folder
   * @return the list
   */
  private List<IndividualReport> processFile(String filename, String outputFile,
      String internalReportFolder) {
    List<IndividualReport> indReports = new ArrayList<IndividualReport>();
    IndividualReport ir = null;
    if (filename.toLowerCase().endsWith(".zip") || filename.toLowerCase().endsWith(".rar")) {
      // Zip File
      try {
        ZipFile zipFile = new ZipFile(filename);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
          ZipEntry entry = entries.nextElement();
          if (isTiff(entry.getName())) {
            InputStream stream = zipFile.getInputStream(entry);
            String filename2 = createTempFile(entry.getName(), stream);
            ir = processTiffFile(filename2, filename2, outputFile, internalReportFolder);
            if (ir != null) {
              indReports.add(ir);
            }
            File file = new File(filename2);
            file.delete();
          }
        }
        zipFile.close();
      } catch (Exception ex) {
        System.out.println("Error reading zip file");
      }
    } else if (isUrl(filename)) {
      // URL
      try {
        InputStream input = new java.net.URL(filename).openStream();
        String filename2 = createTempFile(filename, input);
        filename = java.net.URLDecoder.decode(filename, "UTF-8");
        ir = processTiffFile(filename2, filename, outputFile, internalReportFolder);
        if (ir != null) {
          indReports.add(ir);
        }
        File file = new File(filename2);
        file.delete();
      } catch (Exception ex) {
        System.out.println("Error in URL " + filename);
      }
    } else if (isTiff(filename)) {
      // File
      try {
        ir = processTiffFile(filename, filename, outputFile, internalReportFolder);
        if (ir != null) {
          indReports.add(ir);
        }
      } catch (Exception ex) {
        System.out.println("Error in File " + filename);
      }
    } else {
      // Anything else
      System.out.println("File " + filename + " is not Tiff");
    }
    return indReports;
  }

  /**
   * Creates the temp file.
   *
   * @param name the name
   * @param stream the stream
   * @return the string
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private String createTempFile(String name, InputStream stream) throws IOException {
    String filename2 = name + "2";
    if (filename2.contains("/")) {
      filename2 = filename2.substring(filename2.lastIndexOf("/") + 1);
    }
    while (new File(filename2).isFile()) {
      filename2 += "x";
    }
    File targetFile = new File(filename2);
    OutputStream outStream = new FileOutputStream(targetFile);
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = stream.read(buffer)) != -1) {
      outStream.write(buffer, 0, bytesRead);
    }
    outStream.close();
    return filename2;
  }

  /**
   * Checks if is url.
   *
   * @param filename the filename
   * @return true, if is url
   */
  private boolean isUrl(String filename) {
    boolean ok = true;
    try {
      new java.net.URL(filename);
    } catch (Exception ex) {
      ok = false;
    }
    return ok;
  }

  /**
   * Checks if is tiff.
   *
   * @param filename the filename
   * @return true, if is tiff
   */
  private boolean isTiff(String filename) {
    boolean isTiff = false;
    for (String extension : allowedExtensions) {
      if (filename.toLowerCase().endsWith(extension.toLowerCase())) {
        isTiff = true;
      }
    }
    return isTiff;
  }

  /**
   * Process tiff file.
   *
   * @param filename the filename
   * @param realFilename the real filename
   * @param outputFile the output file
   * @param internalReportFolder the internal report folder
   * @return the individual report
   * @throws ReadTagsIOException the read tags io exception
   * @throws ReadIccConfigIOException the read icc config io exception
   */
  private IndividualReport processTiffFile(String filename, String realFilename, String outputFile,
      String internalReportFolder) throws ReadTagsIOException, ReadIccConfigIOException {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);
      switch (result) {
        case -1:
          System.out.println("File '" + filename + "' does not exist");
          break;
        case -2:
          System.out.println("IO Exception in file '" + filename + "'");
          break;
        case 0:
          TiffDocument to = tr.getModel();
          ValidationResult val = tr.getValidation();
          // String name = tr.getFilename()
          // .substring(tr.getFilename().lastIndexOf(File.separator) + 1,
          // tr.getFilename().length());
          String name =
              realFilename.substring(realFilename.lastIndexOf(File.separator) + 1,
                  realFilename.length());
          IndividualReport ir = new IndividualReport(name, filename, to, val);
          if (outputFile == null) {
            reportResults(name, to, val);
          } else {
            reportResultsXml(ir, outputFile);
          }
          internalReport(ir, tr, realFilename, internalReportFolder);
          return ir;
        default:
          System.out.println("Unknown result (" + result + ") in file '" + filename + "'");
          break;
      }
    } catch (ReadTagsIOException e) {
      System.out.println("Error loading Tiff library dependencies");
    } catch (ReadIccConfigIOException e) {
      System.out.println("Error loading Tiff library dependencies");
    }
    return null;
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
      val.printErrors();
    }
    val.printWarnings();
  }

  /**
   * Report the results of the reading process to the console.
   *
   * @param ir the ir
   * @param outputFile the output file
   */
  private void reportResultsXml(IndividualReport ir, String outputFile) {
    reportGenerator.generateIndividualReport(outputFile, ir);
    System.out.println("Report '" + outputFile + "' created");
  }

  /**
   * Report the results of the reading process to the console.
   *
   * @param ir the individual report
   * @param tiffreader the tiff reader
   * @param realFilename the real filename
   * @param folder the internal report folder
   */
  private void internalReport(IndividualReport ir, TiffReader tiffreader, String realFilename,
      String folder) {
    String outputfile = ReportGenerator.getReportName(folder, realFilename);
    reportGenerator.generateIndividualReport(outputfile, ir);
    System.out.println("Internal report '" + outputfile + "' created");
  }

  /**
   * Shows program usage.
   */
  static void displayHelp() {
    System.out.println("Usage: dpfmanager [options] <file1> <file2> ... <fileN>");
    System.out.println("Options: -help displays help");
    System.out.println("         -reportformat xml,json,html (default: xml)");
  }
}
