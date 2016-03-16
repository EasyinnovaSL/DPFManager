package dpfmanager.shell.conformancechecker.ImplementationChecker.rules;

/**
 * Created by easy on 16/03/2016.
 */
public class RuleResult {
  String message;
  boolean ok;

  public RuleResult(boolean ok, String message) {
    this.message = message;
    this.ok = ok;
  }

  public String getMessage() {
    return message;
  }

  public boolean ok() {
    return ok;
  }
}
