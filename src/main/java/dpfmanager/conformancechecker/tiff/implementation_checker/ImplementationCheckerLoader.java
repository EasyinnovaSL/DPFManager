package dpfmanager.conformancechecker.tiff.implementation_checker;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.IncludeType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RulesType;
import dpfmanager.shell.core.DPFManagerProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Created by Adrià Llorens on 19/10/2016.
 */
public class ImplementationCheckerLoader {

  static HashMap<String, ImplementationCheckerObjectType> preLoadedValidatorsSingleton = new HashMap<>();

  private static List<String> isoNames;
  private static List<String> isoPaths;

  public static String getDefaultIso() {
    return "BaselineProfileChecker";
  }

  public synchronized static ImplementationCheckerObjectType getRules(String rulesFile) {
    if (rulesFile.equals(TiffConformanceChecker.POLICY_ISO)){
      return null;
    }
    if (!rulesFile.contains("/") && !rulesFile.contains(".")) {
      rulesFile = "implementationcheckers/" + rulesFile + ".xml";
    }

    ImplementationCheckerObjectType rules = null;
    try {
      if (!preLoadedValidatorsSingleton.containsKey(rulesFile)) {
        JAXBContext jaxbContext = JAXBContext.newInstance(ImplementationCheckerObjectType.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        rules = (ImplementationCheckerObjectType) jaxbUnmarshaller.unmarshal(getFileFromResources(rulesFile));

        for (RulesType ro : rules.getRules()) {
          for (RuleType rule : ro.getRule()) {
            //rule.iso = rules.getIso();
          }
        }

        if (rules.getInclude() != null) {
          for (IncludeType inc : rules.getInclude()) {
            JAXBContext jaxbContextInc = JAXBContext.newInstance(ImplementationCheckerObjectType.class);
            Unmarshaller jaxbUnmarshallerInc = jaxbContextInc.createUnmarshaller();
            ImplementationCheckerObjectType rulesIncluded = (ImplementationCheckerObjectType) jaxbUnmarshallerInc.unmarshal(getFileFromResources("implementationcheckers/" + inc.getPolicyChecker()));

            for (RulesType ro : rulesIncluded.getRules()) {
              boolean excludedRules = false;
              HashSet<String> excluded = new HashSet<>();
              for (String id : inc.getExcluderules()) {
                if (id.equals(ro.getId())) excludedRules = true;
                excluded.add(id);
              }
              if (!excludedRules) {
                ro.setIncluded(true);
                rules.getRules().add(ro);
                for (int i=0;i<ro.getRule().size();i++) {
                  if (excluded.contains(ro.getRule().get(i).getId())) {
                    ro.getRule().remove(i--);
                  }
                }
                for (RuleType rule : ro.getRule()) {
                  //rule.iso = rulesIncluded.getIso();
                }
              }
            }
          }
        }

        preLoadedValidatorsSingleton.put(rulesFile, rules);
      } else {
        rules = preLoadedValidatorsSingleton.get(rulesFile);
      }
    } catch (JAXBException ex) {
      return null;
    }
    return rules;
  }

  private static InputStream getFileFromResources(String pathStr) {
    InputStream fis = null;
    File file = new File(pathStr);
    File fileConfig = new File(DPFManagerProperties.getIsosDir() + "/" + pathStr);
    try {
      if (file.exists()) {
        // Look in current dir
        fis = new FileInputStream(pathStr);
      } else if (fileConfig.exists()) {
        // Look in isos config
        fis = new FileInputStream(DPFManagerProperties.getIsosDir() + "/" + pathStr);
      } else {
        // Look in JAR
        Class cls = ImplementationCheckerLoader.class;
        ClassLoader cLoader = cls.getClassLoader();
        fis = cLoader.getResourceAsStream(pathStr);
      }
    } catch (FileNotFoundException e) {
    }
    return fis;
  }

  public static List<String> getPathsList() {
    if (isoPaths == null) {
      List<String> list = new ArrayList<>();
      String path = "implementationcheckers";
      try {
        Class cls = ImplementationCheckerLoader.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String resource;
        while ((resource = br.readLine()) != null) {
          if (resource.endsWith(".xml")) {
            list.add(path + "/" + resource);
          }
        }
      } catch (Exception e) {

      }
      isoPaths = list;
    }
    return isoPaths;
  }

  public static List<String> getNamesList() {
    if (isoNames == null) {
      List<String> list = new ArrayList<>();
      String path = "implementationcheckers";
      try {
        Class cls = ImplementationCheckerLoader.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String resource;
        while ((resource = br.readLine()) != null) {
          if (resource.endsWith(".xml")) {
            list.add(resource.replace(".xml", ""));
          }
        }
      } catch (Exception e) {

      }
      isoNames = list;
    }
    return isoNames;
  }

  public static String getFileName(String path) {
    if (path.contains("/") && path.contains(".")) {
      return path.substring(path.indexOf("/") + 1, path.indexOf("."));
    }
    return path;
  }

  public static String getIsoName(String path) {
    if (path.equals(TiffConformanceChecker.POLICY_ISO)){
      return TiffConformanceChecker.POLICY_ISO;
    }
    ImplementationCheckerObjectType icRules = ImplementationCheckerLoader.getRules(path);
    if (icRules != null) {
      return icRules.getTitle();
    }
    return getFileName(path);
  }

  public static boolean isValid(String path) {
    ImplementationCheckerObjectType icRules = ImplementationCheckerLoader.getRules(path);
    return icRules != null;
  }

}
