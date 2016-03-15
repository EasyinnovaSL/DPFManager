package dpfmanager.shell.conformancechecker.ImplementationChecker.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by easy on 08/03/2016.
 */
@XmlType(name = "tag")
public class TiffTag extends TiffNode implements TiffNodeInterface {
  int id;
  String name;
  int cardinality;
  String type;
  int offset;
  String value;
  TiffIfd exif;
  Hashtable<String, String> iptc;

  public void setId(int id) {
    this.id = id;
  }

  @XmlAttribute
  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlAttribute
  public String getName() {
    return name;
  }

  public void setCardinality(int cardinality) {
    this.cardinality = cardinality;
  }

  public int getCardinality() {
    return cardinality;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setExif(TiffIfd exif) {
    this.exif = exif;
  }

  public TiffIfd getExif() {
    return exif;
  }

  public void setIptc(Hashtable<String, String> iptc) {
    this.iptc = iptc;
  }

  public Hashtable<String, String> getIptc() {
    return iptc;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(new TiffSingleNode("id", id + ""));
    childs.add(new TiffSingleNode("cardinality", cardinality + ""));
    childs.add(new TiffSingleNode("offset", offset + ""));
    childs.add(new TiffSingleNode("name", name));
    childs.add(new TiffSingleNode("type", type));
    childs.add(new TiffSingleNode("value", value));
    if (exif !=null) {
      childs.add(exif);
    }
    if (subchilds) {
      //List<TiffNode> subobjects = exif.getChildren();
      //childs.addAll(subobjects);
    }
    return childs;
  }

  public String getContext() {
    return "tag";
  }
}
