package com.framework.util.xml;


import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
/**
 *  DOM 解析器的包装器
 *
 *@author     lgj
 *@created    2005年07月30日
 */
public interface ParserWrapper {

    /**
     *  检验一个XML文档
     *
     *@param  xmlFileName  XML文件名
     *@param  errString    错误信息
     *@return              如果合法返回true,否则为false
     *@roseuid             3C7EDED702F2
     */
    public boolean Validate(String xmlFileName, StringBuffer errString);


    /**
     *  解析文档
     *
     *@param  uri                   文档的URI地址.例如file:///d:/...
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EDED70311
     */
    public Document parse(String uri) throws Exception;


    /**
     *  解析文档
     *
     *@param  strsource             文档的输入流
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EDED70311
     */
    public Document parse(InputSource strsource) throws Exception;


    /**
     *  解析器解析时是否忽略空格
     *
     *@param  bl                    The new IncludeIgnorableWhitespace value
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EDED901C9
     */
    public void setIncludeIgnorableWhitespace(boolean bl) throws Exception;


    /**
     *  新建文档
     *
     *@param  rootName              XML文档的根元素名称
     *@param  dtdFileName           XML文档的DTD名称
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EDEDB0095
     */
    public Document newDocument(String rootName, String dtdFileName) throws Exception;


    /**
     *  新建一个文档
     *
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EDEDC0368
     */
    public Document newDocument() throws Exception;


    /**
     *  设置解析器的属性
     *
     *@param  featureId                               The new Feature value
     *@param  state                                   The new Feature value
     *@exception  SAXNotRecognizedException           Description of Exception
     *@exception  SAXNotSupportedException            Description of Exception
     *@throws  org.xml.sax.SAXNotRecognizedException
     *@throws  org.xml.sax.SAXNotSupportedException
     *@roseuid                                        3C7EDEDE0234
     */
    public void setFeature(String featureId, boolean state) throws SAXNotRecognizedException, SAXNotSupportedException;


    /**
     *  Returns the document information.
     *
     *@return     The DocumentInfo value
     *@roseuid    3C7F1CEF0138
     */
    public ParserWrapper.DocumentInfo getDocumentInfo();


    /**
     *  文档信息接口
     *
     *@author     岳潜龙
     *@created    2003年7月4日
     */
    public interface DocumentInfo {

        /**
         *  是否可以忽略空格
         *
         *@param  text
         *@return       boolean
         *@roseuid      3C7F1CEF015F
         */
        public boolean isIgnorableWhitespace(Text text);
    }

}

