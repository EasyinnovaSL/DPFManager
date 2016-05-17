package dpfmanager.shell.interfaces.gui.workbench;

import javafx.event.EventType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Created by Adrià Llorens on 06/05/2016.
 */
public class DpfCloseEvent extends WindowEvent {
  public DpfCloseEvent(Stage thestage, EventType<WindowEvent> event) {
    super(thestage, event);
  }
}
