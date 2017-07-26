package dpfmanager.implementationchecker;

import static java.io.File.separator;

import dpfmanager.conformancechecker.configuration.Configuration;

import com.easyinnova.implementation_checker.TiffImplementationChecker;
import com.easyinnova.implementation_checker.ValidationResult;
import com.easyinnova.implementation_checker.Validator;
import com.easyinnova.implementation_checker.model.TiffValidationObject;
import com.easyinnova.implementation_checker.rules.RuleResult;
import com.easyinnova.policy_checker.PolicyChecker;
import com.easyinnova.policy_checker.model.Rule;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.reader.TiffReader;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Victor Munoz on 24/07/2017.
 */
public class RunsTest extends TestCase {
  String policyFile;

  HashSet<Integer> classify(String filename, boolean policy_check, boolean tia, boolean print) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);
      assertEquals(0, result);

      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      String content = tiffValidation.getXml();
      Validator v = new Validator();
      if (!tia) v.validate(content, "implementationcheckers/TIFF_Baseline_Extended_6_0.xml", false);
      else v.validate(content, "implementationcheckers/TIAProfileChecker.xml", false);

      ValidationResult policyResult = null;
      if (policy_check) {
        Configuration config = new Configuration();
        config.ReadFile(policyFile);
        PolicyChecker policy = new PolicyChecker();
        policyResult = policy.validateRules(content, config.getRules());
      }

      HashSet<Integer> classes = new HashSet<Integer>();

      List<RuleResult> errors = v.getErrors();
      List<RuleResult> warnings = v.getWarnings();
      if (errors.size() + warnings.size() == 0) classes.add(0);

      List<RuleResult> fullList = new ArrayList<RuleResult>();
      fullList.addAll(errors);
      fullList.addAll(warnings);
      for (RuleResult rr : fullList) {
        if (rr.toString().contains("TIFF signature is not correct")) classes.add(1);
        if (rr.toString().contains("cardinality")) classes.add(2);
        if (rr.toString().contains("readable but not valid")) classes.add(3);
        if (rr.toString().contains("Tag type is not valid")) classes.add(4);
        if (rr.toString().contains("Incorrect Extra Samples Count") || rr.toString().contains("BitsPerSample must have a cardinality according to BitsPerSample")) classes.add(5);
        if (rr.toString().contains("Image width and image height do not match with strips sizes")) classes.add(6);
        if (rr.toString().contains("must have tag X Resolution") || rr.toString().contains("must have tag Y Resolution") || rr.toString().contains("Resolution tag must be greater than zero")) classes.add(7);
        if (rr.toString().contains("must have a Photometric Interpretation tag") || rr.toString().contains("must have a Width tag") || rr.toString().contains("must have a Length tag") || rr.toString().contains("must have a XResolution tag") || rr.toString().contains("must have a YResolution tag")) classes.add(8);
        if (rr.toString().contains("Incorrect page number")) classes.add(9);
        if (rr.toString().contains("type is expected")) classes.add(10);
        if (rr.toString().contains("TIFF signature must be 42")) classes.add(11);
        if (rr.toString().contains("Byte Order")) classes.add(12);
        if (rr.toString().contains("word alignment")) classes.add(13);
        if (rr.toString().contains("ad offset")) classes.add(14);
        if (rr.toString().contains("must have at least one entry")) classes.add(15);
        if (rr.toString().contains("must have at least one Image File Directory")) classes.add(16);
        if (rr.toString().contains("strict ascending order")) classes.add(17);
        if (rr.toString().contains("Photometric Interpretation value must be between")) classes.add(18);
        if (rr.toString().contains("Strips offsets are invalid") || rr.toString().contains("Inconsistent strip lengths")) classes.add(19);
        if (rr.toString().contains("must have the tag Strip")) classes.add(20);
        if (rr.toString().contains("Image width and image height do not match with strips sizes")) classes.add(21);
        if (rr.toString().contains("Tiles offsets are invalid") || rr.toString().contains("Inconsistent tile lengths")) classes.add(22);
        if (rr.toString().contains("must have the tag Tile")) classes.add(23);
        if (rr.toString().contains("must have the tag Strip")) classes.add(24);
        if (rr.toString().contains("Image width and image height do not match with tiles sizes")) classes.add(25);
        if (rr.toString().contains("Incorrect tags for Bilevel images")) classes.add(26);
        if (rr.toString().contains("Incorrect tags for Grayscale images")) classes.add(27);
        if (rr.toString().contains("Color Map must be included in palette color images")) classes.add(28);
        if (rr.toString().contains("for Transparency Mask image")) classes.add(29);
        if (rr.toString().contains("Incorrect tags for CMYK")) classes.add(30);
        if (rr.toString().contains("Incorrect tags for YCbCr")) classes.add(31);
        if (rr.toString().contains("Incorrect tags for CIELab")) classes.add(32);
        if (rr.toString().contains("Incorrect tags for RGB")) classes.add(33);
        if (rr.toString().contains("Only 7-bit ASCII codes are accepted")) classes.add(34);
        if (rr.toString().contains("DateTime cardinality is not valid") || rr.toString().contains("Incorrect format for DateTime")) classes.add(35);
        if (rr.toString().contains("More than 10 private tags")) classes.add(36);
        if (rr.toString().contains("Incorrect Tiff EP")) classes.add(37);
        if (rr.toString().contains("Incorrect Tiff EP with tag")) classes.add(38);
        if (rr.toString().contains("Incorrect Tiff IT")) classes.add(39);
        if (rr.toString().contains("Invalid Compression")) classes.add(40);
        if (rr.toString().contains("is not recommended regarding the TI/A specifications")) classes.add(41);
        if (rr.toString().contains("is mandatory regarding the TI/A specifications")) classes.add(42);
        if (print) {
          System.out.println(rr.toString());
        }
      }
      if (policyResult != null) {
        for (RuleResult rr : policyResult.getErrors()) {
          classes.add(43);
        }
      }

      return classes;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  String printClasses(HashSet<Integer> classes) {
    String s = "";
    for (Integer iClass : classes) {
      if (s.length() > 0) s += ", ";
      s += iClass;
    }
    return s;
  }

  void ClassifyAndScore(File dir, int policyClass, HashSet<Integer> tiaClasses, boolean onlyFinalResult) {
    int nCorrect = 0;
    int nIncorrect = 0;
    if (dir.exists()) {
      for (File dirClass : dir.listFiles()) {
        if (dirClass.isDirectory()) {
          String sClass = dirClass.getName();
          int iClass = Integer.parseInt(sClass.substring(2));
          for (File fileClass : dirClass.listFiles()) {
            boolean policy = policyClass == iClass;
            boolean tia = tiaClasses.contains(iClass);
            HashSet<Integer> classifiedClass = classify(fileClass.getPath(), policy, tia, false);
            assertEquals(classifiedClass != null, true);
            if (!classifiedClass.contains(iClass)) {
              if (!onlyFinalResult) {
                System.out.println("File of class " + sClass + " bad classified as " + printClasses(classifiedClass) + " (" + fileClass.getName() + ")");
              }
              classifiedClass = classify(fileClass.getPath(), policy, tia, true);
              nIncorrect++;
            } else {
              nCorrect++;
            }
            //assertEquals(classifiedClass.contains(iClass), true);
          }
        }
      }
    }
    System.out.println("Correctly classified files: " + nCorrect);
    System.out.println("Incorrectly classified files: " + nIncorrect);
    System.out.println("Score: " + nCorrect*100/(nCorrect+nIncorrect) + "%");
  }

  void CreatePolicy() {
    try {
      if (!new File("temp").exists()) {
        new File("temp").mkdir();
      }

      String path = "temp/output";
      int idx = 1;
      while (new File(path).exists()) path = "temp/output" + idx++;

      policyFile = "temp/xx.cfg";
      idx = 0;
      while (new File(policyFile).exists()) policyFile = "temp/xx" + idx++ + ".cfg";

      PrintWriter bw = new PrintWriter(policyFile);
      bw.write("ISO\tBaseline\n" +
          "FORMAT\tHTML\n" +
          "FORMAT\tXML\n" +
          "RULE\tCompression,=,None\n" +
          "RULE\tByteOrder,=,LITTLE_ENDIAN\n" +
          "RULE\tPhotometric,=,RGB\n");
      bw.close();
    } catch (Exception ex) {
      ex.printStackTrace();
      assertEquals(1, 0);
    }
  }

  void DeletePolicy() {
    try {
      FileUtils.deleteDirectory(new File("temp"));
    } catch (Exception ex) {
      ex.printStackTrace();
      assertEquals(0,1);
    }
  }

  public void testClassify() {
    boolean onlyFinalResult = false;
    int policyClass = 43;
    HashSet<Integer> tiaClasses = new HashSet<Integer>();
    tiaClasses.add(41);
    tiaClasses.add(42);
    CreatePolicy();
    ClassifyAndScore(new File("Z:\\PROJECTES\\Projectes en desenvolupament\\201411 - PREFORMA\\TIFFs\\classes\\training"), policyClass, tiaClasses, onlyFinalResult);
    ClassifyAndScore(new File("Z:\\PROJECTES\\Projectes en desenvolupament\\201411 - PREFORMA\\TIFFs\\classes\\test"), policyClass, tiaClasses, onlyFinalResult);
    DeletePolicy();
  }
}
