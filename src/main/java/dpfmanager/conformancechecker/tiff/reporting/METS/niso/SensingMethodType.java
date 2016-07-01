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
 * <p>Clase Java para sensingMethodType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
    NOT_DEFINED("Not defined"),
    @XmlEnumValue("One-chip color area sensor")
    ONE_CHIP_COLOR_AREA_SENSOR("One-chip color area sensor"),
    @XmlEnumValue("Two-chip color area sensor")
    TWO_CHIP_COLOR_AREA_SENSOR("Two-chip color area sensor"),
    @XmlEnumValue("Three-chip color area sensor")
    THREE_CHIP_COLOR_AREA_SENSOR("Three-chip color area sensor"),
    @XmlEnumValue("Color sequential area sensor")
    COLOR_SEQUENTIAL_AREA_SENSOR("Color sequential area sensor"),
    @XmlEnumValue("Trilinear sensor")
    TRILINEAR_SENSOR("Trilinear sensor"),
    @XmlEnumValue("Color sequential linear sensor")
    COLOR_SEQUENTIAL_LINEAR_SENSOR("Color sequential linear sensor");
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

}
