package dpfmanager.shell.modules.report.core;

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
    version = 1;
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
      ioe.printStackTrace();
      obj = null;
    }
    return obj;
  }

}
