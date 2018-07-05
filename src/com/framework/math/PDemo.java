/**
 * 
 */
package com.framework.math;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.framework.exception.ParserException;
/**
 * @author wangyuanjun
 * company bpc
 * 2006-3-28
 */
// Demonstrate the parser. 
		
		class PDemo { 
		 public static void main(String args[]) 
		 throws IOException
		 { 
		 String expr;
		 BufferedReader br = new
		 BufferedReader(new InputStreamReader(System.in));
		 Parser p = new Parser(); 
//		 System.out.println("Enter an empty expression to stop."); 
		 for(;;) { 
//		 System.out.print("Enter expression: "); 
		 expr = br.readLine(); 
		 if(expr.equals("")) break; 
		 try {
//		 System.out.println("Result: " + p.evaluate(expr)); 
//		 System.out.println();
		 } catch (ParserException exc) {
//		 System.out.println(exc);
		 }
		 } 
		 } 
		}

