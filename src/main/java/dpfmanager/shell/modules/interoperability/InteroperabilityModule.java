/**
 * <h1>DatabaseModule.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.interoperability;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.config.GuiConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.database.core.DatabaseService;
import dpfmanager.shell.modules.database.messages.JobsMessage;
import dpfmanager.shell.modules.interoperability.core.ConformanceConfig;
import dpfmanager.shell.modules.interoperability.core.InteroperabilityService;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityMessage;
import dpfmanager.shell.modules.interoperability.messages.InteroperabilityResponseMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Adrià Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_INTEROPERABILITY,
    name = BasicConfig.MODULE_INTEROPERABILITY,
    active = true)
public class InteroperabilityModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private InteroperabilityService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(InteroperabilityMessage.class)){
      InteroperabilityMessage im = dpfMessage.getTypedMessage(InteroperabilityMessage.class);
      if (im.isAdd()){
        service.add(im.getName(), im.getExtra(), im.getParameters(), im.getConfigure(), im.getExtensions(), im.isEnabled());
      } else if (im.isEdit()){
        service.edit(im.getName(), im.getExtra());
      }  else if (im.isRemove()){
        service.remove(im.getName());
      } else if (im.isInfo()){
        service.list(im.getName());
      } else if (im.isList()){
        service.listAll();
      } else if (im.isParameters()){
        service.setParameters(im.getName(), im.getExtra());
      } else if (im.isConfigure()){
        service.setConfiguration(im.getName(), im.getExtra());
      } else if (im.isExtensions()){
        service.setExtensions(im.getName(), im.getExtensions());
      } else if (im.isEnable()){
        service.setEnabled(im.getName(), true);
      } else if (im.isDisable()){
        service.setEnabled(im.getName(), false);
      } else if (im.isObjects()){
        List<ConformanceConfig> list = service.listObjects();
        context.send(GuiConfig.COMPONENT_INTEROPERABILITY, new InteroperabilityResponseMessage(InteroperabilityResponseMessage.Type.OBJECTS, list));
      } else if (im.isGuiEdit()){
        service.guiedit(im.getConfig());
      }
    }
  }

  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    service.setContext(new GuiContext(context));
  }

  @PreDestroy
  public void onPreDestroyComponent() {
  }

  @Override
  public Context getContext() {
    return context;
  }

}
