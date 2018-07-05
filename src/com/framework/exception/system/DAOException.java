package com.framework.exception.system;

import com.framework.exception.SystemException;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author zhangkeyi
 * @version 1.0
 */
public class DAOException extends SystemException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2127663125508028412L;

	//~ Constructors ===========================================================

    /**
         *
         */
    public DAOException() {
        super();
    }

    /**
     * @param message
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
}
