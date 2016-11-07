/**
 * <h1>RuleResultType.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.implementation_checker.implementation_check;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para rule_resultType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="rule_resultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="level" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="context" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleTest" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="iso_reference" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rule_resultType", propOrder = {
    "level",
    "message",
    "context",
    "location",
    "ruleId",
    "ruleTest",
    "ruleValue",
    "isoReference"
})
public class RuleResultType {

    @XmlElement(required = true)
    protected String level;
    @XmlElement(required = true)
    protected String message;
    @XmlElement(required = true)
    protected String context;
    @XmlElement(required = true)
    protected String location;
    @XmlElement(required = true)
    protected String ruleId;
    @XmlElement(required = true)
    protected String ruleTest;
    @XmlElement(required = true)
    protected String ruleValue;
    @XmlElement(name = "iso_reference", required = true)
    protected String isoReference;

    /**
     * Obtiene el valor de la propiedad level.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel() {
        return level;
    }

    /**
     * Define el valor de la propiedad level.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel(String value) {
        this.level = value;
    }

    /**
     * Obtiene el valor de la propiedad message.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Define el valor de la propiedad message.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Obtiene el valor de la propiedad context.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContext() {
        return context;
    }

    /**
     * Define el valor de la propiedad context.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContext(String value) {
        this.context = value;
    }

    /**
     * Obtiene el valor de la propiedad location.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Define el valor de la propiedad location.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Obtiene el valor de la propiedad ruleId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * Define el valor de la propiedad ruleId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleId(String value) {
        this.ruleId = value;
    }

    /**
     * Obtiene el valor de la propiedad ruleTest.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleTest() {
        return ruleTest;
    }

    /**
     * Define el valor de la propiedad ruleTest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleTest(String value) {
        this.ruleTest = value;
    }

    /**
     * Obtiene el valor de la propiedad ruleValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleValue() {
        return ruleValue;
    }

    /**
     * Define el valor de la propiedad ruleValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleValue(String value) {
        this.ruleValue = value;
    }

    /**
     * Obtiene el valor de la propiedad isoReference.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsoReference() {
        return isoReference;
    }

    /**
     * Define el valor de la propiedad isoReference.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsoReference(String value) {
        this.isoReference = value;
    }

}
