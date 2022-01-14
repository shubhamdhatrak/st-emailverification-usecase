# Email verification use-case with SPA + API (Auth0)

This sample app demonstrates some of the Auth0 related use-cases for SPA + API application. Client application is developed using Angular CLI 8 and integrated with [Auth0 Angular SDK](https://github.com/auth0/auth0-angular)  for authentication.

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

**Step 3** : Add logic in client which will show a popup/link to verify email-address if the current email-id is valid and correct without interupting application. 

Add one more link in a scneario when user want's to change email-address.(Once confirmation on this link, user will have to re-login to application). This will only change the email-address and not primary key/primary identifier/username with which is user is trying to login. (Also currently Auth0 does not support multiple user's with same email-address, logic needs to handle same logic.)

**Step 4** : Once user click on email-verification and `email_verified` flag gets updated in Auth0, we have to revoke current access token and will have to generate new access token with the available refresh-token. In case if refresh token is expired or if not present, application will redirect user to login to continue with work. 