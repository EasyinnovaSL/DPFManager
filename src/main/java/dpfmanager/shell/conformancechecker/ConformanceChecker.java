package dpfmanager.shell.conformancechecker;

import dpfmanager.shell.reporting.IndividualReport;
import dpfmanager.shell.reporting.ReportGenerator;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by easy on 10/02/2016.
 */
public interface ConformanceChecker {
  ArrayList<String> getConformanceCheckerExtensions();

  ArrayList<String> getConformanceCheckerStandards();

  ArrayList<Field> getConformanceCheckerFields();

  boolean acceptsFile(String filename);

  IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder,  Configuration config,
                                          int idReport) throws ReadTagsIOException, ReadIccConfigIOException;
}
