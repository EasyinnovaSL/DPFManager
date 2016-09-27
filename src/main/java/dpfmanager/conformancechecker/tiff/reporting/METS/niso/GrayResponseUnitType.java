/**
 * <h1>GrayResponseUnitType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for grayResponseUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="grayResponseUnitType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Number represents tenths of a unit"/>
 *     &lt;enumeration value="Number represents hundredths of a unit"/>
 *     &lt;enumeration value="Number represents thousandths of a unit"/>
 *     &lt;enumeration value="Number represents ten-thousandths of a unit"/>
 *     &lt;enumeration value="Number represents hundred-thousandths of a unit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "grayResponseUnitType")
@XmlEnum
public enum GrayResponseUnitType {

    @XmlEnumValue("Number represents tenths of a unit")
    NUMBER_REPRESENTS_TENTHS_OF_A_UNIT("1"),
    @XmlEnumValue("Number represents hundredths of a unit")
    NUMBER_REPRESENTS_HUNDREDTHS_OF_A_UNIT("2"),
    @XmlEnumValue("Number represents thousandths of a unit")
    NUMBER_REPRESENTS_THOUSANDTHS_OF_A_UNIT("3"),
    @XmlEnumValue("Number represents ten-thousandths of a unit")
    NUMBER_REPRESENTS_TEN_THOUSANDTHS_OF_A_UNIT("4"),
    @XmlEnumValue("Number represents hundred-thousandths of a unit")
    NUMBER_REPRESENTS_HUNDRED_THOUSANDTHS_OF_A_UNIT("5");
    private final String value;

    GrayResponseUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GrayResponseUnitType fromValue(String v) {
        for (GrayResponseUnitType c: GrayResponseUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    public static boolean verifyTag(String v) {
        for (GrayResponseUnitType c: GrayResponseUnitType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}
