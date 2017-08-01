/**
 * <h1>IsoComparator.java</h1> <p> This program is free software: you can redistribute it and/or
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
 */
package dpfmanager.shell.interfaces.gui.component.statistics.comparators;

import dpfmanager.shell.modules.statistics.model.HistogramTag;
import dpfmanager.shell.modules.statistics.model.StatisticsIso;

import java.util.Comparator;

/**
 * Created by Adrià Llorens on 26/05/2017.
 */
public class IsoComparator implements  Comparator<StatisticsIso> {

  public enum Mode {
    ERRORS, WARNINGS, PASSED, NAME, COUNT
  }

  private Mode mode;

  public IsoComparator(Mode m){
    mode = m;
  }

  @Override
  public int compare(StatisticsIso o1, StatisticsIso o2) {
    Integer compare = 0;
    switch (mode){
      case ERRORS:
        compare = o2.errors.compareTo(o1.errors);
        break;
      case WARNINGS:
        compare = o2.warnings.compareTo(o1.warnings);
        break;
      case PASSED:
        compare = o2.passed.compareTo(o1.passed);
        break;
      case COUNT:
        compare = o2.count.compareTo(o1.count);
        break;
    }

    if (compare == 0){
      compare = o1.iso.compareTo(o2.iso);
    }
    return compare;
  }
}
