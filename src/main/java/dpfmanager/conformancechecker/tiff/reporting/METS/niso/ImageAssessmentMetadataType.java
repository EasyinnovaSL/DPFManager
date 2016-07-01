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
 * <p>Clase Java para ImageAssessmentMetadataType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ImageAssessmentMetadataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SpatialMetrics" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="samplingFrequencyPlane" type="{http://www.loc.gov/mix/v20}typeOfSamplingFrequencyPlaneType" minOccurs="0"/>
 *                   &lt;element name="samplingFrequencyUnit" type="{http://www.loc.gov/mix/v20}typeOfSamplingFrequencyUnitType" minOccurs="0"/>
 *                   &lt;element name="xSamplingFrequency" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                   &lt;element name="ySamplingFrequency" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ImageColorEncoding" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BitsPerSample" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="bitsPerSampleValue" type="{http://www.loc.gov/mix/v20}positiveIntegerType" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="bitsPerSampleUnit" type="{http://www.loc.gov/mix/v20}typeOfBitsPerSampleUnitType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="samplesPerPixel" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                   &lt;element name="extraSamples" type="{http://www.loc.gov/mix/v20}typeOfExtraSamplesType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="Colormap" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="colormapReference" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
 *                             &lt;element name="embeddedColormap" type="{http://www.loc.gov/mix/v20}base64BinaryType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="GrayResponse" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="grayResponseCurve" type="{http://www.loc.gov/mix/v20}nonNegativeIntegerType" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="grayResponseUnit" type="{http://www.loc.gov/mix/v20}typeOfGrayResponseUnitType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="WhitePoint" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="whitePointXValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                             &lt;element name="whitePointYValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="PrimaryChromaticities" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="primaryChromaticitiesRedX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                             &lt;element name="primaryChromaticitiesRedY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                             &lt;element name="primaryChromaticitiesGreenX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                             &lt;element name="primaryChromaticitiesGreenY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                             &lt;element name="primaryChromaticitiesBlueX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                             &lt;element name="primaryChromaticitiesBlueY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
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
 *         &lt;element name="TargetData" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="targetType" type="{http://www.loc.gov/mix/v20}typeOfTargetTypeType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="TargetID" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="targetManufacturer" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="targetName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="targetNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="targetMedia" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="externalTarget" type="{http://www.loc.gov/mix/v20}URIType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="performanceData" type="{http://www.loc.gov/mix/v20}URIType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "ImageAssessmentMetadataType", propOrder = {
    "spatialMetrics",
    "imageColorEncoding",
    "targetData"
})
public class ImageAssessmentMetadataType {

    @XmlElement(name = "SpatialMetrics")
    protected ImageAssessmentMetadataType.SpatialMetrics spatialMetrics;
    @XmlElement(name = "ImageColorEncoding")
    protected ImageAssessmentMetadataType.ImageColorEncoding imageColorEncoding;
    @XmlElement(name = "TargetData")
    protected ImageAssessmentMetadataType.TargetData targetData;

    /**
     * Obtiene el valor de la propiedad spatialMetrics.
     * 
     * @return
     *     possible object is
     *     {@link ImageAssessmentMetadataType.SpatialMetrics }
     *     
     */
    public ImageAssessmentMetadataType.SpatialMetrics getSpatialMetrics() {
        return spatialMetrics;
    }

    /**
     * Define el valor de la propiedad spatialMetrics.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageAssessmentMetadataType.SpatialMetrics }
     *     
     */
    public void setSpatialMetrics(ImageAssessmentMetadataType.SpatialMetrics value) {
        this.spatialMetrics = value;
    }

    /**
     * Obtiene el valor de la propiedad imageColorEncoding.
     * 
     * @return
     *     possible object is
     *     {@link ImageAssessmentMetadataType.ImageColorEncoding }
     *     
     */
    public ImageAssessmentMetadataType.ImageColorEncoding getImageColorEncoding() {
        return imageColorEncoding;
    }

    /**
     * Define el valor de la propiedad imageColorEncoding.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageAssessmentMetadataType.ImageColorEncoding }
     *     
     */
    public void setImageColorEncoding(ImageAssessmentMetadataType.ImageColorEncoding value) {
        this.imageColorEncoding = value;
    }

    /**
     * Obtiene el valor de la propiedad targetData.
     * 
     * @return
     *     possible object is
     *     {@link ImageAssessmentMetadataType.TargetData }
     *     
     */
    public ImageAssessmentMetadataType.TargetData getTargetData() {
        return targetData;
    }

    /**
     * Define el valor de la propiedad targetData.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageAssessmentMetadataType.TargetData }
     *     
     */
    public void setTargetData(ImageAssessmentMetadataType.TargetData value) {
        this.targetData = value;
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
     *         &lt;element name="BitsPerSample" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="bitsPerSampleValue" type="{http://www.loc.gov/mix/v20}positiveIntegerType" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="bitsPerSampleUnit" type="{http://www.loc.gov/mix/v20}typeOfBitsPerSampleUnitType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="samplesPerPixel" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *         &lt;element name="extraSamples" type="{http://www.loc.gov/mix/v20}typeOfExtraSamplesType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="Colormap" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="colormapReference" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
     *                   &lt;element name="embeddedColormap" type="{http://www.loc.gov/mix/v20}base64BinaryType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="GrayResponse" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="grayResponseCurve" type="{http://www.loc.gov/mix/v20}nonNegativeIntegerType" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="grayResponseUnit" type="{http://www.loc.gov/mix/v20}typeOfGrayResponseUnitType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="WhitePoint" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="whitePointXValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                   &lt;element name="whitePointYValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="PrimaryChromaticities" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="primaryChromaticitiesRedX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                   &lt;element name="primaryChromaticitiesRedY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                   &lt;element name="primaryChromaticitiesGreenX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                   &lt;element name="primaryChromaticitiesGreenY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                   &lt;element name="primaryChromaticitiesBlueX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                   &lt;element name="primaryChromaticitiesBlueY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
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
        "bitsPerSample",
        "samplesPerPixel",
        "extraSamples",
        "colormap",
        "grayResponse",
        "whitePoint",
        "primaryChromaticities"
    })
    public static class ImageColorEncoding {

        @XmlElement(name = "BitsPerSample")
        protected ImageAssessmentMetadataType.ImageColorEncoding.BitsPerSample bitsPerSample;
        protected PositiveIntegerType samplesPerPixel;
        protected List<TypeOfExtraSamplesType> extraSamples;
        @XmlElement(name = "Colormap")
        protected ImageAssessmentMetadataType.ImageColorEncoding.Colormap colormap;
        @XmlElement(name = "GrayResponse")
        protected ImageAssessmentMetadataType.ImageColorEncoding.GrayResponse grayResponse;
        @XmlElement(name = "WhitePoint")
        protected List<ImageAssessmentMetadataType.ImageColorEncoding.WhitePoint> whitePoint;
        @XmlElement(name = "PrimaryChromaticities")
        protected List<ImageAssessmentMetadataType.ImageColorEncoding.PrimaryChromaticities> primaryChromaticities;

        /**
         * Obtiene el valor de la propiedad bitsPerSample.
         * 
         * @return
         *     possible object is
         *     {@link ImageAssessmentMetadataType.ImageColorEncoding.BitsPerSample }
         *     
         */
        public ImageAssessmentMetadataType.ImageColorEncoding.BitsPerSample getBitsPerSample() {
            return bitsPerSample;
        }

        /**
         * Define el valor de la propiedad bitsPerSample.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageAssessmentMetadataType.ImageColorEncoding.BitsPerSample }
         *     
         */
        public void setBitsPerSample(ImageAssessmentMetadataType.ImageColorEncoding.BitsPerSample value) {
            this.bitsPerSample = value;
        }

        /**
         * Obtiene el valor de la propiedad samplesPerPixel.
         * 
         * @return
         *     possible object is
         *     {@link PositiveIntegerType }
         *     
         */
        public PositiveIntegerType getSamplesPerPixel() {
            return samplesPerPixel;
        }

        /**
         * Define el valor de la propiedad samplesPerPixel.
         * 
         * @param value
         *     allowed object is
         *     {@link PositiveIntegerType }
         *     
         */
        public void setSamplesPerPixel(PositiveIntegerType value) {
            this.samplesPerPixel = value;
        }

        /**
         * Gets the value of the extraSamples property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the extraSamples property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExtraSamples().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypeOfExtraSamplesType }
         * 
         * 
         */
        public List<TypeOfExtraSamplesType> getExtraSamples() {
            if (extraSamples == null) {
                extraSamples = new ArrayList<TypeOfExtraSamplesType>();
            }
            return this.extraSamples;
        }

        /**
         * Obtiene el valor de la propiedad colormap.
         * 
         * @return
         *     possible object is
         *     {@link ImageAssessmentMetadataType.ImageColorEncoding.Colormap }
         *     
         */
        public ImageAssessmentMetadataType.ImageColorEncoding.Colormap getColormap() {
            return colormap;
        }

        /**
         * Define el valor de la propiedad colormap.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageAssessmentMetadataType.ImageColorEncoding.Colormap }
         *     
         */
        public void setColormap(ImageAssessmentMetadataType.ImageColorEncoding.Colormap value) {
            this.colormap = value;
        }

        /**
         * Obtiene el valor de la propiedad grayResponse.
         * 
         * @return
         *     possible object is
         *     {@link ImageAssessmentMetadataType.ImageColorEncoding.GrayResponse }
         *     
         */
        public ImageAssessmentMetadataType.ImageColorEncoding.GrayResponse getGrayResponse() {
            return grayResponse;
        }

        /**
         * Define el valor de la propiedad grayResponse.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageAssessmentMetadataType.ImageColorEncoding.GrayResponse }
         *     
         */
        public void setGrayResponse(ImageAssessmentMetadataType.ImageColorEncoding.GrayResponse value) {
            this.grayResponse = value;
        }

        /**
         * Gets the value of the whitePoint property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the whitePoint property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getWhitePoint().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ImageAssessmentMetadataType.ImageColorEncoding.WhitePoint }
         * 
         * 
         */
        public List<ImageAssessmentMetadataType.ImageColorEncoding.WhitePoint> getWhitePoint() {
            if (whitePoint == null) {
                whitePoint = new ArrayList<ImageAssessmentMetadataType.ImageColorEncoding.WhitePoint>();
            }
            return this.whitePoint;
        }

        /**
         * Gets the value of the primaryChromaticities property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the primaryChromaticities property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPrimaryChromaticities().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ImageAssessmentMetadataType.ImageColorEncoding.PrimaryChromaticities }
         * 
         * 
         */
        public List<ImageAssessmentMetadataType.ImageColorEncoding.PrimaryChromaticities> getPrimaryChromaticities() {
            if (primaryChromaticities == null) {
                primaryChromaticities = new ArrayList<ImageAssessmentMetadataType.ImageColorEncoding.PrimaryChromaticities>();
            }
            return this.primaryChromaticities;
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
         *         &lt;element name="bitsPerSampleValue" type="{http://www.loc.gov/mix/v20}positiveIntegerType" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="bitsPerSampleUnit" type="{http://www.loc.gov/mix/v20}typeOfBitsPerSampleUnitType" minOccurs="0"/>
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
            "bitsPerSampleValue",
            "bitsPerSampleUnit"
        })
        public static class BitsPerSample {

            protected List<PositiveIntegerType> bitsPerSampleValue;
            protected TypeOfBitsPerSampleUnitType bitsPerSampleUnit;

            /**
             * Gets the value of the bitsPerSampleValue property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the bitsPerSampleValue property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getBitsPerSampleValue().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link PositiveIntegerType }
             * 
             * 
             */
            public List<PositiveIntegerType> getBitsPerSampleValue() {
                if (bitsPerSampleValue == null) {
                    bitsPerSampleValue = new ArrayList<PositiveIntegerType>();
                }
                return this.bitsPerSampleValue;
            }

            /**
             * Obtiene el valor de la propiedad bitsPerSampleUnit.
             * 
             * @return
             *     possible object is
             *     {@link TypeOfBitsPerSampleUnitType }
             *     
             */
            public TypeOfBitsPerSampleUnitType getBitsPerSampleUnit() {
                return bitsPerSampleUnit;
            }

            /**
             * Define el valor de la propiedad bitsPerSampleUnit.
             * 
             * @param value
             *     allowed object is
             *     {@link TypeOfBitsPerSampleUnitType }
             *     
             */
            public void setBitsPerSampleUnit(TypeOfBitsPerSampleUnitType value) {
                this.bitsPerSampleUnit = value;
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
         *         &lt;element name="colormapReference" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
         *         &lt;element name="embeddedColormap" type="{http://www.loc.gov/mix/v20}base64BinaryType" minOccurs="0"/>
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
            "colormapReference",
            "embeddedColormap"
        })
        public static class Colormap {

            protected URIType colormapReference;
            protected Base64BinaryType embeddedColormap;

            /**
             * Obtiene el valor de la propiedad colormapReference.
             * 
             * @return
             *     possible object is
             *     {@link URIType }
             *     
             */
            public URIType getColormapReference() {
                return colormapReference;
            }

            /**
             * Define el valor de la propiedad colormapReference.
             * 
             * @param value
             *     allowed object is
             *     {@link URIType }
             *     
             */
            public void setColormapReference(URIType value) {
                this.colormapReference = value;
            }

            /**
             * Obtiene el valor de la propiedad embeddedColormap.
             * 
             * @return
             *     possible object is
             *     {@link Base64BinaryType }
             *     
             */
            public Base64BinaryType getEmbeddedColormap() {
                return embeddedColormap;
            }

            /**
             * Define el valor de la propiedad embeddedColormap.
             * 
             * @param value
             *     allowed object is
             *     {@link Base64BinaryType }
             *     
             */
            public void setEmbeddedColormap(Base64BinaryType value) {
                this.embeddedColormap = value;
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
         *         &lt;element name="grayResponseCurve" type="{http://www.loc.gov/mix/v20}nonNegativeIntegerType" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="grayResponseUnit" type="{http://www.loc.gov/mix/v20}typeOfGrayResponseUnitType" minOccurs="0"/>
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
            "grayResponseCurve",
            "grayResponseUnit"
        })
        public static class GrayResponse {

            protected List<NonNegativeIntegerType> grayResponseCurve;
            protected TypeOfGrayResponseUnitType grayResponseUnit;

            /**
             * Gets the value of the grayResponseCurve property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the grayResponseCurve property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getGrayResponseCurve().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link NonNegativeIntegerType }
             * 
             * 
             */
            public List<NonNegativeIntegerType> getGrayResponseCurve() {
                if (grayResponseCurve == null) {
                    grayResponseCurve = new ArrayList<NonNegativeIntegerType>();
                }
                return this.grayResponseCurve;
            }

            /**
             * Obtiene el valor de la propiedad grayResponseUnit.
             * 
             * @return
             *     possible object is
             *     {@link TypeOfGrayResponseUnitType }
             *     
             */
            public TypeOfGrayResponseUnitType getGrayResponseUnit() {
                return grayResponseUnit;
            }

            /**
             * Define el valor de la propiedad grayResponseUnit.
             * 
             * @param value
             *     allowed object is
             *     {@link TypeOfGrayResponseUnitType }
             *     
             */
            public void setGrayResponseUnit(TypeOfGrayResponseUnitType value) {
                this.grayResponseUnit = value;
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
         *         &lt;element name="primaryChromaticitiesRedX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *         &lt;element name="primaryChromaticitiesRedY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *         &lt;element name="primaryChromaticitiesGreenX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *         &lt;element name="primaryChromaticitiesGreenY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *         &lt;element name="primaryChromaticitiesBlueX" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *         &lt;element name="primaryChromaticitiesBlueY" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
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
            "primaryChromaticitiesRedX",
            "primaryChromaticitiesRedY",
            "primaryChromaticitiesGreenX",
            "primaryChromaticitiesGreenY",
            "primaryChromaticitiesBlueX",
            "primaryChromaticitiesBlueY"
        })
        public static class PrimaryChromaticities {

            protected RationalType primaryChromaticitiesRedX;
            protected RationalType primaryChromaticitiesRedY;
            protected RationalType primaryChromaticitiesGreenX;
            protected RationalType primaryChromaticitiesGreenY;
            protected RationalType primaryChromaticitiesBlueX;
            protected RationalType primaryChromaticitiesBlueY;

            /**
             * Obtiene el valor de la propiedad primaryChromaticitiesRedX.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getPrimaryChromaticitiesRedX() {
                return primaryChromaticitiesRedX;
            }

            /**
             * Define el valor de la propiedad primaryChromaticitiesRedX.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setPrimaryChromaticitiesRedX(RationalType value) {
                this.primaryChromaticitiesRedX = value;
            }

            /**
             * Obtiene el valor de la propiedad primaryChromaticitiesRedY.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getPrimaryChromaticitiesRedY() {
                return primaryChromaticitiesRedY;
            }

            /**
             * Define el valor de la propiedad primaryChromaticitiesRedY.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setPrimaryChromaticitiesRedY(RationalType value) {
                this.primaryChromaticitiesRedY = value;
            }

            /**
             * Obtiene el valor de la propiedad primaryChromaticitiesGreenX.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getPrimaryChromaticitiesGreenX() {
                return primaryChromaticitiesGreenX;
            }

            /**
             * Define el valor de la propiedad primaryChromaticitiesGreenX.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setPrimaryChromaticitiesGreenX(RationalType value) {
                this.primaryChromaticitiesGreenX = value;
            }

            /**
             * Obtiene el valor de la propiedad primaryChromaticitiesGreenY.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getPrimaryChromaticitiesGreenY() {
                return primaryChromaticitiesGreenY;
            }

            /**
             * Define el valor de la propiedad primaryChromaticitiesGreenY.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setPrimaryChromaticitiesGreenY(RationalType value) {
                this.primaryChromaticitiesGreenY = value;
            }

            /**
             * Obtiene el valor de la propiedad primaryChromaticitiesBlueX.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getPrimaryChromaticitiesBlueX() {
                return primaryChromaticitiesBlueX;
            }

            /**
             * Define el valor de la propiedad primaryChromaticitiesBlueX.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setPrimaryChromaticitiesBlueX(RationalType value) {
                this.primaryChromaticitiesBlueX = value;
            }

            /**
             * Obtiene el valor de la propiedad primaryChromaticitiesBlueY.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getPrimaryChromaticitiesBlueY() {
                return primaryChromaticitiesBlueY;
            }

            /**
             * Define el valor de la propiedad primaryChromaticitiesBlueY.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setPrimaryChromaticitiesBlueY(RationalType value) {
                this.primaryChromaticitiesBlueY = value;
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
         *         &lt;element name="whitePointXValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *         &lt;element name="whitePointYValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
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
            "whitePointXValue",
            "whitePointYValue"
        })
        public static class WhitePoint {

            protected RationalType whitePointXValue;
            protected RationalType whitePointYValue;

            /**
             * Obtiene el valor de la propiedad whitePointXValue.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getWhitePointXValue() {
                return whitePointXValue;
            }

            /**
             * Define el valor de la propiedad whitePointXValue.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setWhitePointXValue(RationalType value) {
                this.whitePointXValue = value;
            }

            /**
             * Obtiene el valor de la propiedad whitePointYValue.
             * 
             * @return
             *     possible object is
             *     {@link RationalType }
             *     
             */
            public RationalType getWhitePointYValue() {
                return whitePointYValue;
            }

            /**
             * Define el valor de la propiedad whitePointYValue.
             * 
             * @param value
             *     allowed object is
             *     {@link RationalType }
             *     
             */
            public void setWhitePointYValue(RationalType value) {
                this.whitePointYValue = value;
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
     *         &lt;element name="samplingFrequencyPlane" type="{http://www.loc.gov/mix/v20}typeOfSamplingFrequencyPlaneType" minOccurs="0"/>
     *         &lt;element name="samplingFrequencyUnit" type="{http://www.loc.gov/mix/v20}typeOfSamplingFrequencyUnitType" minOccurs="0"/>
     *         &lt;element name="xSamplingFrequency" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *         &lt;element name="ySamplingFrequency" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
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
        "samplingFrequencyPlane",
        "samplingFrequencyUnit",
        "xSamplingFrequency",
        "ySamplingFrequency"
    })
    public static class SpatialMetrics {

        protected TypeOfSamplingFrequencyPlaneType samplingFrequencyPlane;
        protected TypeOfSamplingFrequencyUnitType samplingFrequencyUnit;
        protected RationalType xSamplingFrequency;
        protected RationalType ySamplingFrequency;

        /**
         * Obtiene el valor de la propiedad samplingFrequencyPlane.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfSamplingFrequencyPlaneType }
         *     
         */
        public TypeOfSamplingFrequencyPlaneType getSamplingFrequencyPlane() {
            return samplingFrequencyPlane;
        }

        /**
         * Define el valor de la propiedad samplingFrequencyPlane.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfSamplingFrequencyPlaneType }
         *     
         */
        public void setSamplingFrequencyPlane(TypeOfSamplingFrequencyPlaneType value) {
            this.samplingFrequencyPlane = value;
        }

        /**
         * Obtiene el valor de la propiedad samplingFrequencyUnit.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfSamplingFrequencyUnitType }
         *     
         */
        public TypeOfSamplingFrequencyUnitType getSamplingFrequencyUnit() {
            return samplingFrequencyUnit;
        }

        /**
         * Define el valor de la propiedad samplingFrequencyUnit.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfSamplingFrequencyUnitType }
         *     
         */
        public void setSamplingFrequencyUnit(TypeOfSamplingFrequencyUnitType value) {
            this.samplingFrequencyUnit = value;
        }

        /**
         * Obtiene el valor de la propiedad xSamplingFrequency.
         * 
         * @return
         *     possible object is
         *     {@link RationalType }
         *     
         */
        public RationalType getXSamplingFrequency() {
            return xSamplingFrequency;
        }

        /**
         * Define el valor de la propiedad xSamplingFrequency.
         * 
         * @param value
         *     allowed object is
         *     {@link RationalType }
         *     
         */
        public void setXSamplingFrequency(RationalType value) {
            this.xSamplingFrequency = value;
        }

        /**
         * Obtiene el valor de la propiedad ySamplingFrequency.
         * 
         * @return
         *     possible object is
         *     {@link RationalType }
         *     
         */
        public RationalType getYSamplingFrequency() {
            return ySamplingFrequency;
        }

        /**
         * Define el valor de la propiedad ySamplingFrequency.
         * 
         * @param value
         *     allowed object is
         *     {@link RationalType }
         *     
         */
        public void setYSamplingFrequency(RationalType value) {
            this.ySamplingFrequency = value;
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
     *         &lt;element name="targetType" type="{http://www.loc.gov/mix/v20}typeOfTargetTypeType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="TargetID" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="targetManufacturer" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="targetName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="targetNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="targetMedia" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="externalTarget" type="{http://www.loc.gov/mix/v20}URIType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="performanceData" type="{http://www.loc.gov/mix/v20}URIType" maxOccurs="unbounded" minOccurs="0"/>
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
        "targetType",
        "targetID",
        "externalTarget",
        "performanceData"
    })
    public static class TargetData {

        protected List<TypeOfTargetTypeType> targetType;
        @XmlElement(name = "TargetID")
        protected List<ImageAssessmentMetadataType.TargetData.TargetID> targetID;
        protected List<URIType> externalTarget;
        protected List<URIType> performanceData;

        /**
         * Gets the value of the targetType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the targetType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTargetType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TypeOfTargetTypeType }
         * 
         * 
         */
        public List<TypeOfTargetTypeType> getTargetType() {
            if (targetType == null) {
                targetType = new ArrayList<TypeOfTargetTypeType>();
            }
            return this.targetType;
        }

        /**
         * Gets the value of the targetID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the targetID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTargetID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ImageAssessmentMetadataType.TargetData.TargetID }
         * 
         * 
         */
        public List<ImageAssessmentMetadataType.TargetData.TargetID> getTargetID() {
            if (targetID == null) {
                targetID = new ArrayList<ImageAssessmentMetadataType.TargetData.TargetID>();
            }
            return this.targetID;
        }

        /**
         * Gets the value of the externalTarget property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the externalTarget property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExternalTarget().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link URIType }
         * 
         * 
         */
        public List<URIType> getExternalTarget() {
            if (externalTarget == null) {
                externalTarget = new ArrayList<URIType>();
            }
            return this.externalTarget;
        }

        /**
         * Gets the value of the performanceData property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the performanceData property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPerformanceData().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link URIType }
         * 
         * 
         */
        public List<URIType> getPerformanceData() {
            if (performanceData == null) {
                performanceData = new ArrayList<URIType>();
            }
            return this.performanceData;
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
         *         &lt;element name="targetManufacturer" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="targetName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="targetNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="targetMedia" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
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
            "targetManufacturer",
            "targetName",
            "targetNo",
            "targetMedia"
        })
        public static class TargetID {

            protected StringType targetManufacturer;
            protected StringType targetName;
            protected StringType targetNo;
            protected StringType targetMedia;

            /**
             * Obtiene el valor de la propiedad targetManufacturer.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getTargetManufacturer() {
                return targetManufacturer;
            }

            /**
             * Define el valor de la propiedad targetManufacturer.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setTargetManufacturer(StringType value) {
                this.targetManufacturer = value;
            }

            /**
             * Obtiene el valor de la propiedad targetName.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getTargetName() {
                return targetName;
            }

            /**
             * Define el valor de la propiedad targetName.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setTargetName(StringType value) {
                this.targetName = value;
            }

            /**
             * Obtiene el valor de la propiedad targetNo.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getTargetNo() {
                return targetNo;
            }

            /**
             * Define el valor de la propiedad targetNo.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setTargetNo(StringType value) {
                this.targetNo = value;
            }

            /**
             * Obtiene el valor de la propiedad targetMedia.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getTargetMedia() {
                return targetMedia;
            }

            /**
             * Define el valor de la propiedad targetMedia.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setTargetMedia(StringType value) {
                this.targetMedia = value;
            }

        }

    }

}
