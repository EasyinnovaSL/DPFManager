//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:52:21 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.premis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para linkingRightsStatementIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="linkingRightsStatementIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingRightsStatementIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingRightsStatementIdentifierValue"/>
 *       &lt;/sequence>
 *       &lt;attribute name="LinkPermissionStatementXmlID" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="simpleLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "linkingRightsStatementIdentifierComplexType", propOrder = {
    "linkingRightsStatementIdentifierType",
    "linkingRightsStatementIdentifierValue"
})
public class LinkingRightsStatementIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority linkingRightsStatementIdentifierType;
    @XmlElement(required = true)
    protected String linkingRightsStatementIdentifierValue;
    @XmlAttribute(name = "LinkPermissionStatementXmlID")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object linkPermissionStatementXmlID;
    @XmlAttribute(name = "simpleLink")
    @XmlSchemaType(name = "anyURI")
    protected String simpleLink;

    /**
     * Obtiene el valor de la propiedad linkingRightsStatementIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getLinkingRightsStatementIdentifierType() {
        return linkingRightsStatementIdentifierType;
    }

    /**
     * Define el valor de la propiedad linkingRightsStatementIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setLinkingRightsStatementIdentifierType(StringPlusAuthority value) {
        this.linkingRightsStatementIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad linkingRightsStatementIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkingRightsStatementIdentifierValue() {
        return linkingRightsStatementIdentifierValue;
    }

    /**
     * Define el valor de la propiedad linkingRightsStatementIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkingRightsStatementIdentifierValue(String value) {
        this.linkingRightsStatementIdentifierValue = value;
    }

    /**
     * Obtiene el valor de la propiedad linkPermissionStatementXmlID.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getLinkPermissionStatementXmlID() {
        return linkPermissionStatementXmlID;
    }

    /**
     * Define el valor de la propiedad linkPermissionStatementXmlID.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setLinkPermissionStatementXmlID(Object value) {
        this.linkPermissionStatementXmlID = value;
    }

    /**
     * Obtiene el valor de la propiedad simpleLink.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimpleLink() {
        return simpleLink;
    }

    /**
     * Define el valor de la propiedad simpleLink.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimpleLink(String value) {
        this.simpleLink = value;
    }

}
