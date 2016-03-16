package dpfmanager.shell.conformancechecker.ImplementationChecker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by easy on 08/03/2016.
 */
@XmlType(name = "ifd")
public class TiffIfd extends TiffNode implements TiffNodeInterface {
  int n;
  TiffTags tags;
  int tagOrdering;
  int duplicateTags;
  int strips;
  int tiles;
  int correctExtraSamples;
  int onlyNecessaryExtraSamples;
  int validBitsPerSample;
  int equalBitsPerSampleValues;
  int correctTiles;
  int correctStrips;
  String type = "";
  String sclass = "image";

  @XmlAttribute
  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @XmlAttribute(name = "class")
  public void setClassElement(String sclass) {
    this.sclass = sclass;
  }

  public String getClassElement() {
    return sclass;
  }

  @XmlAttribute
  public void setN(int n) {
    this.n = n;
  }

  public int getN() {
    return n;
  }

  public void setTagOrdering(int tagOrdering) {
    this.tagOrdering = tagOrdering;
  }

  public int getTagOrdering() {
    return tagOrdering;
  }

  public void setDuplicateTags(int duplicateTags) {
    this.duplicateTags = duplicateTags;
  }

  public int getDuplicateTags() {
    return duplicateTags;
  }

  public void setTags(TiffTags tags) {
    this.tags = tags;
  }

  public TiffTags getTags() {
    return tags;
  }

  @XmlAttribute
  public void setStrips(int strips) {
    this.strips = strips;
  }

  public int getStrips() {
    return strips;
  }

  @XmlAttribute
  public void setTiles(int tiles) {
    this.tiles = tiles;
  }

  public int getTiles() {
    return tiles;
  }

  public void setCorrectExtraSamples(int correctExtraSamples) {
    this.correctExtraSamples = correctExtraSamples;
  }

  public int getCorrectExtraSamples() {
    return correctExtraSamples;
  }

  public void setOnlyNecessaryExtraSamples(int onlyNecessaryExtraSamples) {
    this.onlyNecessaryExtraSamples = onlyNecessaryExtraSamples;
  }

  public int getOnlyNecessaryExtraSamples() {
    return onlyNecessaryExtraSamples;
  }

  public void setValidBitsPerSample(int validBitsPerSample) {
    this.validBitsPerSample = validBitsPerSample;
  }

  public int getValidBitsPerSample() {
    return validBitsPerSample;
  }

  public void setEqualBitsPerSampleValues(int equalBitsPerSampleValues) {
    this.equalBitsPerSampleValues = equalBitsPerSampleValues;
  }

  public int getEqualBitsPerSampleValues() {
    return equalBitsPerSampleValues;
  }

  public void setCorrectStrips(int correctStrips) {
    this.correctStrips = correctStrips;
  }

  public int getCorrectStrips() {
    return correctStrips;
  }

  public void setCorrectTiles(int correctTiles) {
    this.correctTiles = correctTiles;
  }

  public int getCorrectTiles() {
    return correctTiles;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(new TiffSingleNode("n", n + ""));
    childs.add(new TiffSingleNode("tagOrdering", tagOrdering + ""));
    childs.add(new TiffSingleNode("duplicateTags", duplicateTags + ""));
    childs.add(new TiffSingleNode("strips", strips + ""));
    childs.add(new TiffSingleNode("tiles", tiles + ""));
    childs.add(new TiffSingleNode("correctExtraSamples", correctExtraSamples + ""));
    childs.add(new TiffSingleNode("onlyNecessaryExtraSamples", onlyNecessaryExtraSamples + ""));
    childs.add(new TiffSingleNode("validBitsPerSample", validBitsPerSample + ""));
    childs.add(new TiffSingleNode("equalBitsPerSampleValues", equalBitsPerSampleValues + ""));
    childs.add(new TiffSingleNode("correctTiles", correctTiles + ""));
    childs.add(new TiffSingleNode("correctStrips", correctStrips + ""));
    childs.add(new TiffSingleNode("type", type));
    childs.add(new TiffSingleNode("class", sclass));
    childs.add(tags);
    if (subchilds) {
      List<TiffNode> subobjects = tags.getChildren(subchilds);
      childs.addAll(subobjects);
    }
    return childs;
  }

  public String getContext() {
    return "ifd";
  }

  @Override
  public String toString() {
    String s = "ifd " + n;
    return s;
  }
}
