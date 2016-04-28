package dpfmanager.conformancechecker.tiff.implementation_checker.model;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by easy on 11/03/2016.
 */
public class TiffNode implements TiffNodeInterface {
  private String location;

  public List<TiffNode> getChildren(boolean subchilds) {
    // To override
    return new ArrayList<>();
  }

  public String getContext() {
    return null;
  }

  public String getValue() {
    return null;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public TiffNode getChild(String nodeName) {
    return getChild(nodeName, null);
  }

  public boolean hasChild(String nodeName) {
    return hasChild(nodeName, null);
  }

  public List<TiffNode> getChildren(String nodeName) {
    return getChildren(nodeName, null);
  }

  public TiffNode getChild(String nodeName, Filter filter) {
    for (TiffNode node : getChildren(nodeName, filter)) {
      if (node.getContext().equals(nodeName)) {
        return node;
      }
    }
    return null;
  }

  public List<TiffNode> getChildren(String nodeName, Filter filter) {
    List<TiffNode> nodes = new ArrayList<>();
    for (TiffNode node : getChildren(false)) {
      if (node.getContext().equals(nodeName)) {
        if (filter == null) {
          nodes.add(node);
        } else {
          if (node.hasChild(filter.getAttribute())) {
            if (node.getChild(filter.getAttribute()).getValue().equals(filter.getValue())) {
              nodes.add(node);
            }
          }
        }
      }
    }
    return nodes;
  }

  public boolean hasChild(String nodeName, Filter filter) {
    for (TiffNode node : getChildren(nodeName, filter)) {
      if (node.getContext().equals(nodeName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    String s = "";
    s += getContext();
    return s;
  }

  public boolean contextMatch(String context) {
    if (context.equals("*")) return true;
    if (!context.contains("[")) return getContext().equals(context);
    else {
      String contextBase = context.substring(0, context.indexOf("["));
      if (getContext().equals(contextBase)) {
        String sFilter = context.substring(context.indexOf("[") + 1);
        sFilter = sFilter.substring(0, sFilter.indexOf("]"));
        boolean matches = true;
        String[] filters = sFilter.split(",");
        for (String filter : filters) {
          String childValue = filter.substring(filter.indexOf("=") + 1).trim();
          String childName = filter.substring(0, filter.indexOf("=")).trim();
          if (!hasChild(childName) || !getChild(childName).getValue().equals(childValue))
            matches = false;
        }
        return matches;
      }
    }
    return false;
  }

}
