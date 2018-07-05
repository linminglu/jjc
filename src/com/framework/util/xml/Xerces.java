package com.framework.util.xml;

import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;


/**
 *  封装了APACHE的Xerces解析器
 *
 *@author     lgj
 *@created    2005年07月30日
 */
public class Xerces implements ParserWrapper, ParserWrapper.DocumentInfo, ErrorHandler {

    /**
     *  解析过程中出现的错误信息
     */
    private String errMsg = "";

    /**
     *  是否合法
     */
    private boolean isValid = false;

    /**
     *  Data Parser.
     */
    protected DOMParser parser = new DOMParser();


    /**
     *  Constructors Default constructor.
     *
     *@roseuid    3C7EE04702C0
     */
    public Xerces() {
        parser.setErrorHandler(this);
    }


    /**
     *  设置解析器的属性
     *
     *@param  featureId                               The new Feature value
     *@param  state                                   The new Feature value
     *@exception  SAXNotRecognizedException           Description of Exception
     *@exception  SAXNotSupportedException            Description of Exception
     *@throws  org.xml.sax.SAXNotRecognizedException
     *@throws  org.xml.sax.SAXNotSupportedException
     *@roseuid                                        3C7EE0490196
     */
    public void setFeature(String featureId, boolean state) throws SAXNotRecognizedException, SAXNotSupportedException {
        parser.setFeature(featureId, state);
    }


    /**
     *  解析器解析时是否忽略空格
     *
     *@param  bl                    The new IncludeIgnorableWhitespace value
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EE04702CA
     */
    public void setIncludeIgnorableWhitespace(boolean bl) throws Exception {
//parser.setFeature(INCLUDE_IGNORABLE_WHITESPACE, bl);
        parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", bl);
    }


    /**
     *  返回文档信息
     *
     *@return     The DocumentInfo value
     *@roseuid    3C7EE04E0216
     */
    public ParserWrapper.DocumentInfo getDocumentInfo() {
        return this;
    }


    /**
     *  是否可以忽略空格
     *
     *@param  text  Description of Parameter
     *@return       The IgnorableWhitespace value
     *@roseuid      3C7EE04E0233
     */
    public boolean isIgnorableWhitespace(Text text) {
        return ((TextImpl) text).isIgnorableWhitespace();
    }


    /**
     *  检验一个XML文档
     *
     *@param  xmlFileName  XML文件名
     *@param  errString    错误信息
     *@return              如果合法返回true,否则为false
     *@roseuid             3C7EE05103A0
     */
    public boolean Validate(String xmlFileName, StringBuffer errString) {
        try {
            isValid = true;
            errString.setLength(0);
            org.apache.xerces.jaxp.DocumentBuilderFactoryImpl builderFactory =
                    new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
            builderFactory.setValidating(true);
            javax.xml.parsers.DocumentBuilder db = builderFactory.newDocumentBuilder();
            db.setErrorHandler(this);
            Document doc = db.parse(xmlFileName);
            if (isValid) {
                errString.append("valid");
                return true;
            } else {
                errString.append("no-valid");
                return false;
            }
        } catch (Exception ex) {
            errString.append(ex.getMessage());
            return false;
        }
    }


    /**
     *  warning(SAXParseException) Error.
     *
     *@param  ex                         Description of Parameter
     *@exception  SAXException           Description of Exception
     *@throws  org.xml.sax.SAXException
     *@roseuid                           3C7EE0560109
     */
    public void error(SAXParseException ex) throws SAXException {
        isValid = false;
        printError("Error", ex);
    }


    /**
     *  error(SAXParseException) Fatal error.
     *
     *@param  ex                         Description of Parameter
     *@exception  SAXException           Description of Exception
     *@throws  org.xml.sax.SAXException
     *@roseuid                           3C7EE058035A
     */
    public void fatalError(SAXParseException ex) throws SAXException {
        isValid = false;
        printError("Fatal Error", ex);
        throw ex;
    }


    /**
     *  printError(String,SAXParseException)
     *
     *@param  args  Description of Parameter
     */

    public static void main(String args[]) {
        Xerces xe = new Xerces();
        Document dc;

        try {
//     InputSource is=new InputSource("C:\\stol_new\\stol\\work\\tmp\\tmp.xml");
//     InputSource is=new InputSource(new FileReader("C:\\stol_new\\stol\\work\\tmp\\tmp.xml"));
//     InputSource is=new InputSource(new InputStreamReader(new StringBufferInputStream("<?xml version='1.0' encoding='GB2312'?><hejiang>this is hejiangtest</hejiang>")));
            //   dc=xe.parse(is);
        } catch (Exception e) {
            //utils.Logger.getLogger().log(e.toString());
        }
    }


//  end by add
    /**
     *  新建一个文档
     *
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EE050010A
     */
    public Document newDocument() throws Exception {
        isValid = true;
        org.apache.xerces.jaxp.DocumentBuilderFactoryImpl builderFactory =
                new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
        return builderFactory.newDocumentBuilder().newDocument();
    }


    /**
     *  新建文档
     *
     *@param  rootName              XML文档的根元素名称
     *@param  dtdFileName           XML文档的DTD名称
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EE05103BE
     */
    public Document newDocument(String rootName, String dtdFileName) throws Exception {
        isValid = true;
        org.apache.xerces.jaxp.DocumentBuilderFactoryImpl builderFactory =
                new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
        javax.xml.parsers.DocumentBuilder builder =
                builderFactory.newDocumentBuilder();
        org.w3c.dom.DOMImplementation domImple =
                builder.getDOMImplementation();
        if (dtdFileName != null) {
            org.w3c.dom.DocumentType doctype = domImple.createDocumentType(rootName, null, dtdFileName);
            return domImple.createDocument(null, rootName, doctype);
        } else {
            return domImple.createDocument(null, rootName, null);
        }
    }


    /**
     *  解析文档
     *
     *@param  uri                   文档的URI地址.例如file:///d:/...
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EE04E0248
     */
    public Document parse(String uri) throws Exception {
        isValid = true;
        //change by wjz 031125
        InputSource is = new InputSource(uri);
        String encoding = is.getEncoding();
        if (encoding == null) {
            is.setEncoding("GBK");
        }
        parser.parse(is);
        //parser.parse(uri);
        //end change
        return parser.getDocument();
    }


//  add  by hejiang 20021018

    /**
     *  解析文档
     *
     *@param  strsource             文档的输入流。
     *@return                       Document对象
     *@exception  Exception         Description of Exception
     *@throws  java.lang.Exception
     *@roseuid                      3C7EE04E0248
     */
    public Document parse(InputSource strsource) throws Exception {
        isValid = true;
        parser.parse(strsource);
        return parser.getDocument();
    }


    /**
     *  fatalError(SAXParseException) Protected methods Prints the error
     *  message.
     *
     *@param  type  Description of Parameter
     *@param  ex    Description of Parameter
     *@roseuid      3C7EE05B01CE
     */
    protected void printError(String type, SAXParseException ex) {
        StringBuffer sb = new StringBuffer();

        sb.append("[");
        sb.append(type);
        sb.append("] ");
        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1) {
                systemId = systemId.substring(index + 1);
            }
            sb.append(systemId);
        }
        sb.append(':');
        sb.append(ex.getLineNumber());
        sb.append(':');
        sb.append(ex.getColumnNumber());
        sb.append(": ");
        sb.append(ex.getMessage());
        sb.append("\n");
        this.errMsg = sb.toString();
        //utils.Logger.getLogger().log(errMsg);
        sb = null;
    }


    /**
     *  ErrorHandler methods Warning.
     *
     *@param  ex                         Description of Parameter
     *@exception  SAXException           Description of Exception
     *@throws  org.xml.sax.SAXException
     *@roseuid                           3C7EE0530282
     */
    public void warning(SAXParseException ex) throws SAXException {
        isValid = false;
        printError("Warning", ex);
    }

}
