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
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.statistics.model.HistogramTag;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;

import com.easyinnova.implementation_checker.ImplementationCheckerLoader;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.tiff.model.TiffTags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class IndividualReport.
 */
public class StatisticsObject {

  /**
   * Histogram of tags
   */
  private Map<Integer, HistogramTag> tags;

  /**
   * Main images count
   */
  private Integer mainImagesCount;

  /**
   * Thumbnails count
   */
  private Integer thumbnailsCount;

  /**
   * Averages per ISOs
   */
  private Map<String, StatisticsIso> isos;

  /**
   * Averages per ISOs
   */
  private Map<String, StatisticsIso> policys;

  public StatisticsObject(){
    tags = new HashMap<>();
    isos = new HashMap<>();
    policys = new HashMap<>();
    mainImagesCount = 0;
    thumbnailsCount = 0;
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
  }

  private void parseErrors(IndividualReport ir){
    for (String iso : ir.getSelectedIsos()){
      if (iso.equals(TiffConformanceChecker.POLICY_ISO)){
        parsePolicyErrors(ir);
      } else {
        iso = ImplementationCheckerLoader.getIsoName(iso);
        StatisticsIso sIso = (isos.containsKey(iso)) ? isos.get(iso) : new StatisticsIso();
        sIso.count++;
        sIso.errors += ir.getNErrors(iso);
        sIso.warnings += ir.getNWarnings(iso);
        isos.put(iso, sIso);
      }
    }
  }

  private void parsePolicyErrors(IndividualReport ir){
    for (RuleResult rr : ir.getAllRuleResults(TiffConformanceChecker.POLICY_ISO)) {
      String iso = rr.getRule().getDescription().getValue();
      StatisticsIso sIso = (policys.containsKey(iso)) ? policys.get(iso) : new StatisticsIso();
      sIso.count++;
      if (!rr.ok() && !rr.getWarning()) sIso.errors++;
      if (!rr.ok() && rr.getWarning()) sIso.warnings++;
      policys.put(iso, sIso);
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
        } else {
          hTag.increaseThumbCount();
        }
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * Getters
   */
  public Map<Integer, HistogramTag> getTags() {
    return tags;
  }

  public Integer getMainImagesCount() {
    return mainImagesCount;
  }

  public Integer getThumbnailsCount() {
    return thumbnailsCount;
  }

  /**
   * Test Only
   */
  public void printResults(){
    System.out.println("");

    System.out.println("GENERAL");
    System.out.println("  Total images: "+getMainImagesCount());
    System.out.println("  Total thumbnails: "+getThumbnailsCount());
    System.out.println("");


    System.out.println("TAGS   (main / thumbnail)");
    for(HistogramTag hTag : tags.values()){
      System.out.println("  " + hTag.tv.getName() + ": " + hTag.main + " / " + hTag.thumb + " (" + getMainImagesCount() + " / " + getThumbnailsCount() + ")");
    }
    System.out.println("");

    if (true) return;

    System.out.println("TOTAL ERRORS ISOs");
    for(String iso : isos.keySet()){
      StatisticsIso sIso = isos.get(iso);
      System.out.println("  " + iso + ":");
      System.out.println("    Err - " + sIso.errors);
      System.out.println("    War - " + sIso.warnings);
    }
    System.out.println("");

    System.out.println("TOTAL ERRORS POLICY");
    for(String iso : policys.keySet()){
      StatisticsIso sIso = policys.get(iso);
      System.out.println("  " + iso + ":");
      System.out.println("    Err - " + sIso.errors);
      System.out.println("    War - " + sIso.warnings);
    }
    System.out.println("");
  }
}
