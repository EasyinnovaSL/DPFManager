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
 * <p>Clase Java para autoFocusType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="autoFocusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Auto Focus Used"/>
 *     &lt;enumeration value="Auto Focus Interrupted"/>
 *     &lt;enumeration value="Near Focused"/>
 *     &lt;enumeration value="Soft Focused"/>
 *     &lt;enumeration value="Manual"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "autoFocusType")
@XmlEnum
public enum AutoFocusType {

    @XmlEnumValue("Auto Focus Used")
    AUTO_FOCUS_USED("Auto Focus Used"),
    @XmlEnumValue("Auto Focus Interrupted")
    AUTO_FOCUS_INTERRUPTED("Auto Focus Interrupted"),
    @XmlEnumValue("Near Focused")
    NEAR_FOCUSED("Near Focused"),
    @XmlEnumValue("Soft Focused")
    SOFT_FOCUSED("Soft Focused"),
    @XmlEnumValue("Manual")
    MANUAL("Manual");
    private final String value;

    AutoFocusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AutoFocusType fromValue(String v) {
        for (AutoFocusType c: AutoFocusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
