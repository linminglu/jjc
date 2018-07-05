package com.apps.util; 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
public class ImageCut {
 /** *//**
  * 缩放图像
  * 
  * @param srcImageFile
  *            源图像文件地址
  * @param result
  *            缩放后的图像地址
  * @param scale
  *            缩放比例
  * @param flag
  *            缩放选择:true 放大; false 缩小;
  */
 public static void scale(String srcImageFile, String result, double scale) {
  try {
   BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
   int width = src.getWidth(); // 得到源图宽

   int height = src.getHeight(); // 得到源图长

   
   width = (int)((double)width * scale);
   height = (int)((double)height * scale);
   
   Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);
   BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
   Graphics g = tag.getGraphics();
   g.drawImage(image, 0, 0, null); // 绘制缩小后的图

   g.dispose();
   ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
 
 /** *//**
  * 缩放图像成固定宽度

  * 
  * @param srcImageFile
  *            源图像文件地址
  * @param result
  *            缩放后的图像地址
  * @param picWidth
  *            缩放后的图像宽度
  */
 public static long scaleAlbumPic(String srcImageFile, String result,int picWidth) {
  try {
	  BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
	  
   int width = src.getWidth(); // 得到源图宽

   int height = src.getHeight(); // 得到源图长

   double scale;
   ////System.out.println(width);
   if(width>picWidth){
	   scale = (double)picWidth/(double)width;
	  // //System.out.println(scale);
	   width = (int)((double)width * scale);
	   height = (int)((double)height * scale);
	   
	   Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);
	   BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	   Graphics g = tag.getGraphics();
	   g.drawImage(image, 0, 0, null); // 绘制缩小后的图

	   g.dispose();
	   ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
	   return new File(result).length();
   }
  } catch (IOException e) {
   e.printStackTrace();
  }
  return 0;
 }
 
 /** *//**
  * 缩放图像成固定宽度

  * 
  * @param srcImageFile
  *            源图像文件地址
  * @param result
  *            缩放后的图像地址
  * @param picWidth
  *            缩放后的图像宽度
  */
public static void scaleAlbumSmallPic(String srcImageFile, String result,int picWidth,int picHeight) {
	try {
		BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
		int width = src.getWidth(); // 得到源图宽

		int height = src.getHeight(); // 得到源图长

		double scale;
		double srcScale = (double)width/(double)height;
		double newScale = (double)picWidth/(double)picHeight;
		if(srcScale>newScale){
			scale=(double)picWidth/(double)width;
		}else{
			scale=(double)picHeight/(double)height;
		}
		scale = scale>1?1:scale;
		width = (int)((double)width * scale);
		height = (int)((double)height * scale);
		Image image = src.getScaledInstance(width, height,Image.SCALE_DEFAULT);
		BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.dispose();
		ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		src.flush();
		tag.flush();
		image.flush();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
 
 public static void scale2(String srcImageFile, String result, int newWidth,
		   boolean flag) {
		  try {
		   BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
		   int width = src.getWidth(); // 得到源图宽

		   int height = src.getHeight(); // 得到源图长

		   double beilv = (double)1;
		   if(width>newWidth){
			   beilv = (double)width/(double)newWidth;
		   }
		   if (flag) {
		    // 放大
		    width = (int)((double)width * beilv);
		    height = (int)((double)height * beilv);
		   } else {
		    // 缩小
		    width = (int)((double)width / beilv);
		    height = (int)((double)height / beilv);
		   }
		   Image image = src.getScaledInstance(width, height,
		     Image.SCALE_DEFAULT);
		   BufferedImage tag = new BufferedImage(width, height,
		     BufferedImage.TYPE_INT_RGB);
		   Graphics g = tag.getGraphics();
		   g.drawImage(image, 0, 0, null); // 绘制缩小后的图

		   g.dispose();
		   ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		 }
 
 public static void scale1(File srcImageFile, String result, double scale,
		   boolean flag) {
		  try {
		   BufferedImage src = ImageIO.read(srcImageFile); // 读入文件
		   int width = src.getWidth(); // 得到源图宽

		   int height = src.getHeight(); // 得到源图长


		   if (flag) {
		    // 放大
		    width = (int)((double)width * scale);
		    height = (int)((double)height * scale);
		   } else {
		    // 缩小
		    width = (int)((double)width / scale);
		    height = (int)((double)height / scale);
		   }
		   Image image = src.getScaledInstance(width, height,
		     Image.SCALE_DEFAULT);
		   BufferedImage tag = new BufferedImage(width, height,
		     BufferedImage.TYPE_INT_RGB);
		   Graphics g = tag.getGraphics();
		   g.drawImage(image, 0, 0, null); // 绘制缩小后的图

		   g.dispose();
		   ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		 }
 /** *//**
  * 图像切割
  * 
  * @param srcImageFile
  *            源图像地址
  * @param descDir
  *            切片目标文件夹

  * @param destWidth
  *            目标切片宽度
  * @param destHeight
  *            目标切片高度
  */
 public static void cut(String srcImageFile, String descDir, int destWidth,
   int destHeight) {
  try {
   Image img;
   ImageFilter cropFilter;
   // 读取源图像

   BufferedImage bi = ImageIO.read(new File(srcImageFile));
   int srcWidth = bi.getHeight(); // 源图宽度
   int srcHeight = bi.getWidth(); // 源图高度
   if (srcWidth > destWidth && srcHeight > destHeight) {
    Image image = bi.getScaledInstance(srcWidth, srcHeight,
      Image.SCALE_DEFAULT);
    destWidth = 200; // 切片宽度
    destHeight = 150; // 切片高度
    int cols = 0; // 切片横向数量
    int rows = 0; // 切片纵向数量
    // 计算切片的横向和纵向数量
    if (srcWidth % destWidth == 0) {
     cols = srcWidth / destWidth;
    } else {
     cols = (int) Math.floor(srcWidth / destWidth) + 1;
    }
    if (srcHeight % destHeight == 0) {
     rows = srcHeight / destHeight;
    } else {
     rows = (int) Math.floor(srcHeight / destHeight) + 1;
    }
    // 循环建立切片
    // 改进的想法:是否可用多线程加快切割速度
    for (int i = 0; i < rows; i++) {
     for (int j = 0; j < cols; j++) {
      // 四个参数分别为图像起点坐标和宽高
      // 即: CropImageFilter(int x,int y,int width,int height)
      cropFilter = new CropImageFilter(j * 200, i * 150,
        destWidth, destHeight);
      img = Toolkit.getDefaultToolkit().createImage(
        new FilteredImageSource(image.getSource(),
          cropFilter));
      BufferedImage tag = new BufferedImage(destWidth,
        destHeight, BufferedImage.TYPE_INT_RGB);
      Graphics g = tag.getGraphics();
      g.drawImage(img, 0, 0, null); // 绘制缩小后的图

      g.dispose();
      // 输出为文件

      ImageIO.write(tag, "JPEG", new File(descDir
        + "pre_map_" + i + "_" + j + ".jpg"));
     }
    }
   }
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
 /** *//**
  * 图像类型转换
     * GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
     */
 public static void convert(String source, String result) {
  try {
   File f = new File(source);
   f.canRead();
   f.canWrite();
   BufferedImage src = ImageIO.read(f);
   ImageIO.write(src, "JPG", new File(result));
  } catch (Exception e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }
 /** *//**
  * 彩色转为黑白
  * 
  * @param source
  * @param result
  */
 public static void gray(String source, String result) {
  try {
   BufferedImage src = ImageIO.read(new File(source));
   ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
   ColorConvertOp op = new ColorConvertOp(cs, null);
   src = op.filter(src, null);
   ImageIO.write(src, "JPEG", new File(result));
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
 /** *//** */
 /** *//**
  * 图像切割（改）

  * 
  * @param srcImageFile
  *            源图像地址
  * @param x
  *            目标切片起点x坐标
  * @param y
  *            目标切片起点y坐标
  * @param destWidth
  *            目标切片宽度
  * @param destHeight
  *            目标切片高度
  */
 public static void abscut(String srcImageFile,String outImageFile, int x, int y, int destWidth,
         int destHeight) {
     try {
         Image img;
         ImageFilter cropFilter;
         // 读取源图像

         BufferedImage bi = ImageIO.read(new File(srcImageFile));
         int srcWidth = bi.getWidth(); // 源图宽度
         int srcHeight = bi.getHeight(); // 源图高度
         
//         //System.out.println("srcWidth= " + srcWidth + "\tsrcHeight= "
//                 + srcHeight);
         if (srcWidth >= destWidth && srcHeight >= destHeight) {
             Image image = bi.getScaledInstance(srcWidth, srcHeight,
                     Image.SCALE_DEFAULT);
             // 改进的想法:是否可用多线程加快切割速度
             // 四个参数分别为图像起点坐标和宽高
             // 即: CropImageFilter(int x,int y,int width,int height)
             cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
             img = Toolkit.getDefaultToolkit().createImage(
                     new FilteredImageSource(image.getSource(), cropFilter));
             BufferedImage tag = new BufferedImage(destWidth, destHeight,
                     BufferedImage.TYPE_INT_RGB);
             Graphics g = tag.getGraphics();
             
             g.drawImage(img, 0, 0, null); // 绘制缩小后的图

             g.dispose();
             // 输出为文件

             ImageIO.write(tag, "JPEG", new File(outImageFile));
         }
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
 
//添加水印,filePath 源图片路径， watermark 水印图片路径
 public static boolean createMark(String filePath,String watermark) {
 ImageIcon imgIcon=new ImageIcon(filePath);
 Image theImg =imgIcon.getImage();
 ImageIcon waterIcon=new ImageIcon(watermark);
 Image waterImg =waterIcon.getImage();
 int width=theImg.getWidth(null);
 int height= theImg.getHeight(null);
 BufferedImage bimage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB); 
 Graphics2D g=bimage.createGraphics();
 g.setColor(Color.red);
 g.setBackground(Color.white);
 g.drawImage(theImg, 0, 0, null );
 g.drawImage(waterImg, 100, 100, null );
 g.drawString("12233sdfsdfsdfasf啊沙发沙发",500,500); //添加文字
 g.dispose();
 try{
 FileOutputStream out=new FileOutputStream(filePath);
 JPEGImageEncoder encoder =JPEGCodec.createJPEGEncoder(out); 
 JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage); 
 param.setQuality(50f, true); 
 encoder.encode(bimage, param); 
 out.close();
 }catch(Exception e){ return false; }
 return true;
 }
 /** *//**
  * @param args
  */
 public static void main(String[] args) {
  //cut("e:/1.jpg", "e:/t/", 200, 150);
//	 ImageCut.scaleAlbumPic("d:/Blue hills.jpg","D:/temp/Blue hills.jpg",800);
	 createMark("d:/4817539_8332878.jpg","d:/Sunset.jpg");
 }
}