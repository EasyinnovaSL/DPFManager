package dpfmanager.shell.conformancechecker.ImplementationChecker.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by easy on 08/03/2016.
 */
@XmlType
public class TiffHeader extends TiffNode implements TiffNodeInterface {
  String byteOrder;
  String magicNumber;
  int offset;

  public void setByteOrder(String byteOrder) {
    this.byteOrder = byteOrder;
  }

  public String getByteOrder() {
    return byteOrder;
  }

  public void setMagicNumber(String magicNumber) {
    this.magicNumber = magicNumber;
  }

  public String getMagicNumber() {
    return magicNumber;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(new TiffSingleNode("byteOrder", byteOrder));
    childs.add(new TiffSingleNode("magicNumber", magicNumber));
    childs.add(new TiffSingleNode("offset", offset + ""));
    return childs;
  }

  public String getContext() {
    return "header";
  }
}
