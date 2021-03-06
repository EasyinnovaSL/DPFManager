/**
 * <h1>ImageAssessmentMetadataType.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Mar Llambi
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImageAssessmentMetadataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
    protected SpatialMetrics spatialMetrics;
    @XmlElement(name = "ImageColorEncoding")
    protected ImageColorEncoding imageColorEncoding;
    @XmlElement(name = "TargetData")
    protected TargetData targetData;

    /**
     * Gets the value of the spatialMetrics property.
     * 
     * @return
     *     possible object is
     *     {@link SpatialMetrics }
     *     
     */
    public SpatialMetrics getSpatialMetrics() {
        return spatialMetrics;
    }

    /**
     * Sets the value of the spatialMetrics property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpatialMetrics }
     *     
     */
    public void setSpatialMetrics(SpatialMetrics value) {
        this.spatialMetrics = value;
    }

    /**
     * Gets the value of the imageColorEncoding property.
     * 
     * @return
     *     possible object is
     *     {@link ImageColorEncoding }
     *     
     */
    public ImageColorEncoding getImageColorEncoding() {
        return imageColorEncoding;
    }

    /**
     * Sets the value of the imageColorEncoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageColorEncoding }
     *     
     */
    public void setImageColorEncoding(ImageColorEncoding value) {
        this.imageColorEncoding = value;
    }

    /**
     * Gets the value of the targetData property.
     * 
     * @return
     *     possible object is
     *     {@link TargetData }
     *     
     */
    public TargetData getTargetData() {
        return targetData;
    }

    /**
     * Sets the value of the targetData property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetData }
     *     
     */
    public void setTargetData(TargetData value) {
        this.targetData = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
        protected BitsPerSample bitsPerSample;
        protected PositiveIntegerType samplesPerPixel;
        protected List<TypeOfExtraSamplesType> extraSamples;
        @XmlElement(name = "Colormap")
        protected Colormap colormap;
        @XmlElement(name = "GrayResponse")
        protected GrayResponse grayResponse;
        @XmlElement(name = "WhitePoint")
        protected List<WhitePoint> whitePoint;
        @XmlElement(name = "PrimaryChromaticities")
        protected List<PrimaryChromaticities> primaryChromaticities;

        /**
         * Gets the value of the bitsPerSample property.
         * 
         * @return
         *     possible object is
         *     {@link BitsPerSample }
         *     
         */
        public BitsPerSample getBitsPerSample() {
            return bitsPerSample;
        }

        /**
         * Sets the value of the bitsPerSample property.
         * 
         * @param value
         *     allowed object is
         *     {@link BitsPerSample }
         *     
         */
        public void setBitsPerSample(BitsPerSample value) {
            this.bitsPerSample = value;
        }

        /**
         * Gets the value of the samplesPerPixel property.
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
         * Sets the value of the samplesPerPixel property.
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

        public void setExtraSamples(TypeOfExtraSamplesType t) {
            if (extraSamples == null) {
                extraSamples = new ArrayList<TypeOfExtraSamplesType>();
            }
            extraSamples.add(t);
        }

        /**
         * Gets the value of the colormap property.
         * 
         * @return
         *     possible object is
         *     {@link Colormap }
         *     
         */
        public Colormap getColormap() {
            return colormap;
        }

        /**
         * Sets the value of the colormap property.
         * 
         * @param value
         *     allowed object is
         *     {@link Colormap }
         *     
         */
        public void setColormap(Colormap value) {
            this.colormap = value;
        }

        /**
         * Gets the value of the grayResponse property.
         * 
         * @return
         *     possible object is
         *     {@link GrayResponse }
         *     
         */
        public GrayResponse getGrayResponse() {
            return grayResponse;
        }

        /**
         * Sets the value of the grayResponse property.
         * 
         * @param value
         *     allowed object is
         *     {@link GrayResponse }
         *     
         */
        public void setGrayResponse(GrayResponse value) {
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
         * {@link WhitePoint }
         * 
         * 
         */
        public List<WhitePoint> getWhitePoint() {
            if (whitePoint == null) {
                whitePoint = new ArrayList<WhitePoint>();
            }
            return this.whitePoint;
        }

        public void setWhitePoint(WhitePoint w) {
            if (whitePoint == null) {
                whitePoint = new ArrayList<WhitePoint>();
            }
            this.whitePoint.add(w);
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
         * {@link PrimaryChromaticities }
         * 
         * 
         */
        public List<PrimaryChromaticities> getPrimaryChromaticities() {
            if (primaryChromaticities == null) {
                primaryChromaticities = new ArrayList<PrimaryChromaticities>();
            }
            return this.primaryChromaticities;
        }

        public void setPrimaryChromaticities(PrimaryChromaticities p) {
            if (primaryChromaticities == null) {
                primaryChromaticities = new ArrayList<PrimaryChromaticities>();
            }
            this.primaryChromaticities.add(p);
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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

            public void setBitsPerSampleValue(PositiveIntegerType p) {
                if (bitsPerSampleValue == null) {
                    bitsPerSampleValue = new ArrayList<PositiveIntegerType>();
                }
                bitsPerSampleValue.add(p);
            }

            /**
             * Gets the value of the bitsPerSampleUnit property.
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
             * Sets the value of the bitsPerSampleUnit property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the colormapReference property.
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
             * Sets the value of the colormapReference property.
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
             * Gets the value of the embeddedColormap property.
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
             * Sets the value of the embeddedColormap property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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

            public void setGrayResponseCurve(NonNegativeIntegerType n) {
                if (grayResponseCurve == null) {
                    grayResponseCurve = new ArrayList<NonNegativeIntegerType>();
                }
                this.grayResponseCurve.add(n);
            }

            /**
             * Gets the value of the grayResponseUnit property.
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
             * Sets the value of the grayResponseUnit property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the primaryChromaticitiesRedX property.
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
             * Sets the value of the primaryChromaticitiesRedX property.
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
             * Gets the value of the primaryChromaticitiesRedY property.
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
             * Sets the value of the primaryChromaticitiesRedY property.
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
             * Gets the value of the primaryChromaticitiesGreenX property.
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
             * Sets the value of the primaryChromaticitiesGreenX property.
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
             * Gets the value of the primaryChromaticitiesGreenY property.
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
             * Sets the value of the primaryChromaticitiesGreenY property.
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
             * Gets the value of the primaryChromaticitiesBlueX property.
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
             * Sets the value of the primaryChromaticitiesBlueX property.
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
             * Gets the value of the primaryChromaticitiesBlueY property.
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
             * Sets the value of the primaryChromaticitiesBlueY property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the whitePointXValue property.
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
             * Sets the value of the whitePointXValue property.
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
             * Gets the value of the whitePointYValue property.
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
             * Sets the value of the whitePointYValue property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
         * Gets the value of the samplingFrequencyPlane property.
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
         * Sets the value of the samplingFrequencyPlane property.
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
         * Gets the value of the samplingFrequencyUnit property.
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
         * Sets the value of the samplingFrequencyUnit property.
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
         * Gets the value of the xSamplingFrequency property.
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
         * Sets the value of the xSamplingFrequency property.
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
         * Gets the value of the ySamplingFrequency property.
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
         * Sets the value of the ySamplingFrequency property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
        protected List<TargetID> targetID;
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
         * {@link TargetID }
         * 
         * 
         */
        public List<TargetID> getTargetID() {
            if (targetID == null) {
                targetID = new ArrayList<TargetID>();
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the targetManufacturer property.
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
             * Sets the value of the targetManufacturer property.
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
             * Gets the value of the targetName property.
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
             * Sets the value of the targetName property.
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
             * Gets the value of the targetNo property.
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
             * Sets the value of the targetNo property.
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
             * Gets the value of the targetMedia property.
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
             * Sets the value of the targetMedia property.
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
