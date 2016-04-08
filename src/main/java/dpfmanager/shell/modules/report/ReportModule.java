package dpfmanager.shell.modules.report;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.ArrayMessage;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.core.messages.ReportsMessage;
import dpfmanager.shell.core.messages.ShowMessage;
import dpfmanager.shell.core.messages.UiMessage;
import dpfmanager.shell.modules.conformancechecker.messages.LoadingMessage;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportService;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.report.messages.StatusMessage;

import org.apache.logging.log4j.Level;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_REPORT,
    name = BasicConfig.MODULE_REPORT,
    active = true)
public class ReportModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private ReportService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage){
    if (dpfMessage.isTypeOf(StatusMessage.class)){
      tractStatusMessage(dpfMessage.getTypedMessage(StatusMessage.class));
    } else if (dpfMessage.isTypeOf(IndividualReportMessage.class)){
      service.tractIndividualMessage(dpfMessage.getTypedMessage(IndividualReportMessage.class));
    } else if (dpfMessage.isTypeOf(GlobalReportMessage.class)){
      service.tractGlobalMessage(dpfMessage.getTypedMessage(GlobalReportMessage.class));
    }
  }

  private void tractStatusMessage(StatusMessage status){
    if (status.isInit()){
      service.initNewReportFolder(status.getFolder());
    } else {
      service.deleteTempFiles();
      showReportsGui(status.getFolder(), status.getFormats());
    }
  }

  private void showReportsGui(String filefolder, List<String> formats){
    String type = "";
    String path = "";
    if (formats.contains("HTML")) {
      type = "html";
      path = filefolder + "report.html";
    } else if (formats.contains("XML")) {
      type = "xml";
      path = filefolder + "summary.xml";
    } else if (formats.contains("JSON")) {
      type = "json";
      path = filefolder + "summary.json";
    } else if (formats.contains("PDF")) {
      type = "pdf";
      path = filefolder + "report.pdf";
    }

    // Show reports
    if (!type.isEmpty()) {
      ArrayMessage am = new ArrayMessage();
      am.add(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, new ReportsMessage(ReportsMessage.Type.RELOAD));
      am.add(GuiConfig.PERSPECTIVE_SHOW, new UiMessage());
      am.add(GuiConfig.PERSPECTIVE_SHOW + "." + GuiConfig.COMPONENT_SHOW, new ShowMessage(type, path));
      service.getContext().sendGui(GuiConfig.PERSPECTIVE_REPORTS + "." + GuiConfig.COMPONENT_REPORTS, am);
    } else {
      // No format
      service.getContext().sendGui(BasicConfig.MODULE_MESSAGE, new AlertMessage(AlertMessage.Type.WARNING, "No output format file was selected", formats.toString()));
    }

    // Hide loading
    service.getContext().sendGui(GuiConfig.COMPONENT_DESIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
    service.getContext().sendGui(GuiConfig.PERSPECTIVE_DESSIGN, new LoadingMessage(LoadingMessage.Type.HIDE));
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    service.setContext(new GuiContext(context));
  }

  @Override
  public Context getContext(){
    return context;
  }

}
