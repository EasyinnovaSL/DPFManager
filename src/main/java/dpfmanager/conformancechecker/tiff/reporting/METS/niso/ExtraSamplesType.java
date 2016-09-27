/**
 * <h1>ExtraSamplesType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for extraSamplesType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="extraSamplesType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="unspecified data"/>
 *     &lt;enumeration value="associated alpha data (with pre-multiplied color)"/>
 *     &lt;enumeration value="unassociated alpha data"/>
 *     &lt;enumeration value="range or depth data"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "extraSamplesType")
@XmlEnum
public enum ExtraSamplesType {

    @XmlEnumValue("unspecified data")
    UNSPECIFIED_DATA("0"),
    @XmlEnumValue("associated alpha data (with pre-multiplied color)")
    ASSOCIATED_ALPHA_DATA_WITH_PRE_MULTIPLIED_COLOR("1"),
    @XmlEnumValue("unassociated alpha data")
    UNASSOCIATED_ALPHA_DATA("2"),
    @XmlEnumValue("range or depth data")
    RANGE_OR_DEPTH_DATA("3");
    private final String value;

    ExtraSamplesType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExtraSamplesType fromValue(String v) {
        for (ExtraSamplesType c: ExtraSamplesType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static boolean verifyTag(String v) {
        for (ExtraSamplesType c: ExtraSamplesType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
    }

}
