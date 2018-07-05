package com.framework.exception.business;

import com.framework.exception.BusinessException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author yangjy
 * @version 1.0
 */

public class FilesFileException
    extends BusinessException {

  /**
	 * 
	 */
	private static final long serialVersionUID = -5554481556706061082L;

public FilesFileException() {
  }

  public FilesFileException(String message) {
    super(message);
  }

  public FilesFileException(String message, Throwable cause) {
    super(message, cause);
  }

  public FilesFileException(Throwable cause) {
    super(cause);
  }

}
