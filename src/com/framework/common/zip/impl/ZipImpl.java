package com.framework.common.zip.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.framework.common.zip.IZip;
import com.framework.util.ManageFile;

public class ZipImpl implements IZip {
  private static Log log = LogFactory.getLog(ZipImpl.class);
   private final int BUFFER = 2048;

  public static void main(String args[]) {
    ZipImpl t = new ZipImpl();
    //压缩一个文件
//    System.err.println(t.zipFile("C:\\testttt\\te2stttt.txt", "C:\\testttt\\lalala.zip"));
    //往一个已有的zip里添加文件
//    System.err.println(t.addZipEntry("c:/lalala.zip", "c:/ziptest", "c:/a2.zip"));
    //解压缩到指定目录
//    System.err.println(t.unZipFile("C:\\zz\\1\\2\\a2.zip", "c:\\zz\\1\\"));
    //压缩一个目录下的所有文件生成一个zip文件
   // System.err.println(t.zipFile("C:\\zz", "c:/axx.zip"));
  }

  /**
   *  压缩文件，支持整个目录压缩和文件压缩
   *
   *@param  srcFile  源文件，可以是文件或路径路径,如c:\zxt\feedback
   *@param  dstFile  目标文件名
   *@return          true-成功，false-失败
   */
  public boolean zipFile(String srcFile, String dstFile) {
    return zipFile(srcFile, "*", dstFile);
  }

  /**
   * 压缩文件，支持整个目录压缩和文件压缩
   * @param  srcFile        源文件，可以是文件或路径路径,如c:\ziptest\lala
   * @param  dstFile        目标文件名
   * @param  srcFileFilter  如*.txt,*.zip等
   * @return                true-成功，false-失败
   */
  public boolean zipFile(String srcFile, String srcFileFilter,
                                String dstFile) {
    try {
      if (new File(srcFile).isDirectory() &&
          !srcFile.substring(srcFile.length() - 1).equals(File.separator)) {
        srcFile = srcFile + File.separator;
      }
      File fn = new File(dstFile);
      if (!fn.exists()) {
        fn.createNewFile();
      }
      java.util.zip.ZipOutputStream zout = new java.util.zip.ZipOutputStream(new
          FileOutputStream(dstFile));
      zipFile(null, srcFile, srcFileFilter, zout);
      zout.close();
      return true;
    }
    catch (java.io.IOException ioex) {
      log.debug(ioex.getMessage());
      return false;
    }
  }

  /**
   *  将某个路径下的所有文件压缩---- private
   *@param  from     源路径,如c:\ziptest\1ala\
   *@param  zout     压缩输出流
   *@param  filters  Description of Parameter
   *@param  baseDir  压缩的基准目录
   */
  private void zipFile(String baseDir, String from, String filters,
                              ZipOutputStream zout) {
    String temp_to = null;
    String temp_from = null;
    String entryFileName = null;
    String entryName = "";
    /*
     *  压缩的基准目录
     */
    if (baseDir == null) {
      int idx = from.lastIndexOf(File.separator);
      if (idx == -1) {
        throw new IllegalArgumentException(
            "Argument Error:[from] is not a valid filename");
      }
      baseDir = from.substring(0, idx);
      System.err.println("from=" + from);
      System.err.println("baseDir=" + baseDir);
    }
    try {
      File f = new File(from);

      int filenumber;
      String[] strFile;
      File fTemp;
      if (f.isDirectory()) {
        strFile = f.list();
        for (int i = 0; i < strFile.length; i++) {
          if (from.endsWith(File.separator)) {
            temp_from = from + strFile[i];
          }
          else {
            temp_from = from + File.separator + strFile[i];
          }
          fTemp = new File(temp_from);
          if (fTemp.isDirectory()) {
            zipFile(baseDir, temp_from, filters, zout);
          }
          //if
          else {
            if (!ManageFile.checkFileNames(temp_from, filters)) {
              continue;
            }
            byte[] s = ManageFile.loadFromFile(temp_from);
            entryName = temp_from.substring(baseDir.length() + 1,
                                            temp_from.length());
            System.err.println("entrName=" + entryName);
            ZipEntry zefile = new ZipEntry(entryName);
            zefile.setSize(s.length);
            zout.putNextEntry(zefile);
            zout.write(s);
            zout.closeEntry();
          }
          //else
        }
        //for
      }
      else {
        //for single file
        if (!ManageFile.checkFileNames(from, filters)) {
          return;
        }
        byte[] s = ManageFile.loadFromFile(from);
        entryName = from.substring(baseDir.length() + 1,
                                   from.length());
        System.err.println("entrName=" + entryName);
        ZipEntry zefile = new ZipEntry(entryName);
        zefile.setSize(s.length);
        zout.putNextEntry(zefile);
        zout.write(s);
        zout.closeEntry();

      }
    }
    catch (Exception e) {
      log.debug(e.getMessage());
      throw new IllegalArgumentException("文件压缩失败!");
    }
  }

  /**
   *  在已有的ZIP文件中添加一个文件
   *@param  dstFile       最后生成的zip文件
   *@param  srcZipFile    原始ZIP文件
   *@param  newEntryFile  要添加的文件（可以是文件夹）
   *@return               Description of the Returned Value
   */
  public boolean addZipEntry(String srcZipFile, String newEntryFile,
                                    String dstFile) {
    java.util.zip.ZipOutputStream zout = null;
    InputStream is = null;
    try {
      if (new File(newEntryFile).isDirectory() &&
          !newEntryFile.substring(newEntryFile.length() -
          1).equals(File.separator)) {
        newEntryFile = newEntryFile + File.separator;
      }
      File fn = new File(dstFile);
      if (!fn.exists()) {
        fn.createNewFile();
      }
      zout = new java.util.zip.ZipOutputStream(new
                                               FileOutputStream(dstFile));
      /*
       *  先将原始的Entry放进去
       */
      ZipFile zipfile = new ZipFile(srcZipFile);
      ZipEntry entry = null;
      Enumeration e = zipfile.entries();
      byte[] buffer = new byte[1024];
      while (e.hasMoreElements()) {
        entry = (ZipEntry) e.nextElement();
        System.err.println(entry.getName());
        zout.putNextEntry(entry);
        is = new BufferedInputStream
            (zipfile.getInputStream(entry));
        int count;
        while ( (count = is.read(buffer, 0, 1024))
               != -1) {
          zout.write(buffer, 0, count);
          zout.flush();
        }
        is.close();
        zout.closeEntry();

      }
      /*
       *  再将新添加的文件放进去
       */
      zipFile(null, newEntryFile, "*.*", zout);
      zout.close();
      return true;
    }
    catch (java.io.IOException ioex) {
      log.debug(ioex.getMessage());
      ioex.printStackTrace();
      return false;
    }
    finally {
      try {
        if (zout != null) {
          zout.close();
        }
      }
      catch (Exception ex) {
      }
      try {
        if (is != null) {
          is.close();
        }
      }
      catch (Exception ex) {

      }
    }
  }

  /**
     *  将文件解压缩到某个目录下
     *@param  destDir      目标路径位置
     *@param  zipFileName  zip文件名
     *@return              true-成功,false-失败
     */
    public boolean unZipFile(String zipFileName, String destDir) {

        if (zipFileName == null) {
            throw new IllegalArgumentException("[zipname] is empty");
        }

        File destFile = new File(destDir);
        if (!destFile.isDirectory() || !destFile.exists()) {
            ManageFile.mkDir(destDir);
        }
        ZipFile zipfile = null;
        BufferedOutputStream dest = null;
        BufferedInputStream is = null;

        try {
            zipfile = new ZipFile(zipFileName);

            ZipEntry entry = null;
            int i = 0;
            byte data[] = new byte[BUFFER];
            int count;
            Enumeration e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();

                if (entry.isDirectory()) {
                    ManageFile.mkDir(destDir, entry.getName());

                } else {
                    is = new BufferedInputStream
                            (zipfile.getInputStream(entry));

                    String strFileName = destDir +
                            File.separator + entry.getName();
                    strFileName = ManageFile.refineFilePath(strFileName);
                    ManageFile.mkDir(ManageFile.getParentPath(strFileName));
                    //
                    //                    strFileName.replaceAll("\\\\","\\");
                    //                    ManageFile.saveFile(strFileName,"".getBytes());
                    dest = new
                            BufferedOutputStream(new FileOutputStream(strFileName), BUFFER);

                    while ((count = is.read(data, 0, BUFFER))
                            != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    is.close();

                }
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
            throw new IllegalArgumentException("文件解压缩失败!");
//            return false;
        } finally {
            //add by wjz 0917 ,to ensure zipfile closed
            try {
                dest.close();
            } catch (Exception ex) {
            }
            try {
                is.close();
            } catch (Exception ex) {
            }
            try {
                zipfile.close();
            } catch (Exception ex) {
            }
            //end add
        }
        return true;
    }


}
