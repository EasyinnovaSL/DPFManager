//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.11
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderian si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.16 a las 10:17:25 AM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.mets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * amdSecType: Complex Type for Administrative Metadata Sections
 * 			The administrative metadata section consists of four possible subsidiary sections: techMD (technical metadata for text/image/audio/video files), rightsMD (intellectual property rights metadata), sourceMD (analog/digital source metadata), and digiprovMD (digital provenance metadata, that is, the history of migrations/translations performed on a digital library object from it's original digital capture/encoding).
 * 			
 * 
 * <p>Clase Java para amdSecType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="amdSecType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="techMD" type="{http://www.loc.gov/METS/}mdSecType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="rightsMD" type="{http://www.loc.gov/METS/}mdSecType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sourceMD" type="{http://www.loc.gov/METS/}mdSecType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="digiprovMD" type="{http://www.loc.gov/METS/}mdSecType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "amdSecType", propOrder = {
    "techMD",
    "rightsMD",
    "sourceMD",
    "digiprovMD"
})
public class AmdSecType {

    protected List<MdSecType> techMD;
    protected List<MdSecType> rightsMD;
    protected List<MdSecType> sourceMD;
    protected List<MdSecType> digiprovMD;
    @XmlAttribute(name = "ID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the techMD property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the techMD property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTechMD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MdSecType }
     * 
     * 
     */
    public List<MdSecType> getTechMD() {
        if (techMD == null) {
            techMD = new ArrayList<MdSecType>();
        }
        return this.techMD;
    }
    public void setTechMD(MdSecType tech) {
        if (techMD == null) {
            techMD = new ArrayList<MdSecType>();
        }
        this.techMD.add(tech);
    }

    /**
     * Gets the value of the rightsMD property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rightsMD property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRightsMD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MdSecType }
     * 
     * 
     */
    public List<MdSecType> getRightsMD() {
        if (rightsMD == null) {
            rightsMD = new ArrayList<MdSecType>();
        }
        return this.rightsMD;
    }

    public void setRightsMD(MdSecType mdsec) {
        if (rightsMD == null) {
            rightsMD = new ArrayList<MdSecType>();
        }
        this.rightsMD.add(mdsec);
    }


    /**
     * Gets the value of the sourceMD property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceMD property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceMD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MdSecType }
     * 
     * 
     */
    public List<MdSecType> getSourceMD() {
        if (sourceMD == null) {
            sourceMD = new ArrayList<MdSecType>();
        }
        return this.sourceMD;
    }

    /**
     * Gets the value of the digiprovMD property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the digiprovMD property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDigiprovMD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MdSecType }
     * 
     * 
     */
    public List<MdSecType> getDigiprovMD() {
        if (digiprovMD == null) {
            digiprovMD = new ArrayList<MdSecType>();
        }
        return this.digiprovMD;
    }

    public void setDigiprovMD(MdSecType m) {
        if (digiprovMD == null) {
            digiprovMD = new ArrayList<MdSecType>();
        }
        this.digiprovMD.add(m);
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
