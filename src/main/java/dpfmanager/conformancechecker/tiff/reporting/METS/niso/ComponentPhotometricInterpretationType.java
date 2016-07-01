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
 * <p>Clase Java para componentPhotometricInterpretationType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="componentPhotometricInterpretationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="G"/>
 *     &lt;enumeration value="B"/>
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="Cb"/>
 *     &lt;enumeration value="Cr"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "componentPhotometricInterpretationType")
@XmlEnum
public enum ComponentPhotometricInterpretationType {

    R("R"),
    G("G"),
    B("B"),
    Y("Y"),
    @XmlEnumValue("Cb")
    CB("Cb"),
    @XmlEnumValue("Cr")
    CR("Cr");
    private final String value;

    ComponentPhotometricInterpretationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ComponentPhotometricInterpretationType fromValue(String v) {
        for (ComponentPhotometricInterpretationType c: ComponentPhotometricInterpretationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
