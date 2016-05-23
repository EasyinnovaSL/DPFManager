package dpfmanager.shell.modules.server.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.server.messages.PostMessage;
import dpfmanager.shell.modules.server.runnable.RequestRunnable;
import dpfmanager.shell.modules.server.upload.HttpUploadServer;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_SERVER)
@Scope("singleton")
public class ServerService extends DpfService {

  private HttpUploadServer server;
  private int port;
  private ExecutorService executor;

  @PostConstruct
  public void init() {
    // No context yet
    port = 8080;
    executor = Executors.newCachedThreadPool();
  }

  @Override
  protected void handleContext(DpfContext context) {
    // init context
    server = new HttpUploadServer(port, context);
  }

  public void startServer() {
    // Start the server
    try {
      server.start();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Failed to start server at " + server.getServerUri()));
    }
  }

  public void tractPostRequest(PostMessage pm) {
    RequestRunnable rr = new RequestRunnable(BasicConfig.MODULE_CONFORMANCE, new ConformanceMessage(pm.getUuid(), pm.getFilepath(), pm.getConfigpath()));
    rr.setContext(context);
    executor.execute(rr);
  }

  @PreDestroy
  public void finish() {
    // Finish executor
    executor.shutdownNow();
  }
}
