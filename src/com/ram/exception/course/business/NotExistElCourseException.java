/*
 * Created on 2005-10-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.exception.course.business;


import com.framework.exception.BusinessException;

/**
 * @author zky
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NotExistElCourseException extends BusinessException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -5554481556706061082L;

	public NotExistElCourseException() {
	}
	
	public NotExistElCourseException(String message) {
		super(message);
	}
	
	public NotExistElCourseException(String message, Throwable cause) {
	    super(message, cause);
	}
	
	public NotExistElCourseException(Throwable cause) {
	    super(cause);
	}
}
