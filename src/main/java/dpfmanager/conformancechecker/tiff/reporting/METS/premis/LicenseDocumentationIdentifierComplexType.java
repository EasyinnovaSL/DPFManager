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
 * <p>Clase Java para licenseDocumentationIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="licenseDocumentationIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}licenseDocumentationIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}licenseDocumentationIdentifierValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}licenseDocumentationRole" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "licenseDocumentationIdentifierComplexType", propOrder = {
    "licenseDocumentationIdentifierType",
    "licenseDocumentationIdentifierValue",
    "licenseDocumentationRole"
})
public class LicenseDocumentationIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority licenseDocumentationIdentifierType;
    @XmlElement(required = true)
    protected String licenseDocumentationIdentifierValue;
    protected StringPlusAuthority licenseDocumentationRole;

    /**
     * Obtiene el valor de la propiedad licenseDocumentationIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getLicenseDocumentationIdentifierType() {
        return licenseDocumentationIdentifierType;
    }

    /**
     * Define el valor de la propiedad licenseDocumentationIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setLicenseDocumentationIdentifierType(StringPlusAuthority value) {
        this.licenseDocumentationIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad licenseDocumentationIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseDocumentationIdentifierValue() {
        return licenseDocumentationIdentifierValue;
    }

    /**
     * Define el valor de la propiedad licenseDocumentationIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseDocumentationIdentifierValue(String value) {
        this.licenseDocumentationIdentifierValue = value;
    }

    /**
     * Obtiene el valor de la propiedad licenseDocumentationRole.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getLicenseDocumentationRole() {
        return licenseDocumentationRole;
    }

    /**
     * Define el valor de la propiedad licenseDocumentationRole.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setLicenseDocumentationRole(StringPlusAuthority value) {
        this.licenseDocumentationRole = value;
    }

}
