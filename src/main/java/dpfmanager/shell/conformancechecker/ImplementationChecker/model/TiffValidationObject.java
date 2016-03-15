package dpfmanager.shell.conformancechecker.ImplementationChecker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by easy on 08/03/2016.
 */
@XmlRootElement
public class TiffValidationObject extends TiffNode implements TiffNodeInterface {
  TiffHeader header;
  TiffIfds lifds;
  long size;

  public void setSize(long size) {
    this.size = size;
  }

  public long getSize() {
    return size;
  }

  public void setHeader(TiffHeader header) {
    this.header = header;
  }

  public TiffHeader getHeader() {
    return header;
  }

  public void setIfds(TiffIfds ifds) {
    this.lifds = ifds;
  }

  public TiffIfds getIfds() {
    return lifds;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(header);
    if (subchilds) childs.addAll(header.getChildren(subchilds));
    childs.add(lifds);
    if (subchilds) childs.addAll(lifds.getChildren(subchilds));
    childs.add(new TiffSingleNode("size", size + ""));
    return childs;
  }

  public List<TiffNode> getObjectsFromContext(String context, boolean subchilds) {
    List<TiffNode> objects = new ArrayList<>();
    objects.add(this);
    objects.addAll(getChildren(subchilds));

    List<TiffNode> objectsFromContext = new ArrayList<TiffNode>();
    for (TiffNode node : objects) {
      if (node == null) {
        // Why?
      } else {
        if (node.getContext().equals(context) || context.equals("*")) {
          objectsFromContext.add(node);
        }
      }
    }
    return objectsFromContext;
  }

  public String getContext() {
    return "tiffValidationObject";
  }
}
