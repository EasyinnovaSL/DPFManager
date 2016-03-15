package dpfmanager.shell.conformancechecker.ImplementationChecker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 08/03/2016.
 */
public class TiffIfds extends TiffNode implements TiffNodeInterface {
  List<TiffIfd> ifds = null;

  @XmlElement(name = "ifd")
  public void setIfds(List<TiffIfd> ifds) {
    this.ifds = ifds;
  }

  public List<TiffIfd> getIfds() {
    return ifds;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    for (TiffIfd ifd : ifds) {
      childs.add(ifd);
      if (subchilds) {
        List<TiffNode> subobjects = ifd.getChildren(subchilds);
        childs.addAll(subobjects);
      }
    }
    return childs;
  }

  public String getContext() {
    return "ifds";
  }
}
