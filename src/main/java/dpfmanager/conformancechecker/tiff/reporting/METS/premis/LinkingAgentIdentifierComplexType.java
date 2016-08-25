//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:52:21 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.premis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para linkingAgentIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="linkingAgentIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingAgentIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingAgentIdentifierValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingAgentRole" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="LinkAgentXmlID" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="simpleLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "linkingAgentIdentifierComplexType", propOrder = {
    "linkingAgentIdentifierType",
    "linkingAgentIdentifierValue",
    "linkingAgentRole"
})
public class LinkingAgentIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority linkingAgentIdentifierType;
    @XmlElement(required = true)
    protected String linkingAgentIdentifierValue;
    protected List<StringPlusAuthority> linkingAgentRole;
    @XmlAttribute(name = "LinkAgentXmlID")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object linkAgentXmlID;
    @XmlAttribute(name = "simpleLink")
    @XmlSchemaType(name = "anyURI")
    protected String simpleLink;

    /**
     * Obtiene el valor de la propiedad linkingAgentIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getLinkingAgentIdentifierType() {
        return linkingAgentIdentifierType;
    }

    /**
     * Define el valor de la propiedad linkingAgentIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setLinkingAgentIdentifierType(StringPlusAuthority value) {
        this.linkingAgentIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad linkingAgentIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkingAgentIdentifierValue() {
        return linkingAgentIdentifierValue;
    }

    /**
     * Define el valor de la propiedad linkingAgentIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkingAgentIdentifierValue(String value) {
        this.linkingAgentIdentifierValue = value;
    }

    /**
     * Gets the value of the linkingAgentRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkingAgentRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkingAgentRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringPlusAuthority }
     * 
     * 
     */
    public List<StringPlusAuthority> getLinkingAgentRole() {
        if (linkingAgentRole == null) {
            linkingAgentRole = new ArrayList<StringPlusAuthority>();
        }
        return this.linkingAgentRole;
    }

    /**
     * Obtiene el valor de la propiedad linkAgentXmlID.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getLinkAgentXmlID() {
        return linkAgentXmlID;
    }

    /**
     * Define el valor de la propiedad linkAgentXmlID.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setLinkAgentXmlID(Object value) {
        this.linkAgentXmlID = value;
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
