package com.ram.model;

import java.io.Serializable;

public class UserRole implements Serializable{

	   private Integer userRoleId;

	    /** persistent field */
	   private Integer userId;

	    /** persistent field */
	    private String authorizationCode ;

		public String getAuthorizationCode() {
			return authorizationCode;
		}

		public void setAuthorizationCode(String authorizationCode) {
			this.authorizationCode = authorizationCode;
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public Integer getUserRoleId() {
			return userRoleId;
		}

		public void setUserRoleId(Integer userRoleId) {
			this.userRoleId = userRoleId;
		}

}
