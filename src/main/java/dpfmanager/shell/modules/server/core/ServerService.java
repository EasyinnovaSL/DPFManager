package dpfmanager.shell.modules.server.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.conformancechecker.messages.ConformanceMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.server.messages.PostMessage;
import dpfmanager.shell.modules.server.runnable.RequestRunnable;

import org.apache.logging.log4j.Level;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_SERVER)
@Scope("singleton")
public class ServerService extends DpfService {

  private HttpServer server;
  private int port;
  private ExecutorService executor;

  @Resource(name="parameters")
  private Map<String, String> parameters;

//    Well Known Ports: 0 through 1023.
//    Registered Ports: 1024 through 49151.
//    Dynamic/Private : 49152 through 65535.

  @PostConstruct
  public void init() {
    // No context yet
    executor = Executors.newCachedThreadPool();
    // Random port
    port = randInt(49152, 65535);
  }

  public int randInt(int min, int max) {
    return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
  }

  @Override
  protected void handleContext(DpfContext context) {
    // init context
  }

  public void startServer() {
    // Start the server
    try {
      if (parameters.containsKey("-p")){
        Integer myPort = Integer.parseInt(parameters.get("-p"));
        if (1023 < myPort && myPort < 65536){
          port = myPort;
        }
      }
      server = new HttpServer(port, context);
      server.start();
    } catch (Exception e) {
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, "Failed to start server at " + server.getServerUri()));
    }
  }

  /**
   * Main function new check
   */
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
