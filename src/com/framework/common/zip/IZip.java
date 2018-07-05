package com.framework.common.zip;


public interface IZip {
  /**
   * 压缩文件，支持整个目录压缩和文件压缩
   * @param  srcFile  源文件，可以是文件或路径路径,如c:\zxt\feedback
   * @param  dstFile  目标文件名
   * @return          true-成功，false-失败
   */
  public boolean zipFile(String srcFile, String dstFile);

  /**
   * 压缩文件，支持整个目录压缩和文件压缩
   * @param  srcFile        源文件，可以是文件或路径路径,如c:\ziptest\lala
   * @param  dstFile        目标文件名
   * @param  srcFileFilter  后缀名，如*.txt,*.zip等
   * @return                true-成功，false-失败
   */
  public boolean zipFile(String srcFile, String srcFileFilter, String dstFile);

  /**
   * 在已有的ZIP文件中添加一个文件
   * @param  dstFile       最后生成的zip文件
   * @param  srcZipFile    原始ZIP文件
   * @param  newEntryFile  要添加的文件（可以是文件夹）
   * @return               Description of the Returned Value
   */
  public boolean addZipEntry(String srcZipFile, String newEntryFile,
                             String dstFile);

  /**
   * 将文件解压缩到某个目录下
   * @param zipFileName String  目标路径位置
   * @param destDir String zip  文件名
   * @return boolean            true-成功,false-失败
   */
  public boolean unZipFile(String zipFileName, String destDir);
}
