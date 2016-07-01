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
 * <p>Clase Java para orientationType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="orientationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="normal*"/>
 *     &lt;enumeration value="normal, image flipped"/>
 *     &lt;enumeration value="normal, rotated 180�"/>
 *     &lt;enumeration value="normal, image flipped, rotated 180�"/>
 *     &lt;enumeration value="normal, image flipped, rotated cw 90�"/>
 *     &lt;enumeration value="normal, rotated ccw 90�"/>
 *     &lt;enumeration value="normal, image flipped, rotated ccw 90�"/>
 *     &lt;enumeration value="normal, rotated cw 90�"/>
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
    NORMAL("normal*"),
    @XmlEnumValue("normal, image flipped")
    NORMAL_IMAGE_FLIPPED("normal, image flipped"),
    @XmlEnumValue("normal, rotated 180\u00b0")
    NORMAL_ROTATED_180("normal, rotated 180\u00b0"),
    @XmlEnumValue("normal, image flipped, rotated 180\u00b0")
    NORMAL_IMAGE_FLIPPED_ROTATED_180("normal, image flipped, rotated 180\u00b0"),
    @XmlEnumValue("normal, image flipped, rotated cw 90\u00b0")
    NORMAL_IMAGE_FLIPPED_ROTATED_CW_90("normal, image flipped, rotated cw 90\u00b0"),
    @XmlEnumValue("normal, rotated ccw 90\u00b0")
    NORMAL_ROTATED_CCW_90("normal, rotated ccw 90\u00b0"),
    @XmlEnumValue("normal, image flipped, rotated ccw 90\u00b0")
    NORMAL_IMAGE_FLIPPED_ROTATED_CCW_90("normal, image flipped, rotated ccw 90\u00b0"),
    @XmlEnumValue("normal, rotated cw 90\u00b0")
    NORMAL_ROTATED_CW_90("normal, rotated cw 90\u00b0"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
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
            }
        }
        throw new IllegalArgumentException(v);
    }

}
