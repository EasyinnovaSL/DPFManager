/**
 * <h1>MessageDigestAlgorithmType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for messageDigestAlgorithmType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="messageDigestAlgorithmType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Adler-32"/>
 *     &lt;enumeration value="CRC32"/>
 *     &lt;enumeration value="HAVAL"/>
 *     &lt;enumeration value="MD5"/>
 *     &lt;enumeration value="MNP"/>
 *     &lt;enumeration value="SHA-1"/>
 *     &lt;enumeration value="SHA-256"/>
 *     &lt;enumeration value="SHA-384"/>
 *     &lt;enumeration value="SHA-512"/>
 *     &lt;enumeration value="TIGER"/>
 *     &lt;enumeration value="WHIRLPOOL"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "messageDigestAlgorithmType")
@XmlEnum
public enum MessageDigestAlgorithmType {

    @XmlEnumValue("Adler-32")
    ADLER_32("Adler-32"),
    @XmlEnumValue("CRC32")
    CRC_32("CRC32"),
    HAVAL("HAVAL"),
    @XmlEnumValue("MD5")
    MD_5("MD5"),
    MNP("MNP"),
    @XmlEnumValue("SHA-1")
    SHA_1("SHA-1"),
    @XmlEnumValue("SHA-256")
    SHA_256("SHA-256"),
    @XmlEnumValue("SHA-384")
    SHA_384("SHA-384"),
    @XmlEnumValue("SHA-512")
    SHA_512("SHA-512"),
    TIGER("TIGER"),
    WHIRLPOOL("WHIRLPOOL"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final String value;

    MessageDigestAlgorithmType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageDigestAlgorithmType fromValue(String v) {
        for (MessageDigestAlgorithmType c: MessageDigestAlgorithmType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
