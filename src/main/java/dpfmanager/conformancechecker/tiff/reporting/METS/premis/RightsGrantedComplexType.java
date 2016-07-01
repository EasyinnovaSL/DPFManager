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
 * <p>Clase Java para rightsGrantedComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="rightsGrantedComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}act"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}restriction" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}termOfGrant" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}termOfRestriction" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}rightsGrantedNote" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rightsGrantedComplexType", propOrder = {
    "act",
    "restriction",
    "termOfGrant",
    "termOfRestriction",
    "rightsGrantedNote"
})
public class RightsGrantedComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority act;
    protected List<StringPlusAuthority> restriction;
    protected StartAndEndDateComplexType termOfGrant;
    protected StartAndEndDateComplexType termOfRestriction;
    protected List<String> rightsGrantedNote;

    /**
     * Obtiene el valor de la propiedad act.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getAct() {
        return act;
    }

    /**
     * Define el valor de la propiedad act.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setAct(StringPlusAuthority value) {
        this.act = value;
    }

    /**
     * Gets the value of the restriction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the restriction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRestriction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringPlusAuthority }
     * 
     * 
     */
    public List<StringPlusAuthority> getRestriction() {
        if (restriction == null) {
            restriction = new ArrayList<StringPlusAuthority>();
        }
        return this.restriction;
    }

    /**
     * Obtiene el valor de la propiedad termOfGrant.
     * 
     * @return
     *     possible object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public StartAndEndDateComplexType getTermOfGrant() {
        return termOfGrant;
    }

    /**
     * Define el valor de la propiedad termOfGrant.
     * 
     * @param value
     *     allowed object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public void setTermOfGrant(StartAndEndDateComplexType value) {
        this.termOfGrant = value;
    }

    /**
     * Obtiene el valor de la propiedad termOfRestriction.
     * 
     * @return
     *     possible object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public StartAndEndDateComplexType getTermOfRestriction() {
        return termOfRestriction;
    }

    /**
     * Define el valor de la propiedad termOfRestriction.
     * 
     * @param value
     *     allowed object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public void setTermOfRestriction(StartAndEndDateComplexType value) {
        this.termOfRestriction = value;
    }

    /**
     * Gets the value of the rightsGrantedNote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rightsGrantedNote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRightsGrantedNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRightsGrantedNote() {
        if (rightsGrantedNote == null) {
            rightsGrantedNote = new ArrayList<String>();
        }
        return this.rightsGrantedNote;
    }

}
