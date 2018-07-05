package com.ram.tag;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class JspCacheClearTag extends BodyTagSupport  {

	private static final long serialVersionUID = 1L;

	
	public JspCacheClearTag() {
	}


	

	
	public int doStartTag() throws JspException {
	    	return EVAL_PAGE;	
	}

	public int doAfterBody() throws JspException {

		return SKIP_BODY;
	}
    
	public int doEndTag() throws JspException {
		try{
			HttpServletResponse response=(HttpServletResponse)pageContext.getResponse();
			response.setHeader("pragma","no-cache");  
		    response.setHeader("cache-control","no-cache");  
		    response.setDateHeader("expires",  0);	
		   
		}catch(Exception ex){
		}
		return EVAL_PAGE;
	}





}