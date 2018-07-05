package com.framework.exception.business;

import com.framework.exception.BusinessException;


public class NoExistFileException
    extends BusinessException {

  /**
	 * 
	 */
	private static final long serialVersionUID = -6117046954424618297L;

public NoExistFileException() {
  }

  public NoExistFileException(String message) {
    super(message);
  }

  public NoExistFileException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoExistFileException(Throwable cause) {
    super(cause);
  }
}
