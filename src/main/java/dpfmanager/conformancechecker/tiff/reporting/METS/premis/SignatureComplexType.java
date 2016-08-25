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
 * <p>Clase Java para signatureComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="signatureComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}signatureEncoding"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}signer" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}signatureMethod"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}signatureValue"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}signatureValidationRules"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}signatureProperties" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}keyInformation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signatureComplexType", propOrder = {
    "signatureEncoding",
    "signer",
    "signatureMethod",
    "signatureValue",
    "signatureValidationRules",
    "signatureProperties",
    "keyInformation"
})
public class SignatureComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority signatureEncoding;
    protected StringPlusAuthority signer;
    @XmlElement(required = true)
    protected StringPlusAuthority signatureMethod;
    @XmlElement(required = true)
    protected String signatureValue;
    @XmlElement(required = true)
    protected StringPlusAuthority signatureValidationRules;
    protected List<String> signatureProperties;
    protected List<ExtensionComplexType> keyInformation;

    /**
     * Obtiene el valor de la propiedad signatureEncoding.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getSignatureEncoding() {
        return signatureEncoding;
    }

    /**
     * Define el valor de la propiedad signatureEncoding.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setSignatureEncoding(StringPlusAuthority value) {
        this.signatureEncoding = value;
    }

    /**
     * Obtiene el valor de la propiedad signer.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getSigner() {
        return signer;
    }

    /**
     * Define el valor de la propiedad signer.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setSigner(StringPlusAuthority value) {
        this.signer = value;
    }

    /**
     * Obtiene el valor de la propiedad signatureMethod.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getSignatureMethod() {
        return signatureMethod;
    }

    /**
     * Define el valor de la propiedad signatureMethod.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setSignatureMethod(StringPlusAuthority value) {
        this.signatureMethod = value;
    }

    /**
     * Obtiene el valor de la propiedad signatureValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureValue() {
        return signatureValue;
    }

    /**
     * Define el valor de la propiedad signatureValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureValue(String value) {
        this.signatureValue = value;
    }

    /**
     * Obtiene el valor de la propiedad signatureValidationRules.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getSignatureValidationRules() {
        return signatureValidationRules;
    }

    /**
     * Define el valor de la propiedad signatureValidationRules.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setSignatureValidationRules(StringPlusAuthority value) {
        this.signatureValidationRules = value;
    }

    /**
     * Gets the value of the signatureProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatureProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignatureProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSignatureProperties() {
        if (signatureProperties == null) {
            signatureProperties = new ArrayList<String>();
        }
        return this.signatureProperties;
    }

    /**
     * Gets the value of the keyInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtensionComplexType }
     * 
     * 
     */
    public List<ExtensionComplexType> getKeyInformation() {
        if (keyInformation == null) {
            keyInformation = new ArrayList<ExtensionComplexType>();
        }
        return this.keyInformation;
    }

}
