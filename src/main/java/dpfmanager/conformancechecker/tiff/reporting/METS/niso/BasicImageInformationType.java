/**
 * <h1>BasicImageInformationType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for BasicImageInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
    protected BasicImageCharacteristics basicImageCharacteristics;
    @XmlElement(name = "SpecialFormatCharacteristics")
    protected SpecialFormatCharacteristics specialFormatCharacteristics;

    /**
     * Gets the value of the basicImageCharacteristics property.
     * 
     * @return
     *     possible object is
     *     {@link BasicImageCharacteristics }
     *     
     */
    public BasicImageCharacteristics getBasicImageCharacteristics() {
        return basicImageCharacteristics;
    }

    /**
     * Sets the value of the basicImageCharacteristics property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicImageCharacteristics }
     *     
     */
    public void setBasicImageCharacteristics(BasicImageCharacteristics value) {
        this.basicImageCharacteristics = value;
    }

    /**
     * Gets the value of the specialFormatCharacteristics property.
     * 
     * @return
     *     possible object is
     *     {@link SpecialFormatCharacteristics }
     *     
     */
    public SpecialFormatCharacteristics getSpecialFormatCharacteristics() {
        return specialFormatCharacteristics;
    }

    /**
     * Sets the value of the specialFormatCharacteristics property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpecialFormatCharacteristics }
     *     
     */
    public void setSpecialFormatCharacteristics(SpecialFormatCharacteristics value) {
        this.specialFormatCharacteristics = value;
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
        protected PhotometricInterpretation photometricInterpretation;

        /**
         * Gets the value of the imageWidth property.
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
         * Sets the value of the imageWidth property.
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
         * Gets the value of the imageHeight property.
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
         * Sets the value of the imageHeight property.
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
         * Gets the value of the photometricInterpretation property.
         * 
         * @return
         *     possible object is
         *     {@link PhotometricInterpretation }
         *     
         */
        public PhotometricInterpretation getPhotometricInterpretation() {
            return photometricInterpretation;
        }

        /**
         * Sets the value of the photometricInterpretation property.
         * 
         * @param value
         *     allowed object is
         *     {@link PhotometricInterpretation }
         *     
         */
        public void setPhotometricInterpretation(PhotometricInterpretation value) {
            this.photometricInterpretation = value;
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
            protected ColorProfile colorProfile;
            @XmlElement(name = "YCbCr")
            protected YCbCr yCbCr;
            @XmlElement(name = "ReferenceBlackWhite")
            protected List<ReferenceBlackWhite> referenceBlackWhite;

            /**
             * Gets the value of the colorSpace property.
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
             * Sets the value of the colorSpace property.
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
             * Gets the value of the colorProfile property.
             * 
             * @return
             *     possible object is
             *     {@link ColorProfile }
             *     
             */
            public ColorProfile getColorProfile() {
                return colorProfile;
            }

            /**
             * Sets the value of the colorProfile property.
             * 
             * @param value
             *     allowed object is
             *     {@link ColorProfile }
             *     
             */
            public void setColorProfile(ColorProfile value) {
                this.colorProfile = value;
            }

            /**
             * Gets the value of the yCbCr property.
             * 
             * @return
             *     possible object is
             *     {@link YCbCr }
             *     
             */
            public YCbCr getYCbCr() {
                return yCbCr;
            }

            /**
             * Sets the value of the yCbCr property.
             * 
             * @param value
             *     allowed object is
             *     {@link YCbCr }
             *     
             */
            public void setYCbCr(YCbCr value) {
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
             * {@link ReferenceBlackWhite }
             * 
             * 
             */
            public List<ReferenceBlackWhite> getReferenceBlackWhite() {
                if (referenceBlackWhite == null) {
                    referenceBlackWhite = new ArrayList<ReferenceBlackWhite>();
                }
                return this.referenceBlackWhite;
            }

            public void setReferenceBlackWhite( ReferenceBlackWhite r) {
                if (referenceBlackWhite == null) {
                    referenceBlackWhite = new ArrayList<ReferenceBlackWhite>();
                }
                this.referenceBlackWhite.add(r);
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
                protected IccProfile iccProfile;
                @XmlElement(name = "LocalProfile")
                protected LocalProfile localProfile;
                protected Base64BinaryType embeddedProfile;

                /**
                 * Gets the value of the iccProfile property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link IccProfile }
                 *     
                 */
                public IccProfile getIccProfile() {
                    return iccProfile;
                }

                /**
                 * Sets the value of the iccProfile property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link IccProfile }
                 *     
                 */
                public void setIccProfile(IccProfile value) {
                    this.iccProfile = value;
                }

                /**
                 * Gets the value of the localProfile property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link LocalProfile }
                 *     
                 */
                public LocalProfile getLocalProfile() {
                    return localProfile;
                }

                /**
                 * Sets the value of the localProfile property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link LocalProfile }
                 *     
                 */
                public void setLocalProfile(LocalProfile value) {
                    this.localProfile = value;
                }

                /**
                 * Gets the value of the embeddedProfile property.
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
                 * Sets the value of the embeddedProfile property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the iccProfileName property.
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
                     * Sets the value of the iccProfileName property.
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
                     * Gets the value of the iccProfileVersion property.
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
                     * Sets the value of the iccProfileVersion property.
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
                     * Gets the value of the iccProfileURI property.
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
                     * Sets the value of the iccProfileURI property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the localProfileName property.
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
                     * Sets the value of the localProfileName property.
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
                     * Gets the value of the localProfileURL property.
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
                     * Sets the value of the localProfileURL property.
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                protected List<Component> component;

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
                 * {@link Component }
                 * 
                 * 
                 */
                public List<Component> getComponent() {
                    if (component == null) {
                        component = new ArrayList<Component>();
                    }
                    return this.component;
                }

                public void setComponent(Component c) {
                    if (component == null) {
                        component = new ArrayList<Component>();
                    }
                    this.component.add(c);
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
                     * Gets the value of the componentPhotometricInterpretation property.
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
                     * Sets the value of the componentPhotometricInterpretation property.
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
                     * Gets the value of the footroom property.
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
                     * Sets the value of the footroom property.
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
                     * Gets the value of the headroom property.
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
                     * Sets the value of the headroom property.
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                protected YCbCrSubSampling yCbCrSubSampling;
                protected TypeOfYCbCrPositioningType yCbCrPositioning;
                @XmlElement(name = "YCbCrCoefficients")
                protected YCbCrCoefficients yCbCrCoefficients;

                /**
                 * Gets the value of the yCbCrSubSampling property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link YCbCrSubSampling }
                 *     
                 */
                public YCbCrSubSampling getYCbCrSubSampling() {
                    return yCbCrSubSampling;
                }

                /**
                 * Sets the value of the yCbCrSubSampling property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link YCbCrSubSampling }
                 *     
                 */
                public void setYCbCrSubSampling(YCbCrSubSampling value) {
                    this.yCbCrSubSampling = value;
                }

                /**
                 * Gets the value of the yCbCrPositioning property.
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
                 * Sets the value of the yCbCrPositioning property.
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
                 * Gets the value of the yCbCrCoefficients property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link YCbCrCoefficients }
                 *     
                 */
                public YCbCrCoefficients getYCbCrCoefficients() {
                    return yCbCrCoefficients;
                }

                /**
                 * Sets the value of the yCbCrCoefficients property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link YCbCrCoefficients }
                 *     
                 */
                public void setYCbCrCoefficients(YCbCrCoefficients value) {
                    this.yCbCrCoefficients = value;
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
                     * Gets the value of the lumaRed property.
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
                     * Sets the value of the lumaRed property.
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
                     * Gets the value of the lumaGreen property.
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
                     * Sets the value of the lumaGreen property.
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
                     * Gets the value of the lumaBlue property.
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
                     * Sets the value of the lumaBlue property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the yCbCrSubsampleHoriz property.
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
                     * Sets the value of the yCbCrSubsampleHoriz property.
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
                     * Gets the value of the yCbCrSubsampleVert property.
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
                     * Sets the value of the yCbCrSubsampleVert property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
        protected JPEG2000 jpeg2000;
        @XmlElement(name = "MrSID")
        protected MrSID mrSID;
        @XmlElement(name = "Djvu")
        protected Djvu djvu;

        /**
         * Gets the value of the jpeg2000 property.
         * 
         * @return
         *     possible object is
         *     {@link JPEG2000 }
         *     
         */
        public JPEG2000 getJPEG2000() {
            return jpeg2000;
        }

        /**
         * Sets the value of the jpeg2000 property.
         * 
         * @param value
         *     allowed object is
         *     {@link JPEG2000 }
         *     
         */
        public void setJPEG2000(JPEG2000 value) {
            this.jpeg2000 = value;
        }

        /**
         * Gets the value of the mrSID property.
         * 
         * @return
         *     possible object is
         *     {@link MrSID }
         *     
         */
        public MrSID getMrSID() {
            return mrSID;
        }

        /**
         * Sets the value of the mrSID property.
         * 
         * @param value
         *     allowed object is
         *     {@link MrSID }
         *     
         */
        public void setMrSID(MrSID value) {
            this.mrSID = value;
        }

        /**
         * Gets the value of the djvu property.
         * 
         * @return
         *     possible object is
         *     {@link Djvu }
         *     
         */
        public Djvu getDjvu() {
            return djvu;
        }

        /**
         * Sets the value of the djvu property.
         * 
         * @param value
         *     allowed object is
         *     {@link Djvu }
         *     
         */
        public void setDjvu(Djvu value) {
            this.djvu = value;
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
             * Gets the value of the djvuFormat property.
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
             * Sets the value of the djvuFormat property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
            protected CodecCompliance codecCompliance;
            @XmlElement(name = "EncodingOptions")
            protected EncodingOptions encodingOptions;

            /**
             * Gets the value of the codecCompliance property.
             * 
             * @return
             *     possible object is
             *     {@link JPEG2000 .CodecCompliance }
             *     
             */
            public CodecCompliance getCodecCompliance() {
                return codecCompliance;
            }

            /**
             * Sets the value of the codecCompliance property.
             * 
             * @param value
             *     allowed object is
             *     {@link JPEG2000 .CodecCompliance }
             *     
             */
            public void setCodecCompliance(CodecCompliance value) {
                this.codecCompliance = value;
            }

            /**
             * Gets the value of the encodingOptions property.
             * 
             * @return
             *     possible object is
             *     {@link JPEG2000 .EncodingOptions }
             *     
             */
            public EncodingOptions getEncodingOptions() {
                return encodingOptions;
            }

            /**
             * Sets the value of the encodingOptions property.
             * 
             * @param value
             *     allowed object is
             *     {@link JPEG2000 .EncodingOptions }
             *     
             */
            public void setEncodingOptions(EncodingOptions value) {
                this.encodingOptions = value;
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
                 * Gets the value of the codec property.
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
                 * Sets the value of the codec property.
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
                 * Gets the value of the codecVersion property.
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
                 * Sets the value of the codecVersion property.
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
                 * Gets the value of the codestreamProfile property.
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
                 * Sets the value of the codestreamProfile property.
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
                 * Gets the value of the complianceClass property.
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
                 * Sets the value of the complianceClass property.
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                protected Tiles tiles;
                protected PositiveIntegerType qualityLayers;
                protected PositiveIntegerType resolutionLevels;

                /**
                 * Gets the value of the tiles property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link JPEG2000 .EncodingOptions.Tiles }
                 *     
                 */
                public Tiles getTiles() {
                    return tiles;
                }

                /**
                 * Sets the value of the tiles property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link JPEG2000 .EncodingOptions.Tiles }
                 *     
                 */
                public void setTiles(Tiles value) {
                    this.tiles = value;
                }

                /**
                 * Gets the value of the qualityLayers property.
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
                 * Sets the value of the qualityLayers property.
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
                 * Gets the value of the resolutionLevels property.
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
                 * Sets the value of the resolutionLevels property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the tileWidth property.
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
                     * Sets the value of the tileWidth property.
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
                     * Gets the value of the tileHeight property.
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
                     * Sets the value of the tileHeight property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the zoomLevels property.
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
             * Sets the value of the zoomLevels property.
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
