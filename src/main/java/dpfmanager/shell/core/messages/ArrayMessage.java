package dpfmanager.shell.core.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrià Llorens on 18/03/2016.
 */
public class ArrayMessage {

  private List<String> targets;
  private List<DpfMessage> messages;

  public ArrayMessage() {
    targets = new ArrayList<>();
    messages = new ArrayList<>();
  }

  public void add(String target, DpfMessage message) {
    targets.add(target);
    messages.add(message);
  }

  public boolean hasNext() {
    return targets.size() > 1;
  }

  public String getFirstTarget() {
    return targets.get(0);
  }

  public DpfMessage getFirstMessage() {
    return messages.get(0);
  }

  public void removeFirst() {
    targets.remove(0);
    messages.remove(0);
  }

}
