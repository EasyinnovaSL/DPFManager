package dpfmanager.shell.conformancechecker.PolicyChecker;

/**
 * Created by easy on 06/10/2015.
 */
public class Rule {
  private String tag;
  private String operator;
  private String value;

  /**
   * Instantiates a new Rule.
   */
  public Rule() {

  }

  /**
   * Gets tag.
   *
   * @return the tag
   */
  public String getTag() {
    return tag;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    try {
      Integer.parseInt(value);
      return "int";
    } catch (Exception e2) {
      try {
        Double.parseDouble(value);
        return "double";
      } catch (Exception e3) {
        return "string";
      }
    }
  }

  /**
   * Gets operator.
   *
   * @return the operator
   */
  public String getOperator() {
    return operator;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * Instantiates a new Rule.
   *
   * @param tag      the tag
   * @param operator the operator
   * @param value    the value
   */
  public Rule (String tag, String operator, String value) {
    this.tag = tag;
    this.operator = operator;
    this.value = value;
  }

  /**
   * Txt string.
   *
   * @return the string
   */
  public String Txt() {
    String txt = "";
    if (tag != null) txt += tag;
    txt += ",";
    if (operator != null) txt += operator;
    txt += ",";
    if (value != null) txt += value;
    return txt;
  }

  /**
   * Read txt.
   *
   * @param txt the txt
   */
  public void ReadTxt(String txt) {
    if (txt.contains(",")) {
      tag = txt.substring(0, txt.indexOf(","));
      String v2 = txt.substring(txt.indexOf(",") + 1);
      if (v2.contains(",")) {
        operator = v2.substring(0, v2.indexOf(","));
        value = v2.substring(v2.indexOf(",") + 1);
      }
    }
  }
}
