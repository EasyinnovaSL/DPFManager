/**
 * <h1>ObjectFactory.java</h1> <p> This program is free software: you can redistribute it
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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dpfmanager.conformancechecker.tiff.implementation_checker.rules.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ImplementationCheckerObject_QNAME = new QName("http://www.dpfmanager.org/ProfileChecker", "implementationCheckerObject");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dpfmanager.conformancechecker.tiff.implementation_checker.rules.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RuleType }
     * 
     */
    public RuleType createRuleType() {
        return new RuleType();
    }

    /**
     * Create an instance of {@link ImplementationCheckerObjectType }
     * 
     */
    public ImplementationCheckerObjectType createImplementationCheckerObjectType() {
        return new ImplementationCheckerObjectType();
    }

    /**
     * Create an instance of {@link AssertType }
     * 
     */
    public AssertType createAssertType() {
        return new AssertType();
    }

    /**
     * Create an instance of {@link IncludeType }
     * 
     */
    public IncludeType createIncludeType() {
        return new IncludeType();
    }

    /**
     * Create an instance of {@link RulesType }
     * 
     */
    public RulesType createRulesType() {
        return new RulesType();
    }

    /**
     * Create an instance of {@link ExcludeRulesType }
     * 
     */
    public ExcludeRulesType createExcludeRulesType() {
        return new ExcludeRulesType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link RuleType.Title }
     * 
     */
    public RuleType.Title createRuleTypeTitle() {
        return new RuleType.Title();
    }

    /**
     * Create an instance of {@link RuleType.Description }
     * 
     */
    public RuleType.Description createRuleTypeDescription() {
        return new RuleType.Description();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImplementationCheckerObjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.dpfmanager.org/ProfileChecker", name = "implementationCheckerObject")
    public JAXBElement<ImplementationCheckerObjectType> createImplementationCheckerObject(ImplementationCheckerObjectType value) {
        return new JAXBElement<ImplementationCheckerObjectType>(_ImplementationCheckerObject_QNAME, ImplementationCheckerObjectType.class, null, value);
    }

}
