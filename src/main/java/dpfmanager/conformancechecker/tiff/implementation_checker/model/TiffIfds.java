package dpfmanager.conformancechecker.tiff.implementation_checker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 08/03/2016.
 */
public class TiffIfds extends TiffNode implements TiffNodeInterface {
  List<TiffIfd> ifds = null;

  @XmlAttribute
  int circularReference;

  @XmlElement(name = "ifd")
  public void setIfds(List<TiffIfd> ifds) {
    this.ifds = ifds;
  }

  public List<TiffIfd> getIfds() {
    return ifds;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(new TiffSingleNode("circularReference", circularReference + ""));
    if (ifds != null) {
      for (TiffIfd ifd : ifds) {
        childs.add(ifd);
        if (subchilds) {
          List<TiffNode> subobjects = ifd.getChildren(subchilds);
          childs.addAll(subobjects);
        }
      }
    }
    return childs;
  }

  public String getContext() {
    return "ifds";
  }

  public void setCircularReference(boolean circularReference) {
    this.circularReference = circularReference ? 1 : 0;
  }

  public int getCircularReference() {
    return circularReference;
  }
}
