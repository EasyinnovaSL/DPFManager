package dpfmanager.conformancechecker.tiff.ImplementationChecker.model;

/**
 * Created by easy on 11/03/2016.
 */
public class TiffSingleNode extends TiffNode {
  public String value;
  private String context;

  public TiffSingleNode(String context, String value) {
    this.context = context;
    this.value = value;
  }

  public String getContext() {
    return context;
  }

  public String getValue() {
    return value;
  }
}
