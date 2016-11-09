/**
 * <h1>RuleType.java</h1> <p> This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.implementation_checker.rules.model;

import dpfmanager.shell.core.DPFManagerProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for ruleType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ruleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="description">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="reference" type="{http://www.dpfmanager.org/ProfileChecker}referenceType" maxOccurs="unbounded"/>
 *         &lt;element name="assert" type="{http://www.dpfmanager.org/ProfileChecker}assertType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="level" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="error"/>
 *             &lt;enumeration value="critical"/>
 *             &lt;enumeration value="warning"/>
 *             &lt;enumeration value="info"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ruleType", propOrder = {
    "title",
    "description",
    "reference",
    "_assert"
})
public class RuleType {

  @XmlElement(required = true)
  protected RuleType.Title title;
  @XmlElement(required = true)
  protected RuleType.Description description;
  @XmlElement(required = true)
  protected List<ReferenceType> reference;
  @XmlElement(name = "assert", required = true)
  protected AssertType _assert;
  @XmlAttribute(name = "id", required = true)
  protected String id;
  @XmlAttribute(name = "context", required = true)
  protected String context;
  @XmlAttribute(name = "level", required = true)
  protected String level;
  @XmlAttribute(name = "experimental")
  protected boolean experimental;

  public boolean isCritical() {
    return getLevel() != null && getLevel().equals("critical");
  }

  public boolean isWarning() {
    return getLevel() != null && getLevel().equals("warning");
  }

  public boolean isError() {
    return getLevel() != null && getLevel().equals("error");
  }

  public boolean isInfo() {
    return getLevel() != null && getLevel().equals("info");
  }

  /**
   * Gets the value of the title property.
   *
   * @return
   *     possible object is
   *     {@link RuleType.Title }
   *
   */
  public RuleType.Title getTitle() {
    return title;
  }

  /**
   * Sets the value of the title property.
   *
   * @param value
   *     allowed object is
   *     {@link RuleType.Title }
   *
   */
  public void setTitle(RuleType.Title value) {
    this.title = value;
  }

  /**
   * Gets the value of the experimental property.
   *
   */
  public boolean getExperimental() {
    return experimental;
  }

  /**
   * Sets the value of the experimental property.
   *
   */
  public void setExperimental(boolean experimental) {
    this.experimental = experimental;
  }

  /**
   * Gets the value of the description property.
   *
   * @return
   *     possible object is
   *     {@link RuleType.Description }
   *
   */
  public RuleType.Description getDescription() {
    return description;
  }

  /**
   * Sets the value of the description property.
   *
   * @param value
   *     allowed object is
   *     {@link RuleType.Description }
   *
   */
  public void setDescription(RuleType.Description value) {
    this.description = value;
  }

  /**
   * Gets the value of the reference property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the reference property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getReference().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ReferenceType }
   *
   *
   */
  public List<ReferenceType> getReference() {
    if (reference == null) {
      reference = new ArrayList<ReferenceType>();
    }
    return this.reference;
  }

  public String getReferenceText() {
    String s = "";
    for (ReferenceType ref : getReference()) {
      if (s.length() > 0) s += "\n";
      if (ref.getDocument() != null && ref.getDocument().toString().length() > 0)
        s += ref.getDocument().toString() + ": ";
      if (!ref.getSection().isEmpty()) {
        s += ref.getSection() + ". ";
      }
      String page = DPFManagerProperties.getBundle().getString("rulePage");
      String refPage = ref.getPage();
      if (refPage == null)
        refPage = ref.getPages();
      if (refPage == null)
        refPage = "";
      s += page.replace("%1", refPage);
    }
    return s;
  }

  /**
   * Gets the value of the assert property.
   *
   * @return
   *     possible object is
   *     {@link AssertType }
   *
   */
  public AssertType getAssert() {
    return _assert;
  }

  /**
   * Sets the value of the assert property.
   *
   * @param value
   *     allowed object is
   *     {@link AssertType }
   *
   */
  public void setAssert(AssertType value) {
    this._assert = value;
  }

  /**
   * Gets the value of the id property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the value of the id property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setId(String value) {
    this.id = value;
  }

  /**
   * Gets the value of the context property.
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
   * Sets the value of the context property.
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
   * Gets the value of the level property.
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
   * Sets the value of the level property.
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
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;simpleContent>
   *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
   *     &lt;/extension>
   *   &lt;/simpleContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
      "value"
  })
  public static class Description {

    @XmlValue
    protected String value;

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValue() {
      return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValue(String value) {
      this.value = value;
    }

  }


  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;simpleContent>
   *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
   *     &lt;/extension>
   *   &lt;/simpleContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
      "value"
  })
  public static class Title {

    @XmlValue
    protected String value;

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValue() {
      return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValue(String value) {
      this.value = value;
    }

  }

}
