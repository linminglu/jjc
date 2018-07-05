package com.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ManageFile {
  /**
    *  获取文件所在的路径 add by wjz 1013
    *
    *@param  fileAbsoluteName
    *@return
    */
   public static String getFileDir(String fileAbsoluteName) {
       if (fileAbsoluteName == null) {
           return "";
       }
       int pos1 = fileAbsoluteName.lastIndexOf("\\");
       int pos2 = fileAbsoluteName.lastIndexOf("/");
       int pos = Math.max(pos1, pos2);
       if (pos == -1) {
           return "";
       }
       return refineFilePath(fileAbsoluteName.substring(0, pos + 1));
   }


   /**
    *  获取文件的扩展名 add by wjz 031211
    *
    *@param  fileName  Description of the Parameter
    *@return
    */
   public static String getFileExt(String fileName) {
       if (fileName == null) {
           return "";
       }
       int pos = fileName.lastIndexOf(".");

       if (pos == -1) {
           return "";
       }
       return fileName.substring(pos + 1);
   }


   /**
    *  根据文件路径取得该文件路径下的所有文件名称：
    *
    *@param  filePath
    *@return           存放文件名称的数组
    */
   public static String[] getFileNames(String filePath) {
       if (filePath == null || filePath.length() <= 2) {
//       ShowDialog.ShowInformationDialog("getFileNames() 文件路径错误！", "错误",ShowDialog.ERROR_MESSAGE);//"文件路径错误！");
           return null;
       }
       File f = new File(filePath);
       if (f.isFile()) {
//      ShowDialog.ShowInformationDialog("getFileNames() 文件路径非文件夹！", "错误",ShowDialog.ERROR_MESSAGE);//"文件路径非文件夹！");
           return null;
       }
       String[] flName = f.list();
       f = null;
       return flName;
   }


   //end by add

   /**
    *  Gets the fileSeparator attribute of the ManageFile class
    *
    *@return    The fileSeparator value
    */
   public final static String getFileSeparator() {
       return File.separator;
   }


   /**
    *  根据文件路径取得该文件路径下的所有文件：
    *
    *@param  filePath
    *@return           存放文件的数组
    */
   public static File[] getFiles(String filePath) {
       if (filePath == null || filePath.length() <= 2) {
//      ShowDialog.ShowInformationDialog("getFiles() 文件路径错误！", "错误",ShowDialog.ERROR_MESSAGE);
//           System.out.println("文件路径错误！");
           return null;
       }
       File f = new File(filePath);
       if (f.isFile()) {
//      ShowDialog.ShowInformationDialog("getFiles() 文件路径非文件夹！", "错误",ShowDialog.ERROR_MESSAGE);
//           System.out.println("文件路径非文件夹！");
           return new File[]{};
       }
       File[] fls = f.listFiles();
       f = null;
       return fls;
   }
   
   /**
    * 查询目录下所有文件(不含目录)
    * @param filePath
    * @return
    */
   public static List<File> getAllFiles(String filePath) {
       File file=new File(filePath);
       List<File> files = new ArrayList<File>();
       printFiles(files,file);
       return files;
   }
   
   public static void printFiles(List<File> files,File f){
       if(f!=null){
           if(f.isDirectory()){
               File[] fileArray=f.listFiles();
               if(fileArray!=null){
                   for (int i = 0; i < fileArray.length; i++) {
                       //递归调用
                   	printFiles(files,fileArray[i]);
                   }
               }
           }
           else{
           	files.add(f);
           }
       }
   }
   
   /**
    *  获取父目录
    *
    *@param  strFileName
    *@return
    */
   public static String getParentPath(String strFileName) {
       try {
           String parentPath = strFileName;
           while (parentPath.lastIndexOf(File.separator) == (parentPath.length() - 1)
                   || parentPath.lastIndexOf("/") == (parentPath.length() - 1)) {
               parentPath = parentPath.substring(0, parentPath.length() - 2);
           }
           int idx = parentPath.lastIndexOf(File.separator);
           parentPath = parentPath.substring(0, idx);
           return parentPath;
       } catch (Exception ex) {
//           System.out.println(ex.getMessage());
           return null;
       }

   }


   //end by add
   /**
    *  获取文件名（绝对路径）的相对文件名 add by wjz 1013
    *
    *@param  fileAbsoluteName
    *@return
    */
   public static String getRelativeFileName(String fileAbsoluteName) {
       if (fileAbsoluteName == null) {
           return null;
       }
       int pos1 = fileAbsoluteName.lastIndexOf("\\");
       int pos2 = fileAbsoluteName.lastIndexOf("/");
       int pos = Math.max(pos1, pos2);
       if (pos == -1) {
           return fileAbsoluteName;
       }
       return fileAbsoluteName.substring(pos + 1);
   }


   /**
    *  filePath路径下是否存在文件fn
    *
    *@param  filePath
    *@param  fn        Description of the Parameter
    *@return           存放文件名称的数组
    */
   public static boolean isFileExist(String filePath, String fn) {
       if (filePath == null || fn == null) {
           return false;
       }
       String absPath = null;
       if (filePath.endsWith("/") || filePath.endsWith("\\")) {
           absPath = filePath + fn;
       } else {
           absPath = filePath + File.separator + fn;
       }
       return (new File(absPath)).exists();
   }


   /**
    *  文件fn是否存在
    *
    *@param  fn
    *@return
    */
   public static boolean isFileExist(String fn) {
       if (fn == null) {
           return false;
       }
       return (new File(fn)).exists();
   }



   /**
    *  create new file
    *
    *@param  fileName
    *@return           true is success
    */
   public static boolean CreateFile(String fileName) {
       boolean blret = false;
       try {
           File f = new File(fileName);
           if (f.exists()) {
               if (f.isDirectory()) {
//                   System.out.println("file:" + fileName +
//                           "  exist and is a directory");
                   return false;
               } else {
                   f.delete();
               }
           }
           return f.createNewFile();
       } catch (Exception e) {
//           System.out.println(e.getMessage());
           blret = false;
       }
       return blret;

   }


   /**
    *  删除文件
    *
    *@param  dirName    源文件或目录
    *@param  isRecurse  如果是目录,是否删除其下的子目录
    *@return            true-成功,false-失败
    */
   public static boolean DeleteFile(String dirName, boolean isRecurse) {
       boolean blret = false;
       try {
           File f = new File(dirName);
           if (f.isFile()) {
               blret = f.delete();
               return blret;
           }

           int filenumber = f.listFiles().length;
           File[] fchilda = f.listFiles();

           for (int i = 0; i < filenumber; i++) {
               File fchild = fchilda[i];
               if (fchild.isFile()) {
                   blret = fchild.delete();
                   if (!blret) {
                       break;
                   }
               } else if (isRecurse) {
                   blret = DeleteFile(fchild.getAbsolutePath(), true);
               }
           }
           blret = new File(dirName).delete();

       }
       //try
       catch (Exception e) {
//      LogUtil.getLogger().error(e.getMessage(), e);
           blret = false;
       }
       return blret;
       //catch
   }


   /**
    *  从fn中读取所有内容(返回为字符串格式) <p>
    *
    *  wujinzhong 0531<p>
    *
    *
    *
    *@param  fn  源文件名
    *@return     byte[]
    *@roseuid    3C297D710384
    */
   public static String ReadFile(String fn) {
       if (java.util.Locale.getDefault().toString().indexOf("CN") > -1) {
           return new String(loadFromFile(fn));
       } else {
           return StringUtil.convertToChinese(new String(loadFromFile(fn)));
       }
   }
   
   public static String ReadFileUTF8(String fn) {
	   return StringUtil.convertToUTF8(new String(loadFromFile(fn)));
   }


   /**
    *  读配置文件 searchstr="stic"; srcPath="c:\zxtic.ini"
    *
    *@param  searchstr  Description of the Parameter
    *@param  srcPath    Description of the Parameter
    *@return            Description of the Return Value
    */
   public static String ReadINI(String searchstr, String srcPath) {
       String str = null;
       String temp = null;
       BufferedReader br = null;
       searchstr = searchstr.toLowerCase();
       try {
           br = new BufferedReader(new InputStreamReader(new FileInputStream(srcPath)));
           //"C:/zxtic.ini"
           str = br.readLine();
           while (str != null) {
               str = str.trim();
               if (str.length() > 0 && !str.startsWith("#")) {
                   if (str.toLowerCase().indexOf(searchstr) >= 0) {
                       temp = str.substring(str.indexOf("=") + 1);
                       temp = temp.replace('\\', '/');
                       break;
                   }
               }
               str = br.readLine();
           }

       } catch (Exception e) {
           System.out.println(e.toString());
           e.printStackTrace();
       } finally {
           try {
               br.close();
           } catch (Exception ex) {

           }
       }

       return temp;
   }


   //add by henson 20030605
   /**
    *  查找压缩文件
    *
    *@param  bFlag       搜索位置标志 true：从左到右 false：从右到左
    *@param  sSearchStr  要搜索的字符串 return 文件名
    *@param  sFilePath   Description of the Parameter
    *@return             Description of the Return Value
    */
   public static String SearchFile(String sSearchStr, boolean bFlag, String sFilePath) {
       try {
           String sFileName = "";
           String[] fls = ManageFile.getFileNames(sFilePath);
           if (fls != null && fls.length > 0) {
               for (int i = 0; i < fls.length; i++) {
                   if (bFlag) {
                       if (((String) fls[i]).toLowerCase().indexOf(sSearchStr.toLowerCase()) >= 0) {
                           sFileName = fls[i].toString();
                           break;
                       }
                   } else {
                       if (fls[i].toString().toLowerCase().lastIndexOf(sSearchStr.toLowerCase()) >= 0) {
                           sFileName = fls[i].toString();
                           break;
                       }
                   }
               }
           }
           return sFileName;
       } catch (Exception e) {
           return "";
       }
   }


   /**
    *  检查文件名是否满足Windows风格的通配符<p>
    *
    *  如*表示任意多个字符，？表示任意一个字符<p>
    *
    *  add by wjz 031211 <p>
    *
    *
    *
    *@param  fileName  Description of the Parameter
    *@param  filters   Description of the Parameter
    *@return
    */
   public static boolean checkFileNames(String fileName, String filters) {

       fileName = getRelativeFileName(fileName);
       if (fileName == null || filters == null) {
           return false;
       }
       String[] arr_filters = filters.split(",");
       //没有通配符，表示任意字符
       if (arr_filters == null || arr_filters.length == 0) {
           return true;
       }
       int len = arr_filters.length;
       String filter = "";
       for (int i = 0; i < len; i++) {
           filter = arr_filters[i];
           filter = filter.trim();
           //java中,"."表示任意字符
           filter = filter.replaceAll("\\.", "\\\\.");
           filter = filter.replaceAll("\\*", "\\.\\*");
           filter = filter.replace('?', '.');
//     System.err.println(filter);
           if (fileName.matches(filter)) {
               return true;
           }
       }

       return false;
   }



//  end by add--------------------------------------------------------------------------------------------------------------------------------
   /**
    *  本方法主要用于将一个文件拷贝到另一个文件件
    *
    *@param  from                                       源文件
    *@param  to                                         目的文件
    *@return
    *      返回字节数,-1:读文件错误，－2：文件未找到，-3： IO错误
    *@exception  java.io.FileNotFoundException          Description of the
    *      Exception
    *@throws  java.io.FileNotFoundException,抛出文件没有发现异常
    *@throws  java.io.IOException                       抛出IO异常
    */
   public final static int copyFile(String from, String to) throws
           java.io.FileNotFoundException, java.io.IOException {
       java.io.FileInputStream is = null;
       java.io.FileOutputStream os = null;
       int n = 0;
       try {
           is = new java.io.FileInputStream(from);
           byte[] bts = new byte[is.available()];
           int len = bts.length;
           os = new java.io.FileOutputStream(to);
           int len1 = is.read(bts, 0, bts.length);
           os.write(bts, 0, bts.length);
           bts = null;
           n = len == len1 ? len : -1;

       } catch (java.io.FileNotFoundException fe) {
           System.out.println(fe.getMessage());
           n = -2;

       } catch (java.io.IOException ie) {
           System.out.println(ie.getMessage());
           n = -3;
       } finally {
           try {
               is.close();
           } catch (Exception ex) {
        	   System.out.println(ex.getMessage());
           }
           try {
               os.close();
           } catch (Exception ex) {
        	   System.out.println(ex.getMessage());
           }
       }
       return n;
   }


   //add by hejiang 20030915
   /**
    *  Description of the Method
    *
    *@param  srcFileDir   Description of the Parameter
    *@param  destFileDir  Description of the Parameter
    */
   public static void copyFileToIcDir(String srcFileDir, String destFileDir) {
       String[] srcFiles = getFileNames(srcFileDir);
       try {
           if (srcFiles == null || srcFiles.length == 0) {
               return;
           }
           //if
           else {
               for (int i = 0; i < srcFiles.length; i++) {
                   if (!srcFiles[i].endsWith(".zip")) {
                       copyFile(srcFileDir + "\\" + srcFiles[i],
                               destFileDir + "\\" + srcFiles[i]);
                   }
                   //if
               }
               //for
           }
           //else
       }
       //try
       catch (Exception e) {
           System.out.println("拷贝文件出错！");
       }
   }


   /**
    *  将输入流拷贝到输出流
    *
    *@param  in            输入流
    *@param  out           输出流
    *@throws  IOException
    */
   public final static void copyStream(InputStream in, OutputStream out) throws
           IOException {
       int count;
       for (count = 0; ; count++) {
           int ch = in.read();
           if (ch == -1) {
               break;
           }
           out.write(ch);
       }
       in.close();
       out.flush();
       out.close();
   }


   /**
    *  从fn中读取所有内容
    *
    *@param  fn  源文件名
    *@return     byte[]
    *@roseuid    3C297D710384
    */
   public static synchronized byte[] loadFromFile(String fn) {
       try {
           FileInputStream fis = new FileInputStream(fn);
           byte[] data = new byte[fis.available()];
           fis.read(data, 0, data.length);
           fis.close();
           return data;
       } catch (IOException e) {
           System.out.println(e.getMessage());
           return null;
       }
   }

	public static String loadTextFile(String fn) {
		try {
			InputStreamReader read = new InputStreamReader (new FileInputStream(fn),"UTF-8");
			BufferedReader reader=new BufferedReader(read);
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 读取自定义配置文件的访求，去掉 // 注释的行
	 * @param fn
	 * @return
	 */
	public static String loadTextFileConfig(String fn) {
		try {
			InputStreamReader read = new InputStreamReader (new FileInputStream(fn),"UTF-8");
			BufferedReader reader=new BufferedReader(read);
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				if(line.indexOf("//")==-1){
					sb.append(line);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String loadTextFileGBK(String fn) {
		try {
			InputStreamReader read = new InputStreamReader (new FileInputStream(fn),"GBK");
			BufferedReader reader=new BufferedReader(read);
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			read.close();
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
	public static String loadTextFileUTF8(String fn) {
		try {
			InputStreamReader read = new InputStreamReader (new FileInputStream(fn),"UTF-8");
			BufferedReader reader=new BufferedReader(read);
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line+"\n");
			}
			reader.close();
			read.close();
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
   /**
    *  从fn中读取所有内容
    *
    *@param  fn  源文件名
    *@return     byte[]
    *@roseuid    3C297D710384
    */
   public static byte[] loadFromFile(File fn) {
       try {
           FileInputStream fis = new FileInputStream(fn);
           byte[] data = new byte[fis.available()];
           fis.read(data, 0, data.length);
           fis.close();
           return data;
       } catch (IOException e) {
//    ShowDialog.ShowInformationDialog("loadFromFile() 读文件出错！", "错误",ShowDialog.ERROR_MESSAGE);//"文件路径错误！");
           System.out.println(e.getMessage());
           return null;
       }
   }


   /**
    *@param  args  The command line arguments
    */
   public static void main(String[] args) {
//System.err.println(getRelativeFileName("c:/b/c/\\aaa.doc"));
       System.err.println(getFileExt("c:/b/c/\\aaa.doc"));
       String s = "aa.eoc";
       String f = "*.doc,*.exe,a*.eoc";
       System.err.println("f=" + f);
       System.err.println(checkFileNames(s, f));
   }


   /**
    *  创建文件路径
    *
    *@param  baseDir  该目录所在的父目录
    *@param  dirName  目录名
    *@return          true-成功;False-不成功
    */
   public static boolean mkDir(String baseDir, String dirName) {
       return mkDir(baseDir + File.separator + dirName);
   }


   /**
    *  创建文件路径
    *
    *@param  destDir  目录名
    *@return          true-成功;False-不成功
    */
   public static boolean mkDir(String destDir) {
       boolean blRet = false;
       int idx = destDir.lastIndexOf(File.separator);
       if (destDir.lastIndexOf("/") == -1 && destDir.lastIndexOf("\\") == -1) {
           throw new java.lang.IllegalArgumentException("输入参数不是一个合法的路径");
       }
       return new File(destDir).mkdirs();
   }


   /**
    *  从输入流中所有内容到数组中
    *
    *@param  is  Description of Parameter
    *@return     Description of the Returned Value
    */
   public static byte[] readToBytes(InputStream is) {
       byte buffer[] = null;
       int SIZE = 1024 * 1024;
       ByteArrayOutputStream bao = null;
       try {

           bao = new ByteArrayOutputStream();

           buffer = new byte[SIZE];
           BufferedInputStream in = new BufferedInputStream(is, 1024 * 1024);
           int iBytes = 0;
           while (iBytes != -1) {
               iBytes = in.read(buffer, 0, SIZE);
               if (iBytes != -1) {
                   bao.write(buffer, 0, iBytes);
               }
           }

           return bao.toByteArray();
       } catch (java.io.IOException ex) {
           System.out.println(ex.getMessage());
           return null;
       } finally {
           try {
               bao.close();
               System.out.println("close ByteArrayOutputStream");
           } catch (Exception ex2) {

           }

       }

   }


   /**
    *  Description of the Method
    *
    *@param  fn  Description of the Parameter
    *@return     Description of the Return Value
    */
   public static String refineFilePath(String fn) {
       String s = fn.replace('\\', File.separatorChar);
       s = fn.replace('/', File.separatorChar);
       int i = s.indexOf("//");
       if (i > 0) {
           s = s.substring(0, i) + s.substring(i + 1);
       }
       i = s.indexOf("\\\\");
       if (i > 0) {
           s = s.substring(0, i) + s.substring(i + 1);
       }
       return s;
   }


   /**
    *  将data写到fn中 fn为绝对路径
    *
    *@param  fn    保存文件名
    *@param  data  数据
    *@return       int
    *@since        2001-12-25
    *@roseuid      3C297D4D0102
    */
   public static int saveFile(String fn, byte[] data) {
       try {
           FileOutputStream fos = new FileOutputStream(fn);
           fos.write(data, 0, data.length);
           fos.flush();
           fos.close();
           return 0;
       } catch (IOException e) {
           System.out.println(e.getMessage());
           return 100;
       }
   }


   /**
    *  yql2003－08－27
    *
    *@param  text      文件内容
    *@param  fileName  文件名称
    *@param  append    是否追加的文件的尾部 如果append=true 则将内容追加的文件的尾部 否则，从头开始
    *@return           Description of the Return Value
    */
   public final static boolean writeTextToFile(String text, String fileName, boolean append) {
       boolean flags = append;
       try {
           Writer write = new FileWriter(fileName, flags);
           write.write(text);
           write.flush();
           write.close();
           return true;
       } catch (IOException ex) {
           System.err.println(ex.getMessage());
           return false;
       }
   }
   
   public static boolean writeUTF8File(String filePath,String fileBody){  
       FileOutputStream fos = null;  
       OutputStreamWriter osw = null;  
       try {  
           fos = new FileOutputStream(filePath);  
           osw = new OutputStreamWriter(fos, "UTF-8");  
           osw.write(fileBody);  
           return true;  
       } catch (Exception e) {  
           e.printStackTrace();  
           return false;  
       }finally{  
           if(osw!=null){  
               try {  
                   osw.close();  
               } catch (IOException e1) {  
                   e1.printStackTrace();  
               }  
           }  
           if(fos!=null){  
               try {  
                   fos.close();  
               } catch (IOException e1) {  
                   e1.printStackTrace();  
               }  
           }  
       }  
   }
   
   /**
    * 创建目录
    * @param folder
    * @throws Exception
    */
   public static void createFolder(String folder) {
	    boolean success = true;
	    File fileDirectory = new File(folder);
	    if (!fileDirectory.exists()) {
	      success = fileDirectory.mkdirs();
	    }
	    fileDirectory = null;
	    if (!success) {
	    	System.out.println("文件目录："+folder+"没有创建成功");
	    }
	    else {
	      return;
	    }
	  }
}
