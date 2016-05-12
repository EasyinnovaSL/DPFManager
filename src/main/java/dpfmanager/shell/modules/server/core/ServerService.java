package dpfmanager.shell.modules.server.core;

import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.server.file.FileServer;
import dpfmanager.shell.modules.server.websocket.WebSocketServer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_SERVER)
@Scope("singleton")
public class ServerService extends DpfService {

  private FileServer serverFile;
  private WebSocketServer serverWSocket;
  private int port;

  @PostConstruct
  public void init() {
    // No context yet
    port = 8080;
    serverWSocket = new WebSocketServer(port);
    serverFile = new FileServer(port);
  }

  @Override
  protected void handleContext(DpfContext context) {
    // init context
  }

  public void startServer() {
    // Start the server
    try {
      serverWSocket.start();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Failed!");
    }
  }

  @PreDestroy
  public void finish() {
    // Finish executor
  }
}
