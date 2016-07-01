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
 * <p>Clase Java para backLightType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="backLightType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Front light"/>
 *     &lt;enumeration value="Backlight 1"/>
 *     &lt;enumeration value="Backlight 2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "backLightType")
@XmlEnum
public enum BackLightType {

    @XmlEnumValue("Front light")
    FRONT_LIGHT("Front light"),
    @XmlEnumValue("Backlight 1")
    BACKLIGHT_1("Backlight 1"),
    @XmlEnumValue("Backlight 2")
    BACKLIGHT_2("Backlight 2");
    private final String value;

    BackLightType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BackLightType fromValue(String v) {
        for (BackLightType c: BackLightType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
