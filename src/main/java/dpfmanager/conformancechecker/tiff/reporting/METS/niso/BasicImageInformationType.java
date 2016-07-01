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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para BasicImageInformationType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="BasicImageInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BasicImageCharacteristics" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="imageWidth" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                   &lt;element name="imageHeight" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                   &lt;element name="PhotometricInterpretation" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="colorSpace" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="ColorProfile" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="IccProfile" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="iccProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                                 &lt;element name="iccProfileVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                                 &lt;element name="iccProfileURI" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="LocalProfile" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="localProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                                 &lt;element name="localProfileURL" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="embeddedProfile" type="{http://www.loc.gov/mix/v20}base64BinaryType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="YCbCr" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="YCbCrSubSampling" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="yCbCrSubsampleHoriz" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleHorizType" minOccurs="0"/>
 *                                                 &lt;element name="yCbCrSubsampleVert" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleVertType" minOccurs="0"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="yCbCrPositioning" type="{http://www.loc.gov/mix/v20}typeOfYCbCrPositioningType" minOccurs="0"/>
 *                                       &lt;element name="YCbCrCoefficients" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="lumaRed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                                 &lt;element name="lumaGreen" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                                 &lt;element name="lumaBlue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="ReferenceBlackWhite" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Component" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="componentPhotometricInterpretation" type="{http://www.loc.gov/mix/v20}typeOfComponentPhotometricInterpretationType"/>
 *                                                 &lt;element name="footroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
 *                                                 &lt;element name="headroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SpecialFormatCharacteristics" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="JPEG2000" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="CodecCompliance" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="codec" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="codecVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="codestreamProfile" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="complianceClass" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="EncodingOptions" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Tiles" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="tileWidth" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                                                 &lt;element name="tileHeight" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="qualityLayers" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                                       &lt;element name="resolutionLevels" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="MrSID" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="zoomLevels" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Djvu" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="djvuFormat" type="{http://www.loc.gov/mix/v20}typeOfDjvuFormatType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BasicImageInformationType", propOrder = {
    "basicImageCharacteristics",
    "specialFormatCharacteristics"
})
public class BasicImageInformationType {

    @XmlElement(name = "BasicImageCharacteristics")
    protected BasicImageInformationType.BasicImageCharacteristics basicImageCharacteristics;
    @XmlElement(name = "SpecialFormatCharacteristics")
    protected BasicImageInformationType.SpecialFormatCharacteristics specialFormatCharacteristics;

    /**
     * Obtiene el valor de la propiedad basicImageCharacteristics.
     * 
     * @return
     *     possible object is
     *     {@link BasicImageInformationType.BasicImageCharacteristics }
     *     
     */
    public BasicImageInformationType.BasicImageCharacteristics getBasicImageCharacteristics() {
        return basicImageCharacteristics;
    }

    /**
     * Define el valor de la propiedad basicImageCharacteristics.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicImageInformationType.BasicImageCharacteristics }
     *     
     */
    public void setBasicImageCharacteristics(BasicImageInformationType.BasicImageCharacteristics value) {
        this.basicImageCharacteristics = value;
    }

    /**
     * Obtiene el valor de la propiedad specialFormatCharacteristics.
     * 
     * @return
     *     possible object is
     *     {@link BasicImageInformationType.SpecialFormatCharacteristics }
     *     
     */
    public BasicImageInformationType.SpecialFormatCharacteristics getSpecialFormatCharacteristics() {
        return specialFormatCharacteristics;
    }

    /**
     * Define el valor de la propiedad specialFormatCharacteristics.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicImageInformationType.SpecialFormatCharacteristics }
     *     
     */
    public void setSpecialFormatCharacteristics(BasicImageInformationType.SpecialFormatCharacteristics value) {
        this.specialFormatCharacteristics = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="imageWidth" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *         &lt;element name="imageHeight" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *         &lt;element name="PhotometricInterpretation" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="colorSpace" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="ColorProfile" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="IccProfile" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="iccProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                                       &lt;element name="iccProfileVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                                       &lt;element name="iccProfileURI" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="LocalProfile" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="localProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                                       &lt;element name="localProfileURL" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="embeddedProfile" type="{http://www.loc.gov/mix/v20}base64BinaryType" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="YCbCr" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="YCbCrSubSampling" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="yCbCrSubsampleHoriz" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleHorizType" minOccurs="0"/>
     *                                       &lt;element name="yCbCrSubsampleVert" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleVertType" minOccurs="0"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="yCbCrPositioning" type="{http://www.loc.gov/mix/v20}typeOfYCbCrPositioningType" minOccurs="0"/>
     *                             &lt;element name="YCbCrCoefficients" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="lumaRed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                                       &lt;element name="lumaGreen" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                                       &lt;element name="lumaBlue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="ReferenceBlackWhite" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Component" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="componentPhotometricInterpretation" type="{http://www.loc.gov/mix/v20}typeOfComponentPhotometricInterpretationType"/>
     *                                       &lt;element name="footroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
     *                                       &lt;element name="headroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "imageWidth",
        "imageHeight",
        "photometricInterpretation"
    })
    public static class BasicImageCharacteristics {

        protected PositiveIntegerType imageWidth;
        protected PositiveIntegerType imageHeight;
        @XmlElement(name = "PhotometricInterpretation")
        protected BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation photometricInterpretation;

        /**
         * Obtiene el valor de la propiedad imageWidth.
         * 
         * @return
         *     possible object is
         *     {@link PositiveIntegerType }
         *     
         */
        public PositiveIntegerType getImageWidth() {
            return imageWidth;
        }

        /**
         * Define el valor de la propiedad imageWidth.
         * 
         * @param value
         *     allowed object is
         *     {@link PositiveIntegerType }
         *     
         */
        public void setImageWidth(PositiveIntegerType value) {
            this.imageWidth = value;
        }

        /**
         * Obtiene el valor de la propiedad imageHeight.
         * 
         * @return
         *     possible object is
         *     {@link PositiveIntegerType }
         *     
         */
        public PositiveIntegerType getImageHeight() {
            return imageHeight;
        }

        /**
         * Define el valor de la propiedad imageHeight.
         * 
         * @param value
         *     allowed object is
         *     {@link PositiveIntegerType }
         *     
         */
        public void setImageHeight(PositiveIntegerType value) {
            this.imageHeight = value;
        }

        /**
         * Obtiene el valor de la propiedad photometricInterpretation.
         * 
         * @return
         *     possible object is
         *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation }
         *     
         */
        public BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation getPhotometricInterpretation() {
            return photometricInterpretation;
        }

        /**
         * Define el valor de la propiedad photometricInterpretation.
         * 
         * @param value
         *     allowed object is
         *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation }
         *     
         */
        public void setPhotometricInterpretation(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation value) {
            this.photometricInterpretation = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="colorSpace" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="ColorProfile" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="IccProfile" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="iccProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                             &lt;element name="iccProfileVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                             &lt;element name="iccProfileURI" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="LocalProfile" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="localProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                             &lt;element name="localProfileURL" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="embeddedProfile" type="{http://www.loc.gov/mix/v20}base64BinaryType" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="YCbCr" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="YCbCrSubSampling" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="yCbCrSubsampleHoriz" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleHorizType" minOccurs="0"/>
         *                             &lt;element name="yCbCrSubsampleVert" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleVertType" minOccurs="0"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="yCbCrPositioning" type="{http://www.loc.gov/mix/v20}typeOfYCbCrPositioningType" minOccurs="0"/>
         *                   &lt;element name="YCbCrCoefficients" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="lumaRed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                             &lt;element name="lumaGreen" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                             &lt;element name="lumaBlue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="ReferenceBlackWhite" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Component" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="componentPhotometricInterpretation" type="{http://www.loc.gov/mix/v20}typeOfComponentPhotometricInterpretationType"/>
         *                             &lt;element name="footroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
         *                             &lt;element name="headroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "colorSpace",
            "colorProfile",
            "yCbCr",
            "referenceBlackWhite"
        })
        public static class PhotometricInterpretation {

            protected StringType colorSpace;
            @XmlElement(name = "ColorProfile")
            protected BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile colorProfile;
            @XmlElement(name = "YCbCr")
            protected BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr yCbCr;
            @XmlElement(name = "ReferenceBlackWhite")
            protected List<BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite> referenceBlackWhite;

            /**
             * Obtiene el valor de la propiedad colorSpace.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getColorSpace() {
                return colorSpace;
            }

            /**
             * Define el valor de la propiedad colorSpace.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setColorSpace(StringType value) {
                this.colorSpace = value;
            }

            /**
             * Obtiene el valor de la propiedad colorProfile.
             * 
             * @return
             *     possible object is
             *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile }
             *     
             */
            public BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile getColorProfile() {
                return colorProfile;
            }

            /**
             * Define el valor de la propiedad colorProfile.
             * 
             * @param value
             *     allowed object is
             *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile }
             *     
             */
            public void setColorProfile(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile value) {
                this.colorProfile = value;
            }

            /**
             * Obtiene el valor de la propiedad yCbCr.
             * 
             * @return
             *     possible object is
             *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr }
             *     
             */
            public BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr getYCbCr() {
                return yCbCr;
            }

            /**
             * Define el valor de la propiedad yCbCr.
             * 
             * @param value
             *     allowed object is
             *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr }
             *     
             */
            public void setYCbCr(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr value) {
                this.yCbCr = value;
            }

            /**
             * Gets the value of the referenceBlackWhite property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the referenceBlackWhite property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getReferenceBlackWhite().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite }
             * 
             * 
             */
            public List<BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite> getReferenceBlackWhite() {
                if (referenceBlackWhite == null) {
                    referenceBlackWhite = new ArrayList<BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite>();
                }
                return this.referenceBlackWhite;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="IccProfile" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="iccProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *                   &lt;element name="iccProfileVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *                   &lt;element name="iccProfileURI" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="LocalProfile" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="localProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *                   &lt;element name="localProfileURL" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="embeddedProfile" type="{http://www.loc.gov/mix/v20}base64BinaryType" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "iccProfile",
                "localProfile",
                "embeddedProfile"
            })
            public static class ColorProfile {

                @XmlElement(name = "IccProfile")
                protected BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.IccProfile iccProfile;
                @XmlElement(name = "LocalProfile")
                protected BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.LocalProfile localProfile;
                protected Base64BinaryType embeddedProfile;

                /**
                 * Obtiene el valor de la propiedad iccProfile.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.IccProfile }
                 *     
                 */
                public BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.IccProfile getIccProfile() {
                    return iccProfile;
                }

                /**
                 * Define el valor de la propiedad iccProfile.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.IccProfile }
                 *     
                 */
                public void setIccProfile(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.IccProfile value) {
                    this.iccProfile = value;
                }

                /**
                 * Obtiene el valor de la propiedad localProfile.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.LocalProfile }
                 *     
                 */
                public BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.LocalProfile getLocalProfile() {
                    return localProfile;
                }

                /**
                 * Define el valor de la propiedad localProfile.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.LocalProfile }
                 *     
                 */
                public void setLocalProfile(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.LocalProfile value) {
                    this.localProfile = value;
                }

                /**
                 * Obtiene el valor de la propiedad embeddedProfile.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Base64BinaryType }
                 *     
                 */
                public Base64BinaryType getEmbeddedProfile() {
                    return embeddedProfile;
                }

                /**
                 * Define el valor de la propiedad embeddedProfile.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Base64BinaryType }
                 *     
                 */
                public void setEmbeddedProfile(Base64BinaryType value) {
                    this.embeddedProfile = value;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="iccProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
                 *         &lt;element name="iccProfileVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
                 *         &lt;element name="iccProfileURI" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "iccProfileName",
                    "iccProfileVersion",
                    "iccProfileURI"
                })
                public static class IccProfile {

                    protected StringType iccProfileName;
                    protected StringType iccProfileVersion;
                    protected URIType iccProfileURI;

                    /**
                     * Obtiene el valor de la propiedad iccProfileName.
                     * 
                     * @return
                     *     possible object is
                     *     {@link StringType }
                     *     
                     */
                    public StringType getIccProfileName() {
                        return iccProfileName;
                    }

                    /**
                     * Define el valor de la propiedad iccProfileName.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link StringType }
                     *     
                     */
                    public void setIccProfileName(StringType value) {
                        this.iccProfileName = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad iccProfileVersion.
                     * 
                     * @return
                     *     possible object is
                     *     {@link StringType }
                     *     
                     */
                    public StringType getIccProfileVersion() {
                        return iccProfileVersion;
                    }

                    /**
                     * Define el valor de la propiedad iccProfileVersion.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link StringType }
                     *     
                     */
                    public void setIccProfileVersion(StringType value) {
                        this.iccProfileVersion = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad iccProfileURI.
                     * 
                     * @return
                     *     possible object is
                     *     {@link URIType }
                     *     
                     */
                    public URIType getIccProfileURI() {
                        return iccProfileURI;
                    }

                    /**
                     * Define el valor de la propiedad iccProfileURI.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link URIType }
                     *     
                     */
                    public void setIccProfileURI(URIType value) {
                        this.iccProfileURI = value;
                    }

                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="localProfileName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
                 *         &lt;element name="localProfileURL" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "localProfileName",
                    "localProfileURL"
                })
                public static class LocalProfile {

                    protected StringType localProfileName;
                    protected URIType localProfileURL;

                    /**
                     * Obtiene el valor de la propiedad localProfileName.
                     * 
                     * @return
                     *     possible object is
                     *     {@link StringType }
                     *     
                     */
                    public StringType getLocalProfileName() {
                        return localProfileName;
                    }

                    /**
                     * Define el valor de la propiedad localProfileName.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link StringType }
                     *     
                     */
                    public void setLocalProfileName(StringType value) {
                        this.localProfileName = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad localProfileURL.
                     * 
                     * @return
                     *     possible object is
                     *     {@link URIType }
                     *     
                     */
                    public URIType getLocalProfileURL() {
                        return localProfileURL;
                    }

                    /**
                     * Define el valor de la propiedad localProfileURL.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link URIType }
                     *     
                     */
                    public void setLocalProfileURL(URIType value) {
                        this.localProfileURL = value;
                    }

                }

            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Component" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="componentPhotometricInterpretation" type="{http://www.loc.gov/mix/v20}typeOfComponentPhotometricInterpretationType"/>
             *                   &lt;element name="footroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
             *                   &lt;element name="headroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "component"
            })
            public static class ReferenceBlackWhite {

                @XmlElement(name = "Component")
                protected List<BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component> component;

                /**
                 * Gets the value of the component property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the component property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getComponent().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component }
                 * 
                 * 
                 */
                public List<BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component> getComponent() {
                    if (component == null) {
                        component = new ArrayList<BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component>();
                    }
                    return this.component;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="componentPhotometricInterpretation" type="{http://www.loc.gov/mix/v20}typeOfComponentPhotometricInterpretationType"/>
                 *         &lt;element name="footroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
                 *         &lt;element name="headroom" type="{http://www.loc.gov/mix/v20}rationalType"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "componentPhotometricInterpretation",
                    "footroom",
                    "headroom"
                })
                public static class Component {

                    @XmlElement(required = true)
                    protected TypeOfComponentPhotometricInterpretationType componentPhotometricInterpretation;
                    @XmlElement(required = true)
                    protected RationalType footroom;
                    @XmlElement(required = true)
                    protected RationalType headroom;

                    /**
                     * Obtiene el valor de la propiedad componentPhotometricInterpretation.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TypeOfComponentPhotometricInterpretationType }
                     *     
                     */
                    public TypeOfComponentPhotometricInterpretationType getComponentPhotometricInterpretation() {
                        return componentPhotometricInterpretation;
                    }

                    /**
                     * Define el valor de la propiedad componentPhotometricInterpretation.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TypeOfComponentPhotometricInterpretationType }
                     *     
                     */
                    public void setComponentPhotometricInterpretation(TypeOfComponentPhotometricInterpretationType value) {
                        this.componentPhotometricInterpretation = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad footroom.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getFootroom() {
                        return footroom;
                    }

                    /**
                     * Define el valor de la propiedad footroom.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setFootroom(RationalType value) {
                        this.footroom = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad headroom.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getHeadroom() {
                        return headroom;
                    }

                    /**
                     * Define el valor de la propiedad headroom.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setHeadroom(RationalType value) {
                        this.headroom = value;
                    }

                }

            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="YCbCrSubSampling" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="yCbCrSubsampleHoriz" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleHorizType" minOccurs="0"/>
             *                   &lt;element name="yCbCrSubsampleVert" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleVertType" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="yCbCrPositioning" type="{http://www.loc.gov/mix/v20}typeOfYCbCrPositioningType" minOccurs="0"/>
             *         &lt;element name="YCbCrCoefficients" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="lumaRed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *                   &lt;element name="lumaGreen" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *                   &lt;element name="lumaBlue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "yCbCrSubSampling",
                "yCbCrPositioning",
                "yCbCrCoefficients"
            })
            public static class YCbCr {

                @XmlElement(name = "YCbCrSubSampling")
                protected BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrSubSampling yCbCrSubSampling;
                protected TypeOfYCbCrPositioningType yCbCrPositioning;
                @XmlElement(name = "YCbCrCoefficients")
                protected BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrCoefficients yCbCrCoefficients;

                /**
                 * Obtiene el valor de la propiedad yCbCrSubSampling.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrSubSampling }
                 *     
                 */
                public BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrSubSampling getYCbCrSubSampling() {
                    return yCbCrSubSampling;
                }

                /**
                 * Define el valor de la propiedad yCbCrSubSampling.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrSubSampling }
                 *     
                 */
                public void setYCbCrSubSampling(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrSubSampling value) {
                    this.yCbCrSubSampling = value;
                }

                /**
                 * Obtiene el valor de la propiedad yCbCrPositioning.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfYCbCrPositioningType }
                 *     
                 */
                public TypeOfYCbCrPositioningType getYCbCrPositioning() {
                    return yCbCrPositioning;
                }

                /**
                 * Define el valor de la propiedad yCbCrPositioning.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfYCbCrPositioningType }
                 *     
                 */
                public void setYCbCrPositioning(TypeOfYCbCrPositioningType value) {
                    this.yCbCrPositioning = value;
                }

                /**
                 * Obtiene el valor de la propiedad yCbCrCoefficients.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrCoefficients }
                 *     
                 */
                public BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrCoefficients getYCbCrCoefficients() {
                    return yCbCrCoefficients;
                }

                /**
                 * Define el valor de la propiedad yCbCrCoefficients.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrCoefficients }
                 *     
                 */
                public void setYCbCrCoefficients(BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrCoefficients value) {
                    this.yCbCrCoefficients = value;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="lumaRed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
                 *         &lt;element name="lumaGreen" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
                 *         &lt;element name="lumaBlue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "lumaRed",
                    "lumaGreen",
                    "lumaBlue"
                })
                public static class YCbCrCoefficients {

                    protected RationalType lumaRed;
                    protected RationalType lumaGreen;
                    protected RationalType lumaBlue;

                    /**
                     * Obtiene el valor de la propiedad lumaRed.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getLumaRed() {
                        return lumaRed;
                    }

                    /**
                     * Define el valor de la propiedad lumaRed.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setLumaRed(RationalType value) {
                        this.lumaRed = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad lumaGreen.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getLumaGreen() {
                        return lumaGreen;
                    }

                    /**
                     * Define el valor de la propiedad lumaGreen.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setLumaGreen(RationalType value) {
                        this.lumaGreen = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad lumaBlue.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getLumaBlue() {
                        return lumaBlue;
                    }

                    /**
                     * Define el valor de la propiedad lumaBlue.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setLumaBlue(RationalType value) {
                        this.lumaBlue = value;
                    }

                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="yCbCrSubsampleHoriz" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleHorizType" minOccurs="0"/>
                 *         &lt;element name="yCbCrSubsampleVert" type="{http://www.loc.gov/mix/v20}typeOfYCbCrSubsampleVertType" minOccurs="0"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "yCbCrSubsampleHoriz",
                    "yCbCrSubsampleVert"
                })
                public static class YCbCrSubSampling {

                    protected TypeOfYCbCrSubsampleHorizType yCbCrSubsampleHoriz;
                    protected TypeOfYCbCrSubsampleVertType yCbCrSubsampleVert;

                    /**
                     * Obtiene el valor de la propiedad yCbCrSubsampleHoriz.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TypeOfYCbCrSubsampleHorizType }
                     *     
                     */
                    public TypeOfYCbCrSubsampleHorizType getYCbCrSubsampleHoriz() {
                        return yCbCrSubsampleHoriz;
                    }

                    /**
                     * Define el valor de la propiedad yCbCrSubsampleHoriz.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TypeOfYCbCrSubsampleHorizType }
                     *     
                     */
                    public void setYCbCrSubsampleHoriz(TypeOfYCbCrSubsampleHorizType value) {
                        this.yCbCrSubsampleHoriz = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad yCbCrSubsampleVert.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TypeOfYCbCrSubsampleVertType }
                     *     
                     */
                    public TypeOfYCbCrSubsampleVertType getYCbCrSubsampleVert() {
                        return yCbCrSubsampleVert;
                    }

                    /**
                     * Define el valor de la propiedad yCbCrSubsampleVert.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TypeOfYCbCrSubsampleVertType }
                     *     
                     */
                    public void setYCbCrSubsampleVert(TypeOfYCbCrSubsampleVertType value) {
                        this.yCbCrSubsampleVert = value;
                    }

                }

            }

        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="JPEG2000" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="CodecCompliance" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="codec" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="codecVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="codestreamProfile" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="complianceClass" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="EncodingOptions" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Tiles" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="tileWidth" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                                       &lt;element name="tileHeight" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="qualityLayers" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                             &lt;element name="resolutionLevels" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="MrSID" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="zoomLevels" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Djvu" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="djvuFormat" type="{http://www.loc.gov/mix/v20}typeOfDjvuFormatType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "jpeg2000",
        "mrSID",
        "djvu"
    })
    public static class SpecialFormatCharacteristics {

        @XmlElement(name = "JPEG2000")
        protected BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 jpeg2000;
        @XmlElement(name = "MrSID")
        protected BasicImageInformationType.SpecialFormatCharacteristics.MrSID mrSID;
        @XmlElement(name = "Djvu")
        protected BasicImageInformationType.SpecialFormatCharacteristics.Djvu djvu;

        /**
         * Obtiene el valor de la propiedad jpeg2000.
         * 
         * @return
         *     possible object is
         *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 }
         *     
         */
        public BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 getJPEG2000() {
            return jpeg2000;
        }

        /**
         * Define el valor de la propiedad jpeg2000.
         * 
         * @param value
         *     allowed object is
         *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 }
         *     
         */
        public void setJPEG2000(BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 value) {
            this.jpeg2000 = value;
        }

        /**
         * Obtiene el valor de la propiedad mrSID.
         * 
         * @return
         *     possible object is
         *     {@link BasicImageInformationType.SpecialFormatCharacteristics.MrSID }
         *     
         */
        public BasicImageInformationType.SpecialFormatCharacteristics.MrSID getMrSID() {
            return mrSID;
        }

        /**
         * Define el valor de la propiedad mrSID.
         * 
         * @param value
         *     allowed object is
         *     {@link BasicImageInformationType.SpecialFormatCharacteristics.MrSID }
         *     
         */
        public void setMrSID(BasicImageInformationType.SpecialFormatCharacteristics.MrSID value) {
            this.mrSID = value;
        }

        /**
         * Obtiene el valor de la propiedad djvu.
         * 
         * @return
         *     possible object is
         *     {@link BasicImageInformationType.SpecialFormatCharacteristics.Djvu }
         *     
         */
        public BasicImageInformationType.SpecialFormatCharacteristics.Djvu getDjvu() {
            return djvu;
        }

        /**
         * Define el valor de la propiedad djvu.
         * 
         * @param value
         *     allowed object is
         *     {@link BasicImageInformationType.SpecialFormatCharacteristics.Djvu }
         *     
         */
        public void setDjvu(BasicImageInformationType.SpecialFormatCharacteristics.Djvu value) {
            this.djvu = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="djvuFormat" type="{http://www.loc.gov/mix/v20}typeOfDjvuFormatType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "djvuFormat"
        })
        public static class Djvu {

            protected TypeOfDjvuFormatType djvuFormat;

            /**
             * Obtiene el valor de la propiedad djvuFormat.
             * 
             * @return
             *     possible object is
             *     {@link TypeOfDjvuFormatType }
             *     
             */
            public TypeOfDjvuFormatType getDjvuFormat() {
                return djvuFormat;
            }

            /**
             * Define el valor de la propiedad djvuFormat.
             * 
             * @param value
             *     allowed object is
             *     {@link TypeOfDjvuFormatType }
             *     
             */
            public void setDjvuFormat(TypeOfDjvuFormatType value) {
                this.djvuFormat = value;
            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="CodecCompliance" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="codec" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="codecVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="codestreamProfile" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="complianceClass" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="EncodingOptions" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Tiles" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="tileWidth" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *                             &lt;element name="tileHeight" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="qualityLayers" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *                   &lt;element name="resolutionLevels" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "codecCompliance",
            "encodingOptions"
        })
        public static class JPEG2000 {

            @XmlElement(name = "CodecCompliance")
            protected BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .CodecCompliance codecCompliance;
            @XmlElement(name = "EncodingOptions")
            protected BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions encodingOptions;

            /**
             * Obtiene el valor de la propiedad codecCompliance.
             * 
             * @return
             *     possible object is
             *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .CodecCompliance }
             *     
             */
            public BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .CodecCompliance getCodecCompliance() {
                return codecCompliance;
            }

            /**
             * Define el valor de la propiedad codecCompliance.
             * 
             * @param value
             *     allowed object is
             *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .CodecCompliance }
             *     
             */
            public void setCodecCompliance(BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .CodecCompliance value) {
                this.codecCompliance = value;
            }

            /**
             * Obtiene el valor de la propiedad encodingOptions.
             * 
             * @return
             *     possible object is
             *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions }
             *     
             */
            public BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions getEncodingOptions() {
                return encodingOptions;
            }

            /**
             * Define el valor de la propiedad encodingOptions.
             * 
             * @param value
             *     allowed object is
             *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions }
             *     
             */
            public void setEncodingOptions(BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions value) {
                this.encodingOptions = value;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="codec" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="codecVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="codestreamProfile" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="complianceClass" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "codec",
                "codecVersion",
                "codestreamProfile",
                "complianceClass"
            })
            public static class CodecCompliance {

                protected StringType codec;
                protected StringType codecVersion;
                protected StringType codestreamProfile;
                protected StringType complianceClass;

                /**
                 * Obtiene el valor de la propiedad codec.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getCodec() {
                    return codec;
                }

                /**
                 * Define el valor de la propiedad codec.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setCodec(StringType value) {
                    this.codec = value;
                }

                /**
                 * Obtiene el valor de la propiedad codecVersion.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getCodecVersion() {
                    return codecVersion;
                }

                /**
                 * Define el valor de la propiedad codecVersion.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setCodecVersion(StringType value) {
                    this.codecVersion = value;
                }

                /**
                 * Obtiene el valor de la propiedad codestreamProfile.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getCodestreamProfile() {
                    return codestreamProfile;
                }

                /**
                 * Define el valor de la propiedad codestreamProfile.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setCodestreamProfile(StringType value) {
                    this.codestreamProfile = value;
                }

                /**
                 * Obtiene el valor de la propiedad complianceClass.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getComplianceClass() {
                    return complianceClass;
                }

                /**
                 * Define el valor de la propiedad complianceClass.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setComplianceClass(StringType value) {
                    this.complianceClass = value;
                }

            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="Tiles" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="tileWidth" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
             *                   &lt;element name="tileHeight" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="qualityLayers" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
             *         &lt;element name="resolutionLevels" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "tiles",
                "qualityLayers",
                "resolutionLevels"
            })
            public static class EncodingOptions {

                @XmlElement(name = "Tiles")
                protected BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions.Tiles tiles;
                protected PositiveIntegerType qualityLayers;
                protected PositiveIntegerType resolutionLevels;

                /**
                 * Obtiene el valor de la propiedad tiles.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions.Tiles }
                 *     
                 */
                public BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions.Tiles getTiles() {
                    return tiles;
                }

                /**
                 * Define el valor de la propiedad tiles.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions.Tiles }
                 *     
                 */
                public void setTiles(BasicImageInformationType.SpecialFormatCharacteristics.JPEG2000 .EncodingOptions.Tiles value) {
                    this.tiles = value;
                }

                /**
                 * Obtiene el valor de la propiedad qualityLayers.
                 * 
                 * @return
                 *     possible object is
                 *     {@link PositiveIntegerType }
                 *     
                 */
                public PositiveIntegerType getQualityLayers() {
                    return qualityLayers;
                }

                /**
                 * Define el valor de la propiedad qualityLayers.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link PositiveIntegerType }
                 *     
                 */
                public void setQualityLayers(PositiveIntegerType value) {
                    this.qualityLayers = value;
                }

                /**
                 * Obtiene el valor de la propiedad resolutionLevels.
                 * 
                 * @return
                 *     possible object is
                 *     {@link PositiveIntegerType }
                 *     
                 */
                public PositiveIntegerType getResolutionLevels() {
                    return resolutionLevels;
                }

                /**
                 * Define el valor de la propiedad resolutionLevels.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link PositiveIntegerType }
                 *     
                 */
                public void setResolutionLevels(PositiveIntegerType value) {
                    this.resolutionLevels = value;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="tileWidth" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
                 *         &lt;element name="tileHeight" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "tileWidth",
                    "tileHeight"
                })
                public static class Tiles {

                    protected PositiveIntegerType tileWidth;
                    protected PositiveIntegerType tileHeight;

                    /**
                     * Obtiene el valor de la propiedad tileWidth.
                     * 
                     * @return
                     *     possible object is
                     *     {@link PositiveIntegerType }
                     *     
                     */
                    public PositiveIntegerType getTileWidth() {
                        return tileWidth;
                    }

                    /**
                     * Define el valor de la propiedad tileWidth.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link PositiveIntegerType }
                     *     
                     */
                    public void setTileWidth(PositiveIntegerType value) {
                        this.tileWidth = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad tileHeight.
                     * 
                     * @return
                     *     possible object is
                     *     {@link PositiveIntegerType }
                     *     
                     */
                    public PositiveIntegerType getTileHeight() {
                        return tileHeight;
                    }

                    /**
                     * Define el valor de la propiedad tileHeight.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link PositiveIntegerType }
                     *     
                     */
                    public void setTileHeight(PositiveIntegerType value) {
                        this.tileHeight = value;
                    }

                }

            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="zoomLevels" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "zoomLevels"
        })
        public static class MrSID {

            protected PositiveIntegerType zoomLevels;

            /**
             * Obtiene el valor de la propiedad zoomLevels.
             * 
             * @return
             *     possible object is
             *     {@link PositiveIntegerType }
             *     
             */
            public PositiveIntegerType getZoomLevels() {
                return zoomLevels;
            }

            /**
             * Define el valor de la propiedad zoomLevels.
             * 
             * @param value
             *     allowed object is
             *     {@link PositiveIntegerType }
             *     
             */
            public void setZoomLevels(PositiveIntegerType value) {
                this.zoomLevels = value;
            }

        }

    }

}
