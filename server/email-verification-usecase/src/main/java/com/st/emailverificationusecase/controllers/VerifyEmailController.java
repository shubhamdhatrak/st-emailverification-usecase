package com.st.emailverificationusecase.controllers;

import com.st.emailverificationusecase.utils.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/identity")
@RestController
public class VerifyEmailController {

	@PostMapping("/updateEmailVerification")
	public boolean updateEmailVerificationFlag(@RequestBody Map<String, Object> payload) throws Exception {
		var userId = payload.get("userId");
		var verified = payload.get("emailVerified");
		UserService userService = new UserService();

		return userService.verifyEmailOfLoggedInUser(userId.toString(), Boolean.parseBoolean(verified.toString()));
	}

}
