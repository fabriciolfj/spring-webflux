package com.github.fabriciolfj.springwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;*/
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxApplication.class, args);
	}

	/*@GetMapping("/client")
	public Object getAuthorizedClient(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
		return client;
	}

	@GetMapping("/user")
	public Object getUser(@AuthenticationPrincipal OAuth2User user) {
		return user;
	}*/

}
