package com.ram.web.permission.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class PermissionForm extends ValidatorForm{
	 
	    private Integer permissionId;
        private String permissionTitle;
        private String permissionValue;

        public Integer getPermissionId() {
            return this.permissionId;
        }

        public void setPermissionId(Integer permissionId) {
            this.permissionId = permissionId;
        }
        
	    public String getPermissionTitle() {
	        return this.permissionTitle;
	    }

	    public void setPermissionTitle(String permissionTitle) {
	        this.permissionTitle = permissionTitle;
	    }

	    public String getPermissionValue() {
	        return this.permissionValue;
	    }

	    public void setPermissionValue(String permissionValue) {
	        this.permissionValue = permissionValue;
	    }

	    public void reset(ActionMapping mapping, HttpServletRequest request) {
	    	permissionTitle = null;
	    	permissionValue = null;
			
		}

	   
	
	
}
