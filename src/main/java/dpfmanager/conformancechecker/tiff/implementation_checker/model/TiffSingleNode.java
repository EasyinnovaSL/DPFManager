package dpfmanager.conformancechecker.tiff.implementation_checker.model;

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

  public TiffSingleNode(String context, String value, int nifd) {
    this.context = context;
    this.value = value;
    this.setLocation("IFD" + nifd);
  }

  public String getContext() {
    return context;
  }

  public String getValue() {
    return value;
  }
}
