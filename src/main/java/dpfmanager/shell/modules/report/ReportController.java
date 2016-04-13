package dpfmanager.shell.modules.report;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.ConsoleContext;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.core.MessagesService;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.report.core.ReportService;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;
import dpfmanager.shell.modules.report.messages.StatusMessage;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
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
      // Init new check
      service.initNewReportFolder(status.getFolder());
    } else {
      // Finish check
      if (!status.isSilence()) {
        String name = "report.html";
        String htmlPath = service.getInternalReportFolder() + name;
        String output = service.getConfig().getOutput();
        if (output != null){
          htmlPath = output + "/" + name;
        }
        File htmlFile = new File(htmlPath);
        if (htmlFile.exists()) {
          showToUser(htmlPath);
        }
      }
      service.getContext().send(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(ConformanceMessage.Type.DELETE));
    }
  }

  private void showToUser(String htmlFile){
    if (Desktop.isDesktopSupported()) {
      try {
        String fullHtmlPath = new File(htmlFile).getAbsolutePath();
        fullHtmlPath = fullHtmlPath.replaceAll("\\\\", "/");
        Desktop.getDesktop().browse(new URI("file://" + fullHtmlPath.replaceAll(" ", "%20")));
      } catch (Exception e) {
        e.printStackTrace();
        service.getContext().send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Error opening the bowser with the global report.", e));
      }
    } else {
      service.getContext().send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, "Desktop services not suported."));
    }
  }

  @PostConstruct
  public void init( ) {
    DpfContext dpfContext = new ConsoleContext(appContext);
    service.setContext(dpfContext);
  }

}
