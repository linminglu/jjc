package com.framework;


/**
 * 这里存储所有的framework常量，当在通用模块进行时应该将bean名称都放在这里，与springBeanName一致

 * @author Administrator
 *
 */
public class Constants {
    //~ Static fields/initializers =============================================

    /** The name of the ResourceBundle used in this application */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /** The application scoped attribute for persistence engine used */
    public static final String DAO_TYPE = "daoType";
    public static final String DAO_TYPE_HIBERNATE = "hibernate";

    /** Application scoped attribute for authentication url */
    public static final String AUTH_URL = "authURL";

    /** Application scoped attributes for SSL Switching */
    public static final String HTTP_PORT = "httpPort";
    public static final String HTTPS_PORT = "httpsPort";

    /** The application scoped attribute for indicating a secure login */
    public static final String SECURE_LOGIN = "secureLogin";

    /** The encryption algorithm key to be used for passwords */
    public static final String ENC_ALGORITHM = "algorithm";

    /** A flag to indicate if passwords should be encrypted */
    public static final String ENCRYPT_PASSWORD = "encryptPassword";

    /** File separator from System properties */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /** User home from System properties */
    public static final String USER_HOME =
        System.getProperty("user.home") + FILE_SEP;

    /** hibernate config **/
    public static final String SPRINGCONFIG_HOME = "spring";


    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";



    public final static String VALIDATE_STATUS="1";
    public final static String INVALIDATE_STATUS="0";
    public final static int PAGE_SIZE=15;

}
