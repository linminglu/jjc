package tools.cp;

import java.io.File;
import java.util.List;

import com.framework.util.ManageFile;

public class WapK3Copy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//项目路径E:/product/kelecaipiao
		String projectPath = "E:/product/kelecaipiao/newplat/game";
		
		if(!ManageFile.isFileExist(projectPath)) return;
		//省份前一字母和后面 - 分开, gameType
		//模板jsk3 共14
		String[] items = {
			"a-h,41,安徽快3",
			"b-j,42,北京快3",
			"f-j,43,福建快3",
			"g-z,44,贵州快3",
			"g-x,45,广西快3",
			"g-s,46,甘肃快3",
			"h-ub,47,湖北快3",
			"h-eb,48,河北快3",
			"h-n,49,河南快3",
			"j-x,50,江西快3",
			"j-l,51,吉林快3",
			"n-mg,52,内蒙古快3",
			"s-h,53,上海快3"
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

		String keyPath = "jsk3";//固定模板字符
		String keyName = "JsK3";
		String keyName2 = "jsK3";
		String keyNameConts = "JSK3";
		String keyVarConts = "JS_K3";
		String keyTblConts = "js_k3";
		String keyGameName = "江苏快3";
		//需要拷贝的模板---------------------在这里配置--------------------
//		String gameType = "40";
//		String ga = "y";//省份分成两部分都小写
//		String gb = "n";
//		boolean isFrist = false;//首次运行不改
		String keyPathReplace = ga+gb+"k3";//路径小写
		String keyNameReplace = ga.toUpperCase()+gb+"K3";//名称大小写混合
		String keyName2Replace = ga+gb+"K3";//名称大小写混合
		String keyNameContsReplace = (ga+gb).toUpperCase()+"K3";//常量大写
		String keyVarContsReplace = (ga+gb).toUpperCase()+"_K3";//大写
		String keyTblContsReplace = ga+gb+"_k3";//小写
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
		tplFolder = "/jsk3";//文件目录
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
