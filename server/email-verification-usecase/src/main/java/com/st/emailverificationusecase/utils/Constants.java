package com.st.emailverificationusecase.utils;

public interface Constants {

	String x_email_verified_header = "x-user-email-verified";

	String Scope_create_users = "create:users";
	String Scope_read_users = "read:users";
	String Scope_update_users = "update:users";

	String baseUrl = ""; // Add Auth0 base URL
	String token_endpoint = baseUrl + "oauth/token";
	String management_audience = baseUrl + "api/v2/";
	String management_clientId = ""; // Add Auth0 application clientId
	String management_clientSecret = "";  // Add Auth0 application clientSecret

	String client_credential = "client_credentials";

	String updateUserEndpoint = baseUrl + "api/v2/users/";

	String claim_email_verified = "http://supertokens/api/email_verified";
}
