package dpfmanager.shell.conformancechecker.ImplementationChecker;

import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffHeader;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffIfd;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffIfds;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffSingleNode;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffTag;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffTags;
import dpfmanager.shell.conformancechecker.ImplementationChecker.model.TiffValidationObject;

import com.easyinnova.tiff.model.IfdTags;
import com.easyinnova.tiff.model.Metadata;
import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.TiffDocument;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.IPTC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Created by easy on 08/03/2016.
 */
public class TiffImplementationChecker {
  private TiffDocument tiffDoc;

  public void CreateValidationObject(TiffDocument tiffDoc) {
    this.tiffDoc = tiffDoc;
    TiffValidationObject tiffValidate = new TiffValidationObject();

    // Generic info
    tiffValidate.setSize(tiffDoc.getSize());

    // Header
    TiffHeader header = new TiffHeader();
    header.setByteOrder(tiffDoc.getEndianess() != null ? tiffDoc.getEndianess().toString() : "");
    header.setMagicNumber(tiffDoc.getMagicNumber() + "");
    header.setOffset(tiffDoc.getFirstIFDOffset());
    tiffValidate.setHeader(header);

    // IFDs
    IFD ifd = tiffDoc.getFirstIFD();
    List<TiffIfd> ifdsList = new ArrayList<TiffIfd>();
    int n = 1;
    while (ifd != null) {
      ifdsList.add(CreateIFDValidation(ifd, n++));
      ifd = ifd.getNextIFD();
    }
    TiffIfds ifds = new TiffIfds();
    ifds.setIfds(ifdsList);
    tiffValidate.setIfds(ifds);

    try {
      File file = new File("file.xml");
      JAXBContext jaxbContext = JAXBContext.newInstance(TiffValidationObject.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      jaxbMarshaller.marshal(tiffValidate, file);
      //jaxbMarshaller.marshal(tiffValidate, System.out);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

  public TiffIfd CreateIFDValidation(IFD ifd, int n) {
    IfdTags metadata = ifd.getMetadata();
    TiffIfd tiffIfd = new TiffIfd();
    tiffIfd.setN(n);
    List<TiffTag> tags = new ArrayList<TiffTag>();
    int prevTagId = -1;
    boolean correctTagOrdering = true;
    boolean duplicatedTags = false;
    HashSet tagIds = new HashSet<>();
    for (TagValue tv : ifd.getTags().getTags()) {
      if (tv.getId() <= prevTagId) {
        correctTagOrdering = false;
      }
      if (tagIds.contains(tv.getId())) {
        duplicatedTags = true;
      } else {
        tagIds.add(tv.getId());
      }
      prevTagId = tv.getId();
      tags.add(CreateTiffTag(tv));
    }
    TiffTags tiffTags = new TiffTags();
    tiffTags.setTags(tags);
    tiffIfd.setTags(tiffTags);
    tiffIfd.setTagOrdering(correctTagOrdering ? 1 : 0);
    tiffIfd.setDuplicateTags(duplicatedTags ? 1 : 0);
    tiffIfd.setStrips(ifd.hasStrips() ? 1 : 0);
    tiffIfd.setTiles(ifd.hasTiles() ? 1 : 0);
    tiffIfd.setCorrectStrips(1);
    tiffIfd.setCorrectTiles(1);

    // Strips check
    if (ifd.hasStrips()) {
      int pixelSize = 0;
      for (int i = 0; i < metadata.get("BitsPerSample").getCardinality(); i++) {
        pixelSize += metadata.get("BitsPerSample").getValue().get(i).toInt();
      }
      if (metadata.get("Compression").getFirstNumericValue() == 1 && pixelSize >= 8) {
        int calculatedImageLength = 0;
        int id = com.easyinnova.tiff.model.TiffTags.getTagId("StripOffsets");
        int nsc = metadata.get(id).getCardinality();
        for (int i = 0; i < nsc; i++) {
          calculatedImageLength += metadata.get(id).getValue().get(i).toInt();
        }
        if (calculatedImageLength != metadata.get("ImageLength").getFirstNumericValue()
            * metadata.get("ImageWidth").getFirstNumericValue() * pixelSize / 8) {
          tiffIfd.setCorrectStrips(0);
        }
      }
    }

    // Tiles check
    if (ifd.hasTiles()) {
      long tileLength = metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("TileLength")).getFirstNumericValue();;
      long tileWidth = metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("TileWidth")).getFirstNumericValue();;
      long tilesPerImage =
          ((metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("ImageWidth")).getFirstNumericValue() + tileWidth - 1) / tileWidth)
              * ((metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("ImageLength")).getFirstNumericValue() + tileLength - 1) / tileLength);

      // Check Plannar Configuration
      int id = com.easyinnova.tiff.model.TiffTags.getTagId("PlanarConfiguration");
      int idspp = com.easyinnova.tiff.model.TiffTags.getTagId("SamplesPerPixel");
      if (metadata.containsTagId(id) && metadata.containsTagId(idspp)) {
        long planar = metadata.get(id).getFirstNumericValue();
        long spp = metadata.get(idspp).getFirstNumericValue();
        if (planar == 2) {
          long spp_tpi = spp * tilesPerImage;
          if (ifd.getImageTiles().getTiles().size() < spp_tpi) {
            tiffIfd.setCorrectTiles(0);
          }
        }
      }
    }

    // Check pixel samples bits
    if (metadata.containsTagId(com.easyinnova.tiff.model.TiffTags.getTagId("BitsPerSample"))
        && metadata.containsTagId(com.easyinnova.tiff.model.TiffTags.getTagId("SampesPerPixel"))) {
      boolean sppEqualsBps = true;
      boolean correctExtraSamples = true;
      boolean onlyNecessaryExtraSamples = true;
      boolean validBitsPerSample = true;
      boolean equalBitsPerSampleValues = true;
      long spp = metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("SamplesPerPixel")).getFirstNumericValue();
      int bps = metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("BitsPerSample")).getValue().size();
      if (spp != bps) {
        sppEqualsBps = false;
      }

      if (metadata.containsTagId(com.easyinnova.tiff.model.TiffTags.getTagId("ExtraSamples"))) {
        int ext = metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("ExtraSamples")).getValue().size();
        if (ext + 3 != bps) {
          correctExtraSamples = false;
        } else if (ext > 0 && bps <= 3) {
          onlyNecessaryExtraSamples = false;
        }
      }

      if (bps > 1) {
        TagValue lbps = metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("BitsPerSample"));
        if (lbps == null || lbps.getValue() == null) {
          validBitsPerSample = false;
        } else {
          boolean distinct_bps_samples = false;
          for (int i = 1; i < lbps.getCardinality(); i++) {
            if (lbps.getValue().get(i).toInt() != lbps.getValue().get(i - 1).toInt())
              distinct_bps_samples = true;
          }
          if (distinct_bps_samples)
            equalBitsPerSampleValues = false;
        }
      }

      tiffIfd.setSppEqualsBps(sppEqualsBps ? 1 : 0);
      tiffIfd.setCorrectExtraSamples(correctExtraSamples ? 1 : 0);
      tiffIfd.setOnlyNecessaryExtraSamples(onlyNecessaryExtraSamples ? 1 : 0);
      tiffIfd.setValidBitsPerSample(validBitsPerSample ? 1 : 0);
      tiffIfd.setEqualBitsPerSampleValues(equalBitsPerSampleValues ? 1 : 0);
    }

    // Check image type
    if (metadata.containsTagId(com.easyinnova.tiff.model.TiffTags.getTagId("PhotometricInterpretation"))) {
      int photometric = (int) metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("PhotometricInterpretation")).getFirstNumericValue();
      switch (photometric) {
        case 0:
        case 1:
          if (!metadata.containsTagId(com.easyinnova.tiff.model.TiffTags.getTagId("BitsPerSample"))
              || metadata.get(com.easyinnova.tiff.model.TiffTags.getTagId("BitsPerSample")).getFirstNumericValue() == 1) {
            tiffIfd.setType("Bilevel");
          } else {
            tiffIfd.setType("Grayscale");
          }
          break;
        case 2:
          tiffIfd.setType("RGB");
          break;
        case 3:
          tiffIfd.setType("Pallete");
          break;
        case 4:
          tiffIfd.setType("Transparency");
          break;
        case 5:
          tiffIfd.setType("CMYK");
          break;
        case 6:
          tiffIfd.setType("YCbCr");
          break;
        case 8:
        case 9:
        case 10:
          tiffIfd.setType("CIELab");
          break;
      }
    }

    return tiffIfd;
  }

  public TiffTag CreateTiffTag(TagValue tv) {
    TiffTag tt = new TiffTag();
    tt.setId(tv.getId());
    tt.setName(tv.getName());
    tt.setCardinality(tv.getCardinality());
    tt.setType(com.easyinnova.tiff.model.TiffTags.getTagTypeName(tv.getType()));
    tt.setOffset(tv.getReadOffset());
    if (tv.getId() == 34665) {
      // EXIF
      TiffIfd ifd = new TiffIfd();
      List<TiffTag> tags = new ArrayList<TiffTag>();
      int prevTagId = -1;
      boolean correctTagOrdering = true;
      boolean duplicatedTags = false;
      HashSet tagIds = new HashSet<>();
      IFD exif = (IFD) tv.getValue().get(0);
      for (TagValue tvv : exif.getTags().getTags()) {
        if (tvv.getId() <= prevTagId) {
          correctTagOrdering = false;
        }
        if (tagIds.contains(tvv.getId())) {
          duplicatedTags = true;
        } else {
          tagIds.add(tvv.getId());
        }
        prevTagId = tvv.getId();
        tags.add(CreateTiffTag(tvv));
      }
      TiffTags tiffTags = new TiffTags();
      tiffTags.setTags(tags);
      ifd.setTags(tiffTags);
      ifd.setTagOrdering(correctTagOrdering ? 1 : 0);
      ifd.setDuplicateTags(duplicatedTags ? 1 : 0);
      tt.setExif(ifd);
    } else if (tv.getId() == 700) {
      // XMP
    } else if (tv.getId() == 33723) {
      // IPTC
      IPTC iptc = (IPTC)tv.getValue().get(0);
      Hashtable<String, String> keyvalues = new Hashtable<String, String>();
      Metadata meta = iptc.createMetadata();
      for (String key : meta.keySet())
      {
        keyvalues.put(key, meta.get(key).toString());
      }
      tt.setIptc(keyvalues);
    } else {
      tt.setValue(tv.toString());
    }
    return tt;
  }
}
