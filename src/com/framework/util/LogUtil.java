package com.framework.util;

import org.apache.log4j.Category;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
public class LogUtil {

    private static String configName = null;
    private static org.apache.log4j.Logger log;


    /**
     *  Constructor for the LogUtil object
     */
    private LogUtil() {
        log = org.apache.log4j.Logger.getRootLogger();
    }


    /**
     *  Sets the ConfigFile attribute of the LogUtil class
     *
     *@param  filename  The new ConfigFile value
     */
    public static void setConfigFile(String filename) {
        configName = new String(filename);
    }
//  public static void init(){
//
//
//  }
    /**
     * str 保存日志的地方,保存到文件
     *
     * @param str
     */

    public static void fileAppender(String str){
         String loggerName = "bpcdefault";
          org.apache.log4j.DailyRollingFileAppender fileAppender = new DailyRollingFileAppender();//每天就路一个日志
          fileAppender.setFile(str);//保存日志的地方 str=./bpc.log,可以是相对路径也可以是绝对路径

          fileAppender.setName(loggerName);
          fileAppender.setThreshold(org.apache.log4j.Priority.DEBUG);//这个地方设置级别，如果是在写程序中就用这个，如果上线就用error
          fileAppender.setDatePattern("'.'yyyy-MM-dd-a");//时间格式
          fileAppender.setLayout(new PatternLayout("%-14d{MM-dd  HH:mm:ss}    %-10l - %m%n"));//输出的类和时间格式
          fileAppender.activateOptions();
          Category.getInstance(loggerName).addAppender(fileAppender);
          log.addAppender(fileAppender);

    }
    /**
     * for console output
     * 输出到控制台
     *
     */
    public static void consoleApd(){
       String loggerName = "bpcdefault";
        org.apache.log4j.ConsoleAppender consoleApd = new org.apache.log4j.ConsoleAppender();
         loggerName = "console";
         //fileAppender.setName(loggerName);
         consoleApd.setThreshold(org.apache.log4j.Priority.INFO);
         consoleApd.setLayout(new PatternLayout("%-14d{MM-dd  HH:mm:ss}    %-10l - %m%n"));
         consoleApd.activateOptions();
         Category.getInstance(loggerName).addAppender(consoleApd);
         log.addAppender(consoleApd);
   }

    /**
    *  Gets the Logger attribute of the LogUtil class
    * 这个自己写的getLogger是什么都可以记录，
    * 如果有配置文件，那就的把log4j.properties放在src目录下面
    * 自己写的那日志信息的记录是每天记录一次，真正在上线使用的时候，最好用自己写的这个，
    * @return    The Logger value
    * http://www-128.ibm.com/developerworks/cn/java/l-log4j/index.html
    *
    * 优先级从高到低分别是ERROR、WARN、INFO、DEBUG
    */
   public static org.apache.log4j.Logger getLogger() {
       if (log == null) {
           log = org.apache.log4j.Logger.getRootLogger();
           try {
               if (configName != null) {
                   PropertyConfigurator.configure(configName);
                   return log;
               }
           } catch (Exception ex) {
           
           }
           fileAppender("./bpc.log");//这个地方设置日志保存到文件的地方，
           consoleApd();//这个地方设置到控制台，如果有什么疑问，可以看看下面注视调的代码
       }
       return log;
   }



    /**
     *  Gets the debug attribute of the LogUtil class
     *
     *@return    The debug value
     */
    public static boolean isDebug() {
        return log.getLevel().isGreaterOrEqual(Level.DEBUG);
    }


    /**
     *  Description of the Method
     *
     *@param  object  Description of the Parameter
     */
    public void debug(Object object) {
        log.debug(object);
    }


    /**
     *  Description of the Method
     *
     *@param  object     Description of the Parameter
     *@param  throwable  Description of the Parameter
     */
    public void debug(Object object, Throwable throwable) {
        log.debug(object, throwable);
    }


    /**
     *  Description of the Method
     *
     *@param  object  Description of the Parameter
     */
    public void error(Object object) {
        log.error(object);
    }


    /**
     *  Description of the Method
     *
     *@param  object     Description of the Parameter
     *@param  throwable  Description of the Parameter
     */
    public void error(Object object, Throwable throwable) {
        log.error(object, throwable);
    }


    /**
     *  Description of the Method
     *
     *@param  object  Description of the Parameter
     */
    public void fatal(Object object) {
        log.fatal(object);
    }


    /**
     *  Description of the Method
     *
     *@param  object     Description of the Parameter
     *@param  throwable  Description of the Parameter
     */
    public void fatal(Object object, Throwable throwable) {
        log.fatal(object, throwable);
    }


    /**
     *  Description of the Method
     *
     *@param  object  Description of the Parameter
     */
    public void info(Object object) {
        log.info(object);
    }


    /**
     *  Description of the Method
     *
     *@param  object     Description of the Parameter
     *@param  throwable  Description of the Parameter
     */
    public void info(Object object, Throwable throwable) {
        log.info(object, throwable);
    }


    /**
     *  The main program for the LogUtil class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
      LogUtil.getLogger().debug("debug");
      LogUtil.getLogger().info("info");
      LogUtil.getLogger().warn("warn");
      LogUtil.getLogger().error("error");
      printMemoryUsage();

    }


    /**
     *  打印目前内存使用状况 <p>
     *
     *  wujinzhong 0604</p>
     */
    public static void printMemoryUsage() {
        getLogger().debug("************************ total memory:************************" +
                Runtime.getRuntime().totalMemory());
        getLogger().debug("************************ used memory:************************" +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        getLogger().debug("************************availabe memory:************************" +
                Runtime.getRuntime().freeMemory());

    }


    /**
     *  Description of the Method
     *
     *@param  object  Description of the Parameter
     */
    public void warn(Object object) {
        log.warn(object);
    }


    /**
     *  Description of the Method
     *
     *@param  object     Description of the Parameter
     *@param  throwable  Description of the Parameter
     */
    public void warn(Object object, Throwable throwable) {
        log.warn(object, throwable);
    }

}