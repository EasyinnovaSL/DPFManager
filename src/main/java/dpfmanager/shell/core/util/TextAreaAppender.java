/**
 * <h1>TextAreaAppender.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.core.util;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * Created by Adria Llorens on 21/03/2016.
 */
@Plugin(name = "TextAreaAppender", category = "Core", elementType = "appender", printObject = true)
public class TextAreaAppender extends AbstractAppender {

  private static final long serialVersionUID = 1L;
  private static volatile TextArea textArea = null;

  private int maxLines = 0;

  protected TextAreaAppender(String name, Layout<?> layout, Filter filter, int maxLines, boolean ignoreExceptions) {
    super(name, filter, layout, ignoreExceptions);
    this.maxLines = maxLines;
  }

  @PluginFactory
  public static TextAreaAppender createAppender(@PluginAttribute("name") String name,
                                                @PluginAttribute("maxLines") int maxLines,
                                                @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                                @PluginElement("Layout") Layout<?> layout,
                                                @PluginElement("Filters") Filter filter) {

    if (name == null) {
      return null;
    }

    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }
    return new TextAreaAppender(name, layout, filter, maxLines, ignoreExceptions);
  }

  @Override
  public void append(LogEvent event) {
    if (textArea != null) {
      Layout layout = this.getLayout();
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
            String message = new String(layout.toByteArray(event));
            int count = StringUtils.countMatches(textArea.getText(), "\n");
            if (count < maxLines && maxLines != 0) {
              textArea.appendText(message);
            } else {
              textArea.clear();
              textArea.autosize();
              textArea.appendText(message);
            }
          }
      });
    }

  }

  // Add the target TextArea to be populated and updated by the logging information.
  public static void setTextArea(final TextArea ta) {
    textArea = ta;
  }

  public static boolean hasTextArea() {
    return textArea != null;
  }
}
