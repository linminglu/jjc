package com.framework.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author zhangkeyi
 * @version 1.0
 */
public class ChangeStyleAction extends Action {
    /**
     *
     */
    public ChangeStyleAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        String style = (request.getParameter("style") != null)
            ? request.getParameter("style") : "default";
        request.getSession().setAttribute("STYLE", style);

        return mapping.findForward("success");
    }
}
