package dpfmanager;

import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.ValidationEvent;
import com.easyinnova.tiff.model.types.IFD;

import org.apache.commons.lang.time.FastDateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

/**
 * The Class ReportHtml.
 */
public class ReportHtml {

  /**
   * Read the file of the path
   *
   * @param path the path to read.
   * @return the content of the file in path
   */
  private static String readFile(String path) {
    BufferedReader br;
    String text = "";
    try {
      br = new BufferedReader(new FileReader(path));
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
      }
      text = sb.toString();
      br.close();
    } catch (FileNotFoundException e) {
      System.out.println("Template for html not found.");
    } catch (IOException e) {
      System.out.println("Error reading " + path);
    }
    return text;
  }
  
  private static void copy(File sourceLocation, File targetLocation) throws IOException {
    if (sourceLocation.isDirectory()) {
      copyDirectory(sourceLocation, targetLocation);
    } else {
      copyFile(sourceLocation, targetLocation);
    }
  }
  
  private static void copyDirectory(File source, File target) throws IOException {
    if (!target.exists()) {
      target.mkdir();
    }
    for (String f : source.list()) {
      copy(new File(source, f), new File(target, f));
    }
  }
  
  private static void copyFile(File source, File target) throws IOException {        
    try (
      InputStream in = new FileInputStream(source);
      OutputStream out = new FileOutputStream(target)
    ) {
      byte[] buf = new byte[1024];
      int length;
      while ((length = in.read(buf)) > 0) {
        out.write(buf, 0, length);
      }
    }
  }

  /**
   * Copy the folder path into the folder of the file name
   *
   * @param path the path to copy from.
   * @param name the name of the file inside the path to copy.
   */
  private static void copyFolder(String path, String name) {
    File nameFile = new File(name);
    String absolutePath = nameFile.getAbsolutePath();
    String srcDir = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
    
    File source = new File(path);
    File target = new File(srcDir + "/html");
    if (!target.exists()) {
      target.mkdir();
    }
  
    try {
      copyDirectory(source, target);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Parse an individual report to HTML.
   *
   * @param ir the individual report.
   */
  public static void parseIndividual(String outputfile, IndividualReport ir) {
    String tPath = "resources/templates/individual.html";
    String htmlFolder = "resources/html/";
    copyFolder(htmlFolder,outputfile);
    
    String htmlBody = readFile(tPath);
    
    //Basic info
    htmlBody = htmlBody.replace("##IMG_NAME##",ir.getFileName());
    int ep_err = ir.getErrors().size();
    int ep_war = ir.getWarnings().size();
    if (ep_err > 0) {
      htmlBody = htmlBody.replaceAll("##EP_OK##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR##", "block");
      htmlBody = htmlBody.replaceAll("##EP_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR-WAR##", "block");
    } else if (ep_war > 0) {
      htmlBody = htmlBody.replaceAll("##EP_OK##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_WAR##", "block");
      htmlBody = htmlBody.replaceAll("##EP_ERR-WAR##", "block");
    } else {
      htmlBody = htmlBody.replaceAll("##EP_OK##", "block");
      htmlBody = htmlBody.replaceAll("##EP_ERR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_WAR##", "none");
      htmlBody = htmlBody.replaceAll("##EP_ERR-WAR##", "none");
    }
    
    //Errors
    String clas = "success";
    if (ep_err > 0) {
      clas = "error";
    }
    htmlBody = htmlBody.replaceAll("##U_EP_ERR_N##", "" + ep_err);
    htmlBody = htmlBody.replaceAll("##U_EP_ERR_CLASS##", clas);
    
    //Warnings
    clas = "success";
    if (ep_war > 0) {
      clas = "warning";
    }
    htmlBody = htmlBody.replaceAll("##U_EP_WAR##", "" + ep_war);
    htmlBody = htmlBody.replaceAll("##U_EP_WAR_CLASS##", clas);
    
    
    
    //TO-DO, actually never fix nothing || no policy checker
    htmlBody = htmlBody.replaceAll("##U_PC_CLASS##", "success");
    htmlBody = htmlBody.replaceAll("##U_PCR##", "" + 0);
    htmlBody = htmlBody.replaceAll("##CP_OK##", "none");
    htmlBody = htmlBody.replaceAll("##CP_ERR##", "none");
    htmlBody = htmlBody.replaceAll("##F_EP_ERR_CLASS##", "info");
    htmlBody = htmlBody.replaceAll("##F_EP_WAR_CLASS##", "info");
    htmlBody = htmlBody.replaceAll("##F_PC_CLASS##", "info");
    htmlBody = htmlBody.replaceAll("##F_EP_ERR##", "0");
    htmlBody = htmlBody.replaceAll("##F_EP_WAR##", "0");
    htmlBody = htmlBody.replaceAll("##F_PC##", "0");
    //End TO-DO
   
    //Full Description
    //Errors and warnings
    String row;
    String rows = "";
    for (ValidationEvent val : ir.getErrors()) {
      row = "<tr><td class=\"bold error\">Error</td><td>##TEXT##</td></tr>";
      row = row.replace("##TEXT##", val.getDescription());
      rows += row;
    }
    
    for (ValidationEvent val : ir.getWarnings()) {
      row = "<tr><td class=\"bold warning\">Warning</td><td>##TEXT##</td></tr>";
      row = row.replace("##TEXT##", val.getDescription());
      rows += row;
    }
    htmlBody = htmlBody.replaceAll("##ROWS_EP##", rows);

    //Taggs list
    rows = "";
    Metadata meta = ir.getTiffModel().getMetadata();
    for (String key : meta.keySet()) {
      row = "<tr><td>##KEY##</td><td>##VALUE##</td></tr>";
      row = row.replace("##KEY##", key);
      row = row.replace("##VALUE##", meta.get(key).toString());
      rows += row;
    }
    htmlBody = htmlBody.replaceAll("##ROWS_TAGS##", rows);
    
    //File Structure
    String ul = "<ul>";
    TiffDocument td = ir.getTiffModel();
    for (TiffObject object : td.getIfds()) {
      IFD ifd = (IFD) object;
      ul += "<li><i class=\"fa fa-file-o\"></i> " + ifd.toString();
      if (ifd.getsubIFD() != null) {
        ul += "<ul><li><i class=\"fa fa-file-o\"></i> " + ifd.getsubIFD().toString() + "</li></ul>";
      }
      ul += "</li>";
    }
    ul += "</ul>";
    htmlBody = htmlBody.replaceAll("##UL##", ul);
    
    //Finish, write to html file
    htmlBody = htmlBody.replaceAll("\\.\\./", "");
    ReportHtml.writeToFile(outputfile, htmlBody);
  }
  
  private static int calculatePercent(IndividualReport ir) {
    Double rest = 100.0 - ir.getErrors().size() * 12.5;
    if (rest < 0.0){
      rest = 0.0;
    }
    return rest.intValue();
  }
  
  private static String generateCssColor(int index, IndividualReport ir, String path){
    String css = readFile(path);
    css = css.replace("##INDEX##" , "" + index); 
    if (ir.getErrors().size() > 0) {
      css = css.replace("##FIRST##" , "#CCCCCC");   //Light Grey
      css = css.replace("##SECOND##" , "red");
    } else {
      css = css.replace("##FIRST##" , "#66CC66");   //Green
      css = css.replace("##SECOND##" , "#66CC66");  //Green
    }
    return css;
  }
  
  private static String generateCssRotation(String path, int start, int val){
    String css = readFile(path);
    css = css.replaceAll("##START##", "" + start);
    css = css.replaceAll("##VAL##", "" + val);
    return css;
  }
  
  private static String replacesChart(String body, int angle, int rAngle){
    body = body.replace("##VAL1##", "" + angle);
    body = body.replace("##START2##", "" + angle);
    body = body.replace("##VAL2##", "" + rAngle);
    if (angle > 180) {
      body = body.replace("##BIG1##", "big");
      body = body.replace("##BIG2##", "");
    } else {
      body = body.replace("##BIG1##", "");
      body = body.replace("##BIG2##", "big");
    }
    return body;
  }
  
  /**
   * Parse a global report to XML format.
   *
   * @param xmlfile the file name.
   * @param ir the individual report.
   */
  public static void parseGlobal(String outputfile, GlobalReport gr) {
    String tPath = "resources/templates/global.html";
    String iPath = "resources/templates/image.html";
    String pcPath = "resources/templates/pie-color.css";
    String prPath = "resources/templates/pie-rotation.css";
    String htmlFolder = "resources/html/";
    copyFolder(htmlFolder,outputfile);
    
    
    String imagesBody = "";
    // Parse individual Reports
    int index = 0;
    for (IndividualReport ir : gr.getIndividualReports()) {
      String imageBody = readFile(iPath);
      //Basic
      int percent = calculatePercent(ir);
      imageBody = imageBody.replace("##PERCENT##", "" + percent);
      imageBody = imageBody.replace("##INDEX##" , "" + index); 
      imageBody = imageBody.replace("##IMG_NAME##", "" + ir.getFileName());
      imageBody = imageBody.replace("##ERR_N##", "" + ir.getErrors().size());
      imageBody = imageBody.replace("##WAR_N##", "" + ir.getWarnings().size());
      imageBody = imageBody.replace("##HREF##", ir.getFileName() + ".html");
      if (ir.getErrors().size() > 0) {
        imageBody = imageBody.replace("##ERR_C##", "error");
      } else {
        imageBody = imageBody.replace("##ERR_C##", "success");
      }
      if (ir.getWarnings().size() > 0) {
        imageBody = imageBody.replace("##WAR_C##", "warning");
      } else {
        imageBody = imageBody.replace("##WAR_C##", "success");
      }
      
      //Percent Info
      if (percent == 100) {
        imageBody = imageBody.replace("##CLASS##", "success");
        imageBody = imageBody.replace("##RESULT##", "Passed");
      } else {
        imageBody = imageBody.replace("##CLASS##", "error");
        imageBody = imageBody.replace("##RESULT##", "Failed");
      }
      if (ir.getWarnings().size() > 0 ) {
        imageBody = imageBody.replace("##DISPLAY_WAR##", "inline-block");
      } else {
        imageBody = imageBody.replace("##DISPLAY_WAR##", "none");
      }
      
      //Percent Chart
      int angle = percent * 360 / 100;
      int rAngle = 360 - angle;
      imageBody = replacesChart(imageBody, angle, rAngle);
      String css = generateCssColor(index, ir, pcPath);
      css += generateCssRotation(prPath, 0, angle);
      css += generateCssRotation(prPath, angle, rAngle);
      imageBody = imageBody.replace("##CSS##",css);
      
      //TO-DO
      imageBody = imageBody.replace("##CP_N##", "0");
      imageBody = imageBody.replace("##CP_C##", "success");
      //END TO-DO
      imagesBody += imageBody;
      index++;
    }
    
    //Parse the sumary report
    //numbers
    String htmlBody = readFile(tPath);
    Double doub = 1.0 * gr.getReportsOk() / gr.getReportsCount() * 100.0;
    int globalPercent = doub.intValue();
    htmlBody = htmlBody.replace("##PERCENT##", "" + globalPercent);
    htmlBody = htmlBody.replace("##IMAGES_LIST##", imagesBody);
    htmlBody = htmlBody.replace("##COUNT##", "" + gr.getReportsCount());
    htmlBody = htmlBody.replaceAll("##OK##", "" + gr.getReportsOk());
    htmlBody = htmlBody.replace("##KO##", "" + gr.getReportsKo());
    if (gr.getReportsOk() >= gr.getReportsKo()) {
      htmlBody = htmlBody.replace("##OK_C##", "success");
      htmlBody = htmlBody.replace("##KO_C##", "info-white");
    } else {
      htmlBody = htmlBody.replace("##OK_C##", "info-white");
      htmlBody = htmlBody.replace("##KO_C##", "error");
    }
    //Chart
    String cssG = readFile(pcPath);
    cssG = cssG.replace("##INDEX##" , "global"); 
    if (gr.getReportsOk() >= gr.getReportsKo()) {
      cssG = cssG.replace("##FIRST##" , "#F2F2F2");     //Draker White
      cssG = cssG.replace("##SECOND##" , "#66CC66");    //Green
    } else {
      cssG = cssG.replace("##FIRST##" , "red");         //Red
      cssG = cssG.replace("##SECOND##" , "#F2F2F2");    //Draker White
    }
    int angleG = globalPercent * 360 / 100;
    int rAngleG = 360 - angleG;
    cssG += " " + generateCssRotation(prPath, 0, rAngleG) + " " + generateCssRotation(prPath, rAngleG, angleG);
    htmlBody = htmlBody.replaceAll("##ANGLE##", "" + angleG);
    htmlBody = htmlBody.replaceAll("##RANGLE##", "" + rAngleG);
    htmlBody = htmlBody.replace("##CSS##", "" + cssG);
    if (rAngleG > 180) {
      htmlBody = htmlBody.replaceAll("##BIG1##", "big");
      htmlBody = htmlBody.replaceAll("##BIG2##", "");
    } else {
      htmlBody = htmlBody.replaceAll("##BIG1##", "");
      htmlBody = htmlBody.replaceAll("##BIG2##", "big");
    }
    
    //TO-DO
    htmlBody = htmlBody.replace("##OK_PC##", "0");
    htmlBody = htmlBody.replace("##OK_EP##", "0");
    //END TO-DO
    
    htmlBody = htmlBody.replaceAll("\\.\\./", "");
    writeToFile(outputfile,htmlBody);
  }
  
  /**
   * Parse a global report to XML format.
   *
   * @param xmlfile the file name.
   * @param ir the individual report.
   */
  public static void writeToFile(String outputfile, String body) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(outputfile, "UTF-8");
      writer.println(body);
    } catch (FileNotFoundException e) {
      System.out.println("File not found exception");
    } catch (UnsupportedEncodingException e) {
      System.out.println("UnsupportedEncodingException exception");
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
}
