/**
 * <h1>SensingMethodType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for sensingMethodType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sensingMethodType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Not defined"/>
 *     &lt;enumeration value="One-chip color area sensor"/>
 *     &lt;enumeration value="Two-chip color area sensor"/>
 *     &lt;enumeration value="Three-chip color area sensor"/>
 *     &lt;enumeration value="Color sequential area sensor"/>
 *     &lt;enumeration value="Trilinear sensor"/>
 *     &lt;enumeration value="Color sequential linear sensor"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sensingMethodType")
@XmlEnum
public enum SensingMethodType {

    @XmlEnumValue("Not defined")
    NOT_DEFINED("1"),
    @XmlEnumValue("One-chip color area sensor")
    ONE_CHIP_COLOR_AREA_SENSOR("2"),
    @XmlEnumValue("Two-chip color area sensor")
    TWO_CHIP_COLOR_AREA_SENSOR("3"),
    @XmlEnumValue("Three-chip color area sensor")
    THREE_CHIP_COLOR_AREA_SENSOR("4"),
    @XmlEnumValue("Color sequential area sensor")
    COLOR_SEQUENTIAL_AREA_SENSOR("5"),
    @XmlEnumValue("Trilinear sensor")
    TRILINEAR_SENSOR("7"),
    @XmlEnumValue("Color sequential linear sensor")
    COLOR_SEQUENTIAL_LINEAR_SENSOR("8");
    private final String value;

    SensingMethodType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SensingMethodType fromValue(String v) {
        for (SensingMethodType c: SensingMethodType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    public static boolean verifyTag(String v) {
        for (SensingMethodType c: SensingMethodType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}
