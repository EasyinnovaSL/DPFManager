/**
 * <h1>ImageCaptureMetadataType.java</h1> <p> This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version; or,
 * at your choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+.
 * </p> <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License and the Mozilla Public License for more details. </p>
 * <p> You should have received a copy of the GNU General Public License and the Mozilla Public
 * License along with this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 *
 * @author Mar Llambi
 * @version 1.0
 * @since 23/7/2015
 */

package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImageCaptureMetadataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
    protected SourceInformation sourceInformation;
    @XmlElement(name = "GeneralCaptureInformation")
    protected GeneralCaptureInformation generalCaptureInformation;
    @XmlElement(name = "ScannerCapture")
    protected ScannerCapture scannerCapture;
    @XmlElement(name = "DigitalCameraCapture")
    protected DigitalCameraCapture digitalCameraCapture;
    protected TypeOfOrientationType orientation;
    protected StringType methodology;

    /**
     * Gets the value of the sourceInformation property.
     * 
     * @return
     *     possible object is
     *     {@link SourceInformation }
     *     
     */
    public SourceInformation getSourceInformation() {
        return sourceInformation;
    }

    /**
     * Sets the value of the sourceInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceInformation }
     *     
     */
    public void setSourceInformation(SourceInformation value) {
        this.sourceInformation = value;
    }

    /**
     * Gets the value of the generalCaptureInformation property.
     * 
     * @return
     *     possible object is
     *     {@link GeneralCaptureInformation }
     *     
     */
    public GeneralCaptureInformation getGeneralCaptureInformation() {
        return generalCaptureInformation;
    }

    /**
     * Sets the value of the generalCaptureInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeneralCaptureInformation }
     *     
     */
    public void setGeneralCaptureInformation(GeneralCaptureInformation value) {
        this.generalCaptureInformation = value;
    }

    /**
     * Gets the value of the scannerCapture property.
     * 
     * @return
     *     possible object is
     *     {@link ScannerCapture }
     *     
     */
    public ScannerCapture getScannerCapture() {
        return scannerCapture;
    }

    /**
     * Sets the value of the scannerCapture property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScannerCapture }
     *     
     */
    public void setScannerCapture(ScannerCapture value) {
        this.scannerCapture = value;
    }

    /**
     * Gets the value of the digitalCameraCapture property.
     * 
     * @return
     *     possible object is
     *     {@link DigitalCameraCapture }
     *     
     */
    public DigitalCameraCapture getDigitalCameraCapture() {
        return digitalCameraCapture;
    }

    /**
     * Sets the value of the digitalCameraCapture property.
     * 
     * @param value
     *     allowed object is
     *     {@link DigitalCameraCapture }
     *     
     */
    public void setDigitalCameraCapture(DigitalCameraCapture value) {
        this.digitalCameraCapture = value;
    }

    /**
     * Gets the value of the orientation property.
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
     * Sets the value of the orientation property.
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
     * Gets the value of the methodology property.
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
     * Sets the value of the methodology property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
        protected DigitalCameraModel digitalCameraModel;
        protected TypeOfCameraSensorType cameraSensor;
        @XmlElement(name = "CameraCaptureSettings")
        protected CameraCaptureSettings cameraCaptureSettings;

        /**
         * Gets the value of the digitalCameraManufacturer property.
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
         * Sets the value of the digitalCameraManufacturer property.
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
         * Gets the value of the digitalCameraModel property.
         * 
         * @return
         *     possible object is
         *     {@link DigitalCameraModel }
         *     
         */
        public DigitalCameraModel getDigitalCameraModel() {
            return digitalCameraModel;
        }

        /**
         * Sets the value of the digitalCameraModel property.
         * 
         * @param value
         *     allowed object is
         *     {@link DigitalCameraModel }
         *     
         */
        public void setDigitalCameraModel(DigitalCameraModel value) {
            this.digitalCameraModel = value;
        }

        /**
         * Gets the value of the cameraSensor property.
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
         * Sets the value of the cameraSensor property.
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
         * Gets the value of the cameraCaptureSettings property.
         * 
         * @return
         *     possible object is
         *     {@link CameraCaptureSettings }
         *     
         */
        public CameraCaptureSettings getCameraCaptureSettings() {
            return cameraCaptureSettings;
        }

        /**
         * Sets the value of the cameraCaptureSettings property.
         * 
         * @param value
         *     allowed object is
         *     {@link CameraCaptureSettings }
         *     
         */
        public void setCameraCaptureSettings(CameraCaptureSettings value) {
            this.cameraCaptureSettings = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
            protected ImageData imageData;
            @XmlElement(name = "GPSData")
            protected GPSData gpsData;

            /**
             * Gets the value of the imageData property.
             * 
             * @return
             *     possible object is
             *     {@link ImageData }
             *     
             */
            public ImageData getImageData() {
                return imageData;
            }

            /**
             * Sets the value of the imageData property.
             * 
             * @param value
             *     allowed object is
             *     {@link ImageData }
             *     
             */
            public void setImageData(ImageData value) {
                this.imageData = value;
            }

            /**
             * Gets the value of the gpsData property.
             * 
             * @return
             *     possible object is
             *     {@link GPSData }
             *     
             */
            public GPSData getGPSData() {
                return gpsData;
            }

            /**
             * Sets the value of the gpsData property.
             * 
             * @param value
             *     allowed object is
             *     {@link GPSData }
             *     
             */
            public void setGPSData(GPSData value) {
                this.gpsData = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                protected GPSLatitude gpsLatitude;
                protected TypeOfgpsLongitudeRefType gpsLongitudeRef;
                @XmlElement(name = "GPSLongitude")
                protected GPSLongitude gpsLongitude;
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
                protected GPSDestLatitude gpsDestLatitude;
                protected TypeOfgpsDestLongitudeRefType gpsDestLongitudeRef;
                @XmlElement(name = "GPSDestLongitude")
                protected GPSDestLongitude gpsDestLongitude;
                protected TypeOfgpsDestBearingRefType gpsDestBearingRef;
                protected RationalType gpsDestBearing;
                protected TypeOfgpsDestDistanceRefType gpsDestDistanceRef;
                protected RationalType gpsDestDistance;
                protected StringType gpsProcessingMethod;
                protected StringType gpsAreaInformation;
                protected DateType gpsDateStamp;
                protected TypeOfgpsDifferentialType gpsDifferential;

                /**
                 * Gets the value of the gpsVersionID property.
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
                 * Sets the value of the gpsVersionID property.
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
                 * Gets the value of the gpsLatitudeRef property.
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
                 * Sets the value of the gpsLatitudeRef property.
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
                 * Gets the value of the gpsLatitude property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GPSLatitude }
                 *     
                 */
                public GPSLatitude getGPSLatitude() {
                    return gpsLatitude;
                }

                /**
                 * Sets the value of the gpsLatitude property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GPSLatitude }
                 *     
                 */
                public void setGPSLatitude(GPSLatitude value) {
                    this.gpsLatitude = value;
                }

                /**
                 * Gets the value of the gpsLongitudeRef property.
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
                 * Sets the value of the gpsLongitudeRef property.
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
                 * Gets the value of the gpsLongitude property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GPSLongitude }
                 *     
                 */
                public GPSLongitude getGPSLongitude() {
                    return gpsLongitude;
                }

                /**
                 * Sets the value of the gpsLongitude property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GPSLongitude }
                 *     
                 */
                public void setGPSLongitude(GPSLongitude value) {
                    this.gpsLongitude = value;
                }

                /**
                 * Gets the value of the gpsAltitudeRef property.
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
                 * Sets the value of the gpsAltitudeRef property.
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
                 * Gets the value of the gpsAltitude property.
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
                 * Sets the value of the gpsAltitude property.
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
                 * Gets the value of the gpsTimeStamp property.
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
                 * Sets the value of the gpsTimeStamp property.
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
                 * Gets the value of the gpsSatellites property.
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
                 * Sets the value of the gpsSatellites property.
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
                 * Gets the value of the gpsStatus property.
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
                 * Sets the value of the gpsStatus property.
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
                 * Gets the value of the gpsMeasureMode property.
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
                 * Sets the value of the gpsMeasureMode property.
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
                 * Gets the value of the gpsDOP property.
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
                 * Sets the value of the gpsDOP property.
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
                 * Gets the value of the gpsSpeedRef property.
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
                 * Sets the value of the gpsSpeedRef property.
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
                 * Gets the value of the gpsSpeed property.
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
                 * Sets the value of the gpsSpeed property.
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
                 * Gets the value of the gpsTrackRef property.
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
                 * Sets the value of the gpsTrackRef property.
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
                 * Gets the value of the gpsTrack property.
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
                 * Sets the value of the gpsTrack property.
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
                 * Gets the value of the gpsImgDirectionRef property.
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
                 * Sets the value of the gpsImgDirectionRef property.
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
                 * Gets the value of the gpsImgDirection property.
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
                 * Sets the value of the gpsImgDirection property.
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
                 * Gets the value of the gpsMapDatum property.
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
                 * Sets the value of the gpsMapDatum property.
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
                 * Gets the value of the gpsDestLatitudeRef property.
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
                 * Sets the value of the gpsDestLatitudeRef property.
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
                 * Gets the value of the gpsDestLatitude property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GPSDestLatitude }
                 *     
                 */
                public GPSDestLatitude getGPSDestLatitude() {
                    return gpsDestLatitude;
                }

                /**
                 * Sets the value of the gpsDestLatitude property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GPSDestLatitude }
                 *     
                 */
                public void setGPSDestLatitude(GPSDestLatitude value) {
                    this.gpsDestLatitude = value;
                }

                /**
                 * Gets the value of the gpsDestLongitudeRef property.
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
                 * Sets the value of the gpsDestLongitudeRef property.
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
                 * Gets the value of the gpsDestLongitude property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GPSDestLongitude }
                 *     
                 */
                public GPSDestLongitude getGPSDestLongitude() {
                    return gpsDestLongitude;
                }

                /**
                 * Sets the value of the gpsDestLongitude property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GPSDestLongitude }
                 *     
                 */
                public void setGPSDestLongitude(GPSDestLongitude value) {
                    this.gpsDestLongitude = value;
                }

                /**
                 * Gets the value of the gpsDestBearingRef property.
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
                 * Sets the value of the gpsDestBearingRef property.
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
                 * Gets the value of the gpsDestBearing property.
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
                 * Sets the value of the gpsDestBearing property.
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
                 * Gets the value of the gpsDestDistanceRef property.
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
                 * Sets the value of the gpsDestDistanceRef property.
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
                 * Gets the value of the gpsDestDistance property.
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
                 * Sets the value of the gpsDestDistance property.
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
                 * Gets the value of the gpsProcessingMethod property.
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
                 * Sets the value of the gpsProcessingMethod property.
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
                 * Gets the value of the gpsAreaInformation property.
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
                 * Sets the value of the gpsAreaInformation property.
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
                 * Gets the value of the gpsDateStamp property.
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
                 * Sets the value of the gpsDateStamp property.
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
                 * Gets the value of the gpsDifferential property.
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
                 * Sets the value of the gpsDifferential property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the degrees property.
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
                     * Sets the value of the degrees property.
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
                     * Gets the value of the minutes property.
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
                     * Sets the value of the minutes property.
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
                     * Gets the value of the seconds property.
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
                     * Sets the value of the seconds property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the degrees property.
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
                     * Sets the value of the degrees property.
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
                     * Gets the value of the minutes property.
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
                     * Sets the value of the minutes property.
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
                     * Gets the value of the seconds property.
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
                     * Sets the value of the seconds property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the degrees property.
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
                     * Sets the value of the degrees property.
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
                     * Gets the value of the minutes property.
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
                     * Sets the value of the minutes property.
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
                     * Gets the value of the seconds property.
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
                     * Sets the value of the seconds property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the degrees property.
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
                     * Sets the value of the degrees property.
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
                     * Gets the value of the minutes property.
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
                     * Sets the value of the minutes property.
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
                     * Gets the value of the seconds property.
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
                     * Sets the value of the seconds property.
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                protected SubjectDistance subjectDistance;
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
                protected PrintAspectRatio printAspectRatio;

                /**
                 * Gets the value of the fNumber property.
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
                 * Sets the value of the fNumber property.
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
                 * Gets the value of the exposureTime property.
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
                 * Sets the value of the exposureTime property.
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
                 * Gets the value of the exposureProgram property.
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
                 * Sets the value of the exposureProgram property.
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

                public void setSpectralSensitivity(StringType s) {
                    if (spectralSensitivity == null) {
                        spectralSensitivity = new ArrayList<StringType>();
                    }
                    this.spectralSensitivity.add(s);
                }

                /**
                 * Gets the value of the isoSpeedRatings property.
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
                 * Sets the value of the isoSpeedRatings property.
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
                 * Gets the value of the oecf property.
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
                 * Sets the value of the oecf property.
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
                 * Gets the value of the exifVersion property.
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
                 * Sets the value of the exifVersion property.
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
                 * Gets the value of the shutterSpeedValue property.
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
                 * Sets the value of the shutterSpeedValue property.
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
                 * Gets the value of the apertureValue property.
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
                 * Sets the value of the apertureValue property.
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
                 * Gets the value of the brightnessValue property.
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
                 * Sets the value of the brightnessValue property.
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
                 * Gets the value of the exposureBiasValue property.
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
                 * Sets the value of the exposureBiasValue property.
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
                 * Gets the value of the maxApertureValue property.
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
                 * Sets the value of the maxApertureValue property.
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
                 * Gets the value of the subjectDistance property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SubjectDistance }
                 *     
                 */
                public SubjectDistance getSubjectDistance() {
                    return subjectDistance;
                }

                /**
                 * Sets the value of the subjectDistance property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SubjectDistance }
                 *     
                 */
                public void setSubjectDistance(SubjectDistance value) {
                    this.subjectDistance = value;
                }

                /**
                 * Gets the value of the meteringMode property.
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
                 * Sets the value of the meteringMode property.
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
                 * Gets the value of the lightSource property.
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
                 * Sets the value of the lightSource property.
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
                 * Gets the value of the flash property.
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
                 * Sets the value of the flash property.
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
                 * Gets the value of the focalLength property.
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
                 * Sets the value of the focalLength property.
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
                 * Gets the value of the flashEnergy property.
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
                 * Sets the value of the flashEnergy property.
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
                 * Gets the value of the backLight property.
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
                 * Sets the value of the backLight property.
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
                 * Gets the value of the exposureIndex property.
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
                 * Sets the value of the exposureIndex property.
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
                 * Gets the value of the sensingMethod property.
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
                 * Sets the value of the sensingMethod property.
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
                 * Gets the value of the cfaPattern property.
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
                 * Sets the value of the cfaPattern property.
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
                 * Gets the value of the autoFocus property.
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
                 * Sets the value of the autoFocus property.
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
                 * Gets the value of the printAspectRatio property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link PrintAspectRatio }
                 *     
                 */
                public PrintAspectRatio getPrintAspectRatio() {
                    return printAspectRatio;
                }

                /**
                 * Sets the value of the printAspectRatio property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link PrintAspectRatio }
                 *     
                 */
                public void setPrintAspectRatio(PrintAspectRatio value) {
                    this.printAspectRatio = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                     * Gets the value of the xPrintAspectRatio property.
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
                     * Sets the value of the xPrintAspectRatio property.
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
                     * Gets the value of the yPrintAspectRatio property.
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
                     * Sets the value of the yPrintAspectRatio property.
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
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
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
                    protected MinMaxDistance minMaxDistance;

                    /**
                     * Gets the value of the distance property.
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
                     * Sets the value of the distance property.
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
                     * Gets the value of the minMaxDistance property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link MinMaxDistance }
                     *     
                     */
                    public MinMaxDistance getMinMaxDistance() {
                        return minMaxDistance;
                    }

                    /**
                     * Sets the value of the minMaxDistance property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link MinMaxDistance }
                     *     
                     */
                    public void setMinMaxDistance(MinMaxDistance value) {
                        this.minMaxDistance = value;
                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
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
                         * Gets the value of the minDistance property.
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
                         * Sets the value of the minDistance property.
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
                         * Gets the value of the maxDistance property.
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
                         * Sets the value of the maxDistance property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the digitalCameraModelName property.
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
             * Sets the value of the digitalCameraModelName property.
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
             * Gets the value of the digitalCameraModelNumber property.
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
             * Sets the value of the digitalCameraModelNumber property.
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
             * Gets the value of the digitalCameraModelSerialNo property.
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
             * Sets the value of the digitalCameraModelSerialNo property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
         * Gets the value of the dateTimeCreated property.
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
         * Sets the value of the dateTimeCreated property.
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

        public void setImageProducer(StringType s) {
            if (imageProducer == null) {
                imageProducer = new ArrayList<StringType>();
            }
            this.imageProducer.add(s);
        }

        /**
         * Gets the value of the captureDevice property.
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
         * Sets the value of the captureDevice property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
        protected ScannerModel scannerModel;
        @XmlElement(name = "MaximumOpticalResolution")
        protected MaximumOpticalResolution maximumOpticalResolution;
        protected TypeOfScannerSensorType scannerSensor;
        @XmlElement(name = "ScanningSystemSoftware")
        protected ScanningSystemSoftware scanningSystemSoftware;

        /**
         * Gets the value of the scannerManufacturer property.
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
         * Sets the value of the scannerManufacturer property.
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
         * Gets the value of the scannerModel property.
         * 
         * @return
         *     possible object is
         *     {@link ScannerModel }
         *     
         */
        public ScannerModel getScannerModel() {
            return scannerModel;
        }

        /**
         * Sets the value of the scannerModel property.
         * 
         * @param value
         *     allowed object is
         *     {@link ScannerModel }
         *     
         */
        public void setScannerModel(ScannerModel value) {
            this.scannerModel = value;
        }

        /**
         * Gets the value of the maximumOpticalResolution property.
         * 
         * @return
         *     possible object is
         *     {@link MaximumOpticalResolution }
         *     
         */
        public MaximumOpticalResolution getMaximumOpticalResolution() {
            return maximumOpticalResolution;
        }

        /**
         * Sets the value of the maximumOpticalResolution property.
         * 
         * @param value
         *     allowed object is
         *     {@link MaximumOpticalResolution }
         *     
         */
        public void setMaximumOpticalResolution(MaximumOpticalResolution value) {
            this.maximumOpticalResolution = value;
        }

        /**
         * Gets the value of the scannerSensor property.
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
         * Sets the value of the scannerSensor property.
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
         * Gets the value of the scanningSystemSoftware property.
         * 
         * @return
         *     possible object is
         *     {@link ScanningSystemSoftware }
         *     
         */
        public ScanningSystemSoftware getScanningSystemSoftware() {
            return scanningSystemSoftware;
        }

        /**
         * Sets the value of the scanningSystemSoftware property.
         * 
         * @param value
         *     allowed object is
         *     {@link ScanningSystemSoftware }
         *     
         */
        public void setScanningSystemSoftware(ScanningSystemSoftware value) {
            this.scanningSystemSoftware = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the xOpticalResolution property.
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
             * Sets the value of the xOpticalResolution property.
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
             * Gets the value of the yOpticalResolution property.
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
             * Sets the value of the yOpticalResolution property.
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
             * Gets the value of the opticalResolutionUnit property.
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
             * Sets the value of the opticalResolutionUnit property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the scannerModelName property.
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
             * Sets the value of the scannerModelName property.
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
             * Gets the value of the scannerModelNumber property.
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
             * Sets the value of the scannerModelNumber property.
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
             * Gets the value of the scannerModelSerialNo property.
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
             * Sets the value of the scannerModelSerialNo property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the scanningSoftwareName property.
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
             * Sets the value of the scanningSoftwareName property.
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
             * Gets the value of the scanningSoftwareVersionNo property.
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
             * Sets the value of the scanningSoftwareVersionNo property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
        protected List<SourceID> sourceID;
        @XmlElement(name = "SourceSize")
        protected SourceSize sourceSize;

        /**
         * Gets the value of the sourceType property.
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
         * Sets the value of the sourceType property.
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
         * {@link SourceID }
         * 
         * 
         */
        public List<SourceID> getSourceID() {
            if (sourceID == null) {
                sourceID = new ArrayList<SourceID>();
            }
            return this.sourceID;
        }

        /**
         * Gets the value of the sourceSize property.
         * 
         * @return
         *     possible object is
         *     {@link SourceSize }
         *     
         */
        public SourceSize getSourceSize() {
            return sourceSize;
        }

        /**
         * Sets the value of the sourceSize property.
         * 
         * @param value
         *     allowed object is
         *     {@link SourceSize }
         *     
         */
        public void setSourceSize(SourceSize value) {
            this.sourceSize = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
             * Gets the value of the sourceIDType property.
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
             * Sets the value of the sourceIDType property.
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
             * Gets the value of the sourceIDValue property.
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
             * Sets the value of the sourceIDValue property.
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
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
            protected SourceXDimension sourceXDimension;
            @XmlElement(name = "SourceYDimension")
            protected SourceYDimension sourceYDimension;
            @XmlElement(name = "SourceZDimension")
            protected SourceZDimension sourceZDimension;

            /**
             * Gets the value of the sourceXDimension property.
             * 
             * @return
             *     possible object is
             *     {@link SourceXDimension }
             *     
             */
            public SourceXDimension getSourceXDimension() {
                return sourceXDimension;
            }

            /**
             * Sets the value of the sourceXDimension property.
             * 
             * @param value
             *     allowed object is
             *     {@link SourceXDimension }
             *     
             */
            public void setSourceXDimension(SourceXDimension value) {
                this.sourceXDimension = value;
            }

            /**
             * Gets the value of the sourceYDimension property.
             * 
             * @return
             *     possible object is
             *     {@link SourceYDimension }
             *     
             */
            public SourceYDimension getSourceYDimension() {
                return sourceYDimension;
            }

            /**
             * Sets the value of the sourceYDimension property.
             * 
             * @param value
             *     allowed object is
             *     {@link SourceYDimension }
             *     
             */
            public void setSourceYDimension(SourceYDimension value) {
                this.sourceYDimension = value;
            }

            /**
             * Gets the value of the sourceZDimension property.
             * 
             * @return
             *     possible object is
             *     {@link SourceZDimension }
             *     
             */
            public SourceZDimension getSourceZDimension() {
                return sourceZDimension;
            }

            /**
             * Sets the value of the sourceZDimension property.
             * 
             * @param value
             *     allowed object is
             *     {@link SourceZDimension }
             *     
             */
            public void setSourceZDimension(SourceZDimension value) {
                this.sourceZDimension = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                 * Gets the value of the sourceXDimensionValue property.
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
                 * Sets the value of the sourceXDimensionValue property.
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
                 * Gets the value of the sourceXDimensionUnit property.
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
                 * Sets the value of the sourceXDimensionUnit property.
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                 * Gets the value of the sourceYDimensionValue property.
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
                 * Sets the value of the sourceYDimensionValue property.
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
                 * Gets the value of the sourceYDimensionUnit property.
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
                 * Sets the value of the sourceYDimensionUnit property.
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
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
                 * Gets the value of the sourceZDimensionValue property.
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
                 * Sets the value of the sourceZDimensionValue property.
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
                 * Gets the value of the sourceZDimensionUnit property.
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
                 * Sets the value of the sourceZDimensionUnit property.
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
