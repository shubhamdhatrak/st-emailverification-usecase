package com.st.emailverificationusecase.security;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.st.emailverificationusecase.utils.Constants;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/*
	Post authentication filter which checks if user has verified his flag.
 */
public class EmailVerificationFilter  implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

		if(!httpServletRequest.getRequestURI().contains("/updateEmailVerification")){
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			boolean isEmailVerified = (boolean) ((Jwt) ((JwtAuthenticationToken) authentication).getCredentials()).getClaims().get(Constants.claim_email_verified);

			if(!isEmailVerified){
				httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
				httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
				httpServletResponse.setHeader(Constants.x_email_verified_header, String.valueOf(isEmailVerified)); // here isEmailVerified will always evaluate to false.
				httpServletResponse.setHeader("Access-Control-Allow-Headers", Constants.x_email_verified_header);
				httpServletResponse.setHeader("Access-Control-Expose-Headers", Constants.x_email_verified_header);
				return;
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
