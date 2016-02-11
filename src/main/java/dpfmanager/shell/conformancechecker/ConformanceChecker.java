package dpfmanager.shell.conformancechecker;

import dpfmanager.shell.reporting.IndividualReport;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;

import java.util.ArrayList;

/**
 * Created by Victor Muñoz on 10/02/2016.
 */
public interface ConformanceChecker {
  ArrayList<String> getConformanceCheckerExtensions();

  ArrayList<String> getConformanceCheckerStandards();

  ArrayList<Field> getConformanceCheckerFields();

  boolean acceptsFile(String filename);

  IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder,  Configuration config,
                                          int idReport) throws ReadTagsIOException, ReadIccConfigIOException;
}
