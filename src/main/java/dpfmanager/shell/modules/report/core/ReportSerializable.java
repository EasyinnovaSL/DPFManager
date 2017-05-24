package dpfmanager.shell.modules.report.core;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class ReportSerializable implements Serializable {

  public void write(String internal, String filename){
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
    ReportSerializable obj = null;
    FileInputStream fis = null;
    GZIPInputStream gis = null;
    ObjectInputStream ois = null;
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
