/**
 * <h1>TiffIfds.java</h1> <p> This program is free software: you can redistribute it
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by easy on 08/03/2016.
 */
public class TiffIfds extends TiffNode implements TiffNodeInterface {
  List<TiffIfd> ifds = null;

  @XmlAttribute
  int circularReference;

  @XmlElement(name = "ifd")
  public void setIfds(List<TiffIfd> ifds) {
    this.ifds = ifds;
  }

  public List<TiffIfd> getIfds() {
    return ifds;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(new TiffSingleNode("circularReference", circularReference + ""));
    if (ifds != null) {
      for (TiffIfd ifd : ifds) {
        childs.add(ifd);
        if (subchilds) {
          List<TiffNode> subobjects = ifd.getChildren(subchilds);
          childs.addAll(subobjects);
        }
      }
    }
    return childs;
  }

  public String getContext() {
    return "ifds";
  }

  public void setCircularReference(boolean circularReference) {
    this.circularReference = circularReference ? 1 : 0;
  }

  public int getCircularReference() {
    return circularReference;
  }
}
