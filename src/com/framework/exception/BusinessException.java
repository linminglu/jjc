package com.framework.exception;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author zhangkeyi
 * @version 1.0
 */
public class BusinessException extends BaseException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2909085718880010342L;

	//~ Constructors ===========================================================

    /**
     *
     */
    public BusinessException() {
        super();
    }

    /**
     * @param s
     */
    public BusinessException(String s) {
        super(s);
    }

    /**
     * @param s
     * @param e
     */
    public BusinessException(String s, Throwable e) {
        super(s, e);
    }

    /**
     * @param e
     */
    public BusinessException(Throwable e) {
        super(e);
    }
}
