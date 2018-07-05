package tools.rebot.util;

import com.framework.util.DateTimeUtil;
import com.framework.util.ManageFile;

public class PUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void p(String info){
		System.out.println(info);
	}
	
	public static void pw(String info){
		System.out.println(info);
		//写入日志
		ManageFile.writeTextToFile(info+"\n", "d:/gfxy."+DateTimeUtil.getDateTime("yyMMdd")+".test.log", true);
	}
}
