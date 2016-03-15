package dpfmanager.shell.conformancechecker.ImplementationChecker.rules;

/**
 * Created by easy on 15/03/2016.
 */
public class Filter {
  String attribute;
  String value;

  public Filter(String filter) {
    attribute = filter.substring(0, filter.indexOf("=")).trim();
    value = filter.substring(filter.indexOf("=") + 1).trim();
  }

  public String getAttribute() {
    return attribute;
  }

  public String getValue() {
    return value;
  }
}
