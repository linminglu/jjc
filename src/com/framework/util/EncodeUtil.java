/**
 * @(#)EncodeUtil.java 1.0 06/10/07
 */

package com.framework.util;

import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncodeUtil{
	private static String hex_chr = ".ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+/";
	private static int a = 2;//a{0,3}
	private static int b = 5;//b{1,5}
	private static int len = 65;
	private static long time = 20*60*1000;//20分钟
	private static String cookieName = "t";
	
	public static String decode(String arg){
		int[] bin = str2binl(s2n(arg));
		String str = "";
		int mask = (1 << 16) - 1;
		int a;
		for(int i = 0; i < bin.length * 32; i += 16){
			a = ((bin[i>>5] >>> (i % 32)) & mask);
			if(a==0)break;
			str +=(char) a;
		}
		return str;
	}
	
	public static String encode(String str){
		int[] bin = new int[str.length()];
		int mask = (1 << 16) - 1;
		for(int i = 0; i < str.length() * 16; i += 16)
			bin[i>>5] |= (str.charAt(i / 16) & mask) << (i%32);
		return n2s(bin2str(bin));
	}
	
	private static String n2s(String str){
		str = str.replaceAll("0", hex_chr.charAt(a+0)+"");
		str = str.replaceAll("1", hex_chr.charAt(a+1*b)+"");
		str = str.replaceAll("2", hex_chr.charAt(a+2*b)+"");
		str = str.replaceAll("3", hex_chr.charAt(a+3*b)+"");
		str = str.replaceAll("4", hex_chr.charAt(a+4*b)+"");
		str = str.replaceAll("5", hex_chr.charAt(a+5*b)+"");
		str = str.replaceAll("6", hex_chr.charAt(a+6*b)+"");
		str = str.replaceAll("7", hex_chr.charAt(a+7*b)+"");
		str = str.replaceAll("8", hex_chr.charAt(a+8*b)+"");
		str = str.replaceAll("9", hex_chr.charAt(a+9*b)+"");
		str = str.replaceAll(",", hex_chr.charAt(a+10*b)+"");
		return str;
	}
	
	private static String s2n(String str){
		int index = -1;
		String nstr = "";
		String ostr = "";
		for(int i=0;i<str.length();i++){
			index = hex_chr.indexOf(str.charAt(i));
			if(index==-1)continue;
			nstr = (index-a)/b==10?",":(index-a)/b+"";
			ostr = str.charAt(i)+"";
			if(ostr.equals("+")||ostr.equals("."))ostr = "\\"+ostr;
			str = str.replaceAll(ostr, nstr);
		}
		return str;
	}
	
	private static String bin2str(int[] bin){
		String str = "";
		for(int i=0;i<bin.length;i++){
			if(bin[i]==0)break;
			str += ","+bin[i];
		}
		return str.length()>1?str.substring(1):str;
	}
	
	private static int[] str2binl(String str){
		if(str==null)return null;
		str = str.replaceAll("[^0-9,]","");
		if(str.trim().length()==0)return null;
		String[] spl = str.split(",");
		int[] bin = new int[spl.length];
		for(int i=0;i<spl.length;i++){
			bin[i]=Integer.valueOf(spl[i]).intValue();
		}
		return bin;
	}
	
	public static int sum(String str){
		int count = 0;
		if(str == null)return 0;
		str = str.replaceAll("[^1-9]", "");
		if(str == null || str.trim().equals(""))return 0;
		for(int i=0;i<str.length();i++){
			count += Integer.parseInt(str.charAt(i)+"");
		}
		return count;
	}
	
	public static String ip2num(String ip){
		return ip.replaceAll("[^0-9]", "");
	}
	
	public static String ipsum(String ip){
		return ip2num(ip)+sum(ip);
	}
	
	private static String random(int len){
		String str = "";
		Random rd = new Random();
		for(int i=0; i<len; i++){
			str += hex_chr.charAt(rd.nextInt(51)+1);
		}
		return str;
	}
	
	/**
	 * 用户名编码
	 * @param username
	 * @param req
	 * @return
	 */
	public static String strEncode(String username, HttpServletRequest req, HttpServletResponse res){
		if(username==null)return "";
		username=username.trim();
		if(username.equals(""))return "";
		String str = username+":"+ipsum(req.getRemoteAddr())+":"+System.currentTimeMillis();
//		Cookie c = new Cookie(cookieName, str);//str改成所需要的
//		c.setPath("/");
//		c.setDomain(".ramonline.com");
//		c.setMaxAge(0);
//		res.addCookie(c);
		return random(len)+encode(str);
	}
	/**
	 * 获得用户名
	 * @param str
	 * @param req
	 * @return
	 */
	public static String strDecode(String str, HttpServletRequest req){
		if(str==null)return "";
		str = str.trim();
		if(str.equals(""))return "";
		if(str.length()<=len)return "";
		String referer = req.getHeader("referer");
		int index = referer.indexOf(".ramonline.com");
		if(index==-1||index>13)return "";
//		String _c = "";
//		Cookie[] c = req.getCookies();
//		for(int i=0;i<c.length;i++){
//		  if(c[i].getName().equals(cookieName)){_c=c[i].getValue();break;}
//		}
		str = str.substring(len);
		String[] uis = decode(str).split(":");
		if(uis.length!=3)return "";
		if(uis[0].equals(""))return "";
		if(!uis[1].equals(ipsum(req.getRemoteAddr())))return "";
		String tmp = uis[2].replaceAll("[^0-9]", "");
		tmp = tmp.length()==0?"0":tmp;
		if(Long.valueOf(tmp).longValue()<=System.currentTimeMillis()-time)return "";
		return uis[0];
	}
	
	public static void main(String[] args){
//		int str = 1234567;

//		while(str > 9){
//			str = sum(str+"");
//		}
//		System.out.println(str);
		String str1 = "2ae在rh35erh54h,nuksaer.43534545/6gfdg";
//		System.out.println(str1);
		String ii = encode(str1);
//		System.out.println("len:"+ii.length()+" " +ii);
//		System.out.println(decode(ii));
	
		String rd = random(len);
		String str = rd+encode(str1);
	
		
	}
}