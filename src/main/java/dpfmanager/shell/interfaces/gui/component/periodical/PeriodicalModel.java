package dpfmanager.shell.interfaces.gui.component.periodical;

import dpfmanager.shell.core.mvc.DpfModel;
import dpfmanager.shell.interfaces.gui.fragment.PeriodicFragment;

import org.apache.commons.io.IOUtils;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Adrià Llorens on 07/03/2016.
 */
public class PeriodicalModel extends DpfModel<PeriodicalView, PeriodicalController> {

  private List<ManagedFragmentHandler<PeriodicFragment>> periodicalsFragments;

  public PeriodicalModel() {
    // Init periodical checks list
    periodicalsFragments = new ArrayList<>();
  }

  public void parseXmlString(String xmlTask, String uuid) {
    try {
      InputStream is = IOUtils.toInputStream(xmlTask, "UTF-16");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(is);

      // Parse input & configuration
      String configuration = "";
      String input = "";
      String arguments = doc.getDocumentElement().getElementsByTagName("Arguments").item(0).getTextContent();
      String aux = arguments.substring(18); // Skip -s -configuration
      String[] filesList = aux.split("\"");
      boolean first = true;
      for (String file : filesList) {
        if (!file.replaceAll(" ", "").isEmpty()) {
          if (first) {
            // Configuration
            configuration = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".dpf"));
            first = false;
          } else {
            // Input
            if (input.isEmpty()) {
              input = file;
            } else {
              input += ";" + file;
            }
          }
        }
      }

      // Parse periodicity
      Periodicity periodicity = new Periodicity();
      Periodicity.Mode mode;
      NodeList schDay = doc.getDocumentElement().getElementsByTagName("ScheduleByDay");
      NodeList schWeek = doc.getDocumentElement().getElementsByTagName("ScheduleByWeek");
      NodeList schMonth = doc.getDocumentElement().getElementsByTagName("ScheduleByMonth");
      if (schDay.getLength() > 0) {
        periodicity.setMode(Periodicity.Mode.DAILY);
      } else if (schWeek.getLength() > 0) {
        periodicity.setMode(Periodicity.Mode.WEEKLY);
        Element elemWeek = (Element) schWeek.item(0);
        Node node = elemWeek.getElementsByTagName("DaysOfWeek").item(0);
        NodeList childs = node.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
          Node child = childs.item(i);
          if (child.getNodeType() == Node.ELEMENT_NODE){
            Element elem = (Element) child;
            periodicity.addDaysOfWeek(parseDayName(elem.getTagName()));
          }
        }
      } else if (schMonth.getLength() > 0) {
        periodicity.setMode(Periodicity.Mode.MONTHLY);
        Element elem = (Element) schMonth.item(0);
        String dayStr = elem.getElementsByTagName("Day").item(0).getTextContent();
        periodicity.setDayOfMonth(Integer.parseInt(dayStr));
      }

      // Parse time
      String timeStr = doc.getDocumentElement().getElementsByTagName("StartBoundary").item(0).getTextContent();
      String time = timeStr.substring(11, 16);
      periodicity.setTime(LocalTime.parse(time));

      // Add to model and view
      newPeriodicalFragments(uuid, input, configuration, periodicity);
      getView().addPeriodicalFragmentToView(getPeriodicalFragment(uuid));

      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Integer parseDayName(String name) {
    switch (name) {
      case "Monday":
        return 1;
      case "Tuesday":
        return 2;
      case "Wednesday":
        return 3;
      case "Thursday":
        return 4;
      case "Friday":
        return 5;
      case "Saturday":
        return 6;
      case "Sunday":
        return 7;
      default:
        return -1;
    }
  }

  public void newPeriodicalFragments(String uuid, String input, String configuration, Periodicity periodicity) {
    ManagedFragmentHandler<PeriodicFragment> handler = getView().getManagedFragmentHandler(PeriodicFragment.class);
    handler.getController().init(getController(), uuid, input, configuration, periodicity);
    periodicalsFragments.add(handler);
  }

  public void addPeriodicalsFragments(ManagedFragmentHandler<PeriodicFragment> handler) {
    periodicalsFragments.add(handler);
  }

  public void removePeriodicalCheck(ManagedFragmentHandler<PeriodicFragment> handler) {
    periodicalsFragments.remove(handler);
  }

  public ManagedFragmentHandler<PeriodicFragment> getPeriodicalFragment(String uuid){
    for (ManagedFragmentHandler<PeriodicFragment> handler : periodicalsFragments) {
      if (handler.getController().getUuid().equals(uuid)) {
        return handler;
      }
    }
    return null;
  }

  public List<ManagedFragmentHandler<PeriodicFragment>> getPeriodicalsFragments() {
    return periodicalsFragments;
  }

  public void setPeriodicalsFragments(List<ManagedFragmentHandler<PeriodicFragment>> periodicalsFragments) {
    this.periodicalsFragments = periodicalsFragments;
  }
}
