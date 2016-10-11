/**
 * <h1>CameraSensorType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for cameraSensorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cameraSensorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="undefined"/>
 *     &lt;enumeration value="MonochromeArea"/>
 *     &lt;enumeration value="OneChipColorArea"/>
 *     &lt;enumeration value="TwoChipColorArea"/>
 *     &lt;enumeration value="ThreeChipColorArea"/>
 *     &lt;enumeration value="MonochromeLinear"/>
 *     &lt;enumeration value="ColorTriLinear"/>
 *     &lt;enumeration value="ColorSequentialLinear"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cameraSensorType")
@XmlEnum
public enum CameraSensorType {

    @XmlEnumValue("undefined")
    UNDEFINED("1"),
    @XmlEnumValue("OneChipColorArea")
    ONE_CHIP_COLOR_AREA("2"),
    @XmlEnumValue("TwoChipColorArea")
    TWO_CHIP_COLOR_AREA("3"),
    @XmlEnumValue("ThreeChipColorArea")
    THREE_CHIP_COLOR_AREA("4"),
    @XmlEnumValue("ColorSequencialArea")
    COLOR_SEQUENCIAL_AREA("5"),
    @XmlEnumValue("ColorTriLinear")
    COLOR_TRI_LINEAR("7"),
    @XmlEnumValue("ColorSequentialLinear")
    COLOR_SEQUENTIAL_LINEAR("8");
    private final String value;

    CameraSensorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CameraSensorType fromValue(String v) {
        for (CameraSensorType c: CameraSensorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    public static boolean verifyTag(String v) {
        for (CameraSensorType c: CameraSensorType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}
