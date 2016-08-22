package dpfmanager.conformancechecker.tiff.reporting;

import dpfmanager.conformancechecker.tiff.TiffConformanceChecker;
import dpfmanager.conformancechecker.tiff.policy_checker.Rules;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.BasicDigitalObjectInformationType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.BasicImageInformationType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.ByteOrderType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.CameraSensorType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.ComponentPhotometricInterpretationType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.ExposureProgramType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.FlashType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.ImageCaptureMetadataType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.LightSourceType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.MeteringModeType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.Mix;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.NonNegativeIntegerType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.PositiveIntegerType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.RationalType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.SourceDimensionUnitType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.StringType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfByteOrderType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfCameraSensorType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfComponentPhotometricInterpretationType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfDateType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfExifVersionType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfExposureProgramType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfFlashType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfLightSourceType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfMeteringModeType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfNonNegativeDecimalType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfNonNegativeRealType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfPositiveRealType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfSensingMethodType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfYCbCrPositioningType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfYCbCrSubsampleHorizType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfYCbCrSubsampleVertType;
import dpfmanager.conformancechecker.tiff.reporting.METS.niso.TypeOfsourceDimensionUnitType;
import dpfmanager.shell.modules.report.core.IndividualReport;
import dpfmanager.shell.modules.report.util.ReportHtml;

import dpfmanager.conformancechecker.tiff.reporting.METS.mets.*;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SplittableRandom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Created by Mar Llambí on 20/06/2016.
 */
public class MetsReport {

  //this is a temporary method
//  private boolean isThumbail(IFD ifd){
//    boolean isThumbail = false;
//    if (ifd.getTags().containsTagId(254) && BigInteger.valueOf(ifd.getTags().get(254).getFirstNumericValue()).testBit(0)) isThumbail = true;
//    if (ifd.getTags().containsTagId(255) && ifd.getTags().get(255).getFirstNumericValue() == 2) isThumbail = true;
//    if (ifd.hasSubIFD() && ifd.getImageSize() < ifd.getsubIFD().getImageSize()) isThumbail = true;
//
//    return isThumbail;
//  }

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
    if(metadata.get("title") != null){
      literal.setTitle(metadata.get("title").toString());
    }
    if(metadata.get("creator") != null){
      literal.setCreator(metadata.get("creator").toString());
    }
    if(metadata.get("Description") != null){
      literal.setDescription(metadata.get("Description").toString());
    }
    if(metadata.get("dateTime") != null){
      literal.setDate(metadata.get("dateTime").toString());
    }
    if(metadata.get("Copyright") != null){
      literal.setRights(metadata.get("Copyright").toString());
    }

    xmlData.setLiteral(literal);
    mdwrap.setXmlData(xmlData);

    return mdwrap;
  }

  private BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component constructComponentFromReferences (Rational headroom, Rational footroom){

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

  private String getResolutionUnit (String value){
    switch (value){
      case "0":
        return "";
      case "1":
        return "in.";
      case "2":
        return "mm";
    }
    return "";
  }

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

  private MdSecType.MdWrap constructTechMdWrap (IndividualReport ir){
    MdSecType.MdWrap mdwrap = new MdSecType.MdWrap();
    //TODO completar info mdwrap
    mdwrap.setID("W"+mdwrap.hashCode());
    IFD ifd = ir.getTiffModel().getFirstIFD();
    String byteOrder = ir.getTiffModel().getEndianess().toString();
    mdwrap.setID("W" + mdwrap.hashCode());
    mdwrap.setMDTYPEVERSION("2.0");
    mdwrap.setMDTYPE("NISOIMG");
    mdwrap.setMIMETYPE("text/xml");
    Mix mix =  new Mix();
    MdSecType.MdWrap.XmlData xmlData = new MdSecType.MdWrap.XmlData();

    while (ifd != null) {

      if (ifd.isImage() && !ifd.isThumbnail()){

        /*
          Basic Digital Object Information
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
        TypeOfByteOrderType orderType = new TypeOfByteOrderType();
        orderType.setUse("System");
        orderType.setValue(ByteOrderType.fromValue(byteOrder));
        digitalObject.setByteOrder(orderType);

        //compression
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("Compression"))) {
          BasicDigitalObjectInformationType.Compression compression = new BasicDigitalObjectInformationType.Compression();
          StringType comprScheme = new StringType();
          comprScheme.setUse("System");
          comprScheme.setValue(TiffConformanceChecker.compressionName(Integer.parseInt(ifd.getMetadata().get("Compression").toString())));
          compression.setCompressionScheme(comprScheme);
          digitalObject.setCompression(compression);
        }

        mix.setBasicDigitalObjectInformation(digitalObject);

        /**
         * Basic Image Information
         */

        //Basic Image characteristics
        BasicImageInformationType basicImage = new BasicImageInformationType();
        BasicImageInformationType.BasicImageCharacteristics imageCharac = new BasicImageInformationType.BasicImageCharacteristics();
        PositiveIntegerType width = new PositiveIntegerType();
        width.setUse("System");
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("ImageWidth"))) {
          width.setValue(new BigInteger(ifd.getMetadata().get("ImageWidth").toString()));
        }else{
          width.setValue(BigInteger.valueOf(0));
        }
        imageCharac.setImageWidth(width);
        PositiveIntegerType height = new PositiveIntegerType();
        height.setUse("System");
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("ImageLength"))) {
          height.setValue(new BigInteger(ifd.getMetadata().get("ImageLength").toString()));
        }else{
          height.setValue(BigInteger.valueOf(0));
        }
        imageCharac.setImageHeight(height);

        //photometricInterpreation
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("PhotometricInterpretation"))) {
          BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation photometricInterpretation = new BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation();
          StringType colorSpace = new StringType();
          colorSpace.setUse("System");
          int photo = Integer.parseInt(ifd.getMetadata().get("PhotometricInterpretation").toString());
          colorSpace.setValue(TiffConformanceChecker.photometricName(photo));
          photometricInterpretation.setColorSpace(colorSpace);

          //Icc profile
          if (ifd.getMetadata() != null && ifd.getMetadata().get("ICCProfile") != null) {
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

          //YcbCr image
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
            BasicImageInformationType.BasicImageCharacteristics.PhotometricInterpretation.ReferenceBlackWhite.Component component = constructComponentFromReferences(headroom, footroom);
            referenceBlackWhite.setComponent(component);
            headroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(2);
            footroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(3);
            component = constructComponentFromReferences(headroom, footroom);
            referenceBlackWhite.setComponent(component);
            headroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(4);
            footroom = (Rational)ifd.getMetadata().get("ReferenceBlackWhite").getValue().get(5);
            component = constructComponentFromReferences(headroom, footroom);
            referenceBlackWhite.setComponent(component);

            photometricInterpretation.setReferenceBlackWhite(referenceBlackWhite);

          }

          imageCharac.setPhotometricInterpretation(photometricInterpretation);
        }
        basicImage.setBasicImageCharacteristics(imageCharac);
        mix.setBasicImageInformation(basicImage);

        /**
         * Image Capture Metadata
         */
        ImageCaptureMetadataType imageCapture = new ImageCaptureMetadataType();

        //source information, we don't have sourceType and sourceId so still sourceSize
        ImageCaptureMetadataType.SourceInformation sourceInformation = new ImageCaptureMetadataType.SourceInformation();
        ImageCaptureMetadataType.SourceInformation.SourceSize sourceSize = new ImageCaptureMetadataType.SourceInformation.SourceSize();
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("XResolution"))) {
          ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension sourceXDimension = new ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension();
          TypeOfNonNegativeRealType xDValue = new TypeOfNonNegativeRealType();
          xDValue.setUse("System");
          Rational xResolutionValue =  (Rational)(ifd.getMetadata().get("XResolution").getValue().get(0));
          xDValue.setValue(xResolutionValue.getFloatValue());
          sourceXDimension.setSourceXDimensionValue(xDValue);
          TypeOfsourceDimensionUnitType dimensionUnit = new TypeOfsourceDimensionUnitType();
          dimensionUnit.setUse("System");
          dimensionUnit.setValue(SourceDimensionUnitType.fromValue(getResolutionUnit(ifd.getMetadata().get("ResolutionUnit").toString())));
          sourceXDimension.setSourceXDimensionUnit(dimensionUnit);
          sourceSize.setSourceXDimension(sourceXDimension);
        }
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("YResolution"))) {
          ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension sourceYDimension = new ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension();
          TypeOfNonNegativeRealType yDValue = new TypeOfNonNegativeRealType();
          yDValue.setUse("System");
          Rational yResolutionValue =  (Rational)(ifd.getMetadata().get("YResolution").getValue().get(0));
          yDValue.setValue(yResolutionValue.getFloatValue());
          sourceYDimension.setSourceYDimensionValue(yDValue);
          TypeOfsourceDimensionUnitType dimensionUnit = new TypeOfsourceDimensionUnitType();
          dimensionUnit.setUse("System");
          dimensionUnit.setValue(SourceDimensionUnitType.fromValue(getResolutionUnit(ifd.getMetadata().get("ResolutionUnit").toString())));
          sourceYDimension.setSourceYDimensionUnit(dimensionUnit);
          sourceSize.setSourceYDimension(sourceYDimension);
        }
        sourceInformation.setSourceSize(sourceSize);
        imageCapture.setSourceInformation(sourceInformation);

        //General capture information
        ImageCaptureMetadataType.GeneralCaptureInformation generalCaptureInformation = new ImageCaptureMetadataType.GeneralCaptureInformation();
        TypeOfDateType creationDate = new TypeOfDateType();
        creationDate.setUse("Manager");
        creationDate.setValue(getImageDate(ifd,ir.getFilePath()));
        generalCaptureInformation.setDateTimeCreated(creationDate);
        if (ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("Artist"))) {
          Ascii artist = (Ascii)ifd.getMetadata().get("Artist").getValue().get(0);
          StringType producer = new StringType();
          producer.setUse("Manager");
          producer.setValue(artist.toString());
          generalCaptureInformation.setImageProducer(producer);
        }
        imageCapture.setGeneralCaptureInformation(generalCaptureInformation);

        //Digital Camera Capture
        ImageCaptureMetadataType.DigitalCameraCapture digitalCameraCapture = new ImageCaptureMetadataType.DigitalCameraCapture();
        if(ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("Make"))){
          StringType manufacturer = new StringType();
          manufacturer.setUse("Manager");
          manufacturer.setValue(ifd.getMetadata().get("Make").getValue().toString());
          digitalCameraCapture.setDigitalCameraManufacturer(manufacturer);
        }
        if(ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("Model"))){
          ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel cameraModel = new ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel();
          StringType model = new StringType();
          model.setUse("Manager");
          model.setValue(ifd.getMetadata().get("Model").getValue().toString());
          cameraModel.setDigitalCameraModelName(model);
          digitalCameraCapture.setDigitalCameraModel(cameraModel);
        }
        if(ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("SensingMethod"))){
          TypeOfCameraSensorType cameraSensor = new TypeOfCameraSensorType();
          cameraSensor.setUse("Manager");
          cameraSensor.setValue(CameraSensorType.fromValue(ifd.getMetadata().get("SensingMethod").getValue().toString()));
          digitalCameraCapture.setCameraSensor(cameraSensor);
        }

        //Camera Capture Settings
        //Image Data
        ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings cameraSettings = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings();
        ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData imageData = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData();
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("FNumber")){
          TypeOfNonNegativeRealType fnumber = new TypeOfNonNegativeRealType();
          fnumber.setUse("Manager");
          Rational fnumberValue = (Rational)ir.getTiffModel().getMetadata().get("FNumber");
          fnumber.setValue(fnumberValue.getFloatValue());
          imageData.setFNumber(fnumber);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("ExposureTime")){
          TypeOfNonNegativeRealType exposureTime = new TypeOfNonNegativeRealType();
          exposureTime.setUse("Manager");
          Rational exposure = (Rational)ir.getTiffModel().getMetadata().get("ExposureTime");
          exposureTime.setValue(exposure.getFloatValue());
          imageData.setExposureTime(exposureTime);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("ExposureProgram")){
          TypeOfExposureProgramType exposureProgram = new TypeOfExposureProgramType();
          exposureProgram.setUse("Manager");
          exposureProgram.setValue(ExposureProgramType.fromValue(ir.getTiffModel().getMetadata().get("ExposureProgram").toString()));
          imageData.setExposureProgram(exposureProgram);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("SpectralSensitivity")){
          StringType spectralSensivity = new StringType();
          spectralSensivity.setUse("Manager");
          spectralSensivity.setValue(ir.getTiffModel().getMetadata().get("SpectralSensitivity").toString());
          imageData.setSpectralSensitivity(spectralSensivity);

        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("ISOSpeedRatings")){
          PositiveIntegerType iosSpeed = new PositiveIntegerType();
          iosSpeed.setUse("Manager");
          Short speedRatingValue = (Short)ir.getTiffModel().getMetadata().get("ISOSpeedRatings");
          iosSpeed.setValue(BigInteger.valueOf(speedRatingValue.getValue()));
          imageData.setIsoSpeedRatings(iosSpeed);
        }
        TypeOfExifVersionType exifVersion = new TypeOfExifVersionType();
        exifVersion.setUse("System");
        if(ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("ExifVersion"))){
          exifVersion.setValue(ifd.getMetadata().get("ExifVersion").getValue().toString());
        }else if (ifd.containsTagId(34665)){ //If contains EXIF but version is not included
          exifVersion.setValue("0220");
        }
        imageData.setExifVersion(exifVersion);
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("ShutterSpeedValue")){
          RationalType shutterSpeedValue = new RationalType();
          shutterSpeedValue.setUse("Manager");
          SRational speedTagValue = (SRational)ir.getTiffModel().getMetadata().get("ShutterSpeedValue");
          shutterSpeedValue.setDenominator(BigInteger.valueOf(speedTagValue.getDenominator()));
          shutterSpeedValue.setNumerator(BigInteger.valueOf(speedTagValue.getNumerator()));
          imageData.setShutterSpeedValue(shutterSpeedValue);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("ApertureValue")){
          RationalType apertureValue = new RationalType();
          apertureValue.setUse("Manager");
          Rational apertureTagValue = (Rational)ir.getTiffModel().getMetadata().get("ApertureValue");
          apertureValue.setDenominator(BigInteger.valueOf(apertureTagValue.getDenominator()));
          apertureValue.setNumerator(BigInteger.valueOf(apertureTagValue.getNumerator()));
          imageData.setApertureValue(apertureValue);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("BrightnessValue")){
          RationalType bightnessValue = new RationalType();
          bightnessValue.setUse("Manager");
          SRational brightnessTagValue = (SRational)ir.getTiffModel().getMetadata().get("BrightnessValue");
          bightnessValue.setDenominator(BigInteger.valueOf(brightnessTagValue.getDenominator()));
          bightnessValue.setNumerator(BigInteger.valueOf(brightnessTagValue.getNumerator()));
          imageData.setBrightnessValue(bightnessValue);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("ExposureBiasValue")){
          RationalType exposureBias = new RationalType();
          exposureBias.setUse("Manager");
          SRational exposureTagValue = (SRational)ir.getTiffModel().getMetadata().get("ExposureBiasValue");
          exposureBias.setDenominator(BigInteger.valueOf(exposureTagValue.getDenominator()));
          exposureBias.setNumerator(BigInteger.valueOf(exposureTagValue.getNumerator()));
          imageData.setExposureBiasValue(exposureBias);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("MaxApertureValue")){
          RationalType maxAperture = new RationalType();
          maxAperture.setUse("Manager");
          SRational maxApertureTagValue = (SRational)ir.getTiffModel().getMetadata().get("MaxApertureValue");
          maxAperture.setDenominator(BigInteger.valueOf(maxApertureTagValue.getDenominator()));
          maxAperture.setNumerator(BigInteger.valueOf(maxApertureTagValue.getNumerator()));
          imageData.setMaxApertureValue(maxAperture);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("SubjectDistance")){
          ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance subjectDistance = new ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance();
          TypeOfNonNegativeDecimalType distance = new TypeOfNonNegativeDecimalType();
          distance.setUse("Manager");
          Long subjectLongValue = (Long)ir.getTiffModel().getMetadata().get("SubjectDistance");
          distance.setValue(BigDecimal.valueOf(subjectLongValue.getValue()));
          subjectDistance.setDistance(distance);
          imageData.setSubjectDistance(subjectDistance);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("MeteringMode")){
          TypeOfMeteringModeType meteringModeType = new TypeOfMeteringModeType();
          meteringModeType.setUse("Manager");
          meteringModeType.setValue(MeteringModeType.fromValue(ir.getTiffModel().getMetadata().get("MeteringMode").toString()));
          imageData.setMeteringMode(meteringModeType);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("LightSource")){
          TypeOfLightSourceType lightSourceType = new TypeOfLightSourceType();
          lightSourceType.setUse("Manager");
          lightSourceType.setValue(LightSourceType.fromValue(ir.getTiffModel().getMetadata().get("LightSource").toString()));
          imageData.setLightSource(lightSourceType);
        }
        if(ifd.getMetadata() != null && ir.getTiffModel().getMetadata().contains("Flash")){
          TypeOfFlashType flashType = new TypeOfFlashType();
          flashType.setUse("Manager");
          Integer hexFlashInt = Integer.parseInt(ir.getTiffModel().getMetadata().get("Flash").toString());
          String hexFlash = String.format("%04X", hexFlashInt);
          flashType.setValue(FlashType.fromValue(hexFlash));
          imageData.setFlash(flashType);
        }
        if(ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("FlashEnergy"))){
          RationalType flashEnergy = new RationalType();
          flashEnergy.setUse("Manager");
          Rational flashEnergyTagValue = (Rational)ifd.getMetadata().get("FlashEnergy").getValue().get(0);
          flashEnergy.setDenominator(BigInteger.valueOf(flashEnergyTagValue.getDenominator()));
          flashEnergy.setNumerator(BigInteger.valueOf(flashEnergyTagValue.getNumerator()));
          imageData.setFlashEnergy(flashEnergy);
        }
        //TODO blackLight
        if(ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("ExposureIndex"))){
          TypeOfPositiveRealType exposureIndex = new TypeOfPositiveRealType();
          exposureIndex.setUse("Manager");
          Rational exposureTagValue = (Rational)ifd.getMetadata().get("ExposureIndex").getValue().get(0);
          exposureIndex.setValue(exposureTagValue.getFloatValue());
          imageData.setExposureIndex(exposureIndex);
        }
//        if(ifd.getMetadata() != null && ifd.getMetadata().containsTagId(TiffTags.getTagId("SensingMethod"))){
//          TypeOfSensingMethodType exposureIndex = new TypeOfSensingMethodType();
//          exposureIndex.setUse("Manager");
//          Rational exposureTagValue = (Rational)ifd.getMetadata().get("ExposureIndex").getValue().get(0);
//          exposureIndex.setValue(exposureTagValue.getFloatValue());
//          imageData.setSensingMethod();
//        }

        cameraSettings.setImageData(imageData);
        digitalCameraCapture.setCameraCaptureSettings(cameraSettings);
        imageCapture.setDigitalCameraCapture(digitalCameraCapture);
        mix.setImageCaptureMetadata(imageCapture);



      }
      ifd = ifd.getNextIFD();

    }
    xmlData.setMix(mix);
    mdwrap.setXmlData(xmlData);


    return mdwrap;

  }

  //try to do it recursive
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
   * @return the element
   */
  private Mets buildReportIndividual(IndividualReport ir, Rules rules) {

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
      mets.setAmdSec(amdsec);

      //mets File Sec
      MetsType.FileSec filesec = new MetsType.FileSec();
      filesec.setID("F" + ir.hashCode());
      MetsType.FileSec.FileGrp filegroupMain = new MetsType.FileSec.FileGrp();
      filegroupMain.setID("F" + filegroupMain.hashCode());

      //append tiff structure as streams objects
      FileType file = createFile(ir);
      filegroupMain.setFile(file);
      filesec.setFileGrp(filegroupMain);
      mets.setFileSec(filesec);

      //mets struct map
      StructMapType structMap = new StructMapType();
      structMap.setID("S" + ir.hashCode());
      TiffDocument tiffDocument = ir.getTiffModel();

      IFD ifd = tiffDocument.getFirstIFD();
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
