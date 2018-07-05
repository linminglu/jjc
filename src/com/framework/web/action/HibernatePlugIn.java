/*
 * Created on 2006-4-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.framework.web.action;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * @author lgj createTime 2006-4-6 commpany bpc project newPlat
 */
public class HibernatePlugIn implements PlugIn {
//    private String configFile;
//    protected final Log log = LogFactory.getLog(getClass());
    
//    public static final int SECOND  = 1000;
//    public static final int MINUTE  = SECOND*60;
//    private static long waitTime=1000*60*30;//10 分钟
//    public static final int WEEK  =7*24*60*60*1000; 
//    
//    private static Log log = LogFactory.getLog(HibernatePlugIn.class);

    // This method will be called at application shutdown time
    public void destroy() {
//        log.info("Entering HibernatePlugIn.destroy()");
        //Put hibernate cleanup code here
//        log.info("Exiting HibernatePlugIn.destroy()");
    }

    //This method will be called at application startup time
    public void init(ActionServlet actionServlet, ModuleConfig config)
            throws ServletException {
//        log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//        log.info("Entering HibernatePlugIn.init()");
//        log.info("value is:"+getConfigFile());
//        TestHandler th=new TestHandler();
//        th.sendMail();
        //Test.test();
//        TestTask.getInstance().schedule(MINUTE*3,waitTime);//定时去发送邮件,10分钟去检索一下，看是否有要发送的邮件没有
//        schedule();//每周星期5下午17点发送邮件
//        log.info("Exiting HibernatePlugIn.init()");
//        log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    

//    public static void schedule() { 
//       Date date=new Date();
//       int next=TestHandler.getNext(date,TestHandler.SURETIME);
//       String happyTime=DateUtil.getNextSomeDay(date,next)+" 17:00:00";
//      // log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%next and time is:"+next+","+happyTime);
//       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       long waitTime=0;
//       try{       
//           waitTime=sdf.parse(happyTime).getTime()-date.getTime();           
//       }catch(Exception ex){
//          ex.printStackTrace();    
//       }
//  //     log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%waitTime is:"+waitTime);
//       TestWeekTask.getInstance().schedule(waitTime,WEEK);
// //        TestWeekTask.getInstance().schedule(MINUTE,MINUTE);     
//    }
    
//    public String getConfigFile() {
//        return configFile;
//    }
//
//    public void setConfigFile(String string) {
//        configFile = string;
//    }
}