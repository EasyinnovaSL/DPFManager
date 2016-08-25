//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:52:21 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.premis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Clase Java para intellectualEntity complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="intellectualEntity">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.loc.gov/premis/v3}objectComplexType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}objectIdentifier" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}preservationLevel" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}significantProperties" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}originalName" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentFunction" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentDesignation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentRegistry" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}environmentExtension" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}relationship" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingEventIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}linkingRightsStatementIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="xmlID" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="version" type="{http://www.loc.gov/premis/v3}version3" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "intellectualEntity", propOrder = {
    "objectIdentifier",
    "preservationLevel",
    "significantProperties",
    "originalName",
    "environmentFunction",
    "environmentDesignation",
    "environmentRegistry",
    "environmentExtension",
    "relationship",
    "linkingEventIdentifier",
    "linkingRightsStatementIdentifier"
})
public class IntellectualEntity
    extends ObjectComplexType
{

    @XmlElement(required = true)
    protected List<ObjectIdentifierComplexType> objectIdentifier;
    protected List<PreservationLevelComplexType> preservationLevel;
    protected List<SignificantPropertiesComplexType> significantProperties;
    protected OriginalNameComplexType originalName;
    protected List<EnvironmentFunctionComplexType> environmentFunction;
    protected List<EnvironmentDesignationComplexType> environmentDesignation;
    protected List<EnvironmentRegistryComplexType> environmentRegistry;
    protected List<ExtensionComplexType> environmentExtension;
    protected List<RelationshipComplexType> relationship;
    protected List<LinkingEventIdentifierComplexType> linkingEventIdentifier;
    protected List<LinkingRightsStatementIdentifierComplexType> linkingRightsStatementIdentifier;
    @XmlAttribute(name = "xmlID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String xmlID;
    @XmlAttribute(name = "version")
    protected String version;

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
     * {@link ObjectIdentifierComplexType }
     * 
     * 
     */
    public List<ObjectIdentifierComplexType> getObjectIdentifier() {
        if (objectIdentifier == null) {
            objectIdentifier = new ArrayList<ObjectIdentifierComplexType>();
        }
        return this.objectIdentifier;
    }

    /**
     * Gets the value of the preservationLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the preservationLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreservationLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PreservationLevelComplexType }
     * 
     * 
     */
    public List<PreservationLevelComplexType> getPreservationLevel() {
        if (preservationLevel == null) {
            preservationLevel = new ArrayList<PreservationLevelComplexType>();
        }
        return this.preservationLevel;
    }

    /**
     * Gets the value of the significantProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the significantProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignificantProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignificantPropertiesComplexType }
     * 
     * 
     */
    public List<SignificantPropertiesComplexType> getSignificantProperties() {
        if (significantProperties == null) {
            significantProperties = new ArrayList<SignificantPropertiesComplexType>();
        }
        return this.significantProperties;
    }

    /**
     * Obtiene el valor de la propiedad originalName.
     * 
     * @return
     *     possible object is
     *     {@link OriginalNameComplexType }
     *     
     */
    public OriginalNameComplexType getOriginalName() {
        return originalName;
    }

    /**
     * Define el valor de la propiedad originalName.
     * 
     * @param value
     *     allowed object is
     *     {@link OriginalNameComplexType }
     *     
     */
    public void setOriginalName(OriginalNameComplexType value) {
        this.originalName = value;
    }

    /**
     * Gets the value of the environmentFunction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the environmentFunction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvironmentFunction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvironmentFunctionComplexType }
     * 
     * 
     */
    public List<EnvironmentFunctionComplexType> getEnvironmentFunction() {
        if (environmentFunction == null) {
            environmentFunction = new ArrayList<EnvironmentFunctionComplexType>();
        }
        return this.environmentFunction;
    }

    /**
     * Gets the value of the environmentDesignation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the environmentDesignation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvironmentDesignation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvironmentDesignationComplexType }
     * 
     * 
     */
    public List<EnvironmentDesignationComplexType> getEnvironmentDesignation() {
        if (environmentDesignation == null) {
            environmentDesignation = new ArrayList<EnvironmentDesignationComplexType>();
        }
        return this.environmentDesignation;
    }

    /**
     * Gets the value of the environmentRegistry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the environmentRegistry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvironmentRegistry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvironmentRegistryComplexType }
     * 
     * 
     */
    public List<EnvironmentRegistryComplexType> getEnvironmentRegistry() {
        if (environmentRegistry == null) {
            environmentRegistry = new ArrayList<EnvironmentRegistryComplexType>();
        }
        return this.environmentRegistry;
    }

    /**
     * Gets the value of the environmentExtension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the environmentExtension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvironmentExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtensionComplexType }
     * 
     * 
     */
    public List<ExtensionComplexType> getEnvironmentExtension() {
        if (environmentExtension == null) {
            environmentExtension = new ArrayList<ExtensionComplexType>();
        }
        return this.environmentExtension;
    }

    /**
     * Gets the value of the relationship property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relationship property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelationship().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelationshipComplexType }
     * 
     * 
     */
    public List<RelationshipComplexType> getRelationship() {
        if (relationship == null) {
            relationship = new ArrayList<RelationshipComplexType>();
        }
        return this.relationship;
    }

    /**
     * Gets the value of the linkingEventIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkingEventIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkingEventIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinkingEventIdentifierComplexType }
     * 
     * 
     */
    public List<LinkingEventIdentifierComplexType> getLinkingEventIdentifier() {
        if (linkingEventIdentifier == null) {
            linkingEventIdentifier = new ArrayList<LinkingEventIdentifierComplexType>();
        }
        return this.linkingEventIdentifier;
    }

    /**
     * Gets the value of the linkingRightsStatementIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkingRightsStatementIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkingRightsStatementIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinkingRightsStatementIdentifierComplexType }
     * 
     * 
     */
    public List<LinkingRightsStatementIdentifierComplexType> getLinkingRightsStatementIdentifier() {
        if (linkingRightsStatementIdentifier == null) {
            linkingRightsStatementIdentifier = new ArrayList<LinkingRightsStatementIdentifierComplexType>();
        }
        return this.linkingRightsStatementIdentifier;
    }

    /**
     * Obtiene el valor de la propiedad xmlID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlID() {
        return xmlID;
    }

    /**
     * Define el valor de la propiedad xmlID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlID(String value) {
        this.xmlID = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Define el valor de la propiedad version.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
