package com.framework.systeminfo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author zhangkeyi
 *
 * 读取当前的java类库信息，反映当前系统状态
 * 使用作为初始化方法
 */
public class LibInfo {

    boolean supportJNDI             = false;
    boolean supportJavaxSql         = false;
    boolean supportJAF              = false;
    boolean supportMail             = false;

    boolean supportBeanUtils        = false;
    boolean supportCommonLogging    = false;
    boolean supportJakartaRegExp    = false;
    boolean supportLucene           = false;

    boolean supportMmMysqlDriver    = false;
    boolean supportComMysqlDriver   = false;

    boolean supportImageProcessing  = false;
    
    boolean supportMSSQLServerDriver  = false;
    boolean supportJSQLSQLServerDriver  = false;
    boolean supportJTDSSQLServerDriver  = false;
    
    boolean supportOracleDriver  = false;

    public LibInfo() {
        try {
            Class.forName("javax.naming.Name");
            supportJNDI = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("javax.sql.DataSource");
            supportJavaxSql = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("javax.activation.DataSource");
            supportJAF = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("javax.mail.Message");
            supportMail = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("org.apache.commons.beanutils.MethodUtils");
            supportBeanUtils = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("org.apache.commons.logging.LogFactory");
            supportCommonLogging = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("org.apache.regexp.RE");
            supportJakartaRegExp = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("org.apache.lucene.index.IndexWriter");
            supportLucene = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            supportMmMysqlDriver = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("com.mysql.jdbc.Driver");
            supportComMysqlDriver = true;
        } catch (ClassNotFoundException ex) {}

        try {
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            supportMSSQLServerDriver = true;
        } catch (ClassNotFoundException ex) {}
        
        try {
            Class.forName("com.jnetdirect.jsql.JSQLDriver");
            supportJSQLSQLServerDriver = true;
        } catch (ClassNotFoundException ex) {}
        
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            supportJTDSSQLServerDriver = true;
        } catch (ClassNotFoundException ex) {}
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            supportOracleDriver = true;
        } catch (ClassNotFoundException ex) {}
        
        try {
            BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawLine(0, 0, 10, 10);
            g.dispose();// free resource

            supportImageProcessing = true;
        } catch (Throwable ex) {}

    }

    public boolean isSupportJAF() {
        return supportJAF;
    }

    public boolean isSupportJavaxSql() {
        return supportJavaxSql;
    }

    public boolean isSupportJNDI() {
        return supportJNDI;
    }

    public boolean isSupportMail() {
        return supportMail;
    }

    public boolean isSupportBeanUtils() {
        return supportBeanUtils;
    }

    public boolean isSupportCommonLogging() {
        return supportCommonLogging;
    }

    public boolean isSupportJakartaRegExp() {
        return supportJakartaRegExp;
    }

    public boolean isSupportLucene() {
        return supportLucene;
    }

    public boolean isSupportMmMysqlDriver() {
        return supportMmMysqlDriver;
    }

    public boolean isSupportComMysqlDriver() {
        return supportComMysqlDriver;
    }

    public boolean isSsupportMSSQLServerDriver() {
        return supportMSSQLServerDriver;
    }

    public boolean isSupportJSQLSQLServerDriver() {
        return supportJSQLSQLServerDriver;
    }
    
    public boolean isSupportJTDSSQLServerDriver() {
        return supportJTDSSQLServerDriver;
    }
    
    public boolean isSupportImageProcessing() {
        return supportImageProcessing;
    }
    
    public boolean isSupportOracleDriver() {
        return supportOracleDriver;
    }

}
