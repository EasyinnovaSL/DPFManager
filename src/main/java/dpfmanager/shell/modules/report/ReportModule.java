package dpfmanager.shell.modules.report;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.core.ReportService;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

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
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(IndividualReportMessage.class)) {
      service.tractIndividualMessage(dpfMessage.getTypedMessage(IndividualReportMessage.class));
    } else if (dpfMessage.isTypeOf(GlobalReportMessage.class)) {
      service.tractGlobalMessage(dpfMessage.getTypedMessage(GlobalReportMessage.class));
    }
  }


  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    service.setContext(new GuiContext(context));
  }

  @Override
  public Context getContext() {
    return context;
  }

}
