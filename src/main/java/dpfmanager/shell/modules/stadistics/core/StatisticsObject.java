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

package dpfmanager.shell.modules.stadistics.core;

import dpfmanager.conformancechecker.tiff.reporting.ReportTag;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.stadistics.model.HistogramTag;

import com.easyinnova.tiff.model.TiffTags;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class IndividualReport.
 */
public class StatisticsObject {

  /**
   * Histogram of main tags
   */
  private Map<Integer, HistogramTag> mainTags;

  /**
   * Histogram of thumbnail tags
   */
  private Map<Integer, HistogramTag> thumbTags;

  public StatisticsObject(){
    mainTags = new HashMap<>();
    thumbTags = new HashMap<>();
  }

  public void parseIndividualReport(IndividualReport ir){
    try {
      for (ReportTag tag : ir.getTags(true)) {
        if (tag.tv == null) continue;
        if (!tag.thumbnail) {
          if (!mainTags.containsKey(tag.tv.getId()))
            mainTags.put(tag.tv.getId(), new HistogramTag(tag));
          mainTags.get(tag.tv.getId()).increaseCount();
        } else {
          if (!thumbTags.containsKey(tag.tv.getId()))
            thumbTags.put(tag.tv.getId(), new HistogramTag(tag));
          thumbTags.get(tag.tv.getId()).increaseCount();
        }
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void printResults(){
    try{
      TiffTags.getTiffTags();
    } catch (Exception e){
      e.printStackTrace();
    }

    System.out.println("MAIN");
    for(HistogramTag hTag : mainTags.values()){
      System.out.println(hTag.tv.getName() + ": " + hTag.count);
    }

    System.out.println("THUMB");
    for(HistogramTag hTag : thumbTags.values()){
      System.out.println(hTag.tv.getName() + ": " + hTag.count);
    }
  }
}
