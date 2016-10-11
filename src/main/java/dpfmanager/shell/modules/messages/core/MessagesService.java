/**
 * <h1>MessagesService.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.messages.core;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.adapter.DpfService;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.interfaces.gui.workbench.DpfCloseEvent;
import dpfmanager.shell.interfaces.gui.workbench.GuiWorkbench;
import dpfmanager.shell.modules.messages.messages.AlertMessage;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import javafx.stage.WindowEvent;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
@Service(BasicConfig.SERVICE_MESSAGES)
@Scope("singleton")
public class MessagesService extends DpfService {

  @PostConstruct
  public void init() {
    if (System.getProperty("app.home") == null){
      System.setProperty("app.home", DPFManagerProperties.getConfigDir());
    }
  }

  @Override
  protected void handleContext(DpfContext context){
  }

  public void logMessage(LogMessage lm){
    String clazz = lm.getMyClass().toString();
    clazz = clazz.substring(clazz.lastIndexOf(".") + 1, clazz.length());
    if (lm.getLevel().equals(Level.DEBUG)) {
      // Log in console
      systemOut(lm.getMessage());
    } else {
      // Default pattern
      LogManager.getLogger(clazz).log(lm.getLevel(), lm.getMessage());
    }
  }

  public void exceptionMessage(ExceptionMessage em) {
    LogManager.getLogger("").log(Level.ERROR, em.getHeader());
    if (!em.isOutOfMemory()) {
      systemErr(em.getException().getMessage());
      systemErr(AlertsManager.getExceptionText(em.getException()));
    } else {

    }
  }

  public void alertMessage(AlertMessage am){
    Level level = Level.WARN;
    if (am.getType().equals(AlertMessage.Type.ERROR)){
      level = Level.ERROR;
    }
    LogManager.getLogger("").log(level, am.getHeader());
    systemOut(am.getContent());
  }

  public void systemOut(String message){
    LogManager.getLogger("").log(Level.DEBUG, MarkerManager.getMarker("PLAIN"), message);
  }

  public void systemErr(String message){
    LogManager.getLogger("").log(Level.ERROR, MarkerManager.getMarker("EXCEPTION"), message);
  }


}
