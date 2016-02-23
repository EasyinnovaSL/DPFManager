package dpfmanager.conformancechecker.tiff;

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
