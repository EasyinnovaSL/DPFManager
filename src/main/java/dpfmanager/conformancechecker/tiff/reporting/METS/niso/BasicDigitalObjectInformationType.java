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
 * <p>Clase Java para BasicDigitalObjectInformationType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="BasicDigitalObjectInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ObjectIdentifier" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="objectIdentifierType" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="objectIdentifierValue" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="fileSize" type="{http://www.loc.gov/mix/v20}nonNegativeIntegerType" minOccurs="0"/>
 *         &lt;element name="FormatDesignation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="formatName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="formatVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="FormatRegistry" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="formatRegistryName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="formatRegistryKey" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="byteOrder" type="{http://www.loc.gov/mix/v20}typeOfByteOrderType" minOccurs="0"/>
 *         &lt;element name="Compression" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="compressionScheme" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="compressionSchemeLocalList" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
 *                   &lt;element name="compressionSchemeLocalValue" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="compressionRatio" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Fixity" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="messageDigestAlgorithm" type="{http://www.loc.gov/mix/v20}typeOfMessageDigestAlgorithmType" minOccurs="0"/>
 *                   &lt;element name="messageDigest" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="messageDigestOriginator" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
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
@XmlType(name = "BasicDigitalObjectInformationType", propOrder = {
    "objectIdentifier",
    "fileSize",
    "formatDesignation",
    "formatRegistry",
    "byteOrder",
    "compression",
    "fixity"
})
public class BasicDigitalObjectInformationType {

    @XmlElement(name = "ObjectIdentifier")
    protected List<BasicDigitalObjectInformationType.ObjectIdentifier> objectIdentifier;
    protected NonNegativeIntegerType fileSize;
    @XmlElement(name = "FormatDesignation")
    protected BasicDigitalObjectInformationType.FormatDesignation formatDesignation;
    @XmlElement(name = "FormatRegistry")
    protected BasicDigitalObjectInformationType.FormatRegistry formatRegistry;
    protected TypeOfByteOrderType byteOrder;
    @XmlElement(name = "Compression")
    protected List<BasicDigitalObjectInformationType.Compression> compression;
    @XmlElement(name = "Fixity")
    protected List<BasicDigitalObjectInformationType.Fixity> fixity;

    /**
     * Gets the value of the objectIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BasicDigitalObjectInformationType.ObjectIdentifier }
     * 
     * 
     */
    public List<BasicDigitalObjectInformationType.ObjectIdentifier> getObjectIdentifier() {
        if (objectIdentifier == null) {
            objectIdentifier = new ArrayList<BasicDigitalObjectInformationType.ObjectIdentifier>();
        }
        return this.objectIdentifier;
    }

    /**
     * Obtiene el valor de la propiedad fileSize.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerType }
     *     
     */
    public NonNegativeIntegerType getFileSize() {
        return fileSize;
    }

    /**
     * Define el valor de la propiedad fileSize.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerType }
     *     
     */
    public void setFileSize(NonNegativeIntegerType value) {
        this.fileSize = value;
    }

    /**
     * Obtiene el valor de la propiedad formatDesignation.
     * 
     * @return
     *     possible object is
     *     {@link BasicDigitalObjectInformationType.FormatDesignation }
     *     
     */
    public BasicDigitalObjectInformationType.FormatDesignation getFormatDesignation() {
        return formatDesignation;
    }

    /**
     * Define el valor de la propiedad formatDesignation.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicDigitalObjectInformationType.FormatDesignation }
     *     
     */
    public void setFormatDesignation(BasicDigitalObjectInformationType.FormatDesignation value) {
        this.formatDesignation = value;
    }

    /**
     * Obtiene el valor de la propiedad formatRegistry.
     * 
     * @return
     *     possible object is
     *     {@link BasicDigitalObjectInformationType.FormatRegistry }
     *     
     */
    public BasicDigitalObjectInformationType.FormatRegistry getFormatRegistry() {
        return formatRegistry;
    }

    /**
     * Define el valor de la propiedad formatRegistry.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicDigitalObjectInformationType.FormatRegistry }
     *     
     */
    public void setFormatRegistry(BasicDigitalObjectInformationType.FormatRegistry value) {
        this.formatRegistry = value;
    }

    /**
     * Obtiene el valor de la propiedad byteOrder.
     * 
     * @return
     *     possible object is
     *     {@link TypeOfByteOrderType }
     *     
     */
    public TypeOfByteOrderType getByteOrder() {
        return byteOrder;
    }

    /**
     * Define el valor de la propiedad byteOrder.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeOfByteOrderType }
     *     
     */
    public void setByteOrder(TypeOfByteOrderType value) {
        this.byteOrder = value;
    }

    /**
     * Gets the value of the compression property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compression property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompression().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BasicDigitalObjectInformationType.Compression }
     * 
     * 
     */
    public List<BasicDigitalObjectInformationType.Compression> getCompression() {
        if (compression == null) {
            compression = new ArrayList<BasicDigitalObjectInformationType.Compression>();
        }
        return this.compression;
    }

    /**
     * Gets the value of the fixity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BasicDigitalObjectInformationType.Fixity }
     * 
     * 
     */
    public List<BasicDigitalObjectInformationType.Fixity> getFixity() {
        if (fixity == null) {
            fixity = new ArrayList<BasicDigitalObjectInformationType.Fixity>();
        }
        return this.fixity;
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
     *         &lt;element name="compressionScheme" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="compressionSchemeLocalList" type="{http://www.loc.gov/mix/v20}URIType" minOccurs="0"/>
     *         &lt;element name="compressionSchemeLocalValue" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="compressionRatio" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
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
        "compressionScheme",
        "compressionSchemeLocalList",
        "compressionSchemeLocalValue",
        "compressionRatio"
    })
    public static class Compression {

        protected StringType compressionScheme;
        protected URIType compressionSchemeLocalList;
        protected StringType compressionSchemeLocalValue;
        protected RationalType compressionRatio;

        /**
         * Obtiene el valor de la propiedad compressionScheme.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getCompressionScheme() {
            return compressionScheme;
        }

        /**
         * Define el valor de la propiedad compressionScheme.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setCompressionScheme(StringType value) {
            this.compressionScheme = value;
        }

        /**
         * Obtiene el valor de la propiedad compressionSchemeLocalList.
         * 
         * @return
         *     possible object is
         *     {@link URIType }
         *     
         */
        public URIType getCompressionSchemeLocalList() {
            return compressionSchemeLocalList;
        }

        /**
         * Define el valor de la propiedad compressionSchemeLocalList.
         * 
         * @param value
         *     allowed object is
         *     {@link URIType }
         *     
         */
        public void setCompressionSchemeLocalList(URIType value) {
            this.compressionSchemeLocalList = value;
        }

        /**
         * Obtiene el valor de la propiedad compressionSchemeLocalValue.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getCompressionSchemeLocalValue() {
            return compressionSchemeLocalValue;
        }

        /**
         * Define el valor de la propiedad compressionSchemeLocalValue.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setCompressionSchemeLocalValue(StringType value) {
            this.compressionSchemeLocalValue = value;
        }

        /**
         * Obtiene el valor de la propiedad compressionRatio.
         * 
         * @return
         *     possible object is
         *     {@link RationalType }
         *     
         */
        public RationalType getCompressionRatio() {
            return compressionRatio;
        }

        /**
         * Define el valor de la propiedad compressionRatio.
         * 
         * @param value
         *     allowed object is
         *     {@link RationalType }
         *     
         */
        public void setCompressionRatio(RationalType value) {
            this.compressionRatio = value;
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
     *         &lt;element name="messageDigestAlgorithm" type="{http://www.loc.gov/mix/v20}typeOfMessageDigestAlgorithmType" minOccurs="0"/>
     *         &lt;element name="messageDigest" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="messageDigestOriginator" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
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
        "messageDigestAlgorithm",
        "messageDigest",
        "messageDigestOriginator"
    })
    public static class Fixity {

        protected TypeOfMessageDigestAlgorithmType messageDigestAlgorithm;
        protected StringType messageDigest;
        protected StringType messageDigestOriginator;

        /**
         * Obtiene el valor de la propiedad messageDigestAlgorithm.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfMessageDigestAlgorithmType }
         *     
         */
        public TypeOfMessageDigestAlgorithmType getMessageDigestAlgorithm() {
            return messageDigestAlgorithm;
        }

        /**
         * Define el valor de la propiedad messageDigestAlgorithm.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfMessageDigestAlgorithmType }
         *     
         */
        public void setMessageDigestAlgorithm(TypeOfMessageDigestAlgorithmType value) {
            this.messageDigestAlgorithm = value;
        }

        /**
         * Obtiene el valor de la propiedad messageDigest.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getMessageDigest() {
            return messageDigest;
        }

        /**
         * Define el valor de la propiedad messageDigest.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setMessageDigest(StringType value) {
            this.messageDigest = value;
        }

        /**
         * Obtiene el valor de la propiedad messageDigestOriginator.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getMessageDigestOriginator() {
            return messageDigestOriginator;
        }

        /**
         * Define el valor de la propiedad messageDigestOriginator.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setMessageDigestOriginator(StringType value) {
            this.messageDigestOriginator = value;
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
     *         &lt;element name="formatName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="formatVersion" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
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
        "formatName",
        "formatVersion"
    })
    public static class FormatDesignation {

        protected StringType formatName;
        protected StringType formatVersion;

        /**
         * Obtiene el valor de la propiedad formatName.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getFormatName() {
            return formatName;
        }

        /**
         * Define el valor de la propiedad formatName.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setFormatName(StringType value) {
            this.formatName = value;
        }

        /**
         * Obtiene el valor de la propiedad formatVersion.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getFormatVersion() {
            return formatVersion;
        }

        /**
         * Define el valor de la propiedad formatVersion.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setFormatVersion(StringType value) {
            this.formatVersion = value;
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
     *         &lt;element name="formatRegistryName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="formatRegistryKey" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
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
        "formatRegistryName",
        "formatRegistryKey"
    })
    public static class FormatRegistry {

        protected StringType formatRegistryName;
        protected StringType formatRegistryKey;

        /**
         * Obtiene el valor de la propiedad formatRegistryName.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getFormatRegistryName() {
            return formatRegistryName;
        }

        /**
         * Define el valor de la propiedad formatRegistryName.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setFormatRegistryName(StringType value) {
            this.formatRegistryName = value;
        }

        /**
         * Obtiene el valor de la propiedad formatRegistryKey.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getFormatRegistryKey() {
            return formatRegistryKey;
        }

        /**
         * Define el valor de la propiedad formatRegistryKey.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setFormatRegistryKey(StringType value) {
            this.formatRegistryKey = value;
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
     *         &lt;element name="objectIdentifierType" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="objectIdentifierValue" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
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
        "objectIdentifierType",
        "objectIdentifierValue"
    })
    public static class ObjectIdentifier {

        protected StringType objectIdentifierType;
        protected StringType objectIdentifierValue;

        /**
         * Obtiene el valor de la propiedad objectIdentifierType.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getObjectIdentifierType() {
            return objectIdentifierType;
        }

        /**
         * Define el valor de la propiedad objectIdentifierType.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setObjectIdentifierType(StringType value) {
            this.objectIdentifierType = value;
        }

        /**
         * Obtiene el valor de la propiedad objectIdentifierValue.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getObjectIdentifierValue() {
            return objectIdentifierValue;
        }

        /**
         * Define el valor de la propiedad objectIdentifierValue.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setObjectIdentifierValue(StringType value) {
            this.objectIdentifierValue = value;
        }

    }

}
