package dpfmanager;

import dpfmanager.shell.modules.Rules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by easy on 06/10/2015.
 */
public class Configuration {
  private ArrayList<String> isos;
  private Rules rules;
  private ArrayList<String> formats;
  private ArrayList<String> fixes;

  public ArrayList<String> getIsos() {
    return isos;
  }

  public ArrayList<String> getFormats() {
    return formats;
  }

  public ArrayList<String> getFixes() {
    return fixes;
  }

  public Rules getRules() {
    return rules;
  }

  public Configuration() {
    isos = new ArrayList<String>();
    rules = new Rules();
    formats = new ArrayList<String>();
    fixes = new ArrayList<String>();
  }

  public void SaveFile(String filename) throws Exception {
    PrintWriter writer = new PrintWriter(filename, "UTF-8");
    for (String iso : isos) {
      writer.println("ISO\t" + iso);
    }
    for (String format : formats) {
      writer.println("FORMAT\t" + format);
    }
    for (String fix : fixes) {
      writer.println("FIX\t" + fix);
    }
    rules.Write(writer);
    writer.close();
  }

  public void ReadFile(String filename) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    try {
      String line = br.readLine();

      while (line != null) {
        if (line.contains("\t")) {
          String field1 = line.substring(0, line.indexOf("\t"));
          String field2 = line.substring(line.indexOf("\t") + 1);
          switch (field1) {
            case "ISO": isos.add(field2); break;
            case "FORMAT": formats.add(field2); break;
            case "FIX": fixes.add(field2); break;
            case "RULE": rules.addRuleFromTxt(field2); break;
          }
        }
        line = br.readLine();
      }
    } finally {
      br.close();
    }
  }
}
