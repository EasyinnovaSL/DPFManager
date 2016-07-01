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
 * <p>Clase Java para gpsDifferentialType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="gpsDifferentialType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Measurement without differential correction"/>
 *     &lt;enumeration value="Differential correction applied"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gpsDifferentialType")
@XmlEnum
public enum GpsDifferentialType {

    @XmlEnumValue("Measurement without differential correction")
    MEASUREMENT_WITHOUT_DIFFERENTIAL_CORRECTION("Measurement without differential correction"),
    @XmlEnumValue("Differential correction applied")
    DIFFERENTIAL_CORRECTION_APPLIED("Differential correction applied");
    private final String value;

    GpsDifferentialType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GpsDifferentialType fromValue(String v) {
        for (GpsDifferentialType c: GpsDifferentialType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
