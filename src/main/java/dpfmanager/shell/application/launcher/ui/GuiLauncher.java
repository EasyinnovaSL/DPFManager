/**
 * <h1>GuiLauncher.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.application.launcher.ui;

import javafx.stage.Stage;

import org. jacpfx.api.annotations.workbench.Workbench;
import org.jacpfx.api.exceptions.AnnotationNotFoundException;
import org.jacpfx.api.exceptions.AttributeNotFoundException;
import org.jacpfx.api.exceptions.ComponentNotFoundException;
import org.jacpfx.api.fragment.Scope;
import org.jacpfx.api.launcher.Launcher;
import org.jacpfx.rcp.workbench.EmbeddedFXWorkbench;
import org.jacpfx.rcp.workbench.FXWorkbench;
import org.jacpfx.spring.launcher.ASpringLauncher;
import org.jacpfx.spring.launcher.SpringXmlConfigLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by amo on 21.08.14.
 */
public abstract class GuiLauncher extends ASpringLauncher {

  public static Parameters parameters;

  @SuppressWarnings("unchecked")
  @Override
  public void start(Stage stage) throws Exception {
    parameters = getParameters();
    initExceptionHandler();
    final Launcher<ClassPathXmlApplicationContext> launcher = new SpringXmlConfigLauncher(getXmlConfig());
    scanPackegesAndInitRegestry();
    final Class<? extends FXWorkbench> workbenchHandler = getWorkbenchClass();
    if (workbenchHandler == null)
      throw new ComponentNotFoundException("no FXWorkbench class defined");
    initWorkbench(stage, launcher, workbenchHandler);
  }


  private void initWorkbench(final Stage stage, final Launcher<ClassPathXmlApplicationContext> launcher, final Class<? extends FXWorkbench> workbenchHandler) {
    if (workbenchHandler.isAnnotationPresent(Workbench.class)) {
      this.workbench = createWorkbench(launcher, workbenchHandler);
      workbench.init(launcher, stage);
      postInit(stage);
    } else {
      throw new AnnotationNotFoundException("no @Workbench annotation found on class");
    }
  }

  private EmbeddedFXWorkbench createWorkbench(final Launcher<ClassPathXmlApplicationContext> launcher, final Class<? extends FXWorkbench> workbenchHandler) {
    final Workbench annotation = workbenchHandler.getAnnotation(Workbench.class);
    final String id = annotation.id();
    if (id.isEmpty())
      throw new AttributeNotFoundException("no workbench id found for: " + workbenchHandler);
    final FXWorkbench handler = launcher.registerAndGetBean(workbenchHandler, id, Scope.SINGLETON);
    return new EmbeddedFXWorkbench(handler, getWorkbenchDecorator());
  }

  public abstract String getXmlConfig();

  public static Parameters getMyParameters(){
    return parameters;
  }

}