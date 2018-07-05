/*
 * Created on 2006-10-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ObjectNotFoundException extends Exception {

    private Throwable mRootCause = null;
    
    public ObjectNotFoundException(String msg) {
       super(msg);
    }
        
	/**
	 * Construct RollerException, wrapping existing throwable.
	 * @param s Error message
	 * @param t Existing connection to wrap.
	 */
	public ObjectNotFoundException(String s,Throwable t)
	{
		super(s);
        mRootCause = t;
	}
	/**
	 * Construct RollerException, wrapping existing throwable.
	 * @param t Existing exception to be wrapped.
	 */
	public ObjectNotFoundException(Throwable t)
	{
        mRootCause = t;
	}	
	/**
	 * Construct emtpy exception object.
	 */
	public ObjectNotFoundException()
	{
		super();
	}
    /**
     * Get root cause object, or null if none.
     * @return Root cause or null if none.
     */
    public Throwable getRootCause()
    {
        return mRootCause;
    }
   
    /**
     * Print stack trace for exception and for root cause exception if htere is one.
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace()
    {
        super.printStackTrace();
        if (mRootCause != null)
        {
          
            mRootCause.printStackTrace();
        }
    }

    /**
     * Print stack trace for exception and for root cause exception if htere is one.
     * @param s Stream to print to.
     */
    public void printStackTrace(PrintStream s)
    {
        super.printStackTrace(s);
        if (mRootCause != null)
        {
            
            mRootCause.printStackTrace(s);
        }
    }

    /**
     * Print stack trace for exception and for root cause exception if htere is one.
     * @param s Writer to write to.
     */
    public void printStackTrace(PrintWriter s)
    {
       super.printStackTrace(s);
       if (null != mRootCause) 
       {
          
           mRootCause.printStackTrace(s);
       }
    }

    /**
     * Get root cause message.
     * @return Root cause message.
     */
    public String getRootCauseMessage() 
    {
        String rcmessage = null;
        if (getRootCause()!=null) 
        {
            if (getRootCause().getCause()!=null)
            {
                rcmessage = getRootCause().getCause().getMessage();
            }
            rcmessage = (rcmessage == null) ? getRootCause().getMessage() : rcmessage;
            rcmessage = (rcmessage == null) ? super.getMessage() : rcmessage;
            rcmessage = (rcmessage == null) ? "NONE" : rcmessage;
        }
        return rcmessage;
    }
}