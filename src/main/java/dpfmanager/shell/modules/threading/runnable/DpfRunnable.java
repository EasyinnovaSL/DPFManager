/**
 * <h1>DpfRunnable.java</h1> <p> This program is free software: you can redistribute it
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
 * @author Adria Llorens
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.shell.modules.threading.runnable;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.DpfContext;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.messages.messages.LogMessage;
import dpfmanager.shell.modules.threading.messages.ThreadsMessage;

import org.apache.logging.log4j.Level;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 13/04/2016.
 */
public abstract class DpfRunnable implements Runnable {

  private boolean paused;
  private boolean canceled;
  protected boolean interrupted;
  private boolean init;
  private boolean finish;


  protected String name;
  protected DpfContext context;
  private Long uuid;

  protected ResourceBundle bundle;

  public DpfRunnable() {
    name = "";
    paused = false;
    canceled = false;
    interrupted = false;

    init = false;
    finish = false;

    bundle = DPFManagerProperties.getBundle();
  }

  public void setName(String n){
    name = n;
    name = name.substring(name.lastIndexOf(".")+1);
  }

  /**
   * Main Run task
   */
  @Override
  public void run() {
    try {
      init = true;
      runTask();
      finish = true;
    } catch (OutOfMemoryError err){
      // Cancel the check and informs out of memory
      context.send(BasicConfig.MODULE_THREADING, new ThreadsMessage(ThreadsMessage.Type.CANCEL, getUuid(), true, "default"));
      context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(bundle.getString("checkCancelled"), err));
    }
  }

  /**
   * Setters & Getters
   */
  public void setContext(DpfContext c){
    context = c;
    handleContext(context);
  }

  public Long getUuid() {
    return uuid;
  }

  public void setUuid(Long uuid) {
    this.uuid = uuid;
  }

  public boolean isFinish() {
    return finish;
  }

  public boolean isInit() {
    return init;
  }

  /**
   * Reimplemented
   */
  public abstract void runTask();

  public abstract void handleContext(DpfContext context);

  /**
   * Thread management
   */
  public synchronized void pause() {
    paused = true;
  }

  public synchronized void resume() {
    paused = false;
    notify();
  }

  public synchronized void cancel() {
    canceled = true;
  }

  public boolean isPaused() {
    return paused;
  }

  /**
   * Custom print lines
   */
  protected void printOut(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.DEBUG, message));
  }

  protected void printErr(String message){
    context.send(BasicConfig.MODULE_MESSAGE, new LogMessage(getClass(), Level.ERROR, message));
  }

  protected void printException(String header, Exception ex){
    context.send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage(header, ex));
  }

}
