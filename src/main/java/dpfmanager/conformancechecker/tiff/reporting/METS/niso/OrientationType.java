/**
 * <h1>OrientationType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for orientationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="orientationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="normal*"/>
 *     &lt;enumeration value="normal, image flipped"/>
 *     &lt;enumeration value="normal, rotated 180"/>
 *     &lt;enumeration value="normal, image flipped, rotated 180"/>
 *     &lt;enumeration value="normal, image flipped, rotated cw 90"/>
 *     &lt;enumeration value="normal, rotated ccw 90"/>
 *     &lt;enumeration value="normal, image flipped, rotated ccw 90"/>
 *     &lt;enumeration value="normal, rotated cw 90"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "orientationType")
@XmlEnum
public enum OrientationType {

    @XmlEnumValue("normal*")
    NORMAL("1"),
    @XmlEnumValue("normal, image flipped")
    NORMAL_IMAGE_FLIPPED("2"),
    @XmlEnumValue("normal, rotated 180\u00b0")
    NORMAL_ROTATED_180("3"),
    @XmlEnumValue("normal, image flipped, rotated 180\u00b0")
    NORMAL_IMAGE_FLIPPED_ROTATED_180("4"),
    @XmlEnumValue("normal, image flipped, rotated cw 90\u00b0")
    NORMAL_IMAGE_FLIPPED_ROTATED_CW_90("5"),
    @XmlEnumValue("normal, rotated ccw 90\u00b0")
    NORMAL_ROTATED_CCW_90("6"),
    @XmlEnumValue("normal, image flipped, rotated ccw 90\u00b0")
    NORMAL_IMAGE_FLIPPED_ROTATED_CCW_90("7"),
    @XmlEnumValue("normal, rotated cw 90\u00b0")
    NORMAL_ROTATED_CW_90("8"),
    @XmlEnumValue("unknown")
    UNKNOWN("9");
    private final String value;

    OrientationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OrientationType fromValue(String v) {
        for (OrientationType c: OrientationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }else if (c.value.equals("9")){
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    public static boolean verifyTag(String v) {
        for (OrientationType c: OrientationType.values()) {
            if (c.value.equals(v)) {
                return true;
            }else if (c.value.equals("9")){
                return true;
            }
        }
        return false;
    }

}
