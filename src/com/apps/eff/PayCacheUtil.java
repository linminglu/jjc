package com.apps.eff;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.apps.model.Param;
import com.apps.model.PayConfig;
import com.apps.service.IBaseDataService;
import com.framework.service.impl.ServiceLocatorImpl;

/**
 * 
 * 
 * @author Mr.zang
 * 
 */
public class PayCacheUtil {
	public static Logger log = Logger.getLogger(PayCacheUtil.class);
	private final static IBaseDataService baseDataService = (IBaseDataService) ServiceLocatorImpl
			.getInstance().getService("baseDataService");



	@SuppressWarnings("unchecked")
	public static List<PayConfig> getConfigList() {
		List<PayConfig> list = new ArrayList<PayConfig>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("payConfig");
			Element element = cache.get("payConfig");
			if (element != null) {
				list = (List<PayConfig>) element.getObjectValue();
			} else {
				list=baseDataService.findPayConfig();
				element = new Element("payConfig", list);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void delConfigList() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("payConfig");
		if (cache.get("payConfig") != null) {
			cache.remove("payConfig");
		}
	}
	public static void updateConfigList() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("payConfig");
		Element element = cache.get("payConfig");
		List<PayConfig> list =baseDataService.findPayConfig();
		element = new Element("payConfig", list);
		cache.put(element);
	}
	
	
	
	
}
