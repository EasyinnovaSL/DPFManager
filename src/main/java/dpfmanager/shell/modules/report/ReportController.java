package dpfmanager.shell.modules.report;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportService;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.File;
import java.net.URI;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Controller(BasicConfig.MODULE_REPORT)
public class ReportController extends DpfSpringController {

  @Autowired
  private ReportService service;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(IndividualReportMessage.class)) {
      service.tractIndividualMessage(dpfMessage.getTypedMessage(IndividualReportMessage.class));
    } else if (dpfMessage.isTypeOf(GlobalReportMessage.class)) {
      service.tractGlobalMessage(dpfMessage.getTypedMessage(GlobalReportMessage.class));
    }
  }

  private void showToUser(String htmlFile) {
    if (Desktop.isDesktopSupported()) {
      try {
        String fullHtmlPath = new File(htmlFile).getAbsolutePath();
        fullHtmlPath = fullHtmlPath.replaceAll("\\\\", "/");
        Desktop.getDesktop().browse(new URI(fullHtmlPath));
      } catch (Exception e) {
        e.printStackTrace();
        service.getContext().send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Error opening the bowser with the global report.", e));
      }
    } else {
      service.getContext().send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Desktop services not suported."));
    }
  }

  @PostConstruct
  public void init() {
    DpfContext dpfContext = new ConsoleContext(appContext);
    service.setContext(dpfContext);
  }

}
