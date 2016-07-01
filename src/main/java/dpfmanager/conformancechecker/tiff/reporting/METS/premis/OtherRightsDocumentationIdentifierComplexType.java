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
 * <p>Clase Java para otherRightsDocumentationIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="otherRightsDocumentationIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsDocumentationIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsDocumentationIdentifierValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsDocumentationRole" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otherRightsDocumentationIdentifierComplexType", propOrder = {
    "otherRightsDocumentationIdentifierType",
    "otherRightsDocumentationIdentifierValue",
    "otherRightsDocumentationRole"
})
public class OtherRightsDocumentationIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority otherRightsDocumentationIdentifierType;
    @XmlElement(required = true)
    protected String otherRightsDocumentationIdentifierValue;
    protected StringPlusAuthority otherRightsDocumentationRole;

    /**
     * Obtiene el valor de la propiedad otherRightsDocumentationIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getOtherRightsDocumentationIdentifierType() {
        return otherRightsDocumentationIdentifierType;
    }

    /**
     * Define el valor de la propiedad otherRightsDocumentationIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setOtherRightsDocumentationIdentifierType(StringPlusAuthority value) {
        this.otherRightsDocumentationIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad otherRightsDocumentationIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherRightsDocumentationIdentifierValue() {
        return otherRightsDocumentationIdentifierValue;
    }

    /**
     * Define el valor de la propiedad otherRightsDocumentationIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherRightsDocumentationIdentifierValue(String value) {
        this.otherRightsDocumentationIdentifierValue = value;
    }

    /**
     * Obtiene el valor de la propiedad otherRightsDocumentationRole.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getOtherRightsDocumentationRole() {
        return otherRightsDocumentationRole;
    }

    /**
     * Define el valor de la propiedad otherRightsDocumentationRole.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setOtherRightsDocumentationRole(StringPlusAuthority value) {
        this.otherRightsDocumentationRole = value;
    }

}
