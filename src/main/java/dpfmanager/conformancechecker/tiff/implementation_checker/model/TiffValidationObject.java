/**
 * <h1>TiffValidationObject.java</h1> <p> This program is free software: you can redistribute it
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

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by easy on 08/03/2016.
 */
@XmlRootElement
public class TiffValidationObject extends TiffNode implements TiffNodeInterface {
  TiffHeader header;
  TiffIfds lifds;
  long size;
  HashMap<String, List<TiffNode>> hashObjects = null;

  public void setSize(long size) {
    this.size = size;
  }

  public long getSize() {
    return size;
  }

  public void setHeader(TiffHeader header) {
    this.header = header;
  }

  public TiffHeader getHeader() {
    return header;
  }

  public void setIfds(TiffIfds ifds) {
    this.lifds = ifds;
  }

  public TiffIfds getIfds() {
    return lifds;
  }

  public List<TiffNode> getChildren(boolean subchilds) {
    List<TiffNode> childs = new ArrayList<TiffNode>();
    childs.add(header);
    if (subchilds) childs.addAll(header.getChildren(subchilds));
    childs.add(lifds);
    if (subchilds) childs.addAll(lifds.getChildren(subchilds));
    childs.add(new TiffSingleNode("size", size + ""));
    return childs;
  }

  public List<TiffNode> getObjectsFromContext(String context, boolean subchilds) {
    String key = context;
    if (subchilds) key += "1"; else key += "0";
    if (hashObjects != null && hashObjects.containsKey(key)) return hashObjects.get(key);

    List<TiffNode> objects = new ArrayList<>();
    objects.add(this);
    objects.addAll(getChildren(subchilds));

    List<TiffNode> objectsFromContext = new ArrayList<TiffNode>();
    for (TiffNode node : objects) {
      if (node == null) {
        // Why?
      } else {
        if (node.contextMatch(context)) {
          objectsFromContext.add(node);
        }
      }
    }

    if (hashObjects == null) hashObjects = new HashMap<>();
    hashObjects.put(key, objectsFromContext);

    return objectsFromContext;
  }

  public String getContext() {
    return "tiffValidationObject";
  }

  public void writeXml(String filename) {
    try {
      File file = new File(filename);
      JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      jaxbMarshaller.marshal(this, file);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

  public String getXml() {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(this, sw);
      return sw.toString();
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return "";
  }
}
