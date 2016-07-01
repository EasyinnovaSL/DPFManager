//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:54:03 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para messageDigestAlgorithmType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
