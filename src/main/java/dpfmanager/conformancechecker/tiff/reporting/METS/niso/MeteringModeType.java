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
 * <p>Clase Java para meteringModeType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="meteringModeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Average"/>
 *     &lt;enumeration value="Center weighted average"/>
 *     &lt;enumeration value="Spot"/>
 *     &lt;enumeration value="Multispot"/>
 *     &lt;enumeration value="Pattern"/>
 *     &lt;enumeration value="Partial"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "meteringModeType")
@XmlEnum
public enum MeteringModeType {

    @XmlEnumValue("Average")
    AVERAGE("Average"),
    @XmlEnumValue("Center weighted average")
    CENTER_WEIGHTED_AVERAGE("Center weighted average"),
    @XmlEnumValue("Spot")
    SPOT("Spot"),
    @XmlEnumValue("Multispot")
    MULTISPOT("Multispot"),
    @XmlEnumValue("Pattern")
    PATTERN("Pattern"),
    @XmlEnumValue("Partial")
    PARTIAL("Partial");
    private final String value;

    MeteringModeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MeteringModeType fromValue(String v) {
        for (MeteringModeType c: MeteringModeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
