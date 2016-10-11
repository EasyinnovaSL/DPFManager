/**
 * <h1>ServerService.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adrià Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.server.core;

import dpfmanager.shell.core.DPFManagerProperties;
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
import java.util.ResourceBundle;
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
  private ResourceBundle bundle;

  @Resource(name="parameters")
  private Map<String, String> parameters;

//    Well Known Ports: 0 through 1023.
//    Registered Ports: 1024 through 49151.
//    Dynamic/Private : 49152 through 65535.

  @PostConstruct
  public void init() {
    // No context yet
    bundle = DPFManagerProperties.getBundle();
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
      context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, bundle.getString("failedServer").replace("%1",server.getServerUri())));
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
