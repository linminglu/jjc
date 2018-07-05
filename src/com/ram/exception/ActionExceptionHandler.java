package com.ram.exception;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;


/**
 * 处理Action抛出的异常，并且将页面定向到异常提示页面../_error/exception.jsp
 * 
 * @author hulei
 * 
 * 在struts-config-模块.xml中增加下列代码
 	<!-- ==================================== Global Exceptions -->
	<global-exceptions>
		<exception key="error" type="java.lang.Exception"
			handler="com.ram.exception.ActionExceptionHandler" />
	</global-exceptions>

	<!-- ==================================== Global Forwards    -->
	<global-forwards>
		<forward name="exception" path="/_exception.jsp" />
	</global-forwards>
 *	
 *  注意：此处<forward name="error" path="/_exception.jsp" />
 *  的_exception.jsp文件必须在当前模块所在的目录中。
 */
public class ActionExceptionHandler extends ExceptionHandler {

	private static Logger log = Logger.getLogger("ExceptionHandlerImpl");

	public ActionForward execute(Exception ex, ExceptionConfig exCfg,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		
		request.setAttribute("exception",ex.toString());
		log.error("Error Start###############################################################");
		
		//User user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		//log.error("$$$$$$$$$$$$$$$$$ userId is:"+user.getUserId());
		log.error(ex.getLocalizedMessage(),ex);
		log.error("Error end################################################################");
		return mapping.findForward("exception");
	}

	protected void logException(Exception arg0) {

		log.info("Error Catched by ActionExceptionHandler,Exception="
				+ arg0.getMessage());
		super.logException(arg0);
	}

	protected void storeException(HttpServletRequest arg0, String arg1,
			ActionError arg2, ActionForward arg3, String arg4) {

		super.storeException(arg0, arg1, arg2, arg3, arg4);
	}

	protected void storeException(HttpServletRequest arg0, String arg1,
			ActionMessage arg2, ActionForward arg3, String arg4) {
	
		super.storeException(arg0, arg1, arg2, arg3, arg4);
	}

}
