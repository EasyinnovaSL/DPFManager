//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:52:21 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.premis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para fixityComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="fixityComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}messageDigestAlgorithm"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}messageDigest"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}messageDigestOriginator" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fixityComplexType", propOrder = {
    "messageDigestAlgorithm",
    "messageDigest",
    "messageDigestOriginator"
})
public class FixityComplexType {

    @XmlElement(required = true)
    protected StringPlusAuthority messageDigestAlgorithm;
    @XmlElement(required = true)
    protected String messageDigest;
    protected StringPlusAuthority messageDigestOriginator;

    /**
     * Obtiene el valor de la propiedad messageDigestAlgorithm.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getMessageDigestAlgorithm() {
        return messageDigestAlgorithm;
    }

    /**
     * Define el valor de la propiedad messageDigestAlgorithm.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setMessageDigestAlgorithm(StringPlusAuthority value) {
        this.messageDigestAlgorithm = value;
    }

    /**
     * Obtiene el valor de la propiedad messageDigest.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDigest() {
        return messageDigest;
    }

    /**
     * Define el valor de la propiedad messageDigest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDigest(String value) {
        this.messageDigest = value;
    }

    /**
     * Obtiene el valor de la propiedad messageDigestOriginator.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getMessageDigestOriginator() {
        return messageDigestOriginator;
    }

    /**
     * Define el valor de la propiedad messageDigestOriginator.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setMessageDigestOriginator(StringPlusAuthority value) {
        this.messageDigestOriginator = value;
    }

}
