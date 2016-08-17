package dpfmanager.conformancechecker.tiff.implementation_checker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 08/03/2016.
 */
public class TiffTags extends TiffNode implements TiffNodeInterface {
  List<TiffTag> tags = null;
  int tagsCount;

  public void setTagsCount(int n) {
    tagsCount = n;
  }

  @XmlAttribute
  public int getTagsCount() {
    return tagsCount;
  }

  @XmlElement(name = "tag")
  public void setTags(List<TiffTag> tags) {
    this.tags = tags;
  }

  public List<TiffTag> getTags() {
    return tags;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(new TiffSingleNode("tagsCount", tagsCount + ""));
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
