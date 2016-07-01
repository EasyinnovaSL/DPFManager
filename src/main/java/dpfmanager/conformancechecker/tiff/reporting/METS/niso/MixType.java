//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:54:03 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mixType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mixType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BasicDigitalObjectInformation" type="{http://www.loc.gov/mix/v20}BasicDigitalObjectInformationType" minOccurs="0"/>
 *         &lt;element name="BasicImageInformation" type="{http://www.loc.gov/mix/v20}BasicImageInformationType" minOccurs="0"/>
 *         &lt;element name="ImageCaptureMetadata" type="{http://www.loc.gov/mix/v20}ImageCaptureMetadataType" minOccurs="0"/>
 *         &lt;element name="ImageAssessmentMetadata" type="{http://www.loc.gov/mix/v20}ImageAssessmentMetadataType" minOccurs="0"/>
 *         &lt;element name="ChangeHistory" type="{http://www.loc.gov/mix/v20}ChangeHistoryType" minOccurs="0"/>
 *         &lt;element name="Extension" type="{http://www.loc.gov/mix/v20}extensionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mixType", propOrder = {
    "basicDigitalObjectInformation",
    "basicImageInformation",
    "imageCaptureMetadata",
    "imageAssessmentMetadata",
    "changeHistory",
    "extension"
})
@XmlSeeAlso({
    Mix.class
})
public class MixType {

    @XmlElement(name = "BasicDigitalObjectInformation")
    protected BasicDigitalObjectInformationType basicDigitalObjectInformation;
    @XmlElement(name = "BasicImageInformation")
    protected BasicImageInformationType basicImageInformation;
    @XmlElement(name = "ImageCaptureMetadata")
    protected ImageCaptureMetadataType imageCaptureMetadata;
    @XmlElement(name = "ImageAssessmentMetadata")
    protected ImageAssessmentMetadataType imageAssessmentMetadata;
    @XmlElement(name = "ChangeHistory")
    protected ChangeHistoryType changeHistory;
    @XmlElement(name = "Extension")
    protected List<ExtensionType> extension;

    /**
     * Obtiene el valor de la propiedad basicDigitalObjectInformation.
     * 
     * @return
     *     possible object is
     *     {@link BasicDigitalObjectInformationType }
     *     
     */
    public BasicDigitalObjectInformationType getBasicDigitalObjectInformation() {
        return basicDigitalObjectInformation;
    }

    /**
     * Define el valor de la propiedad basicDigitalObjectInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicDigitalObjectInformationType }
     *     
     */
    public void setBasicDigitalObjectInformation(BasicDigitalObjectInformationType value) {
        this.basicDigitalObjectInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad basicImageInformation.
     * 
     * @return
     *     possible object is
     *     {@link BasicImageInformationType }
     *     
     */
    public BasicImageInformationType getBasicImageInformation() {
        return basicImageInformation;
    }

    /**
     * Define el valor de la propiedad basicImageInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicImageInformationType }
     *     
     */
    public void setBasicImageInformation(BasicImageInformationType value) {
        this.basicImageInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad imageCaptureMetadata.
     * 
     * @return
     *     possible object is
     *     {@link ImageCaptureMetadataType }
     *     
     */
    public ImageCaptureMetadataType getImageCaptureMetadata() {
        return imageCaptureMetadata;
    }

    /**
     * Define el valor de la propiedad imageCaptureMetadata.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageCaptureMetadataType }
     *     
     */
    public void setImageCaptureMetadata(ImageCaptureMetadataType value) {
        this.imageCaptureMetadata = value;
    }

    /**
     * Obtiene el valor de la propiedad imageAssessmentMetadata.
     * 
     * @return
     *     possible object is
     *     {@link ImageAssessmentMetadataType }
     *     
     */
    public ImageAssessmentMetadataType getImageAssessmentMetadata() {
        return imageAssessmentMetadata;
    }

    /**
     * Define el valor de la propiedad imageAssessmentMetadata.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageAssessmentMetadataType }
     *     
     */
    public void setImageAssessmentMetadata(ImageAssessmentMetadataType value) {
        this.imageAssessmentMetadata = value;
    }

    /**
     * Obtiene el valor de la propiedad changeHistory.
     * 
     * @return
     *     possible object is
     *     {@link ChangeHistoryType }
     *     
     */
    public ChangeHistoryType getChangeHistory() {
        return changeHistory;
    }

    /**
     * Define el valor de la propiedad changeHistory.
     * 
     * @param value
     *     allowed object is
     *     {@link ChangeHistoryType }
     *     
     */
    public void setChangeHistory(ChangeHistoryType value) {
        this.changeHistory = value;
    }

    /**
     * Gets the value of the extension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtensionType }
     * 
     * 
     */
    public List<ExtensionType> getExtension() {
        if (extension == null) {
            extension = new ArrayList<ExtensionType>();
        }
        return this.extension;
    }

}
