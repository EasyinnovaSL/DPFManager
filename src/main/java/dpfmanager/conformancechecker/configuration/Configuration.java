package dpfmanager.conformancechecker.configuration;

import dpfmanager.conformancechecker.tiff.policy_checker.Rule;
import dpfmanager.conformancechecker.tiff.metadata_fixer.Fix;
import dpfmanager.conformancechecker.tiff.metadata_fixer.Fixes;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;

import java.io.BufferedReader;
import java.io.FileInputStream;
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
  private String output = null;

  /**
   * Gets isos.
   *
   * @return the isos
   */
  public ArrayList<String> getIsos() {
    return isos;
  }

  public void addISO(String iso) {
    if (!isos.contains(iso)) {
      isos.add(iso);
    }
  }

  public void addFormat(String format) {
    if (!formats.contains(format)) {
      formats.add(format);
    }
  }

  /**
   * Gets formats.
   *
   * @return the formats
   */
  public ArrayList<String> getFormats() {
    return formats;
  }

  /**
   * Gets fixes.
   *
   * @return the fixes
   */
  public Fixes getFixes() {
    return fixes;
  }

  /**
   * Gets rules.
   *
   * @return the rules
   */
  public Rules getRules() {
    return rules;
  }

  /**
   * Instantiates a new Configuration.
   */
  public Configuration() {
    isos = new ArrayList<>();
    rules = new Rules();
    formats = new ArrayList<>();
    fixes = new Fixes();
  }

  /**
   * Instantiates a new Configuration with params
   */
  public Configuration(Rules rules, Fixes fixes, ArrayList<String> formats) {
    isos = new ArrayList<>();
    this.rules = rules;
    this.formats = formats;
    this.fixes = fixes;
  }

  /**
   * Set the default values for a new configuration
   */
  public void initDefault(){
    addISO("Baseline");
  }

  /**
   * Sets output.
   *
   * @param path the path
   */
  public void setOutput(String path) {
    output = path;
  }

  /**
   * Gets output.
   * Null means default.
   *
   * @return the output
   */
  public String getOutput() {
    return output;
  }

  /**
   * Save file.
   *
   * @param filename the filename
   * @throws Exception the exception
   */
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
    if (output != null) {
      writer.println("OUTPUT\t" + output);
    }
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

  /**
   * Read file.
   *
   * @param filename the filename
   * @throws Exception the exception
   */
  public void ReadFile(String filename) throws Exception {
    output = null;
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
            case "OUTPUT": output = field2; break;
          }
        }
        line = br.readLine();
      }
    } finally {
      br.close();
    }
  }

  /**
   * Gets txt rules.
   *
   * @return the txt rules
   */
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

  /**
   * Gets txt fixes.
   *
   * @return the txt fixes
   */
  public String getTxtFixes() {
    String txt = "";
    for (int i=0;i<fixes.getFixes().size();i++) {
      Fix fix = fixes.getFixes().get(i);
      String val = "";
      String op = fix.getOperator();
      if (op == null) op = "";
      if (fix.getValue() != null && !fix.getValue().isEmpty()) val = "'" + fix.getValue() + "'";
      txt += (op + " " + fix.getTag() + " " + val).trim();
      if (i+1 < fixes.getFixes().size()) txt += ", ";
    }
    return txt;
  }

  /**
   * Gets txt formats.
   *
   * @return the txt formats
   */
  public String getTxtFormats() {
    String txt = "";
    for (int i=0;i<formats.size();i++) {
      txt += formats.get(i);
      if (i+1 < formats.size()) txt += ", ";
    }
    return txt;
  }

  /**
   * Gets txt isos.
   *
   * @return the txt isos
   */
  public String getTxtIsos() {
    String txt = "";
    for (int i=0;i<isos.size();i++) {
      txt += isos.get(i);
      if (i+1 < isos.size()) txt += ", ";
    }
    return txt;
  }
}
