//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.15 a las 01:54:03 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ImageCaptureMetadataType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ImageCaptureMetadataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SourceInformation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="sourceType" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="SourceID" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="sourceIDType" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="sourceIDValue" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="SourceSize" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="SourceXDimension" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="sourceXDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
 *                                       &lt;element name="sourceXDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="SourceYDimension" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="sourceYDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
 *                                       &lt;element name="sourceYDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="SourceZDimension" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="sourceZDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
 *                                       &lt;element name="sourceZDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="GeneralCaptureInformation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="dateTimeCreated" type="{http://www.loc.gov/mix/v20}typeOfDateType" minOccurs="0"/>
 *                   &lt;element name="imageProducer" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="captureDevice" type="{http://www.loc.gov/mix/v20}typeOfCaptureDeviceType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ScannerCapture" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="scannerManufacturer" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="ScannerModel" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="scannerModelName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="scannerModelNumber" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="scannerModelSerialNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="MaximumOpticalResolution" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="xOpticalResolution" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                             &lt;element name="yOpticalResolution" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                             &lt;element name="opticalResolutionUnit" type="{http://www.loc.gov/mix/v20}typeOfOpticalResolutionUnitType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="scannerSensor" type="{http://www.loc.gov/mix/v20}typeOfScannerSensorType" minOccurs="0"/>
 *                   &lt;element name="ScanningSystemSoftware" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="scanningSoftwareName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="scanningSoftwareVersionNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DigitalCameraCapture" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="digitalCameraManufacturer" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                   &lt;element name="DigitalCameraModel" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="digitalCameraModelName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="digitalCameraModelNumber" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                             &lt;element name="digitalCameraModelSerialNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="cameraSensor" type="{http://www.loc.gov/mix/v20}typeOfCameraSensorType" minOccurs="0"/>
 *                   &lt;element name="CameraCaptureSettings" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ImageData" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="fNumber" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
 *                                       &lt;element name="exposureTime" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
 *                                       &lt;element name="exposureProgram" type="{http://www.loc.gov/mix/v20}typeOfExposureProgramType" minOccurs="0"/>
 *                                       &lt;element name="spectralSensitivity" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
 *                                       &lt;element name="isoSpeedRatings" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
 *                                       &lt;element name="oECF" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="exifVersion" type="{http://www.loc.gov/mix/v20}typeOfExifVersionType" minOccurs="0"/>
 *                                       &lt;element name="shutterSpeedValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="apertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="brightnessValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="exposureBiasValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="maxApertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="SubjectDistance" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="distance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
 *                                                 &lt;element name="MinMaxDistance" minOccurs="0">
 *                                                   &lt;complexType>
 *                                                     &lt;complexContent>
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                         &lt;sequence>
 *                                                           &lt;element name="minDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
 *                                                           &lt;element name="maxDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
 *                                                         &lt;/sequence>
 *                                                       &lt;/restriction>
 *                                                     &lt;/complexContent>
 *                                                   &lt;/complexType>
 *                                                 &lt;/element>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="meteringMode" type="{http://www.loc.gov/mix/v20}typeOfMeteringModeType" minOccurs="0"/>
 *                                       &lt;element name="lightSource" type="{http://www.loc.gov/mix/v20}typeOfLightSourceType" minOccurs="0"/>
 *                                       &lt;element name="flash" type="{http://www.loc.gov/mix/v20}typeOfFlashType" minOccurs="0"/>
 *                                       &lt;element name="focalLength" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
 *                                       &lt;element name="flashEnergy" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="backLight" type="{http://www.loc.gov/mix/v20}typeOfBackLightType" minOccurs="0"/>
 *                                       &lt;element name="exposureIndex" type="{http://www.loc.gov/mix/v20}typeOfPositiveRealType" minOccurs="0"/>
 *                                       &lt;element name="sensingMethod" type="{http://www.loc.gov/mix/v20}typeOfSensingMethodType" minOccurs="0"/>
 *                                       &lt;element name="cfaPattern" type="{http://www.loc.gov/mix/v20}integerType" minOccurs="0"/>
 *                                       &lt;element name="autoFocus" type="{http://www.loc.gov/mix/v20}typeOfAutoFocusType" minOccurs="0"/>
 *                                       &lt;element name="PrintAspectRatio" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="xPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
 *                                                 &lt;element name="yPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="GPSData" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="gpsVersionID" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="gpsLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLatitudeRefType" minOccurs="0"/>
 *                                       &lt;element name="GPSLatitude" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="gpsLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLongitudeRefType" minOccurs="0"/>
 *                                       &lt;element name="GPSLongitude" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="gpsAltitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsAltitudeRefType" minOccurs="0"/>
 *                                       &lt;element name="gpsAltitude" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="gpsTimeStamp" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="gpsSatellites" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="gpsStatus" type="{http://www.loc.gov/mix/v20}typeOfgpsStatusType" minOccurs="0"/>
 *                                       &lt;element name="gpsMeasureMode" type="{http://www.loc.gov/mix/v20}typeOfgpsMeasureModeType" minOccurs="0"/>
 *                                       &lt;element name="gpsDOP" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="gpsSpeedRef" type="{http://www.loc.gov/mix/v20}typeOfgpsSpeedRefType" minOccurs="0"/>
 *                                       &lt;element name="gpsSpeed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="gpsTrackRef" type="{http://www.loc.gov/mix/v20}typeOfgpsTrackRefType" minOccurs="0"/>
 *                                       &lt;element name="gpsTrack" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="gpsImgDirectionRef" type="{http://www.loc.gov/mix/v20}typeOfgpsImgDirectionRefType" minOccurs="0"/>
 *                                       &lt;element name="gpsImgDirection" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="gpsMapDatum" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="gpsDestLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLatitudeRefType" minOccurs="0"/>
 *                                       &lt;element name="GPSDestLatitude" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="gpsDestLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLongitudeRefType" minOccurs="0"/>
 *                                       &lt;element name="GPSDestLongitude" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="gpsDestBearingRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestBearingRefType" minOccurs="0"/>
 *                                       &lt;element name="gpsDestBearing" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="gpsDestDistanceRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestDistanceRefType" minOccurs="0"/>
 *                                       &lt;element name="gpsDestDistance" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
 *                                       &lt;element name="gpsProcessingMethod" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="gpsAreaInformation" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *                                       &lt;element name="gpsDateStamp" type="{http://www.loc.gov/mix/v20}dateType" minOccurs="0"/>
 *                                       &lt;element name="gpsDifferential" type="{http://www.loc.gov/mix/v20}typeOfgpsDifferentialType" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="orientation" type="{http://www.loc.gov/mix/v20}typeOfOrientationType" minOccurs="0"/>
 *         &lt;element name="methodology" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImageCaptureMetadataType", propOrder = {
    "sourceInformation",
    "generalCaptureInformation",
    "scannerCapture",
    "digitalCameraCapture",
    "orientation",
    "methodology"
})
public class ImageCaptureMetadataType {

    @XmlElement(name = "SourceInformation")
    protected ImageCaptureMetadataType.SourceInformation sourceInformation;
    @XmlElement(name = "GeneralCaptureInformation")
    protected ImageCaptureMetadataType.GeneralCaptureInformation generalCaptureInformation;
    @XmlElement(name = "ScannerCapture")
    protected ImageCaptureMetadataType.ScannerCapture scannerCapture;
    @XmlElement(name = "DigitalCameraCapture")
    protected ImageCaptureMetadataType.DigitalCameraCapture digitalCameraCapture;
    protected TypeOfOrientationType orientation;
    protected StringType methodology;

    /**
     * Obtiene el valor de la propiedad sourceInformation.
     * 
     * @return
     *     possible object is
     *     {@link ImageCaptureMetadataType.SourceInformation }
     *     
     */
    public ImageCaptureMetadataType.SourceInformation getSourceInformation() {
        return sourceInformation;
    }

    /**
     * Define el valor de la propiedad sourceInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageCaptureMetadataType.SourceInformation }
     *     
     */
    public void setSourceInformation(ImageCaptureMetadataType.SourceInformation value) {
        this.sourceInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad generalCaptureInformation.
     * 
     * @return
     *     possible object is
     *     {@link ImageCaptureMetadataType.GeneralCaptureInformation }
     *     
     */
    public ImageCaptureMetadataType.GeneralCaptureInformation getGeneralCaptureInformation() {
        return generalCaptureInformation;
    }

    /**
     * Define el valor de la propiedad generalCaptureInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageCaptureMetadataType.GeneralCaptureInformation }
     *     
     */
    public void setGeneralCaptureInformation(ImageCaptureMetadataType.GeneralCaptureInformation value) {
        this.generalCaptureInformation = value;
    }

    /**
     * Obtiene el valor de la propiedad scannerCapture.
     * 
     * @return
     *     possible object is
     *     {@link ImageCaptureMetadataType.ScannerCapture }
     *     
     */
    public ImageCaptureMetadataType.ScannerCapture getScannerCapture() {
        return scannerCapture;
    }

    /**
     * Define el valor de la propiedad scannerCapture.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageCaptureMetadataType.ScannerCapture }
     *     
     */
    public void setScannerCapture(ImageCaptureMetadataType.ScannerCapture value) {
        this.scannerCapture = value;
    }

    /**
     * Obtiene el valor de la propiedad digitalCameraCapture.
     * 
     * @return
     *     possible object is
     *     {@link ImageCaptureMetadataType.DigitalCameraCapture }
     *     
     */
    public ImageCaptureMetadataType.DigitalCameraCapture getDigitalCameraCapture() {
        return digitalCameraCapture;
    }

    /**
     * Define el valor de la propiedad digitalCameraCapture.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageCaptureMetadataType.DigitalCameraCapture }
     *     
     */
    public void setDigitalCameraCapture(ImageCaptureMetadataType.DigitalCameraCapture value) {
        this.digitalCameraCapture = value;
    }

    /**
     * Obtiene el valor de la propiedad orientation.
     * 
     * @return
     *     possible object is
     *     {@link TypeOfOrientationType }
     *     
     */
    public TypeOfOrientationType getOrientation() {
        return orientation;
    }

    /**
     * Define el valor de la propiedad orientation.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeOfOrientationType }
     *     
     */
    public void setOrientation(TypeOfOrientationType value) {
        this.orientation = value;
    }

    /**
     * Obtiene el valor de la propiedad methodology.
     * 
     * @return
     *     possible object is
     *     {@link StringType }
     *     
     */
    public StringType getMethodology() {
        return methodology;
    }

    /**
     * Define el valor de la propiedad methodology.
     * 
     * @param value
     *     allowed object is
     *     {@link StringType }
     *     
     */
    public void setMethodology(StringType value) {
        this.methodology = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="digitalCameraManufacturer" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="DigitalCameraModel" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="digitalCameraModelName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="digitalCameraModelNumber" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="digitalCameraModelSerialNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="cameraSensor" type="{http://www.loc.gov/mix/v20}typeOfCameraSensorType" minOccurs="0"/>
     *         &lt;element name="CameraCaptureSettings" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ImageData" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="fNumber" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
     *                             &lt;element name="exposureTime" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
     *                             &lt;element name="exposureProgram" type="{http://www.loc.gov/mix/v20}typeOfExposureProgramType" minOccurs="0"/>
     *                             &lt;element name="spectralSensitivity" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
     *                             &lt;element name="isoSpeedRatings" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                             &lt;element name="oECF" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="exifVersion" type="{http://www.loc.gov/mix/v20}typeOfExifVersionType" minOccurs="0"/>
     *                             &lt;element name="shutterSpeedValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="apertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="brightnessValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="exposureBiasValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="maxApertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="SubjectDistance" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="distance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
     *                                       &lt;element name="MinMaxDistance" minOccurs="0">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;sequence>
     *                                                 &lt;element name="minDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
     *                                                 &lt;element name="maxDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
     *                                               &lt;/sequence>
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="meteringMode" type="{http://www.loc.gov/mix/v20}typeOfMeteringModeType" minOccurs="0"/>
     *                             &lt;element name="lightSource" type="{http://www.loc.gov/mix/v20}typeOfLightSourceType" minOccurs="0"/>
     *                             &lt;element name="flash" type="{http://www.loc.gov/mix/v20}typeOfFlashType" minOccurs="0"/>
     *                             &lt;element name="focalLength" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
     *                             &lt;element name="flashEnergy" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="backLight" type="{http://www.loc.gov/mix/v20}typeOfBackLightType" minOccurs="0"/>
     *                             &lt;element name="exposureIndex" type="{http://www.loc.gov/mix/v20}typeOfPositiveRealType" minOccurs="0"/>
     *                             &lt;element name="sensingMethod" type="{http://www.loc.gov/mix/v20}typeOfSensingMethodType" minOccurs="0"/>
     *                             &lt;element name="cfaPattern" type="{http://www.loc.gov/mix/v20}integerType" minOccurs="0"/>
     *                             &lt;element name="autoFocus" type="{http://www.loc.gov/mix/v20}typeOfAutoFocusType" minOccurs="0"/>
     *                             &lt;element name="PrintAspectRatio" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="xPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
     *                                       &lt;element name="yPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="GPSData" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="gpsVersionID" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="gpsLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLatitudeRefType" minOccurs="0"/>
     *                             &lt;element name="GPSLatitude" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="gpsLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLongitudeRefType" minOccurs="0"/>
     *                             &lt;element name="GPSLongitude" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="gpsAltitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsAltitudeRefType" minOccurs="0"/>
     *                             &lt;element name="gpsAltitude" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="gpsTimeStamp" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="gpsSatellites" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="gpsStatus" type="{http://www.loc.gov/mix/v20}typeOfgpsStatusType" minOccurs="0"/>
     *                             &lt;element name="gpsMeasureMode" type="{http://www.loc.gov/mix/v20}typeOfgpsMeasureModeType" minOccurs="0"/>
     *                             &lt;element name="gpsDOP" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="gpsSpeedRef" type="{http://www.loc.gov/mix/v20}typeOfgpsSpeedRefType" minOccurs="0"/>
     *                             &lt;element name="gpsSpeed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="gpsTrackRef" type="{http://www.loc.gov/mix/v20}typeOfgpsTrackRefType" minOccurs="0"/>
     *                             &lt;element name="gpsTrack" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="gpsImgDirectionRef" type="{http://www.loc.gov/mix/v20}typeOfgpsImgDirectionRefType" minOccurs="0"/>
     *                             &lt;element name="gpsImgDirection" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="gpsMapDatum" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="gpsDestLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLatitudeRefType" minOccurs="0"/>
     *                             &lt;element name="GPSDestLatitude" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="gpsDestLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLongitudeRefType" minOccurs="0"/>
     *                             &lt;element name="GPSDestLongitude" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="gpsDestBearingRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestBearingRefType" minOccurs="0"/>
     *                             &lt;element name="gpsDestBearing" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="gpsDestDistanceRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestDistanceRefType" minOccurs="0"/>
     *                             &lt;element name="gpsDestDistance" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
     *                             &lt;element name="gpsProcessingMethod" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="gpsAreaInformation" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                             &lt;element name="gpsDateStamp" type="{http://www.loc.gov/mix/v20}dateType" minOccurs="0"/>
     *                             &lt;element name="gpsDifferential" type="{http://www.loc.gov/mix/v20}typeOfgpsDifferentialType" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "digitalCameraManufacturer",
        "digitalCameraModel",
        "cameraSensor",
        "cameraCaptureSettings"
    })
    public static class DigitalCameraCapture {

        protected StringType digitalCameraManufacturer;
        @XmlElement(name = "DigitalCameraModel")
        protected ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel digitalCameraModel;
        protected TypeOfCameraSensorType cameraSensor;
        @XmlElement(name = "CameraCaptureSettings")
        protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings cameraCaptureSettings;

        /**
         * Obtiene el valor de la propiedad digitalCameraManufacturer.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getDigitalCameraManufacturer() {
            return digitalCameraManufacturer;
        }

        /**
         * Define el valor de la propiedad digitalCameraManufacturer.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setDigitalCameraManufacturer(StringType value) {
            this.digitalCameraManufacturer = value;
        }

        /**
         * Obtiene el valor de la propiedad digitalCameraModel.
         * 
         * @return
         *     possible object is
         *     {@link ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel }
         *     
         */
        public ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel getDigitalCameraModel() {
            return digitalCameraModel;
        }

        /**
         * Define el valor de la propiedad digitalCameraModel.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel }
         *     
         */
        public void setDigitalCameraModel(ImageCaptureMetadataType.DigitalCameraCapture.DigitalCameraModel value) {
            this.digitalCameraModel = value;
        }

        /**
         * Obtiene el valor de la propiedad cameraSensor.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfCameraSensorType }
         *     
         */
        public TypeOfCameraSensorType getCameraSensor() {
            return cameraSensor;
        }

        /**
         * Define el valor de la propiedad cameraSensor.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfCameraSensorType }
         *     
         */
        public void setCameraSensor(TypeOfCameraSensorType value) {
            this.cameraSensor = value;
        }

        /**
         * Obtiene el valor de la propiedad cameraCaptureSettings.
         * 
         * @return
         *     possible object is
         *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings }
         *     
         */
        public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings getCameraCaptureSettings() {
            return cameraCaptureSettings;
        }

        /**
         * Define el valor de la propiedad cameraCaptureSettings.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings }
         *     
         */
        public void setCameraCaptureSettings(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings value) {
            this.cameraCaptureSettings = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="ImageData" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="fNumber" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
         *                   &lt;element name="exposureTime" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
         *                   &lt;element name="exposureProgram" type="{http://www.loc.gov/mix/v20}typeOfExposureProgramType" minOccurs="0"/>
         *                   &lt;element name="spectralSensitivity" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
         *                   &lt;element name="isoSpeedRatings" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *                   &lt;element name="oECF" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="exifVersion" type="{http://www.loc.gov/mix/v20}typeOfExifVersionType" minOccurs="0"/>
         *                   &lt;element name="shutterSpeedValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="apertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="brightnessValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="exposureBiasValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="maxApertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="SubjectDistance" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="distance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
         *                             &lt;element name="MinMaxDistance" minOccurs="0">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;sequence>
         *                                       &lt;element name="minDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
         *                                       &lt;element name="maxDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
         *                                     &lt;/sequence>
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="meteringMode" type="{http://www.loc.gov/mix/v20}typeOfMeteringModeType" minOccurs="0"/>
         *                   &lt;element name="lightSource" type="{http://www.loc.gov/mix/v20}typeOfLightSourceType" minOccurs="0"/>
         *                   &lt;element name="flash" type="{http://www.loc.gov/mix/v20}typeOfFlashType" minOccurs="0"/>
         *                   &lt;element name="focalLength" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
         *                   &lt;element name="flashEnergy" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="backLight" type="{http://www.loc.gov/mix/v20}typeOfBackLightType" minOccurs="0"/>
         *                   &lt;element name="exposureIndex" type="{http://www.loc.gov/mix/v20}typeOfPositiveRealType" minOccurs="0"/>
         *                   &lt;element name="sensingMethod" type="{http://www.loc.gov/mix/v20}typeOfSensingMethodType" minOccurs="0"/>
         *                   &lt;element name="cfaPattern" type="{http://www.loc.gov/mix/v20}integerType" minOccurs="0"/>
         *                   &lt;element name="autoFocus" type="{http://www.loc.gov/mix/v20}typeOfAutoFocusType" minOccurs="0"/>
         *                   &lt;element name="PrintAspectRatio" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="xPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
         *                             &lt;element name="yPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="GPSData" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="gpsVersionID" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="gpsLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLatitudeRefType" minOccurs="0"/>
         *                   &lt;element name="GPSLatitude" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="gpsLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLongitudeRefType" minOccurs="0"/>
         *                   &lt;element name="GPSLongitude" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="gpsAltitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsAltitudeRefType" minOccurs="0"/>
         *                   &lt;element name="gpsAltitude" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="gpsTimeStamp" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="gpsSatellites" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="gpsStatus" type="{http://www.loc.gov/mix/v20}typeOfgpsStatusType" minOccurs="0"/>
         *                   &lt;element name="gpsMeasureMode" type="{http://www.loc.gov/mix/v20}typeOfgpsMeasureModeType" minOccurs="0"/>
         *                   &lt;element name="gpsDOP" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="gpsSpeedRef" type="{http://www.loc.gov/mix/v20}typeOfgpsSpeedRefType" minOccurs="0"/>
         *                   &lt;element name="gpsSpeed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="gpsTrackRef" type="{http://www.loc.gov/mix/v20}typeOfgpsTrackRefType" minOccurs="0"/>
         *                   &lt;element name="gpsTrack" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="gpsImgDirectionRef" type="{http://www.loc.gov/mix/v20}typeOfgpsImgDirectionRefType" minOccurs="0"/>
         *                   &lt;element name="gpsImgDirection" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="gpsMapDatum" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="gpsDestLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLatitudeRefType" minOccurs="0"/>
         *                   &lt;element name="GPSDestLatitude" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="gpsDestLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLongitudeRefType" minOccurs="0"/>
         *                   &lt;element name="GPSDestLongitude" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="gpsDestBearingRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestBearingRefType" minOccurs="0"/>
         *                   &lt;element name="gpsDestBearing" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="gpsDestDistanceRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestDistanceRefType" minOccurs="0"/>
         *                   &lt;element name="gpsDestDistance" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
         *                   &lt;element name="gpsProcessingMethod" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="gpsAreaInformation" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *                   &lt;element name="gpsDateStamp" type="{http://www.loc.gov/mix/v20}dateType" minOccurs="0"/>
         *                   &lt;element name="gpsDifferential" type="{http://www.loc.gov/mix/v20}typeOfgpsDifferentialType" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "imageData",
            "gpsData"
        })
        public static class CameraCaptureSettings {

            @XmlElement(name = "ImageData")
            protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData imageData;
            @XmlElement(name = "GPSData")
            protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData gpsData;

            /**
             * Obtiene el valor de la propiedad imageData.
             * 
             * @return
             *     possible object is
             *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData }
             *     
             */
            public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData getImageData() {
                return imageData;
            }

            /**
             * Define el valor de la propiedad imageData.
             * 
             * @param value
             *     allowed object is
             *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData }
             *     
             */
            public void setImageData(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData value) {
                this.imageData = value;
            }

            /**
             * Obtiene el valor de la propiedad gpsData.
             * 
             * @return
             *     possible object is
             *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData }
             *     
             */
            public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData getGPSData() {
                return gpsData;
            }

            /**
             * Define el valor de la propiedad gpsData.
             * 
             * @param value
             *     allowed object is
             *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData }
             *     
             */
            public void setGPSData(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData value) {
                this.gpsData = value;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="gpsVersionID" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="gpsLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLatitudeRefType" minOccurs="0"/>
             *         &lt;element name="GPSLatitude" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="gpsLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsLongitudeRefType" minOccurs="0"/>
             *         &lt;element name="GPSLongitude" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="gpsAltitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsAltitudeRefType" minOccurs="0"/>
             *         &lt;element name="gpsAltitude" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="gpsTimeStamp" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="gpsSatellites" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="gpsStatus" type="{http://www.loc.gov/mix/v20}typeOfgpsStatusType" minOccurs="0"/>
             *         &lt;element name="gpsMeasureMode" type="{http://www.loc.gov/mix/v20}typeOfgpsMeasureModeType" minOccurs="0"/>
             *         &lt;element name="gpsDOP" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="gpsSpeedRef" type="{http://www.loc.gov/mix/v20}typeOfgpsSpeedRefType" minOccurs="0"/>
             *         &lt;element name="gpsSpeed" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="gpsTrackRef" type="{http://www.loc.gov/mix/v20}typeOfgpsTrackRefType" minOccurs="0"/>
             *         &lt;element name="gpsTrack" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="gpsImgDirectionRef" type="{http://www.loc.gov/mix/v20}typeOfgpsImgDirectionRefType" minOccurs="0"/>
             *         &lt;element name="gpsImgDirection" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="gpsMapDatum" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="gpsDestLatitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLatitudeRefType" minOccurs="0"/>
             *         &lt;element name="GPSDestLatitude" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="gpsDestLongitudeRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestLongitudeRefType" minOccurs="0"/>
             *         &lt;element name="GPSDestLongitude" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="gpsDestBearingRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestBearingRefType" minOccurs="0"/>
             *         &lt;element name="gpsDestBearing" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="gpsDestDistanceRef" type="{http://www.loc.gov/mix/v20}typeOfgpsDestDistanceRefType" minOccurs="0"/>
             *         &lt;element name="gpsDestDistance" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="gpsProcessingMethod" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="gpsAreaInformation" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
             *         &lt;element name="gpsDateStamp" type="{http://www.loc.gov/mix/v20}dateType" minOccurs="0"/>
             *         &lt;element name="gpsDifferential" type="{http://www.loc.gov/mix/v20}typeOfgpsDifferentialType" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "gpsVersionID",
                "gpsLatitudeRef",
                "gpsLatitude",
                "gpsLongitudeRef",
                "gpsLongitude",
                "gpsAltitudeRef",
                "gpsAltitude",
                "gpsTimeStamp",
                "gpsSatellites",
                "gpsStatus",
                "gpsMeasureMode",
                "gpsDOP",
                "gpsSpeedRef",
                "gpsSpeed",
                "gpsTrackRef",
                "gpsTrack",
                "gpsImgDirectionRef",
                "gpsImgDirection",
                "gpsMapDatum",
                "gpsDestLatitudeRef",
                "gpsDestLatitude",
                "gpsDestLongitudeRef",
                "gpsDestLongitude",
                "gpsDestBearingRef",
                "gpsDestBearing",
                "gpsDestDistanceRef",
                "gpsDestDistance",
                "gpsProcessingMethod",
                "gpsAreaInformation",
                "gpsDateStamp",
                "gpsDifferential"
            })
            public static class GPSData {

                protected StringType gpsVersionID;
                protected TypeOfgpsLatitudeRefType gpsLatitudeRef;
                @XmlElement(name = "GPSLatitude")
                protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLatitude gpsLatitude;
                protected TypeOfgpsLongitudeRefType gpsLongitudeRef;
                @XmlElement(name = "GPSLongitude")
                protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLongitude gpsLongitude;
                protected TypeOfgpsAltitudeRefType gpsAltitudeRef;
                protected RationalType gpsAltitude;
                protected StringType gpsTimeStamp;
                protected StringType gpsSatellites;
                protected TypeOfgpsStatusType gpsStatus;
                protected TypeOfgpsMeasureModeType gpsMeasureMode;
                protected RationalType gpsDOP;
                protected TypeOfgpsSpeedRefType gpsSpeedRef;
                protected RationalType gpsSpeed;
                protected TypeOfgpsTrackRefType gpsTrackRef;
                protected RationalType gpsTrack;
                protected TypeOfgpsImgDirectionRefType gpsImgDirectionRef;
                protected RationalType gpsImgDirection;
                protected StringType gpsMapDatum;
                protected TypeOfgpsDestLatitudeRefType gpsDestLatitudeRef;
                @XmlElement(name = "GPSDestLatitude")
                protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLatitude gpsDestLatitude;
                protected TypeOfgpsDestLongitudeRefType gpsDestLongitudeRef;
                @XmlElement(name = "GPSDestLongitude")
                protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLongitude gpsDestLongitude;
                protected TypeOfgpsDestBearingRefType gpsDestBearingRef;
                protected RationalType gpsDestBearing;
                protected TypeOfgpsDestDistanceRefType gpsDestDistanceRef;
                protected RationalType gpsDestDistance;
                protected StringType gpsProcessingMethod;
                protected StringType gpsAreaInformation;
                protected DateType gpsDateStamp;
                protected TypeOfgpsDifferentialType gpsDifferential;

                /**
                 * Obtiene el valor de la propiedad gpsVersionID.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getGpsVersionID() {
                    return gpsVersionID;
                }

                /**
                 * Define el valor de la propiedad gpsVersionID.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setGpsVersionID(StringType value) {
                    this.gpsVersionID = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsLatitudeRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsLatitudeRefType }
                 *     
                 */
                public TypeOfgpsLatitudeRefType getGpsLatitudeRef() {
                    return gpsLatitudeRef;
                }

                /**
                 * Define el valor de la propiedad gpsLatitudeRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsLatitudeRefType }
                 *     
                 */
                public void setGpsLatitudeRef(TypeOfgpsLatitudeRefType value) {
                    this.gpsLatitudeRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsLatitude.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLatitude }
                 *     
                 */
                public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLatitude getGPSLatitude() {
                    return gpsLatitude;
                }

                /**
                 * Define el valor de la propiedad gpsLatitude.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLatitude }
                 *     
                 */
                public void setGPSLatitude(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLatitude value) {
                    this.gpsLatitude = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsLongitudeRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsLongitudeRefType }
                 *     
                 */
                public TypeOfgpsLongitudeRefType getGpsLongitudeRef() {
                    return gpsLongitudeRef;
                }

                /**
                 * Define el valor de la propiedad gpsLongitudeRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsLongitudeRefType }
                 *     
                 */
                public void setGpsLongitudeRef(TypeOfgpsLongitudeRefType value) {
                    this.gpsLongitudeRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsLongitude.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLongitude }
                 *     
                 */
                public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLongitude getGPSLongitude() {
                    return gpsLongitude;
                }

                /**
                 * Define el valor de la propiedad gpsLongitude.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLongitude }
                 *     
                 */
                public void setGPSLongitude(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSLongitude value) {
                    this.gpsLongitude = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsAltitudeRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsAltitudeRefType }
                 *     
                 */
                public TypeOfgpsAltitudeRefType getGpsAltitudeRef() {
                    return gpsAltitudeRef;
                }

                /**
                 * Define el valor de la propiedad gpsAltitudeRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsAltitudeRefType }
                 *     
                 */
                public void setGpsAltitudeRef(TypeOfgpsAltitudeRefType value) {
                    this.gpsAltitudeRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsAltitude.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getGpsAltitude() {
                    return gpsAltitude;
                }

                /**
                 * Define el valor de la propiedad gpsAltitude.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setGpsAltitude(RationalType value) {
                    this.gpsAltitude = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsTimeStamp.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getGpsTimeStamp() {
                    return gpsTimeStamp;
                }

                /**
                 * Define el valor de la propiedad gpsTimeStamp.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setGpsTimeStamp(StringType value) {
                    this.gpsTimeStamp = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsSatellites.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getGpsSatellites() {
                    return gpsSatellites;
                }

                /**
                 * Define el valor de la propiedad gpsSatellites.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setGpsSatellites(StringType value) {
                    this.gpsSatellites = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsStatus.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsStatusType }
                 *     
                 */
                public TypeOfgpsStatusType getGpsStatus() {
                    return gpsStatus;
                }

                /**
                 * Define el valor de la propiedad gpsStatus.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsStatusType }
                 *     
                 */
                public void setGpsStatus(TypeOfgpsStatusType value) {
                    this.gpsStatus = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsMeasureMode.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsMeasureModeType }
                 *     
                 */
                public TypeOfgpsMeasureModeType getGpsMeasureMode() {
                    return gpsMeasureMode;
                }

                /**
                 * Define el valor de la propiedad gpsMeasureMode.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsMeasureModeType }
                 *     
                 */
                public void setGpsMeasureMode(TypeOfgpsMeasureModeType value) {
                    this.gpsMeasureMode = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDOP.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getGpsDOP() {
                    return gpsDOP;
                }

                /**
                 * Define el valor de la propiedad gpsDOP.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setGpsDOP(RationalType value) {
                    this.gpsDOP = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsSpeedRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsSpeedRefType }
                 *     
                 */
                public TypeOfgpsSpeedRefType getGpsSpeedRef() {
                    return gpsSpeedRef;
                }

                /**
                 * Define el valor de la propiedad gpsSpeedRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsSpeedRefType }
                 *     
                 */
                public void setGpsSpeedRef(TypeOfgpsSpeedRefType value) {
                    this.gpsSpeedRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsSpeed.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getGpsSpeed() {
                    return gpsSpeed;
                }

                /**
                 * Define el valor de la propiedad gpsSpeed.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setGpsSpeed(RationalType value) {
                    this.gpsSpeed = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsTrackRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsTrackRefType }
                 *     
                 */
                public TypeOfgpsTrackRefType getGpsTrackRef() {
                    return gpsTrackRef;
                }

                /**
                 * Define el valor de la propiedad gpsTrackRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsTrackRefType }
                 *     
                 */
                public void setGpsTrackRef(TypeOfgpsTrackRefType value) {
                    this.gpsTrackRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsTrack.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getGpsTrack() {
                    return gpsTrack;
                }

                /**
                 * Define el valor de la propiedad gpsTrack.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setGpsTrack(RationalType value) {
                    this.gpsTrack = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsImgDirectionRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsImgDirectionRefType }
                 *     
                 */
                public TypeOfgpsImgDirectionRefType getGpsImgDirectionRef() {
                    return gpsImgDirectionRef;
                }

                /**
                 * Define el valor de la propiedad gpsImgDirectionRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsImgDirectionRefType }
                 *     
                 */
                public void setGpsImgDirectionRef(TypeOfgpsImgDirectionRefType value) {
                    this.gpsImgDirectionRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsImgDirection.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getGpsImgDirection() {
                    return gpsImgDirection;
                }

                /**
                 * Define el valor de la propiedad gpsImgDirection.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setGpsImgDirection(RationalType value) {
                    this.gpsImgDirection = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsMapDatum.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getGpsMapDatum() {
                    return gpsMapDatum;
                }

                /**
                 * Define el valor de la propiedad gpsMapDatum.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setGpsMapDatum(StringType value) {
                    this.gpsMapDatum = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestLatitudeRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsDestLatitudeRefType }
                 *     
                 */
                public TypeOfgpsDestLatitudeRefType getGpsDestLatitudeRef() {
                    return gpsDestLatitudeRef;
                }

                /**
                 * Define el valor de la propiedad gpsDestLatitudeRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsDestLatitudeRefType }
                 *     
                 */
                public void setGpsDestLatitudeRef(TypeOfgpsDestLatitudeRefType value) {
                    this.gpsDestLatitudeRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestLatitude.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLatitude }
                 *     
                 */
                public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLatitude getGPSDestLatitude() {
                    return gpsDestLatitude;
                }

                /**
                 * Define el valor de la propiedad gpsDestLatitude.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLatitude }
                 *     
                 */
                public void setGPSDestLatitude(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLatitude value) {
                    this.gpsDestLatitude = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestLongitudeRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsDestLongitudeRefType }
                 *     
                 */
                public TypeOfgpsDestLongitudeRefType getGpsDestLongitudeRef() {
                    return gpsDestLongitudeRef;
                }

                /**
                 * Define el valor de la propiedad gpsDestLongitudeRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsDestLongitudeRefType }
                 *     
                 */
                public void setGpsDestLongitudeRef(TypeOfgpsDestLongitudeRefType value) {
                    this.gpsDestLongitudeRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestLongitude.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLongitude }
                 *     
                 */
                public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLongitude getGPSDestLongitude() {
                    return gpsDestLongitude;
                }

                /**
                 * Define el valor de la propiedad gpsDestLongitude.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLongitude }
                 *     
                 */
                public void setGPSDestLongitude(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.GPSData.GPSDestLongitude value) {
                    this.gpsDestLongitude = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestBearingRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsDestBearingRefType }
                 *     
                 */
                public TypeOfgpsDestBearingRefType getGpsDestBearingRef() {
                    return gpsDestBearingRef;
                }

                /**
                 * Define el valor de la propiedad gpsDestBearingRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsDestBearingRefType }
                 *     
                 */
                public void setGpsDestBearingRef(TypeOfgpsDestBearingRefType value) {
                    this.gpsDestBearingRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestBearing.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getGpsDestBearing() {
                    return gpsDestBearing;
                }

                /**
                 * Define el valor de la propiedad gpsDestBearing.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setGpsDestBearing(RationalType value) {
                    this.gpsDestBearing = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestDistanceRef.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsDestDistanceRefType }
                 *     
                 */
                public TypeOfgpsDestDistanceRefType getGpsDestDistanceRef() {
                    return gpsDestDistanceRef;
                }

                /**
                 * Define el valor de la propiedad gpsDestDistanceRef.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsDestDistanceRefType }
                 *     
                 */
                public void setGpsDestDistanceRef(TypeOfgpsDestDistanceRefType value) {
                    this.gpsDestDistanceRef = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDestDistance.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getGpsDestDistance() {
                    return gpsDestDistance;
                }

                /**
                 * Define el valor de la propiedad gpsDestDistance.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setGpsDestDistance(RationalType value) {
                    this.gpsDestDistance = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsProcessingMethod.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getGpsProcessingMethod() {
                    return gpsProcessingMethod;
                }

                /**
                 * Define el valor de la propiedad gpsProcessingMethod.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setGpsProcessingMethod(StringType value) {
                    this.gpsProcessingMethod = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsAreaInformation.
                 * 
                 * @return
                 *     possible object is
                 *     {@link StringType }
                 *     
                 */
                public StringType getGpsAreaInformation() {
                    return gpsAreaInformation;
                }

                /**
                 * Define el valor de la propiedad gpsAreaInformation.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link StringType }
                 *     
                 */
                public void setGpsAreaInformation(StringType value) {
                    this.gpsAreaInformation = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDateStamp.
                 * 
                 * @return
                 *     possible object is
                 *     {@link DateType }
                 *     
                 */
                public DateType getGpsDateStamp() {
                    return gpsDateStamp;
                }

                /**
                 * Define el valor de la propiedad gpsDateStamp.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link DateType }
                 *     
                 */
                public void setGpsDateStamp(DateType value) {
                    this.gpsDateStamp = value;
                }

                /**
                 * Obtiene el valor de la propiedad gpsDifferential.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfgpsDifferentialType }
                 *     
                 */
                public TypeOfgpsDifferentialType getGpsDifferential() {
                    return gpsDifferential;
                }

                /**
                 * Define el valor de la propiedad gpsDifferential.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfgpsDifferentialType }
                 *     
                 */
                public void setGpsDifferential(TypeOfgpsDifferentialType value) {
                    this.gpsDifferential = value;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "degrees",
                    "minutes",
                    "seconds"
                })
                public static class GPSDestLatitude {

                    protected RationalType degrees;
                    protected RationalType minutes;
                    protected RationalType seconds;

                    /**
                     * Obtiene el valor de la propiedad degrees.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getDegrees() {
                        return degrees;
                    }

                    /**
                     * Define el valor de la propiedad degrees.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setDegrees(RationalType value) {
                        this.degrees = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad minutes.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getMinutes() {
                        return minutes;
                    }

                    /**
                     * Define el valor de la propiedad minutes.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setMinutes(RationalType value) {
                        this.minutes = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad seconds.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getSeconds() {
                        return seconds;
                    }

                    /**
                     * Define el valor de la propiedad seconds.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setSeconds(RationalType value) {
                        this.seconds = value;
                    }

                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "degrees",
                    "minutes",
                    "seconds"
                })
                public static class GPSDestLongitude {

                    protected RationalType degrees;
                    protected RationalType minutes;
                    protected RationalType seconds;

                    /**
                     * Obtiene el valor de la propiedad degrees.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getDegrees() {
                        return degrees;
                    }

                    /**
                     * Define el valor de la propiedad degrees.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setDegrees(RationalType value) {
                        this.degrees = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad minutes.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getMinutes() {
                        return minutes;
                    }

                    /**
                     * Define el valor de la propiedad minutes.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setMinutes(RationalType value) {
                        this.minutes = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad seconds.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getSeconds() {
                        return seconds;
                    }

                    /**
                     * Define el valor de la propiedad seconds.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setSeconds(RationalType value) {
                        this.seconds = value;
                    }

                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "degrees",
                    "minutes",
                    "seconds"
                })
                public static class GPSLatitude {

                    protected RationalType degrees;
                    protected RationalType minutes;
                    protected RationalType seconds;

                    /**
                     * Obtiene el valor de la propiedad degrees.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getDegrees() {
                        return degrees;
                    }

                    /**
                     * Define el valor de la propiedad degrees.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setDegrees(RationalType value) {
                        this.degrees = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad minutes.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getMinutes() {
                        return minutes;
                    }

                    /**
                     * Define el valor de la propiedad minutes.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setMinutes(RationalType value) {
                        this.minutes = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad seconds.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getSeconds() {
                        return seconds;
                    }

                    /**
                     * Define el valor de la propiedad seconds.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setSeconds(RationalType value) {
                        this.seconds = value;
                    }

                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;group ref="{http://www.loc.gov/mix/v20}gpsGroup"/>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "degrees",
                    "minutes",
                    "seconds"
                })
                public static class GPSLongitude {

                    protected RationalType degrees;
                    protected RationalType minutes;
                    protected RationalType seconds;

                    /**
                     * Obtiene el valor de la propiedad degrees.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getDegrees() {
                        return degrees;
                    }

                    /**
                     * Define el valor de la propiedad degrees.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setDegrees(RationalType value) {
                        this.degrees = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad minutes.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getMinutes() {
                        return minutes;
                    }

                    /**
                     * Define el valor de la propiedad minutes.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setMinutes(RationalType value) {
                        this.minutes = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad seconds.
                     * 
                     * @return
                     *     possible object is
                     *     {@link RationalType }
                     *     
                     */
                    public RationalType getSeconds() {
                        return seconds;
                    }

                    /**
                     * Define el valor de la propiedad seconds.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link RationalType }
                     *     
                     */
                    public void setSeconds(RationalType value) {
                        this.seconds = value;
                    }

                }

            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="fNumber" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
             *         &lt;element name="exposureTime" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
             *         &lt;element name="exposureProgram" type="{http://www.loc.gov/mix/v20}typeOfExposureProgramType" minOccurs="0"/>
             *         &lt;element name="spectralSensitivity" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
             *         &lt;element name="isoSpeedRatings" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
             *         &lt;element name="oECF" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="exifVersion" type="{http://www.loc.gov/mix/v20}typeOfExifVersionType" minOccurs="0"/>
             *         &lt;element name="shutterSpeedValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="apertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="brightnessValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="exposureBiasValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="maxApertureValue" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="SubjectDistance" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="distance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
             *                   &lt;element name="MinMaxDistance" minOccurs="0">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;sequence>
             *                             &lt;element name="minDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
             *                             &lt;element name="maxDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
             *                           &lt;/sequence>
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="meteringMode" type="{http://www.loc.gov/mix/v20}typeOfMeteringModeType" minOccurs="0"/>
             *         &lt;element name="lightSource" type="{http://www.loc.gov/mix/v20}typeOfLightSourceType" minOccurs="0"/>
             *         &lt;element name="flash" type="{http://www.loc.gov/mix/v20}typeOfFlashType" minOccurs="0"/>
             *         &lt;element name="focalLength" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
             *         &lt;element name="flashEnergy" type="{http://www.loc.gov/mix/v20}rationalType" minOccurs="0"/>
             *         &lt;element name="backLight" type="{http://www.loc.gov/mix/v20}typeOfBackLightType" minOccurs="0"/>
             *         &lt;element name="exposureIndex" type="{http://www.loc.gov/mix/v20}typeOfPositiveRealType" minOccurs="0"/>
             *         &lt;element name="sensingMethod" type="{http://www.loc.gov/mix/v20}typeOfSensingMethodType" minOccurs="0"/>
             *         &lt;element name="cfaPattern" type="{http://www.loc.gov/mix/v20}integerType" minOccurs="0"/>
             *         &lt;element name="autoFocus" type="{http://www.loc.gov/mix/v20}typeOfAutoFocusType" minOccurs="0"/>
             *         &lt;element name="PrintAspectRatio" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="xPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
             *                   &lt;element name="yPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "fNumber",
                "exposureTime",
                "exposureProgram",
                "spectralSensitivity",
                "isoSpeedRatings",
                "oecf",
                "exifVersion",
                "shutterSpeedValue",
                "apertureValue",
                "brightnessValue",
                "exposureBiasValue",
                "maxApertureValue",
                "subjectDistance",
                "meteringMode",
                "lightSource",
                "flash",
                "focalLength",
                "flashEnergy",
                "backLight",
                "exposureIndex",
                "sensingMethod",
                "cfaPattern",
                "autoFocus",
                "printAspectRatio"
            })
            public static class ImageData {

                protected TypeOfNonNegativeRealType fNumber;
                protected TypeOfNonNegativeRealType exposureTime;
                protected TypeOfExposureProgramType exposureProgram;
                protected List<StringType> spectralSensitivity;
                protected PositiveIntegerType isoSpeedRatings;
                @XmlElement(name = "oECF")
                protected RationalType oecf;
                protected TypeOfExifVersionType exifVersion;
                protected RationalType shutterSpeedValue;
                protected RationalType apertureValue;
                protected RationalType brightnessValue;
                protected RationalType exposureBiasValue;
                protected RationalType maxApertureValue;
                @XmlElement(name = "SubjectDistance")
                protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance subjectDistance;
                protected TypeOfMeteringModeType meteringMode;
                protected TypeOfLightSourceType lightSource;
                protected TypeOfFlashType flash;
                protected TypeOfNonNegativeDecimalType focalLength;
                protected RationalType flashEnergy;
                protected TypeOfBackLightType backLight;
                protected TypeOfPositiveRealType exposureIndex;
                protected TypeOfSensingMethodType sensingMethod;
                protected IntegerType cfaPattern;
                protected TypeOfAutoFocusType autoFocus;
                @XmlElement(name = "PrintAspectRatio")
                protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.PrintAspectRatio printAspectRatio;

                /**
                 * Obtiene el valor de la propiedad fNumber.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public TypeOfNonNegativeRealType getFNumber() {
                    return fNumber;
                }

                /**
                 * Define el valor de la propiedad fNumber.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public void setFNumber(TypeOfNonNegativeRealType value) {
                    this.fNumber = value;
                }

                /**
                 * Obtiene el valor de la propiedad exposureTime.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public TypeOfNonNegativeRealType getExposureTime() {
                    return exposureTime;
                }

                /**
                 * Define el valor de la propiedad exposureTime.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public void setExposureTime(TypeOfNonNegativeRealType value) {
                    this.exposureTime = value;
                }

                /**
                 * Obtiene el valor de la propiedad exposureProgram.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfExposureProgramType }
                 *     
                 */
                public TypeOfExposureProgramType getExposureProgram() {
                    return exposureProgram;
                }

                /**
                 * Define el valor de la propiedad exposureProgram.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfExposureProgramType }
                 *     
                 */
                public void setExposureProgram(TypeOfExposureProgramType value) {
                    this.exposureProgram = value;
                }

                /**
                 * Gets the value of the spectralSensitivity property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the spectralSensitivity property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getSpectralSensitivity().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link StringType }
                 * 
                 * 
                 */
                public List<StringType> getSpectralSensitivity() {
                    if (spectralSensitivity == null) {
                        spectralSensitivity = new ArrayList<StringType>();
                    }
                    return this.spectralSensitivity;
                }

                /**
                 * Obtiene el valor de la propiedad isoSpeedRatings.
                 * 
                 * @return
                 *     possible object is
                 *     {@link PositiveIntegerType }
                 *     
                 */
                public PositiveIntegerType getIsoSpeedRatings() {
                    return isoSpeedRatings;
                }

                /**
                 * Define el valor de la propiedad isoSpeedRatings.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link PositiveIntegerType }
                 *     
                 */
                public void setIsoSpeedRatings(PositiveIntegerType value) {
                    this.isoSpeedRatings = value;
                }

                /**
                 * Obtiene el valor de la propiedad oecf.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getOECF() {
                    return oecf;
                }

                /**
                 * Define el valor de la propiedad oecf.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setOECF(RationalType value) {
                    this.oecf = value;
                }

                /**
                 * Obtiene el valor de la propiedad exifVersion.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfExifVersionType }
                 *     
                 */
                public TypeOfExifVersionType getExifVersion() {
                    return exifVersion;
                }

                /**
                 * Define el valor de la propiedad exifVersion.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfExifVersionType }
                 *     
                 */
                public void setExifVersion(TypeOfExifVersionType value) {
                    this.exifVersion = value;
                }

                /**
                 * Obtiene el valor de la propiedad shutterSpeedValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getShutterSpeedValue() {
                    return shutterSpeedValue;
                }

                /**
                 * Define el valor de la propiedad shutterSpeedValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setShutterSpeedValue(RationalType value) {
                    this.shutterSpeedValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad apertureValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getApertureValue() {
                    return apertureValue;
                }

                /**
                 * Define el valor de la propiedad apertureValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setApertureValue(RationalType value) {
                    this.apertureValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad brightnessValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getBrightnessValue() {
                    return brightnessValue;
                }

                /**
                 * Define el valor de la propiedad brightnessValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setBrightnessValue(RationalType value) {
                    this.brightnessValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad exposureBiasValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getExposureBiasValue() {
                    return exposureBiasValue;
                }

                /**
                 * Define el valor de la propiedad exposureBiasValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setExposureBiasValue(RationalType value) {
                    this.exposureBiasValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad maxApertureValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getMaxApertureValue() {
                    return maxApertureValue;
                }

                /**
                 * Define el valor de la propiedad maxApertureValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setMaxApertureValue(RationalType value) {
                    this.maxApertureValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad subjectDistance.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance }
                 *     
                 */
                public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance getSubjectDistance() {
                    return subjectDistance;
                }

                /**
                 * Define el valor de la propiedad subjectDistance.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance }
                 *     
                 */
                public void setSubjectDistance(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance value) {
                    this.subjectDistance = value;
                }

                /**
                 * Obtiene el valor de la propiedad meteringMode.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfMeteringModeType }
                 *     
                 */
                public TypeOfMeteringModeType getMeteringMode() {
                    return meteringMode;
                }

                /**
                 * Define el valor de la propiedad meteringMode.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfMeteringModeType }
                 *     
                 */
                public void setMeteringMode(TypeOfMeteringModeType value) {
                    this.meteringMode = value;
                }

                /**
                 * Obtiene el valor de la propiedad lightSource.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfLightSourceType }
                 *     
                 */
                public TypeOfLightSourceType getLightSource() {
                    return lightSource;
                }

                /**
                 * Define el valor de la propiedad lightSource.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfLightSourceType }
                 *     
                 */
                public void setLightSource(TypeOfLightSourceType value) {
                    this.lightSource = value;
                }

                /**
                 * Obtiene el valor de la propiedad flash.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfFlashType }
                 *     
                 */
                public TypeOfFlashType getFlash() {
                    return flash;
                }

                /**
                 * Define el valor de la propiedad flash.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfFlashType }
                 *     
                 */
                public void setFlash(TypeOfFlashType value) {
                    this.flash = value;
                }

                /**
                 * Obtiene el valor de la propiedad focalLength.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfNonNegativeDecimalType }
                 *     
                 */
                public TypeOfNonNegativeDecimalType getFocalLength() {
                    return focalLength;
                }

                /**
                 * Define el valor de la propiedad focalLength.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfNonNegativeDecimalType }
                 *     
                 */
                public void setFocalLength(TypeOfNonNegativeDecimalType value) {
                    this.focalLength = value;
                }

                /**
                 * Obtiene el valor de la propiedad flashEnergy.
                 * 
                 * @return
                 *     possible object is
                 *     {@link RationalType }
                 *     
                 */
                public RationalType getFlashEnergy() {
                    return flashEnergy;
                }

                /**
                 * Define el valor de la propiedad flashEnergy.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link RationalType }
                 *     
                 */
                public void setFlashEnergy(RationalType value) {
                    this.flashEnergy = value;
                }

                /**
                 * Obtiene el valor de la propiedad backLight.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfBackLightType }
                 *     
                 */
                public TypeOfBackLightType getBackLight() {
                    return backLight;
                }

                /**
                 * Define el valor de la propiedad backLight.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfBackLightType }
                 *     
                 */
                public void setBackLight(TypeOfBackLightType value) {
                    this.backLight = value;
                }

                /**
                 * Obtiene el valor de la propiedad exposureIndex.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfPositiveRealType }
                 *     
                 */
                public TypeOfPositiveRealType getExposureIndex() {
                    return exposureIndex;
                }

                /**
                 * Define el valor de la propiedad exposureIndex.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfPositiveRealType }
                 *     
                 */
                public void setExposureIndex(TypeOfPositiveRealType value) {
                    this.exposureIndex = value;
                }

                /**
                 * Obtiene el valor de la propiedad sensingMethod.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfSensingMethodType }
                 *     
                 */
                public TypeOfSensingMethodType getSensingMethod() {
                    return sensingMethod;
                }

                /**
                 * Define el valor de la propiedad sensingMethod.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfSensingMethodType }
                 *     
                 */
                public void setSensingMethod(TypeOfSensingMethodType value) {
                    this.sensingMethod = value;
                }

                /**
                 * Obtiene el valor de la propiedad cfaPattern.
                 * 
                 * @return
                 *     possible object is
                 *     {@link IntegerType }
                 *     
                 */
                public IntegerType getCfaPattern() {
                    return cfaPattern;
                }

                /**
                 * Define el valor de la propiedad cfaPattern.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link IntegerType }
                 *     
                 */
                public void setCfaPattern(IntegerType value) {
                    this.cfaPattern = value;
                }

                /**
                 * Obtiene el valor de la propiedad autoFocus.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfAutoFocusType }
                 *     
                 */
                public TypeOfAutoFocusType getAutoFocus() {
                    return autoFocus;
                }

                /**
                 * Define el valor de la propiedad autoFocus.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfAutoFocusType }
                 *     
                 */
                public void setAutoFocus(TypeOfAutoFocusType value) {
                    this.autoFocus = value;
                }

                /**
                 * Obtiene el valor de la propiedad printAspectRatio.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.PrintAspectRatio }
                 *     
                 */
                public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.PrintAspectRatio getPrintAspectRatio() {
                    return printAspectRatio;
                }

                /**
                 * Define el valor de la propiedad printAspectRatio.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.PrintAspectRatio }
                 *     
                 */
                public void setPrintAspectRatio(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.PrintAspectRatio value) {
                    this.printAspectRatio = value;
                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="xPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
                 *         &lt;element name="yPrintAspectRatio" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "xPrintAspectRatio",
                    "yPrintAspectRatio"
                })
                public static class PrintAspectRatio {

                    protected TypeOfNonNegativeRealType xPrintAspectRatio;
                    protected TypeOfNonNegativeRealType yPrintAspectRatio;

                    /**
                     * Obtiene el valor de la propiedad xPrintAspectRatio.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TypeOfNonNegativeRealType }
                     *     
                     */
                    public TypeOfNonNegativeRealType getXPrintAspectRatio() {
                        return xPrintAspectRatio;
                    }

                    /**
                     * Define el valor de la propiedad xPrintAspectRatio.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TypeOfNonNegativeRealType }
                     *     
                     */
                    public void setXPrintAspectRatio(TypeOfNonNegativeRealType value) {
                        this.xPrintAspectRatio = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad yPrintAspectRatio.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TypeOfNonNegativeRealType }
                     *     
                     */
                    public TypeOfNonNegativeRealType getYPrintAspectRatio() {
                        return yPrintAspectRatio;
                    }

                    /**
                     * Define el valor de la propiedad yPrintAspectRatio.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TypeOfNonNegativeRealType }
                     *     
                     */
                    public void setYPrintAspectRatio(TypeOfNonNegativeRealType value) {
                        this.yPrintAspectRatio = value;
                    }

                }


                /**
                 * <p>Clase Java para anonymous complex type.
                 * 
                 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="distance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
                 *         &lt;element name="MinMaxDistance" minOccurs="0">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;sequence>
                 *                   &lt;element name="minDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
                 *                   &lt;element name="maxDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
                 *                 &lt;/sequence>
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "distance",
                    "minMaxDistance"
                })
                public static class SubjectDistance {

                    protected TypeOfNonNegativeDecimalType distance;
                    @XmlElement(name = "MinMaxDistance")
                    protected ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance.MinMaxDistance minMaxDistance;

                    /**
                     * Obtiene el valor de la propiedad distance.
                     * 
                     * @return
                     *     possible object is
                     *     {@link TypeOfNonNegativeDecimalType }
                     *     
                     */
                    public TypeOfNonNegativeDecimalType getDistance() {
                        return distance;
                    }

                    /**
                     * Define el valor de la propiedad distance.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link TypeOfNonNegativeDecimalType }
                     *     
                     */
                    public void setDistance(TypeOfNonNegativeDecimalType value) {
                        this.distance = value;
                    }

                    /**
                     * Obtiene el valor de la propiedad minMaxDistance.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance.MinMaxDistance }
                     *     
                     */
                    public ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance.MinMaxDistance getMinMaxDistance() {
                        return minMaxDistance;
                    }

                    /**
                     * Define el valor de la propiedad minMaxDistance.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance.MinMaxDistance }
                     *     
                     */
                    public void setMinMaxDistance(ImageCaptureMetadataType.DigitalCameraCapture.CameraCaptureSettings.ImageData.SubjectDistance.MinMaxDistance value) {
                        this.minMaxDistance = value;
                    }


                    /**
                     * <p>Clase Java para anonymous complex type.
                     * 
                     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;sequence>
                     *         &lt;element name="minDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
                     *         &lt;element name="maxDistance" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeDecimalType" minOccurs="0"/>
                     *       &lt;/sequence>
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "minDistance",
                        "maxDistance"
                    })
                    public static class MinMaxDistance {

                        protected TypeOfNonNegativeDecimalType minDistance;
                        protected TypeOfNonNegativeDecimalType maxDistance;

                        /**
                         * Obtiene el valor de la propiedad minDistance.
                         * 
                         * @return
                         *     possible object is
                         *     {@link TypeOfNonNegativeDecimalType }
                         *     
                         */
                        public TypeOfNonNegativeDecimalType getMinDistance() {
                            return minDistance;
                        }

                        /**
                         * Define el valor de la propiedad minDistance.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link TypeOfNonNegativeDecimalType }
                         *     
                         */
                        public void setMinDistance(TypeOfNonNegativeDecimalType value) {
                            this.minDistance = value;
                        }

                        /**
                         * Obtiene el valor de la propiedad maxDistance.
                         * 
                         * @return
                         *     possible object is
                         *     {@link TypeOfNonNegativeDecimalType }
                         *     
                         */
                        public TypeOfNonNegativeDecimalType getMaxDistance() {
                            return maxDistance;
                        }

                        /**
                         * Define el valor de la propiedad maxDistance.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link TypeOfNonNegativeDecimalType }
                         *     
                         */
                        public void setMaxDistance(TypeOfNonNegativeDecimalType value) {
                            this.maxDistance = value;
                        }

                    }

                }

            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="digitalCameraModelName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="digitalCameraModelNumber" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="digitalCameraModelSerialNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "digitalCameraModelName",
            "digitalCameraModelNumber",
            "digitalCameraModelSerialNo"
        })
        public static class DigitalCameraModel {

            protected StringType digitalCameraModelName;
            protected StringType digitalCameraModelNumber;
            protected StringType digitalCameraModelSerialNo;

            /**
             * Obtiene el valor de la propiedad digitalCameraModelName.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getDigitalCameraModelName() {
                return digitalCameraModelName;
            }

            /**
             * Define el valor de la propiedad digitalCameraModelName.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setDigitalCameraModelName(StringType value) {
                this.digitalCameraModelName = value;
            }

            /**
             * Obtiene el valor de la propiedad digitalCameraModelNumber.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getDigitalCameraModelNumber() {
                return digitalCameraModelNumber;
            }

            /**
             * Define el valor de la propiedad digitalCameraModelNumber.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setDigitalCameraModelNumber(StringType value) {
                this.digitalCameraModelNumber = value;
            }

            /**
             * Obtiene el valor de la propiedad digitalCameraModelSerialNo.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getDigitalCameraModelSerialNo() {
                return digitalCameraModelSerialNo;
            }

            /**
             * Define el valor de la propiedad digitalCameraModelSerialNo.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setDigitalCameraModelSerialNo(StringType value) {
                this.digitalCameraModelSerialNo = value;
            }

        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="dateTimeCreated" type="{http://www.loc.gov/mix/v20}typeOfDateType" minOccurs="0"/>
     *         &lt;element name="imageProducer" type="{http://www.loc.gov/mix/v20}stringType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="captureDevice" type="{http://www.loc.gov/mix/v20}typeOfCaptureDeviceType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dateTimeCreated",
        "imageProducer",
        "captureDevice"
    })
    public static class GeneralCaptureInformation {

        protected TypeOfDateType dateTimeCreated;
        protected List<StringType> imageProducer;
        protected TypeOfCaptureDeviceType captureDevice;

        /**
         * Obtiene el valor de la propiedad dateTimeCreated.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfDateType }
         *     
         */
        public TypeOfDateType getDateTimeCreated() {
            return dateTimeCreated;
        }

        /**
         * Define el valor de la propiedad dateTimeCreated.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfDateType }
         *     
         */
        public void setDateTimeCreated(TypeOfDateType value) {
            this.dateTimeCreated = value;
        }

        /**
         * Gets the value of the imageProducer property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the imageProducer property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getImageProducer().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StringType }
         * 
         * 
         */
        public List<StringType> getImageProducer() {
            if (imageProducer == null) {
                imageProducer = new ArrayList<StringType>();
            }
            return this.imageProducer;
        }

        /**
         * Obtiene el valor de la propiedad captureDevice.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfCaptureDeviceType }
         *     
         */
        public TypeOfCaptureDeviceType getCaptureDevice() {
            return captureDevice;
        }

        /**
         * Define el valor de la propiedad captureDevice.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfCaptureDeviceType }
         *     
         */
        public void setCaptureDevice(TypeOfCaptureDeviceType value) {
            this.captureDevice = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="scannerManufacturer" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="ScannerModel" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="scannerModelName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="scannerModelNumber" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="scannerModelSerialNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="MaximumOpticalResolution" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="xOpticalResolution" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                   &lt;element name="yOpticalResolution" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
     *                   &lt;element name="opticalResolutionUnit" type="{http://www.loc.gov/mix/v20}typeOfOpticalResolutionUnitType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="scannerSensor" type="{http://www.loc.gov/mix/v20}typeOfScannerSensorType" minOccurs="0"/>
     *         &lt;element name="ScanningSystemSoftware" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="scanningSoftwareName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="scanningSoftwareVersionNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "scannerManufacturer",
        "scannerModel",
        "maximumOpticalResolution",
        "scannerSensor",
        "scanningSystemSoftware"
    })
    public static class ScannerCapture {

        protected StringType scannerManufacturer;
        @XmlElement(name = "ScannerModel")
        protected ImageCaptureMetadataType.ScannerCapture.ScannerModel scannerModel;
        @XmlElement(name = "MaximumOpticalResolution")
        protected ImageCaptureMetadataType.ScannerCapture.MaximumOpticalResolution maximumOpticalResolution;
        protected TypeOfScannerSensorType scannerSensor;
        @XmlElement(name = "ScanningSystemSoftware")
        protected ImageCaptureMetadataType.ScannerCapture.ScanningSystemSoftware scanningSystemSoftware;

        /**
         * Obtiene el valor de la propiedad scannerManufacturer.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getScannerManufacturer() {
            return scannerManufacturer;
        }

        /**
         * Define el valor de la propiedad scannerManufacturer.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setScannerManufacturer(StringType value) {
            this.scannerManufacturer = value;
        }

        /**
         * Obtiene el valor de la propiedad scannerModel.
         * 
         * @return
         *     possible object is
         *     {@link ImageCaptureMetadataType.ScannerCapture.ScannerModel }
         *     
         */
        public ImageCaptureMetadataType.ScannerCapture.ScannerModel getScannerModel() {
            return scannerModel;
        }

        /**
         * Define el valor de la propiedad scannerModel.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageCaptureMetadataType.ScannerCapture.ScannerModel }
         *     
         */
        public void setScannerModel(ImageCaptureMetadataType.ScannerCapture.ScannerModel value) {
            this.scannerModel = value;
        }

        /**
         * Obtiene el valor de la propiedad maximumOpticalResolution.
         * 
         * @return
         *     possible object is
         *     {@link ImageCaptureMetadataType.ScannerCapture.MaximumOpticalResolution }
         *     
         */
        public ImageCaptureMetadataType.ScannerCapture.MaximumOpticalResolution getMaximumOpticalResolution() {
            return maximumOpticalResolution;
        }

        /**
         * Define el valor de la propiedad maximumOpticalResolution.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageCaptureMetadataType.ScannerCapture.MaximumOpticalResolution }
         *     
         */
        public void setMaximumOpticalResolution(ImageCaptureMetadataType.ScannerCapture.MaximumOpticalResolution value) {
            this.maximumOpticalResolution = value;
        }

        /**
         * Obtiene el valor de la propiedad scannerSensor.
         * 
         * @return
         *     possible object is
         *     {@link TypeOfScannerSensorType }
         *     
         */
        public TypeOfScannerSensorType getScannerSensor() {
            return scannerSensor;
        }

        /**
         * Define el valor de la propiedad scannerSensor.
         * 
         * @param value
         *     allowed object is
         *     {@link TypeOfScannerSensorType }
         *     
         */
        public void setScannerSensor(TypeOfScannerSensorType value) {
            this.scannerSensor = value;
        }

        /**
         * Obtiene el valor de la propiedad scanningSystemSoftware.
         * 
         * @return
         *     possible object is
         *     {@link ImageCaptureMetadataType.ScannerCapture.ScanningSystemSoftware }
         *     
         */
        public ImageCaptureMetadataType.ScannerCapture.ScanningSystemSoftware getScanningSystemSoftware() {
            return scanningSystemSoftware;
        }

        /**
         * Define el valor de la propiedad scanningSystemSoftware.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageCaptureMetadataType.ScannerCapture.ScanningSystemSoftware }
         *     
         */
        public void setScanningSystemSoftware(ImageCaptureMetadataType.ScannerCapture.ScanningSystemSoftware value) {
            this.scanningSystemSoftware = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="xOpticalResolution" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *         &lt;element name="yOpticalResolution" type="{http://www.loc.gov/mix/v20}positiveIntegerType" minOccurs="0"/>
         *         &lt;element name="opticalResolutionUnit" type="{http://www.loc.gov/mix/v20}typeOfOpticalResolutionUnitType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "xOpticalResolution",
            "yOpticalResolution",
            "opticalResolutionUnit"
        })
        public static class MaximumOpticalResolution {

            protected PositiveIntegerType xOpticalResolution;
            protected PositiveIntegerType yOpticalResolution;
            protected TypeOfOpticalResolutionUnitType opticalResolutionUnit;

            /**
             * Obtiene el valor de la propiedad xOpticalResolution.
             * 
             * @return
             *     possible object is
             *     {@link PositiveIntegerType }
             *     
             */
            public PositiveIntegerType getXOpticalResolution() {
                return xOpticalResolution;
            }

            /**
             * Define el valor de la propiedad xOpticalResolution.
             * 
             * @param value
             *     allowed object is
             *     {@link PositiveIntegerType }
             *     
             */
            public void setXOpticalResolution(PositiveIntegerType value) {
                this.xOpticalResolution = value;
            }

            /**
             * Obtiene el valor de la propiedad yOpticalResolution.
             * 
             * @return
             *     possible object is
             *     {@link PositiveIntegerType }
             *     
             */
            public PositiveIntegerType getYOpticalResolution() {
                return yOpticalResolution;
            }

            /**
             * Define el valor de la propiedad yOpticalResolution.
             * 
             * @param value
             *     allowed object is
             *     {@link PositiveIntegerType }
             *     
             */
            public void setYOpticalResolution(PositiveIntegerType value) {
                this.yOpticalResolution = value;
            }

            /**
             * Obtiene el valor de la propiedad opticalResolutionUnit.
             * 
             * @return
             *     possible object is
             *     {@link TypeOfOpticalResolutionUnitType }
             *     
             */
            public TypeOfOpticalResolutionUnitType getOpticalResolutionUnit() {
                return opticalResolutionUnit;
            }

            /**
             * Define el valor de la propiedad opticalResolutionUnit.
             * 
             * @param value
             *     allowed object is
             *     {@link TypeOfOpticalResolutionUnitType }
             *     
             */
            public void setOpticalResolutionUnit(TypeOfOpticalResolutionUnitType value) {
                this.opticalResolutionUnit = value;
            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="scannerModelName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="scannerModelNumber" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="scannerModelSerialNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "scannerModelName",
            "scannerModelNumber",
            "scannerModelSerialNo"
        })
        public static class ScannerModel {

            protected StringType scannerModelName;
            protected StringType scannerModelNumber;
            protected StringType scannerModelSerialNo;

            /**
             * Obtiene el valor de la propiedad scannerModelName.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getScannerModelName() {
                return scannerModelName;
            }

            /**
             * Define el valor de la propiedad scannerModelName.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setScannerModelName(StringType value) {
                this.scannerModelName = value;
            }

            /**
             * Obtiene el valor de la propiedad scannerModelNumber.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getScannerModelNumber() {
                return scannerModelNumber;
            }

            /**
             * Define el valor de la propiedad scannerModelNumber.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setScannerModelNumber(StringType value) {
                this.scannerModelNumber = value;
            }

            /**
             * Obtiene el valor de la propiedad scannerModelSerialNo.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getScannerModelSerialNo() {
                return scannerModelSerialNo;
            }

            /**
             * Define el valor de la propiedad scannerModelSerialNo.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setScannerModelSerialNo(StringType value) {
                this.scannerModelSerialNo = value;
            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="scanningSoftwareName" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="scanningSoftwareVersionNo" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "scanningSoftwareName",
            "scanningSoftwareVersionNo"
        })
        public static class ScanningSystemSoftware {

            protected StringType scanningSoftwareName;
            protected StringType scanningSoftwareVersionNo;

            /**
             * Obtiene el valor de la propiedad scanningSoftwareName.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getScanningSoftwareName() {
                return scanningSoftwareName;
            }

            /**
             * Define el valor de la propiedad scanningSoftwareName.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setScanningSoftwareName(StringType value) {
                this.scanningSoftwareName = value;
            }

            /**
             * Obtiene el valor de la propiedad scanningSoftwareVersionNo.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getScanningSoftwareVersionNo() {
                return scanningSoftwareVersionNo;
            }

            /**
             * Define el valor de la propiedad scanningSoftwareVersionNo.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setScanningSoftwareVersionNo(StringType value) {
                this.scanningSoftwareVersionNo = value;
            }

        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="sourceType" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *         &lt;element name="SourceID" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="sourceIDType" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                   &lt;element name="sourceIDValue" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="SourceSize" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="SourceXDimension" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="sourceXDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
     *                             &lt;element name="sourceXDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="SourceYDimension" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="sourceYDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
     *                             &lt;element name="sourceYDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="SourceZDimension" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="sourceZDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
     *                             &lt;element name="sourceZDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "sourceType",
        "sourceID",
        "sourceSize"
    })
    public static class SourceInformation {

        protected StringType sourceType;
        @XmlElement(name = "SourceID")
        protected List<ImageCaptureMetadataType.SourceInformation.SourceID> sourceID;
        @XmlElement(name = "SourceSize")
        protected ImageCaptureMetadataType.SourceInformation.SourceSize sourceSize;

        /**
         * Obtiene el valor de la propiedad sourceType.
         * 
         * @return
         *     possible object is
         *     {@link StringType }
         *     
         */
        public StringType getSourceType() {
            return sourceType;
        }

        /**
         * Define el valor de la propiedad sourceType.
         * 
         * @param value
         *     allowed object is
         *     {@link StringType }
         *     
         */
        public void setSourceType(StringType value) {
            this.sourceType = value;
        }

        /**
         * Gets the value of the sourceID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the sourceID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSourceID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ImageCaptureMetadataType.SourceInformation.SourceID }
         * 
         * 
         */
        public List<ImageCaptureMetadataType.SourceInformation.SourceID> getSourceID() {
            if (sourceID == null) {
                sourceID = new ArrayList<ImageCaptureMetadataType.SourceInformation.SourceID>();
            }
            return this.sourceID;
        }

        /**
         * Obtiene el valor de la propiedad sourceSize.
         * 
         * @return
         *     possible object is
         *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize }
         *     
         */
        public ImageCaptureMetadataType.SourceInformation.SourceSize getSourceSize() {
            return sourceSize;
        }

        /**
         * Define el valor de la propiedad sourceSize.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize }
         *     
         */
        public void setSourceSize(ImageCaptureMetadataType.SourceInformation.SourceSize value) {
            this.sourceSize = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="sourceIDType" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *         &lt;element name="sourceIDValue" type="{http://www.loc.gov/mix/v20}stringType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "sourceIDType",
            "sourceIDValue"
        })
        public static class SourceID {

            protected StringType sourceIDType;
            protected StringType sourceIDValue;

            /**
             * Obtiene el valor de la propiedad sourceIDType.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getSourceIDType() {
                return sourceIDType;
            }

            /**
             * Define el valor de la propiedad sourceIDType.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setSourceIDType(StringType value) {
                this.sourceIDType = value;
            }

            /**
             * Obtiene el valor de la propiedad sourceIDValue.
             * 
             * @return
             *     possible object is
             *     {@link StringType }
             *     
             */
            public StringType getSourceIDValue() {
                return sourceIDValue;
            }

            /**
             * Define el valor de la propiedad sourceIDValue.
             * 
             * @param value
             *     allowed object is
             *     {@link StringType }
             *     
             */
            public void setSourceIDValue(StringType value) {
                this.sourceIDValue = value;
            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="SourceXDimension" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="sourceXDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
         *                   &lt;element name="sourceXDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="SourceYDimension" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="sourceYDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
         *                   &lt;element name="sourceYDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="SourceZDimension" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="sourceZDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
         *                   &lt;element name="sourceZDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "sourceXDimension",
            "sourceYDimension",
            "sourceZDimension"
        })
        public static class SourceSize {

            @XmlElement(name = "SourceXDimension")
            protected ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension sourceXDimension;
            @XmlElement(name = "SourceYDimension")
            protected ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension sourceYDimension;
            @XmlElement(name = "SourceZDimension")
            protected ImageCaptureMetadataType.SourceInformation.SourceSize.SourceZDimension sourceZDimension;

            /**
             * Obtiene el valor de la propiedad sourceXDimension.
             * 
             * @return
             *     possible object is
             *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension }
             *     
             */
            public ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension getSourceXDimension() {
                return sourceXDimension;
            }

            /**
             * Define el valor de la propiedad sourceXDimension.
             * 
             * @param value
             *     allowed object is
             *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension }
             *     
             */
            public void setSourceXDimension(ImageCaptureMetadataType.SourceInformation.SourceSize.SourceXDimension value) {
                this.sourceXDimension = value;
            }

            /**
             * Obtiene el valor de la propiedad sourceYDimension.
             * 
             * @return
             *     possible object is
             *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension }
             *     
             */
            public ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension getSourceYDimension() {
                return sourceYDimension;
            }

            /**
             * Define el valor de la propiedad sourceYDimension.
             * 
             * @param value
             *     allowed object is
             *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension }
             *     
             */
            public void setSourceYDimension(ImageCaptureMetadataType.SourceInformation.SourceSize.SourceYDimension value) {
                this.sourceYDimension = value;
            }

            /**
             * Obtiene el valor de la propiedad sourceZDimension.
             * 
             * @return
             *     possible object is
             *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize.SourceZDimension }
             *     
             */
            public ImageCaptureMetadataType.SourceInformation.SourceSize.SourceZDimension getSourceZDimension() {
                return sourceZDimension;
            }

            /**
             * Define el valor de la propiedad sourceZDimension.
             * 
             * @param value
             *     allowed object is
             *     {@link ImageCaptureMetadataType.SourceInformation.SourceSize.SourceZDimension }
             *     
             */
            public void setSourceZDimension(ImageCaptureMetadataType.SourceInformation.SourceSize.SourceZDimension value) {
                this.sourceZDimension = value;
            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="sourceXDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
             *         &lt;element name="sourceXDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "sourceXDimensionValue",
                "sourceXDimensionUnit"
            })
            public static class SourceXDimension {

                protected TypeOfNonNegativeRealType sourceXDimensionValue;
                protected TypeOfsourceDimensionUnitType sourceXDimensionUnit;

                /**
                 * Obtiene el valor de la propiedad sourceXDimensionValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public TypeOfNonNegativeRealType getSourceXDimensionValue() {
                    return sourceXDimensionValue;
                }

                /**
                 * Define el valor de la propiedad sourceXDimensionValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public void setSourceXDimensionValue(TypeOfNonNegativeRealType value) {
                    this.sourceXDimensionValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad sourceXDimensionUnit.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfsourceDimensionUnitType }
                 *     
                 */
                public TypeOfsourceDimensionUnitType getSourceXDimensionUnit() {
                    return sourceXDimensionUnit;
                }

                /**
                 * Define el valor de la propiedad sourceXDimensionUnit.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfsourceDimensionUnitType }
                 *     
                 */
                public void setSourceXDimensionUnit(TypeOfsourceDimensionUnitType value) {
                    this.sourceXDimensionUnit = value;
                }

            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="sourceYDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
             *         &lt;element name="sourceYDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "sourceYDimensionValue",
                "sourceYDimensionUnit"
            })
            public static class SourceYDimension {

                protected TypeOfNonNegativeRealType sourceYDimensionValue;
                protected TypeOfsourceDimensionUnitType sourceYDimensionUnit;

                /**
                 * Obtiene el valor de la propiedad sourceYDimensionValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public TypeOfNonNegativeRealType getSourceYDimensionValue() {
                    return sourceYDimensionValue;
                }

                /**
                 * Define el valor de la propiedad sourceYDimensionValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public void setSourceYDimensionValue(TypeOfNonNegativeRealType value) {
                    this.sourceYDimensionValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad sourceYDimensionUnit.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfsourceDimensionUnitType }
                 *     
                 */
                public TypeOfsourceDimensionUnitType getSourceYDimensionUnit() {
                    return sourceYDimensionUnit;
                }

                /**
                 * Define el valor de la propiedad sourceYDimensionUnit.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfsourceDimensionUnitType }
                 *     
                 */
                public void setSourceYDimensionUnit(TypeOfsourceDimensionUnitType value) {
                    this.sourceYDimensionUnit = value;
                }

            }


            /**
             * <p>Clase Java para anonymous complex type.
             * 
             * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="sourceZDimensionValue" type="{http://www.loc.gov/mix/v20}typeOfNonNegativeRealType" minOccurs="0"/>
             *         &lt;element name="sourceZDimensionUnit" type="{http://www.loc.gov/mix/v20}typeOfsourceDimensionUnitType" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "sourceZDimensionValue",
                "sourceZDimensionUnit"
            })
            public static class SourceZDimension {

                protected TypeOfNonNegativeRealType sourceZDimensionValue;
                protected TypeOfsourceDimensionUnitType sourceZDimensionUnit;

                /**
                 * Obtiene el valor de la propiedad sourceZDimensionValue.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public TypeOfNonNegativeRealType getSourceZDimensionValue() {
                    return sourceZDimensionValue;
                }

                /**
                 * Define el valor de la propiedad sourceZDimensionValue.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfNonNegativeRealType }
                 *     
                 */
                public void setSourceZDimensionValue(TypeOfNonNegativeRealType value) {
                    this.sourceZDimensionValue = value;
                }

                /**
                 * Obtiene el valor de la propiedad sourceZDimensionUnit.
                 * 
                 * @return
                 *     possible object is
                 *     {@link TypeOfsourceDimensionUnitType }
                 *     
                 */
                public TypeOfsourceDimensionUnitType getSourceZDimensionUnit() {
                    return sourceZDimensionUnit;
                }

                /**
                 * Define el valor de la propiedad sourceZDimensionUnit.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link TypeOfsourceDimensionUnitType }
                 *     
                 */
                public void setSourceZDimensionUnit(TypeOfsourceDimensionUnitType value) {
                    this.sourceZDimensionUnit = value;
                }

            }

        }

    }

}
