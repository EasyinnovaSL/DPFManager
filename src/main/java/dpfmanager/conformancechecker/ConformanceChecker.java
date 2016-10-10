/**
 * <h1>ConformanceChecker.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.conformancechecker;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.configuration.Field;
import dpfmanager.shell.modules.interoperability.core.ConformanceConfig;
import dpfmanager.shell.modules.report.core.IndividualReport;

import com.easyinnova.tiff.model.ReadIccConfigIOException;
import com.easyinnova.tiff.model.ReadTagsIOException;

import java.util.List;

/**
 * Created by Victor Muñoz on 10/02/2016.
 */
public abstract class ConformanceChecker {

  public static DpfLogger Logger = initDefault();
  private ConformanceConfig config;

//  abstract public List<String> getConformanceCheckerExtensions();

  abstract public List<String> getConformanceCheckerStandards();

  abstract public List<Field> getConformanceCheckerFields();

  abstract public boolean acceptsFile(String filename);

  public abstract Configuration getDefaultConfiguration();

  abstract public IndividualReport processFile(String pathToFile, String reportFilename, String internalReportFolder, Configuration config, int id) throws ReadTagsIOException, ReadIccConfigIOException;

  public String getDefaultConfigurationPath(){
    return config.getConfiguration();
  }

  public ConformanceConfig getConfig() {
    return config;
  }

  public void setConfig(ConformanceConfig config) {
    this.config = config;
  }

  public static DpfLogger initDefault() {
    return new DpfLogger();
  }

  public static void setLogger(DpfLogger log) {
    Logger = log;
  }

}
