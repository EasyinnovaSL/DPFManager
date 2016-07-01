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
 * <p>Clase Java para grayResponseUnitType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="grayResponseUnitType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Number represents tenths of a unit"/>
 *     &lt;enumeration value="Number represents hundredths of a unit"/>
 *     &lt;enumeration value="Number represents thousandths of a unit"/>
 *     &lt;enumeration value="Number represents ten-thousandths of a unit"/>
 *     &lt;enumeration value="Number represents hundred-thousandths of a unit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "grayResponseUnitType")
@XmlEnum
public enum GrayResponseUnitType {

    @XmlEnumValue("Number represents tenths of a unit")
    NUMBER_REPRESENTS_TENTHS_OF_A_UNIT("Number represents tenths of a unit"),
    @XmlEnumValue("Number represents hundredths of a unit")
    NUMBER_REPRESENTS_HUNDREDTHS_OF_A_UNIT("Number represents hundredths of a unit"),
    @XmlEnumValue("Number represents thousandths of a unit")
    NUMBER_REPRESENTS_THOUSANDTHS_OF_A_UNIT("Number represents thousandths of a unit"),
    @XmlEnumValue("Number represents ten-thousandths of a unit")
    NUMBER_REPRESENTS_TEN_THOUSANDTHS_OF_A_UNIT("Number represents ten-thousandths of a unit"),
    @XmlEnumValue("Number represents hundred-thousandths of a unit")
    NUMBER_REPRESENTS_HUNDRED_THOUSANDTHS_OF_A_UNIT("Number represents hundred-thousandths of a unit");
    private final String value;

    GrayResponseUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GrayResponseUnitType fromValue(String v) {
        for (GrayResponseUnitType c: GrayResponseUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
