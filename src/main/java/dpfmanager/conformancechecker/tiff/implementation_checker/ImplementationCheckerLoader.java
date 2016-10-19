package dpfmanager.conformancechecker.tiff.implementation_checker;

import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.ImplementationCheckerObjectType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.IncludeType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RuleType;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.model.RulesType;
import dpfmanager.shell.modules.report.core.ReportGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Created by Adrià Llorens on 19/10/2016.
 */
public class ImplementationCheckerLoader {

  static HashMap<String, ImplementationCheckerObjectType> preLoadedValidatorsSingleton = new HashMap<>();

  public synchronized static ImplementationCheckerObjectType getRules(String rulesFile) {
    ImplementationCheckerObjectType rules = null;
    try {
      if (!rulesFile.contains("/")) {
        rulesFile = "implementationcheckers/" + rulesFile + ".xml";
      }
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
              //for (String id : inc.getExclude().getRule()) {
              //  if (id.equals(ro.getDescription())) excludedRules = true;
              //}
              if (!excludedRules) {
                rules.getRules().add(ro);
              }
              for (RuleType rule : ro.getRule()) {
                //rule.iso = rulesIncluded.getIso();
              }
            }

          /*if (inc.getSubsection() == null || inc.getSubsection().length() == 0) {
            for (RulesType ro : rulesIncluded.getRules()) {
              rules.getRules().add((ImplementationCheckerObjectType.Rules)ro);
              for (RuleType rule : ro.getRule()) {
                rule.setIso(rulesIncluded.getIso());
              }
            }
          } else {
            for (RulesType ro : rulesIncluded.getRules()) {
              if (ro.getDescription().equals(inc.getSubsection())) {
                rules.getRules().add((ImplementationCheckerObjectType.Rules)ro);
                for (RuleType rule : ro.getRule()) {
                  rule.setIso(rulesIncluded.getIso());
                }
              }
            }
          }*/
          }
        }

        preLoadedValidatorsSingleton.put(rulesFile, rules);
      } else {
        rules = preLoadedValidatorsSingleton.get(rulesFile);
      }
    } catch (JAXBException ex){

    }
    return rules;
  }

  private static InputStream getFileFromResources(String pathStr) {
    InputStream fis = null;
    Path path = Paths.get(pathStr);
    try {
      if (Files.exists(path)) {
        // Look in current dir
        fis = new FileInputStream(pathStr);
      } else {
        // Look in JAR
        Class cls = ReportGenerator.class;
        ClassLoader cLoader = cls.getClassLoader();
        fis = cLoader.getResourceAsStream(pathStr);
      }
    } catch (FileNotFoundException e) {
    }
    return fis;
  }

  public static List<String> getPathsList(){
    List<String> list = new ArrayList<>();
    list.add("implementationcheckers/BaselineProfileChecker.xml");
    list.add("implementationcheckers/TiffEPProfileChecker.xml");
    list.add("implementationcheckers/TiffITProfileChecker.xml");
    list.add("implementationcheckers/TiffITP1ProfileChecker.xml");
    list.add("implementationcheckers/TiffITP2ProfileChecker.xml");
    return list;
  }

  public static String getName(String path){
    return path.substring(path.indexOf("/")+1, path.indexOf("."));
  }
}
