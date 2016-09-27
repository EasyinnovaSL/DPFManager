/**
 * <h1>ExternalConformanceChecker.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.external;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.shell.modules.report.core.IndividualReport;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by easy on 31/03/2016.
 */
public class ExternalConformanceChecker extends ConformanceChecker {
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
   * @param pathToFile           the path in local disk to the file
   * @param reportFilename       the file name that will be displayed in the report
   * @param internalReportFolder the internal report folder
   * @return the individual report
   * @throws ReadTagsIOException      the read tags io exception
   * @throws ReadIccConfigIOException the read icc config io exception
   */
  public IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder, Configuration config, int id) throws ReadTagsIOException, ReadIccConfigIOException {
    try {
      ArrayList<String> params = new ArrayList();
      params.add(exePath);
      params.addAll(this.params);
      params.add(pathToFile);
      Process process = new ProcessBuilder(params).start();
      InputStream is = process.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line;
      String report = "";
      while ((line = br.readLine()) != null) {
        report += line + "\r\n";
      }
      String pathNorm = reportFilename.replaceAll("\\\\", "/");
      String name = pathNorm.substring(pathNorm.lastIndexOf("/") + 1);
      IndividualReport ir = new IndividualReport(name, pathToFile, reportFilename);
      while (report.indexOf("<?xml", report.indexOf("<?xml") + 1) > -1) {
        report = report.substring(report.indexOf("<?xml", report.indexOf("<?xml") + 1));
      }
      ir.setConformanceCheckerReport(report);

      return ir;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String toString() {
    return "ExternalConformanceChecker";
  }
}
