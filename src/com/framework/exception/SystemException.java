package com.framework.exception;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author zhangkeyi
 * @version 1.0
 */
public class SystemException extends BaseRuntimeException {

    //~ Constructors ===========================================================

    /**
	 * 
	 */
	private static final long serialVersionUID = 8704175275745158962L;

	/**
     *
     */
    public SystemException() {
        super();
    }

    /**
     * @param s
     */
    public SystemException(String s) {
        super(s);
    }

    /**
     * @param s
     * @param e
     */
    public SystemException(String s, Throwable e) {
        super(s, e);
    }

    /**
     * @param e
     */
    public SystemException(Throwable e) {
        super(e);
    }
}
