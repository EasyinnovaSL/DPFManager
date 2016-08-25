//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:52:21 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.premis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para environmentFunctionComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="environmentFunctionComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentFunctionType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentFunctionLevel"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentFunctionComplexType", propOrder = {
    "environmentFunctionType",
    "environmentFunctionLevel"
})
public class EnvironmentFunctionComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority environmentFunctionType;
    @XmlElement(required = true)
    protected String environmentFunctionLevel;

    /**
     * Obtiene el valor de la propiedad environmentFunctionType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getEnvironmentFunctionType() {
        return environmentFunctionType;
    }

    /**
     * Define el valor de la propiedad environmentFunctionType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setEnvironmentFunctionType(StringPlusAuthority value) {
        this.environmentFunctionType = value;
    }

    /**
     * Obtiene el valor de la propiedad environmentFunctionLevel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvironmentFunctionLevel() {
        return environmentFunctionLevel;
    }

    /**
     * Define el valor de la propiedad environmentFunctionLevel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvironmentFunctionLevel(String value) {
        this.environmentFunctionLevel = value;
    }

}
