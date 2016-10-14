/**
 * <h1>IncludeType.java</h1> <p> This program is free software: you can redistribute it
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
package dpfmanager.conformancechecker.tiff.implementation_checker.rules.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


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
 *         &lt;element name="implementation" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="exclude" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "includeType", propOrder = {
    "implementation",
    "exclude"
})
public class IncludeType {

    @XmlElement(required = true)
    protected Object implementation;
    protected Object exclude;

    /**
     * Gets the value of the implementation property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getImplementation() {
        return implementation;
    }

    /**
     * Sets the value of the implementation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setImplementation(Object value) {
        this.implementation = value;
    }

    /**
     * Gets the value of the exclude property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getExclude() {
        return exclude;
    }

    /**
     * Sets the value of the exclude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setExclude(Object value) {
        this.exclude = value;
    }

}
