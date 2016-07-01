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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para relationshipComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="relationshipComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relationshipType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relationshipSubType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relatedObjectIdentifier" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relatedEventIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relatedEnvironmentPurpose" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relatedEnvironmentCharacteristic" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relationshipComplexType", propOrder = {
    "relationshipType",
    "relationshipSubType",
    "relatedObjectIdentifier",
    "relatedEventIdentifier",
    "relatedEnvironmentPurpose",
    "relatedEnvironmentCharacteristic"
})
public class RelationshipComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority relationshipType;
    @XmlElement(required = true)
    protected StringPlusAuthority relationshipSubType;
    @XmlElement(required = true)
    protected List<RelatedObjectIdentifierComplexType> relatedObjectIdentifier;
    protected List<RelatedEventIdentifierComplexType> relatedEventIdentifier;
    protected List<StringPlusAuthority> relatedEnvironmentPurpose;
    protected StringPlusAuthority relatedEnvironmentCharacteristic;

    /**
     * Obtiene el valor de la propiedad relationshipType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getRelationshipType() {
        return relationshipType;
    }

    /**
     * Define el valor de la propiedad relationshipType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setRelationshipType(StringPlusAuthority value) {
        this.relationshipType = value;
    }

    /**
     * Obtiene el valor de la propiedad relationshipSubType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getRelationshipSubType() {
        return relationshipSubType;
    }

    /**
     * Define el valor de la propiedad relationshipSubType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setRelationshipSubType(StringPlusAuthority value) {
        this.relationshipSubType = value;
    }

    /**
     * Gets the value of the relatedObjectIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relatedObjectIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedObjectIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelatedObjectIdentifierComplexType }
     * 
     * 
     */
    public List<RelatedObjectIdentifierComplexType> getRelatedObjectIdentifier() {
        if (relatedObjectIdentifier == null) {
            relatedObjectIdentifier = new ArrayList<RelatedObjectIdentifierComplexType>();
        }
        return this.relatedObjectIdentifier;
    }

    /**
     * Gets the value of the relatedEventIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relatedEventIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedEventIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelatedEventIdentifierComplexType }
     * 
     * 
     */
    public List<RelatedEventIdentifierComplexType> getRelatedEventIdentifier() {
        if (relatedEventIdentifier == null) {
            relatedEventIdentifier = new ArrayList<RelatedEventIdentifierComplexType>();
        }
        return this.relatedEventIdentifier;
    }

    /**
     * Gets the value of the relatedEnvironmentPurpose property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relatedEnvironmentPurpose property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedEnvironmentPurpose().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringPlusAuthority }
     * 
     * 
     */
    public List<StringPlusAuthority> getRelatedEnvironmentPurpose() {
        if (relatedEnvironmentPurpose == null) {
            relatedEnvironmentPurpose = new ArrayList<StringPlusAuthority>();
        }
        return this.relatedEnvironmentPurpose;
    }

    /**
     * Obtiene el valor de la propiedad relatedEnvironmentCharacteristic.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getRelatedEnvironmentCharacteristic() {
        return relatedEnvironmentCharacteristic;
    }

    /**
     * Define el valor de la propiedad relatedEnvironmentCharacteristic.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setRelatedEnvironmentCharacteristic(StringPlusAuthority value) {
        this.relatedEnvironmentCharacteristic = value;
    }

}
