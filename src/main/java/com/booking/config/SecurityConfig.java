package com.booking.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/h2-console/**")
					.permitAll()
					.anyRequest()
					.authenticated())
				.httpBasic(withDefaults());

		http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

		return http.build();
	}

	@Bean
	public UserDetailsService users() {

		UserDetails gajendra = User.withUsername("gajendra")
				.password("{noop}gajendra")
				.roles("USER")
				.build();
		
		UserDetails ram = User.withUsername("ram")
				.password("{noop}ram")
				.roles("USER")
				.build();

		UserDetails admin = User.withUsername("admin")
				.password("{noop}pass")
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(gajendra,ram, admin);
	}
}