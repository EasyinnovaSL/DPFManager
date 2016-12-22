/**
 * <h1>Validator.java</h1> <p> This program is free software: you can redistribute it and/or modify
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

package dpfmanager.conformancechecker.tiff.policy_checker;

import dpfmanager.conformancechecker.ConformanceChecker;
import dpfmanager.conformancechecker.DpfLogger;
import dpfmanager.conformancechecker.tiff.implementation_checker.ImplementationCheckerLoader;
import dpfmanager.conformancechecker.tiff.implementation_checker.ValidationResult;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffNode;
import dpfmanager.conformancechecker.tiff.implementation_checker.model.TiffValidationObject;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.Clausule;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.Clausules;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleElement;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.AssertType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RulesType;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by easy on 11/03/2016.
 */
public class PolicyChecker {
  /* The Dpf logger */
  private DpfLogger Logger;

  public PolicyChecker() {
    Logger = null;
  }

  public PolicyChecker(DpfLogger log) {
    Logger = log;
  }

  public ValidationResult validate(ValidationResult result, List<String> removedRulesId){
    if (removedRulesId.size() > 0) {
      for (RuleResult ruleResult : result.getResult()) {
        if (ruleResult.getRule() != null) {
          ruleResult.setRelaxed(removedRulesId.contains(ruleResult.getRule().getId()));
        } else {
          ruleResult.setRelaxed(false);
        }
      }
    }
    return result;
  }

  private void printOut(String line) {
    if (Logger != null) {
      Logger.println(line);
    }
  }
}
