/**
 * <h1>TimeSpinner.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.interfaces.gui.component.periodical;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Adrià Llorens on 01/07/2016.
 */
public class TimeSpinner extends Spinner<LocalTime> {

  enum Mode {

    HOURS {
      @Override
      LocalTime increment(LocalTime time, int steps) {
        return time.plusHours(steps);
      }

      @Override
      void select(TimeSpinner spinner) {
        int index = spinner.getEditor().getText().indexOf(':');
        spinner.getEditor().selectRange(0, index);
      }
    },
    MINUTES {
      @Override
      LocalTime increment(LocalTime time, int steps) {
        return time.plusMinutes(steps);
      }

      @Override
      void select(TimeSpinner spinner) {
        int index = spinner.getEditor().getText().lastIndexOf(':');
        spinner.getEditor().selectRange(index + 1, spinner.getEditor().getText().length());
      }
    };

    abstract LocalTime increment(LocalTime time, int steps);

    abstract void select(TimeSpinner spinner);

    LocalTime decrement(LocalTime time, int steps) {
      return increment(time, -steps);
    }
  }

  // Property containing the current editing mode:

  private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS);

  public ObjectProperty<Mode> modeProperty() {
    return mode;
  }

  public final Mode getMode() {
    return modeProperty().get();
  }

  public final void setMode(Mode mode) {
    modeProperty().set(mode);
  }

  public TimeSpinner(LocalTime time) {
    setPrefWidth(80.0);
    setEditable(true);

    // Create a StringConverter for converting between the text in the
    // editor and the actual value:
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

      @Override
      public String toString(LocalTime time) {
        return formatter.format(time);
      }

      @Override
      public LocalTime fromString(String string) {
        String[] tokens = string.split(":");
        int hours = getIntField(tokens, 0);
        int minutes = getIntField(tokens, 1);
        int totalSeconds = (hours * 60 + minutes) * 60;
        return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60);
      }

      private int getIntField(String[] tokens, int index) {
        if (tokens.length <= index || tokens[index].isEmpty()) {
          return 0;
        }
        return Integer.parseInt(tokens[index]);
      }

    };

    // The textFormatter both manages the text <-> LocalTime conversion,
    // and vetoes any edits that are not valid. We just make sure we have
    // two colons and only digits in between:
    TextFormatter<LocalTime> textFormatter = new TextFormatter<LocalTime>(localTimeConverter, time, c -> {
      String newText = c.getControlNewText();
      if (newText.matches("[0-9]{0,2}:[0-9]{0,2}")) {
        return c;
      }
      return null;
    });

    // The spinner value factory defines increment and decrement by
    // delegating to the current editing mode:
    SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
      {
        setConverter(localTimeConverter);
        setValue(time);
      }

      @Override
      public void decrement(int steps) {
        setValue(mode.get().decrement(getValue(), steps));
        mode.get().select(TimeSpinner.this);
      }

      @Override
      public void increment(int steps) {
        setValue(mode.get().increment(getValue(), steps));
        mode.get().select(TimeSpinner.this);
      }

    };

    this.setValueFactory(valueFactory);
    this.getEditor().setTextFormatter(textFormatter);

    // Update the mode when the user interacts with the editor.
    // This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
    // could result in incorrect state. Directly observing the caretPostion
    // didn't work well though; getting that to work properly might be
    // a better approach in the long run.
    this.getEditor().addEventHandler(InputEvent.ANY, e -> {
      int caretPos = this.getEditor().getCaretPosition();
      int hrIndex = this.getEditor().getText().indexOf(':');
      if (caretPos <= hrIndex) {
        mode.set(Mode.HOURS);
      } else {
        mode.set(Mode.MINUTES);
      }
    });

    // When the mode changes, select the new portion:
    mode.addListener((obs, oldMode, newMode) -> newMode.select(this));

  }

  public TimeSpinner() {
    this(LocalTime.now());
  }
}