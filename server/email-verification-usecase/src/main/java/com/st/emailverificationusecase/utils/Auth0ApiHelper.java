package com.st.emailverificationusecase.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jdi.InternalException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

public class Auth0ApiHelper {

	public static boolean updateUser(String accessToken, String userId, JSONObject body) throws UnirestException {
		HttpResponse<String> userEmailApiResponse = null;

		userEmailApiResponse = Unirest.patch(Constants.updateUserEndpoint + userId)
				.header("content-type", "application/json")
				.header("authorization", "Bearer " + accessToken)
				.body(body)
				.asString();

		if(userEmailApiResponse != null && userEmailApiResponse.getStatus() == HttpStatus.OK.value()){
			// ToDo : check if updated user object has email-verification flag updated or not
			return true;
		}else{
			throw new InternalException("Some error occurred while calling internal API : " +
					(userEmailApiResponse != null ? userEmailApiResponse.getStatus() + " \n" +
					", Exception-message : " + userEmailApiResponse.getBody().toString() : null));
		}
	}
}
