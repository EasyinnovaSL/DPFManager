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
 * <p>Clase Java para statuteDocumentationIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="statuteDocumentationIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteDocumentationIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteDocumentationIdentifierValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteDocumentationRole" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "statuteDocumentationIdentifierComplexType", propOrder = {
    "statuteDocumentationIdentifierType",
    "statuteDocumentationIdentifierValue",
    "statuteDocumentationRole"
})
public class StatuteDocumentationIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority statuteDocumentationIdentifierType;
    @XmlElement(required = true)
    protected String statuteDocumentationIdentifierValue;
    protected StringPlusAuthority statuteDocumentationRole;

    /**
     * Obtiene el valor de la propiedad statuteDocumentationIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getStatuteDocumentationIdentifierType() {
        return statuteDocumentationIdentifierType;
    }

    /**
     * Define el valor de la propiedad statuteDocumentationIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setStatuteDocumentationIdentifierType(StringPlusAuthority value) {
        this.statuteDocumentationIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad statuteDocumentationIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatuteDocumentationIdentifierValue() {
        return statuteDocumentationIdentifierValue;
    }

    /**
     * Define el valor de la propiedad statuteDocumentationIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatuteDocumentationIdentifierValue(String value) {
        this.statuteDocumentationIdentifierValue = value;
    }

    /**
     * Obtiene el valor de la propiedad statuteDocumentationRole.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getStatuteDocumentationRole() {
        return statuteDocumentationRole;
    }

    /**
     * Define el valor de la propiedad statuteDocumentationRole.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setStatuteDocumentationRole(StringPlusAuthority value) {
        this.statuteDocumentationRole = value;
    }

}
