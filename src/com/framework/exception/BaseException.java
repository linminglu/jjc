package com.framework.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 属于系统商务逻辑或者必须捕获的异常父类,在最终action 层抛出时统一由action层捕获
 * @author zhangkeyi
 */
public class BaseException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6616231117207053545L;


	//~ Instance fields ========================================================
    /** log ini */
    private Log log = LogFactory.getLog(getClass());

    /** private Object */
    private Throwable rootCause;

    //~ Constructors ===========================================================

    public BaseException() {
        super();
    }

    /**
     * @param s
     */
    public BaseException(String s) {
        this(s, null);
        rootCause = this;
    }

    /**
     * Creates a new BaseException object.
     *
     * @param s DOCUMENT ME!
     * @param e DOCUMENT ME!
     */
    public BaseException(String s, Throwable e) {
        super(s);

        if (e instanceof BaseException) {

            rootCause = ((BaseException) e).rootCause;
        } else {

            rootCause = e;
        }
        log.info(s, e);
    }

    /**
     * Creates a new BaseException object.
     * @param e Throwable
     */
    public BaseException(Throwable e) {
        this("", e);
    }

    //~ Methods ================================================================

    /**
     * @return
     */
    public Throwable getRootCause() {

        return rootCause;
    }
}
