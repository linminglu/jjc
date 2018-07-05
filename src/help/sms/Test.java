package help.sms;

import java.util.Date;
import java.util.UUID;

import com.apps.util.UploadUtil;
import com.framework.util.DateTimeUtil;
import com.ram.util.MD5;

public class Test {
	public static void main(String[] args) {
//		String getUniqueID = DateTimeUtil.GetUniqueID();
//		System.out.println(getUniqueID);
//		String exc = MD5.exc(getUniqueID);
//		System.out.println(exc);
//		for (int i = 0; i < 6; i++) {
//			
//			System.out.println(generate());
//		}
//		System.out.println(createUUID(15));
//		System.out.println(UUID.randomUUID());
//		System.out.println(Long.toString(new Date().getTime(), 36));
		
//		String myj="";
//		for(int i=0;i<6;i++){
//			String xfCode="xfCode"+i;
//			if(i>0){
//				myj=myj+",";
//			}
//			myj=myj+xfCode;
//			
//		}
//		System.out.println(myj);
		
		String fileExt = UploadUtil.getFileExt("djddd.png");
		System.out.println(fileExt);
	}
	
	private  static String str = "1234567890abcdefghijklmnopqrstuvwxyz";
    private  static int pixLen = str.length();
    private static volatile int pixOne = 0;
    private static volatile int pixTwo = 0;
    private static volatile int pixThree = 0;
    private static volatile int pixFour = 0;
	
    public static String createUUID(int len) {  
    	  
        String uuid = java.util.UUID.randomUUID().toString()  
                .replaceAll("-", "");  
        return uuid.substring(0, len);  
    }  
 public synchronized static String generate() {
        StringBuilder sb = new StringBuilder();// 创建一个StringBuilder
        sb.append(Long.toHexString(System.currentTimeMillis()));// 先添加当前时间的毫秒值的16进制
        pixFour++;
        if (pixFour == pixLen) {
            pixFour = 0;
            pixThree++;
            if (pixThree == pixLen) {
                pixThree = 0;
                pixTwo++;
                if (pixTwo == pixLen) {
                    pixTwo = 0;
                    pixOne++;
                    if (pixOne == pixLen) {
                        pixOne = 0;
                    }
                }
            }
        }
        return sb.append(str.charAt(pixOne)).append(str.charAt(pixTwo)).append(str.charAt(pixThree)).append(str.charAt(pixFour)).toString();
    }
 
	
}
