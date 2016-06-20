package dpfmanager.conformancechecker.tiff.implementation_checker;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by easy on 29/03/2016.
 */
public class ValidationResult {
  private List<RuleResult> result;

  /**
   * Instantiates a new Validation result.
   */
  public ValidationResult() {
    result = new ArrayList<>();
  }

  /**
   * Add rule.
   *
   * @param ruleResult the rule result
   */
  public void add(RuleResult ruleResult) {
    result.add(ruleResult);
  }

  /**
   * Gets result.
   *
   * @return the result
   */
  public List<RuleResult> getResult() {
    return result;
  }

  /**
   * Gets errors.
   *
   * @return the errors
   */
  public List<RuleResult> getErrors() {
    List<RuleResult> errors = new ArrayList<>();
    for (RuleResult res : result) {
      if (!res.ok() && !res.getWarning()) {
        errors.add(res);
      }
    }
    return errors;
  }

  /**
   * Gets warnings.
   *
   * @return the warnings
   */
  public List<RuleResult> getWarnings() {
    List<RuleResult> errors = new ArrayList<>();
    for (RuleResult res : result) {
      if (!res.ok() && res.getWarning()) {
        errors.add(res);
      }
    }
    return errors;
  }
}
