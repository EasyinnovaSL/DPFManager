/**
 * <h1>ExposureProgramType.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Mar Llambi
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for exposureProgramType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="exposureProgramType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Not defined"/>
 *     &lt;enumeration value="Manual"/>
 *     &lt;enumeration value="Normal program"/>
 *     &lt;enumeration value="Aperture priority"/>
 *     &lt;enumeration value="Shutter priority"/>
 *     &lt;enumeration value="Creative program (biased toward depth of field)"/>
 *     &lt;enumeration value="Action program (biased toward fast shutter speed)"/>
 *     &lt;enumeration value="Portrait mode (for closeup photos with the background out of focus)"/>
 *     &lt;enumeration value="Landscape mode (for landscape photos with the background in focus)"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "exposureProgramType")
@XmlEnum
public enum ExposureProgramType {

    @XmlEnumValue("Not defined")
    NOT_DEFINED("0"),
    @XmlEnumValue("Manual")
    MANUAL("1"),
    @XmlEnumValue("Normal program")
    NORMAL_PROGRAM("2"),
    @XmlEnumValue("Aperture priority")
    APERTURE_PRIORITY("3"),
    @XmlEnumValue("Shutter priority")
    SHUTTER_PRIORITY("4"),
    @XmlEnumValue("Creative program (biased toward depth of field)")
    CREATIVE_PROGRAM_BIASED_TOWARD_DEPTH_OF_FIELD("5"),
    @XmlEnumValue("Action program (biased toward fast shutter speed)")
    ACTION_PROGRAM_BIASED_TOWARD_FAST_SHUTTER_SPEED("6"),
    @XmlEnumValue("Portrait mode (for closeup photos with the background out of focus)")
    PORTRAIT_MODE_FOR_CLOSEUP_PHOTOS_WITH_THE_BACKGROUND_OUT_OF_FOCUS("7"),
    @XmlEnumValue("Landscape mode (for landscape photos with the background in focus)")
    LANDSCAPE_MODE_FOR_LANDSCAPE_PHOTOS_WITH_THE_BACKGROUND_IN_FOCUS("8");
    private final String value;

    ExposureProgramType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExposureProgramType fromValue(String v) {
        for (ExposureProgramType c: ExposureProgramType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    public static  boolean verifyTag(String v) {
        for (ExposureProgramType c: ExposureProgramType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}
