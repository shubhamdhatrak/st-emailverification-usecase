package com.st.emailverificationusecase.utils;

public interface Constants {

	String x_email_verified_header = "x-user-email-verified";

	String Scope_create_users = "create:users";
	String Scope_read_users = "read:users";
	String Scope_update_users = "update:users";

	String baseUrl = "https://idmsandbox.auth0.com/";
	String token_endpoint = baseUrl + "oauth/token";
	String management_audience = baseUrl + "api/v2/";
	String management_clientId = "m6bjnTGurO3nHyzZK7CgG8tsH3lJpt7S";
	String management_clientSecret = "qy-QYgIr3v1RUw1oJEmDPBVEjz9BwQvBO-AuMaKKrwWkGmwWlKarZeWmdtt5vY32";

	String client_credential = "client_credentials";

	String updateUserEndpoint = baseUrl + "api/v2/users/";

	String claim_email_verified = "http://supertokens/api/email_verified";
}
