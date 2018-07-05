package com.apps.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class PinyinUtil {

	public static List<String[]> getPinyin(String hanzi) throws Exception{
		//汉语拼音格式输出类  
		List <String[]>list = new ArrayList<String[]>();
		HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat(); 
		hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V); 
		char []c = hanzi.toCharArray();
		for(int i=0,j=c.length;i<j;i++){
			boolean flag = isHanzi(c[i]);
			if(!flag) {
				String [] arr = {c[i]+""};
				list.add(arr);
				continue;
			}
			String []s = PinyinHelper.toHanyuPinyinStringArray(c[i], hanYuPinOutputFormat);
			list.add(s);
		}
		return list;
	}
	
	public static String getStringPinyin(String hanzi,String split) throws Exception{
		HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat(); 
		hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		@SuppressWarnings("deprecation")
		String pinyin = PinyinHelper.toHanyuPinyinString(hanzi,hanYuPinOutputFormat,split);
		return pinyin;
	}
	
	public static String getFirstCharToString2(String s) {
		try{
			if(null==s||s.equals("")) return "";
			HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat(); 
			hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
			
			char[]cs = s.toCharArray();
			char c = cs[0];
			boolean b = isHanzi(c);
			if(!b){
				return c+"";
			}
			String []pinyin = PinyinHelper.toHanyuPinyinStringArray(c, hanYuPinOutputFormat);
			if(pinyin.length>0) return pinyin[0].charAt(0)+"";
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return "";
	}
	/**
	 * 返回第一个字符的首字母
	 * @param s
	 * @return 臧 返回 Z ，zang 返回 z 其他返回 # 
	 * @author Mr.zang
	 */
	public static String getFirstCharToString(String s) {
		try {
			if (null == s || s.equals(""))
				return "";
			HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
			hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

			char[] cs = s.toCharArray();
			char c = cs[0];
			boolean isABC = isABC(c);
			boolean isHanzi = isHanzi(c);
			if (isABC) {
				return c + "";
			}else if (isHanzi) {
				String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c,
						hanYuPinOutputFormat);
				if (pinyin.length > 0)
					return pinyin[0].charAt(0) + "";
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "#";
	}
	/**
	 * 是否是字母类型
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isABC(char c) {
		return String.valueOf(c).matches("^[A-Za-z]+$");
	}
//	private static String joinChar(String []s){
//		StringBuffer sb  =new StringBuffer();
//		for(int m=0,n=s.length;m<n;m++){
//			sb.append(s[m]);
//		}
//		return sb.toString();
//	}
	
	private static boolean isHanzi(char c){
		return String.valueOf(c).matches("[\\u4E00-\\u9FA5]+");
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		long l1 = new Date().getTime();
		List<String[]> list = getPinyin("你是ss11中国人吗腻万文伟");
		long l2 = new Date().getTime();
		System.out.println((l2-l1));
		for(int i=0,j=list.size();i<j;i++){
			String []s =  list.get(i);
			
			System.out.println(s[0]);
		}
//		String sss = PinyinHelper.toHanyuPinyinString("中国人",null,null);
//		System.out.println(sss);
		String stringPinyin = getStringPinyin("你是ss11中国人吗腻万文伟","-");
		System.out.println(stringPinyin);
		System.out.println(getFirstCharToString("dada"));
		System.out.println(getFirstCharToString("你好"));
		System.out.println(getFirstCharToString("12"));
	}

}
