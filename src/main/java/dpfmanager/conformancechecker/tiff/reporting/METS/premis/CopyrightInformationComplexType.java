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
 * <p>Clase Java para copyrightInformationComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="copyrightInformationComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightStatus"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightJurisdiction"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightStatusDeterminationDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightNote" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightDocumentationIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightApplicableDates" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "copyrightInformationComplexType", propOrder = {
    "copyrightStatus",
    "copyrightJurisdiction",
    "copyrightStatusDeterminationDate",
    "copyrightNote",
    "copyrightDocumentationIdentifier",
    "copyrightApplicableDates"
})
public class CopyrightInformationComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority copyrightStatus;
    @XmlElement(required = true)
    protected CountryCode copyrightJurisdiction;
    protected String copyrightStatusDeterminationDate;
    protected List<String> copyrightNote;
    protected List<CopyrightDocumentationIdentifierComplexType> copyrightDocumentationIdentifier;
    protected StartAndEndDateComplexType copyrightApplicableDates;

    /**
     * Obtiene el valor de la propiedad copyrightStatus.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getCopyrightStatus() {
        return copyrightStatus;
    }

    /**
     * Define el valor de la propiedad copyrightStatus.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setCopyrightStatus(StringPlusAuthority value) {
        this.copyrightStatus = value;
    }

    /**
     * Obtiene el valor de la propiedad copyrightJurisdiction.
     * 
     * @return
     *     possible object is
     *     {@link CountryCode }
     *     
     */
    public CountryCode getCopyrightJurisdiction() {
        return copyrightJurisdiction;
    }

    /**
     * Define el valor de la propiedad copyrightJurisdiction.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCode }
     *     
     */
    public void setCopyrightJurisdiction(CountryCode value) {
        this.copyrightJurisdiction = value;
    }

    /**
     * Obtiene el valor de la propiedad copyrightStatusDeterminationDate.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopyrightStatusDeterminationDate() {
        return copyrightStatusDeterminationDate;
    }

    /**
     * Define el valor de la propiedad copyrightStatusDeterminationDate.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopyrightStatusDeterminationDate(String value) {
        this.copyrightStatusDeterminationDate = value;
    }

    /**
     * Gets the value of the copyrightNote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the copyrightNote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCopyrightNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCopyrightNote() {
        if (copyrightNote == null) {
            copyrightNote = new ArrayList<String>();
        }
        return this.copyrightNote;
    }

    /**
     * Gets the value of the copyrightDocumentationIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the copyrightDocumentationIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCopyrightDocumentationIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CopyrightDocumentationIdentifierComplexType }
     * 
     * 
     */
    public List<CopyrightDocumentationIdentifierComplexType> getCopyrightDocumentationIdentifier() {
        if (copyrightDocumentationIdentifier == null) {
            copyrightDocumentationIdentifier = new ArrayList<CopyrightDocumentationIdentifierComplexType>();
        }
        return this.copyrightDocumentationIdentifier;
    }

    /**
     * Obtiene el valor de la propiedad copyrightApplicableDates.
     * 
     * @return
     *     possible object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public StartAndEndDateComplexType getCopyrightApplicableDates() {
        return copyrightApplicableDates;
    }

    /**
     * Define el valor de la propiedad copyrightApplicableDates.
     * 
     * @param value
     *     allowed object is
     *     {@link StartAndEndDateComplexType }
     *     
     */
    public void setCopyrightApplicableDates(StartAndEndDateComplexType value) {
        this.copyrightApplicableDates = value;
    }

}
