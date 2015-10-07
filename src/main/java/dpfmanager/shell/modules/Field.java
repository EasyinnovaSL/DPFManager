package dpfmanager.shell.modules;

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

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public ArrayList<String> getOperators() {
    return operators;
  }

  public Field() {
    operators = new ArrayList<String>();
  }

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
      }
    }
  }
}