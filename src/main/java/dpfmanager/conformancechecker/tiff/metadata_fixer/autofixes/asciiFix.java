/**
 * <h1>fixMetadataInconsistencies.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Víctor Muñoz Sola
 * @version 1.0
 * @since 14/06/2017
 */

package dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes;

import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.Ascii;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.abstractTiffType;

/**
 * Created by Victor Munoz on 04/10/2016.
 */
public class asciiFix implements autofix {
  /**
   * The Definition.
   */
  public String Definition = "Fix non-7-bit Ascii tags";

  public String getDescription() {
    return Definition;
  }

  public void run(TiffDocument td) {
    try {
      // Fix asciis
      IFD ifd = td.getFirstIFD();
      while (ifd != null) {
        for (TagValue tv : ifd.getTags().getTags()) {
          if (tv.getType() == 2) {
            // Ascii
            for (abstractTiffType tt : tv.getValue()) {
              if (tt.toByte() > 127 || tt.toByte() < 0) {
                ((Ascii)tt).setValue((byte)63); // Set character '?'
              }
            }
          }
        }
        ifd = ifd.getNextIFD();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}