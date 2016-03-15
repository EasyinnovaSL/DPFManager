package dpfmanager.shell.conformancechecker.ImplementationChecker.model;

import java.util.List;

/**
 * Created by easy on 11/03/2016.
 */
public interface TiffNodeInterface {
  List<TiffNode> getChildren(boolean subchilds);

  String getContext();

  String getValue();
}
