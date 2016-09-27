/**
 * <h1>TiffNode.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Víctor Muñoz Solà
 * @version 1.0
 * @since 23/7/2015
 */

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
          if (!hasChild(childName) || getChild(childName).getValue() == null || !getChild(childName).getValue().equals(childValue))
            matches = false;
        }
        return matches;
      }
    }
    return false;
  }

}
