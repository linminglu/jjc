package com.framework.exception.business;

import com.framework.exception.BusinessException;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author zhangkeyi
 * @version 1.0
 */
public class UnknownException extends BusinessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -448097424335326425L;

	//~ Constructors ===========================================================

    /**
         *
         */
    public UnknownException() {
        super();
    }

    /**
     * @param s
     */
    public UnknownException(String s) {
        super(s);
    }

    /**
     * @param s
     * @param e
     */
    public UnknownException(String s, Throwable e) {
        super(s, e);
    }

    /**
     * @param e
     */
    public UnknownException(Throwable e) {
        super(e);
    }
}
