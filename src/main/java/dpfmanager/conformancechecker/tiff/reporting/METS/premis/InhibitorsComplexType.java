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
 * <p>Clase Java para inhibitorsComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="inhibitorsComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}inhibitorType"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}inhibitorTarget" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}inhibitorKey" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inhibitorsComplexType", propOrder = {
    "inhibitorType",
    "inhibitorTarget",
    "inhibitorKey"
})
public class InhibitorsComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority inhibitorType;
    protected List<StringPlusAuthority> inhibitorTarget;
    protected String inhibitorKey;

    /**
     * Obtiene el valor de la propiedad inhibitorType.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getInhibitorType() {
        return inhibitorType;
    }

    /**
     * Define el valor de la propiedad inhibitorType.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setInhibitorType(StringPlusAuthority value) {
        this.inhibitorType = value;
    }

    /**
     * Gets the value of the inhibitorTarget property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inhibitorTarget property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInhibitorTarget().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringPlusAuthority }
     * 
     * 
     */
    public List<StringPlusAuthority> getInhibitorTarget() {
        if (inhibitorTarget == null) {
            inhibitorTarget = new ArrayList<StringPlusAuthority>();
        }
        return this.inhibitorTarget;
    }

    /**
     * Obtiene el valor de la propiedad inhibitorKey.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInhibitorKey() {
        return inhibitorKey;
    }

    /**
     * Define el valor de la propiedad inhibitorKey.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInhibitorKey(String value) {
        this.inhibitorKey = value;
    }

}
