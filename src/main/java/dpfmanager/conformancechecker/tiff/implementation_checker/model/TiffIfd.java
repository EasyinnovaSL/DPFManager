package dpfmanager.conformancechecker.tiff.implementation_checker.model;

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
  int correctCompression;
  int correctPhotometricCasuistic;
  int correctYcbcr;
  int offset;
  String type = "";
  String sclass = "image";
  String filetype = "";
  String imgtype = "";

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

  @XmlAttribute
  public void setFiletype(String filetype) {
    this.filetype = filetype;
  }

  public String getFiletype() {
    return filetype;
  }

  @XmlAttribute
  public void setImgtype(String imgtype) {
    this.imgtype = imgtype;
  }

  public String getImgtype() {
    return imgtype;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
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

  public void setCorrectCompression(int correctCompression) {
    this.correctCompression = correctCompression;
  }

  public int getCorrectCompression() {
    return correctCompression;
  }

  public void setCorrectPhotometricCasuistic(int correctPhotometricCasuistic) {
    this.correctPhotometricCasuistic = correctPhotometricCasuistic;
  }

  public int getCorrectPhotometricCasuistic() {
    return correctPhotometricCasuistic;
  }

  public void setCorrectYcbcr(int correctYcbcr) { this.correctYcbcr = correctYcbcr; }

  public int getCorrectYcbcr() {
    return correctYcbcr;
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
    childs.add(new TiffSingleNode("tagOrdering", tagOrdering + "", n));
    childs.add(new TiffSingleNode("duplicateTags", duplicateTags + "", n));
    childs.add(new TiffSingleNode("strips", strips + "", n));
    childs.add(new TiffSingleNode("tiles", tiles + "", n));
    childs.add(new TiffSingleNode("correctExtraSamples", correctExtraSamples + "", n));
    childs.add(new TiffSingleNode("onlyNecessaryExtraSamples", onlyNecessaryExtraSamples + "", n));
    childs.add(new TiffSingleNode("validBitsPerSample", validBitsPerSample + "", n));
    childs.add(new TiffSingleNode("equalBitsPerSampleValues", equalBitsPerSampleValues + "", n));
    childs.add(new TiffSingleNode("correctTiles", correctTiles + "", n));
    childs.add(new TiffSingleNode("correctStrips", correctStrips + "", n));
    childs.add(new TiffSingleNode("correctCompression", correctCompression + "", n));
    childs.add(new TiffSingleNode("correctPhotometricCasuistic", correctPhotometricCasuistic + "", n));
    childs.add(new TiffSingleNode("correctYcbcr", correctYcbcr + "", n));
    childs.add(new TiffSingleNode("type", type, n));
    childs.add(new TiffSingleNode("class", sclass, n));
    childs.add(new TiffSingleNode("offset", offset + "", n));
    childs.add(new TiffSingleNode("filetype", filetype + "", n));
    childs.add(new TiffSingleNode("imgtype", imgtype + "", n));
    tags.setLocation("IFD" + n);
    childs.add(tags);
    if (subchilds) {
      List<TiffNode> subobjects = tags.getChildren(subchilds);
      for (TiffNode node : subobjects) {
        node.setLocation("IFD" + n);
        childs.add(node);
      }
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
