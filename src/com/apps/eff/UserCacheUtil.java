package com.apps.eff;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.framework.service.impl.ServiceLocatorImpl;
import com.ram.model.User;
import com.ram.service.user.IUserService;

/**
 * 
 * 
 * @author Mr.zang
 * 
 */
public class UserCacheUtil {
	public static Logger log = Logger.getLogger(UserCacheUtil.class);
	private final static IUserService userService = (IUserService) ServiceLocatorImpl
			.getInstance().getService("userService");



	@SuppressWarnings("unchecked")
	public static User getUser(Integer uid){
		User user=null;
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("user");
			Element element = cache.get("userList");
			Map<Integer, User> userList =null;
			if (element != null) {
				userList = (Map<Integer, User>) element.getObjectValue();
				user = userList.get(uid);
				if(user==null){
					User userDB = userService.getUser(uid);
					if(userDB!=null){
						user=userDB;
						userList.put(uid, user);
					}
				}
			} else {
				userList=new Hashtable<Integer, User>();
				User userDB = userService.getUser(uid);
				if(userDB!=null){
					user=userDB;
					userList.put(uid, user);
				}
				element = new Element("userList", userList);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public static void saveUser(User user) {
		if(user==null){
			return;
		}
		Integer userId = user.getUserId();
		
		Map<Integer, User> map = new HashMap<Integer, User>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("user");
			Element element = cache.get("userList");
			if (element != null) {
				map = (Map<Integer, User>) element.getObjectValue();
			}
			map.put(userId, user);
			element = new Element("userList", map);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	
}
