package com.apps.pay.wechat;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import com.framework.util.xml.ParserWrapper;
import com.framework.util.xml.Writer;

/**
 *  XML的处理工具类
 *
 *@author     lgj
 *@created    2005年07月30日
 */
public class XmlUtil {
    private static ParserWrapper parser = null;
    private static Node tmpNode = null;


    /**
     *  Constructor for the XmlUtil object
     */
    public XmlUtil() { }


    /**
     *  设置节点的Text值.该节点必须是下列形式: <AA>aaa</AA> 类型,否则可能发生意外错误.
     *
     *@param  node   目标节点
     *@param  value  字符串值
     *@return        成功,返回true,否则返回false
     *@roseuid       3C7EE0250194
     */
    public static boolean setNodeTextValue(Node node, String value) {
        if (node == null) {
            return false;
        }
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return false;
        }
        if (node.getChildNodes().getLength() != 1 && node.getChildNodes().getLength() != 0) {
            return false;
        }
        Node child = node.getFirstChild();
        if (child == null) {
            Text addValue = node.getOwnerDocument().createTextNode(value);
            node.appendChild(addValue);
            return true;
        }
        child.setNodeValue(value);
        return true;
    }


    /**
     *  从XML文件中产生一个Document对象
     *
     *@param  xmlFileName  源XML文件名
     *@return              成功,返回Document对象,否则返回null
     *@roseuid             3C7EE02501B2
     */
    public static synchronized Document getDOMDocument(String xmlFileName) {
        try {
          Document dm=parser.parse(xmlFileName);
          return dm;
        } catch (Exception ex) {
           // zxt.pub.util.LogUtil.getLogger().debug(ex);
            ex.printStackTrace();
            return null;
        }
    }


    /**
     *  从字符串中产生一个Document对象
     *
     *@param  src  源字符串
     *@return      成功,返回Document对象,否则返回null
     *@roseuid     3C7EE02501D0
     */
    public static synchronized Document getDOMDocumentFromString(String src) {
        try {
            src = refineString(src);
            InputSource is = new InputSource(new BufferedReader(new StringReader(src)));
            return parser.parse(is);
        } catch (Exception ex) {
            //Logger.getLogger().log("XmlUtil:getDocumentFromString() error:\n"+ex.toString());
            //Logger.getLogger().log(ex);
            ex.printStackTrace();

            return null;
        }

        /*
         *  try {
         *  src=this.refineString("");
         *  synchronized (this) {
         *  BufferedWriter bw = new BufferedWriter(new FileWriter(".."+Symbol_Java.separator+"work"+Symbol_Java.separator+"tmp"+Symbol_Java.separator+"tmp.xml"));
         *  bw.write(src);
         *  bw.close();
         *  return parser.parse(".."+Symbol_Java.separator+"work"+Symbol_Java.separator+"tmp"+Symbol_Java.separator+"tmp.xml");
         *  }
         *  }
         *  catch (Exception ex) {
         *  ex.printStackTrace();
         *  Logger.getLogger().log(ex.getMessage());
         *  return null;
         *  }
         */
    }


    /**
     *  Gets the DOMDocumentFromString2 attribute of the XmlUtil class
     *
     *@param  src            Description of Parameter
     *@return                The DOMDocumentFromString2 value
     *@exception  Exception  Description of Exception
     */
    public static synchronized Document getDOMDocumentFromString2(String src) throws Exception {
        try {
            src = refineString(src);
            InputSource is = new InputSource(new BufferedReader(new StringReader(src)));
            return parser.parse(is);
        } catch (Exception ex) {
            //Logger.getLogger().log("XmlUtil:getDocumentFromString() error:\n"+ex.toString());
            //Logger.getLogger().log(ex);
            ex.printStackTrace();
            throw ex;
        }
    }


    /**
     *  （扩展）获取Elment的属性，对于值以@开头的，表示引用其他属性 add by wjz 1002
     *
     *@param  e
     *@param  attr
     *@return
     */
    public static String getElementAttribute(Element e, String attr) {
        String s = e.getAttribute(attr);
        if (s == null) {
            return null;
        }
        String a2 = "";
        while (s.startsWith("@")) {
            a2 = s.substring(1);
            s = e.getAttribute(a2);
            if (s == null) {
                return null;
            }
        }
        return s;
    }


    /**
     *  获取节点的文本值. 如果节点是 TEXT_NODE或CDATA_SECTION_NODE类型,那么返回值为node.getNodeValue().trim().否则,返
     *  回值为子节点的文本值的累计.例如 <AA>a</AA> 返回a, <A><AA>a</AA> <AB>b</AB> </A> 返回ab .
     *
     *@param  node  源节点
     *@return       The NodeTextValue value
     *@roseuid      3C7EE0250202
     */
    public static String getNodeTextValue(Node node) {
        if (node != null) {
            String strTemp = "";
            if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
                return node.getNodeValue().trim();
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                StringBuffer sb = new StringBuffer();
                NodeList nlist = node.getChildNodes();
                for (int i = 0; i < nlist.getLength(); i++) {
                    sb.append(getNodeTextValue(nlist.item(i)));
                }
                strTemp = sb.toString();
                sb = null;
                return strTemp;
            } else {
                return "";
            }
        } else {
            return "";
        }

    }


    /**
     *  将Document的对象转换为字符串 <p>
     *
     *  wujinzhong 0604</p>
     *
     *@param  dc  Description of Parameter
     *@return     Description of the Returned Value
     */
    public static String Document2String(Document dc) {
        String s = "";
        try {
            Writer wr = new Writer();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            wr.setOutput(bao, "GB2312");
            wr.setIncludeIgnorableWhitespace(false);
            wr.write(dc);
            bao.flush();
            s = new String(bao.toByteArray(), "GBK");
            bao.close();
            wr.closeOutput();
            return s;
        } catch (Exception e) {
            //LogUtil.getLogger().debug(e.toString());
            return null;
        }

    }


    /**
     *  将Map转换为XML格式的字符串 <p>
     *
     *  add by wjz 1128
     *
     *@param  rootNodeName  Description of Parameter
     *@param  map           Description of Parameter
     *@return               Description of the Returned Value
     */
    public static String Map2XML(String rootNodeName, Map map) {
        if (rootNodeName == null || map == null) {
            return "";
        }
        Object[] keys = map.keySet().toArray();
        if (keys == null) {
            return "";
        }
        int len = keys.length;
        String tab = "    ";
        StringBuffer sb = new StringBuffer("\n<" + rootNodeName + ">");
        Object obj = null;
        String key = null;
        for (int i = 0; i < len; i++) {
            key = (String) keys[i];
            if (key == null) {
                continue;
            }
            key = key.trim();
            obj = map.get(key);
            if (obj == null) {
//                obj = "null";
                obj = "";
            }
            if (obj instanceof Map) {
                sb.append(Map2XML((String) key, (Map) obj));
            } else {
                sb.append(tab + "\n");
                sb.append("<" + key + ">");
                sb.append(obj.toString());
                sb.append("</" + key + ">");
            }
        }
        sb.append("\n</" + rootNodeName + ">");
        return sb.toString();
    }


    /**
     *  将Node的对象转换为字符串 <p>
     *
     *  wujinzhong 0604</p>
     *
     *@param  dc  Description of Parameter
     *@return     Description of the Returned Value
     */
    public static String Node2String(Node dc) {
        String s = "";
        try {
            Writer wr = new Writer();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            wr.setOutput(bao, "GB2312");
            wr.setIncludeIgnorableWhitespace(false);
            wr.write(dc);
            bao.flush();
            s = new String(bao.toByteArray(), "GBK");
            bao.close();
            wr.closeOutput();
            return s;
        } catch (Exception e) {
            //LogUtil.getLogger().debug(e.toString());
            return null;
        }

    }


    /**
     *  检验XML文件是否合法(Valid)
     *
     *@param  xmlFileName  xml文件名
     *@return              合法返回为true,否则返回false
     *@roseuid             3C7EE0250285
     */
    public static boolean Validate(String xmlFileName) {
        boolean temp;
        StringBuffer sb = new StringBuffer();
        boolean blRet = parser.Validate(xmlFileName, sb);
        //utils.Logger.getLogger().Debug("XmlUtil validate:" + sb);
        temp = sb.toString().equalsIgnoreCase("valid") ? true : false;
        sb = null;
        return temp;
    }


    /**
     *  获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看
     *
     *@param  args  The command line arguments
     */
//    public static XPathResult evaluate(String xpath, Document doc) {
//        try {
//            xpath = xpath.replaceAll("&quot;", "'");
//            XPathEvaluator evaluator = new XPathEvaluatorImpl(doc);
//            XPathNSResolver resolver = evaluator.createNSResolver(doc);
//
//            // Evaluate the xpath expression
//            XPathResult result = (XPathResult) evaluator.evaluate(xpath, doc, resolver,
//                    XPathResult.ANY_TYPE, null);
//            return result;
//        }
//        catch (Exception ex) {
//            LogUtil.getLogger().error(ex.getMessage(), ex);
//            return null;
//        }
//
//    }


    /**
     *  获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取Number类型(Double)的表达式的值
     *
     *@param  args  The command line arguments
     */
//    public static double evaluateNumber(String xpath, Document doc) {
//        try {
//            double d = 0;
//            //force to be a valid number expression
//            xpath = xpath + "-0.0";
//            XPathResult result = evaluate(xpath, doc);
//
//            if (result.getResultType() == XPathResult.NUMBER_TYPE) {
//                d = result.getNumberValue();
//                if (java.lang.Double.isNaN(d)) {
//                    d = 0.0;
//                }
//            }else if(result.getResultType() == XPathResult.UNORDERED_NODE_ITERATOR_TYPE)
//            {
//              Node tmpNode=result.iterateNext();
//              if(tmpNode==null)
//                return 0.0;
//              String strValue=XmlUtil.getNodeTextValue(tmpNode);
//              if(strValue==null||strValue.length()<=0)
//                return 0.0;
//              return Double.parseDouble(strValue);
//            }
//            else {
//                throw new java.lang.IllegalAccessException("only can call this method for number result!");
//            }
//            return d;
//        }
//        catch (Exception ex) {
//            LogUtil.getLogger().error(ex.getMessage(), ex);
//            return Double.NaN;
//        }
//
//    }


    /**
     *  获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取Number类型(Double)的表达式的值
     *  获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取Number类型(Double)的表达式的值
     *  获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取Number类型(Double)的表达式的值
     *  获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取Number类型(Double)的表达式的值
     *  获取某个表达式的值 例如 <p>
     *
     *  String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
     *  <p>
     *
     *  Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
     *  <p>
     *
     *  XPathResult xr= xmlUtil1.evaluate(xpath,doc1); <p>
     *
     *  注意：取出XPATHResult后，对于不同的类型，应该调用不同的方法，例如 <p>
     *
     *  上例中，应该调用 xr.getNumberValue() <p>
     *
     *  如果不知道返回值的具体类型，可以调用xr.getResultType() 查看 获取Number类型(Double)的表达式的值 The
     *  main program for the XmlUtil class
     *
     *@param  args  The command line arguments
     *@roseuid      3C7EE02502A3
     */
    public static void main(String[] args) {
        LinkedHashMap map = new LinkedHashMap();
        map.put("A", "111");
        map.put("BBB", "w11");
        map.put("CCC", "CA");
        LinkedHashMap map2 = new LinkedHashMap();
        map2.put("2A", "111");
        map2.put("2BBB", "w11");
        map2.put("2CCC", "CA");
        map.put("LJ", map2);
        String s = Map2XML("ZZS", map);
        System.err.println("s=" + s);

        Node node;
        NodeList nodelist;
        String RootPath = "";
        XmlUtil xmlUtil1 = new XmlUtil();
        String xpath = "//VARS/VAR[@itemid=&quot;JMSE&quot;]";
//         String xpath="max(//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-0,//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]-0)";
        String xpath1 = "//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-0";
        String xpath2 = "//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-0";
//      String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=&quot;BQYNSE_HJ_BY&quot;]-//TABLE/ROW[position()=13]/ITEM[@itemid=&quot;QZBQYJSE_BY&quot;]";
//       String xpath="//TABLE/ROW[position()=9]/ITEM[@itemid=\"BQYNSE_HJ_BY\"]-1";
//        Document doc1=xmlUtil1.getDOMDocument("E:\\Projects\\vss\\stic\\projectsrc\\src\\zxt\\stic\\config\\refine\\refine_ZZS_YBR2.xml");
        Document doc1 = xmlUtil1.getDOMDocument("d:/YQL.xml");
//        XPathResult xr = xmlUtil1.evaluate(xpath, doc1);

        System.exit(0);
        Document doc = xmlUtil1.getDOMDocument(RootPath);
        /*
         *  取节点
         */
        node = xmlUtil1.selectNode(doc, "//D");
//        System.out.println("get node test:" + xmlUtil1.getNodeTextValue(node));
        node = xmlUtil1.selectNode(doc, "//B/C");
//        System.out.println("get node test:" + xmlUtil1.getNodeTextValue(node));
        /*
         *  取节点属性
         */
        node = xmlUtil1.selectNode(doc, "//D");
        Element el = (Element) node;
//        System.out.println("get node attribute:" + el.getAttribute("test6"));
        /*
         *  取同名所有节点
         */
        nodelist = xmlUtil1.selectNodes(doc, "//A");
        for (int i = 0; i < nodelist.getLength(); i++) {
            node = nodelist.item(i);
//            System.out.println("get all node :" + xmlUtil1.getNodeTextValue(node));
        }
        /*
         *  根据属性取节点：
         */
        node = xmlUtil1.selectNode(doc, "//A[@test1='1']");
//        System.out.println("get node by one attribute :" + xmlUtil1.getNodeTextValue(node));
        /*
         *  根据多个属性取节点：
         */
        node = xmlUtil1.selectNode(doc, "//A[@test1='1' and @test11='1']");
//        System.out.println("get node by more attribute:" + xmlUtil1.getNodeTextValue(node));
        //设节点值
        node = xmlUtil1.selectNode(doc, "//E");
        xmlUtil1.setNodeTextValue(node, "set value test");
        //根据doc写文件
        xmlUtil1.writeDoc(doc, "c:\\xmltest.xml");
    }


// add by hejiang 20021022
    /**
     *  Description of the Method
     *
     *@param  src  Description of Parameter
     *@return      Description of the Returned Value
     */
    public static String refineString(String src) {
        StringBuffer sbReturn = new StringBuffer();
        String strSource = src;
        String strTemp = "";
        sbReturn.setLength(0);
        for (int i = 0; i < strSource.length(); i++) {
            strTemp = strSource.substring(i, i + 1);
            if (!strTemp.equalsIgnoreCase("\r\n")) {
                sbReturn.append(strTemp);
            }
            //if
        }
        //for
        strTemp = sbReturn.toString();
        sbReturn = null;
        return strTemp;
    }


    /**
     *  选择节点.
     *
     *@param  startNode  起始节点
     *@param  xpathStr   选择节点的XPATH表达式
     *@return            目标节点,如果发送错误,返回null
     *@roseuid           3C8760C903C6
     */
    public static Node selectNode(Node startNode, String xpathStr) {
        NodeList nList = selectNodes(startNode, xpathStr);
        if (nList == null) {
            return null;
        }
        if (nList.getLength() != 1) {
            return null;
        }
        return nList.item(0);
    }


    /**
     *  选择节点集合.
     *
     *@param  startNode  起始节点
     *@param  xpathStr   选择节点的XPATH表达式
     *@return            目标节点集合,如果发送错误,返回null
     *@roseuid           3C7EE0250249
     */
    public static NodeList selectNodes(Node startNode, String xpathStr) {
        try {
            return XPathAPI.selectNodeList(startNode, xpathStr);
        } catch (javax.xml.transform.TransformerException tex) {
            //Logger.getLogger().log("XmlUtil:selectNodes() error:\n"+tex.toString());
            //Logger.getLogger().log(tex);
            return null;
        }
    }


// end by add

    /**
     *  写DOCUMENT
     *
     *@param  dc        目标文档
     *@param  fileName  Description of Parameter
     *@since            2003-0416
     */
    public static void writeDoc(Document dc, String fileName) {
        try {
            Writer wr = new Writer();
            FileOutputStream fos = new FileOutputStream(fileName, false);
            wr.setOutput(fos, "GB2312");
            wr.setIncludeIgnorableWhitespace(false);
            wr.write(dc);
            fos.close();
            wr.closeOutput();
        } catch (Exception e) {
           // LogUtil.getLogger().debug(e.toString());
        }

    }
    static {
        try {
            parser = (ParserWrapper) Class.forName("com.framework.util.xml.Xerces").newInstance();
            parser.setIncludeIgnorableWhitespace(true);

        } catch (Exception ex) {
            //Logger.getLogger().log("XmlUtil:XmlUtil() error:\n"+ex.toString());
            //Logger.getLogger().log(ex);
            ex.printStackTrace();
            parser = null;
        }
    }


    //add by hejiang 20030915

    //end by add

}
