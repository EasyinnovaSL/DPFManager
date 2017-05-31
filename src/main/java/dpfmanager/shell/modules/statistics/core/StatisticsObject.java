/**
 * <h1>ReportGenerator.java</h1> <p> This program is free software: you can redistribute it and/or
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
 * @author Adria Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.statistics.core;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.reporting.ReportTag;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.statistics.model.HistogramTag;
import dpfmanager.shell.modules.statistics.model.StatisticsError;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;
import dpfmanager.shell.modules.statistics.model.StatisticsIsoErrors;
import dpfmanager.shell.modules.statistics.model.StatisticsRule;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.tiff.model.TiffTags;

import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The Class IndividualReport.
 */
public class StatisticsObject {

  /**
   * Histogram of tags
   */
  private Map<Integer, HistogramTag> tags;

  /**
   * Global reports count
   */
  private Integer reportsCount;

  /**
   * Tiffs count
   */
  private Integer tiffsCount;

  /**
   * Main images count
   */
  private Integer mainImagesCount;

  /**
   * Thumbnails count
   */
  private Integer thumbnailsCount;

  /**
   * The files total size
   */
  private Long totalSize;

  /**
   * Statistics per ISOs
   */
  private Map<String, StatisticsIso> isos;

  /**
   * ISO errors
   */
  private Map<String, StatisticsIsoErrors> isoErrors;

  /**
   * Policy Rules
   */
  private Map<String, StatisticsRule> policys;

  /**
   * Expandable tags
   */
  private final List<String> expandableTagNames = Arrays.asList("PhotometricInterpretation", "Orientation", "Compression", "BitsPerSample", "NewSubfileType", "SamplesPerPixel", "PlanarConfiguration", "ResolutionUnit", "Make", "Model", "Software", "Copyright");

  public StatisticsObject(){
    tags = new HashMap<>();
    isos = new HashMap<>();
    policys = new HashMap<>();
    isoErrors = new HashMap<>();
    mainImagesCount = 0;
    thumbnailsCount = 0;
    reportsCount = 0;
    tiffsCount = 0;
    totalSize = 0L;
    // Load tags from dictionary
    try{
      TiffTags.getTiffTags();
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void parseIndividualReport(IndividualReport ir){
    parseTags(ir);
    parseErrors(ir);
    mainImagesCount += ir.getTiffModel().getMainImagesCount();
    thumbnailsCount += ir.getTiffModel().getThumbnailsImagesCount();
    tiffsCount++;
    totalSize += ir.getTiffModel().getSize();
  }

  private void parseErrors(IndividualReport ir){
    for (String iso : ir.getSelectedIsos()){
      if (iso.equals(TiffConformanceChecker.POLICY_ISO)){
        parsePolicyErrors(ir);
      } else {
        String isoName = ImplementationCheckerLoader.getIsoName(iso);
        StatisticsIso sIso = (isos.containsKey(iso)) ? isos.get(iso) : new StatisticsIso(isoName, iso);
        sIso.count++;
        if (ir.getNErrors(iso) > 0){
          sIso.errors ++;
        } else if (ir.getNWarnings(iso) > 0) {
          sIso.warnings ++;
        } else {
          sIso.passed ++;
        }
        parseIsoErrors(ir, iso, sIso);
        isos.put(iso, sIso);
      }
    }
  }

  private void parseIsoErrors(IndividualReport ir, String isoId, StatisticsIso sIso){
    StatisticsIsoErrors sIsoErrors = sIso.getIsoErrors();
    for (RuleResult rr : ir.getKORuleResults(isoId)) {
      if (!rr.getInfo()) {
        sIsoErrors.addError(rr);
      }
    }
    isoErrors.put(isoId, sIsoErrors);
  }

  private void parsePolicyErrors(IndividualReport ir){
    for (RuleResult rr : ir.getAllRuleResults(TiffConformanceChecker.POLICY_ISO)) {
      String name = rr.getRule().getDescription().getValue();
      String id = name + String.valueOf(rr.getWarning());
      StatisticsRule sRule = (policys.containsKey(id)) ? policys.get(id) : new StatisticsRule(name, rr.getWarning());
      sRule.total++;
      if (!rr.ok()) sRule.count++;
      policys.put(id, sRule);
    }
  }

  private void parseTags(IndividualReport ir){
    try {
      for (ReportTag tag : ir.getTags(true)) {
        if (tag.tv == null) continue;
        if (!tags.containsKey(tag.tv.getId())) tags.put(tag.tv.getId(), new HistogramTag(tag));
        HistogramTag hTag = tags.get(tag.tv.getId());
        if (!tag.thumbnail) {
          hTag.increaseMainCount();
          if (expandableTagNames.contains(hTag.getValue().getName())){
            hTag.addMainValue(tag.tv.getFirstTextReadValue());
          }
        } else {
          hTag.increaseThumbCount();
          if (expandableTagNames.contains(hTag.getValue().getName())){
            hTag.addThumbValue(tag.tv.getFirstTextReadValue());
          }
        }
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void setReportsCount(Integer reportsCount) {
    this.reportsCount = reportsCount;
  }

  /**
   * Getters
   */
  public Map<Integer, HistogramTag> getTags() {
    return tags;
  }

  public Integer getReportsCount() {
    return reportsCount;
  }

  public Integer getTiffsCount() {
    return tiffsCount;
  }

  public Integer getMainImagesCount() {
    return mainImagesCount;
  }

  public Integer getThumbnailsCount() {
    return thumbnailsCount;
  }

  public Map<String, StatisticsIso> getIsos() {
    return isos;
  }

  public Map<String, StatisticsRule> getPolicys() {
    return policys;
  }

  public Map<String, StatisticsIsoErrors> getIsoErrors() {
    return isoErrors;
  }

  public Long getTotalSize() {
    return totalSize;
  }

}
