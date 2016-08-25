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
 * <p>Clase Java para copyrightDocumentationIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="copyrightDocumentationIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightDocumentationIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightDocumentationIdentifierValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightDocumentationRole" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "copyrightDocumentationIdentifierComplexType", propOrder = {
    "copyrightDocumentationIdentifierType",
    "copyrightDocumentationIdentifierValue",
    "copyrightDocumentationRole"
})
public class CopyrightDocumentationIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority copyrightDocumentationIdentifierType;
    @XmlElement(required = true)
    protected String copyrightDocumentationIdentifierValue;
    protected StringPlusAuthority copyrightDocumentationRole;

    /**
     * Obtiene el valor de la propiedad copyrightDocumentationIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getCopyrightDocumentationIdentifierType() {
        return copyrightDocumentationIdentifierType;
    }

    /**
     * Define el valor de la propiedad copyrightDocumentationIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setCopyrightDocumentationIdentifierType(StringPlusAuthority value) {
        this.copyrightDocumentationIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad copyrightDocumentationIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopyrightDocumentationIdentifierValue() {
        return copyrightDocumentationIdentifierValue;
    }

    /**
     * Define el valor de la propiedad copyrightDocumentationIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopyrightDocumentationIdentifierValue(String value) {
        this.copyrightDocumentationIdentifierValue = value;
    }

    /**
     * Obtiene el valor de la propiedad copyrightDocumentationRole.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getCopyrightDocumentationRole() {
        return copyrightDocumentationRole;
    }

    /**
     * Define el valor de la propiedad copyrightDocumentationRole.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setCopyrightDocumentationRole(StringPlusAuthority value) {
        this.copyrightDocumentationRole = value;
    }

}
