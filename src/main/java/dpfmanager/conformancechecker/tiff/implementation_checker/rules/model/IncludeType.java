
package dpfmanager.conformancechecker.tiff.implementation_checker.rules.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for includeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="includeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="policyChecker" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="excluderules" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "includeType", namespace = "http://www.dpfmanager.org/ProfileChecker", propOrder = {
    "policyChecker",
    "excluderules"
})
public class IncludeType {

    @XmlElement(namespace = "http://www.dpfmanager.org/ProfileChecker", required = true)
    protected String policyChecker;
    @XmlElement(namespace = "http://www.dpfmanager.org/ProfileChecker")
    protected List<String> excluderules;

    /**
     * Gets the value of the policyChecker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyChecker() {
        return policyChecker;
    }

    /**
     * Sets the value of the policyChecker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyChecker(String value) {
        this.policyChecker = value;
    }

    /**
     * Gets the value of the excluderules property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the excluderules property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExcluderules().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getExcluderules() {
        if (excluderules == null) {
            excluderules = new ArrayList<String>();
        }
        return this.excluderules;
    }

}
