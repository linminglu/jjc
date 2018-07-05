/*
 * Created on 2005-10-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.exception.permission;


import com.framework.exception.BusinessException;

/**
 * @author hulei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NoFunctionPermissionException extends BusinessException{

	private static final long serialVersionUID = 1L;
	public NoFunctionPermissionException() {
	}
	
	public NoFunctionPermissionException(String message) {
		super(message);
	}
	
	public NoFunctionPermissionException(String message, Throwable cause) {
	    super(message, cause);
	}
	
	public NoFunctionPermissionException(Throwable cause) {
	    super(cause);
	}
}
