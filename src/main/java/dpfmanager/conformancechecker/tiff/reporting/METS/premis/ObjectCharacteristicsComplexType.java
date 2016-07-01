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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para objectCharacteristicsComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="objectCharacteristicsComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}compositionLevel" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}fixity" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}size" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}format" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}creatingApplication" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}inhibitors" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.loc.gov/premis/v3}objectCharacteristicsExtension" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "objectCharacteristicsComplexType", propOrder = {
    "compositionLevel",
    "fixity",
    "size",
    "format",
    "creatingApplication",
    "inhibitors",
    "objectCharacteristicsExtension"
})
public class ObjectCharacteristicsComplexType {

    protected CompositionLevelComplexType compositionLevel;
    protected List<FixityComplexType> fixity;
    protected Long size;
    @XmlElement(required = true)
    protected List<FormatComplexType> format;
    protected List<CreatingApplicationComplexType> creatingApplication;
    protected List<InhibitorsComplexType> inhibitors;
    protected List<ExtensionComplexType> objectCharacteristicsExtension;

    /**
     * Obtiene el valor de la propiedad compositionLevel.
     * 
     * @return
     *     possible object is
     *     {@link CompositionLevelComplexType }
     *     
     */
    public CompositionLevelComplexType getCompositionLevel() {
        return compositionLevel;
    }

    /**
     * Define el valor de la propiedad compositionLevel.
     * 
     * @param value
     *     allowed object is
     *     {@link CompositionLevelComplexType }
     *     
     */
    public void setCompositionLevel(CompositionLevelComplexType value) {
        this.compositionLevel = value;
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
     * {@link FixityComplexType }
     * 
     * 
     */
    public List<FixityComplexType> getFixity() {
        if (fixity == null) {
            fixity = new ArrayList<FixityComplexType>();
        }
        return this.fixity;
    }

    /**
     * Obtiene el valor de la propiedad size.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSize() {
        return size;
    }

    /**
     * Define el valor de la propiedad size.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSize(Long value) {
        this.size = value;
    }

    /**
     * Gets the value of the format property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the format property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FormatComplexType }
     * 
     * 
     */
    public List<FormatComplexType> getFormat() {
        if (format == null) {
            format = new ArrayList<FormatComplexType>();
        }
        return this.format;
    }

    /**
     * Gets the value of the creatingApplication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creatingApplication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreatingApplication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CreatingApplicationComplexType }
     * 
     * 
     */
    public List<CreatingApplicationComplexType> getCreatingApplication() {
        if (creatingApplication == null) {
            creatingApplication = new ArrayList<CreatingApplicationComplexType>();
        }
        return this.creatingApplication;
    }

    /**
     * Gets the value of the inhibitors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inhibitors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInhibitors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InhibitorsComplexType }
     * 
     * 
     */
    public List<InhibitorsComplexType> getInhibitors() {
        if (inhibitors == null) {
            inhibitors = new ArrayList<InhibitorsComplexType>();
        }
        return this.inhibitors;
    }

    /**
     * Gets the value of the objectCharacteristicsExtension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectCharacteristicsExtension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectCharacteristicsExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtensionComplexType }
     * 
     * 
     */
    public List<ExtensionComplexType> getObjectCharacteristicsExtension() {
        if (objectCharacteristicsExtension == null) {
            objectCharacteristicsExtension = new ArrayList<ExtensionComplexType>();
        }
        return this.objectCharacteristicsExtension;
    }

}
