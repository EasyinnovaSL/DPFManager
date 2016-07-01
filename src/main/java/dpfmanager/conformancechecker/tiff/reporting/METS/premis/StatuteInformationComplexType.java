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
 * <p>Clase Java para statuteInformationComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="statuteInformationComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteJurisdiction"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteCitation"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteInformationDeterminationDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteNote" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteDocumentationIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteApplicableDates" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "statuteInformationComplexType", propOrder = {
    "statuteJurisdiction",
    "statuteCitation",
    "statuteInformationDeterminationDate",
    "statuteNote",
    "statuteDocumentationIdentifier",
    "statuteApplicableDates"
})
public class StatuteInformationComplexType {

    @XmlElement(required = true)
    protected CountryCode statuteJurisdiction;
    @XmlElement(required = true)
    protected StringPlusAuthority statuteCitation;
    protected String statuteInformationDeterminationDate;
    protected List<String> statuteNote;
    protected List<StatuteDocumentationIdentifierComplexType> statuteDocumentationIdentifier;
    protected StartAndEndDateComplexType statuteApplicableDates;

    /**
     * Obtiene el valor de la propiedad statuteJurisdiction.
     * 
     * @return
     *     possible object is
     *     {@link CountryCode }
     *     
     */
    public CountryCode getStatuteJurisdiction() {
        return statuteJurisdiction;
    }

    /**
     * Define el valor de la propiedad statuteJurisdiction.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCode }
     *     
     */
    public void setStatuteJurisdiction(CountryCode value) {
        this.statuteJurisdiction = value;
    }

    /**
     * Obtiene el valor de la propiedad statuteCitation.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getStatuteCitation() {
        return statuteCitation;
    }

    /**
     * Define el valor de la propiedad statuteCitation.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setStatuteCitation(StringPlusAuthority value) {
        this.statuteCitation = value;
    }

    /**
     * Obtiene el valor de la propiedad statuteInformationDeterminationDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatuteInformationDeterminationDate() {
        return statuteInformationDeterminationDate;
    }

    /**
     * Define el valor de la propiedad statuteInformationDeterminationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatuteInformationDeterminationDate(String value) {
        this.statuteInformationDeterminationDate = value;
    }

    /**
     * Gets the value of the statuteNote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statuteNote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatuteNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getStatuteNote() {
        if (statuteNote == null) {
            statuteNote = new ArrayList<String>();
        }
        return this.statuteNote;
    }

    /**
     * Gets the value of the statuteDocumentationIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statuteDocumentationIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatuteDocumentationIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StatuteDocumentationIdentifierComplexType }
     * 
     * 
     */
    public List<StatuteDocumentationIdentifierComplexType> getStatuteDocumentationIdentifier() {
        if (statuteDocumentationIdentifier == null) {
            statuteDocumentationIdentifier = new ArrayList<StatuteDocumentationIdentifierComplexType>();
        }
        return this.statuteDocumentationIdentifier;
    }

    /**
     * Obtiene el valor de la propiedad statuteApplicableDates.
     * 
     * @return
     *     possible object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public StartAndEndDateComplexType getStatuteApplicableDates() {
        return statuteApplicableDates;
    }

    /**
     * Define el valor de la propiedad statuteApplicableDates.
     * 
     * @param value
     *     allowed object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public void setStatuteApplicableDates(StartAndEndDateComplexType value) {
        this.statuteApplicableDates = value;
    }

}
