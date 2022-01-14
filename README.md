# Email verification use-case with SPA + API (Auth0)

This sample app demonstrates some of the Auth0 related use-cases for SPA + API application. Client application is developed using Angular CLI 8 and integrated with [Auth0 Angular SDK](https://github.com/auth0/auth0-angular)  for authentication. Backend application is developed with Java 11 and integerated with Spring Security. 

This sample demonstrates the following use cases:

- Login
- Showing the user profile
- Protecting routes using the authentication guard
- Calling APIs with automatically-attached bearer tokens
- Users are enforced to verify emails if not done earlier. 

### Scenarios which needs to consider : 
 
 1. User are already logged in and on application when we switched a toggle to verify email from backend. There should not be any impact on end-user during switching toggle.
 2. User is inactive and will have to relogin inorder to access application. In this scenario, user can verify email-address in the login process itself.
3. User who have incorrect email setup and want's to change the email-address before verifying email-address.

-------------------------------

___Different approach to solve above mentioned scnearios___ : 

## **Approach 1** : 

**Step 1** : Add rule in Auth0 which will add custom claim in access-token.
```
function (user, context, callback) {
  const namespace = 'http://example.com/api/';
  context.accessToken[namespace + 'email_verified'] = user.email_verified;
  callback(null, user, context);
}
```

**Step 2** : Add a Custom post-authentication filter on server which will return forbidden status with one of current email-verification response header. 

```
@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            .
            . 
            . 
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			boolean isEmailVerified = (boolean) ((Jwt) ((JwtAuthenticationToken) authentication).getCredentials()).getClaims().get(Constants.claim_email_verified);

			if(!isEmailVerified){
				httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
				httpServletResponse.setHeader(Constants.x_email_verified_header, String.valueOf(isEmailVerified);
				return;
			}
		}
            . 
            .

		filterChain.doFilter(servletRequest, servletResponse);
	}

```

**Step 3** : Add logic in client which will show a popup/link to verify email-address if the current email-id is valid. 
  Logic around how we can update email_verified flag in listed below : 
  Solution 1 : Client will call backend API which internally willl call Auth0 update-users API to update `email_verified` flag to true. 
  Solution 2 : Backend API can call Auth0's email-verification token API to get email-verification token link which we can open in new tab/popup once user's click 'Submit'. 
  Solution 3 : We can directly send verification link to user's email-address with Auth0 API and only after post email-verification, user will be able to access application. 

[Edge case scneario where user wants to update email-address before verification] : In this case, add one more link/button on client UI. Once user's clicks on this link/button, with the help of Auth0 API we can modify user's email and it will send out verification link over email. Since this affects the current Auth0's user session, user will have to re-login in order to access application. (Currently Auth0 does not support multiple users with same email-address, so user will have to enter unique email-address during update) 

**Step 4** : Once user click on email-verification and `email_verified` flag gets updated in Auth0, we have to revoke current access token and will have to generate new access token with the available refresh-token. In case if refresh token is expired or if not present, application will redirect user to login to continue. 
