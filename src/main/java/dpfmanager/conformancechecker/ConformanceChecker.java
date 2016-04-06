package dpfmanager.conformancechecker;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.shell.modules.report.core.IndividualReport;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;

import java.util.ArrayList;

/**
 * Created by Victor Muñoz on 10/02/2016.
 */
public abstract class ConformanceChecker {

  protected static DpfLogger logger;

  abstract public ArrayList<String> getConformanceCheckerExtensions();

  abstract public ArrayList<String> getConformanceCheckerStandards();

  abstract public ArrayList<Field> getConformanceCheckerFields();

  abstract public boolean acceptsFile(String filename);

  abstract public IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder,  Configuration config,
                                          int idReport) throws ReadTagsIOException, ReadIccConfigIOException;

  public void setLogger(DpfLogger log){
    logger = log;
  }

}
