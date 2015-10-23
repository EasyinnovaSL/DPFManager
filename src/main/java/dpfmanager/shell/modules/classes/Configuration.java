package dpfmanager.shell.modules.classes;

import dpfmanager.shell.modules.classes.Fixes;
import dpfmanager.shell.modules.classes.Rules;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by easy on 06/10/2015.
 */
public class Configuration {
  private ArrayList<String> isos;
  private Rules rules;
  private ArrayList<String> formats;
  private Fixes fixes;

  public ArrayList<String> getIsos() {
    return isos;
  }

  public ArrayList<String> getFormats() {
    return formats;
  }

  public Fixes getFixes() {
    return fixes;
  }

  public Rules getRules() {
    return rules;
  }

  public Configuration() {
    isos = new ArrayList<String>();
    rules = new Rules();
    formats = new ArrayList<String>();
    fixes = new Fixes();
  }

  public void SaveFile(String filename) throws Exception {
    PrintWriter writer = new PrintWriter(filename, "UTF-8");
    for (String iso : isos) {
      writer.println("ISO\t" + iso);
    }
    for (String format : formats) {
      writer.println("FORMAT\t" + format);
    }
    rules.Write(writer);
    fixes.Write(writer);
    writer.close();
  }

  private InputStream getInputStream(String filename) throws Exception {
    InputStream sc = null;
    if (Files.exists(Paths.get(filename))) {
      // Look in local dir
      sc = new FileInputStream(filename);
    } else {
      // Look in JAR
      CodeSource src = Configuration.class.getProtectionDomain().getCodeSource();
      if (src != null) {
        URL jar = src.getLocation();
        ZipInputStream zip = new ZipInputStream(jar.openStream());
        ZipEntry zipFile;
        while ((zipFile = zip.getNextEntry()) != null) {
          String name = zipFile.getName();
          if (name.contains(filename)) {
            System.out.println("Found in JAR");
            try {
              sc = zip;
            } catch (Exception ex) {
              throw new Exception("");
            }
          }
        }
      } else {
        throw new Exception("");
      }
    }
    return sc;
  }

  public void ReadFile(String filename) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream(filename)));
    try {
      String line = br.readLine();

      while (line != null) {
        if (line.contains("\t")) {
          String field1 = line.substring(0, line.indexOf("\t"));
          String field2 = line.substring(line.indexOf("\t") + 1);
          switch (field1) {
            case "ISO": isos.add(field2); break;
            case "FORMAT": formats.add(field2); break;
            case "FIX": fixes.addFixFromTxt(field2); break;
            case "RULE": rules.addRuleFromTxt(field2); break;
          }
        }
        line = br.readLine();
      }
    } finally {
      br.close();
    }
  }

  public String getTxtRules() {
    String txt = "";
    for (int i=0;i<rules.getRules().size();i++) {
      Rule rule = rules.getRules().get(i);
      String val = "";
      if (rule.getValue() != null) val = rule.getValue();
      txt += (rule.getTag() + " " + rule.getOperator() + " " + val).trim();
      if (i+1 < rules.getRules().size()) txt += ", ";
    }
    return txt;
  }

  public String getTxtFixes() {
    String txt = "";
    for (int i=0;i<fixes.getFixes().size();i++) {
      Fix fix = fixes.getFixes().get(i);
      String val = "";
      if (fix.getValue() != null) val = "'" + fix.getValue() + "'";
      txt += (fix.getOperator() + " " + fix.getTag() + " " + val).trim();
      if (i+1 < fixes.getFixes().size()) txt += ", ";
    }
    return txt;
  }

  public String getTxtFormats() {
    String txt = "";
    for (int i=0;i<formats.size();i++) {
      txt += formats.get(i);
      if (i+1 < formats.size()) txt += ", ";
    }
    return txt;
  }

  public String getTxtIsos() {
    String txt = "";
    for (int i=0;i<isos.size();i++) {
      txt += isos.get(i);
      if (i+1 < isos.size()) txt += ", ";
    }
    return txt;
  }
}
