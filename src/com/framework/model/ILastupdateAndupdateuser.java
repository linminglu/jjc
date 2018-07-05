/*
 * Created on 2005-7-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.framework.model;

import java.util.Date;

/**
 * @author zhangkeyi
 * 同一数据上传人与上传日期接口，
 * 凡是实现此接口的model都会被DAO验证是否进行了上传人与上传日期的初始化
 */
public interface ILastupdateAndupdateuser {
    public Date getLastupdate();

    public void setLastupdate(Date lastupdate);

    public Long getUpdateuserid();

    public void setUpdateuserid(Long updateuserid);
}
