/**
 * <h1>JacpExceptionMessage.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.messages.messages;

import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.core.messages.DpfMessage;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 06/04/2016.
 */
public class JacpExceptionMessage extends DpfMessage {

  private String title;
  private String header;
  private String content;
  private Throwable throwable;

  public JacpExceptionMessage(Throwable t){
    ResourceBundle bundle = DPFManagerProperties.getBundle();
    title = bundle.getString("guiException");
    header = bundle.getString("guiError");
    content = t.getMessage();
    throwable = t;
  }

  public String getTitle(){
    return title;
  }

  public String getHeader(){
    return header;
  }

  public String getContent(){
    return content;
  }

  public Throwable getThrowable(){
    return throwable;
  }
}
