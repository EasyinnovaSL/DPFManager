/**
 * <h1>ReportGenerator.java</h1>
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version; or, at your choice, under the terms of the
 * Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License and the Mozilla Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License and the Mozilla Public License
 * along with this program. If not, see <a
 * href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a> and at <a
 * href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> .
 * </p>
 * <p>
 * NB: for the © statement, include Easy Innova SL or other company/Person contributing the code.
 * </p>
 * <p>
 * © 2015 Easy Innova, SL
 * </p>
 *
 * @author Adrià Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;

import java.io.File;

/**
 * The Class ReportJson.
 * 
 * @author Adrià Llorens
 */
public class ReportJson {

  /**
   * XML to JSON.
   *
   * @param xml the XML
   * @param jsonFilename the json filename
   */
  public static void xmlToJson(String xml, String jsonFilename) {
    CamelContext context = new DefaultCamelContext();
    XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
    xmlJsonFormat.setEncoding("UTF-8");

    // Convert to JSON
    try {
      context.addRoutes(new RouteBuilder() {
        public void configure() {
          from("direct:marshal").marshal(xmlJsonFormat).to("file:" + jsonFilename);
        }
      });
      ProducerTemplate template = context.createProducerTemplate();
      context.start();
      template.sendBody("direct:marshal", xml);
      context.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Move the converted string to the correct file
    String json = getJsonString(jsonFilename);
    ReportGenerator.deleteFileOrFolder(new File(jsonFilename));
    ReportGenerator.writeToFile(jsonFilename, json);
  }

  /**
   * Get the JSON string into the file inside the folderPath.
   *
   * @param folderPath the folder path
   * @return the JSON string
   */
  private static String getJsonString(String folderPath) {
    String path = null;
    // First get the JSON file path
    File folder = new File(folderPath);
    if (folder.isDirectory()) {
      File[] files = folder.listFiles();
      int index = 0;
      boolean found = false;
      while (index < files.length && !found) {
        File file = files[index];
        if (file.isFile()) {
          path = file.getPath();
          found = true;
        }
        index++;
      }
    }

    // Get the file content
    if (path != null) {
      return ReportGenerator.readFile(path);
    }
    return "";
  }
}
