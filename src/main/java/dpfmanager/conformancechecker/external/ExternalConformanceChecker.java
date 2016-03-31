package dpfmanager.conformancechecker.external;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.conformancechecker.tiff.implementation_checker.Validator;
import dpfmanager.shell.modules.report.IndividualReport;
import dpfmanager.shell.modules.report.ReportGenerator;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;
import com.easyinnova.tiff.reader.TiffReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by easy on 31/03/2016.
 */
public class ExternalConformanceChecker implements ConformanceChecker {
  ArrayList<String> standards;
  ArrayList<String> extensions;
  String exePath;
  ArrayList<String> params;

  public ExternalConformanceChecker(String exePath, ArrayList<String> params, ArrayList<String> standards, ArrayList<String> extensions) {
    this.standards = standards;
    this.extensions = extensions;
    this.exePath = exePath;
    this.params = params;
  }

  public ArrayList<String> getConformanceCheckerStandards() {
    return standards;
  }

  public ArrayList<String> getConformanceCheckerExtensions() {
    return extensions;
  }

  public ArrayList<Field> getConformanceCheckerFields() {
    ArrayList<Field> fields = new ArrayList<Field>();
    return fields;
  }

  /**
   * Checks if is tiff.
   *
   * @param filename the filename
   * @return true, if is tiff
   */
  public boolean acceptsFile(String filename) {
    boolean isAccepted = false;
    for (String extension : extensions) {
      if (filename.toLowerCase().endsWith(extension.toLowerCase())) {
        isAccepted = true;
      }
    }
    return isAccepted;
  }

  /**
   * Process tiff file.
   *
   * @param pathToFile the path in local disk to the file
   * @param reportFilename the file name that will be displayed in the report
   * @param internalReportFolder the internal report folder
   * @return the individual report
   * @throws ReadTagsIOException the read tags io exception
   * @throws ReadIccConfigIOException the read icc config io exception
   */
  public IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder, Configuration config,
                                      int idReport) throws ReadTagsIOException, ReadIccConfigIOException {
    try {
      ArrayList<String> params = new ArrayList();
      params.add(exePath);
      params.addAll(this.params);
      params.add(pathToFile);
      Process process = new ProcessBuilder(params).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String outputfile = ReportGenerator.getReportName(internalReportFolder, reportFilename, idReport);
      String xmlFileStr = outputfile + ".xml";
      PrintWriter pw = new PrintWriter(xmlFileStr);
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
        pw.write(line);
      }
      pw.close();
      String pathNorm = reportFilename.replaceAll("\\\\", "/");
      String name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
      IndividualReport ir = new IndividualReport(name, pathToFile);
      return ir;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
