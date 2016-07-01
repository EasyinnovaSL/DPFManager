//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:52:21 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.premis;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para relatedObjectIdentifierComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="relatedObjectIdentifierComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relatedObjectIdentifierType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relatedObjectIdentifierValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relatedObjectSequence" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RelObjectXmlID" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="simpleLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relatedObjectIdentifierComplexType", propOrder = {
    "relatedObjectIdentifierType",
    "relatedObjectIdentifierValue",
    "relatedObjectSequence"
})
public class RelatedObjectIdentifierComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority relatedObjectIdentifierType;
    @XmlElement(required = true)
    protected String relatedObjectIdentifierValue;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger relatedObjectSequence;
    @XmlAttribute(name = "RelObjectXmlID")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object relObjectXmlID;
    @XmlAttribute(name = "simpleLink")
    @XmlSchemaType(name = "anyURI")
    protected String simpleLink;

    /**
     * Obtiene el valor de la propiedad relatedObjectIdentifierType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getRelatedObjectIdentifierType() {
        return relatedObjectIdentifierType;
    }

    /**
     * Define el valor de la propiedad relatedObjectIdentifierType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setRelatedObjectIdentifierType(StringPlusAuthority value) {
        this.relatedObjectIdentifierType = value;
    }

    /**
     * Obtiene el valor de la propiedad relatedObjectIdentifierValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelatedObjectIdentifierValue() {
        return relatedObjectIdentifierValue;
    }

    /**
     * Define el valor de la propiedad relatedObjectIdentifierValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelatedObjectIdentifierValue(String value) {
        this.relatedObjectIdentifierValue = value;
    }

    /**
     * Obtiene el valor de la propiedad relatedObjectSequence.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRelatedObjectSequence() {
        return relatedObjectSequence;
    }

    /**
     * Define el valor de la propiedad relatedObjectSequence.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRelatedObjectSequence(BigInteger value) {
        this.relatedObjectSequence = value;
    }

    /**
     * Obtiene el valor de la propiedad relObjectXmlID.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRelObjectXmlID() {
        return relObjectXmlID;
    }

    /**
     * Define el valor de la propiedad relObjectXmlID.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRelObjectXmlID(Object value) {
        this.relObjectXmlID = value;
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
