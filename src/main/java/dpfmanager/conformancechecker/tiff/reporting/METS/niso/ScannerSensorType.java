/**
 * <h1>ScannerSensorType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for scannerSensorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="scannerSensorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="undefined"/>
 *     &lt;enumeration value="MonochromeLinear"/>
 *     &lt;enumeration value="ColorTriLinear"/>
 *     &lt;enumeration value="ColorSequentialLinear"/>
 *     &lt;enumeration value="MonochromeArea"/>
 *     &lt;enumeration value="OneChipColourArea"/>
 *     &lt;enumeration value="TwoChipColorArea"/>
 *     &lt;enumeration value="ThreeChipColorArea"/>
 *     &lt;enumeration value="ColorSequentialArea"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "scannerSensorType")
@XmlEnum
public enum ScannerSensorType {

    @XmlEnumValue("undefined")
    UNDEFINED("undefined"),
    @XmlEnumValue("MonochromeLinear")
    MONOCHROME_LINEAR("MonochromeLinear"),
    @XmlEnumValue("ColorTriLinear")
    COLOR_TRI_LINEAR("ColorTriLinear"),
    @XmlEnumValue("ColorSequentialLinear")
    COLOR_SEQUENTIAL_LINEAR("ColorSequentialLinear"),
    @XmlEnumValue("MonochromeArea")
    MONOCHROME_AREA("MonochromeArea"),
    @XmlEnumValue("OneChipColourArea")
    ONE_CHIP_COLOUR_AREA("OneChipColourArea"),
    @XmlEnumValue("TwoChipColorArea")
    TWO_CHIP_COLOR_AREA("TwoChipColorArea"),
    @XmlEnumValue("ThreeChipColorArea")
    THREE_CHIP_COLOR_AREA("ThreeChipColorArea"),
    @XmlEnumValue("ColorSequentialArea")
    COLOR_SEQUENTIAL_AREA("ColorSequentialArea");
    private final String value;

    ScannerSensorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ScannerSensorType fromValue(String v) {
        for (ScannerSensorType c: ScannerSensorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
