package com.framework.util.xml;



//import utils.*;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 *  ��ӡһ��DOM��
 *
 *@author     vision star
 *@created    2002��1��30��
 */
public class Writer {

    /**
     *  Default canonical output (false).
     */
    protected final static boolean DEFAULT_CANONICAL = false;

    /**
     *  Default namespaces support (true).
     */
    protected final static boolean DEFAULT_NAMESPACES = true;

    /**
     *  default settings Default parser name.
     */
    protected final static String DEFAULT_PARSER_NAME = "txt2xml.wrappers.Xerces";

    /**
     *  Default Schema validation support (true).
     */
    protected final static boolean DEFAULT_SCHEMA_VALIDATION = true;

    /**
     *  Default validation support (false).
     */
    protected final static boolean DEFAULT_VALIDATION = false;

    /**
     *  property ids Lexical handler property id
     *  (http://xml.org/sax/properties/lexical-handler).
     */
    protected final static String LEXICAL_HANDLER_PROPERTY_ID = "http://xml.org/sax/properties/lexical-handler";

    /**
     *  Constants feature ids Namespaces feature id
     *  (http://xml.org/sax/features/namespaces).
     */
    protected final static String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /**
     *  Schema validation feature id (http://apache.org/xml/features/validation/schema).
     */
    protected final static String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /**
     *  Validation feature id (http://xml.org/sax/features/validation).
     */
    protected final static String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /**
     *  ÿ�����������ַ�
     */
    private final String basicIndent = "  ";

    /**
     *  �淶�����:���罫�س��滻Ϊ&#13;��.һ��Ϊfalse
     */
    protected boolean fCanonical;

    /**
     *  Print writer.
     */
    protected PrintWriter fOut;

    /**
     *  �Ƿ���Կո�
     */
    private boolean includeIgnorableWhitespace;

    /**
     *  ����
     */
    private int indent = 0;
    private boolean trimOutput = false;


    /**
     *  <init>() Constructor for the Writer object
     *
     *@param  canonical  Description of Parameter
     *@roseuid           3C7EDF120211
     */
    public Writer(boolean canonical) {
        fCanonical = canonical;
    }


    /**
     *  Constructors Default constructor.
     *
     *@roseuid    3C7EDF120207
     */
    public Writer() { }


    /**
     *  Sets whether output is canonical.
     *
     *@param  canonical  The new Canonical value
     *@roseuid           3C7EDF120225
     */
    public void setCanonical(boolean canonical) {
        fCanonical = canonical;
    }


    /**
     *  Sets the IncludeIgnorableWhitespace attribute of the Writer object
     *
     *@param  includeIgnorableWhitespace  The new IncludeIgnorableWhitespace
     *      value
     *@roseuid                            3C7EDF1400FB
     */
    public void setIncludeIgnorableWhitespace(boolean includeIgnorableWhitespace) {
        this.includeIgnorableWhitespace = includeIgnorableWhitespace;
    }


    /**
     *  ���ô�ӡĿ�ĵ�
     *
     *@param  stream                                 The new Output value
     *@param  encoding                               The new Output value
     *@exception  UnsupportedEncodingException       Description of Exception
     *@throws  java.io.UnsupportedEncodingException
     *@roseuid                                       3C7EDF120239
     */
    public void setOutput(OutputStream stream, String encoding) throws UnsupportedEncodingException {

        if (encoding == null) {
            encoding = "GB2312";
        }

        java.io.Writer writer = new OutputStreamWriter(stream, encoding);
        fOut = new PrintWriter(writer);
    }


    /**
     *  ���ô�ӡ��Ŀ�ĵ�
     *
     *@param  writer  The new Output value
     *@roseuid        3C7F1D080260
     */
    public void setOutput(java.io.Writer writer) {
        fOut = writer instanceof PrintWriter
                ? (PrintWriter) writer : new PrintWriter(writer);

    }


    /**
     *  Sets the TrimOutput attribute of the Writer object
     *
     *@param  trimOutput  The new TrimOutput value
     */
    public void setTrimOutput(boolean trimOutput) {
        this.trimOutput = trimOutput;
    }


    /**
     *  Gets the IncludeIgnorableWhitespace attribute of the Writer object
     *
     *@return     The IncludeIgnorableWhitespace value
     *@roseuid    3C7EDF14011A
     */
    public boolean isIncludeIgnorableWhitespace() {
        return includeIgnorableWhitespace;
    }


    /**
     *  Gets the TrimOutput attribute of the Writer object
     *
     *@return    The TrimOutput value
     */
    public boolean isTrimOutput() {
        return trimOutput;
    }


    /**
     *  TBD
     */
    public void closeOutput() {
        try {
            fOut.close();
        } catch (Exception ex) {

        }

    }


    /**
     *  normalizeAndPrint(char) Main Main program entry point.
     *
     *@param  argv  Description of Parameter@param argv[]
     *@roseuid      3C7EDF140200
     */
    public static void main(String argv[]) {

// is there anything to do?
        if (argv.length == 0) {
            printUsage();
            System.exit(1);
        }

// variables
        Writer writer = null;

        ParserWrapper parser = null;
        boolean namespaces = DEFAULT_NAMESPACES;
        boolean validation = DEFAULT_VALIDATION;
        boolean schemaValidation = DEFAULT_SCHEMA_VALIDATION;
        boolean canonical = DEFAULT_CANONICAL;

// process arguments
        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (arg.startsWith("-")) {
                String option = arg.substring(1);
                if (option.equals("p")) {
// get parser name
                    if (++i == argv.length) {
                        //utils.Logger.getLogger().log("error: Missing argument to -p option.");
                    }
                    String parserName = argv[i];

// create parser
                    try {
                        parser = (ParserWrapper) Class.forName(parserName).newInstance();
                    } catch (Exception e) {
                        parser = null;
                        //utils.Logger.getLogger().log("error: Unable to instantiate parser (" + parserName + ")");
                    }
                    continue;
                }
                if (option.equalsIgnoreCase("n")) {
                    namespaces = option.equals("n");
                    continue;
                }
                if (option.equalsIgnoreCase("v")) {
                    validation = option.equals("v");
                    continue;
                }
                if (option.equalsIgnoreCase("s")) {
                    schemaValidation = option.equals("s");
                    continue;
                }
                if (option.equalsIgnoreCase("c")) {
                    canonical = option.equals("c");
                    continue;
                }
                if (option.equals("h")) {
                    printUsage();
                    continue;
                }
            }

// use default parser?
            if (parser == null) {

// create parser
                try {
                    parser = (ParserWrapper) Class.forName(DEFAULT_PARSER_NAME).newInstance();
                } catch (Exception e) {
                    //utils.Logger.getLogger().log("error: Unable to instantiate parser (" + DEFAULT_PARSER_NAME + ")");
                    continue;
                }
            }

// set parser features
            try {
                parser.setFeature(NAMESPACES_FEATURE_ID, namespaces);
            } catch (SAXException e) {
                //utils.Logger.getLogger().log("warning: Parser does not support feature (" + NAMESPACES_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(VALIDATION_FEATURE_ID, validation);
            } catch (SAXException e) {
                //utils.Logger.getLogger().log("warning: Parser does not support feature (" + VALIDATION_FEATURE_ID + ")");
            }
            try {
                parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, schemaValidation);
            } catch (SAXException e) {
                //utils.Logger.getLogger().log("warning: Parser does not support feature (" + SCHEMA_VALIDATION_FEATURE_ID + ")");
            }
//parser.setFeature(INCLUDE_IGNORABLE_WHITESPACE, false);
            try {
                parser.setIncludeIgnorableWhitespace(false);
            } catch (Exception ex) {
                ex.printStackTrace();
                //Logger.getLogger().log(ex);
            }

// setup writer
            if (writer == null) {
                writer = new Writer();
                try {
                    writer.setOutput(System.out, "GB2312");
                    writer.setIncludeIgnorableWhitespace(true);
                } catch (UnsupportedEncodingException e) {
                    //utils.Logger.getLogger().log("error: Unable to set output. Exiting.");
                    System.exit(1);
                }
            }

// parse file
//        writer.setCanonical(canonical);
            String arg2 = "file:///D:/wjz/stol/txt2Xml/design/dest/mb09.xml";
            try {
                Document document = parser.parse(arg2);
                writer.write(document);
            } catch (SAXParseException e) {
// ignore
            } catch (Exception e) {
                //utils.Logger.getLogger().log("error: Parse error occurred - " + e.getMessage());
                if (e instanceof SAXException) {
                    e = ((SAXException) e).getException();
                }
                e.printStackTrace(System.err);
                //Logger.getLogger().log(e);
            }
        }
    }


    /**
     *  �淶�������ַ�
     *
     *@param  s
     *@roseuid    3C7EDF1401BA
     */
    protected void normalizeAndPrint(String s) {
        if (s == null) {
            return;
        }

        //TBD
        if (isTrimOutput()) {
            s = s.trim();
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            normalizeAndPrint(c);
        }
    }


    /**
     *  �淶�������ַ�
     *
     *@param  c   Description of Parameter
     *@roseuid    3C7EDF1401D8
     */
    protected void normalizeAndPrint(char c) {

//fOut.print(c);
//if(true) return;
        switch (c) {
            case '<':
            {
                fOut.print("&lt;");
                break;
            }
            case '>':
            {
                fOut.print("&gt;");
                break;
            }
            case '&':
            {
                fOut.print("&amp;");
                break;
            }
            case '"':
            {
                fOut.print("&quot;");
                break;
            }
            case '\r':
            case '\n':
            {
                if (fCanonical) {
                    fOut.print("&#");
                    fOut.print(Integer.toString(c));
                    fOut.print(';');
                    break;
                }
// else, default print char
            }
            default:
            {
                fOut.print(c);
            }
        }
    }


    /**
     *  ��ӡ���
     *
     *@roseuid    3C7EDF1401EC
     */
    private void outputIndentation() {
        if (!isIncludeIgnorableWhitespace()) {
            return;
        }
        fOut.print("\n");

        for (int i = 0; i < indent; i++) {
            fOut.print(basicIndent);
        }
    }


    /**
     *  ��ӡʹ�÷���
     *
     *@roseuid    3C7EDF14021E
     */
    private static void printUsage() {

        //utils.Logger.getLogger().log("usage: java dom.Writer (options) uri ...");
        //utils.Logger.getLogger().log("--");

        //utils.Logger.getLogger().log("options:");
        //utils.Logger.getLogger().log("  -p name  Select parser by name.");
        //utils.Logger.getLogger().log("  -n | -N  Turn on/off namespace processing.");
        //utils.Logger.getLogger().log("  -v | -V  Turn on/off validation.");
        //utils.Logger.getLogger().log("  -s | -S  Turn on/off Schema validation support.");
        //utils.Logger.getLogger().log("           NOTE: Not supported by all parsers.");
        //utils.Logger.getLogger().log("  -c | -C  Turn on/off Canonical XML output.");
        //utils.Logger.getLogger().log("           NOTE: This is not W3C canonical output.");
        //utils.Logger.getLogger().log("  -h       This help screen.");
        //utils.Logger.getLogger().log("--");

        //utils.Logger.getLogger().log("defaults:");
        //utils.Logger.getLogger().log("  Parser:     " + DEFAULT_PARSER_NAME);
        System.err.print("  Namespaces: ");
        //utils.Logger.getLogger().log(DEFAULT_NAMESPACES ? "on" : "off");
        System.err.print("  Validation: ");
        //utils.Logger.getLogger().log(DEFAULT_VALIDATION ? "on" : "off");
        System.err.print("  Schema:     ");
        //utils.Logger.getLogger().log(DEFAULT_SCHEMA_VALIDATION ? "on" : "off");
        System.err.print("  Canonical:  ");
        //utils.Logger.getLogger().log(DEFAULT_CANONICAL ? "on" : "off");
    }


    /**
     *  �Խڵ����������
     *
     *@param  attrs  �����б�
     *@return        �����������б�
     *@roseuid       3C7F1D0802CE
     */
    protected Attr[] sortAttributes(NamedNodeMap attrs) {

        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }

        return array;
    }


    /**
     *  �ݹ�ش�ӡһ��ڵ�
     *
     *@param  node  ��Ҫ��ӡ�Ľڵ�
     *@roseuid      3C7EDF140124
     */
    public void write(Node node) {

// is there anything to do?
        if (node == null) {
            return;
        }

        short type = node.getNodeType();
        if (type != Node.DOCUMENT_NODE) {
            outputIndentation();
        }
//      //utils.Logger.getLogger().log(node.getLocalName());
        switch (type) {
            case Node.DOCUMENT_NODE:
            {
                if (!fCanonical) {
                    fOut.print("<?xml version=\"1.0\" encoding=\"GB2312\"?>\n");
                    fOut.flush();
                }

                Document document = (Document) node;
                DocumentType docType = document.getDoctype();
                if (docType != null) {
                    fOut.print("<!DOCTYPE ");

                    fOut.print(docType.getName() + " SYSTEM \"" + docType.getSystemId() + "\" >");

                    fOut.flush();
                }

                NodeList nl = document.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                        write(nl.item(i));
                    }
                }

                write(document.getDocumentElement());
                break;
            }

            case Node.ELEMENT_NODE:
            {
                fOut.print('<');
                fOut.print(node.getNodeName());
                Attr attrs[] = sortAttributes(node.getAttributes());
                for (int i = 0; i < attrs.length; i++) {
                    Attr attr = attrs[i];
                    fOut.print(' ');
                    fOut.print(attr.getNodeName());
                    fOut.print("=\"");
                    normalizeAndPrint(attr.getNodeValue());
                    fOut.print('"');
                }
                fOut.print('>');
//                                fOut.print('\n');
                fOut.flush();
                indent++;
                Node child = node.getFirstChild();
                while (child != null) {
                    write(child);
                    child = child.getNextSibling();
                }
                indent--;
                break;
            }

            case Node.ENTITY_REFERENCE_NODE:
            {
                if (fCanonical) {
                    Node child = node.getFirstChild();
                    while (child != null) {
                        write(child);
                        child = child.getNextSibling();
                    }
                } else {
                    fOut.print('&');
                    fOut.print(node.getNodeName());
                    fOut.print(';');
                    fOut.flush();
                }
                break;
            }

            case Node.CDATA_SECTION_NODE:
            {
                org.w3c.dom.CDATASection cs = (org.w3c.dom.CDATASection) node;

                cs.normalize();
//              //utils.Logger.getLogger().log(cs.getData() + cs.toString() + cs.getNodeType());

                if (fCanonical) {
                    normalizeAndPrint(node.getNodeValue());
                } else {
                    fOut.print("<![CDATA[");
                    fOut.print(node.getNodeValue());
                    fOut.print("]]>");
                }
                fOut.flush();
                break;
            }

            case Node.TEXT_NODE:
            {
                normalizeAndPrint(node.getNodeValue());
//                fOut.print("\n");
                fOut.flush();
                break;
            }

            case Node.PROCESSING_INSTRUCTION_NODE:
            {
                fOut.print("<?");
                fOut.print(node.getNodeName());
                String data = node.getNodeValue();
                if (data != null && data.length() > 0) {
                    fOut.print(' ');
                    fOut.print(data);
                }
                fOut.print("?>");
                fOut.flush();
                break;
            }
        }

        if (type == Node.ELEMENT_NODE) {
            outputIndentation();
            fOut.print("</");
            fOut.print(node.getNodeName());
            fOut.print('>');
//                            fOut.print('\n');
            fOut.flush();
        }
//        fOut.print("\n");
        fOut.flush();
    }

}

