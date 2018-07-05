package com.framework.util;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.framework.common.properties.impl.ReadPropertiesImpl;
public class CreateXlslHelper{
	public WritableFont font1= new WritableFont(WritableFont.SIMHEI,12,WritableFont.BOLD);  
	public WritableFont font2= new WritableFont(WritableFont.SIMHEI,10,WritableFont.BOLD);
	public WritableFont font3= new WritableFont(WritableFont.SIMHEI,9,WritableFont.BOLD);
	//大标题格式
	public WritableCellFormat format1=new WritableCellFormat(font1); 	  
	//正文字体，居中
	public WritableCellFormat format2=new WritableCellFormat(font2);
	//正文字体，居左
	public WritableCellFormat format3=new WritableCellFormat(font2);
	//bottom字体，居左。
	public WritableCellFormat format4=new WritableCellFormat(font3);
	//正文字体，居左，居上。
	public WritableCellFormat format5=new WritableCellFormat(font3);
	public CreateXlslHelper(){
		try{
		this.format2.setAlignment(Alignment.CENTRE);
		this.format1.setAlignment(Alignment.CENTRE);
		this.format5.setVerticalAlignment(VerticalAlignment.TOP);
		//this.format1.setBorder(Border.ALL, BorderLineStyle.THIN);
		this.format2.setBorder(Border.ALL, BorderLineStyle.THIN); 
		//this.format3.setBorder(Border.ALL, BorderLineStyle.THIN);
		//this.format4.setBorder(Border.ALL, BorderLineStyle.THIN);
		//this.format5.setBorder(Border.ALL, BorderLineStyle.THIN);
		}catch(Exception e) {
//		
			e.printStackTrace();
		}
	}
	public WritableWorkbook createBook(String path,String fileName){
		WritableWorkbook book = null;
		try{			

			
			File dir = new File(path);
		    if (!dir.exists())
		        dir.mkdir();
			book = Workbook.createWorkbook(new File(path+fileName)); 
		}catch(Exception e) 
		{ 
		
		e.printStackTrace();
		} 
		return book;
	}
	
	public void setSheetColumnWidth(WritableSheet sheet,int[] columnWidths){
		for(int i=0;i<columnWidths.length;i++){
			sheet.setColumnView(i,columnWidths[i]);
		}
	}
	
	public void setSheetHeader(WritableSheet sheet,int[] columnWidths,String[] headerStrings){
		this.setSheetColumnWidth(sheet,columnWidths);
//		在Label对象的构造子中指名单元格位置是第一列第一行(0,0) 	
		//定义字体格式
		try{
//			WritableFont font1= new WritableFont(WritableFont.SIMHEI,16,WritableFont.BOLD);  
//			WritableCellFormat format1=new WritableCellFormat(font1); 
			//第一行七列合并
			//	参数说明：
			//	第一个0：是第0行。
			//	第二个0：第0列
			//	第三个1：合并到第几列。
			//	第四个2：合并到第几行。
			sheet.mergeCells(0,0,6,0);
			//填写内容
			Label labelTitle1=new Label(0,0,headerStrings[0],this.format1);	
			
			//	=========================第二行=================================
//			WritableFont font2= new WritableFont(WritableFont.SIMHEI,13,WritableFont.BOLD);  
//			WritableCellFormat format2=new WritableCellFormat(font2); 
			//第二行七列合并
			sheet.mergeCells(0,1,6,1);
			//填写内容
			Label labelTitle2=new Label(0,1,headerStrings[1],format3);
			
			
			//=========================第三行=================================
			//第三行，分别合并前三列和后四列。
			sheet.mergeCells(0,2,2,2);
			sheet.mergeCells(3,2,6,2);
			//填写内容
			Label labelTitle3_1=new Label(0,2,headerStrings[2],format3);
			Label labelTitle3_2=new Label(3,2,headerStrings[3],format3);
			
			
			//=========================第四行=================================
			//第四行，合并七列。
			sheet.mergeCells(0,3,6,3);	
			//填写内容
			Label labelTitle4=new Label(0,3,headerStrings[4],format3);
			
	
			//=========================第五行=================================
			//第五行，合并七列。
			sheet.mergeCells(0,4,6,4);
			//填写内容
			Label labelTitle5=new Label(0,4,headerStrings[5],format3);
			
			//将定义好的单元格添加到工作表中 
			sheet.addCell(labelTitle1); 
			sheet.addCell(labelTitle2);
			sheet.addCell(labelTitle3_1);
			sheet.addCell(labelTitle3_2);
			sheet.addCell(labelTitle4);
			sheet.addCell(labelTitle5);
		}catch(Exception e) 
		{ 
		
		e.printStackTrace();
		} 
	}
	
	public void setSheetDataTableTitle(WritableSheet sheet,int row,String[] tableTitles){
		try{
			//增加表头
			Label labelTableTitle = null;
			for(int i=0;i<tableTitles.length;i++){
				labelTableTitle = new Label(i,row,tableTitles[i],format2);
				sheet.addCell(labelTableTitle);
			}
		}catch(Exception e) 
		{ 
//		System.out.println("in the method setLabelTitle ,the error info is:"+e.getMessage()); 
		e.printStackTrace();
		} 
	}
	
	public void setSheetData(WritableSheet sheet,int row,String[] datas,WritableCellFormat formats){
		try{
			//增加数据
			Label dataCell = null;
			for(int i=0;i<datas.length;i++){
				dataCell = new Label(i,row,datas[i],formats);
				sheet.addCell(dataCell);
			}
		}catch(Exception e) 
		{ 
//		System.out.println("in the method setLabelTitle ,the error info is:"+e.getMessage()); 
		e.printStackTrace();
		} 
	}
	
	public void setSheetBottom(WritableSheet sheet,int row,String[] bottomStrings){
		try{
			//bottom第一行七列合并 
			sheet.mergeCells(0,row,6,row);
			//填写内容
			Label bottom1=new Label(0,row,bottomStrings[0],format4);
//			=========================第二行=================================
			sheet.mergeCells(0,row+1,6,row+2);
			Label bottom2=new Label(0,row+1,bottomStrings[1],format5);
//			=========================第三行=================================			
			sheet.mergeCells(0,row+3,2,row+3);
			Label bottom3_1=new Label(0,row+3,bottomStrings[2],format4);
			sheet.mergeCells(3,row+3,4,row+3);
			Label bottom3_2=new Label(3,row+3,bottomStrings[3],format4);
			sheet.mergeCells(5,row+3,6,row+3);
			Label bottom3_3=new Label(5,row+3,bottomStrings[4],format4);
			sheet.addCell(bottom1);
			sheet.addCell(bottom2);
			sheet.addCell(bottom3_1);
			sheet.addCell(bottom3_2);
			sheet.addCell(bottom3_3);
		}catch(Exception e) 
		{ 
//		System.out.println("in the method setSheetBottom ,the error info is:"+e.getMessage()); 
		e.printStackTrace();
		} 
	}
	
	/*
	 * 一、生成文件。 二、生成文件中的sheet. 三、设置列宽度和header. 四、设置表头信息。 五、数据填表。
	 */
	public static void main(String args[]) { 
	try { 
// 打开文件
	ReadPropertiesImpl readPropertiesImpl = new ReadPropertiesImpl();
	String path= readPropertiesImpl.getValue("EXAM.REPORT.PATH");
	String fileName = "报考组考表["+DateTimeUtil.GetUniqueID()+"].xls";
	CreateXlslHelper createExl=new CreateXlslHelper();	
	WritableWorkbook book = createExl.createBook(path,fileName);
// test.format2.setAlignment(jxl.format.Alignment.CENTRE);
	// 设置宽度信息及头、表头、尾信息
	int[] columnWidths = {8,15,22,22,7,8,18};
	
	// 设置头信息
	String header = "考试报考及组考情况统计表";
	String tcTitle = "站点名称：北京外国语学习中心";
	String examPlace = "考场编号：";
	String examDate = "考试时间：2006/1/6 9:00-11:00";
	String examTitle = "考试名称：综合英语一听力+听说交互一听力";
	String examCode = "考试代码：XXXXXXXXX+BBBBBBBBBBBBBB";
	
	// 设置表头信息
	String colum1 = "序号";
	String colum2 = "考生姓名";
	String colum3 = "学籍号";
	String colum4 = "身份证号";
	String colum5 = "性别";
	String colum6 = "座位号";
	String colum7 = "考生签到栏";	
	String[] tableTitles = {colum1,colum2,colum3,colum4,colum5,colum6,colum7};
	
	// 设置尾信息
	String bottom1 = "总计：";
	String bottom2 = "备注:";
	String bottom3 = "远程教育学习中心负责人(签字)：";
	String bottom4 = "制表人:";
	String bottom5 = "制表日期";	
	
	ArrayList allDates = new ArrayList();
	for(int i=0;i<220;i++){
		allDates.add(new Integer(i));
	}
	
// ArrayList currPageDatas = null;
	int dataSize = allDates.size();
	int perPageSize = 25;
	int allPageNumber = 0;
	int endNum = 0;
	int startNum = 0;
	int ctr = 0;	
	Integer data = null;
	String[] datas = null;
	Label dataCell = null;	
	if(dataSize%perPageSize==0)
		allPageNumber = dataSize/perPageSize;
	else
		allPageNumber = dataSize/perPageSize+1;
// System.out.println("the all pageNumber is:"+allPageNumber);
	
	// 一、生成文件。
	WritableSheet sheet = null;
	for(int i=0;i<allPageNumber;i++){		
		// 二、生成名为“第一页”的工作表，参数0表示这是第一页
		sheet = book.createSheet("第"+(i+1)+"页",0); 
		// sheet.setName("组考表第一页");
		createExl.setSheetColumnWidth(sheet,columnWidths);
		sheet.setRowView(0,25); 
		sheet.setRowView(1,20);
		sheet.setRowView(2,20);
		sheet.setRowView(3,20);
		sheet.setRowView(4,20);
		for(int k=5;k<35;k++){
			sheet.setRowView(k,28);
		}
		// 三、设定每列的宽度数组和header动态信息。
		createExl.setSheetColumnWidth(sheet,columnWidths);
		String[] headerStrings = {header,tcTitle+(i),examPlace,examDate+(i),examTitle+(i),examCode+(i)};
		// 定义header动态信息。
		createExl.setSheetHeader(sheet,columnWidths,headerStrings);
		
		// 四、设置表头信息。
		createExl.setSheetDataTableTitle(sheet,5,tableTitles);
		
		// 五、数据填表。
		startNum = i*perPageSize;
		endNum = (i+1)*perPageSize-1;
		if(endNum>dataSize)
			endNum = dataSize-1;
		ctr = 1;
		for(int j=startNum;j<=endNum;j++){			
			data = (Integer)allDates.get(j);
			String number = ""+ctr;
			String userName = data.toString();
			String studyNumber = data.toString();
			datas = new String[3];
			datas[0] = number;
			datas[1] = userName;
			datas[2] = studyNumber;
			createExl.setSheetData(sheet,ctr+5,datas,createExl.format2);
			ctr ++;
		}
		// 六、定义bottom.
		String[] bottomStrings = {bottom1+(ctr-1)+"人",bottom2,bottom3,bottom4,bottom5+DateTimeUtil.DateToString(new Date())+"("+dataSize+")"};
		createExl.setSheetBottom(sheet,perPageSize+6,bottomStrings);
	}	
// 写入数据并关闭文件
	book.write(); 
	book.close(); 

	}catch(Exception e) 
	{ 
//	System.out.println(e); 
	} 
	} 
	} 