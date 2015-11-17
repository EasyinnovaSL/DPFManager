package dpfmanager.shell.modules.autofixes;

import com.easyinnova.tiff.model.TiffDocument;

/**
 * Created by easy on 23/10/2015.
 */
public class clearPrivateData implements autofix {
  /**
   * The Definition.
   */
  public String Definition = "Clear Private Data";

  public String getDescription() {
    return Definition;
  }

  public void run(TiffDocument td) {
    td.removeTag("GPS IFD");
    td.removeTag("GPSAltitude");
    td.removeTag("GPSAltitudeRef");
    td.removeTag("GPSAreaInformation");
    td.removeTag("GPSDateStamp");
    td.removeTag("GPSDestBearing");
    td.removeTag("GPSDestBearingRef");
    td.removeTag("GPSDistance");
    td.removeTag("GPSDistanceRef");
    td.removeTag("GPSLatitude");
    td.removeTag("GPSLatitudeRef");
    td.removeTag("GPSLONGitude");
    td.removeTag("GPSLONGitudeRef");
    td.removeTag("GPSDifferential");
    td.removeTag("GPSDOP");
    td.removeTag("GPSImgDirection");
    td.removeTag("GPSImgDirectionRef");
    td.removeTag("GPSDestLatitude");
    td.removeTag("GPSDestLatitudeRef");
    td.removeTag("GPSDestLONGitude");
    td.removeTag("GPSDestLONGitudeRef");
    td.removeTag("GPSMapDatum");
    td.removeTag("GPSMeasureMode");
    td.removeTag("GPSProcessingMethod");
    td.removeTag("GPSSatellites");
    td.removeTag("GPSSpeed");
    td.removeTag("GPSSpeedRef");
    td.removeTag("GPSStatus");
    td.removeTag("GPSTimestamp");
    td.removeTag("GPSTrack");
    td.removeTag("GPSTrackRef");
    td.removeTag("GPSVersionID");
  }
}
