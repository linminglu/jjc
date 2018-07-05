package tools.press;

import com.framework.util.ManageFile;

public class PressJSCSS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String rootPath = "D:/_Projects/_bxs_app/bxs.caipiao/gfxykele/application/newplat";
		
		StringBuffer sb = new StringBuffer();
		//game/index.html
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/jquery-1.10.2.min.js"));
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/mui/mui.min.js"));
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/jquery.lazyload.js"));
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/smarticker.js"));
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/rollups/tripledes.js"));
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/components/mode-ecb.js"));
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/move.min.js"));
//		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/index.min.js"));
//		ManageFile.writeUTF8File(rootPath+"/game/js/h2.min.js", sb.toString());
		
		sb = new StringBuffer();
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/css/mui/mui.min.css"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/css/login.min.css"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/css/base.min.css"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/css/index.min.css"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/css/me.min.css"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/css/jquery.smarticker.min.css"));
		ManageFile.writeUTF8File(rootPath+"/game/css/h.min.css", sb.toString());
		p(rootPath+"/game/css/h.min.css_____rebuild ok");
		
		//通用库
		sb = new StringBuffer();
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/jquery-1.10.2.min.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/mui/mui.min.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/jquery.lazyload.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/smarticker.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/md5.min.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/rollups/tripledes.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/components/mode-ecb.js"));
		ManageFile.writeUTF8File(rootPath+"/game/js/b.min.js", sb.toString());
		p(rootPath+"/game/js/b.min.js_____rebuild ok");
		
		//页面
		sb = new StringBuffer();
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/move.min.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/index.min.js"));
		sb.append(ManageFile.loadTextFileUTF8(rootPath+"/game/js/me.min.js"));
		ManageFile.writeUTF8File(rootPath+"/game/js/h.min.js", sb.toString());
		p(rootPath+"/game/js/h.min.js_____rebuild ok");
	}

	public static void p(Object obj){
		System.out.println(obj.toString());
	}
}
