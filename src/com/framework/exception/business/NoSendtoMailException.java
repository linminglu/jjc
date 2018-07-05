package com.framework.exception.business;

import com.framework.exception.BusinessException;

public class NoSendtoMailException extends BusinessException{
  /**
	 * 
	 */
	private static final long serialVersionUID = -2224622101737967890L;

	
public NoSendtoMailException() {
  }
  public NoSendtoMailException(String message) {
    super(message);
  }
}
