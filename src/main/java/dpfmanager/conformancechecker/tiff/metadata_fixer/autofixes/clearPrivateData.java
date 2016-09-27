/**
 * <h1>clearPrivateData.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes;

import com.easyinnova.tiff.model.TiffDocument;

/**
 * Created by easy on 23/10/2015.
 */
public class clearPrivateData implements autofix {
  /**
   * The Definition.
   */
  public String Definition = "Clear Private Data";

  public String getDescription() {
    return Definition;
  }

  public void run(TiffDocument td) {
    td.removeTag("GPS IFD");
    td.removeTag("GPSAltitude");
    td.removeTag("GPSAltitudeRef");
    td.removeTag("GPSAreaInformation");
    td.removeTag("GPSDateStamp");
    td.removeTag("GPSDestBearing");
    td.removeTag("GPSDestBearingRef");
    td.removeTag("GPSDistance");
    td.removeTag("GPSDistanceRef");
    td.removeTag("GPSLatitude");
    td.removeTag("GPSLatitudeRef");
    td.removeTag("GPSLONGitude");
    td.removeTag("GPSLONGitudeRef");
    td.removeTag("GPSDifferential");
    td.removeTag("GPSDOP");
    td.removeTag("GPSImgDirection");
    td.removeTag("GPSImgDirectionRef");
    td.removeTag("GPSDestLatitude");
    td.removeTag("GPSDestLatitudeRef");
    td.removeTag("GPSDestLONGitude");
    td.removeTag("GPSDestLONGitudeRef");
    td.removeTag("GPSMapDatum");
    td.removeTag("GPSMeasureMode");
    td.removeTag("GPSProcessingMethod");
    td.removeTag("GPSSatellites");
    td.removeTag("GPSSpeed");
    td.removeTag("GPSSpeedRef");
    td.removeTag("GPSStatus");
    td.removeTag("GPSTimestamp");
    td.removeTag("GPSTrack");
    td.removeTag("GPSTrackRef");
    td.removeTag("GPSVersionID");
  }
}
