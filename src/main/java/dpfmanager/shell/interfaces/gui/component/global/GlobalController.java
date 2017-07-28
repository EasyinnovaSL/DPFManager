/**
 * <h1>ReportsController.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.interfaces.gui.component.global;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.mvc.DpfController;
import dpfmanager.shell.interfaces.gui.component.global.comparators.IndividualComparator;
import dpfmanager.shell.interfaces.gui.component.global.messages.GuiGlobalMessage;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.report.core.GlobalReport;
import dpfmanager.shell.modules.report.core.SmallIndividualReport;
import dpfmanager.shell.modules.report.util.ReportGui;
import dpfmanager.shell.modules.report.util.ReportIndividualGui;
import javafx.stage.FileChooser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adria Llorens on 07/03/2016.
 */
public class GlobalController extends DpfController<GlobalModel, GlobalView> {

  public static Integer itemsPerPage = 12;

  private List<ReportIndividualGui> individuals;

  public GlobalController() {
  }

  public void readIndividualReports(ReportGui reportGui){
    individuals = new ArrayList<>();
    GlobalReport global = reportGui.getGlobalReport();
    if (global == null) {
      // Old report format, read from dir
      Integer count = 0;
      List<String> idsFound = new ArrayList<>();
      File reportDir = new File(reportGui.getInternalReportFolder());
      File htmlDir = new File(reportGui.getInternalReportFolder() + "/html");
      // Search in report dir
      if (reportDir.exists() && reportDir.isDirectory()) {
        for (File individualReport : reportDir.listFiles()) {
          String name = individualReport.getName();
          if (individualReport.exists() && individualReport.isFile() && !name.startsWith("summary") && !name.startsWith("report")) {
            String id = name.substring(0, name.indexOf("-"));
            if (!idsFound.contains(id)) {
              individuals.add(new ReportIndividualGui(individualReport.getAbsolutePath(), count));
              idsFound.add(id);
              count++;
            }
          }
        }
      }
      // Search in html dir if no reports found
      if (count == 0 && htmlDir.exists() && htmlDir.isDirectory()) {
        for (File individualReport : htmlDir.listFiles()) {
          String name = individualReport.getName();
          if (individualReport.exists() && individualReport.isFile() && !name.startsWith("summary") && !name.startsWith("report")) {
            String id = name.substring(0, name.indexOf("-"));
            if (!idsFound.contains(id)) {
              individuals.add(new ReportIndividualGui(individualReport.getAbsolutePath(), count));
              idsFound.add(id);
              count++;
            }
          }
        }
      }
      return;
    }
    Configuration config = global.getConfig();
    String internal = reportGui.getInternalReportFolder();
    if (global.getVersion() >= 3) {
      Integer count = 0;
      for (SmallIndividualReport sir : global.getIndividualReports()) {
        String serPath = sir.getSerPath();
        if (serPath == null || serPath.isEmpty()){
          String filenameNorm = sir.getFileName().replaceAll("\\\\", "/");
          String serFileName = sir.getIdReport() + "-" +filenameNorm.substring(filenameNorm.lastIndexOf("/") + 1) + ".ser";
          serPath = internal + "/serialized/" + serFileName;
        }
        File individualSer = new File(serPath);
        if (individualSer.exists() && individualSer.isFile() && individualSer.getName().endsWith(".ser")){
          individuals.add(new ReportIndividualGui(global, sir, config, count));
          count++;
        }
      }
    } else {
      Integer count = 0;
      File serializedDirectory = new File(internal + "/serialized");
      if (serializedDirectory.exists() && serializedDirectory.isDirectory()) {
        for (File individualSer : serializedDirectory.listFiles()) {
          if (individualSer.exists() && individualSer.isFile() && individualSer.getName().endsWith(".ser")) {
            individuals.add(new ReportIndividualGui(individualSer.getAbsolutePath(), global, count));
            count++;
          }
        }
      }
    }
    sortIndividuals();
  }

  public void sortIndividuals(){
    individuals.sort(new IndividualComparator(getView().getCurrentMode(), getView().getCurrentOrder()));
  }

  public void loadAndPrintIndividuals(String vboxId, int page){
    int init = page * itemsPerPage;
    int end = init + itemsPerPage;
    int i = init;
    while (i < individuals.size() && i < end) {
      ReportIndividualGui rig = individuals.get(i);
      rig.setLast(i == individuals.size() - 1 || i == end - 1);
      getContext().send(GuiConfig.COMPONENT_GLOBAL, new GuiGlobalMessage(GuiGlobalMessage.Type.ADD_INDIVIDUAL, vboxId, rig));
      i++;
    }
  }

  public List<ReportIndividualGui> getIndividualReports(){
    return individuals;
  }

  public Integer getPagesCount(){
    int size = individuals.size();
    int pages = size / itemsPerPage;
    if (size % itemsPerPage > 0) pages++;
    return pages;
  }

  /**
   * Starts a new full check based on this quick check.
   */
  public boolean transformReport(){
    GlobalReport global = getView().getInfo().getGlobalReport();
    if (global.getConfig().isQuick()){
      List<String> inputFiles = getInputFiles(getView().isErrors(), getView().isCorrect(), true);
      List<String> inputNotFound = getInputFiles(getView().isErrors(), getView().isCorrect(), false);
      if (inputFiles.size() == 0) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.INFO, getBundle().getString("filesEmpty")));
      } else {
        getContext().send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(String.join(";", inputFiles), getView().getInfo().getGlobalReport().getConfig(), 100, false, false));
        return true;
      }
      if (inputNotFound.size() > 0) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ALERT, getBundle().getString("filesNotFound"), String.join("\n", inputNotFound)));
      }
    }
    return false;
  }

  private List<String> getInputFiles(boolean err, boolean pas, boolean found){
    List<String> inputFiles = new ArrayList<>();
    GlobalReport global = getView().getInfo().getGlobalReport();
    for (SmallIndividualReport individual : global.getIndividualReports()){
      int errors = 0;
      int warnings = 0;
      for (String iso : global.getSelectedIsos()){
        if (err && individual.getNErrors(iso) > 0)  errors++;
        else if (individual.getNWarnings(iso) > 0) warnings++;
      }
      if ((err && errors > 0) || (pas && errors == 0 && warnings == 0)) {
        String filePath = readZipOrFilePath(global, individual.getFilePath());
        if (new File(filePath).exists() == found){
          if (!inputFiles.contains(filePath)){
            inputFiles.add(filePath);
          }
        }
      }
    }
    return inputFiles;
  }

  private String readZipOrFilePath(GlobalReport global, String filePath){
    for (String key : global.getZipsPaths().keySet()){
      if (filePath.contains("/" + key + "/") || filePath.contains("\\" + key + "\\")) {
        return global.getZipsPaths().get(key);
      }
    }
    return filePath;
  }

  public void downloadReport(String path){
    File src = new File(path);
    if (!src.exists() || !src.isFile()) return;

    String name = src.getName().substring(0, src.getName().indexOf("."));
    String extension = src.getName().substring(src.getName().indexOf("."));

    FileChooser fileChooser = new FileChooser();

    //Set extension filter
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extension.toLowerCase().substring(1, extension.length() -1) + " files (*"+extension+")", "*" + extension);
    fileChooser.getExtensionFilters().add(extFilter);
    fileChooser.setInitialFileName(name);

    //Show save file dialog
    File dest = fileChooser.showSaveDialog(GuiWorkbench.getMyStage());

    if(dest != null){
      try {
        FileUtils.copyFile(src, dest);
      } catch (Exception e) {
        getContext().send(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.ERROR, getBundle().getString("errorSavingReport")));
      }
    }
  }

}
