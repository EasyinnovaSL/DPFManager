package dpfmanager.conformancechecker.tiff.metadata_fixer.autofixes;

import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.TiffTags;
import com.easyinnova.tiff.model.types.ExifIFD;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.XMP;
import com.easyinnova.tiff.model.types.IPTC;

/**
 * Created by easy on 11/10/2016.
 */
public class fixMetadataInconsistencies implements autofix {
  /**
   * The Definition.
   */
  public String Definition = "Fix Metadata Inconsistencies";

  public String getDescription() {
    return Definition;
  }

  public void run(TiffDocument td) {
    IFD ifd = td.getFirstIFD();

    while (ifd != null) {
      checkIfd(ifd);
      ifd = ifd.getNextIFD();
    }
  }

  void checkIfd(IFD ifd) {
    XMP xmp = null;
    IPTC iptc = null;
    IFD exif = null;

    if (ifd != null && ifd.containsTagId(TiffTags.getTagId("XMP"))) {
      xmp = (XMP) ifd.getTag("XMP").getValue().get(0);
    }
    if (ifd != null && ifd.containsTagId(TiffTags.getTagId("IPTC"))) {
      iptc = (IPTC)ifd.getTag("IPTC").getValue().get(0);
    }
    if (ifd != null && ifd.containsTagId(TiffTags.getTagId("ExifIFD"))) {
      exif = (IFD) ifd.getTag("ExifIFD").getValue().get(0);
    }

    String creatorXmp = null;
    String descriptionXmp = null;
    String copyrightXmp = null;
    String dateXmp = null;

    String creatorIptc = null;
    String descriptionIptc = null;
    String copyrightIptc = null;
    String dateIptc = null;

    String creatorExif = null;
    String descriptionExif = null;
    String copyrightExif = null;
    String dateExif = null;

    if (xmp != null) {
      creatorXmp = xmp.getCreator();
      descriptionXmp = xmp.getDescription();
      copyrightXmp = xmp.getCopyright();
      dateXmp = xmp.getDatetime();
    }

    if (iptc != null) {
      creatorIptc = iptc.getCreator();
      descriptionIptc = iptc.getDescription();
      copyrightIptc = iptc.getCopyright();
      dateIptc = iptc.getDatetime().toString();
    }

    if (exif != null) {
      creatorExif = exif.getTag("Artist") != null ? exif.getTag("Artist").toString() : null;
      descriptionExif = exif.getTag("UserComment") != null ? exif.getTag("UserComment").toString() : null;
      copyrightExif = exif.getTag("Copyright") != null ? exif.getTag("Copyright").toString() : null;
      dateExif = exif.getTag("DateTimeOriginal") != null ? exif.getTag("DateTimeOriginal").toString() : null;

      if (creatorExif == null)
        creatorExif = ifd.getTag("Artist") != null ? ifd.getTag("Artist").toString() : null;
      if (copyrightExif == null)
        copyrightExif = ifd.getTag("Copyright") != null ? ifd.getTag("Copyright").toString() : null;
    }

    if (creatorExif != null) {
      if (creatorIptc != null && !creatorExif.equals(creatorIptc)) {
        iptc.editCreator(creatorExif);
      }
      if (creatorXmp != null && !creatorExif.equals(creatorXmp)) {
        xmp.editCreator(creatorExif);
      }
    } else {
      if (creatorXmp != null && creatorIptc != null && !creatorXmp.equals(creatorIptc)) {
        iptc.editCreator(creatorXmp);
      }
    }

    if (descriptionExif != null) {
      if (descriptionIptc != null && !descriptionExif.equals(descriptionIptc)) {
        iptc.editDescription(descriptionExif);
      }
      if (descriptionXmp != null && !descriptionExif.equals(descriptionXmp)) {
        xmp.editDescription(descriptionExif);
      }
    } else {
      if (descriptionXmp != null && descriptionIptc != null && !descriptionXmp.equals(descriptionIptc)) {
        iptc.editDescription(descriptionXmp);
      }
    }

    if (copyrightExif != null) {
      if (copyrightIptc != null && !copyrightExif.equals(copyrightIptc)) {
        iptc.editCopyright(copyrightExif);
      }
      if (copyrightXmp != null && !copyrightExif.equals(copyrightXmp)) {
        xmp.editCopyright(copyrightExif);
      }
    } else {
      if (copyrightXmp != null && copyrightIptc != null && !copyrightXmp.equals(copyrightIptc)) {
        iptc.editCopyright(copyrightXmp);
      }
    }

    if (dateExif != null) {
      if (dateIptc != null && !dateExif.equals(dateIptc)) {
        //iptc.editDatetime(dateExif);
      }
      if (dateXmp != null && !dateExif.equals(dateXmp)) {
        //xmp.editDatetime(dateExif);
      }
    } else {
      if (dateXmp != null && dateIptc != null && !dateXmp.equals(dateIptc)) {
        //iptc.editDatetime(dateXmp);
      }
    }
  }
}
