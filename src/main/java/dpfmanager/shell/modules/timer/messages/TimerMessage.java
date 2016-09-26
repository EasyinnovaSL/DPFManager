/**
 * <h1>TimerMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.timer.messages;

import dpfmanager.shell.core.messages.DpfMessage;

/**
 * Created by Adrià Llorens on 21/04/2016.
 */
public class TimerMessage extends DpfMessage {

  public enum Type {
    PLAY, STOP, FINISH, RUN
  }

  private Type type;
  private Class clazz;

  public TimerMessage(Type type, Class clazz) {
    // Play & Stop & Finish & Run
    this.type = type;
    this.clazz = clazz;
  }

  public boolean isPlay(){
    return type.equals(Type.PLAY);
  }

  public boolean isStop(){
    return type.equals(Type.STOP);
  }

  public boolean isFinish(){
    return type.equals(Type.FINISH);
  }

  public boolean isRun(){
    return type.equals(Type.RUN);
  }

  public Class getClazz() {
    return clazz;
  }
}
