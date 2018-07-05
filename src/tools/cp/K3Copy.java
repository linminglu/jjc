package tools.cp;

import java.io.File;
import java.util.List;

import com.framework.util.ManageFile;

public class K3Copy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//项目路径
		String projectPath = "D:/eclipse2/workspace/gfxy";
		
		if(!ManageFile.isFileExist(projectPath)) return;
		
		//省份前一字母和后面 - 分开, gameType
		//模板jsk3 共14
		String[] items = {
			"a-h,602,安徽",
			"b-j,603,北京",
			"f-j,604,福建",
			"g-z,605,贵州",
			"g-x,606,广西",
			"g-s,607,甘肃",
			"h-ub,608,湖北",
			"h-eb,609,河北",
			"h-n,610,河南",
			"j-x,611,江西",
			"j-l,612,吉林",
			"n-mg,613,内蒙古",
			"s-h,614,上海"
		};
		for(int i=0;i<items.length;i++){
			String[] arr = items[i].split(",");
			String ga = arr[0].split("-")[0];
			String gb = arr[0].split("-")[1];
			String gameType = arr[1];
			String chineseName = arr[2];
			
			//拷贝
			copyFile(projectPath,ga,gb,gameType,chineseName);
		}
		
	}
	
	public static void copyFile(String projectPath,String ga,String gb,String gameType,String chineseName){

		String keyPath = "jsk3";//固定模板
		String keyName = "JsK3";
		String keyName2 = "jsK3";
		String keyName3 = "江苏快3基本走势";
		String keyName4 = "江苏快3走势图";
		String keyName5 = "江苏快3";
		String keyNameConts = "JSK3";
		String keyVarConts = "JS_K3";
		String keyTblConts = "js_k3";
		//需要拷贝的模板---------------------在这里配置--------------------
//		String gameType = "53";
//		String ga = "s";//省份分成两部分都小写
//		String gb = "h";
		boolean isFrist = false;//首次运行不改
		String keyPathReplace = ga+gb+"k3";//路径小写
		String keyNameReplace = ga.toUpperCase()+gb+"K3";//名称大小写混合
		String keyName2Replace = ga+gb+"K3";//名称大小写混合
		String keyName3Replace = chineseName+"快3基本走势";//中文名称
		String keyName4Replace = chineseName+"快3走势图";//中文名称
		String keyName5Replace = chineseName+"快3";//中文名称
		String keyNameContsReplace = (ga+gb).toUpperCase()+"K3";//常量大写
		String keyVarContsReplace = (ga+gb).toUpperCase()+"_K3";//大写
		String keyTblContsReplace = ga+gb+"_k3";//小写
		//---------------------------------------------------------------
		
		String tplFolder;//模板目录
		String tplFolder2;
		String from;//源路径
		String from2;//
		String to;//目标路径
		String toPathFolder;//用于创建目标物理路径
		//替换内容
		String fData;//文件数据
		String fData2;
		List<File> files = null;//文件
		
		//1.拷贝com模块*.java,*.xml--------------------------
//		tplFolder = "/src/com/gf/k3/jsk3";//com目录路径
//		files = ManageFile.getAllFiles(projectPath+tplFolder);
//		for(File f:files){
//			from = f.getAbsolutePath();//源路径
//			to = from.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace);//目标路径
//			toPathFolder = getFilePath(to);//用于创建目标物理路径
//			//替换内容
//			fData = ManageFile.loadTextFileUTF8(from);
//			fData = fData.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace)
//			.replaceAll(keyNameConts, keyNameContsReplace)
//			.replaceAll(keyVarConts, keyVarContsReplace)
//			.replaceAll(keyTblConts, keyTblContsReplace);
//			//创建目录与拷贝
//			ManageFile.createFolder(toPathFolder);
//			ManageFile.writeTextToFile(fData, to, false);
//			System.out.println("Copy com>"+to);
//		}
		
		//2.拷贝help模块---------------------------
//		tplFolder = "/src/help/gf/k3/JsK3Manager.java";//一个文件
//		from = projectPath+tplFolder;//源路径
//		to = from.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace);//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		//ManageFile.createFolder(toPathFolder);
//		ManageFile.writeTextToFile(fData, to, false);
//		System.out.println("Copy help>"+to);
		
//		//3.拷贝config .xml----------------------------------
//		tplFolder = "/src/config/applicationContext-gaGfJsK3.xml";//一个文件
//		from = projectPath+tplFolder;//源路径
//		to = from.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace);//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		//ManageFile.createFolder(toPathFolder);
//		ManageFile.writeTextToFile(fData, to, false);
//		System.out.println("Copy config>"+to);
		
		//4.拷贝struts .xml----------------------------------
//		tplFolder = "/newplat/WEB-INF/struts-config-gaJsK3.xml";//一个文件
//		from = projectPath+tplFolder;//源路径
//		to = from.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace);//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		//ManageFile.createFolder(toPathFolder);
//		ManageFile.writeTextToFile(fData, to, false);
//		System.out.println("Copy struts>"+to);
		
		//5.拷贝 /newplat/jsp------------------------------------------
//		tplFolder = "/newplat/jsk3";//目录路径
//        files = ManageFile.getAllFiles(projectPath+tplFolder);
//		for(File f:files){
//			from = f.getAbsolutePath();//源路径
//			to = from.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace);//目标路径
//			toPathFolder = getFilePath(to);//用于创建目标物理路径
//			//替换内容
//			fData = ManageFile.loadTextFileUTF8(from);
//			fData = fData.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace)
//			.replaceAll(keyNameConts, keyNameContsReplace)
//			.replaceAll(keyVarConts, keyVarContsReplace)
//			.replaceAll(keyTblConts, keyTblContsReplace);
//			//创建目录与拷贝
//			ManageFile.createFolder(toPathFolder);
//			ManageFile.writeTextToFile(fData, to, false);
//			System.out.println("Copy newplat/jsp>"+to);
//		}
		
		//6.拷贝 /newplat/trade------------------------------------------
//		tplFolder = "/newplat/trade/jsk3";//目录路径
//        files = ManageFile.getAllFiles(projectPath+tplFolder);
//		for(File f:files){
//			from = f.getAbsolutePath();//源路径
//			to = from.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace);//目标路径
//			toPathFolder = getFilePath(to);//用于创建目标物理路径
//			//替换内容
//			fData = ManageFile.loadTextFileUTF8(from);
//			fData = fData.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace)
//			.replaceAll(keyName5, keyName5Replace)
//			.replaceAll(keyNameConts, keyNameContsReplace)
//			.replaceAll(keyVarConts, keyVarContsReplace)
//			.replaceAll(keyTblConts, keyTblContsReplace)
//			.replaceAll("gameType=601", "gameType="+gameType+"")
//			.replaceAll("gameType:\"601\",", "gameType:\""+gameType+"\",");
//			//创建目录与拷贝
//			ManageFile.createFolder(toPathFolder);
//			ManageFile.writeTextToFile(fData, to, false);
//			System.out.println("Copy newplat/trade>"+to);
//		}
		
		//7.生成web.xml--------------------
//		tplFolder = "/src/tools/cp/tmp/jsk3.web.xml";//一个文件
//		tplFolder2 = "/src/tools/cp/tmp/web.xml";
//		from = projectPath+tplFolder;//源路径
//		from2 = projectPath+tplFolder2;
//		to = "/newplat/WEB-INF/web.xml";//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		if(isFrist){
////			fData2 = ManageFile.loadTextFileUTF8(from2);
////			fData2 = fData2.replace("<!--###-->", fData);
////			ManageFile.writeTextToFile(fData2, projectPath+to, false);
//		}
//		System.out.println("Copy web.xml>\n"+fData);
		
		//8.生成api.xml--------------------
//		tplFolder = "/src/tools/cp/tmp/jsk3.api.xml";//一个文件
//		tplFolder2 = "/src/tools/cp/tmp/struts-config-api.xml";
//		from = projectPath+tplFolder;//源路径
//		from2 = projectPath+tplFolder2;
//		to = "/newplat/WEB-INF/struts-config-api.xml";//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		if(isFrist){
////			fData2 = ManageFile.loadTextFileUTF8(from2);
////			fData2 = fData2.replace("<!--###-->", fData);
////			ManageFile.writeTextToFile(fData2, projectPath+to, false);
//		}
//		System.out.println("Copy api.xml>\n"+fData);
		
//		//9.拷贝config.xml
//		tplFolder  = "/src/tools/cp/tmp/jsk3.config.xml";//一个文件
//		tplFolder2 = "/src/tools/cp/tmp/applicationContext-base.xml";
//		from = projectPath+tplFolder;//源路径
//		from2 = projectPath+tplFolder2;
//		to = "/src/config/applicationContext-base.xml";//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		if(isFrist){
////			fData2 = ManageFile.loadTextFileUTF8(from2);
////			fData2 = fData2.replace("<!--###-->", fData);
////			ManageFile.writeTextToFile(fData2, projectPath+to, false);
//		}
//		System.out.println("Copy config.xml>\n"+fData);
		
		//10.生成数据脚本
//		tplFolder = "/src/tools/cp/tmp/jsk3.table.sql";//一个文件
//		from = projectPath+tplFolder;//源路径
//		to = from.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace);//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		//ManageFile.createFolder(toPathFolder);
//		//ManageFile.writeTextToFile(fData, to, false);
//		System.out.println("Copy table.sql>\n"+fData);
		
		
		//11.拷贝 /newplat/zoushitu------------------------------------------
//		tplFolder = "/newplat/zoushitu/jsk3";//目录路径
//        files = ManageFile.getAllFiles(projectPath+tplFolder);
//		for(File f:files){
//			from = f.getAbsolutePath();//源路径
//			to = from.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace)
//			.replaceAll(keyName3, keyName3Replace)
//			.replaceAll(keyName4, keyName4Replace);//目标路径
//			toPathFolder = getFilePath(to);//用于创建目标物理路径
//			//替换内容
//			fData = ManageFile.loadTextFileUTF8(from);
//			fData = fData.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName3, keyName3Replace);
//			//创建目录与拷贝
//			ManageFile.createFolder(toPathFolder);
//			ManageFile.writeTextToFile(fData, to, false);
//			System.out.println("Copy newplat/zoushitu>"+to);
//		}
		
		
		//12.生成zoushitu.js--------------------
//		tplFolder = "/src/tools/cp/tmp/jsk3zoushitu.js";//一个文件
//		tplFolder2 = "/src/tools/cp/tmp/zoushitu.js";
//		from = projectPath+tplFolder;//源路径
//		from2 = projectPath+tplFolder2;
//		to = "/newplat/js/zoushitu.js";//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyName3, keyName3Replace)
//		.replaceAll(keyName4, keyName4Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		if(true){
////			fData2 = ManageFile.loadTextFileUTF8(from2);
////			fData2 = fData2.replace("<!--###-->", fData);
////			ManageFile.writeTextToFile(fData2, projectPath+to, false);
//		}
//		System.out.println("\n"+fData);
		
		
		//13.生成zoushitu/index.html--------------------
//		tplFolder = "/src/tools/cp/tmp/jsk3zoushitu.html";//一个文件
//		tplFolder2 = "/src/tools/cp/tmp/index.html";
//		from = projectPath+tplFolder;//源路径
//		from2 = projectPath+tplFolder2;
//		to = "/newplat/zoushitu/index.html";//目标路径
//		toPathFolder = getFilePath(to);//用于创建目标物理路径
//		//替换内容
//		fData = ManageFile.loadTextFileUTF8(from);
//		fData = fData.replaceAll(keyPath, keyPathReplace)
//		.replaceAll(keyName, keyNameReplace)
//		.replaceAll(keyName2, keyName2Replace)
//		.replaceAll(keyName3, keyName3Replace)
//		.replaceAll(keyNameConts, keyNameContsReplace)
//		.replaceAll(keyVarConts, keyVarContsReplace)
//		.replaceAll(keyTblConts, keyTblContsReplace);
//		//创建目录与拷贝
//		if(true){
////					fData2 = ManageFile.loadTextFileUTF8(from2);
////					fData2 = fData2.replace("<!--###-->", fData);
////					ManageFile.writeTextToFile(fData2, projectPath+to, false);
//		}
//		System.out.println("\n"+fData);
		
		
		//14.拷贝 /newplat/game/gf------------------------------------------
//		tplFolder = "/newplat/game/gf/jsk3";//目录路径
//        files = ManageFile.getAllFiles(projectPath+tplFolder);
//		for(File f:files){
//			from = f.getAbsolutePath();//源路径
//			to = from.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace);//目标路径
//			toPathFolder = getFilePath(to);//用于创建目标物理路径
//			//替换内容
//			fData = ManageFile.loadTextFileUTF8(from);
//			fData = fData.replaceAll(keyPath, keyPathReplace)
//			.replaceAll(keyName, keyNameReplace)
//			.replaceAll(keyName2, keyName2Replace)
//			.replaceAll(keyName5, keyName5Replace)
//			.replaceAll(keyNameConts, keyNameContsReplace)
//			.replaceAll(keyVarConts, keyVarContsReplace)
//			.replaceAll(keyTblConts, keyTblContsReplace);
//			//创建目录与拷贝
//			ManageFile.createFolder(toPathFolder);
//			ManageFile.writeTextToFile(fData, to, false);
//			System.out.println("Copy newplat/game>"+to);
//		}
	}
	
	/**
	 * 获取文件物理路径
	 * @param fullPath
	 * @return
	 */
	public static String getFilePath(String fullPath){
		String[] arr = fullPath.split("\\\\");
		if(arr==null || arr.length==0) arr = fullPath.split("\\/");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<arr.length-1;i++){
			sb.append(arr[i]).append("/");
		}
		return sb.toString();
	}
	
}
