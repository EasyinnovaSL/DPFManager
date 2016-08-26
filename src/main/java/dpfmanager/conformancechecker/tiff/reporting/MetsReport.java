package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.configuration.Configuration;
import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.*;
import dpfmanager.conformancechecker.tiff.reporting.METS.premis.Event;
import dpfmanager.shell.core.DPFManagerProperties;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.ReportHtml;

import dpfmanager.conformancechecker.tiff.reporting.METS.mets.*;

import com.easyinnova.tiff.model.*;
import com.easyinnova.tiff.model.types.*;
import com.easyinnova.tiff.model.types.Long;
import com.easyinnova.tiff.model.types.Short;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.Byte;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.SplittableRandom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Created by Mar Llambí on 20/06/2016.
 */
public class MetsReport {

  /**
   * This method generate element MdWrap from dmd Section in METS structure
   * @param ir Individual Report
   * @return mdWrap element
   */
  private  MdSecType.MdWrap constructDmdMdWrap (IndividualReport ir){

    MdSecType.MdWrap mdwrap = new MdSecType.MdWrap();
    mdwrap.setID("W" + mdwrap.hashCode());
    mdwrap.setMDTYPEVERSION("1.1");
    mdwrap.setMDTYPE("DC");
    mdwrap.setMIMETYPE("text/xml");

    try{
      GregorianCalendar gregorianCalendar = new GregorianCalendar();
      DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
      XMLGregorianCalendar now =
            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
      mdwrap.setCREATED(now);
    }catch(Exception e){
        e.printStackTrace();
    }
    MdSecType.MdWrap.XmlData xmlData = new MdSecType.MdWrap.XmlData();
    MdSecType.MdWrap.XmlData.SimpleLiteral literal = new MdSecType.MdWrap.XmlData.SimpleLiteral();
    literal.setType("image/tiff");

    //getting tiff info
    TiffDocument tiffDocument = ir.getTiffModel();
    Metadata metadata = tiffDocument.getMetadata();
    if (metadata != null) {
      if (metadata.get("title") != null) {
        literal.setTitle(metadata.get("title").toString());
      }
      if (metadata.get("creator") != null) {
        literal.setCreator(metadata.get("creator").toString());
      }
      if (metadata.get("Description") != null) {
        literal.setDescription(metadata.get("Description").toString());
      }
      if (metadata.get("dateTime") != null) {
        literal.setDate(metadata.get("dateTime").toString());
      }
      if (metadata.get("Copyright") != null) {
        literal.setRights(metadata.get("Copyright").toString());
      }

      xmlData.setLiteral(literal);
      mdwrap.setXmlData(xmlData);

      return mdwrap;
    }else{
      return null;
    }
  }

  /**
   * This method constructs a component from PhotometricInterpretation of Black and White
   * @param headroom data value (code) from ReferenceBlackWhite
   * @param footroom data value (code) from ReferenceBlackWhite
   * @return ReferenceBlackWhite components of METS structure
   */
  private BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component createComponentFromReferences (Rational headroom, Rational footroom){

    BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component component = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component();
    TypeOfComponentPhotometricInterpretationType componentPhotometric = new TypeOfComponentPhotometricInterpretationType();
    componentPhotometric.setValue(ComponentPhotometricInterpretationType.fromValue("Y"));
    componentPhotometric.setUse("System");
    component.setComponentPhotometricInterpretation(componentPhotometric);
    RationalType footRoomType = new RationalType();
    RationalType headRoomType = new RationalType();
    headRoomType.setDenominator(BigInteger.valueOf(headroom.getDenominator()));
    headRoomType.setNumerator(BigInteger.valueOf(headroom.getNumerator()));
    footRoomType.setDenominator(BigInteger.valueOf(footroom.getDenominator()));
    footRoomType.setNumerator(BigInteger.valueOf(footroom.getNumerator()));
    component.setFootroom(footRoomType);
    component.setHeadroom(headRoomType);

    return component;
  }


  /**
   * This method obtains a date from a File comprising different ways to get it
   * @param ifd IFD element
   * @param filepath Filepath from original tiff document
   * @return date in String format
   */
  private String getImageDate(IFD ifd, String filepath){

    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("DateTimeOriginal"))){
      return ifd.getMetadata().get("DateTimeOriginal").toString();
    }else if (ifd.getMetadata().containsTagId(TiffTags.getTagId("DateTime"))){
      return ifd.getMetadata().get("DateTime").toString();
    }else{
      try {
        File file = new File(filepath);
        Path filePath = file.toPath();
        BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
        return attr.creationTime().toString();
      } catch (IOException e) {
        e.printStackTrace();
        return "";
      }

    }
  }

  /**
   *  This method create a NISO element basicImage from IFD metadata as NISO chapter 7 stardard
   * @param ifd IFD element
   * @return BasicImageInformationType element
   *
   */
  private BasicImageInformationType createNisoBasicImageInformation(IFD ifd){

    //7.1 Basic Image characteristics
    BasicImageInformationType basicImage = new BasicImageInformationType();
    BasicImageInformationType.BasicImageCharacteristics imageCharac = new BasicImageInformationType.BasicImageCharacteristics();
    PositiveIntegerType width = new PositiveIntegerType();
    width.setUse("System");
    if (ifd.getMetadata().containsTagId(TiffTags.getTagId("ImageWidth"))) {
      width.setValue(new BigInteger(ifd.getMetadata().get("ImageWidth").toString()));
    }else{
      width.setValue(BigInteger.valueOf(0));
    }
    imageCharac.setImageWidth(width);
    PositiveIntegerType height = new PositiveIntegerType();
    height.setUse("System");
    if (ifd.getMetadata().containsTagId(TiffTags.getTagId("ImageLength"))) {
      height.setValue(new BigInteger(ifd.getMetadata().get("ImageLength").toString()));
    }else{
      height.setValue(BigInteger.valueOf(0));
    }
    imageCharac.setImageHeight(height);

    //7.1.3 photometricInterpreation
    if (ifd.getMetadata().containsTagId(TiffTags.getTagId("PhotometricInterpretation"))) {
      BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation photometricInterpretation = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation();
      StringType colorSpace = new StringType();
      colorSpace.setUse("System");
      int photo = Integer.parseInt(ifd.getMetadata().get("PhotometricInterpretation").toString());
      colorSpace.setValue(TiffConformanceChecker.photometricName(photo));
      photometricInterpretation.setColorSpace(colorSpace);

      //7.1.3.2.1 Icc profile
      if (ifd.getMetadata().get("ICCProfile") != null) {
        BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile colorProfile = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile();
        BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.IccProfile iccProfile = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ColorProfile.IccProfile();
        abstractTiffType iccProfileFromIfd = ifd.getMetadata().get("ICCProfile").getValue().get(0);
        IccProfile icc = (IccProfile)iccProfileFromIfd;
        StringType iccVersion = new StringType();
        iccVersion.setUse("System");
        iccVersion.setValue(icc.getVersion());
        iccProfile.setIccProfileVersion(iccVersion);
        StringType iccName = new StringType();
        iccName.setUse("System");
        iccName.setValue(icc.getDescription());
        iccProfile.setIccProfileName(iccName);
        colorProfile.setIccProfile(iccProfile);
        photometricInterpretation.setColorProfile(colorProfile);
      }

      //7.1.3.3 YcbCr image
      if(colorSpace.getValue().equals("YCbCr")){
        BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr ycbcr = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr();
        //YcbCr sampling
        BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrSubSampling yCbCrSubSampling = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrSubSampling();
        TypeOfYCbCrSubsampleHorizType subsampleHorizType = new TypeOfYCbCrSubsampleHorizType();
        subsampleHorizType.setUse("System");
        subsampleHorizType.setValue(BigInteger.valueOf(ifd.getMetadata().get("YCbCrSubSampling").getValue().get(0).toInt()));
        yCbCrSubSampling.setYCbCrSubsampleHoriz(subsampleHorizType);
        TypeOfYCbCrSubsampleVertType subsampleVertType = new TypeOfYCbCrSubsampleVertType();
        subsampleVertType.setUse("System");
        subsampleVertType.setValue(BigInteger.valueOf(ifd.getMetadata().get("YCbCrSubSampling").getValue().get(1).toInt()));
        yCbCrSubSampling.setYCbCrSubsampleVert(subsampleVertType);
        ycbcr.setYCbCrSubSampling(yCbCrSubSampling);

        //YcbCr coefficients
        BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrCoefficients yCbCrCoefficients = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.YCbCr.YCbCrCoefficients();
        Rational redRational = (Rational)ifd.getMetadata().get("YCbCrCoefficients").getValue().get(0);
        RationalType lumaRed = new RationalType();
        lumaRed.setDenominator(BigInteger.valueOf(redRational.getDenominator()));
        lumaRed.setNumerator(BigInteger.valueOf(redRational.getNumerator()));
        yCbCrCoefficients.setLumaRed(lumaRed);
        Rational greenRational = (Rational)ifd.getMetadata().get("YCbCrCoefficients").getValue().get(1);
        RationalType lumaGreen = new RationalType();
        lumaGreen.setDenominator(BigInteger.valueOf(greenRational.getDenominator()));
        lumaGreen.setNumerator(BigInteger.valueOf(greenRational.getNumerator()));
        yCbCrCoefficients.setLumaGreen(lumaGreen);
        Rational blueRational = (Rational)ifd.getMetadata().get("YCbCrCoefficients").getValue().get(2);
        RationalType lumaBlue = new RationalType();
        lumaBlue.setDenominator(BigInteger.valueOf(blueRational.getDenominator()));
        lumaBlue.setNumerator(BigInteger.valueOf(blueRational.getNumerator()));
        yCbCrCoefficients.setLumaBlue(lumaBlue);
        ycbcr.setYCbCrCoefficients(yCbCrCoefficients);

        //yCbCr positioning
        TypeOfYCbCrPositioningType yCbCrPositioning = new TypeOfYCbCrPositioningType();
        yCbCrPositioning.setUse("System");
        yCbCrPositioning.setValue(BigInteger.valueOf(ifd.getMetadata().get("YCbCrPositioning").getValue().get(0).toInt()));
        ycbcr.setYCbCrPositioning(yCbCrPositioning);

        photometricInterpretation.setYCbCr(ycbcr);

        //reference Black and White

        BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite referenceBlackWhite = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite();

        Rational headroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(0);
        Rational footroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(1);
        BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component component = createComponentFromReferences(headroom, footroom);
        referenceBlackWhite.setComponent(component);
        headroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(2);
        footroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(3);
        component = createComponentFromReferences(headroom, footroom);
        referenceBlackWhite.setComponent(component);
        headroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(4);
        footroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(5);
        component = createComponentFromReferences(headroom, footroom);
        referenceBlackWhite.setComponent(component);

        photometricInterpretation.setReferenceBlackWhite(referenceBlackWhite);

      }

      imageCharac.setPhotometricInterpretation(photometricInterpretation);
    }
    basicImage.setBasicImageCharacteristics(imageCharac);

    return basicImage;

  }

  /**
   * This method creates a NISO element GPSData from IFD private Tags as NISO chapter 8.4.4.2 standard
   * @param ir Individual report
   * @return GPSData element
   */
  private ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData createNisoGPSData (IndividualReport ir){

    ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData gpsData = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData ();
    if (ir.getTiffModel().getMetadata().contains("CFAPattern") ){
      StringType gpsVersion = new StringType();
      gpsVersion.setUse("System");
      gpsVersion.setValue(ir.getTiffModel().getMetadata().get("CFAPattern").toString());
      gpsData.setGpsVersionID(gpsVersion);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSLatitudeRef") && GpsLatitudeRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSLatitudeRef").toString())){
      TypeOfgpsLatitudeRefType gpsLatitudeRefType = new TypeOfgpsLatitudeRefType();
      gpsLatitudeRefType.setUse("System");
      gpsLatitudeRefType.setValue(GpsLatitudeRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSLatitudeRef").toString()));
      gpsData.setGpsLatitudeRef(gpsLatitudeRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSLatitude") ){
      ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLatitude latitude = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLatitude();
      List<Rational> gpsLatitudeList = (List<Rational>)ir.getTiffModel().getMetadata().get("GPSLatitude");
      RationalType degreesValue = new RationalType();
      degreesValue.setUse("System");
      degreesValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(0).getNumerator()));
      degreesValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(0).getDenominator()));
      latitude.setDegrees(degreesValue);
      RationalType minutesValue = new RationalType();
      minutesValue.setUse("System");
      minutesValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(1).getNumerator()));
      minutesValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(1).getDenominator()));
      latitude.setMinutes(minutesValue);
      RationalType secondsValue = new RationalType();
      secondsValue.setUse("System");
      secondsValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(2).getNumerator()));
      secondsValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(2).getDenominator()));
      latitude.setSeconds(secondsValue);
      gpsData.setGPSLatitude(latitude);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSLongitudeRef") && GpsLongitudeRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSLongitudeRef").toString())){
      TypeOfgpsLongitudeRefType gpsLongitudRefType = new TypeOfgpsLongitudeRefType();
      gpsLongitudRefType.setUse("System");
      gpsLongitudRefType.setValue(GpsLongitudeRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSLongitudeRef").toString()));
      gpsData.setGpsLongitudeRef(gpsLongitudRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSLongitude") ){
      ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLongitude longitude = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLongitude();
      List<Rational> gpsLatitudeList = (List<Rational>)ir.getTiffModel().getMetadata().get("GPSLongitude");
      RationalType degreesValue = new RationalType();
      degreesValue.setUse("System");
      degreesValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(0).getNumerator()));
      degreesValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(0).getDenominator()));
      longitude.setDegrees(degreesValue);
      RationalType minutesValue = new RationalType();
      minutesValue.setUse("System");
      minutesValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(1).getNumerator()));
      minutesValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(1).getDenominator()));
      longitude.setMinutes(minutesValue);
      RationalType secondsValue = new RationalType();
      secondsValue.setUse("System");
      secondsValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(2).getNumerator()));
      secondsValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(2).getDenominator()));
      longitude.setSeconds(secondsValue);
      gpsData.setGPSLongitude(longitude);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSAltitudeRef") && GpsAltitudeRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSAltitudeRef").toString())){
      TypeOfgpsAltitudeRefType gpsAltitudeRefType = new TypeOfgpsAltitudeRefType();
      gpsAltitudeRefType.setUse("System");
      gpsAltitudeRefType.setValue(GpsAltitudeRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSAltitudeRef").toString()));
      gpsData.setGpsAltitudeRef(gpsAltitudeRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSAltitude") ){
      RationalType altitude = new RationalType();
      altitude.setUse("System");
      Rational altitudeFromTag = (Rational)ir.getTiffModel().getMetadata().get("GPSAltitude");
      altitude.setNumerator(BigInteger.valueOf(altitudeFromTag.getNumerator()));
      altitude.setDenominator(BigInteger.valueOf(altitudeFromTag.getDenominator()));
      gpsData.setGpsAltitude(altitude);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSTimeStamp") ){
      StringType timestamp = new StringType();
      timestamp.setUse("System");
      timestamp.setValue(ir.getTiffModel().getMetadata().get("GPSTimeStamp").toString());
      gpsData.setGpsTimeStamp(timestamp);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSSatellites") ){
      StringType satellites = new StringType();
      satellites.setUse("System");
      satellites.setValue(ir.getTiffModel().getMetadata().get("GPSSatellites").toString());
      gpsData.setGpsSatellites(satellites);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSStatus") && GpsStatusType.verifyTag(ir.getTiffModel().getMetadata().get("GPSStatus").toString()) ){
      TypeOfgpsStatusType gpsStatusType = new TypeOfgpsStatusType();
      gpsStatusType.setUse("System");
      gpsStatusType.setValue(GpsStatusType.fromValue(ir.getTiffModel().getMetadata().get("GPSStatus").toString()));
      gpsData.setGpsStatus(gpsStatusType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSMeasureMode") ){
      TypeOfgpsMeasureModeType gpsMeasureModeType = new TypeOfgpsMeasureModeType();
      gpsMeasureModeType.setUse("System");
      gpsMeasureModeType.setValue(ir.getTiffModel().getMetadata().get("GPSMeasureMode").toString());
      gpsData.setGpsMeasureMode(gpsMeasureModeType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDOP") ){
      RationalType gpsDop = new RationalType();
      gpsDop.setUse("System");
      Rational gpsDopFromTag = (Rational)ir.getTiffModel().getMetadata().get("GPSDOP");
      gpsDop.setDenominator(BigInteger.valueOf(gpsDopFromTag.getDenominator()));
      gpsDop.setNumerator(BigInteger.valueOf(gpsDopFromTag.getNumerator()));
      gpsData.setGpsDOP(gpsDop);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSSpeedRef") && GpsSpeedRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSSpeedRef").toString()) ){
      TypeOfgpsSpeedRefType typeOfgpsSpeedRefType = new TypeOfgpsSpeedRefType();
      typeOfgpsSpeedRefType.setUse("System");
      typeOfgpsSpeedRefType.setValue(GpsSpeedRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSSpeedRef").toString()));
      gpsData.setGpsSpeedRef(typeOfgpsSpeedRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSSpeed") ){
      RationalType gpsSpeed = new RationalType();
      gpsSpeed.setUse("System");
      Rational gpsSpeedfromTag = (Rational)ir.getTiffModel().getMetadata().get("GPSSpeed");
      gpsSpeed.setDenominator(BigInteger.valueOf(gpsSpeedfromTag.getDenominator()));
      gpsSpeed.setNumerator(BigInteger.valueOf(gpsSpeedfromTag.getNumerator()));
      gpsData.setGpsSpeed(gpsSpeed);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSTrackRef") && GpsTrackRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSTrackRef").toString())){
      TypeOfgpsTrackRefType typeOfgpsTrackRefType = new TypeOfgpsTrackRefType();
      typeOfgpsTrackRefType.setUse("System");
      typeOfgpsTrackRefType.setValue(GpsTrackRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSTrackRef").toString()));
      gpsData.setGpsTrackRef(typeOfgpsTrackRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSTrack") ){
      RationalType gpsTrack = new RationalType();
      gpsTrack.setUse("System");
      Rational gpsTrackfromTag = (Rational)ir.getTiffModel().getMetadata().get("GPSTrack");
      gpsTrack.setDenominator(BigInteger.valueOf(gpsTrackfromTag.getDenominator()));
      gpsTrack.setNumerator(BigInteger.valueOf(gpsTrackfromTag.getNumerator()));
      gpsData.setGpsTrack(gpsTrack);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSImgDirectionRef") && GpsImgDirectionRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSImgDirectionRef").toString())){
      TypeOfgpsImgDirectionRefType typeOfgpsImgDirectionRefType = new TypeOfgpsImgDirectionRefType();
      typeOfgpsImgDirectionRefType.setUse("System");
      typeOfgpsImgDirectionRefType.setValue(GpsImgDirectionRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSImgDirectionRef").toString()));
      gpsData.setGpsImgDirectionRef(typeOfgpsImgDirectionRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSImgDirection") ){
      RationalType gpsImgDirection = new RationalType();
      gpsImgDirection.setUse("System");
      Rational gpsImgDirectionfromTag = (Rational)ir.getTiffModel().getMetadata().get("GPSImgDirection");
      gpsImgDirection.setDenominator(BigInteger.valueOf(gpsImgDirectionfromTag.getDenominator()));
      gpsImgDirection.setNumerator(BigInteger.valueOf(gpsImgDirectionfromTag.getNumerator()));
      gpsData.setGpsImgDirection(gpsImgDirection);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSMapDatum") ){
      StringType mapDatum = new StringType();
      mapDatum.setUse("System");
      mapDatum.setValue(ir.getTiffModel().getMetadata().get("GPSMapDatum").toString());
      gpsData.setGpsMapDatum(mapDatum);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestLatitudeRef") && GpsDestLatitudeRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSDestLatitudeRef").toString())){
      TypeOfgpsDestLatitudeRefType typeOfgpsDestLatitudeRefType = new TypeOfgpsDestLatitudeRefType();
      typeOfgpsDestLatitudeRefType.setUse("System");
      typeOfgpsDestLatitudeRefType.setValue(GpsDestLatitudeRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSDestLatitudeRef").toString()));
      gpsData.setGpsDestLatitudeRef(typeOfgpsDestLatitudeRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestLatitude") ){
      ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLatitude destLatitude = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLatitude();
      List<Rational> gpsLatitudeList = (List<Rational>)ir.getTiffModel().getMetadata().get("GPSDestLatitude");
      RationalType degreesValue = new RationalType();
      degreesValue.setUse("System");
      degreesValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(0).getNumerator()));
      degreesValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(0).getDenominator()));
      destLatitude.setDegrees(degreesValue);
      RationalType minutesValue = new RationalType();
      minutesValue.setUse("System");
      minutesValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(1).getNumerator()));
      minutesValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(1).getDenominator()));
      destLatitude.setMinutes(minutesValue);
      RationalType secondsValue = new RationalType();
      secondsValue.setUse("System");
      secondsValue.setNumerator(BigInteger.valueOf(gpsLatitudeList.get(2).getNumerator()));
      secondsValue.setDenominator(BigInteger.valueOf(gpsLatitudeList.get(2).getDenominator()));
      destLatitude.setSeconds(secondsValue);
      gpsData.setGPSDestLatitude(destLatitude);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestLongitudeRef") && GpsDestLongitudeRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSDestLongitudeRef").toString())){
      TypeOfgpsDestLongitudeRefType typeOfgpsDestLongitudeRefType = new TypeOfgpsDestLongitudeRefType();
      typeOfgpsDestLongitudeRefType.setUse("System");
      typeOfgpsDestLongitudeRefType.setValue(GpsDestLongitudeRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSDestLongitudeRef").toString()));
      gpsData.setGpsDestLongitudeRef(typeOfgpsDestLongitudeRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestLongitude") ){
      ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLongitude destLongitude = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLongitude();
      List<Rational> gpsLongitudeList = (List<Rational>)ir.getTiffModel().getMetadata().get("GPSDestLongitude");
      RationalType degreesValue = new RationalType();
      degreesValue.setUse("System");
      degreesValue.setNumerator(BigInteger.valueOf(gpsLongitudeList.get(0).getNumerator()));
      degreesValue.setDenominator(BigInteger.valueOf(gpsLongitudeList.get(0).getDenominator()));
      destLongitude.setDegrees(degreesValue);
      RationalType minutesValue = new RationalType();
      minutesValue.setUse("System");
      minutesValue.setNumerator(BigInteger.valueOf(gpsLongitudeList.get(1).getNumerator()));
      minutesValue.setDenominator(BigInteger.valueOf(gpsLongitudeList.get(1).getDenominator()));
      destLongitude.setMinutes(minutesValue);
      RationalType secondsValue = new RationalType();
      secondsValue.setUse("System");
      secondsValue.setNumerator(BigInteger.valueOf(gpsLongitudeList.get(2).getNumerator()));
      secondsValue.setDenominator(BigInteger.valueOf(gpsLongitudeList.get(2).getDenominator()));
      destLongitude.setSeconds(secondsValue);
      gpsData.setGPSDestLongitude(destLongitude);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestBearingRef") && GpsDestBearingRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSDestBearingRef").toString()) ){
      TypeOfgpsDestBearingRefType typeOfgpsDestBearingRefType = new TypeOfgpsDestBearingRefType();
      typeOfgpsDestBearingRefType.setUse("System");
      typeOfgpsDestBearingRefType.setValue(GpsDestBearingRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSDestBearingRef").toString()));
      gpsData.setGpsDestBearingRef(typeOfgpsDestBearingRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestBearing") ){
      RationalType gpsDestBearing = new RationalType();
      gpsDestBearing.setUse("System");
      Rational gpsDopFromTag = (Rational)ir.getTiffModel().getMetadata().get("GPSDestBearing");
      gpsDestBearing.setDenominator(BigInteger.valueOf(gpsDopFromTag.getDenominator()));
      gpsDestBearing.setNumerator(BigInteger.valueOf(gpsDopFromTag.getNumerator()));
      gpsData.setGpsDestBearing(gpsDestBearing);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestDistanceRef") && GpsDestDistanceRefType.verifyTag(ir.getTiffModel().getMetadata().get("GPSDestDistanceRef").toString()) ){
      TypeOfgpsDestDistanceRefType typeOfgpsDestDistanceRefType = new TypeOfgpsDestDistanceRefType();
      typeOfgpsDestDistanceRefType.setUse("System");
      typeOfgpsDestDistanceRefType.setValue(GpsDestDistanceRefType.fromValue(ir.getTiffModel().getMetadata().get("GPSDestDistanceRef").toString()));
      gpsData.setGpsDestDistanceRef(typeOfgpsDestDistanceRefType);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDestDistance") ){
      RationalType gpsDestDistance = new RationalType();
      gpsDestDistance.setUse("System");
      Rational gpsDopFromTag = (Rational)ir.getTiffModel().getMetadata().get("GPSDestDistance");
      gpsDestDistance.setDenominator(BigInteger.valueOf(gpsDopFromTag.getDenominator()));
      gpsDestDistance.setNumerator(BigInteger.valueOf(gpsDopFromTag.getNumerator()));
      gpsData.setGpsDestDistance(gpsDestDistance);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSProcessingMethod") ){
      StringType processingMethod = new StringType();
      processingMethod.setUse("System");
      processingMethod.setValue(ir.getTiffModel().getMetadata().get("GPSProcessingMethod").toString());
      gpsData.setGpsProcessingMethod(processingMethod);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSAreaInformation") ){
      StringType areaInformation = new StringType();
      areaInformation.setUse("System");
      areaInformation.setValue(ir.getTiffModel().getMetadata().get("GPSAreaInformation").toString());
      gpsData.setGpsAreaInformation(areaInformation);
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDateStamp") ){
      try{
        DateType gpsDateStamp = new DateType();
        gpsDateStamp.setUse("System");
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(ir.getTiffModel().getMetadata().get("GPSDateStamp").toString());
        gpsDateStamp.setValue(xmlDate);
        gpsData.setGpsDateStamp(gpsDateStamp);
      }catch(DatatypeConfigurationException e){
        e.printStackTrace();
        return null;
      }
    }
    if (ir.getTiffModel().getMetadata().contains("GPSDifferential") && GpsDifferentialType.verifyTag(ir.getTiffModel().getMetadata().get("GPSDifferential").toString()) ){
      TypeOfgpsDifferentialType gpsDifferentialType = new TypeOfgpsDifferentialType();
      gpsDifferentialType.setUse("System");
      gpsDifferentialType.setValue(GpsDifferentialType.fromValue(ir.getTiffModel().getMetadata().get("GPSDifferential").toString()));
      gpsData.setGpsDifferential(gpsDifferentialType);
    }
    return gpsData;
  }

  /**
   * This method create a NISO element ImageData from Exif Tags as Niso chapter 8.4.4.1 standard
   * @param ir IndividualReport element
   * @param ifd IFD from Tiff document
   * @return ImageData
   */

  private ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData createNisoImageData (IndividualReport ir, IFD ifd) {

    ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData imageData = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData();
    if(ir.getTiffModel().getMetadata().contains("FNumber")){
      TypeOfNonNegativeRealType fnumber = new TypeOfNonNegativeRealType();
      fnumber.setUse("Manager");
      Rational fnumberValue = (Rational)ir.getTiffModel().getMetadata().get("FNumber");
      fnumber.setValue(fnumberValue.getFloatValue());
      imageData.setFNumber(fnumber);
    }
    if(ir.getTiffModel().getMetadata().contains("ExposureTime")){
      TypeOfNonNegativeRealType exposureTime = new TypeOfNonNegativeRealType();
      exposureTime.setUse("Manager");
      Rational exposure = (Rational)ir.getTiffModel().getMetadata().get("ExposureTime");
      exposureTime.setValue(exposure.getFloatValue());
      imageData.setExposureTime(exposureTime);
    }
    if(ir.getTiffModel().getMetadata().contains("ExposureProgram") && ExposureProgramType.verifyTag(ir.getTiffModel().getMetadata().get("ExposureProgram").toString())){
      TypeOfExposureProgramType exposureProgram = new TypeOfExposureProgramType();
      exposureProgram.setUse("Manager");
      exposureProgram.setValue(ExposureProgramType.fromValue(ir.getTiffModel().getMetadata().get("ExposureProgram").toString()));
      imageData.setExposureProgram(exposureProgram);
    }
    if(ir.getTiffModel().getMetadata().contains("SpectralSensitivity")){
      StringType spectralSensivity = new StringType();
      spectralSensivity.setUse("Manager");
      spectralSensivity.setValue(ir.getTiffModel().getMetadata().get("SpectralSensitivity").toString());
      imageData.setSpectralSensitivity(spectralSensivity);

    }
    if(ir.getTiffModel().getMetadata().contains("ISOSpeedRatings")){
      PositiveIntegerType iosSpeed = new PositiveIntegerType();
      iosSpeed.setUse("Manager");
      Short speedRatingValue = (Short)ir.getTiffModel().getMetadata().get("ISOSpeedRatings");
      iosSpeed.setValue(BigInteger.valueOf(speedRatingValue.getValue()));
      imageData.setIsoSpeedRatings(iosSpeed);
    }
    TypeOfExifVersionType exifVersion = new TypeOfExifVersionType();
    exifVersion.setUse("System");
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("ExifVersion"))){
      exifVersion.setValue(ifd.getMetadata().get("ExifVersion").getValue().toString());
    }else if (ifd.containsTagId(34665)){ //If contains EXIF but version is not included
      exifVersion.setValue("0220");
    }
    imageData.setExifVersion(exifVersion);
    if(ir.getTiffModel().getMetadata().contains("ShutterSpeedValue")){
      RationalType shutterSpeedValue = new RationalType();
      shutterSpeedValue.setUse("Manager");
      SRational speedTagValue = (SRational)ir.getTiffModel().getMetadata().get("ShutterSpeedValue");
      shutterSpeedValue.setDenominator(BigInteger.valueOf(speedTagValue.getDenominator()));
      shutterSpeedValue.setNumerator(BigInteger.valueOf(speedTagValue.getNumerator()));
      imageData.setShutterSpeedValue(shutterSpeedValue);
    }
    if(ir.getTiffModel().getMetadata().contains("ApertureValue")){
      RationalType apertureValue = new RationalType();
      apertureValue.setUse("Manager");
      Rational apertureTagValue = (Rational)ir.getTiffModel().getMetadata().get("ApertureValue");
      apertureValue.setDenominator(BigInteger.valueOf(apertureTagValue.getDenominator()));
      apertureValue.setNumerator(BigInteger.valueOf(apertureTagValue.getNumerator()));
      imageData.setApertureValue(apertureValue);
    }
    if(ir.getTiffModel().getMetadata().contains("BrightnessValue")){
      RationalType bightnessValue = new RationalType();
      bightnessValue.setUse("Manager");
      SRational brightnessTagValue = (SRational)ir.getTiffModel().getMetadata().get("BrightnessValue");
      bightnessValue.setDenominator(BigInteger.valueOf(brightnessTagValue.getDenominator()));
      bightnessValue.setNumerator(BigInteger.valueOf(brightnessTagValue.getNumerator()));
      imageData.setBrightnessValue(bightnessValue);
    }
    if(ir.getTiffModel().getMetadata().contains("ExposureBiasValue")){
      RationalType exposureBias = new RationalType();
      exposureBias.setUse("Manager");
      SRational exposureTagValue = (SRational)ir.getTiffModel().getMetadata().get("ExposureBiasValue");
      exposureBias.setDenominator(BigInteger.valueOf(exposureTagValue.getDenominator()));
      exposureBias.setNumerator(BigInteger.valueOf(exposureTagValue.getNumerator()));
      imageData.setExposureBiasValue(exposureBias);
    }
    if(ir.getTiffModel().getMetadata().contains("MaxApertureValue")){
      RationalType maxAperture = new RationalType();
      maxAperture.setUse("Manager");
      SRational maxApertureTagValue = (SRational)ir.getTiffModel().getMetadata().get("MaxApertureValue");
      maxAperture.setDenominator(BigInteger.valueOf(maxApertureTagValue.getDenominator()));
      maxAperture.setNumerator(BigInteger.valueOf(maxApertureTagValue.getNumerator()));
      imageData.setMaxApertureValue(maxAperture);
    }
    if(ir.getTiffModel().getMetadata().contains("SubjectDistance")){
      ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance subjectDistance = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance();
      TypeOfNonNegativeDecimalType distance = new TypeOfNonNegativeDecimalType();
      distance.setUse("Manager");
      Long subjectLongValue = (Long)ir.getTiffModel().getMetadata().get("SubjectDistance");
      distance.setValue(BigDecimal.valueOf(subjectLongValue.getValue()));
      subjectDistance.setDistance(distance);
      imageData.setSubjectDistance(subjectDistance);
    }
    if(ir.getTiffModel().getMetadata().contains("MeteringMode") && MeteringModeType.verifyTag(ir.getTiffModel().getMetadata().get("MeteringMode").toString())){
      TypeOfMeteringModeType meteringModeType = new TypeOfMeteringModeType();
      meteringModeType.setUse("Manager");
      meteringModeType.setValue(MeteringModeType.fromValue(ir.getTiffModel().getMetadata().get("MeteringMode").toString()));
      imageData.setMeteringMode(meteringModeType);
    }
    if(ir.getTiffModel().getMetadata().contains("LightSource") && LightSourceType.verifyTag(ir.getTiffModel().getMetadata().get("LightSource").toString())){
      TypeOfLightSourceType lightSourceType = new TypeOfLightSourceType();
      lightSourceType.setUse("Manager");
      lightSourceType.setValue(LightSourceType.fromValue(ir.getTiffModel().getMetadata().get("LightSource").toString()));
      imageData.setLightSource(lightSourceType);
    }
    if(ir.getTiffModel().getMetadata().contains("Flash")){
      TypeOfFlashType flashType = new TypeOfFlashType();
      flashType.setUse("Manager");
      Integer hexFlashInt = Integer.parseInt(ir.getTiffModel().getMetadata().get("Flash").toString());
      String hexFlash = String.format("%04X", hexFlashInt);
      if(FlashType.verifyTag(hexFlash)){
        flashType.setValue(FlashType.fromValue(hexFlash));
        imageData.setFlash(flashType);
      }

    }
    if(ir.getTiffModel().getMetadata().contains("FlashEnergy")){
      RationalType flashEnergy = new RationalType();
      flashEnergy.setUse("Manager");
      Rational flashEnergyTagValue = (Rational)ir.getTiffModel().getMetadata().get("FlashEnergy");
      flashEnergy.setDenominator(BigInteger.valueOf(flashEnergyTagValue.getDenominator()));
      flashEnergy.setNumerator(BigInteger.valueOf(flashEnergyTagValue.getNumerator()));
      imageData.setFlashEnergy(flashEnergy);
    }
    if(ir.getTiffModel().getMetadata().contains("ExposureIndex")){
      TypeOfPositiveRealType exposureIndex = new TypeOfPositiveRealType();
      exposureIndex.setUse("Manager");
      Rational exposureTagValue = (Rational)ir.getTiffModel().getMetadata().get("ExposureIndex");
      exposureIndex.setValue(exposureTagValue.getFloatValue());
      imageData.setExposureIndex(exposureIndex);
    }
    if(ir.getTiffModel().getMetadata().contains("SensingMethod")){
      TypeOfSensingMethodType exposureIndex = new TypeOfSensingMethodType();
      exposureIndex.setUse("Manager");
      Short exposureTagValue = (Short)ir.getTiffModel().getMetadata().get("SensingMethod");
      if(SensingMethodType.verifyTag(exposureTagValue.toString())){
        exposureIndex.setValue(SensingMethodType.fromValue(exposureTagValue.toString()));
        imageData.setSensingMethod(exposureIndex);
      }
    }
    if(ir.getTiffModel().getMetadata().contains("CFAPattern")){
      IntegerType cfaPattern = new IntegerType();
      cfaPattern.setUse("Manager");
      cfaPattern.setValue(new BigInteger (ir.getTiffModel().getMetadata().get("CFAPattern").toString()));
      imageData.setCfaPattern(cfaPattern);
    }
    return imageData;
  }

  /**
   * This method creates a NISO element ImageCaptureMetadataType from Baseline tiff tags as NISO chapter 8 standard
   * @param ifd IFD element from Tiff document
   * @param ir IndividualReport
   * @return ImageCaptureMetadataType NISO element
   */
  private ImageCaptureMetadataType createNisoImageCaptureMetadata (IFD ifd, IndividualReport ir){

    ImageCaptureMetadataType imageCapture = new ImageCaptureMetadataType();

    //8.1 source information, we don't have sourceType and sourceId so still sourceSize
    ImageCaptureMetadataType.SourceInformation sourceInformation = new ImageCaptureMetadataType.SourceInformation();
    ImageCaptureMetadataType.SourceInformation.SourceSize sourceSize = new ImageCaptureMetadataType.SourceInformation.SourceSize();
    if (ifd.getMetadata().containsTagId(TiffTags.getTagId("XResolution")) && SourceDimensionUnitType.verifyTag(ifd.getMetadata().get("ResolutionUnit").toString())) {
      ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension sourceXDimension = new ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension();
      TypeOfNonNegativeRealType xDValue = new TypeOfNonNegativeRealType();
      xDValue.setUse("System");
      Rational xResolutionValue =  (Rational)(ifd.getMetadata().get("XResolution").getValue().get(0));
      xDValue.setValue(xResolutionValue.getFloatValue());
      sourceXDimension.setSourceXDimensionValue(xDValue);
      TypeOfsourceDimensionUnitType dimensionUnit = new TypeOfsourceDimensionUnitType();
      dimensionUnit.setUse("System");
      dimensionUnit.setValue(SourceDimensionUnitType.fromValue(ifd.getMetadata().get("ResolutionUnit").toString()));
      sourceXDimension.setSourceXDimensionUnit(dimensionUnit);
      sourceSize.setSourceXDimension(sourceXDimension);
    }
    if (ifd.getMetadata().containsTagId(TiffTags.getTagId("YResolution")) && SourceDimensionUnitType.verifyTag(ifd.getMetadata().get("ResolutionUnit").toString())) {
      ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension sourceYDimension = new ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension();
      TypeOfNonNegativeRealType yDValue = new TypeOfNonNegativeRealType();
      yDValue.setUse("System");
      Rational yResolutionValue =  (Rational)(ifd.getMetadata().get("YResolution").getValue().get(0));
      yDValue.setValue(yResolutionValue.getFloatValue());
      sourceYDimension.setSourceYDimensionValue(yDValue);
      TypeOfsourceDimensionUnitType dimensionUnit = new TypeOfsourceDimensionUnitType();
      dimensionUnit.setUse("System");
      dimensionUnit.setValue(SourceDimensionUnitType.fromValue(ifd.getMetadata().get("ResolutionUnit").toString()));
      sourceYDimension.setSourceYDimensionUnit(dimensionUnit);
      sourceSize.setSourceYDimension(sourceYDimension);
    }
    sourceInformation.setSourceSize(sourceSize);
    imageCapture.setSourceInformation(sourceInformation);

    //8.2 General capture information
    ImageCaptureMetadataType.GeneralCaptureInformation generalCaptureInformation = new ImageCaptureMetadataType.GeneralCaptureInformation();
    TypeOfDateType creationDate = new TypeOfDateType();
    creationDate.setUse("Manager");
    creationDate.setValue(getImageDate(ifd,ir.getFilePath()));
    generalCaptureInformation.setDateTimeCreated(creationDate);
    if (ifd.getMetadata().containsTagId(TiffTags.getTagId("Artist"))) {
      Ascii artist = (Ascii)ifd.getMetadata().get("Artist").getValue().get(0);
      StringType producer = new StringType();
      producer.setUse("Manager");
      producer.setValue(artist.toString());
      generalCaptureInformation.setImageProducer(producer);
    }
    imageCapture.setGeneralCaptureInformation(generalCaptureInformation);

    //8.4 Digital Camera Capture
    ImageCaptureMetadataType.DigitalCameraCapture digitalCameraCapture = new ImageCaptureMetadataType.DigitalCameraCapture();
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("Make"))){
      StringType manufacturer = new StringType();
      manufacturer.setUse("Manager");
      manufacturer.setValue(ifd.getMetadata().get("Make").toString());
      digitalCameraCapture.setDigitalCameraManufacturer(manufacturer);
    }
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("Model"))){
      ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel cameraModel = new ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel();
      StringType model = new StringType();
      model.setUse("Manager");
      model.setValue(ifd.getMetadata().get("Model").toString());
      cameraModel.setDigitalCameraModelName(model);
      digitalCameraCapture.setDigitalCameraModel(cameraModel);
    }
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("SensingMethod")) && CameraSensorType.verifyTag(ifd.getMetadata().get("SensingMethod").getValue().toString())){
      TypeOfCameraSensorType cameraSensor = new TypeOfCameraSensorType();
      cameraSensor.setUse("Manager");
      cameraSensor.setValue(CameraSensorType.fromValue(ifd.getMetadata().get("SensingMethod").getValue().toString()));
      digitalCameraCapture.setCameraSensor(cameraSensor);
    }

    //8.4.4 Camera Capture Settings
    //8.4.4.1 Image Data
    ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings cameraSettings = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings();
    ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData imageData = createNisoImageData(ir, ifd);
    cameraSettings.setImageData(imageData);

    //8.4.4.2 GPSData
    ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData gpsData = createNisoGPSData(ir);
    cameraSettings.setGPSData(gpsData);
    digitalCameraCapture.setCameraCaptureSettings(cameraSettings);
    imageCapture.setDigitalCameraCapture(digitalCameraCapture);

    //8.5 orientation
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("Orientation")) && OrientationType.verifyTag(ifd.getMetadata().get("Orientation").toString())){
      TypeOfOrientationType typeOfOrientationType = new TypeOfOrientationType();
      typeOfOrientationType.setUse("System");
      typeOfOrientationType.setValue(OrientationType.fromValue(ifd.getMetadata().get("Orientation").toString()));
      imageCapture.setOrientation(typeOfOrientationType);
    }

    return imageCapture;
  }

  /**
   * This method create a NISO element ImageAssessmentMetadataType from Baseline Tiff tags of a IFD as NISO chapter 9 standard
   * @param ifd IFD element from Tiff document
   * @return ImageAssessmentMetadataType NISO element
   */
  private ImageAssessmentMetadataType createNisoImageAssessmentMetadata (IFD ifd){

    ImageAssessmentMetadataType imageAssessmentMetadata = new ImageAssessmentMetadataType();
    //9.2 Image Color Encoding
    ImageAssessmentMetadataType.ImageColorEncoding imageColorEncoding = new ImageAssessmentMetadataType.ImageColorEncoding();

    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("BitsPerSample"))){
      ImageAssessmentMetadataType.ImageColorEncoding.BitsPerSample bitsPerSample = new ImageAssessmentMetadataType.ImageColorEncoding.BitsPerSample();
      List<abstractTiffType> bitsPerSampleList = ifd.getMetadata().get("BitsPerSample").getValue();
      Iterator<abstractTiffType> iteratorBitPerSample = bitsPerSampleList.iterator();
      while(iteratorBitPerSample.hasNext()){
        PositiveIntegerType bitPerSampleInv = new PositiveIntegerType();
        bitPerSampleInv.setUse("System");
        Short bitValue = (Short)iteratorBitPerSample.next();
        bitPerSampleInv.setValue(BigInteger.valueOf(bitValue.toInt()));
        bitsPerSample.setBitsPerSampleValue(bitPerSampleInv);
      }
      TypeOfBitsPerSampleUnitType bitsPerSampleUnit = new TypeOfBitsPerSampleUnitType();
      bitsPerSampleUnit.setUse("System");
      bitsPerSampleUnit.setValue(BitsPerSampleUnit.fromValue("integer"));
      bitsPerSample.setBitsPerSampleUnit(bitsPerSampleUnit);
      imageColorEncoding.setBitsPerSample(bitsPerSample);
    }
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("SamplesPerPixel"))) {
      PositiveIntegerType samplePerPixel = new PositiveIntegerType();
      samplePerPixel.setUse("System");
      samplePerPixel.setValue(new BigInteger(ifd.getMetadata().get("SamplesPerPixel").toString()));
      imageColorEncoding.setSamplesPerPixel(samplePerPixel);
    }
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("ExtraSamples")) && ExtraSamplesType.verifyTag(ifd.getMetadata().get("ExtraSamples").toString())) {
      TypeOfExtraSamplesType extraSampleType = new TypeOfExtraSamplesType();
      extraSampleType.setUse("System");
      extraSampleType.setValue(ExtraSamplesType.fromValue(ifd.getMetadata().get("ExtraSamples").toString()));
      imageColorEncoding.setExtraSamples(extraSampleType);
    }

    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("GrayResponseCurve")) || ifd.getMetadata().containsTagId(TiffTags.getTagId("GrayResponseUnit"))) {
      ImageAssessmentMetadataType.ImageColorEncoding.GrayResponse grayResponse = new ImageAssessmentMetadataType.ImageColorEncoding.GrayResponse();
      if(ifd.getMetadata().containsTagId(TiffTags.getTagId("GrayResponseCurve"))){
        List<abstractTiffType> responseCurveValues = ifd.getMetadata().get("GrayResponseCurve").getValue();
        Iterator<abstractTiffType> iteratorCurveValues = responseCurveValues.iterator();
        while(iteratorCurveValues.hasNext()){
          NonNegativeIntegerType grayResponseCurve = new NonNegativeIntegerType();
          grayResponseCurve.setUse("System");
          grayResponseCurve.setValue(BigInteger.valueOf(iteratorCurveValues.next().toInt()));
          grayResponse.setGrayResponseCurve(grayResponseCurve);
        }
      }
      if(ifd.getMetadata().containsTagId(TiffTags.getTagId("GrayResponseUnit")) && GrayResponseUnitType.verifyTag(ifd.getMetadata().get("GrayResponseUnit").toString())){
        TypeOfGrayResponseUnitType grayResponseUnit = new TypeOfGrayResponseUnitType();
        grayResponseUnit.setUse("System");
        grayResponseUnit.setValue(GrayResponseUnitType.fromValue(ifd.getMetadata().get("GrayResponseUnit").toString()));
        grayResponse.setGrayResponseUnit(grayResponseUnit);
      }
      imageColorEncoding.setGrayResponse(grayResponse);
    }
    if(ifd.getMetadata().containsTagId(TiffTags.getTagId("WhitePoint"))) {
      ImageAssessmentMetadataType.ImageColorEncoding.WhitePoint whitePoint = new ImageAssessmentMetadataType.ImageColorEncoding.WhitePoint();
      //x value
      RationalType whitePointXValue = new RationalType();
      whitePointXValue.setUse("System");
      Rational xvalueFromTag = (Rational)ifd.getMetadata().get("GrayResponseUnit").getValue().get(0);
      whitePointXValue.setDenominator(BigInteger.valueOf(xvalueFromTag.getDenominator()));
      whitePointXValue.setNumerator(BigInteger.valueOf(xvalueFromTag.getNumerator()));
      whitePoint.setWhitePointXValue(whitePointXValue);
      //y value
      RationalType whitePointYValue = new RationalType();
      whitePointXValue.setUse("System");
      Rational yvalueFromTag = (Rational)ifd.getMetadata().get("GrayResponseUnit").getValue().get(1);
      whitePointYValue.setDenominator(BigInteger.valueOf(yvalueFromTag.getDenominator()));
      whitePointYValue.setNumerator(BigInteger.valueOf(yvalueFromTag.getNumerator()));
      whitePoint.setWhitePointYValue(whitePointYValue);
      imageColorEncoding.setWhitePoint(whitePoint);
    }
    if (ifd.getMetadata().containsTagId(TiffTags.getTagId("PrimaryChromaticities"))){
      List<abstractTiffType> primaryChromasList = ifd.getMetadata().get("PrimaryChromaticities").getValue();
      ImageAssessmentMetadataType.ImageColorEncoding.PrimaryChromaticities primaryChromaticities = new ImageAssessmentMetadataType.ImageColorEncoding.PrimaryChromaticities();
      //red X
      RationalType primChromaRedX = new RationalType();
      primChromaRedX.setUse("System");
      Rational rational = (Rational)primaryChromasList.get(0);
      primChromaRedX.setNumerator(BigInteger.valueOf(rational.getNumerator()));
      primChromaRedX.setDenominator(BigInteger.valueOf(rational.getDenominator()));
      primaryChromaticities.setPrimaryChromaticitiesRedX(primChromaRedX);
      //red Y
      RationalType primChromaRedY = new RationalType();
      primChromaRedY.setUse("System");
      rational = (Rational)primaryChromasList.get(1);
      primChromaRedY.setNumerator(BigInteger.valueOf(rational.getNumerator()));
      primChromaRedY.setDenominator(BigInteger.valueOf(rational.getDenominator()));
      primaryChromaticities.setPrimaryChromaticitiesRedY(primChromaRedY);
      //Green X
      RationalType primChromaGreenX = new RationalType();
      primChromaGreenX.setUse("System");
      rational = (Rational)primaryChromasList.get(2);
      primChromaGreenX.setNumerator(BigInteger.valueOf(rational.getNumerator()));
      primChromaGreenX.setDenominator(BigInteger.valueOf(rational.getDenominator()));
      primaryChromaticities.setPrimaryChromaticitiesGreenX(primChromaGreenX);
      //Green Y
      RationalType primChromaGreenY = new RationalType();
      primChromaGreenY.setUse("System");
      rational = (Rational)primaryChromasList.get(3);
      primChromaGreenY.setNumerator(BigInteger.valueOf(rational.getNumerator()));
      primChromaGreenY.setDenominator(BigInteger.valueOf(rational.getDenominator()));
      primaryChromaticities.setPrimaryChromaticitiesGreenY(primChromaGreenY);
      //Blue X
      RationalType primChromaBlueX = new RationalType();
      primChromaBlueX.setUse("System");
      rational = (Rational)primaryChromasList.get(4);
      primChromaBlueX.setNumerator(BigInteger.valueOf(rational.getNumerator()));
      primChromaBlueX.setDenominator(BigInteger.valueOf(rational.getDenominator()));
      primaryChromaticities.setPrimaryChromaticitiesBlueX(primChromaBlueX);
      //Blue Y
      RationalType primChromaBlueY = new RationalType();
      primChromaBlueY.setUse("System");
      rational = (Rational)primaryChromasList.get(5);
      primChromaBlueY.setNumerator(BigInteger.valueOf(rational.getNumerator()));
      primChromaBlueY.setDenominator(BigInteger.valueOf(rational.getDenominator()));
      primaryChromaticities.setPrimaryChromaticitiesBlueY(primChromaBlueY);
      imageColorEncoding.setPrimaryChromaticities(primaryChromaticities);
    }
    imageAssessmentMetadata.setImageColorEncoding(imageColorEncoding);
    return imageAssessmentMetadata;

  }

  private MdSecType.MdWrap constructDigiProvMdWrap(IndividualReport ir, Configuration config){

    MdSecType.MdWrap mdwrap = new MdSecType.MdWrap();
    MdSecType.MdWrap.XmlData xmlData = new MdSecType.MdWrap.XmlData();
    mdwrap.setID("W" + mdwrap.hashCode());
    mdwrap.setMDTYPE("PREMIS");
    mdwrap.setMIMETYPE("text/xml");
    try{

      Date today = new Date();
      GregorianCalendar gregory = new GregorianCalendar();
      gregory.setTime(today);
      XMLGregorianCalendar calendar = DatatypeFactory.newInstance()
          .newXMLGregorianCalendar(
              gregory);
      mdwrap.setCREATED(calendar);

      Event premisObject = new Event();
      Event.EventIdentifier eventIdentifier= new Event.EventIdentifier();
      eventIdentifier.setEventIdentifierType("Validation");
      eventIdentifier.setEventIdentifierValue("Input_validation"); //TODO how to define it?
      premisObject.setEventIdentifier(eventIdentifier);
      premisObject.setEventType("Validation");
      premisObject.setEventDateTime(today.toString());
      premisObject.setEventDetail(config);
      Event.LinkingAgentIdentifier linkingAgentIdentifier = new Event.LinkingAgentIdentifier();
      linkingAgentIdentifier.setLinkingAgentIdentifierType("Software");
      linkingAgentIdentifier.setLinkingAgentIdentifierValue("DPF Manager " + DPFManagerProperties.getVersion());
      premisObject.setLinkingAgentIdentifier(linkingAgentIdentifier);
      Event.EventOutcomeInformation eventOutcomeInformation = new Event.EventOutcomeInformation();
      eventOutcomeInformation.setEventOutcome("Report output");
      Event.EventOutcomeInformation.EventOutcomeDetail eventOutcomeDetail = new Event.EventOutcomeInformation.EventOutcomeDetail();
      eventOutcomeDetail.setAny(null);
      eventOutcomeInformation.setEventOutcomeDetail(eventOutcomeDetail);
      premisObject.setEventOutcomeInformation(eventOutcomeInformation);


      xmlData.setEvent(premisObject);
      mdwrap.setXmlData(xmlData);
      return mdwrap;

    }catch (DatatypeConfigurationException e){

      e.printStackTrace();
      return mdwrap;
    }

  }

  /**
   * This method construct TechMdWrap from AmdSec of Mets.  This section includes technical metadata under NISO stardard
   * Info: numbers in method comments reference NISO Z39.87-2006 chapters
   * @param ir Individual Report
   * @return MdWrap element
   */
  private MdSecType.MdWrap constructTechMdWrap (IndividualReport ir){
    MdSecType.MdWrap mdwrap = new MdSecType.MdWrap();
    IFD ifd = ir.getTiffModel().getFirstIFD();
    String byteOrder = ir.getTiffModel().getEndianess().toString();
    mdwrap.setID("W" + mdwrap.hashCode());
    mdwrap.setMDTYPEVERSION("2.0");
    mdwrap.setMDTYPE("NISOIMG");
    mdwrap.setMIMETYPE("text/xml");

    try{

      Date today = new Date();
      GregorianCalendar gregory = new GregorianCalendar();
      gregory.setTime(today);
      XMLGregorianCalendar calendar = DatatypeFactory.newInstance()
          .newXMLGregorianCalendar(
              gregory);
      mdwrap.setCREATED(calendar);
    }catch (DatatypeConfigurationException e){

      e.printStackTrace();
      return mdwrap;
    }

    Mix mix =  new Mix();
    MdSecType.MdWrap.XmlData xmlData = new MdSecType.MdWrap.XmlData();

    while (ifd != null) {

      if (ifd.isImage() && !ifd.isThumbnail() && ifd.getMetadata() != null){

        /*
          6 Basic Digital Object Information
         */

        //ObjectIdentifier
        BasicDigitalObjectInformationType digitalObject = new BasicDigitalObjectInformationType();
        StringType objIdType = new StringType();
        objIdType.setUse("Manager");
        objIdType.setValue("DPF Manager hashcode");
        StringType objIddValue = new StringType();
        objIddValue.setUse("Manager");
        objIddValue.setValue(String.valueOf(ifd.hashCode()));
        BasicDigitalObjectInformationType.ObjectIdentifier objectId = new BasicDigitalObjectInformationType.ObjectIdentifier();
        objectId.setObjectIdentifierType(objIdType);
        objectId.setObjectIdentifierValue(objIddValue);

        //fileSize
        NonNegativeIntegerType fileSize = new NonNegativeIntegerType();
        fileSize.setUse("System");
        fileSize.setValue(BigInteger.valueOf(2 + (ifd.getTags().getTags().size() * 12) + 4));
        digitalObject.setFileSize(fileSize);

        //formatDesignation
        BasicDigitalObjectInformationType.FormatDesignation formatDesign = new BasicDigitalObjectInformationType.FormatDesignation();
        StringType formatType = new StringType();
        formatType.setUse("System");
        formatType.setValue("image/tiff");
        StringType formatVersion = new StringType();
        formatType.setUse("System");
        formatType.setValue("6.0");
        formatDesign.setFormatName(formatType);
        formatDesign.setFormatVersion(formatVersion);
        digitalObject.setFormatDesignation(formatDesign);

        //formatRegistry is optional
        BasicDigitalObjectInformationType.FormatRegistry formatReg = new BasicDigitalObjectInformationType.FormatRegistry();
        StringType formatReyKey = new StringType();
        formatType.setUse("System");
        formatType.setValue("TIFF/6.0");
        formatReg.setFormatRegistryKey(formatReyKey);
        digitalObject.setFormatRegistry(formatReg);

        //byteOrder
        if(ByteOrderType.verifyTag(byteOrder)) {
          TypeOfByteOrderType orderType = new TypeOfByteOrderType();
          orderType.setUse("System");
          orderType.setValue(ByteOrderType.fromValue(byteOrder));
          digitalObject.setByteOrder(orderType);
        }

        //compression
        if (ifd.getMetadata().containsTagId(TiffTags.getTagId("Compression"))) {
          BasicDigitalObjectInformationType.Compression compression = new BasicDigitalObjectInformationType.Compression();
          StringType comprScheme = new StringType();
          comprScheme.setUse("System");
          comprScheme.setValue(TiffConformanceChecker.compressionName(Integer.parseInt(ifd.getMetadata().get("Compression").toString())));
          compression.setCompressionScheme(comprScheme);
          digitalObject.setCompression(compression);
        }

        mix.setBasicDigitalObjectInformation(digitalObject);

        /**
         * 7 Basic Image Information
         */

        BasicImageInformationType basicImage = createNisoBasicImageInformation(ifd);
        mix.setBasicImageInformation(basicImage);

        /**
         * 8 Image Capture Metadata
         */
        ImageCaptureMetadataType imageCapture = createNisoImageCaptureMetadata(ifd, ir);
        if (imageCapture == null){
          return mdwrap;
        }
        mix.setImageCaptureMetadata(imageCapture);

        /**
         *  9 Image Assessment Metadata
         */
        ImageAssessmentMetadataType imageAssessmentMetadata = createNisoImageAssessmentMetadata(ifd);
        mix.setImageAssessmentMetadata(imageAssessmentMetadata);

        /**
         *  10 Change history
         */
        if (ifd.containsTagId(700)) { //XMP
          ChangeHistoryType changeHistoryType = new ChangeHistoryType();
          XMP xmp = (XMP)ifd.getTag("XMP").getValue().get(0);
          List<Hashtable<String,String>> xmpHistoryList = xmp.getHistory();
          Iterator<Hashtable<String,String>> iteratorXmpHistory = xmpHistoryList.iterator();
          while(iteratorXmpHistory.hasNext()){
            ChangeHistoryType.ImageProcessing imageProcessing = new ChangeHistoryType.ImageProcessing();
            Hashtable<String,String> xmpHistory = iteratorXmpHistory.next();
            if(xmpHistory.containsKey("when")){
              TypeOfDateType dateType = new TypeOfDateType();
              dateType.setUse("Manager");
              dateType.setValue(xmpHistory.get("when"));
              imageProcessing.setDateTimeProcessed(dateType);
            }
            if(xmpHistory.containsKey("softwareAgent")){
              ChangeHistoryType.ImageProcessing.ProcessingSoftware processingSoftware = new ChangeHistoryType.ImageProcessing.ProcessingSoftware();
              StringType softwareName = new StringType();
              softwareName.setUse("Manager");
              softwareName.setValue(xmpHistory.get("softwareAgent"));
              processingSoftware.setProcessingSoftwareName(softwareName);
              imageProcessing.setProcessingSoftware(processingSoftware);
            }
            if(xmpHistory.containsKey("action")){
              StringType action = new StringType();
              action.setUse("Manager");
              action.setValue(xmpHistory.get("action"));
              imageProcessing.setProcessingActions(action);
            }

            changeHistoryType.setImageProcessing(imageProcessing);
          }
          mix.setChangeHistory(changeHistoryType);
        }


      }
      ifd = ifd.getNextIFD();

    }
    xmlData.setMix(mix);
    mdwrap.setXmlData(xmlData);


    return mdwrap;

  }

  /**
   * This method generates Div tree from an Ifd
   * @param ifd ifd element from Tiff document
   * @param file FileType from Mets structure
   * @param startIfd Integer point to the first byte of the first Ifd from Tiff document
   * @return root Div
   */
  private DivType extractDivsFromIFD(IFD ifd, FileType file, int startIfd){

    int tags = 0;
    DivType div = new DivType();
    div.setID("I" + ifd.hashCode());
    div.setTYPE("IFD");
    if (ifd.isImage() && !ifd.isThumbnail()){
      div.setLABEL("Main Image");
    }else if(ifd.isImage() && ifd.isThumbnail()){
      div.setLABEL("Thumbail");
    }
    DivType.Fptr fptr = new DivType.Fptr();
    fptr.setID("f" + file.hashCode());
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
      TagValue auxTagValue =  iterator.next(); //we need it to work with it
      DivType divTag = new DivType();
      divTag.setID("Tag" + auxTagValue.hashCode());
      divTag.setTYPE("Tag");
      DivType.Fptr fptrT = new DivType.Fptr();
      fptrT.setID("f" + file.hashCode());
      fptrT.setFILEID(file);
      AreaType areaT = new AreaType();
      areaT.setID("a" + areaT.hashCode());
      areaT.setFILEID(file);
      areaT.setBEGIN(String.valueOf(auxTagValue.getTagOffset()));
      areaT.setEND(String.valueOf(auxTagValue.getTagOffset()+12));
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
      areaV.setBEGIN(String.valueOf(auxTagValue.getReadOffset()));
      areaV.setEND(String.valueOf(auxTagValue.getReadOffset() + auxTagValue.getReadlength()));
      fptrV.setArea(areaV);
      divValue.setFptr(fptrV);

      divTag.setDiv(divValue);
      div.setDiv(divTag);
    }
    area.setEND(String.valueOf(startIfd + ifd.getLength()));
    fptr.setArea(area);
    div.setFptr(fptr);
    return div;

  }
  /**
   * Creates a List of Streams from an Ifd
   *
   * @param ir the ir
   * @param ifd the ifd
   * @param index the index
   * @return the streamList
   */
  private List<FileType.Stream> extractStreamsFromIFD( IndividualReport ir, IFD ifd, int index) {

    List<FileType.Stream> streamList = new ArrayList<FileType.Stream>();

    // SubImage and extras
    boolean isThumbail = ifd.isThumbnail();

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
   * This method creates the element FileType from a Individual report to append to FileSec from METS
   * @param ir individual report
   * @return FileType file from report
   */
  private FileType constructFile(IndividualReport ir){
    int index = 0;

    IFD ifd = ir.getTiffModel().getFirstIFD();

    FileType file = new FileType();
    file.setID("F"+ir.hashCode());
    List<FileType.Stream> streams = new ArrayList<FileType.Stream>();

    while (ifd != null) {
      if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("Compression"))) {
        FileType.TransformFile transformFile = new FileType.TransformFile();
        transformFile.setID("T"+ifd.hashCode());
        int comp = Integer.parseInt(ifd.getMetadata().get("Compression").toString());
        String value = comp > 0 ? TiffConformanceChecker.compressionName(comp) : "Unknown";
        transformFile.setTRANSFORMALGORITHM(value);
        transformFile.setTRANSFORMTYPE("compression");
        file.setTransformFile(transformFile);
      }

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
   * @return element Mets report
   */
  private Mets buildReportIndividual(IndividualReport ir, Configuration config) {

    Mets mets = new Mets();

    if (ir.containsData()) {
      //mets properties
      mets.setOBJID("123456");
      mets.setLABEL("My title");
      mets.setTYPE("myType");
      mets.setPROFILE("myProfile");

      //mets Hdr
      MetsType.MetsHdr metsHdr = new MetsType.MetsHdr();
      metsHdr.setID("Hdr" + ir.hashCode());
      metsHdr.setRECORDSTATUS("Incoming");
      MetsType.MetsHdr.Agent agent = new MetsType.MetsHdr.Agent();
      agent.setROLE("CREATOR");
      agent.setID("A" + agent.hashCode());
      agent.setName("C. Reator");
      metsHdr.setAgent(agent);
      mets.setMetsHdr(metsHdr);


      //mets dmdSec
      MdSecType dmdSec = new MdSecType();
      dmdSec.setID("D" + ir.hashCode());
      dmdSec.setSTATUS("");
      dmdSec.setMdWrap(constructDmdMdWrap(ir));
      mets.setDmdSec(dmdSec);


      //mets amdSec
      AmdSecType amdsec = new AmdSecType();
      amdsec.setID("A" + ir.hashCode());
      MdSecType techMD = new MdSecType();
      techMD.setID("T" + techMD.hashCode());
      techMD.setMdWrap(constructTechMdWrap(ir));
      amdsec.setTechMD(techMD);

      MdSecType digiprovMD = new MdSecType();
      digiprovMD.setID("D" + digiprovMD.hashCode());
      digiprovMD.setMdWrap(constructDigiProvMdWrap(ir,config));
      amdsec.setDigiprovMD(digiprovMD);


      mets.setAmdSec(amdsec);

      //mets File Sec
      MetsType.FileSec filesec = new MetsType.FileSec();
      filesec.setID("F" + ir.hashCode());
      MetsType.FileSec.FileGrp filegroupMain = new MetsType.FileSec.FileGrp();
      filegroupMain.setID("F" + filegroupMain.hashCode());

      //append tiff structure as streams objects
      FileType file = constructFile(ir);
      filegroupMain.setFile(file);
      filesec.setFileGrp(filegroupMain);
      mets.setFileSec(filesec);

      //mets struct map
      StructMapType structMap = new StructMapType();
      structMap.setID("S" + ir.hashCode());
      TiffDocument tiffDocument = ir.getTiffModel();

      IFD ifd = tiffDocument.getFirstIFD();
      if (ifd != null) {
        DivType divHdr = new DivType();
        divHdr.setID("Hdr" + ifd.hashCode());
        divHdr.setTYPE("Header");
        structMap.setDiv(divHdr);
        int startIfd = 8;
        while (ifd != null) {
          DivType div = extractDivsFromIFD(ifd, file, startIfd);
          startIfd = ifd.getNextOffset();
          divHdr.setDiv(div);
          ifd = ifd.getNextIFD();
        }
        structMap.setDiv(divHdr);
        mets.setStructMap(structMap);
      }
    }
    return mets;

  }

  /**
   * Parse an individual report to XML Mets format.
   *
   * @param ir      the individual report.
   * @param config   the policy checker.
   * @return the XML Mets string generated.
   */
  public String parseIndividual(IndividualReport ir, Configuration config) {

    try {

      Mets mets = buildReportIndividual(ir, config);
      JAXBContext context =  JAXBContext.newInstance(Mets.class);

      Marshaller m = context.createMarshaller();
       //for pretty-print XML in JAXB
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      StringWriter sw = new StringWriter();
      m.marshal(mets, sw);

      //to String
      return sw.toString();

    } catch (JAXBException e) {
      e.printStackTrace();
      return "";
    }

  }
}
