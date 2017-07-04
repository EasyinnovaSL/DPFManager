/**
 * <h1>ReportModule.java</h1> <p> This program is free software: you can redistribute it
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

package dpfmanager.shell.modules.report;

import dpfmanager.shell.core.adapter.DpfModule;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.core.context.GuiContext;
import dpfmanager.shell.core.messages.DpfMessage;
import dpfmanager.shell.modules.report.core.ReportService;
import dpfmanager.shell.modules.report.messages.GenerateIndividualMessage;
import dpfmanager.shell.modules.report.messages.GenerateMessage;
import dpfmanager.shell.modules.report.messages.GlobalReportMessage;
import dpfmanager.shell.modules.report.messages.IndividualReportMessage;

import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.rcp.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;

/**
 * Created by Adria Llorens on 25/02/2016.
 */
@Component(id = BasicConfig.MODULE_REPORT,
    name = BasicConfig.MODULE_REPORT,
    active = true)
public class ReportModule extends DpfModule {

  @Resource
  protected Context context;

  @Autowired
  private ReportService service;

  @Override
  public void handleMessage(DpfMessage dpfMessage) {
    if (dpfMessage.isTypeOf(IndividualReportMessage.class)) {
      service.tractIndividualMessage(dpfMessage.getTypedMessage(IndividualReportMessage.class));
    } else if (dpfMessage.isTypeOf(GlobalReportMessage.class)) {
      service.tractGlobalMessage(dpfMessage.getTypedMessage(GlobalReportMessage.class));
    } else if (dpfMessage.isTypeOf(GenerateMessage.class)) {
      service.tractGenerateMessage(dpfMessage.getTypedMessage(GenerateMessage.class));
    } else if (dpfMessage.isTypeOf(GenerateIndividualMessage.class)) {
      service.tractGenerateIndividualMessage(dpfMessage.getTypedMessage(GenerateIndividualMessage.class));
    }
  }


  @PostConstruct
  public void onPostConstructComponent(final ResourceBundle resourceBundle) {
    service.setContext(new GuiContext(context));
  }

  @Override
  public Context getContext() {
    return context;
  }

}
