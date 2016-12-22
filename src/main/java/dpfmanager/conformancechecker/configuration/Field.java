/**
 * <h1>Field.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.conformancechecker.configuration;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by easy on 07/10/2015.
 */
public class Field {
  private String name;
  private String type;
  private String description;
  private ArrayList<String> operators;
  private ArrayList<String> values;

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets operators.
   *
   * @return the operators
   */
  public ArrayList<String> getOperators() {
    return operators;
  }

  /**
   * Gets values.
   *
   * @return the values
   */
  public ArrayList<String> getValues() {
    return values;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Instantiates a new Field.
   */
  public Field() {
    operators = new ArrayList<String>();
  }

  /**
   * Instantiates a new Field.
   *
   * @param nodelist the nodelist
   */
  public Field(NodeList nodelist) {
    for (int j=0;j<nodelist.getLength();j++) {
      Node child = nodelist.item(j);
      if (child.getNodeName().equals("name")) {
        this.name = nodelist.item(j).getTextContent();
      } else if (child.getNodeName().equals("type")) {
        this.type = nodelist.item(j).getTextContent();
      } else if (child.getNodeName().equals("description")) {
        this.description = nodelist.item(j).getTextContent();
      } else if (child.getNodeName().equals("operators")) {
        String operators = nodelist.item(j).getTextContent();
        this.operators = new ArrayList();
        for (String op : operators.split(",")) {
          this.operators.add(op);
        }
      } else if (child.getNodeName().equals("values")) {
        String operators = nodelist.item(j).getTextContent();
        this.values = new ArrayList();
        for (String val : operators.split(",")) {
          this.values.add(val);
        }
      }
    }
  }
}
