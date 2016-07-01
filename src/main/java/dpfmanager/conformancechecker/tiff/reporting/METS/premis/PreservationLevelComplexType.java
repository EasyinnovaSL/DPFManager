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
 * <p>Clase Java para preservationLevelComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="preservationLevelComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}preservationLevelType" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}preservationLevelValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}preservationLevelRole" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}preservationLevelRationale" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}preservationLevelDateAssigned" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "preservationLevelComplexType", propOrder = {
    "preservationLevelType",
    "preservationLevelValue",
    "preservationLevelRole",
    "preservationLevelRationale",
    "preservationLevelDateAssigned"
})
public class PreservationLevelComplexType {

    protected StringPlusAuthority preservationLevelType;
    @XmlElement(required = true)
    protected StringPlusAuthority preservationLevelValue;
    protected StringPlusAuthority preservationLevelRole;
    protected List<String> preservationLevelRationale;
    protected String preservationLevelDateAssigned;

    /**
     * Obtiene el valor de la propiedad preservationLevelType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getPreservationLevelType() {
        return preservationLevelType;
    }

    /**
     * Define el valor de la propiedad preservationLevelType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setPreservationLevelType(StringPlusAuthority value) {
        this.preservationLevelType = value;
    }

    /**
     * Obtiene el valor de la propiedad preservationLevelValue.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getPreservationLevelValue() {
        return preservationLevelValue;
    }

    /**
     * Define el valor de la propiedad preservationLevelValue.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setPreservationLevelValue(StringPlusAuthority value) {
        this.preservationLevelValue = value;
    }

    /**
     * Obtiene el valor de la propiedad preservationLevelRole.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getPreservationLevelRole() {
        return preservationLevelRole;
    }

    /**
     * Define el valor de la propiedad preservationLevelRole.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setPreservationLevelRole(StringPlusAuthority value) {
        this.preservationLevelRole = value;
    }

    /**
     * Gets the value of the preservationLevelRationale property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the preservationLevelRationale property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreservationLevelRationale().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPreservationLevelRationale() {
        if (preservationLevelRationale == null) {
            preservationLevelRationale = new ArrayList<String>();
        }
        return this.preservationLevelRationale;
    }

    /**
     * Obtiene el valor de la propiedad preservationLevelDateAssigned.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreservationLevelDateAssigned() {
        return preservationLevelDateAssigned;
    }

    /**
     * Define el valor de la propiedad preservationLevelDateAssigned.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreservationLevelDateAssigned(String value) {
        this.preservationLevelDateAssigned = value;
    }

}
