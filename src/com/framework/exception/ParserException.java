/**
 * 
 */
package com.framework.exception;

/**
 * @author wangyuanjun
 * company bpc
 * 2006-3-28
 */
public class ParserException extends BaseRuntimeException{
	private static final long serialVersionUID = -5554481556706061082L;

	public  ParserException() {
	  super();  
	}

	  public ParserException(String message) {
	    super(message);
	  }

	  public ParserException(String message, Throwable cause) {
	    super(message, cause);
	  }

	  public ParserException(Throwable cause) {
	    super(cause);
	  }

}
