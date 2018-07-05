		/*
		 * Created on 2005-7-13
		 *
		 * TODO To change the template for this generated file go to
		 * Window - Preferences - Java - Code Style - Code Templates
		 */
		package com.framework.web.strutsplug;
		
		import java.sql.Timestamp;
import java.util.Date;
		
		import javax.servlet.ServletException;
		
		import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
		import org.apache.struts.action.ActionServlet;
		import org.apache.struts.action.PlugIn;
		import org.apache.struts.config.ModuleConfig;
		
import com.framework.util.CurrencyConverter;
		import com.framework.util.DateConverter;
import com.framework.util.TimestampConverter;
		
		/**
		 * @author Administrator
		 *
		 * TODO To change the template for this generated type comment go to
		 * Window - Preferences - Java - Code Style - Code Templates
		 */
		public class ConvertPlugIn implements PlugIn { 
		    private static Long defaultLong = null;
			
			public void destroy() { 
			ConvertUtils.deregister(); 
			} 
			public void init(ActionServlet arg0, ModuleConfig arg1) 
			throws ServletException { 
			ConvertUtils.register(new DateConverter(), Date.class); 
	
		      ConvertUtils.register(new CurrencyConverter(), Double.class);
		      ConvertUtils.register(new DateConverter(), Date.class);
		      ConvertUtils.register(new DateConverter(), String.class);
		      ConvertUtils.register(new LongConverter(defaultLong), Long.class);
		      ConvertUtils.register(new IntegerConverter(defaultLong), Integer.class);
		      ConvertUtils.register(new TimestampConverter(), Timestamp.class);
	
			} 
			}