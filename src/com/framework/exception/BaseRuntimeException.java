package com.framework.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 通常,属于系统级别的异常没有必要处理的,比如jdbc的一些异常,nullpointment,等等
 * 必须打包成客户已知的异常或者直接作为错误程序处理好,这些异常在本系统内的父类为本类
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) bpc limit company2005</p>
 * <p>Company: bpc</p>
 * @author zhangkeyi
 * @version 1.0
 */
public class BaseRuntimeException extends RuntimeException {

    //~ Instance fields ========================================================

    /**
	 * 
	 */
	private static final long serialVersionUID = 898079688558063444L;

	/** log ini */
    private Log log = LogFactory.getLog(getClass());

    /** private Object */
    private Throwable rootCause;

    //~ Constructors ===========================================================

    /**
     * default constructor
     */
    public BaseRuntimeException() {
        super();
    }

    /**
     * @param arg0 message
     */
    public BaseRuntimeException(final String arg0) {

        //super(arg0);
        this(arg0, null);
        rootCause = this;
    }

    /**
     * @param arg0 throwable
     */
    public BaseRuntimeException(final Throwable arg0) {
        this("", arg0);
    }

    /**
     * @param arg0 message
     * @param arg1 throwable
     */
    public BaseRuntimeException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);

        if (arg1 instanceof BaseRuntimeException) {

            rootCause = ((BaseRuntimeException) arg1).rootCause;
        } else {

            rootCause = arg1;
        }

        log.error(arg0, arg1);
    }
}
