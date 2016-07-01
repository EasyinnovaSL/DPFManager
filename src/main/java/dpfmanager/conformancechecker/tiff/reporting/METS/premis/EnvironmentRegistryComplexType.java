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
 * <p>Clase Java para environmentRegistryComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="environmentRegistryComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentRegistryName"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentRegistryKey"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentRegistryRole" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "environmentRegistryComplexType", propOrder = {
    "environmentRegistryName",
    "environmentRegistryKey",
    "environmentRegistryRole"
})
public class EnvironmentRegistryComplexType {

    @XmlElement(required = true)
    protected String environmentRegistryName;
    @XmlElement(required = true)
    protected String environmentRegistryKey;
    protected StringPlusAuthority environmentRegistryRole;

    /**
     * Obtiene el valor de la propiedad environmentRegistryName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvironmentRegistryName() {
        return environmentRegistryName;
    }

    /**
     * Define el valor de la propiedad environmentRegistryName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvironmentRegistryName(String value) {
        this.environmentRegistryName = value;
    }

    /**
     * Obtiene el valor de la propiedad environmentRegistryKey.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvironmentRegistryKey() {
        return environmentRegistryKey;
    }

    /**
     * Define el valor de la propiedad environmentRegistryKey.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvironmentRegistryKey(String value) {
        this.environmentRegistryKey = value;
    }

    /**
     * Obtiene el valor de la propiedad environmentRegistryRole.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getEnvironmentRegistryRole() {
        return environmentRegistryRole;
    }

    /**
     * Define el valor de la propiedad environmentRegistryRole.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setEnvironmentRegistryRole(StringPlusAuthority value) {
        this.environmentRegistryRole = value;
    }

}
