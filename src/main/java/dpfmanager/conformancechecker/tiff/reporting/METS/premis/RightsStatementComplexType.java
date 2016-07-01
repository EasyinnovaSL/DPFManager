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
 * <p>Clase Java para rightsStatementComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="rightsStatementComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}rightsStatementIdentifier"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}rightsBasis"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}copyrightInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}licenseInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}statuteInformation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}otherRightsInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}rightsGranted" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingObjectIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingAgentIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rightsStatementComplexType", propOrder = {
    "rightsStatementIdentifier",
    "rightsBasis",
    "copyrightInformation",
    "licenseInformation",
    "statuteInformation",
    "otherRightsInformation",
    "rightsGranted",
    "linkingObjectIdentifier",
    "linkingAgentIdentifier"
})
public class RightsStatementComplexType {

    @XmlElement(required = true)
    protected RightsStatementIdentifierComplexType rightsStatementIdentifier;
    @XmlElement(required = true)
    protected StringPlusAuthority rightsBasis;
    protected CopyrightInformationComplexType copyrightInformation;
    protected LicenseInformationComplexType licenseInformation;
    protected List<StatuteInformationComplexType> statuteInformation;
    protected OtherRightsInformationComplexType otherRightsInformation;
    protected List<RightsGrantedComplexType> rightsGranted;
    protected List<LinkingObjectIdentifierComplexType> linkingObjectIdentifier;
    protected List<LinkingAgentIdentifierComplexType> linkingAgentIdentifier;

    /**
     * Obtiene el valor de la propiedad rightsStatementIdentifier.
     * 
     * @return
     *     possible object is
     *     {@link RightsStatementIdentifierComplexType }
     *     
     */
    public RightsStatementIdentifierComplexType getRightsStatementIdentifier() {
        return rightsStatementIdentifier;
    }

    /**
     * Define el valor de la propiedad rightsStatementIdentifier.
     * 
     * @param value
     *     allowed object is
     *     {@link RightsStatementIdentifierComplexType }
     *     
     */
    public void setRightsStatementIdentifier(RightsStatementIdentifierComplexType value) {
        this.rightsStatementIdentifier = value;
    }

    /**
     * Obtiene el valor de la propiedad rightsBasis.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getRightsBasis() {
        return rightsBasis;
    }

    /**
     * Define el valor de la propiedad rightsBasis.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setRightsBasis(StringPlusAuthority value) {
        this.rightsBasis = value;
    }

    /**
     * Obtiene el valor de la propiedad copyrightInformation.
     * 
     * @return
     *     possible object is
     *     {@link CopyrightInformationComplexType }
     *     
     */
    public CopyrightInformationComplexType getCopyrightInformation() {
        return copyrightInformation;
    }

    /**
     * Define el valor de la propiedad copyrightInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link CopyrightInformationComplexType }
     *     
     */
    public void setCopyrightInformation(CopyrightInformationComplexType value) {
        this.copyrightInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad licenseInformation.
     * 
     * @return
     *     possible object is
     *     {@link LicenseInformationComplexType }
     *     
     */
    public LicenseInformationComplexType getLicenseInformation() {
        return licenseInformation;
    }

    /**
     * Define el valor de la propiedad licenseInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link LicenseInformationComplexType }
     *     
     */
    public void setLicenseInformation(LicenseInformationComplexType value) {
        this.licenseInformation = value;
    }

    /**
     * Gets the value of the statuteInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statuteInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatuteInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StatuteInformationComplexType }
     * 
     * 
     */
    public List<StatuteInformationComplexType> getStatuteInformation() {
        if (statuteInformation == null) {
            statuteInformation = new ArrayList<StatuteInformationComplexType>();
        }
        return this.statuteInformation;
    }

    /**
     * Obtiene el valor de la propiedad otherRightsInformation.
     * 
     * @return
     *     possible object is
     *     {@link OtherRightsInformationComplexType }
     *     
     */
    public OtherRightsInformationComplexType getOtherRightsInformation() {
        return otherRightsInformation;
    }

    /**
     * Define el valor de la propiedad otherRightsInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherRightsInformationComplexType }
     *     
     */
    public void setOtherRightsInformation(OtherRightsInformationComplexType value) {
        this.otherRightsInformation = value;
    }

    /**
     * Gets the value of the rightsGranted property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rightsGranted property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRightsGranted().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RightsGrantedComplexType }
     * 
     * 
     */
    public List<RightsGrantedComplexType> getRightsGranted() {
        if (rightsGranted == null) {
            rightsGranted = new ArrayList<RightsGrantedComplexType>();
        }
        return this.rightsGranted;
    }

    /**
     * Gets the value of the linkingObjectIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkingObjectIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkingObjectIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinkingObjectIdentifierComplexType }
     * 
     * 
     */
    public List<LinkingObjectIdentifierComplexType> getLinkingObjectIdentifier() {
        if (linkingObjectIdentifier == null) {
            linkingObjectIdentifier = new ArrayList<LinkingObjectIdentifierComplexType>();
        }
        return this.linkingObjectIdentifier;
    }

    /**
     * Gets the value of the linkingAgentIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkingAgentIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkingAgentIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinkingAgentIdentifierComplexType }
     * 
     * 
     */
    public List<LinkingAgentIdentifierComplexType> getLinkingAgentIdentifier() {
        if (linkingAgentIdentifier == null) {
            linkingAgentIdentifier = new ArrayList<LinkingAgentIdentifierComplexType>();
        }
        return this.linkingAgentIdentifier;
    }

}
