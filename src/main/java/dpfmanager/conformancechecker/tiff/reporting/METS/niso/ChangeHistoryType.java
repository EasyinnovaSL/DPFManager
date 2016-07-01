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
 * <p>Clase Java para ChangeHistoryType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
    protected List<ChangeHistoryType.ImageProcessing> imageProcessing;
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
     * {@link ChangeHistoryType.ImageProcessing }
     * 
     * 
     */
    public List<ChangeHistoryType.ImageProcessing> getImageProcessing() {
        if (imageProcessing == null) {
            imageProcessing = new ArrayList<ChangeHistoryType.ImageProcessing>();
        }
        return this.imageProcessing;
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
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
        protected List<ChangeHistoryType.ImageProcessing.ProcessingSoftware> processingSoftware;
        protected List<StringType> processingActions;

        /**
         * Obtiene el valor de la propiedad dateTimeProcessed.
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
         * Define el valor de la propiedad dateTimeProcessed.
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
         * Obtiene el valor de la propiedad sourceData.
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
         * Define el valor de la propiedad sourceData.
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
         * Obtiene el valor de la propiedad processingRationale.
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
         * Define el valor de la propiedad processingRationale.
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
         * {@link ChangeHistoryType.ImageProcessing.ProcessingSoftware }
         * 
         * 
         */
        public List<ChangeHistoryType.ImageProcessing.ProcessingSoftware> getProcessingSoftware() {
            if (processingSoftware == null) {
                processingSoftware = new ArrayList<ChangeHistoryType.ImageProcessing.ProcessingSoftware>();
            }
            return this.processingSoftware;
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
             * Obtiene el valor de la propiedad processingSoftwareName.
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
             * Define el valor de la propiedad processingSoftwareName.
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
             * Obtiene el valor de la propiedad processingSoftwareVersion.
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
             * Define el valor de la propiedad processingSoftwareVersion.
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
             * Obtiene el valor de la propiedad processingOperatingSystemName.
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
             * Define el valor de la propiedad processingOperatingSystemName.
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
             * Obtiene el valor de la propiedad processingOperatingSystemVersion.
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
             * Define el valor de la propiedad processingOperatingSystemVersion.
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
