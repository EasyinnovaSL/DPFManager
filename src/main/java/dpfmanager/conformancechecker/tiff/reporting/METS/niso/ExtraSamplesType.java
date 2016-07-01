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
 * <p>Clase Java para extraSamplesType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
    UNSPECIFIED_DATA("unspecified data"),
    @XmlEnumValue("associated alpha data (with pre-multiplied color)")
    ASSOCIATED_ALPHA_DATA_WITH_PRE_MULTIPLIED_COLOR("associated alpha data (with pre-multiplied color)"),
    @XmlEnumValue("unassociated alpha data")
    UNASSOCIATED_ALPHA_DATA("unassociated alpha data"),
    @XmlEnumValue("range or depth data")
    RANGE_OR_DEPTH_DATA("range or depth data");
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

}
