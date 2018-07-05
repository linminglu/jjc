package com.framework.exception.business;

import com.framework.exception.BusinessException;

public class NoFromMailException extends BusinessException{
  /**
	 * 
	 */
	private static final long serialVersionUID = -6166569438022931508L;
public NoFromMailException() {
  }
  public NoFromMailException(String message) {
    super(message);
  }
}
