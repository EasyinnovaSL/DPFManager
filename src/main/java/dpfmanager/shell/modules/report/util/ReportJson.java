/**
 * <h1>ReportGenerator.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Adria Llorens Martinez
 * @version 1.0
 * @since 23/6/2015
 */

package dpfmanager.shell.modules.report.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dpfmanager.shell.core.config.BasicConfig;
import dpfmanager.shell.modules.messages.messages.ExceptionMessage;
import dpfmanager.shell.modules.report.core.ReportGenerator;
import dpfmanager.shell.modules.report.core.ReportGeneric;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The Class ReportJson.
 *
 * @author Adria Llorens
 */
public class ReportJson extends ReportGeneric {

  /**
   * XML to JSON.
   *
   * @param xml          the XML
   * @param jsonFilename the json filename
   */
  public void xmlToJson(String xml, String jsonFilename, ReportGenerator generator) {
    // Convert to JSON
    try {
      XmlMapper xmlMapper = new XmlMapper();
      JsonNode node = xmlMapper.readTree(Charset.forName("UTF-8").encode(xml.replace("&#0;", "")).array());

      ObjectMapper jsonMapper = new ObjectMapper();
      String json = jsonMapper.writeValueAsString(node);

      /*JSONObject soapDatainJsonObject = XML.toJSONObject(xml);
      String json = soapDatainJsonObject.toString();*/

      ReportGenerator.deleteFileOrFolder(new File(jsonFilename));
      generator.writeToFile(jsonFilename, json);
    } catch (Exception e) {
      getContext().send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception converting to JSON report " + jsonFilename, e));
    }
  }

  /**
   * XML to JSON.
   *
   * @param xmlFile      the XML File
   * @param jsonFilename the json filename
   */
  public void xmlToJsonFile(String xmlFile, String jsonFilename, ReportGenerator generator) {
    // Convert to JSON
    try {
      String content = new String(Files.readAllBytes(Paths.get(xmlFile)));

      XmlMapper xmlMapper = new XmlMapper();
      JsonNode node = xmlMapper.readTree(content.getBytes());

      ObjectMapper jsonMapper = new ObjectMapper();
      String json = jsonMapper.writeValueAsString(node);

      /*JSONObject soapDatainJsonObject = XML.toJSONObject(content);
      String json = soapDatainJsonObject.toString();*/

      ReportGenerator.deleteFileOrFolder(new File(jsonFilename));
      generator.writeToFile(jsonFilename, json);
    } catch (Exception e) {
      getContext().send(BasicConfig.MODULE_MESSAGE, new ExceptionMessage("Exception converting to JSON", e));
    }
  }

  /**
   * Get the JSON string into the file inside the folderPath.
   *
   * @param folderPath the folder path
   * @return the JSON string
   */
  private static String getJsonString(String folderPath, ReportGenerator generator) {
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
      return generator.readFile(path);
    }
    return "";
  }
}
