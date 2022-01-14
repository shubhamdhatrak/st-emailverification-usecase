package com.st.emailverificationusecase.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jdi.InternalException;
import org.apache.http.HttpException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpServerErrorException;

public class TokenHelper {

	public static String getTokenForManagementApi(String scopes){
		return getToken(Constants.management_clientId, Constants.management_clientSecret, Constants.management_audience, scopes);
	}

	public static String getToken(String clientId, String clientSecret, String audience, String scopes){
		HttpResponse<String> tokenApiResponse = null;
		try {
			tokenApiResponse = Unirest.post(Constants.token_endpoint)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.body("grant_type=" + Constants.client_credential +
							"&client_id=" + clientId +
							"&client_secret=" + clientSecret +
 							"&audience=" + audience +
							"&scope=" + scopes)
					.asString();
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}

		if(tokenApiResponse != null && tokenApiResponse.getStatus() == HttpStatus.OK.value()){
			var apiResponse = new JSONObject(tokenApiResponse.getBody());
			var accessToken = apiResponse.get("access_token");
			return accessToken.toString();
		}else {
			throw new InternalException("Some error occurred while calling internal API : " +
					(tokenApiResponse != null ? tokenApiResponse.getStatus() + " \n" +
							", Exception-message : " + tokenApiResponse.getBody().toString() : null));

		}
	}
}
