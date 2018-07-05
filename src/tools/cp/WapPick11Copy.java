package tools.cp;

import java.io.File;
import java.util.List;

import com.framework.util.ManageFile;

public class WapPick11Copy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//项目路径
		String projectPath = "E:/product/kelecaipiao/newplat/game";
		
		if(!ManageFile.isFileExist(projectPath)) return;
		
		//省份前一字母和后面 - 分开, gameType
		//模板/game/gdpick11 共24
		String[] items = {
			"b-j,20,北京11选5",
			"t-j,21,天津11选5",
			"h-eb,22,河北11选5",
			"n-mg,23,内蒙古11选5",
			"l-n,24,辽宁11选5",
			"j-l,25,吉林11选5",
			"h-lj,26,黑龙江11选5",
			"s-h,27,上海11选5",
			"j-s,28,江苏11选5",
			"z-j,29,浙江11选5",
			"a-h,30,安徽11选5",
			"f-j,31,福建11选5",
			"j-x,16,江西11选5",
			"h-n,32,河南11选5",
			"h-ub,33,湖北11选5",
			"g-x,34,广西11选5",
			"g-z,36,贵州11选5",
			"s-hx,37,陕西11选5",
			"g-s,38,甘肃11选5",
			"x-j,39,新疆11选5",
			"s-d,18,山东11选5",
			"s-x,19,山西11选5",
			"y-n,40,云南11选5"
		};
		for(int i=0;i<items.length;i++){
			String[] arr = items[i].split(",");
			String ga = arr[0].split("-")[0];
			String gb = arr[0].split("-")[1];
			String gameType = arr[1];
			String gname = arr[2];
			
			//拷贝
			copyFile(projectPath,ga,gb,gname,gameType);
		}
	}
	
	public static void copyFile(String projectPath,String ga,String gb,String gname,String gameType){

		String keyPath = "gdpick11";//固定模板字符
		String keyName = "GdPick11";
		String keyName2 = "gdPick11";
		String keyNameConts = "GDPICK11";
		String keyVarConts = "GD_PICK11";
		String keyTblConts = "gd_pick";
		String keyGameName = "广东11选5";
		//需要拷贝的模板---------------------在这里配置--------------------
//		String gameType = "40";
//		String ga = "y";//省份分成两部分都小写
//		String gb = "n";
//		boolean isFrist = false;//首次运行不改
		String keyPathReplace = ga+gb+"pick11";//路径小写
		String keyNameReplace = ga.toUpperCase()+gb+"Pick11";//名称大小写混合
		String keyName2Replace = ga+gb+"Pick11";//名称大小写混合
		String keyNameContsReplace = (ga+gb).toUpperCase()+"PICK11";//常量大写
		String keyVarContsReplace = (ga+gb).toUpperCase()+"_PICK11";//大写
		String keyTblContsReplace = ga+gb+"_pick";//小写
		String keyGameNameReplace = gname;
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
		
		//1.拷贝wap页面--------------------------
		tplFolder = "/gdpick11";//文件目录
		files = ManageFile.getAllFiles(projectPath+tplFolder);
		System.out.println(files.size());
		for(File f:files){
			from = f.getAbsolutePath();//源路径
			to = from.replaceAll(keyPath, keyPathReplace)
			.replaceAll(keyName, keyNameReplace)
			.replaceAll(keyName2, keyName2Replace);//目标路径
			toPathFolder = getFilePath(to);//用于创建目标物理路径
			//替换内容
			fData = ManageFile.loadTextFileUTF8(from);
			fData = fData.replaceAll(keyPath, keyPathReplace)
			.replaceAll(keyName, keyNameReplace)
			.replaceAll(keyName2, keyName2Replace)
			.replaceAll(keyNameConts, keyNameContsReplace)
			.replaceAll(keyVarConts, keyVarContsReplace)
			.replaceAll(keyTblConts, keyTblContsReplace)
			.replaceAll(keyGameName, keyGameNameReplace);
			//创建目录与拷贝
			ManageFile.createFolder(toPathFolder);
			ManageFile.writeTextToFile(fData, to, false);
			System.out.println("Copy game>"+to);
		}
		
		
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
