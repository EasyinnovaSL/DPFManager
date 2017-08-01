/**
 * <h1>ReportSerializable.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 */
package dpfmanager.shell.modules.report.core;

import com.easyinnova.tiff.model.TagValue;
import com.easyinnova.tiff.model.types.IFD;
import com.easyinnova.tiff.model.types.abstractTiffType;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class ReportSerializable implements Serializable {

  /**
   * Do not modify!
   */
  private static final long serialVersionUID = 6625L;

  private Integer version;

  public Integer getVersion(){
    return version;
  }

  public void write(String internal, String filename){
    version = 3;
    try {
      File internalFile = new File(internal);
      if (!internalFile.exists()) internalFile.mkdirs();
      FileOutputStream fos = new FileOutputStream(internal + "/" + filename);
      GZIPOutputStream gos = new GZIPOutputStream(fos);
      ObjectOutputStream oos = new ObjectOutputStream(gos);
      oos.writeObject(this);
      oos.flush();
      oos.close();
      gos.close();
      fos.close();
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public static ReportSerializable read(String file){
    ReportSerializable obj;
    FileInputStream fis;
    GZIPInputStream gis;
    ObjectInputStream ois;
    try {
      fis = new FileInputStream(file);
      gis = new GZIPInputStream(fis);
      ois = new ObjectInputStream(gis);
      obj = (ReportSerializable) ois.readObject();
      ois.close();
      gis.close();
      fis.close();
    } catch(Exception ioe) {
      System.err.println("Error in file: "+file);
      System.err.println("Error message: "+ioe.getMessage());
      obj = null;
    }
    return obj;
  }

  public void filter(){
    if (this instanceof IndividualReport){
      IndividualReport ir = (IndividualReport) this;
      IFD ifd = ir.getTiffModel().getFirstIFD();
      while (ifd != null) {                                 // Each IFD
        for (TagValue tv : ifd.getMetadata().getTags()){    // Each Tag
          if (tv.getFirstTextReadValue().isEmpty() && !tv.getName().equals("XMP") && !tv.getName().equals("ICCProfile") && !tv.getName().equals("SubIFDs") && !tv.getName().equals("ExifIFD")) {
            tv.setValueBackup(tv.getValue());
            tv.getValue().clear();
          }
        }
        ifd = ifd.getNextIFD();
      }
    }
  }

  public void defilter(){
    if (this instanceof IndividualReport){
      IndividualReport ir = (IndividualReport) this;
      IFD ifd = ir.getTiffModel().getFirstIFD();
      while (ifd != null) {                                 // Each IFD
        for (TagValue tv : ifd.getMetadata().getTags()){    // Each Tag
          if (tv.getValueBackup() != null && !tv.getValueBackup().isEmpty()) {
            tv.setValue(tv.getValueBackup());
          }
        }
        ifd = ifd.getNextIFD();
      }
    }
  }

}
