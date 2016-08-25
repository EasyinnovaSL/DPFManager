package dpfmanager.conformancechecker.tiff.implementation_checker.model;

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
  boolean asci7;
  int offset;
  String value;
  boolean duplicatedNuls;
  boolean usedOffset;
  boolean offsetOverlap;
  int lastByte;
  TiffIfd exif;
  TiffIfd subifd;
  TiffIfd globalparameters;
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
  public boolean getAsci7() {
    return asci7;
  }

  public void setAsci7(boolean asci7) {
    this.asci7 = asci7;
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

  @XmlAttribute
  public String getType() {
    return type;
  }

  public void setLastByte(int val) {
    this.lastByte = val;
  }

  public int getLastByte() {
    return lastByte;
  }

  public void setDuplicatedNuls(boolean val) {
    this.duplicatedNuls = val;
  }

  public boolean getDuplicatedNuls() {
    return duplicatedNuls;
  }

  public void setUsedOffset(boolean val) {
    this.usedOffset = val;
  }

  public boolean getUsedOffset() {
    return usedOffset;
  }

  public void setOffsetOverlap(boolean val) {
    this.offsetOverlap = val;
  }

  public boolean getOffsetOverlap() {
    return offsetOverlap;
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

  public void setSubIfd(TiffIfd subifd) {
    this.subifd = subifd;
  }

  public TiffIfd getSubIfd() {
    return subifd;
  }

  public void setGlobalParameters(TiffIfd globalparameters) {
    this.globalparameters = globalparameters;
  }

  public TiffIfd getGlobalParameters() {
    return globalparameters;
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
    childs.add(new TiffSingleNode("asci7", asci7 + ""));
    childs.add(new TiffSingleNode("value", value));
    childs.add(new TiffSingleNode("duplicatedNuls", duplicatedNuls + ""));
    childs.add(new TiffSingleNode("lastByte", lastByte + ""));
    childs.add(new TiffSingleNode("usedOffset", usedOffset + ""));
    childs.add(new TiffSingleNode("offsetOverlap", offsetOverlap + ""));
    if (exif !=null) {
      childs.add(exif);
    }
    if (subifd !=null) {
      childs.add(subifd);
      if (subchilds) {
        childs.addAll(subifd.getChildren(subchilds));
      }
    }
    if (globalparameters !=null) {
      childs.add(globalparameters);
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

  @Override
  public String toString() {
    String s = "tag " + id + " " + name;
    return s;
  }
}
