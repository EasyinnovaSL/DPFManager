package dpfmanager.shell.conformancechecker.ImplementationChecker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 08/03/2016.
 */
public class TiffTags extends TiffNode implements TiffNodeInterface {
  List<TiffTag> tags = null;

  @XmlElement(name = "tag")
  public void setTags(List<TiffTag> tags) {
    this.tags = tags;
  }

  public List<TiffTag> getTags() {
    return tags;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    if (tags != null) {
      for (TiffTag tag : tags) {
        childs.add(tag);
        if (subchilds) {
          List<TiffNode> subobjects = tag.getChildren(subchilds);
          childs.addAll(subobjects);
        }
      }
    }
    return childs;
  }

  public String getContext() {
    return "tags";
  }
}
