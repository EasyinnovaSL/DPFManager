package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.implementation_checker.rules.RuleResult;
import dpfmanager.conformancechecker.tiff.policy_checker.Rule;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;
import dpfmanager.conformancechecker.tiff.policy_checker.Schematron;
import dpfmanager.conformancechecker.tiff.reporting.METS.dc.*;
import dpfmanager.conformancechecker.tiff.reporting.METS.dc.ObjectFactory;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.ReportHtml;
import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import dpfmanager.conformancechecker.tiff.reporting.METS.mets.*;
import dpfmanager.conformancechecker.tiff.reporting.METS.dc.*;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.ImageStrips;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.Strip;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffObject;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.IFD;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sun.awt.image.ImageDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Mar Llambí on 20/06/2016.
 */
public class MetsReport {

  //this is a temporary method
  private boolean isThumbail(IFD ifd){
    boolean isThumbail = false;
    if (ifd.getTags().containsTagId(254) && BigInteger.valueOf(ifd.getTags().get(254).getFirstNumericValue()).testBit(0)) isThumbail = true;
    if (ifd.getTags().containsTagId(255) && ifd.getTags().get(255).getFirstNumericValue() == 2) isThumbail = true;
    if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()) isThumbail = true;

    return isThumbail;
  }

  private  MdSecType.MdWrap extractMdWrap (IndividualReport ir){

    String title;
    String creator;
    String description;
    String date;
    String type = "image/tiff";
    String rights;
    MdSecType.MdWrap mdwrap = new MdSecType.MdWrap();
    mdwrap.setID("W" + mdwrap.hashCode());
    mdwrap.setMDTYPEVERSION("1.1");
    mdwrap.setMDTYPE("DC");

    try{
      GregorianCalendar gregorianCalendar = new GregorianCalendar();
      DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
      XMLGregorianCalendar now =
            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
      mdwrap.setCREATED(now);
    }catch(Exception e){
        e.printStackTrace();
    }
//    ObjectFactory documentDc = new ObjectFactory();
//    ElementContainer container = documentDc.createElementContainer();
//    TiffDocument tiff = ir.getTiffModel();
//    Metadata tiffData = tiff.getMetadata();
//    SimpleLiteral simple = new SimpleLiteral();
//    if(tiffData.get("title") == null){
//      title = "";
//    }else{
//      title = tiffData.get("title").toString();
//    }
//    simple.setLang(title);
//    container.setLiteral(documentDc.createTitle(simple));
//    MdSecType.MdWrap.XmlData xmlData = new MdSecType.MdWrap.XmlData();
//    xmlData.setData(container);
//    mdwrap.setXmlData(xmlData);

    return mdwrap;
  }

  //try to do it recursive
  private DivType extractDivsFromIFD(IFD ifd, FileType file, int startIfd){

    int tags = 0;
    DivType div = new DivType();
    div.setID("I" + div.hashCode());
    div.setTYPE("IFD");
    if (ifd.isImage() && !isThumbail(ifd)){
      div.setLABEL("Main Image");
    }else if(ifd.isImage() && isThumbail(ifd)){
      div.setLABEL("Thumbail");
    }
    DivType.Fptr fptr = new DivType.Fptr();
    fptr.setID("f" + fptr.hashCode());
    fptr.setFILEID(file);

    AreaType area = new AreaType();
    area.setID("a"+area.hashCode());
    area.setFILEID(file);
    area.setBEGIN(String.valueOf(startIfd));
    //add IFD begin and end

    if(ifd.hasSubIFD()){
      DivType subdiv = extractDivsFromIFD(ifd.getsubIFD(), file, startIfd);
      div.setDiv(subdiv);
    }
    Iterator<TagValue> iterator = ifd.getTags().getTags().iterator();
    tags = ifd.getTags().getTags().size();
    while(iterator.hasNext()){
      //create div tag with own start and end
      DivType divTag = new DivType();
      divTag.setID("Tag" + divTag.hashCode());
      divTag.setTYPE("Tag");
      DivType.Fptr fptrT = new DivType.Fptr();
      fptrT.setID("f" + fptrT.hashCode());
      fptrT.setFILEID(file);
      AreaType areaT = new AreaType();
      areaT.setID("a" + areaT.hashCode());
      areaT.setFILEID(file);
      TagValue auxTagValue = iterator.next(); //we need it to work with it
      areaT.setBEGIN("0");
      areaT.setEND("0");
      fptrT.setArea(areaT);
      divTag.setFptr(fptrT);

      //create div value with own start and end
      DivType divValue = new DivType();
      divValue.setID("V"+divValue.hashCode());
      divValue.setTYPE("Value");
      DivType.Fptr fptrV = new DivType.Fptr();
      fptrV.setID("f" + fptrV.hashCode());
      fptrV.setFILEID(file);
      AreaType areaV = new AreaType();
      areaV.setID("a" + areaV.hashCode());
      areaV.setFILEID(file);
      areaV.setBEGIN(String.valueOf(auxTagValue.getOffset()));
      areaV.setEND(String.valueOf(auxTagValue.getOffset() + auxTagValue.getReadlength()));
      fptrV.setArea(areaV);
      divValue.setFptr(fptrV);

      divTag.setDiv(divValue);
      div.setDiv(divTag);
    }
    area.setEND(String.valueOf(startIfd + 2 + (tags * 12) + 4));
    fptr.setArea(area);
    div.setFptr(fptr);
    return div;

  }
  /**
   * Creates the ifd stream.
   *
   * @param ir the ir
   * @param ifd the ifd
   * @param index the index
   * @return the stream
   */
  private List<FileType.Stream> extractStreamsFromIFD( IndividualReport ir, IFD ifd, int index) {

    List<FileType.Stream> streamList = new ArrayList<FileType.Stream>();

    // SubImage and extras
    boolean isThumbail = isThumbail(ifd);

    List <String> streamsStrings = new ArrayList<String>();

    if ((isThumbail && ifd.hasSubIFD() && ifd.getsubIFD().isImage()) || ifd.isImage() ){
      //iff thumbail, and contains subIfd, then is main image, then check if ifd is an image
      streamsStrings.add("image/tiff");
    }

    if (ifd.containsTagId(34665)){  //EXIF
      streamsStrings.add("application/octet-stream");
    }if(isThumbail && ifd.getsubIFD().containsTagId(34665)){
      streamsStrings.add("application/octet-stream");
    }
    if (ifd.containsTagId(700)){ //XMP
      streamsStrings.add("application/xmpp+xml");
    }if (isThumbail && ifd.getsubIFD().containsTagId(34665)) {
      streamsStrings.add("application/xmpp+xml");
    }
    if (ifd.containsTagId(33723)){
      streamsStrings.add("text/vnd.IPTC.NITF");
    }if (isThumbail && ifd.getsubIFD().containsTagId(34665)) { //IPTC
      streamsStrings.add("text/vnd.IPTC.NITF");
    }if (ifd.containsTagId(34675)){
      streamsStrings.add("vnd.iccprofile");
    }if (isThumbail && ifd.getsubIFD().containsTagId(34665)) { //ICC
      streamsStrings.add("vnd.iccprofile");
    }

    Iterator<String> iterator = streamsStrings.iterator();
    while(iterator.hasNext()){
      FileType.Stream stream = new FileType.Stream();
      stream.setID("S" + stream.hashCode());
      stream.setStreamType(iterator.next());
      streamList.add(stream);
    }

    return streamList;
  }


  /**
   * Read showable tags file.
   *
   * @return hashset of tags
   */
  protected HashSet<String> readShowableTags() {
    HashSet<String> hs = new HashSet<String>();
    try {
      Path path = Paths.get("./src/main/resources/");
      if (Files.exists(path)) {
        // Look in current dir
        FileReader fr = new FileReader("./src/main/resources/htmltags.txt");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null) {
          String[] fields = line.split("\t");
          if (fields.length == 1) {
            hs.add(fields[0]);
          }
          line = br.readLine();
        }
        br.close();
        fr.close();
      } else {
        // Look in JAR
        String resource = "htmltags.txt";
        Class cls = ReportHtml.class;
        ClassLoader cLoader = cls.getClassLoader();
        InputStream in = cLoader.getResourceAsStream(resource);
        //CodeSource src = ReportHtml.class.getProtectionDomain().getCodeSource();
        if (in != null) {
          try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            while (line != null) {
              String[] fields = line.split("\t");
              if (fields.length == 1) {
                hs.add(fields[0]);
              }
              line = br.readLine();
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        } else {
          throw new Exception("InputStream is null");
        }
      }
    } catch (Exception ex) {
    }
    return hs;
  }


  private FileType createFile(IndividualReport ir){
    int index = 0;

    IFD ifd = ir.getTiffModel().getFirstIFD();

    FileType file = new FileType();
    file.setID("F"+file.hashCode());
    List<FileType.Stream> streams = new ArrayList<FileType.Stream>();

    while (ifd != null) {
      streams.addAll(extractStreamsFromIFD(ir, ifd, index)); //to each idf, create own streams
      ifd = ifd.getNextIFD();
      index++;
    }
    Iterator<FileType.Stream> iterator = streams.iterator();
    while(iterator.hasNext()){
      file.setStream(iterator.next());
    }
    return file;
  }

  /**
   * Parse an individual report to XML Mets format.
   *
   * @param ir the individual report.
   * @return the element
   */
  private Mets buildReportIndividual(IndividualReport ir, Rules rules){

    Mets mets = new Mets();

    //mets properties
    mets.setOBJID("123456");
    mets.setLABEL("My title");
    mets.setTYPE("myType");
    mets.setPROFILE("myProfile");

    //mets Hdr
    MetsType.MetsHdr metsHdr = new MetsType.MetsHdr();
    metsHdr.setID("Hdr"+metsHdr.hashCode());
    metsHdr.setRECORDSTATUS("Incoming");
    MetsType.MetsHdr.Agent agent = new MetsType.MetsHdr.Agent();
    agent.setROLE("CREATOR");
    agent.setID("A" + agent.hashCode());
    agent.setName("C. Reator");
    metsHdr.setAgent(agent);
    mets.setMetsHdr(metsHdr);


    //mets dmdSec
    MdSecType dmdSec = new MdSecType();
    dmdSec.setID("D" + dmdSec.hashCode());
    dmdSec.setSTATUS("");
    MdSecType.MdWrap mdwrap = extractMdWrap(ir);

    dmdSec.setMdWrap(mdwrap);
    mets.setDmdSec(dmdSec);


    //mets amdSec
    AmdSecType amdsec = new AmdSecType();
    amdsec.setID("A" + amdsec.hashCode());
    MdSecType mdsec_amd = new MdSecType();
    mdsec_amd.setID("R"+mdsec_amd.hashCode());
    amdsec.setRightsMD(mdsec_amd);
    mets.setAmdSec(amdsec);

    //mets File Sec
    MetsType.FileSec filesec = new MetsType.FileSec();
    filesec.setID("F" + filesec.hashCode());
    MetsType.FileSec.FileGrp filegroupMain = new MetsType.FileSec.FileGrp();
    filegroupMain.setID("F" + filegroupMain.hashCode());

    //append tiff structure as streams objects
    FileType file = new FileType();
    if (ir.containsData()) {
      file = createFile(ir);
      filegroupMain.setFile(file);
      filesec.setFileGrp(filegroupMain);
    }

    mets.setFileSec(filesec);

    //mets struct map
    StructMapType structMap = new StructMapType();
    structMap.setID("S"+structMap.hashCode());
    TiffDocument tiffDocument = ir.getTiffModel();

    IFD ifd = tiffDocument.getFirstIFD();
    DivType divHdr = new DivType();
    divHdr.setID("Hdr" + divHdr.hashCode());
    divHdr.setTYPE("Header");
    structMap.setDiv(divHdr);
    int startIfd = 8;
    while(ifd != null){
      DivType div = extractDivsFromIFD(ifd, file, startIfd);
      startIfd = ifd.getNextOffset();
      divHdr.setDiv(div);
      ifd = ifd.getNextIFD();
    }
    structMap.setDiv(divHdr);
    mets.setStructMap(structMap);
    return mets;

  }

  /**
   * Parse an individual report to XML Mets format.
   *
   * @param ir      the individual report.
   * @param rules   the policy checker.
   * @return the XML Mets string generated.
   */
  public String parseIndividual(IndividualReport ir, Rules rules) {

    try {
      Mets mets = new Mets();
      mets = buildReportIndividual(ir, rules);

      JAXBContext context = null;
      context = JAXBContext.newInstance(Mets.class);
      Marshaller m = null;
      m = context.createMarshaller();
       //for pretty-print XML in JAXB
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      StringWriter sw = new StringWriter();
      m.marshal(mets, sw);

      //to String
        String output = sw.toString();

      return output;
    } catch (JAXBException e) {
      e.printStackTrace();
      return "";
    }

  }
}
