package com.framework.web.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

import com.framework.Constants;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.SslUtil;



/**
 * <p>基于struts Action 进行的拓展Action基类</p>
 *
 * <p>举个例子，可以用一个Action通过parameter配置的方式实现多种操作</p>

 * <p>在具体Struts配置的时候可以采用如下方式, &lt;a href="emailAction.do?method=findEmail"/&gt;.</p>
 * 
 * <p>
 * 根据method的参数与具体Action中的操作方法相匹配实现一个Action供多个完整业务页面调用
 * </p>
 * <p>
 * 如果设置模式为deu
 * </p>
 */
public abstract class BaseLookupDispatchAction extends LookupDispatchAction {
    protected final Log log = LogFactory.getLog(getClass());
    public static final String SECURE = "secure";
    private static IServiceLocator serviceLocator ;
    /**
     * 实际操作中也可以不调用此方法,直接调用facadeService方法进行操作
     */
    public Object getService(String name) {
    	serviceLocator = ServiceLocatorImpl.getInstance();
        return serviceLocator.getService(name);
    }
    

    /**
     * Convenience method to initialize messages in a subclass.
     *
     * @param request the current request
     * @return the populated (or empty) messages
     */
    public ActionMessages getMessages(HttpServletRequest request) {
        ActionMessages messages = null;
        HttpSession session = request.getSession();

        if (request.getAttribute(Globals.MESSAGE_KEY) != null) {
            messages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
            saveMessages(request, messages);
        } else if (session.getAttribute(Globals.MESSAGE_KEY) != null) {
            messages = (ActionMessages) session.getAttribute(Globals.MESSAGE_KEY);
            saveMessages(request, messages);
            session.removeAttribute(Globals.MESSAGE_KEY);
        } else {
            messages = new ActionMessages();
        }

        return messages;
    }

    /**
     * Gets the method name based on the mapping passed to it 
     */
    private String getActionMethodWithMapping(HttpServletRequest request,
                                              ActionMapping mapping) {
        return getActionMethod(request, mapping.getParameter());
    }

    /** 
     * Gets the method name based on the prepender passed to it.
     */
    protected String getActionMethod(HttpServletRequest request, String prepend) {
        String name = null;
        
        // for backwards compatibility, try with no prepend first
        name = request.getParameter(prepend);
        if (name != null) {
            // trim any whitespace around - this might happen on buttons
            name = name.trim();
            // lowercase first letter
            return name.replace(name.charAt(0), Character.toLowerCase(name.charAt(0)));
        }
        
        Enumeration e = request.getParameterNames();

        while (e.hasMoreElements()) {
            String currentName = (String) e.nextElement();

            if (currentName.startsWith(prepend + ".")) {
                if (log.isDebugEnabled()) {
                    log.debug("calling method: " + currentName);
                }

                String[] parameterMethodNameAndArgs = StringUtils.split(currentName, ".");
                name = parameterMethodNameAndArgs[1];
                break;
            }
        }
        
        return name;
    }

    /** 
     * Override the execute method in DispatchAction to parse
     * URLs and forward to methods without parameters.</p>
     * <p>
     * This is based on the following system:
     * <p/>
     * <ul>
     * <li>edit*.html -> edit method</li>
     * <li>save*.html -> save method</li>
     * <li>view*.html -> search method</li>
     * </ul>
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {
        
        if (isCancelled(request)) {
            try {
                getMethod("cancel");
                return dispatchMethod(mapping, form, request, response, "cancel");
            } catch (NoSuchMethodException n) {
                log.warn("No 'cancel' method found, returning null");
                return cancelled(mapping, form, request, response);
            }
        }

        // Check to see if methodName indicated by request parameter
        String actionMethod = getActionMethodWithMapping(request, mapping);
        
        if (actionMethod != null) {
            return dispatchMethod(mapping, form, request, response, actionMethod);
        } else {
            String[] rules = {"edit", "save", "search", "view"};
            for (int i = 0; i < rules.length; i++) {
                // apply the rules for automatically appending the method name
                if (request.getServletPath().indexOf(rules[i]) > -1) {
                    return dispatchMethod(mapping, form, request, response, rules[i]);
                }
            }
        }
        
        return super.execute(mapping, form, request, response);
    }

    /**
     * Convenience method for getting an action form base on it's mapped scope.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     * @return ActionForm the form from the specifies scope, or null if nothing
     *         found
     */
    protected ActionForm getActionForm(ActionMapping mapping,
                                       HttpServletRequest request) {
        ActionForm actionForm = null;

        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                actionForm = (ActionForm) request.getAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();
                actionForm = (ActionForm) session.getAttribute(mapping.getAttribute());
            }
        }

        return actionForm;
    }


    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     *
     * @return the user's populated form from the session
     */
    public Map getConfiguration() {
        Map config = (HashMap) getServlet().getServletContext().getAttribute(Constants.CONFIG);

        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }

        return config;
    }

    /**
     * Method to check and see if https is required for this resource
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return boolean true if redirection to SSL is needed
     */
    protected boolean checkSsl(ActionMapping mapping,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        String redirectString =
            SslUtil.getRedirectString(request, getServlet().getServletContext(),
                                      SECURE.equals(mapping.getParameter()));

        if (redirectString != null) {
            log.debug("protocol switch needed, redirecting...");

            try {
                // Redirect the page to the desired URL
                response.sendRedirect(response.encodeRedirectURL(redirectString));

                return true;
            } catch (Exception ioe) {
                log.error("redirect to new protocol failed...");
            }
        }

        return false;
    }

    /**
     * Convenience method for removing the obsolete form bean.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     */
    protected void removeFormBean(ActionMapping mapping,
                                  HttpServletRequest request) {
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.removeAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();
                session.removeAttribute(mapping.getAttribute());
            }
        }
    }

    /**
     * Convenience method to update a formBean in it's scope
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     * @param form    The ActionForm
     */
    protected void updateFormBean(ActionMapping mapping,
                                  HttpServletRequest request, ActionForm form) {
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute(mapping.getAttribute(), form);
            }
        }
    }
}
