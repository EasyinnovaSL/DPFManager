//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.11
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderian si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.16 a las 10:17:25 AM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.mets;


import dpfmanager.conformancechecker.tiff.reporting.METS.niso.Mix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * mdSecType: Complex Type for Metadata Sections
 * 			A generic framework for pointing to/including metadata within a METS document, a la Warwick Framework.
 * 			
 * 
 * <p>Clase Java para mdSecType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mdSecType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="mdRef" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attGroup ref="{http://www.loc.gov/METS/}LOCATION"/&gt;
 *                 &lt;attGroup ref="{http://www.loc.gov/METS/}FILECORE"/&gt;
 *                 &lt;attGroup ref="{http://www.loc.gov/METS/}METADATA"/&gt;
 *                 &lt;attGroup ref="{http://www.w3.org/1999/xlink}simpleLink"/&gt;
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *                 &lt;attribute name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="XPTR" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="mdWrap" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="binData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *                   &lt;element name="xmlData" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;any processContents='lax' maxOccurs="unbounded"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/choice&gt;
 *                 &lt;attGroup ref="{http://www.loc.gov/METS/}FILECORE"/&gt;
 *                 &lt;attGroup ref="{http://www.loc.gov/METS/}METADATA"/&gt;
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *                 &lt;attribute name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/all&gt;
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *       &lt;attribute name="GROUPID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ADMID" type="{http://www.w3.org/2001/XMLSchema}IDREFS" /&gt;
 *       &lt;attribute name="CREATED" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *       &lt;attribute name="STATUS" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mdSecType", propOrder = {

})
public class MdSecType {

    protected MdRef mdRef;
    protected MdWrap mdWrap;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "GROUPID")
    protected String groupid;
    @XmlAttribute(name = "ADMID")
    @XmlIDREF
    @XmlSchemaType(name = "IDREFS")
    protected List<Object> admid;
    @XmlAttribute(name = "CREATED")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;
    @XmlAttribute(name = "STATUS")
    protected String status;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Obtiene el valor de la propiedad mdRef.
     * 
     * @return
     *     possible object is
     *     {@link MdRef }
     *     
     */
    public MdRef getMdRef() {
        return mdRef;
    }

    /**
     * Define el valor de la propiedad mdRef.
     * 
     * @param value
     *     allowed object is
     *     {@link MdRef }
     *     
     */
    public void setMdRef(MdRef value) {
        this.mdRef = value;
    }

    /**
     * Obtiene el valor de la propiedad mdWrap.
     * 
     * @return
     *     possible object is
     *     {@link MdWrap }
     *     
     */
    public MdWrap getMdWrap() {
        return mdWrap;
    }

    /**
     * Define el valor de la propiedad mdWrap.
     * 
     * @param value
     *     allowed object is
     *     {@link MdWrap }
     *     
     */
    public void setMdWrap(MdWrap value) {
        this.mdWrap = value;
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
     * Obtiene el valor de la propiedad groupid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGROUPID() {
        return groupid;
    }

    /**
     * Define el valor de la propiedad groupid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGROUPID(String value) {
        this.groupid = value;
    }

    /**
     * Gets the value of the admid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the admid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getADMID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getADMID() {
        if (admid == null) {
            admid = new ArrayList<Object>();
        }
        return this.admid;
    }

    public void setADMID(Object a) {
        if (admid == null) {
            admid = new ArrayList<Object>();
        }
        this.admid.add(a);
    }

    /**
     * Obtiene el valor de la propiedad created.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCREATED() {
        return created;
    }

    /**
     * Define el valor de la propiedad created.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCREATED(XMLGregorianCalendar value) {
        this.created = value;
    }

    /**
     * Obtiene el valor de la propiedad status.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTATUS() {
        return status;
    }

    /**
     * Define el valor de la propiedad status.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTATUS(String value) {
        this.status = value;
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


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attGroup ref="{http://www.loc.gov/METS/}LOCATION"/&gt;
     *       &lt;attGroup ref="{http://www.loc.gov/METS/}FILECORE"/&gt;
     *       &lt;attGroup ref="{http://www.loc.gov/METS/}METADATA"/&gt;
     *       &lt;attGroup ref="{http://www.w3.org/1999/xlink}simpleLink"/&gt;
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
     *       &lt;attribute name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="XPTR" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class MdRef {

        @XmlAttribute(name = "ID")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;
        @XmlAttribute(name = "LABEL")
        protected String label;
        @XmlAttribute(name = "XPTR")
        protected String xptr;
        @XmlAttribute(name = "LOCTYPE", required = true)
        protected String loctype;
        @XmlAttribute(name = "OTHERLOCTYPE")
        protected String otherloctype;
        @XmlAttribute(name = "MIMETYPE")
        protected String mimetype;
        @XmlAttribute(name = "SIZE")
        protected Long size;
        @XmlAttribute(name = "CREATED")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar created;
        @XmlAttribute(name = "CHECKSUM")
        protected String checksum;
        @XmlAttribute(name = "CHECKSUMTYPE")
        protected String checksumtype;
        @XmlAttribute(name = "MDTYPE", required = true)
        protected String mdtype;
        @XmlAttribute(name = "OTHERMDTYPE")
        protected String othermdtype;
        @XmlAttribute(name = "MDTYPEVERSION")
        protected String mdtypeversion;
        @XmlAttribute(name = "type", namespace = "http://www.w3.org/1999/xlink")
        protected String type;
        @XmlAttribute(name = "href", namespace = "http://www.w3.org/1999/xlink")
        @XmlSchemaType(name = "anyURI")
        protected String href;
        @XmlAttribute(name = "role", namespace = "http://www.w3.org/1999/xlink")
        protected String role;
        @XmlAttribute(name = "arcrole", namespace = "http://www.w3.org/1999/xlink")
        protected String arcrole;
        @XmlAttribute(name = "title", namespace = "http://www.w3.org/1999/xlink")
        protected String title;
        @XmlAttribute(name = "show", namespace = "http://www.w3.org/1999/xlink")
        protected String show;
        @XmlAttribute(name = "actuate", namespace = "http://www.w3.org/1999/xlink")
        protected String actuate;

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
         * Obtiene el valor de la propiedad label.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLABEL() {
            return label;
        }

        /**
         * Define el valor de la propiedad label.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLABEL(String value) {
            this.label = value;
        }

        /**
         * Obtiene el valor de la propiedad xptr.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getXPTR() {
            return xptr;
        }

        /**
         * Define el valor de la propiedad xptr.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setXPTR(String value) {
            this.xptr = value;
        }

        /**
         * Obtiene el valor de la propiedad loctype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLOCTYPE() {
            return loctype;
        }

        /**
         * Define el valor de la propiedad loctype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLOCTYPE(String value) {
            this.loctype = value;
        }

        /**
         * Obtiene el valor de la propiedad otherloctype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOTHERLOCTYPE() {
            return otherloctype;
        }

        /**
         * Define el valor de la propiedad otherloctype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOTHERLOCTYPE(String value) {
            this.otherloctype = value;
        }

        /**
         * Obtiene el valor de la propiedad mimetype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMIMETYPE() {
            return mimetype;
        }

        /**
         * Define el valor de la propiedad mimetype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMIMETYPE(String value) {
            this.mimetype = value;
        }

        /**
         * Obtiene el valor de la propiedad size.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        public Long getSIZE() {
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
        public void setSIZE(Long value) {
            this.size = value;
        }

        /**
         * Obtiene el valor de la propiedad created.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getCREATED() {
            return created;
        }

        /**
         * Define el valor de la propiedad created.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setCREATED(XMLGregorianCalendar value) {
            this.created = value;
        }

        /**
         * Obtiene el valor de la propiedad checksum.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCHECKSUM() {
            return checksum;
        }

        /**
         * Define el valor de la propiedad checksum.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCHECKSUM(String value) {
            this.checksum = value;
        }

        /**
         * Obtiene el valor de la propiedad checksumtype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCHECKSUMTYPE() {
            return checksumtype;
        }

        /**
         * Define el valor de la propiedad checksumtype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCHECKSUMTYPE(String value) {
            this.checksumtype = value;
        }

        /**
         * Obtiene el valor de la propiedad mdtype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMDTYPE() {
            return mdtype;
        }

        /**
         * Define el valor de la propiedad mdtype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMDTYPE(String value) {
            this.mdtype = value;
        }

        /**
         * Obtiene el valor de la propiedad othermdtype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOTHERMDTYPE() {
            return othermdtype;
        }

        /**
         * Define el valor de la propiedad othermdtype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOTHERMDTYPE(String value) {
            this.othermdtype = value;
        }

        /**
         * Obtiene el valor de la propiedad mdtypeversion.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMDTYPEVERSION() {
            return mdtypeversion;
        }

        /**
         * Define el valor de la propiedad mdtypeversion.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMDTYPEVERSION(String value) {
            this.mdtypeversion = value;
        }

        /**
         * Obtiene el valor de la propiedad type.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            if (type == null) {
                return "simple";
            } else {
                return type;
            }
        }

        /**
         * Define el valor de la propiedad type.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Obtiene el valor de la propiedad href.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHref() {
            return href;
        }

        /**
         * Define el valor de la propiedad href.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHref(String value) {
            this.href = value;
        }

        /**
         * Obtiene el valor de la propiedad role.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRole() {
            return role;
        }

        /**
         * Define el valor de la propiedad role.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRole(String value) {
            this.role = value;
        }

        /**
         * Obtiene el valor de la propiedad arcrole.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getArcrole() {
            return arcrole;
        }

        /**
         * Define el valor de la propiedad arcrole.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setArcrole(String value) {
            this.arcrole = value;
        }

        /**
         * Obtiene el valor de la propiedad title.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTitle() {
            return title;
        }

        /**
         * Define el valor de la propiedad title.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTitle(String value) {
            this.title = value;
        }

        /**
         * Obtiene el valor de la propiedad show.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getShow() {
            return show;
        }

        /**
         * Define el valor de la propiedad show.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setShow(String value) {
            this.show = value;
        }

        /**
         * Obtiene el valor de la propiedad actuate.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActuate() {
            return actuate;
        }

        /**
         * Define el valor de la propiedad actuate.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActuate(String value) {
            this.actuate = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element name="binData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
     *         &lt;element name="xmlData" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;any processContents='lax' maxOccurs="unbounded"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/choice&gt;
     *       &lt;attGroup ref="{http://www.loc.gov/METS/}FILECORE"/&gt;
     *       &lt;attGroup ref="{http://www.loc.gov/METS/}METADATA"/&gt;
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
     *       &lt;attribute name="LABEL" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "binData",
        "xmlData"
    })
    public static class MdWrap {

        protected byte[] binData;
        protected XmlData xmlData;
        @XmlAttribute(name = "ID")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;
        @XmlAttribute(name = "LABEL")
        protected String label;
        @XmlAttribute(name = "MIMETYPE")
        protected String mimetype;
        @XmlAttribute(name = "SIZE")
        protected Long size;
        @XmlAttribute(name = "CREATED")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar created;
        @XmlAttribute(name = "CHECKSUM")
        protected String checksum;
        @XmlAttribute(name = "CHECKSUMTYPE")
        protected String checksumtype;
        @XmlAttribute(name = "MDTYPE", required = true)
        protected String mdtype;
        @XmlAttribute(name = "OTHERMDTYPE")
        protected String othermdtype;
        @XmlAttribute(name = "MDTYPEVERSION")
        protected String mdtypeversion;

        /**
         * Obtiene el valor de la propiedad binData.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getBinData() {
            return binData;
        }

        /**
         * Define el valor de la propiedad binData.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setBinData(byte[] value) {
            this.binData = value;
        }

        /**
         * Obtiene el valor de la propiedad xmlData.
         * 
         * @return
         *     possible object is
         *     {@link XmlData }
         *     
         */
        public XmlData getXmlData() {
            return xmlData;
        }

        /**
         * Define el valor de la propiedad xmlData.
         * 
         * @param value
         *     allowed object is
         *     {@link XmlData }
         *     
         */
        public void setXmlData(XmlData value) {
            this.xmlData = value;
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
         * Obtiene el valor de la propiedad label.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLABEL() {
            return label;
        }

        /**
         * Define el valor de la propiedad label.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLABEL(String value) {
            this.label = value;
        }

        /**
         * Obtiene el valor de la propiedad mimetype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMIMETYPE() {
            return mimetype;
        }

        /**
         * Define el valor de la propiedad mimetype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMIMETYPE(String value) {
            this.mimetype = value;
        }

        /**
         * Obtiene el valor de la propiedad size.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        public Long getSIZE() {
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
        public void setSIZE(Long value) {
            this.size = value;
        }

        /**
         * Obtiene el valor de la propiedad created.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getCREATED() {
            return created;
        }

        /**
         * Define el valor de la propiedad created.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setCREATED(XMLGregorianCalendar value) {
            this.created = value;
        }

        /**
         * Obtiene el valor de la propiedad checksum.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCHECKSUM() {
            return checksum;
        }

        /**
         * Define el valor de la propiedad checksum.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCHECKSUM(String value) {
            this.checksum = value;
        }

        /**
         * Obtiene el valor de la propiedad checksumtype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCHECKSUMTYPE() {
            return checksumtype;
        }

        /**
         * Define el valor de la propiedad checksumtype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCHECKSUMTYPE(String value) {
            this.checksumtype = value;
        }

        /**
         * Obtiene el valor de la propiedad mdtype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMDTYPE() {
            return mdtype;
        }

        /**
         * Define el valor de la propiedad mdtype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMDTYPE(String value) {
            this.mdtype = value;
        }

        /**
         * Obtiene el valor de la propiedad othermdtype.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOTHERMDTYPE() {
            return othermdtype;
        }

        /**
         * Define el valor de la propiedad othermdtype.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOTHERMDTYPE(String value) {
            this.othermdtype = value;
        }

        /**
         * Obtiene el valor de la propiedad mdtypeversion.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMDTYPEVERSION() {
            return mdtypeversion;
        }

        /**
         * Define el valor de la propiedad mdtypeversion.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMDTYPEVERSION(String value) {
            this.mdtypeversion = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;any processContents='lax' maxOccurs="unbounded"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "dcContainer",
            "nisoContainer"
        })
        public static class XmlData {

            protected List<SimpleLiteral> dcContainer; //to contain Dublin core metadata
            protected List<Mix> nisoContainer; //to contain NISO metadata

            /**
             * Gets the value of the any property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the any property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAny().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Object }
             * {@link Element }
             * 
             * 
             */
            public List<SimpleLiteral> getLiteral() {
                if (dcContainer == null) {
                    dcContainer = new ArrayList<SimpleLiteral>();
                }
                return this.dcContainer;
            }

            public List<Mix> getMix() {
                if (nisoContainer == null) {
                    nisoContainer = new ArrayList<Mix>();
                }
                return this.nisoContainer;
            }

            public void setLiteral(SimpleLiteral o){
                if (dcContainer == null) {
                    dcContainer = new ArrayList<SimpleLiteral>();
                }
                this.dcContainer.add(o);
            }

            public void setMix(Mix o){
                if (nisoContainer == null) {
                    nisoContainer = new ArrayList<Mix>();
                }
                this.nisoContainer.add(o);
            }


            /**
             * Dublin core format class
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "rights",
                "title",
                "creator",
                "description",
                "type",
                "date"
            })
            public static class SimpleLiteral{

              //decided to declare all avaible atributtes to prevent bad declaration practises
              @XmlElement(name = "dc:rights")
              private String rights;
              @XmlElement(name = "dc:title")
              private String title;
              @XmlElement(name = "dc:creator")
              private String creator;
              @XmlElement(name = "dc:description")
              private String description;
              @XmlElement(name = "dc:type")
              private String type;
              @XmlElement(name = "dc:date")
              private String date;

              /**
               * set rights method
               * @param value
               */
              public void setRights (String value){
                this.rights= value;
              }

              /**
               *
               * @return rights attribute
               */

              public String getRights (){
                return this.rights;
              }

              /*
                Set
               */
              public void setTitle (String value){
                this.title= value;
              }
              public String getTitle (){
                return this.title;
              }

              public void setCreator (String value){
                this.creator= value;
              }
              public String getCreator (){
                return this.creator;
              }

              public void setDescription (String value){
                this.description= value;
              }
              public String getDescription (){
                return this.description;
              }

              public void setType (String value){
                this.type= value;
              }
              public String getType (){
                return this.type;
              }

              public void setDate (String value){
                this.date= value;
              }
              public String getDate (){
                return this.date;
              }

            }

        }

    }

}
