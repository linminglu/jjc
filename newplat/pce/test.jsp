<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="java.util.HashMap"/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
 
<html> 
<head></head>
<body>

  <%ArrayList  testList=new ArrayList();
    HashMap testMap1=new HashMap();
    testMap1.put("grade","七年级");
    testMap1.put("class","一班");
    testMap1.put("master","张三");
    testList.add(testMap1);
    HashMap testMap2=new HashMap();
    testMap2.put("grade","八年级");
    testMap2.put("class","二班");
    testMap2.put("master","张三");
    testList.add(testMap2);
    HashMap testMap3=new HashMap();
    testMap3.put("grade","九年级");
    testMap3.put("class","五班");
    testMap3.put("master","李四");
    testList.add(testMap3);
    request.setAttribute("testList",testList);
     %>

    <table border="1" cellpadding="0" cellspacing="0" bordercolor="#0033CC" name="tableList">
         <tr><td>master</td><td>class</td><td>grade</td></tr>
          <logic:iterate id="element" indexId="ind" name="testList" >
              <tr><td>
               <logic:iterate id="elementValue" indexId="idx" name="element">
                     <logic:equal name="elementValue" property="key" value="master"> 
                           <bean:write name="elementValue" property="value"/>
                     </logic:equal>
           </logic:iterate>
          </td>
           <td> 
           <logic:iterate id="elementValue" indexId="idx" name="element">
                 <logic:equal name="elementValue" property="key" value="class">             
                        <bean:write name="elementValue" property="value"/>                    
                  </logic:equal>     
           
			</logic:iterate>
			</td>
			<td>
			<logic:iterate id="elementValue" indexId="idx" name="element"> 
			       <logic:equal name="elementValue" property="key" value="grade"> 
			               <bean:write name="elementValue" property="value"/>
			        </logic:equal>
			 </logic:iterate>
			 
			 </td> 
			</tr>
           </logic:iterate>
            </table>


</body>

</html>