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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Victor Munoz on 24/07/2017.
 */
public class RunsTest extends TestCase {
  String policyFile;

  HashSet<Integer> classify(String filename, boolean policy_check, boolean tia, boolean ep, boolean it, boolean print) {
    try {
      TiffReader tr = new TiffReader();
      int result = tr.readFile(filename);
      assertEquals(0, result);

      TiffDocument td = tr.getModel();
      TiffImplementationChecker tic = new TiffImplementationChecker();
      TiffValidationObject tiffValidation = tic.CreateValidationObject(td);
      String content = tiffValidation.getXml();
      Validator v = new Validator();
      if (tia) v.validate(content, "implementationcheckers/TIAProfileChecker.xml", false);
      else if (ep) v.validate(content, "implementationcheckers/TIFF_EP.xml", false);
      else if (it) v.validate(content, "implementationcheckers/TiffITProfileChecker.xml", false);
      else v.validate(content, "implementationcheckers/TIFF_Baseline_Extended_6_0.xml", false);

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
      boolean falta_tag_ep = false;
      for (RuleResult rr : fullList) {
        if (rr.toString().contains("TIFF signature is not correct")) classes.add(1);
        if (rr.toString().contains("cardinality")) classes.add(2);
        if (rr.toString().contains("but it is compatible")) classes.add(3);
        if (rr.toString().contains("type is not valid")) classes.add(4);
        if (rr.toString().contains("Incorrect Extra Samples Count") || rr.toString().contains("BitsPerSample must have a cardinality according to BitsPerSample")) classes.add(5);
        if (rr.toString().contains("Image width and image height do not match with strips sizes")) classes.add(6);
        if (rr.toString().contains("must have tag X Resolution") || rr.toString().contains("must have tag Y Resolution") || rr.toString().contains("Resolution tag must be greater than zero")) classes.add(7);
        if (rr.toString().contains("must have a Photometric Interpretation tag") || rr.toString().contains("must have a Width tag") || rr.toString().contains("must have a Length tag") || rr.toString().contains("must have a XResolution tag") || rr.toString().contains("must have a YResolution tag")) classes.add(8);
        if (rr.toString().contains("Incorrect page number") || rr.toString().contains("Incoherent page number") || rr.toString().contains("invalid total number of pages")) classes.add(9);
        if (rr.toString().contains("type is expected")) classes.add(10);
        if (rr.toString().contains("TIFF signature must be 42")) classes.add(11);
        if (rr.toString().contains("Byte Order")) classes.add(12);
        if (rr.toString().contains("word alignment")) classes.add(13);
        if (rr.toString().contains("ad offset") || rr.toString().contains("Duplicate pointer") || rr.toString().contains("Data already referenced")) classes.add(14);
        if (rr.toString().contains("must have at least one entry")) classes.add(15);
        if (rr.toString().contains("must have at least one Image File Directory")) classes.add(16);
        if (rr.toString().contains("strict ascending order")) classes.add(17);
        if (rr.toString().contains("Photometric Interpretation value must be between")) classes.add(18);
        if (rr.toString().contains("Strips offsets are invalid") || rr.toString().contains("Inconsistent strip lengths")) classes.add(19);
        if (rr.toString().contains("must have the tag Strip") || rr.toString().contains("RowsPerStrip is required")) classes.add(20);
        if (rr.toString().contains("Image width and image height do not match with strips sizes")) classes.add(21);
        if (rr.toString().contains("Tiles offsets are invalid") || rr.toString().contains("Inconsistent tile lengths")) classes.add(22);
        if (rr.toString().contains("must have the tag Tile") || rr.toString().contains("Missing required TileByteCounts tag for tiled images") || rr.toString().contains("Missing required TileOffsets tag for tiled images")) classes.add(23);
        if (rr.toString().contains("Invalid value for field TileWidth") || rr.toString().contains("Invalid value for field TileLength")) classes.add(24);
        if (rr.toString().contains("Image width and image height do not match with tiles sizes") || rr.toString().contains("TileWidth greater than the ImageWidth") || rr.toString().contains("TileLength bigger than the ImageLength") || rr.toString().contains("tiles are too large")) classes.add(25);
        if ((rr.toString().contains("should only be used in")) && getPhotometric(tr) == 0) classes.add(26); //bilevel
        if (rr.toString().contains("Invalid Compression for Bilevel image")) classes.add(26); //bilevel
        if (rr.toString().contains("should only be used in") && getPhotometric(tr) == 1) classes.add(27);
        if (rr.toString().contains("CCITT compression is defined only for bilevel") && getPhotometric(tr) == 1) classes.add(27);
        if ((rr.toString().contains("should only be used in") && getPhotometric(tr) == 3) || rr.toString().contains("Color Map must be included in palette color images")) classes.add(28); //palette
        if (rr.toString().contains("CCITT compression is defined only for bilevel") && getPhotometric(tr) == 2) classes.add(28);
        if ((rr.toString().contains("should only be used in") && getPhotometric(tr) == 4) ||
            rr.toString().contains("NewSubfileType defines a transparency mask while PhotometricInterpretation defines another image type") ||
            rr.toString().contains("Invalid Bits per Sample for Transparency Mask image") ||
            rr.toString().contains("Invalid Samples per Pixel for Transparency Mask image")) classes.add(29); //transparency mask
        if (rr.toString().contains("CCITT compression is defined only for bilevel") && getPhotometric(tr) == 4) classes.add(29);
        if (rr.toString().contains("should only be used in") && getPhotometric(tr) == 5) classes.add(30); //cmyk
        if (rr.toString().contains("CCITT compression is defined only for bilevel") && getPhotometric(tr) == 5) classes.add(30);
        if ((rr.toString().contains("should only be used in") && getPhotometric(tr) == 6) || rr.toString().contains("inappropriate for Class Y")) classes.add(31); //ycbcr
        if (rr.toString().contains("CCITT compression is defined only for bilevel") && getPhotometric(tr) == 6) classes.add(31);
        if (rr.toString().contains("should only be used in") && getPhotometric(tr) == 8) classes.add(32); //cielab
        if (rr.toString().contains("CCITT compression is defined only for bilevel") && getPhotometric(tr) == 8) classes.add(32);
        if (rr.toString().contains("should only be used in") && getPhotometric(tr) == 2) classes.add(33); //rgb
        if (rr.toString().contains("CCITT compression is defined only for bilevel") && getPhotometric(tr) == 2) classes.add(33);
        if (rr.toString().contains("Only 7-bit ASCII codes are accepted") || rr.toString().contains("ASCII strings must terminate with NUL")) classes.add(34);
        if (rr.toString().contains("DateTime cardinality is not valid") || rr.toString().contains("Incorrect format for DateTime")) classes.add(35);
        if (rr.toString().contains("More than 10 private tags")) classes.add(36);
        if (rr.toString().contains("TIFF/EPStandardID tag must be used")) falta_tag_ep = true;
        if (rr.toString().contains("Invalid Compression")) classes.add(40);
        if (rr.toString().contains("is not recommended regarding the TI/A specifications")) classes.add(41);
        if (rr.toString().contains("is mandatory regarding the TI/A specifications")) classes.add(42);
        if (print) {
          System.out.println(rr.toString());
        }
      }
      if (ep && errors.size() > 0) {
        classes.add(37);
      }
      if (ep && errors.size() > 0 && !falta_tag_ep) {
        classes.add(38);
      }
      if (it && errors.size() > 0) {
        classes.add(39);
      }
      if (policyResult != null) {
        for (RuleResult rr : policyResult.getErrors()) {
          classes.add(43);
        }
      }

      if (classes.contains(0) && classes.size() > 1) {
        classes.remove(0);
      }

      return classes;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  long getPhotometric(TiffReader tr) {
    try {
      long photo = tr.getModel().getFirstIFD().getTag("PhotometricInterpretation").getFirstNumericValue();
      if (photo == 0 || photo == 1)
      {
        if (tr.getModel().getFirstIFD().getTag("BitsPerSample").getFirstNumericValue() == 1) {
          photo = 0;
        } else {
          photo = 1;
        }
      }
      return photo;
    } catch (Exception ex) {

    }
    return -1;
  }

  String printClasses(HashSet<Integer> classes) {
    String s = "";
    for (Integer iClass : classes) {
      if (s.length() > 0) s += ", ";
      s += iClass;
    }
    return s;
  }

  String TresDigits(int i) {
    String s = i+"";
    while (s.length() < 3) s = "0" + s;
    return s;
  }

  void ClassifyAndScore(File dir, int policyClass, HashSet<Integer> tiaClasses, HashSet<Integer> epClasses, HashSet<Integer> itClasses, boolean onlyFinalResult) {
    int nCorrect = 0;
    int nIncorrect = 0;
    int nRun = 3;
    try {
      PrintWriter writer = new PrintWriter("result"+dir.getName() + ".txt", "UTF-8");
      PrintWriter writer2 = new PrintWriter("result-gt"+dir.getName() + ".txt", "UTF-8");
      List<String> lkeys = new ArrayList<String>();
      HashMap<String, HashSet<String>> dones = new HashMap<String, HashSet<String>>();
      if (dir.exists()) {
        for (File dirClass : dir.listFiles()) {
          if (dirClass.isDirectory()) {
            String sClass = dirClass.getName();
            int iClass = Integer.parseInt(sClass.substring(2));
            lkeys.add(TresDigits(iClass));
            for (File fileClass : dirClass.listFiles()) {
              if (fileClass.getName().toLowerCase().contains("thumbs.db")) continue;
              boolean policy = policyClass == iClass;
              boolean tia = tiaClasses.contains(iClass);
              boolean ep = epClasses.contains(iClass);
              boolean it = itClasses.contains(iClass);
              HashSet<Integer> classifiedClass = classify(fileClass.getPath(), policy, tia, ep, it, false);
              for (Integer iclass : classifiedClass) {
                if (!dones.containsKey(fileClass.getName())) dones.put(fileClass.getName(), new HashSet<>());
                if (!dones.get(fileClass.getName()).contains("IC" + TresDigits(iclass))) {
                  dones.get(fileClass.getName()).add("IC" + TresDigits(iclass));
                }
              }
              assertEquals(classifiedClass != null, true);
              if (!classifiedClass.contains(iClass)) {
                if (!onlyFinalResult) {
                  System.out.println("File of class " + sClass + " bad classified as " + printClasses(classifiedClass) + " (" + fileClass.getName() + ")");
                }
                classifiedClass = classify(fileClass.getPath(), policy, tia, ep, it, true);
                nIncorrect++;
              } else {
                nCorrect++;
              }
              //assertEquals(classifiedClass.contains(iClass), true);
            }
          }
        }
        System.out.println("Correctly classified files: " + nCorrect);
        System.out.println("Incorrectly classified files: " + nIncorrect);
        System.out.println("Score: " + nCorrect * 100 / (nCorrect + nIncorrect) + "%");

        for (String sClass : lkeys) {
          String linia = sClass;
          for (String file : dones.keySet()) {
            HashSet<String> classes = dones.get(file);
            if (classes.contains("IC000") && classes.size() > 1) classes.remove("IC000");
            String sc = "IC" + sClass;
            if (classes.contains(sc)) {
              linia += "\t" + file;
              writer.println(sc + "\tQ0" + "\t" + file + "\t" + 0 + "\t" + 1 + "\trun-" + nRun);
            }
          }
          writer2.println(linia);
        }
      }
      writer.close();
      writer2.close();
    } catch (Exception ex ) {
      ex.printStackTrace();
    }
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
    HashSet<Integer> epClasses = new HashSet<Integer>();
    epClasses.add(37);
    epClasses.add(38);
    HashSet<Integer> itClasses = new HashSet<Integer>();
    itClasses.add(39);
    CreatePolicy();
    //ClassifyAndScore(new File("Z:\\PROJECTES\\Projectes en desenvolupament\\201411 - PREFORMA\\TIFFs\\classes\\training"), policyClass, tiaClasses, epClasses, itClasses, onlyFinalResult);
    ClassifyAndScore(new File("Z:\\PROJECTES\\Projectes en desenvolupament\\201411 - PREFORMA\\TIFFs\\classes\\test"), policyClass, tiaClasses, epClasses, itClasses, onlyFinalResult);
    DeletePolicy();
  }
}
