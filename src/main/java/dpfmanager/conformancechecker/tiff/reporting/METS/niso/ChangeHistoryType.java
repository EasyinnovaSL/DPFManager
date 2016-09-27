/**
 * <h1>ChangeHistoryType.java</h1> <p> This program is free software: you can redistribute it
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
 * <p>Java class for ChangeHistoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChangeHistoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ImageProcessing" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="dateTimeProcessed" type="{http://www.loc.gov/mix/v20}typeOfDateType" minOccurs="0"/>
 *                   &lt;element name="sourceData" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="processingAgency" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="processingRationale" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="ProcessingSoftware" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="processingSoftwareName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="processingSoftwareVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="processingOperatingSystemName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="processingOperatingSystemVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="processingActions" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PreviousImageMetadata" type="{http://www.loc.gov/mix/v20}typeOfPreviousImageMetadataType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeHistoryType", propOrder = {
    "imageProcessing",
    "previousImageMetadata"
})
public class ChangeHistoryType {

    @XmlElement(name = "ImageProcessing")
    protected List<ImageProcessing> imageProcessing;
    @XmlElement(name = "PreviousImageMetadata")
    protected List<TypeOfPreviousImageMetadataType> previousImageMetadata;

    /**
     * Gets the value of the imageProcessing property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageProcessing property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageProcessing().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImageProcessing }
     * 
     * 
     */
    public List<ImageProcessing> getImageProcessing() {
        if (imageProcessing == null) {
            imageProcessing = new ArrayList<ImageProcessing>();
        }
        return this.imageProcessing;
    }

    public void setImageProcessing(ImageProcessing i) {
        if (imageProcessing == null) {
            imageProcessing = new ArrayList<ImageProcessing>();
        }
        this.imageProcessing.add(i);
    }

    /**
     * Gets the value of the previousImageMetadata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the previousImageMetadata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreviousImageMetadata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeOfPreviousImageMetadataType }
     * 
     * 
     */
    public List<TypeOfPreviousImageMetadataType> getPreviousImageMetadata() {
        if (previousImageMetadata == null) {
            previousImageMetadata = new ArrayList<TypeOfPreviousImageMetadataType>();
        }
        return this.previousImageMetadata;
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
     *         &lt;element name="dateTimeProcessed" type="{http://www.loc.gov/mix/v20}typeOfDateType" minOccurs="0"/>
     *         &lt;element name="sourceData" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="processingAgency" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="processingRationale" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="ProcessingSoftware" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="processingSoftwareName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="processingSoftwareVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="processingOperatingSystemName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="processingOperatingSystemVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="processingActions" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
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
        "dateTimeProcessed",
        "sourceData",
        "processingAgency",
        "processingRationale",
        "processingSoftware",
        "processingActions"
    })
    public static class ImageProcessing {

        protected TypeOfDateType dateTimeProcessed;
        protected StringType sourceData;
        protected List<StringType> processingAgency;
        protected StringType processingRationale;
        @XmlElement(name = "ProcessingSoftware")
        protected List<ProcessingSoftware> processingSoftware;
        protected List<StringType> processingActions;

        /**
         * Gets the value of the dateTimeProcessed property.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfDateType }
         *     
         */
        public TypeOfDateType getDateTimeProcessed() {
            return dateTimeProcessed;
        }

        /**
         * Sets the value of the dateTimeProcessed property.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfDateType }
         *     
         */
        public void setDateTimeProcessed(TypeOfDateType value) {
            this.dateTimeProcessed = value;
        }

        /**
         * Gets the value of the sourceData property.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getSourceData() {
            return sourceData;
        }

        /**
         * Sets the value of the sourceData property.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setSourceData(StringType value) {
            this.sourceData = value;
        }

        /**
         * Gets the value of the processingAgency property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the processingAgency property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProcessingAgency().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StringType }
         * 
         * 
         */
        public List<StringType> getProcessingAgency() {
            if (processingAgency == null) {
                processingAgency = new ArrayList<StringType>();
            }
            return this.processingAgency;
        }

        /**
         * Gets the value of the processingRationale property.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getProcessingRationale() {
            return processingRationale;
        }

        /**
         * Sets the value of the processingRationale property.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setProcessingRationale(StringType value) {
            this.processingRationale = value;
        }

        /**
         * Gets the value of the processingSoftware property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the processingSoftware property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProcessingSoftware().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProcessingSoftware }
         * 
         * 
         */
        public List<ProcessingSoftware> getProcessingSoftware() {
            if (processingSoftware == null) {
                processingSoftware = new ArrayList<ProcessingSoftware>();
            }
            return this.processingSoftware;
        }

        public void setProcessingSoftware(ProcessingSoftware p) {
            if (processingSoftware == null) {
                processingSoftware = new ArrayList<ProcessingSoftware>();
            }
            this.processingSoftware.add(p);
        }

        /**
         * Gets the value of the processingActions property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the processingActions property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProcessingActions().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StringType }
         * 
         * 
         */
        public List<StringType> getProcessingActions() {
            if (processingActions == null) {
                processingActions = new ArrayList<StringType>();
            }
            return this.processingActions;
        }

        public void setProcessingActions(StringType s) {
            if (processingActions == null) {
                processingActions = new ArrayList<StringType>();
            }
            this.processingActions.add(s);
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
         *         &lt;element name="processingSoftwareName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="processingSoftwareVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="processingOperatingSystemName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="processingOperatingSystemVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
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
            "processingSoftwareName",
            "processingSoftwareVersion",
            "processingOperatingSystemName",
            "processingOperatingSystemVersion"
        })
        public static class ProcessingSoftware {

            protected StringType processingSoftwareName;
            protected StringType processingSoftwareVersion;
            protected StringType processingOperatingSystemName;
            protected StringType processingOperatingSystemVersion;

            /**
             * Gets the value of the processingSoftwareName property.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getProcessingSoftwareName() {
                return processingSoftwareName;
            }

            /**
             * Sets the value of the processingSoftwareName property.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setProcessingSoftwareName(StringType value) {
                this.processingSoftwareName = value;
            }

            /**
             * Gets the value of the processingSoftwareVersion property.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getProcessingSoftwareVersion() {
                return processingSoftwareVersion;
            }

            /**
             * Sets the value of the processingSoftwareVersion property.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setProcessingSoftwareVersion(StringType value) {
                this.processingSoftwareVersion = value;
            }

            /**
             * Gets the value of the processingOperatingSystemName property.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getProcessingOperatingSystemName() {
                return processingOperatingSystemName;
            }

            /**
             * Sets the value of the processingOperatingSystemName property.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setProcessingOperatingSystemName(StringType value) {
                this.processingOperatingSystemName = value;
            }

            /**
             * Gets the value of the processingOperatingSystemVersion property.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getProcessingOperatingSystemVersion() {
                return processingOperatingSystemVersion;
            }

            /**
             * Sets the value of the processingOperatingSystemVersion property.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setProcessingOperatingSystemVersion(StringType value) {
                this.processingOperatingSystemVersion = value;
            }

        }

    }

}
