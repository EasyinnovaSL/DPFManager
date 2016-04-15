package dpfmanager.shell.modules.report.messages;

import dpfmanager.shell.core.messages.DpfMessage;

import java.util.List;

/**
 * Created by Adrià Llorens on 08/04/2016.
 */
public class FinishReportMessage extends DpfMessage {

  private String internal;
  private List<String> formats;

  public FinishReportMessage(List<String> f, String i){
    formats = f;
    internal = i;
  }

  public List<String> getFormats() {
    return formats;
  }

  public String getInternalFolder() {
    return internal;
  }
}
