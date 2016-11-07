/**
 * <h1>ImplementationCheckerType.java</h1> <p> This program is free software: you can redistribute it
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para implementation_checkerType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="implementation_checkerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="implementation_check" type="{}implementation_checkType"/>
 *         &lt;element name="results" type="{}resultsType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="TIAProfileChecker" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TIFF_Baseline_Core_6_0" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TIFF_Baseline_Extended_6_0" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TIFF_EP" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TiffITP1ProfileChecker" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TiffITP2ProfileChecker" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TiffITProfileChecker" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalErrors" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalWarnings" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name="implementation_checker")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "implementation_checkerType", propOrder = {
    "implementationCheck",
    "results"
})
public class ImplementationCheckerType {

    @XmlElement(name = "implementation_check", required = true)
    protected ImplementationCheckType implementationCheck;
    @XmlElement(required = true)
    protected ResultsType results;
    @XmlAttribute(name = "TIAProfileChecker")
    protected String tiaProfileChecker;
    @XmlAttribute(name = "TIFF_Baseline_Core_6_0")
    protected String tiffBaselineCore60;
    @XmlAttribute(name = "TIFF_Baseline_Extended_6_0")
    protected String tiffBaselineExtended60;
    @XmlAttribute(name = "TIFF_EP")
    protected String tiffep;
    @XmlAttribute(name = "TiffITP1ProfileChecker")
    protected String tiffITP1ProfileChecker;
    @XmlAttribute(name = "TiffITP2ProfileChecker")
    protected String tiffITP2ProfileChecker;
    @XmlAttribute(name = "TiffITProfileChecker")
    protected String tiffITProfileChecker;
    @XmlAttribute(name = "ref")
    protected String ref;
    @XmlAttribute(name = "totalErrors")
    protected String totalErrors;
    @XmlAttribute(name = "totalWarnings")
    protected String totalWarnings;
    @XmlAttribute(name = "version")
    protected String version;

    /**
     * Obtiene el valor de la propiedad implementationCheck.
     * 
     * @return
     *     possible object is
     *     {@link ImplementationCheckType }
     *     
     */
    public ImplementationCheckType getImplementationCheck() {
        return implementationCheck;
    }

    /**
     * Define el valor de la propiedad implementationCheck.
     * 
     * @param value
     *     allowed object is
     *     {@link ImplementationCheckType }
     *     
     */
    public void setImplementationCheck(ImplementationCheckType value) {
        this.implementationCheck = value;
    }

    /**
     * Obtiene el valor de la propiedad results.
     * 
     * @return
     *     possible object is
     *     {@link ResultsType }
     *     
     */
    public ResultsType getResults() {
        return results;
    }

    /**
     * Define el valor de la propiedad results.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsType }
     *     
     */
    public void setResults(ResultsType value) {
        this.results = value;
    }

    /**
     * Obtiene el valor de la propiedad tiaProfileChecker.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIAProfileChecker() {
        return tiaProfileChecker;
    }

    /**
     * Define el valor de la propiedad tiaProfileChecker.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIAProfileChecker(String value) {
        this.tiaProfileChecker = value;
    }

    /**
     * Obtiene el valor de la propiedad tiffBaselineCore60.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIFFBaselineCore60() {
        return tiffBaselineCore60;
    }

    /**
     * Define el valor de la propiedad tiffBaselineCore60.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIFFBaselineCore60(String value) {
        this.tiffBaselineCore60 = value;
    }

    /**
     * Obtiene el valor de la propiedad tiffBaselineExtended60.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIFFBaselineExtended60() {
        return tiffBaselineExtended60;
    }

    /**
     * Define el valor de la propiedad tiffBaselineExtended60.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIFFBaselineExtended60(String value) {
        this.tiffBaselineExtended60 = value;
    }

    /**
     * Obtiene el valor de la propiedad tiffep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIFFEP() {
        return tiffep;
    }

    /**
     * Define el valor de la propiedad tiffep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIFFEP(String value) {
        this.tiffep = value;
    }

    /**
     * Obtiene el valor de la propiedad tiffITP1ProfileChecker.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTiffITP1ProfileChecker() {
        return tiffITP1ProfileChecker;
    }

    /**
     * Define el valor de la propiedad tiffITP1ProfileChecker.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTiffITP1ProfileChecker(String value) {
        this.tiffITP1ProfileChecker = value;
    }

    /**
     * Obtiene el valor de la propiedad tiffITP2ProfileChecker.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTiffITP2ProfileChecker() {
        return tiffITP2ProfileChecker;
    }

    /**
     * Define el valor de la propiedad tiffITP2ProfileChecker.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTiffITP2ProfileChecker(String value) {
        this.tiffITP2ProfileChecker = value;
    }

    /**
     * Obtiene el valor de la propiedad tiffITProfileChecker.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTiffITProfileChecker() {
        return tiffITProfileChecker;
    }

    /**
     * Define el valor de la propiedad tiffITProfileChecker.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTiffITProfileChecker(String value) {
        this.tiffITProfileChecker = value;
    }

    /**
     * Obtiene el valor de la propiedad ref.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Define el valor de la propiedad ref.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Obtiene el valor de la propiedad totalErrors.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalErrors() {
        return totalErrors;
    }

    /**
     * Define el valor de la propiedad totalErrors.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalErrors(String value) {
        this.totalErrors = value;
    }

    /**
     * Obtiene el valor de la propiedad totalWarnings.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalWarnings() {
        return totalWarnings;
    }

    /**
     * Define el valor de la propiedad totalWarnings.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalWarnings(String value) {
        this.totalWarnings = value;
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
