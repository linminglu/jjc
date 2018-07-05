package com.apps.util;

/**
 * @author "shihuc"
 * @date   2016年12月1日
 */

/**
 * @author chengsh05
 *
 */
public class IllegalCombinationInputException extends Exception{

    private static final long serialVersionUID = 678024281707796100L;
    
    public IllegalCombinationInputException(){
        super("The combination base number should be not less than the selection number.");  
    }

}