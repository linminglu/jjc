package com.framework.systeminfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author zhangkeyi
 *
 * 读取当前的数据库信息，反映当前系统状态
 * 使用initInfo()作为初始化方法
 */
public class DatabaseInfo{

    private static Log log = LogFactory.getLog(DatabaseInfo.class);
    private DataSource dataSource;
    private String databaseProductName;
    private String databaseProductVersion;
    private String databaseUrl;
    private String databaseUsername;
    private String driverName;
    private String driverVersion;

    private String errorMessage;
    
    public void setDataSource(DataSource dataSource){
    	this.dataSource = dataSource;
    }
    
    
    public DatabaseInfo() {
    	
    }

    
    public void initInfo() {
        try{
            Connection connection = dataSource.getConnection();
            DatabaseMetaData dbmd   = connection.getMetaData();
            databaseUrl             = dbmd.getURL();
            databaseUsername        = dbmd.getUserName();
            databaseProductName     = dbmd.getDatabaseProductName();
            databaseProductVersion  = dbmd.getDatabaseProductVersion();
            driverName              = dbmd.getDriverName();
            driverVersion           = dbmd.getDriverVersion();
        }catch(SQLException ex){
        	if(log.isErrorEnabled()){
        		log.error("读取数据库版本信息出错！");	
        	}
        	errorMessage = ex.getMessage();
        }
    }
    
    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
}
