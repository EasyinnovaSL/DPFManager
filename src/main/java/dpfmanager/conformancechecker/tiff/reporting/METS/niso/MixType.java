/**
 * <h1>MixType.java</h1> <p> This program is free software: you can redistribute it
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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mixType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the basicDigitalObjectInformation property.
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
     * Sets the value of the basicDigitalObjectInformation property.
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
     * Gets the value of the basicImageInformation property.
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
     * Sets the value of the basicImageInformation property.
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
     * Gets the value of the imageCaptureMetadata property.
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
     * Sets the value of the imageCaptureMetadata property.
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
     * Gets the value of the imageAssessmentMetadata property.
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
     * Sets the value of the imageAssessmentMetadata property.
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
     * Gets the value of the changeHistory property.
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
     * Sets the value of the changeHistory property.
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
