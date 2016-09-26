/**
 * <h1>ConsoleContext.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.core.context;

import dpfmanager.shell.core.adapter.DpfSpringController;
import dpfmanager.shell.core.messages.DpfMessage;

import org.springframework.context.ApplicationContext;

/**
 * Created by Adrià Llorens on 07/04/2016.
 */
public class ConsoleContext implements DpfContext {

  private ApplicationContext context;

  public ConsoleContext(ApplicationContext c){
    context = c;
  }

  @Override
  public void send(String target, Object message) {
    if (context != null){
      DpfSpringController controller = (DpfSpringController) context.getBean(target);
      controller.handleDpfMessage((DpfMessage) message);
    } else{
      System.out.println("ERROR!");
      System.out.println("Application context is null.");
    }
  }

  @Override
  public void sendAfter(String target, Object message, Integer seconds) {
    if (context != null){
      try {
        Thread.sleep(seconds * 1000);
      } catch (InterruptedException e){
      }
      DpfSpringController controller = (DpfSpringController) context.getBean(target);
      controller.handleDpfMessage((DpfMessage) message);
    } else{
      System.out.println("ERROR!");
      System.out.println("Application context is null.");
    }
  }

  @Override
  public void sendGui(String target, Object message){
  }

  @Override
  public void sendConsole(String target, Object message){
    send(target, message);
  }

  @Override
  public Object sendAndWaitResponse(String target, Object message) {
    if (context != null){
      DpfSpringController controller = (DpfSpringController) context.getBean(target);
      return controller.handleDpfMessageWithResponse((DpfMessage) message);
    } else{
      System.out.println("ERROR!");
      System.out.println("Application context is null.");
      return null;
    }
  }

  @Override
  public boolean isConsole() {
    return true;
  }

  @Override
  public boolean isGui() {
    return false;
  }
}
