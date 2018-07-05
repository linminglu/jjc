package com.framework.util;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <p>Title: </p>
 * <p>Description: 这个类中一个关键的方法是递归：initProperties</p>
 *  To retrieve the value of an attribute of an element, use <code>X.Y.Z[@attribute]</code>.
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: bpc</p>
 * @author lgj
 * @version 1.0
 */

public class DOM4JConfiguration
    extends AbstractConfiguration {
  // 标志attribute
  private static final char ATTRIB_MARKER = '@';
  /**
   * The XML document from our data source.
   */
  private Document document;

  public DOM4JConfiguration(String xmlPath) throws Exception {
    load(xmlPath);
  }

  /**
   * Attempt to load the all document from path
   * @param path
   * @throws java.lang.Exception
   */
  public void load(String path) throws Exception {
    document = new SAXReader().read(path);
    Element element=document.getRootElement();
    initProperties(element, new StringBuffer());

  }

  /**
   * 一个递归方法，把所有的xml文件加到hashmap中，如果有多级就用.
   * @param element The element to start processing from.  Callers
   * should supply the root element of the document.
   * @param hierarchy
   */
  private void initProperties(Element element, StringBuffer hierarchy) {
    for (Iterator it = element.elementIterator(); it.hasNext(); ) {
      StringBuffer subhierarchy = new StringBuffer(hierarchy.toString());
      Element child = (Element) it.next();
      String nodeName = child.getName();
      String nodeValue = child.getTextTrim();
      subhierarchy.append(nodeName);
      String str = subhierarchy.toString();

      if (nodeValue.length() > 0) {
        super.addProperty(subhierarchy.toString(), nodeValue); //在这里，我就可以把它放到hashmap里面呢

      }

      // Add attributes as x.y{ATTRIB_START_MARKER}att{ATTRIB_END_MARKER}
      List attributes = child.attributes(); //list all attribute
      for (int j = 0, k = attributes.size(); j < k; j++) {
        Attribute a = (Attribute) attributes.get(j);
        /*注意下面这句是我了我们项目需要，根据属性中的id来判断特殊性,
         在用getString()方法，应该加上[id="attribute.name"]只对id有效
         */
        if(a.getName().equals("id")){
          subhierarchy.append('[' + a.getName() + '=' + a.getValue() + ']');
        }
        String attName = subhierarchy.toString() + '[' + ATTRIB_MARKER +
            a.getName() + ']';

        String attValue = a.getValue();
        super.addProperty(attName, attValue);
      }
      
      //subhierarchy.append(nodeName);
      StringBuffer buf = new StringBuffer(subhierarchy.toString());
      initProperties(child, buf.append('.'));
    }
  }

}