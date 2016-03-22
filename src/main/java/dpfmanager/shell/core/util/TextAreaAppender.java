package dpfmanager.shell.core.util;

import javafx.scene.control.TextArea;

import org.apache.commons.lang.StringUtils;
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
 * Created by Adrià Llorens on 21/03/2016.
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
    String message = new String(this.getLayout().toByteArray(event));
    int count = StringUtils.countMatches(textArea.getText(), "\n");
    if (count < maxLines && maxLines != 0) {
      textArea.appendText(message);
    } else {
      textArea.clear();
      textArea.appendText(message);
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
