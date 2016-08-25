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
 * <p>Clase Java para otherRightsInformationComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="otherRightsInformationComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsDocumentationIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsBasis"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsApplicableDates" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsNote" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otherRightsInformationComplexType", propOrder = {
    "otherRightsDocumentationIdentifier",
    "otherRightsBasis",
    "otherRightsApplicableDates",
    "otherRightsNote"
})
public class OtherRightsInformationComplexType {

    protected List<OtherRightsDocumentationIdentifierComplexType> otherRightsDocumentationIdentifier;
    @XmlElement(required = true)
    protected StringPlusAuthority otherRightsBasis;
    protected StartAndEndDateComplexType otherRightsApplicableDates;
    protected List<String> otherRightsNote;

    /**
     * Gets the value of the otherRightsDocumentationIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherRightsDocumentationIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherRightsDocumentationIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherRightsDocumentationIdentifierComplexType }
     * 
     * 
     */
    public List<OtherRightsDocumentationIdentifierComplexType> getOtherRightsDocumentationIdentifier() {
        if (otherRightsDocumentationIdentifier == null) {
            otherRightsDocumentationIdentifier = new ArrayList<OtherRightsDocumentationIdentifierComplexType>();
        }
        return this.otherRightsDocumentationIdentifier;
    }

    /**
     * Obtiene el valor de la propiedad otherRightsBasis.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getOtherRightsBasis() {
        return otherRightsBasis;
    }

    /**
     * Define el valor de la propiedad otherRightsBasis.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setOtherRightsBasis(StringPlusAuthority value) {
        this.otherRightsBasis = value;
    }

    /**
     * Obtiene el valor de la propiedad otherRightsApplicableDates.
     * 
     * @return
     *     possible object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public StartAndEndDateComplexType getOtherRightsApplicableDates() {
        return otherRightsApplicableDates;
    }

    /**
     * Define el valor de la propiedad otherRightsApplicableDates.
     * 
     * @param value
     *     allowed object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public void setOtherRightsApplicableDates(StartAndEndDateComplexType value) {
        this.otherRightsApplicableDates = value;
    }

    /**
     * Gets the value of the otherRightsNote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherRightsNote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherRightsNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOtherRightsNote() {
        if (otherRightsNote == null) {
            otherRightsNote = new ArrayList<String>();
        }
        return this.otherRightsNote;
    }

}
