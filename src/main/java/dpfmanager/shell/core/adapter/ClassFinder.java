package dpfmanager.shell.core.adapter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Adria Llorens on 22/12/2016.
 */
public class ClassFinder {


  public static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException, URISyntaxException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL packageURL;
    ArrayList<String> names = new ArrayList<String>();

    packageName = packageName.replace(".", "/");
    packageURL = classLoader.getResource(packageName);

    if (packageURL.getProtocol().equals("jar")) {
      String jarFileName;
      JarFile jf;
      Enumeration<JarEntry> jarEntries;
      String entryName;

      // build jar file name, then loop through zipped entries
      jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
      jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
      jf = new JarFile(jarFileName);
      jarEntries = jf.entries();
      while (jarEntries.hasMoreElements()) {
        entryName = jarEntries.nextElement().getName();
        if (entryName.startsWith(packageName) && !entryName.endsWith("/") && entryName.length() > packageName.length() + 5) {
          entryName = entryName.substring(packageName.length() +1, entryName.lastIndexOf('.'));
          names.add(entryName.replaceAll("/","."));
        }
      }

      // loop through files in classpath
    } else {
      URI uri = new URI(packageURL.toString());
      File folder = new File(uri.getPath());
      // won't work with path which contains blank (%20)
      // File folder = new File(packageURL.getFile());
      File[] contenuti = folder.listFiles();
      String entryName;
      for (File actual : contenuti) {
        entryName = actual.getName();
        entryName = entryName.substring(0, entryName.lastIndexOf('.'));
        names.add(entryName);
      }
    }
    return names;
  }


  private static final char PKG_SEPARATOR = '.';

  private static final char DIR_SEPARATOR = '/';

  private static final String CLASS_FILE_SUFFIX = ".class";

  private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

  public static List<Class<?>> find(String scannedPackage) {
    String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
    URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource("");
    if (scannedUrl == null) {
      throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
    }
    File scannedDir = new File(scannedUrl.getFile());
    List<Class<?>> classes = new ArrayList<Class<?>>();
    for (File file : scannedDir.listFiles()) {
      classes.addAll(find(file, scannedPackage));
    }
    return classes;
  }

  private static List<Class<?>> find(File file, String scannedPackage) {
    List<Class<?>> classes = new ArrayList<Class<?>>();
    String resource = scannedPackage + PKG_SEPARATOR + file.getName();
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        classes.addAll(find(child, resource));
      }
    } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
      int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
      String className = resource.substring(0, endIndex);
      try {
        classes.add(Class.forName(className));
      } catch (ClassNotFoundException ignore) {
      }
    }
    return classes;
  }

}