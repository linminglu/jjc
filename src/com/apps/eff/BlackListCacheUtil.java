package com.apps.eff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.apps.Constants;
import com.apps.model.BlackList;
import com.apps.service.IBlackListService;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DesUtils;
import com.framework.util.ParamUtils;
import com.ram.util.IPUtil;

/**
 * 
 * 
 * @author Mr.zang
 * 
 */
public class BlackListCacheUtil {
	public static Logger log = Logger.getLogger(BlackListCacheUtil.class);
	private final static IBlackListService blackListService = (IBlackListService) ServiceLocatorImpl
			.getInstance().getService("blackListService");
	private final static int DURING_TIME = 86400*1000;//毫秒
	public static int onlineNumber = 0;//在线人数
	
	
	/**
	 * 保存U添加到在线列表
	 * @return u
	 */
	public static void saveOnlineListU(String u) {
		if(!ParamUtils.chkString(u)){
			return;
		}
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("onlineList");
			if (element != null) {
				map = (Map<String, Long>) element.getObjectValue();
			}
			map.put(u, System.currentTimeMillis());
			element = new Element("onlineList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 在线用户的列表
	 * @return map<u,时间戳>
	 */
	public static Map<String, Long> getOnlineListU() {
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("onlineList");
			if (element != null) {
				map = (Map<String, Long>) element.getObjectValue();
			} else {
				element = new Element("onlineList", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 删除用户U
	 * @return u
	 */
	public static void delOnlineListU(String u) {
		if(!ParamUtils.chkString(u)){
			return;
		}
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("onlineList");
			if (element != null) {
				map = (Map<String, Long>) element.getObjectValue();
			}
			map.remove(u);
			element = new Element("onlineList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/***
	 * 定时清除登出缓存
	 */
	public static void clearBlacklistU() {
		Map<String, Long> map = getBlacklistU();
		Set<Map.Entry<String,Long>> entry = map.entrySet();  
		Iterator<Map.Entry<String,Long>> it = entry.iterator();  
		while (it.hasNext()){
            //将键值关系取出存入Map.Entry这个映射关系集合接口中  
            Map.Entry<String,Long>  me = it.next();  
            //使用Map.Entry中的方法获取键和值  
            String u = me.getKey();  
            Long time = me.getValue();  
            Long dtNow=System.currentTimeMillis();//当前时间
    		if(time==null) time = dtNow;
    		long diff = dtNow-time;//时间差
    		
    		if(diff>DURING_TIME){//需要清除
    			map.remove(u);
    		}
       }
	}
	
	
	/**
	 * 保存U添加到登录列表
	 * @return uid
	 */
	public static void saveBlacklistU(String u) {
		if(!ParamUtils.chkString(u)){
			return;
		}
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("userOutList");
			if (element != null) {
				map = (Map<String, Long>) element.getObjectValue();
			}
			map.put(u, System.currentTimeMillis());
			element = new Element("userOutList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除用户U
	 * @return uid
	 */
	public static void delBlacklistU(String u) {
		if(!ParamUtils.chkString(u)){
			return;
		}
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("userOutList");
			if (element != null) {
				map = (Map<String, Long>) element.getObjectValue();
			}
			map.remove(u);
			element = new Element("userOutList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 需要登出用户的列表
	 * @return 时间戳,有可能为空
	 */
	public static Long getBlacklistU(String u) {
		Map<String, Long> map = getBlacklistU();
		Long time=map.get(u);
		return time;
	}
	/**
	 * 需要登出用户的列表
	 * @return map<u,时间戳>
	 */
	public static Map<String, Long> getBlacklistU() {
		Map<String, Long> map = new HashMap<String, Long>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("userOutList");
			if (element != null) {
				map = (Map<String, Long>) element.getObjectValue();
			} else {
				element = new Element("userOutList", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 保存用户id到黑名单
	 * @return uid
	 */
	public static void saveBlacklistUser(Integer uid) {
		if(!ParamUtils.chkInteger(uid)){
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("userList");
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			}
			map.put(uid+"", uid+"");
			element = new Element("userList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存用户id到黑名单
	 * @return uid
	 */
	public static void delBlacklistUser(Integer uid) {
		if(!ParamUtils.chkInteger(uid)){
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("userList");
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			}
			map.remove(uid+"");
			element = new Element("userList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得用户黑名单
	 * @return map<uid,uid>
	 */
	public static Map<String, String> getBlacklistUser() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("userList");
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			} else {
				//此处获得数据库的黑名单数据
				List<BlackList> list=blackListService.findBlackList("1");//用户id黑名单列表
				if(list!=null&&list.size()>0){
					for(BlackList black:list){
						map.put(black.getValue(), black.getValue());
					}
				}
				element = new Element("userList", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/**
	 * 保存ip到黑名单
	 * @return ip
	 */
	public static void saveBlacklistIP(String ip) {
		if(!ParamUtils.chkString(ip)){
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("ipList");
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			}
			map.put(ip+"", ip+"");
			element = new Element("ipList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存ip到黑名单
	 * @return ip
	 */
	public static void delBlacklistIP(String ip) {
		if(!ParamUtils.chkString(ip)){
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("ipList");
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			}
			map.remove(ip);
			element = new Element("ipList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获得IP黑名单
	 * @return map<ip,ip>
	 */
	public static Map<String, String> getBlacklistIP() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("ipList");
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			} else {
				//此处获得数据库的黑名单数据
				List<BlackList> list=blackListService.findBlackList("2");//用户id黑名单列表
				if(list!=null&&list.size()>0){
					for(BlackList black:list){
						map.put(black.getValue(), black.getValue());
					}
				}
				element = new Element("ipList", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/***
	 * 判断是否在黑名单内-API通用方法
	 * @return true 在黑名单内 false 不在黑名单
	 */
	public static boolean isBlacklist(HttpServletRequest request) {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		Integer uid =0;
		Map<String, String> decryptMap = DesUtils.decryptMap(u);
		if(decryptMap!=null){
			uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
		}
		
		//判断uid黑名单
		if(ParamUtils.chkInteger(uid)){
			Map<String, String> blacklistUser = getBlacklistUser();
			String blacklist = blacklistUser.get(uid);
			if(blacklist!=null){//在黑名单内
				return true;
			}
		}
		
		String ipAddr = IPUtil.getIpAddr(request);
		//判断IP黑名单
		if(ParamUtils.chkString(ipAddr)){
			Map<String, String> blacklistIP = getBlacklistIP();
			String blacklist = blacklistIP.get(ipAddr);
			if(blacklist!=null){//在黑名单内
				return true;
			}
		}
		return false;
	}
	/***
	 * 判断用户是否在黑名单内
	 * @return true 在黑名单内 false 不在黑名单
	 */
	public static boolean isBlacklistUser(Integer uid) {
		//判断uid黑名单
		if(ParamUtils.chkInteger(uid)){
			Map<String, String> blacklistUser = getBlacklistUser();
			String blacklist = blacklistUser.get(uid);
			if(blacklist!=null){//在黑名单内
				return true;
			}
		}
		return false;
	}
	/***
	 * 判断IP是否在黑名单内
	 * @return true 在黑名单内 false 不在黑名单
	 */
	public static boolean isBlacklistIP(String ip) {
		//判断IP黑名单
		if(ParamUtils.chkString(ip)){
			Map<String, String> blacklistIP = getBlacklistIP();
			String blacklist = blacklistIP.get(ip);
			if(blacklist!=null){//在黑名单内
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 更新blackList
	 */
	@SuppressWarnings("unchecked")
	public static void updateBlackList(String type) {
		Map<String, List<BlackList>> map = new HashMap<String, List<BlackList>>();
		List<BlackList> list = null;
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("list");
			if (element != null) {
				map = (Map<String, List<BlackList>>) element.getObjectValue();
			}

			//此处获得数据库的黑名单数据
			list = blackListService.findBlackList(type);//黑名单列表
			map.put(type, list);
			element = new Element("list", map);
			cache.put(element);
		}catch(CacheException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存ip到黑名单
	 * @return ip
	 */
	@SuppressWarnings("unchecked")
	public static void delBlacklistByType(String type,String value) {
		if(!ParamUtils.chkString(type)||!ParamUtils.chkString(value)){
			return;
		}
		Map<String, String> subMap = new HashMap<String, String>();
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("map");
			if (element != null) {
				map = (Map<String, Map<String, String>>) element.getObjectValue();
				if(map.containsKey(type)){
					subMap = map.get(type);
					subMap.remove(value);
					map.put(type, subMap);
					element = new Element("map", map);
					cache.put(element);
				}
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据type获取黑名单数据
	 * @return Map<String,String>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getBlackMapByType(String type) {
		Map<String, Map<String,String>> map = new HashMap<String, Map<String,String>>();
		Map<String,String> subMap = new HashMap<String, String>();
		List<BlackList> list = new ArrayList<BlackList>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("map");
			if (element != null) {
				map = (Map<String, Map<String,String>>) element.getObjectValue();
				if(map.containsKey(type)){
					subMap = map.get(type);
				}else{
					list = getBlacklistByType(type);
					if(list!=null&&list.size()>0){
						for(BlackList black:list){
							subMap.put(black.getValue(), black.getValue());
						}
					}
					map.put(type, subMap);
				}
			} else {
				list = getBlacklistByType(type);
				if(list!=null&&list.size()>0){
					for(BlackList black:list){
						subMap.put(black.getValue(), black.getValue());
					}
				}
				map.put(type, subMap);

				element = new Element("map", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return subMap;
	}
	
	/**
	 * 根据type获取黑名单数据
	 * @return List<BlackList>
	 */
	@SuppressWarnings("unchecked")
	public static List<BlackList> getBlacklistByType(String type) {
		Map<String, List<BlackList>> map = new HashMap<String, List<BlackList>>();
		List<BlackList> list = new ArrayList<BlackList>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("list");
			if (element != null) {
				map = (Map<String, List<BlackList>>) element.getObjectValue();
				if(map.containsKey(type)){
					list = map.get(type);
				}else{
					list = blackListService.findBlackList(type);//黑名单列表
					map.put(type, list);
				}
			} else {
				//此处获得数据库的黑名单数据
				list = blackListService.findBlackList(type);//黑名单列表
				map.put(type, list);
				element = new Element("list", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据type获取保存黑名单数据
	 * @return Map<String,String>
	 */
	@SuppressWarnings("unchecked")
	public static void saveBlackMapByType(String type,String value) {
		Map<String, Map<String,String>> map = new HashMap<String, Map<String,String>>();
		Map<String,String> subMap = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("blacklist");
			Element element = cache.get("map");
			if (element != null) {
				map = (Map<String, Map<String,String>>) element.getObjectValue();
				if(map.containsKey(type)){
					subMap = map.get(type);
					subMap.put(value, value);
					map.put(type, subMap);
					element = new Element("map", map);
					cache.put(element);
				}
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
}
