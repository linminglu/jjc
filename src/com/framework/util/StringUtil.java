package com.framework.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.apps.Constants;
import com.ram.RamConstants;

public class StringUtil {

	/**
	 * 获取一个UUID，
	 * 
	 * @param len
	 *            位数 最大32
	 * @return
	 */
	public static String getUUID(int len) {
		String uuid = java.util.UUID.randomUUID().toString()
				.replaceAll("-", "");
		return uuid.substring(0, len);
	}
	/**
	 * 比较版本号，支持[x.y.z][x.yz][x.yzz]等 如：1.0 1.0.5 1.21，0.51等
	 * 
	 * @param latestVer
	 *            最新版本
	 * @param ver
	 *            当前传递版本
	 * @return >0表示有新版本 =0表示当前是最新版本 <0表示版本超出最新版本的错误情况
	 */
	public static int compareVer(String latestVer, String ver) {
		if (latestVer == null && ver == null)
			return 0;
		else if (latestVer == null)
			return -1;
		else if (ver == null)
			return 1;
		return latestVer.compareTo(ver);
	}
	/**
	 * 获取指定长度的随机字符串
	 * 
	 * @param count
	 * @return
	 */
	public static String getRandomString(int count) {
		String rasc = "";
		int number = 0;
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			number = random.nextInt(61);
			rasc = rasc + getASC(number);
		}
		return rasc;
	}

	public static String getRandomNumber(int count) {
		String rasc = "";
		int number = 0;
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			number = random.nextInt(30);
			rasc = rasc + getNumber(number);
		}
		return rasc;
	}

	public static String getRandomNumberAndString(int count) {
		String rasc = "";
		int number = 0;
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			number = random.nextInt(80);
			rasc = rasc + getRandomNumberString(number);
		}
		return rasc;
	}
	
	public static void main(String[] args) {
		System.out.println(getRandomInt(10, 30));
	}

	public static int getRandomInt(int min, int max) {
		Random random = new Random();
		return new Float(random.nextFloat() * (max - min + 1) + min).intValue();
	}

	public static int getRandomInt(int max) {
		Random random = new Random();
		return new Float(random.nextFloat() * max).intValue();
	}

	private static String getASC(int number) {
		// String str =
		// "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String str = "9876543210abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String asc = "" + str.charAt(number);
		return asc;
	}

	public static String getNumber(int number) {
		// String str =
		// "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String str = "9876543210123456789013579086428";
		String asc = "" + str.charAt(number);
		return asc;
	}

	private static String getRandomNumberString(int number) {
		String str = "6X95K923456789523456789ABCDEFGHJK898765432TUA765432XMNPQRSTUVWXY698765432G98653F";
		String asc = "" + str.charAt(number);
		return asc;
	}

	public static String getOrderNumber() {
		Date now = DateTimeUtil.getJavaSQLDateNow();
		String randomstr = new Double(Math.random()).toString();
		randomstr = "";// randomstr.substring(2, 5);
		return now.getTime() + randomstr;
	}

	/**
	 * 截取字符串
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String cutStr(String src, int length) {
		if (src.length() <= length) {
			return src;
		} else {
			String tmp = src.substring(0, length);
			tmp = tmp + "...";
			return tmp;
		}

	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param s
	 *            需要得到长度的字符串
	 * @return i得到的字符串长度
	 */
	public static int getBytes(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param char c, 需要判断的字符
	 * @return boolean, 返回true,Ascill字符
	 */
	private static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 获得当前时间
	 * 
	 * @param parrten
	 *            输出的时间格式
	 * 
	 * 
	 * 
	 * @return 返回时间
	 */
	public static String getTime(String parrten) {
		String timestr;
		if (parrten == null || parrten.equals("")) {
			parrten = "yyyyMMddHHmmss";
		}
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(parrten);
		java.util.Date cday = new Date();
		timestr = sdf.format(cday);
		return timestr;
	}

	/**
	 * 比较两个字符串时间的大小
	 * 
	 * @param t1
	 *            时间1
	 * @param t2
	 *            时间2
	 * @param parrten
	 *            时间格式 :yyyy-MM-dd
	 * @return 返回long =0相等，>0 t1>t2，<0 t1<t2
	 */
	public static long compareStringTime(String t1, String t2, String parrten) {
		SimpleDateFormat formatter = new SimpleDateFormat(parrten);
		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos1 = new ParsePosition(0);
		Date dt1 = formatter.parse(t1, pos);
		Date dt2 = formatter.parse(t2, pos1);
		long l = dt1.getTime() - dt2.getTime();
		return l;
	}

	public static String convertToChinese(String strInput) {
		String str = "";
		if (strInput == null || strInput.length() <= 0) {
			return strInput;
		}
		try {
			str = new String(strInput.getBytes("iso-8859-1"), "gb2312");
		} catch (Exception e) {
			str = strInput;
		}
		return str;
	}

	public static String convertToUTF8(String strInput) {
		String str = "";
		if (strInput == null || strInput.length() <= 0) {
			return strInput;
		}
		try {
			str = new String(strInput.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			str = strInput;
		}
		return str;
	}

	public static List quoteStrList(List list) {
		List tmpList = list;
		list = new ArrayList();
		Iterator i = tmpList.iterator();
		while (i.hasNext()) {
			String str = (String) i.next();
			str = "'" + str + "'";
			list.add(str);
		}
		return list;
	}

	public static String join(List list, String delim) {
		if (list == null || list.size() < 1) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		Iterator i = list.iterator();
		while (i.hasNext()) {
			buf.append((String) i.next());
			if (i.hasNext()) {
				buf.append(delim);
			}
		}
		return buf.toString();
	}

	public static List split(String str, String delim) {
		List splitList = null;
		StringTokenizer st = null;

		if (str == null) {
			return splitList;
		}

		if (delim != null) {
			st = new StringTokenizer(str, delim);
		} else {
			st = new StringTokenizer(str);

		}
		if (st != null && st.hasMoreTokens()) {
			splitList = new ArrayList();

			while (st.hasMoreTokens()) {
				splitList.add(st.nextToken());
			}
		}
		return splitList;
	}

	public static String createBreaks(String input, int maxLength) {
		char chars[] = input.toCharArray();
		int len = chars.length;
		StringBuffer buf = new StringBuffer(len);
		int count = 0;
		int cur = 0;
		for (int i = 0; i < len; i++) {
			if (Character.isWhitespace(chars[i])) {
				count = 0;
			}
			if (count >= maxLength) {
				count = 0;
				buf.append(chars, cur, i - cur).append("\n");
				cur = i;
			}
			count++;
		}
		buf.append(chars, cur, len - cur);

		return buf.toString();
	}

	/**
	 * Escape SQL tags, ' to ''; \ to \\.
	 * 
	 * @param input
	 *            string to replace
	 * @return string
	 */
	public static String escapeSQLTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuffer buf = new StringBuffer();
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '\\') {
				buf.append("\\\\");
			} else if (ch == '\'') {
				buf.append("\'\'");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * Escape HTML tags.
	 * 
	 * @param input
	 *            string to replace
	 * @return string
	 */
	public static String escapeHTMLTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuffer buf = new StringBuffer();
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else if (ch == '&') {
				buf.append("&amp;");
			} else if (ch == '"') {
				buf.append("&quot;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * Convert new lines, \n or \r\n to <BR />
	 * .
	 * 
	 * @param input
	 *            string to convert
	 * @return string
	 */
	public static String convertNewlines(String input) {
		input = replace(input, "\r\n", "<P>");
		input = replace(input, "\n", "<BR>");
		return input;
	}

	public static String convertNewlinesP(String input) {
		input = replace(input, "\r\n", "<P>");
		return input;
	}

	public static String convertNewlinesBR(String input) {
		input = replace(input, "\r\n", "<BR>");
		input = replace(input, "\n", "<BR>");
		return input;
	}

	public static String convertBR(String input) {
		input = replace(input, "\n", "<BR>");
		return input;
	}

	public static String replace(String mainString, String oldString,
			String newString) {
		if (mainString == null) {
			return null;
		}
		int i = mainString.lastIndexOf(oldString);
		if (i < 0) {
			return mainString;
		}
		StringBuffer mainSb = new StringBuffer(mainString);
		while (i >= 0) {
			mainSb.replace(i, i + oldString.length(), newString);
			i = mainString.lastIndexOf(oldString, i - 1);
		}
		return mainSb.toString();
	}

	/**
	 * Check a string null or blank.
	 * 
	 * @param param
	 *            string to check
	 * @return boolean
	 */
	public static boolean nullOrBlank(String param) {
		return (param == null || param.trim().equals("")) ? true : false;
	}

	public static String notNull(String param) {
		return param == null ? "" : param.trim();
	}

	public static boolean isEmpty(String s) {
		if (s == null)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * Parse a string to int.
	 * 
	 * @param param
	 *            string to parse
	 * @return int value, on exception return 0.
	 */

	public static int parseInt(String param) {
		int i = 0;
		try {
			i = Integer.parseInt(param);
		} catch (Exception e) {
			i = (int) parseFloat(param);
		}
		return i;
	}

	public static long parseLong(String param) {
		long l = 0;
		try {
			l = Long.parseLong(param);
		} catch (Exception e) {
			l = (long) parseDouble(param);
		}
		return l;
	}

	public static float parseFloat(String param) {
		float f = 0;
		try {
			f = Float.parseFloat(param);
		} catch (Exception e) {
			//
		}
		return f;
	}

	public static double parseDouble(String param) {
		double d = 0;
		try {
			d = Double.parseDouble(param);
		} catch (Exception e) {
			//
		}
		return d;
	}

	/**
	 * Parse a string to boolean.
	 * 
	 * @param param
	 *            string to parse
	 * @return boolean value, if param begin with(1,y,Y,t,T) return true, on
	 *         exception return false.
	 */
	public static boolean parseBoolean(String param) {
		if (nullOrBlank(param)) {
			return false;
		}
		switch (param.charAt(0)) {
		case '1':
		case 'y':
		case 'Y':
		case 't':
		case 'T':
			return true;
		}
		return false;
	}

	/**
	 * Convert URL .
	 * 
	 * @param input
	 *            string to convert
	 * @return string
	 */
	public static String convertURL(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuffer buf = new StringBuffer(input.length() + 25);
		char chars[] = input.toCharArray();
		int len = input.length();
		int index = -1;
		int i = 0;
		int j = 0;
		int oldend = 0;
		while (++index < len) {
			char cur = chars[i = index];
			j = -1;
			if ((cur == 'f' && index < len - 6 && chars[++i] == 't'
					&& chars[++i] == 'p' || cur == 'h' && (i = index) < len - 7
					&& chars[++i] == 't' && chars[++i] == 't'
					&& chars[++i] == 'p'
					&& (chars[++i] == 's' || chars[--i] == 'p'))
					&& i < len - 4
					&& chars[++i] == ':'
					&& chars[++i] == '/'
					&& chars[++i] == '/') {
				j = ++i;
			}
			if (j > 0) {
				if (index == 0 || (cur = chars[index - 1]) != '\''
						&& cur != '"' && cur != '<' && cur != '=') {
					cur = chars[j];
					while (j < len) {
						if (cur == ' ' || cur == '\t' || cur == '\''
								|| cur == '"' || cur == '<' || cur == '['
								|| cur == '\n' || cur == '\r' && j < len - 1
								&& chars[j + 1] == '\n') {
							break;
						}
						if (++j < len) {
							cur = chars[j];
						}
					}
					cur = chars[j - 1];
					if (cur == '.' || cur == ',' || cur == ')' || cur == ']') {
						j--;
					}
					buf.append(chars, oldend, index - oldend);
					buf.append("<a href=\"");
					buf.append(chars, index, j - index);
					buf.append('"');
					buf.append(" target=\"_blank\"");
					buf.append('>');
					buf.append(chars, index, j - index);
					buf.append("</a>");
				} else {
					buf.append(chars, oldend, j - oldend);
				}
				oldend = index = j;
			} else if (cur == '[' && index < len - 6
					&& chars[i = index + 1] == 'u' && chars[++i] == 'r'
					&& chars[++i] == 'l'
					&& (chars[++i] == '=' || chars[i] == ' ')) {
				j = ++i;
				int u2;
				int u1 = u2 = input.indexOf("]", j);
				if (u1 > 0) {
					u2 = input.indexOf("[/url]", u1 + 1);
				}
				if (u2 < 0) {
					buf.append(chars, oldend, j - oldend);
					oldend = j;
				} else {
					buf.append(chars, oldend, index - oldend);
					buf.append("<a href =\"");
					String href = input.substring(j, u1).trim();
					if (href.indexOf("javascript:") == -1
							&& href.indexOf("file:") == -1) {
						buf.append(href);
					}
					buf.append("\" target=\"_blank");
					buf.append("\">");
					buf.append(input.substring(u1 + 1, u2).trim());
					buf.append("</a>");
					oldend = u2 + 6;
				}
				index = oldend;
			}
		}
		if (oldend < len) {
			buf.append(chars, oldend, len - oldend);
		}
		return buf.toString();
	}

	/**
	 * Display a string in html page, call methods: escapeHTMLTags, convertURL,
	 * convertNewlines.
	 * 
	 * @param input
	 *            string to display
	 * @return string
	 */
	public static String dspHtml(String input) {
		String str = input;
		str = createBreaks(str, 80);
		str = escapeHTMLTags(str);
		str = convertURL(str);
		str = convertNewlines(str);
		return str;
	}

	/**
	 * 超过80字符自动换行
	 * 
	 * @param input
	 * @return
	 */
	public static String dspHtmlBreak50(String input) {
		String str = input;
		str = createBreaks(str, 50);
		str = escapeHTMLTags(str);
		str = convertURL(str);
		str = convertNewlinesBR(str);
		return str;
	}

	/**
	 * 转换从数据库中取出的记录的中文并用iso8859_1输出
	 */
	public static String gb2iso(String s) {
		String str = "";
		if (s == null) {
			return "";
		}
		try {
			str = new String(s.getBytes("gb2312"), "8859_1");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doBracket(str);
	}

	/**
	 * 转换从网页传递的中文,然后插入到数据库中
	 */
	public static String iso2gb(String s) {
		String str = "";

		if (s == null) {
			return "";
		}

		try {
			str = new String(s.getBytes("8859_1"), "gb2312");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doQuote(str);

	}

	/**
	 * 将'转化为''，将<转化为&lt;，将>转化为&gt;
	 */
	public static String doQuote(String str) {
		StringBuffer sbCon = new StringBuffer("");
		int iLen = str.length();
		char[] cCon = str.toCharArray();
		String[] sCon = new String[iLen];
		for (int j = 0; j < iLen; j++) {
			if (cCon[j] == '\'') {
				sCon[j] = "''";
			} else {
				sCon[j] = (new Character(cCon[j])).toString();
			}
			sbCon.append(sCon[j]);
		}
		return (sbCon.toString());
	}

	/**
	 * 将<转化为&lt;，将>转化为&gt;
	 */
	public static String doBracket(String str) {
		StringBuffer sbCon = new StringBuffer("");
		int iLen = str.length();
		char[] cCon = str.toCharArray();
		String[] sCon = new String[iLen];
		for (int j = 0; j < iLen; j++) {
			if (cCon[j] == '<' || cCon[j] == '>') {
				if (cCon[j] == '<') {
					sCon[j] = "&lt;";
				} else {
					sCon[j] = "&gt;";
				}
			} else {
				sCon[j] = (new Character(cCon[j])).toString();
			}
			sbCon.append(sCon[j]);
		}
		return (sbCon.toString());
	}

	public static String genNumberString(int yourNumber, int yourStringLen) {
		int yourNumberCount = String.valueOf(yourNumber).length();
		int zeroLen = yourStringLen - yourNumberCount;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < zeroLen; i++) {
			sb.append("0");
		}
		sb.append(yourNumber);

		return sb.toString();
	}

	public static String genDeskToefSN() {
		// return
		// StringUtil.getRandomString(4)+"-"+StringUtil.getRandomString(6)+"-"+StringUtil.getRandomString(6);
		return StringUtil.getRandomNumberAndString(4)
				+ StringUtil.getRandomNumberAndString(4)
				+ StringUtil.getRandomNumberAndString(2);
	}

	/**
	 * 判断一个数组中是否有某一对象,如果有,就返回>=0,没有就返回-1 add by lgj
	 * 
	 * @param a
	 * @param key
	 * @return
	 */
	public static int binarySearch(Object[] a, Object key) {
		if (a == null || key == null) {
			return -1;
		}
		int len = a.length;

		for (int i = 0; i < len; i++) {
			if (a[i].equals(key)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 检索一个字符串中有另一个字符串没有
	 */
	private static boolean compStr(String fixStr, String str) {
		// String fixStr="abcde";
		// String str="ab";
		int i = fixStr.indexOf(str);
		if (i >= 0) {
			return true;
		} else
			return false;
	}

	/**
	 * 如果集合set2中有的元素在集合set1中出现，就从set1中移出
	 * 
	 * @param a
	 * @param key
	 * @return
	 */
	public static Set removeSet(Set set1, Set set2) {
		if (set2 == null || set2.size() <= 0) {
			return set1;
		}
		Set set3 = set1;
		Object[] obj1 = set1.toArray();
		Object[] obj2 = set2.toArray();
		for (int i = 0; i < obj1.length; i++) {
			for (int j = 0; j < obj2.length; j++) {
				if (obj1[i].equals(obj2[j])) {
					set3.remove(obj2[j]);
				}
			}
		}
		return set3;
	}

	public static int getSize(String input) {
		String str = input;
		int size = 0;
		byte[] b = str.getBytes();
		size = b.length;
		return size;
	}

	/**
	 * 由于实际的需要,现在把string[]中的值先转换成long,然后再 放到list中
	 */
	public static ArrayList str2List(String[] str) {
		if (str == null) {
			return null;
		}
		int len = str.length;
		ArrayList list = new ArrayList();
		for (int i = 0; i < len; i++) {
			list.add(new Long(str[i]));
		}
		return list;
	}

	public static String array2String(String[] str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
			sb.append(";");
		}
		return sb.toString();
	}

	public static String array2Str(String[] str) {
		StringBuffer sb = new StringBuffer();
		int len = str.length;
		for (int i = 0; i < len; i++) {
			sb.append(str[i]);
			if (i < len - 1)
				sb.append(",");
		}
		return sb.toString();
	}
	
	/**
	 * list<Object>转换成英文逗号间隔的字符串
	 * @param list
	 * @return
	 */
	public static String List2StrOfId(List<Object> list) {
		StringBuffer sb = new StringBuffer();
		if(list==null || list.size()==0) return null;
		int len = list.size();
		for (int i = 0; i < list.size(); i++) {
			sb.append((Integer)list.get(i));
			if(i<len-1) sb.append(",");
		}
		return sb.toString();
	}

	public static String getSaveAsName() {
		// String dateStr = DateUtil.convertDateToString2(new Date());
		String dateStr = DateTimeUtil.getDateTime("yyMMddHHmmss");
		String randomstr = new Double(Math.random()).toString();
		randomstr = randomstr.substring(2, 7);
		return dateStr + randomstr;
	}

	public static String getDateTimeStr() {
		return DateTimeUtil.getDateTime("yyyyMMddHHmmss");
	}

	/**
	 * 清除String中的html标记
	 * 
	 * @param input
	 * @return
	 */
	public static String clearHtml(String input) {
		String regex = "<[\\s\\S]*?>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String replaceHtmlSpecial(String input) {
		if (input == null || input.length() == 0)
			return input;
		// 带有实体名称的 ASCII 实体
		input = input.replace("&quot;", "\"");
		input = input.replace("&apos;", "'");
		input = input.replace("&amp;", "&");
		input = input.replace("&lt;", "<");
		input = input.replace("&gt;", ">");

		// ISO 8859-1 符号实体
		input = input.replace("&nbsp;", " ");
		input = input.replace("&iexcl;", "¡");
		input = input.replace("&cent;", "¢");
		input = input.replace("&pound;", "£");
		input = input.replace("&curren;", "¤");
		input = input.replace("&yen;", "¥");
		input = input.replace("&brvbar;", "¦");
		input = input.replace("&sect;", "§");
		input = input.replace("&uml;", "¨");
		input = input.replace("&copy;", "©");
		input = input.replace("&ordf;", "ª");
		input = input.replace("&laquo;", "«");
		input = input.replace("&not;", "¬");
		input = input.replace("&shy;", "");
		input = input.replace("&reg;", "®");
		input = input.replace("&macr;", "¯");
		input = input.replace("&deg;", "°");
		input = input.replace("&plusmn;", "±");
		input = input.replace("&sup2;", "²");
		input = input.replace("&sup3;", "³");
		input = input.replace("&acute;", "´");
		input = input.replace("&micro;", "µ");
		input = input.replace("&para;", "¶");
		input = input.replace("&middot;", "·");
		input = input.replace("&cedil;", "¸");
		input = input.replace("&sup1;", "¹");
		input = input.replace("&ordm;", "º");
		input = input.replace("&raquo;", "»");
		input = input.replace("&frac14;", "¼");
		input = input.replace("&frac12;", "½");
		input = input.replace("&frac34;", "¾");
		input = input.replace("&iquest;", "¿");
		input = input.replace("&times;", "×");
		input = input.replace("&divide;", "÷");

		// ISO 8859-1 字符实体
		input = input.replace("&Agrave;", "À");
		input = input.replace("&Aacute;", "Á");
		input = input.replace("&Acirc;", "Â");
		input = input.replace("&Atilde;", "Ã");
		input = input.replace("&Auml;", "Ä");
		input = input.replace("&Aring;", "Å");
		input = input.replace("&AElig;", "Æ");
		input = input.replace("&Ccedil;", "Ç");
		input = input.replace("&Egrave;", "È");
		input = input.replace("&Eacute;", "É");
		input = input.replace("&Ecirc;", "Ê");
		input = input.replace("&Euml;", "Ë");
		input = input.replace("&Igrave;", "Ì");
		input = input.replace("&Iacute;", "Í");
		input = input.replace("&Icirc;", "Î");
		input = input.replace("&Iuml;", "Ï");
		input = input.replace("&ETH;", "Ð");
		input = input.replace("&Ntilde;", "Ñ");
		input = input.replace("&Ograve;", "Ò");
		input = input.replace("&Oacute;", "Ó");
		input = input.replace("&Ocirc;", "Ô");
		input = input.replace("&Otilde;", "Õ");
		input = input.replace("&Ouml;", "Ö");
		input = input.replace("&Oslash;", "Ø");
		input = input.replace("&Ugrave;", "Ù");
		input = input.replace("&Uacute;", "Ú");
		input = input.replace("&Ucirc;", "Û");
		input = input.replace("&Uuml;", "Ü");
		input = input.replace("&Yacute;", "Ý");
		input = input.replace("&THORN;", "Þ");
		input = input.replace("&szlig;", "ß");
		input = input.replace("&agrave;", "à");
		input = input.replace("&aacute;", "á");
		input = input.replace("&acirc;", "â");
		input = input.replace("&atilde;", "ã");
		input = input.replace("&auml;", "ä");
		input = input.replace("&aring;", "å");
		input = input.replace("&aelig;", "æ");
		input = input.replace("&ccedil;", "ç");
		input = input.replace("&egrave;", "è");
		input = input.replace("&eacute;", "é");
		input = input.replace("&ecirc;", "ê");
		input = input.replace("&euml;", "ë");
		input = input.replace("&igrave;", "ì");
		input = input.replace("&iacute;", "í");
		input = input.replace("&icirc;", "î");
		input = input.replace("&iuml;", "ï");
		input = input.replace("&eth;", "ð");
		input = input.replace("&ntilde;", "ñ");
		input = input.replace("&ograve;", "ò");
		input = input.replace("&oacute;", "ó");
		input = input.replace("&ocirc;", "ô");
		input = input.replace("&otilde;", "õ");
		input = input.replace("&ouml;", "ö");
		input = input.replace("&oslash;", "ø");
		input = input.replace("&ugrave;", "ù");
		input = input.replace("&uacute;", "ú");
		input = input.replace("&ucirc;", "û");
		input = input.replace("&uuml;", "ü");
		input = input.replace("&yacute;", "ý");
		input = input.replace("&thorn;", "þ");
		input = input.replace("&yuml;", "ÿ");

		// HTML 支持的数学符号
		input = input.replace("&forall;", "∀");
		input = input.replace("&part;", "∂");
		input = input.replace("&exists;", "∃");
		input = input.replace("&empty;", "∅");
		input = input.replace("&nabla;", "∇");
		input = input.replace("&isin;", "∈");
		input = input.replace("&notin;", "∉");
		input = input.replace("&ni;", "∋");
		input = input.replace("&prod;", "∏");
		input = input.replace("&sum;", "∑");
		input = input.replace("&minus;", "−");
		input = input.replace("&lowast;", "∗");
		input = input.replace("&radic;", "√");
		input = input.replace("&prop;", "∝");
		input = input.replace("&infin;", "∞");
		input = input.replace("&ang;", "∠");
		input = input.replace("&and;", "∧");
		input = input.replace("&or;", "∨");
		input = input.replace("&cap;", "∩");
		input = input.replace("&cup;", "∪");
		input = input.replace("&int;", "∫");
		input = input.replace("&there4;", "∴");
		input = input.replace("&sim;", "∼");
		input = input.replace("&cong;", "≅");
		input = input.replace("&asymp;", "≈");
		input = input.replace("&ne;", "≠");
		input = input.replace("&equiv;", "≡");
		input = input.replace("&le;", "≤");
		input = input.replace("&ge;", "≥");
		input = input.replace("&sub;", "⊂");
		input = input.replace("&sup;", "⊃");
		input = input.replace("&nsub;", "⊄");
		input = input.replace("&sube;", "⊆");
		input = input.replace("&supe;", "⊇");
		input = input.replace("&oplus;", "⊕");
		input = input.replace("&otimes;", "⊗");
		input = input.replace("&perp;", "⊥");
		input = input.replace("&sdot;", "⋅");

		// HTML 支持的希腊字母
		input = input.replace("&Alpha;", "Α");
		input = input.replace("&Beta;", "Β");
		input = input.replace("&Gamma;", "Γ");
		input = input.replace("&Delta;", "Δ");
		input = input.replace("&Epsilon;", "Ε");
		input = input.replace("&Zeta;", "Ζ");
		input = input.replace("&Eta;", "Η");
		input = input.replace("&Theta;", "Θ");
		input = input.replace("&Iota;", "Ι");
		input = input.replace("&Kappa;", "Κ");
		input = input.replace("&Lambda;", "Λ");
		input = input.replace("&Mu;", "Μ");
		input = input.replace("&Nu;", "Ν");
		input = input.replace("&Xi;", "Ξ");
		input = input.replace("&Omicron;", "Ο");
		input = input.replace("&Pi;", "Π");
		input = input.replace("&Rho;", "Ρ");
		input = input.replace("&Sigma;", "Σ");
		input = input.replace("&Tau;", "Τ");
		input = input.replace("&Upsilon;", "Υ");
		input = input.replace("&Phi;", "Φ");
		input = input.replace("&Chi;", "Χ");
		input = input.replace("&Psi;", "Ψ");
		input = input.replace("&Omega;", "Ω");
		input = input.replace("&alpha;", "α");
		input = input.replace("&beta;", "β");
		input = input.replace("&gamma;", "γ");
		input = input.replace("&delta;", "δ");
		input = input.replace("&epsilon;", "ε");
		input = input.replace("&zeta;", "ζ");
		input = input.replace("&eta;", "η");
		input = input.replace("&theta;", "θ");
		input = input.replace("&iota;", "ι");
		input = input.replace("&kappa;", "κ");
		input = input.replace("&lambda;", "λ");
		input = input.replace("&mu;", "μ");
		input = input.replace("&nu;", "ν");
		input = input.replace("&xi;", "ξ");
		input = input.replace("&omicron;", "ο");
		input = input.replace("&pi;", "π");
		input = input.replace("&rho;", "ρ");
		input = input.replace("&sigmaf;", "ς");
		input = input.replace("&sigma;", "σ");
		input = input.replace("&tau;", "τ");
		input = input.replace("&upsilon;", "υ");
		input = input.replace("&phi;", "φ");
		input = input.replace("&chi;", "χ");
		input = input.replace("&psi;", "ψ");
		input = input.replace("&omega;", "ω");
		input = input.replace("&thetasym;", "ϑ");
		input = input.replace("&upsih;", "ϒ");
		input = input.replace("&piv;", "ϖ");

		// HTML 支持的其他实体
		input = input.replace("&OElig;", "Œ");
		input = input.replace("&oelig;", "œ");
		input = input.replace("&Scaron;", "Š");
		input = input.replace("&scaron;", "š");
		input = input.replace("&Yuml;", "Ÿ");
		input = input.replace("&fnof;", "ƒ");
		input = input.replace("&circ;", "ˆ");
		input = input.replace("&tilde;", "˜");
		input = input.replace("&ensp;", "");
		input = input.replace("&emsp;", "");
		input = input.replace("&thinsp;", "");
		input = input.replace("&zwnj;", "‌");
		input = input.replace("&zwj;", "‍");
		input = input.replace("&lrm;", "");
		input = input.replace("&rlm;", "");
		input = input.replace("&ndash;", "–");
		input = input.replace("&mdash;", "—");
		input = input.replace("&lsquo;", "‘");
		input = input.replace("&rsquo;", "’");
		input = input.replace("&sbquo;", "‚");
		input = input.replace("&ldquo;", "“");
		input = input.replace("&rdquo;", "”");
		input = input.replace("&bdquo;", "„");
		input = input.replace("&dagger;", "†");
		input = input.replace("&Dagger;", "‡");
		input = input.replace("&bull;", "•");
		input = input.replace("&hellip;", "…");
		input = input.replace("&permil;", "‰");
		input = input.replace("&prime;", "′");
		input = input.replace("&Prime;", "″");
		input = input.replace("&lsaquo;", "‹");
		input = input.replace("&rsaquo;", "›");
		input = input.replace("&oline;", "‾");
		input = input.replace("&euro;", "€");
		input = input.replace("&trade;", "™");
		input = input.replace("&larr;", "←");
		input = input.replace("&uarr;", "↑");
		input = input.replace("&rarr;", "→");
		input = input.replace("&darr;", "↓");
		input = input.replace("&harr;", "↔");
		input = input.replace("&crarr;", "↵");
		input = input.replace("&lceil;", "");
		input = input.replace("&rceil;", "");
		input = input.replace("&lfloor;", "");
		input = input.replace("&rfloor;", "");
		input = input.replace("&loz;", "◊");
		input = input.replace("&spades;", "♠");
		input = input.replace("&clubs;", "♣");
		input = input.replace("&hearts;", "♥");
		input = input.replace("&diams;", "♦");

		return input;
	}

	/**
	 * 获取数字字符串
	 * 
	 * @return
	 */
	public static String getNumberStr() {
		String s = DateTimeUtil
				.getYyyyMMddStr(Calendar.getInstance().getTime());
		return s;
	}

	public static String getNumberStrOfYyyymm() {
		return DateTimeUtil.getYyyyMMStr(Calendar.getInstance().getTime());
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param newName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		if (fileName == null)
			return "";
		if (fileName.trim().length() == 0)
			return "";
		String[] ext = fileName.split("\\.");
		int len = ext.length;
		if (len > 1) {
			return "." + ext[len - 1].trim();
		} else {
			return ".unknown";
		}
	}

	public static boolean chkString(String str) {
		if (str == null || str.trim().length() == 0)
			return false;
		return true;
	}

	public static boolean chkEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		if (email.indexOf("@") > 0 && email.indexOf(".") > 0)
			return true;
		return false;
	}

	public static boolean chkArrayContains(String[] arr, String str) {
		if (arr == null || arr.length == 0)
			return false;
		if (str == null || str.trim().length() == 0)
			return false;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].trim().equals(str.trim()))
				return true;
		}
		return false;
	}

	public static String getFromSql(String sql) {
		if (!StringUtil.chkString(sql))
			return "";
		if (sql.indexOf("from") > 0) {
			return "from" + sql.split("from")[1];
		}
		return sql;
	}

	public static String getRelativeString(String content, String webContext,
			String filter) {
		if (!ParamUtils.chkString(content))
			return content;
		Pattern p = Pattern.compile("http(.*)" + filter);
		// 用Pattern类的matcher()方法生成一个Matcher对象
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		// 使用find()方法查找第一个匹配的对象
		boolean result = m.find();
		// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
		while (result) {
			m.appendReplacement(sb, webContext + filter);
			result = m.find();
		}
		// 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
		m.appendTail(sb);
		return sb.toString();

	}

	public static boolean noValue(String value) {
		if (null == value || "".equals(value.trim())
				|| "null".equals(value.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static String getFckContentHrefFilter(String content) {
		if (!ParamUtils.chkString(content))
			return content;
		String root = RamConstants.getWebappContext();
		Pattern p = Pattern.compile("http(.*)/file_upload/Image/");
		// 用Pattern类的matcher()方法生成一个Matcher对象
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		// 使用find()方法查找第一个匹配的对象
		boolean result = m.find();
		// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
		while (result) {
			m.appendReplacement(sb, root + "/file_upload/Image/");
			result = m.find();
		}
		// 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
		m.appendTail(sb);
		return sb.toString();
	}

	public final static String vobGeneralMD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断手机号
	 * 
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((14[0-9])|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}

	/**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param s
	 *            原文件名
	 * @return 重新编码后的文件名
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {

					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 检测马来西亚手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNOMalaysia(String mobiles) {
		Pattern p = Pattern.compile("^((1[2-9]))\\d{7}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	public static String checkAPIHttpUrl(String url) {
		if (ParamUtils.chkString(url)) {
			if (url.indexOf("http") > -1) {
				return url;
			} else {
				return Constants.getDomainName() + url;
			}
		} else {
			return url;
		}

	}

	public static String formatImgHttpUrlToRelative(String url) {
		if (ParamUtils.chkString(url)) {
			if (url.indexOf("http") > -1 && url.indexOf("file_upload") > -1) {
				return "/file_upload" + url.split("file_upload")[1];
			} else {
				return url;
			}
		} else {
			return url;
		}
	}

	static public String filterOffUtf8Mb4(String text)
			throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("utf-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

//	/**
//	 * 比较版本号，支付1.0 1.0.5等
//	 * 
//	 * @param latestVer
//	 *            最新版本
//	 * @param ver
//	 *            当前传递版本
//	 * @return >0表示有新版本 =0表示当前是最新版本 <0表示版本超出最新版本的错误情况
//	 */
//	public static int compareVersion(String latestVer, String ver) {
//		if (latestVer == null && ver == null)
//			return 0;
//		else if (latestVer == null)
//			return -1;
//		else if (ver == null)
//			return 1;
//
//		String[] arr1 = latestVer.split("[^a-zA-Z0-9]+"), arr2 = ver
//				.split("[^a-zA-Z0-9]+");
//		int i1, i2, i3;
//
//		for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++) {
//			if (ii == arr1.length)
//				return ii == arr2.length ? 0 : -1;
//			else if (ii == arr2.length)
//				return 1;
//
//			try {
//				i1 = Integer.parseInt(arr1[ii]);
//			} catch (Exception x) {
//				i1 = Integer.MAX_VALUE;
//			}
//
//			try {
//				i2 = Integer.parseInt(arr2[ii]);
//			} catch (Exception x) {
//				i2 = Integer.MAX_VALUE;
//			}
//
//			if (i1 != i2)
//				return i1 - i2;
//			i3 = arr1[ii].compareTo(arr2[ii]);
//			if (i3 != 0)
//				return i3;
//		}
//		return 0;
//	}
	
	/**
	 * 比较版本号，点号分割逐一比较主/次版本号大小，支持x.y.z的格式
	 * xyz取值范围：[0-999]整数，如：[0.5.1] [1.5.16]
	 * 示例：compareVersion(1.0.10,1.0.9) return 1;有新版本
	 * @param latestVer 最新版本
	 * @param ver 当前传递版本
	 * @return >0表示有新版本 =0表示当前是最新版本 <0表示版本超出最新版本的错误情况
	 */
	public static int compareVersion(String latestVer, String ver){  
	    if (latestVer == null || ver == null) {  
	        return 0;
	    }  
	    String[] versionArray2 = ver.split("\\.");//注意此处为正则匹配，不能用"."；  
	    String[] versionArray1 = latestVer.split("\\.");
	    int idx = 0;  
	    int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值  
	    int diff = 0;  
	    while (idx < minLength  
	            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度  
	            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符  
	        ++idx;  
	    }  
	    //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；  
	    diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;  
	    return diff;  
	}


	/**
	 * 比较版本号，支持[x.y.z][x.yz][x.yzz]等 如：1.0 1.0.5 1.21，0.51等
	 * 
	 * @param latestVer
	 *            最新版本
	 * @param ver
	 *            当前传递版本
	 * @return >0表示有新版本 =0表示当前是最新版本 <0表示版本超出最新版本的错误情况
	 */
//	public static int compareVer(String latestVer, String ver) {
//		if (latestVer == null && ver == null)
//			return 0;
//		else if (latestVer == null)
//			return -1;
//		else if (ver == null)
//			return 1;
//		return latestVer.compareTo(ver);
//	}
	
	/**
	 * 截取字符串
	 * @param content 内容
	 * @param length 截取的长度
	 * @return
	 */
	public static String substring(String content,int length){
		if(ParamUtils.chkString(content)){
			int size = content.length();
			if(size>length){
				content = content.substring(0, length);
				content=content+"...";
			}
		}
		return content;
	}
	
	//add raopeng 
	   public static boolean isMobileDevice(String userAgent){
		   String str = StringUtil.checkMobileAgent(userAgent);
		   if(str.equals("ios") || str.equals("android")){
			   return true;
		   }
		   return false;
	   }
	   public static String checkMobileAgent(String userAgent){
		   	if(userAgent!=null && userAgent.length()>0){
		   		if(userAgent.toLowerCase().indexOf("iphone")>-1){
		   			return "ios";
		   		}else if(userAgent.toLowerCase().indexOf("android")>-1){
		   			return "android";
		   		}else{
		   			return "unknown";
		   		}
		   	}else{
		   		return "unknown";
		   	}
		   }
	   
		public static boolean chkListIntContains(List<Integer> list, Integer id) {
			if (list == null || list.size() == 0 || id==null) return false;
			for(Integer cId:list){
				if(cId.equals(id)) return true;
			}
			return false;
		}
	   
	   public static String httpTohttps(HttpServletRequest request,String str){
//			String device=StringUtil.checkMobileAgent(request.getHeader("User-Agent"));
//
//			if(device.equals("ios")){
//				if(str.indexOf("https:")>-1){
//					return str;
//				}else{
//					str=str.replaceAll("http", "https");
//					return str;
//				}
//			}else{
				return str;
//			}

		}
}
