package dpfmanager.shell.modules;

/**
 * Created by easy on 06/10/2015.
 */
public class Rule {
  private String tag;
  private String operator;
  private String value;

  public Rule() {

  }

  public Rule (String tag, String operator, String value) {
    this.tag = tag;
    this.operator = operator;
    this.value = value;
  }
}
