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
 * <p>Clase Java para linkingObjectIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="linkingObjectIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingObjectIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingObjectIdentifierValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingObjectRole" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="LinkObjectXmlID" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="simpleLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "linkingObjectIdentifierComplexType", propOrder = {
    "linkingObjectIdentifierType",
    "linkingObjectIdentifierValue",
    "linkingObjectRole"
})
public class LinkingObjectIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority linkingObjectIdentifierType;
    @XmlElement(required = true)
    protected String linkingObjectIdentifierValue;
    protected List<StringPlusAuthority> linkingObjectRole;
    @XmlAttribute(name = "LinkObjectXmlID")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object linkObjectXmlID;
    @XmlAttribute(name = "simpleLink")
    @XmlSchemaType(name = "anyURI")
    protected String simpleLink;

    /**
     * Obtiene el valor de la propiedad linkingObjectIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getLinkingObjectIdentifierType() {
        return linkingObjectIdentifierType;
    }

    /**
     * Define el valor de la propiedad linkingObjectIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setLinkingObjectIdentifierType(StringPlusAuthority value) {
        this.linkingObjectIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad linkingObjectIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkingObjectIdentifierValue() {
        return linkingObjectIdentifierValue;
    }

    /**
     * Define el valor de la propiedad linkingObjectIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkingObjectIdentifierValue(String value) {
        this.linkingObjectIdentifierValue = value;
    }

    /**
     * Gets the value of the linkingObjectRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkingObjectRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkingObjectRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringPlusAuthority }
     * 
     * 
     */
    public List<StringPlusAuthority> getLinkingObjectRole() {
        if (linkingObjectRole == null) {
            linkingObjectRole = new ArrayList<StringPlusAuthority>();
        }
        return this.linkingObjectRole;
    }

    /**
     * Obtiene el valor de la propiedad linkObjectXmlID.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getLinkObjectXmlID() {
        return linkObjectXmlID;
    }

    /**
     * Define el valor de la propiedad linkObjectXmlID.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setLinkObjectXmlID(Object value) {
        this.linkObjectXmlID = value;
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
