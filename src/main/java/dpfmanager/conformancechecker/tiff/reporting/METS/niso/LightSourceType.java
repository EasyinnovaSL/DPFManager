/**
 * <h1>LightSourceType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for lightSourceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="lightSourceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Daylight"/>
 *     &lt;enumeration value="Fluorescent"/>
 *     &lt;enumeration value="Tungsten (incandescent light)"/>
 *     &lt;enumeration value="Flash"/>
 *     &lt;enumeration value="Fine weather"/>
 *     &lt;enumeration value="Cloudy weather"/>
 *     &lt;enumeration value="Shade"/>
 *     &lt;enumeration value="Daylight fluorescent (D 5700 - 7100K)"/>
 *     &lt;enumeration value="Day white fluorescent (N 4600 - 5400K)"/>
 *     &lt;enumeration value="Cool white fluorescent (W 3900 - 4500K)"/>
 *     &lt;enumeration value="White fluorescent (WW 3200 - 3700K)"/>
 *     &lt;enumeration value="Standard light A"/>
 *     &lt;enumeration value="Standard light B"/>
 *     &lt;enumeration value="Standard light C"/>
 *     &lt;enumeration value="D55"/>
 *     &lt;enumeration value="D65"/>
 *     &lt;enumeration value="D75"/>
 *     &lt;enumeration value="D50"/>
 *     &lt;enumeration value="ISO studio tungsten"/>
 *     &lt;enumeration value="other light source"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "lightSourceType")
@XmlEnum
public enum LightSourceType {

    @XmlEnumValue("Daylight")
    DAYLIGHT("1"),
    @XmlEnumValue("Fluorescent")
    FLUORESCENT("2"),
    @XmlEnumValue("Tungsten (incandescent light)")
    TUNGSTEN_INCANDESCENT_LIGHT("3"),
    @XmlEnumValue("Flash")
    FLASH("4"),
    @XmlEnumValue("Fine weather")
    FINE_WEATHER("9"),
    @XmlEnumValue("Cloudy weather")
    CLOUDY_WEATHER("10"),
    @XmlEnumValue("Shade")
    SHADE("11"),
    @XmlEnumValue("Daylight fluorescent (D 5700 - 7100K)")
    DAYLIGHT_FLUORESCENT_D_5700_7100_K("12"),
    @XmlEnumValue("Day white fluorescent (N 4600 - 5400K)")
    DAY_WHITE_FLUORESCENT_N_4600_5400_K("13"),
    @XmlEnumValue("Cool white fluorescent (W 3900 - 4500K)")
    COOL_WHITE_FLUORESCENT_W_3900_4500_K("14"),
    @XmlEnumValue("White fluorescent (WW 3200 - 3700K)")
    WHITE_FLUORESCENT_WW_3200_3700_K("15"),
    @XmlEnumValue("Standard light A")
    STANDARD_LIGHT_A("17"),
    @XmlEnumValue("Standard light B")
    STANDARD_LIGHT_B("18"),
    @XmlEnumValue("Standard light C")
    STANDARD_LIGHT_C("19"),
    @XmlEnumValue("D55")
    D_55("20"),
    @XmlEnumValue("D65")
    D_65("21"),
    @XmlEnumValue("D75")
    D_75("22"),
    @XmlEnumValue("D50")
    D_50("23"),
    @XmlEnumValue("ISO studio tungsten")
    ISO_STUDIO_TUNGSTEN("24"),
    @XmlEnumValue("other light source")
    OTHER_LIGHT_SOURCE("255"),
    @XmlEnumValue("unknown")
    UNKNOWN("0");
    private final String value;

    LightSourceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LightSourceType fromValue(String v) {
        for (LightSourceType c: LightSourceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static boolean verifyTag(String v) {
        for (LightSourceType c: LightSourceType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}
