package dpfmanager.conformancechecker.tiff.implementation_checker.rules;

/**
 * Created by easy on 14/10/2016.
 */
public class ReferenceObject {
  String page;
  String section;

  public void setPage(String page) {
    this.page = page;
  }

  public String getPage() {
    return page;
  }

  public void setSection(String section) {
    this.section = section;
  }

  public String getSection() {
    return section;
  }

  public String getText() {
    return section + ". Page " + page;
  }
}
