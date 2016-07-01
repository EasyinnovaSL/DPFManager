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
 * <p>Clase Java para captureDeviceType.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="captureDeviceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="transmission scanner"/>
 *     &lt;enumeration value="reflection print scanner"/>
 *     &lt;enumeration value="digital still camera"/>
 *     &lt;enumeration value="still from video"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "captureDeviceType")
@XmlEnum
public enum CaptureDeviceType {

    @XmlEnumValue("transmission scanner")
    TRANSMISSION_SCANNER("transmission scanner"),
    @XmlEnumValue("reflection print scanner")
    REFLECTION_PRINT_SCANNER("reflection print scanner"),
    @XmlEnumValue("digital still camera")
    DIGITAL_STILL_CAMERA("digital still camera"),
    @XmlEnumValue("still from video")
    STILL_FROM_VIDEO("still from video");
    private final String value;

    CaptureDeviceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CaptureDeviceType fromValue(String v) {
        for (CaptureDeviceType c: CaptureDeviceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
