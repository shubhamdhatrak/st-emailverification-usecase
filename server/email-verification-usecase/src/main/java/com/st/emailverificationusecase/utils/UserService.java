package com.st.emailverificationusecase.utils;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class UserService {

	private String accessToken = null;

	public UserService(){
		this.accessToken = TokenHelper.getTokenForManagementApi(Constants.Scope_update_users + " " + Constants.Scope_read_users);
	}

	public boolean verifyEmailOfLoggedInUser(String userId, boolean verifyFlag) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		var authenticatedUserId = ((Jwt) ((JwtAuthenticationToken) authentication).getCredentials()).getSubject();

		if(!userId.equalsIgnoreCase(authenticatedUserId)){
			throw new Exception("Authenticated user and Principal does not match. Please re-login to your application to continue !");
		}
		JSONObject jsonObject = new JSONObject("{\"email_verified\" : " + verifyFlag + "}");

		if(accessToken == null) return false;

		return Auth0ApiHelper.updateUser(accessToken, userId, jsonObject);
	}
}
