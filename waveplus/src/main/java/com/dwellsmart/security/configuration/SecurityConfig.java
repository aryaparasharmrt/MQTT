package com.dwellsmart.security.configuration;

import static com.dwellsmart.constants.Endpoints.BASE;
import static com.dwellsmart.constants.Endpoints.MANAGEMENT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dwellsmart.security.JwtAuthenticationFilter;
import com.dwellsmart.security.UnAuthorizedHandler;

import lombok.RequiredArgsConstructor;

//@EnableWebSecurity
@Configuration
@EnableMethodSecurity // Enables method-level security annotations
@RequiredArgsConstructor
public class SecurityConfig {

//	 private static final String[] PUBLIC_ENDPOINTS = {
//		        "/authenticate",
//		        "/register",
//		        "/forgot-password",
//		        "/api/public/**"
//		    };
//	

	private final UnAuthorizedHandler unauthorizedHandler;

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//		return source;
//	}
	
	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowCredentials(true);
	        config.addAllowedOrigin("http://localhost:3000");  // Allowed domain
	        config.addAllowedHeader("*");
	        config.addAllowedMethod("*");

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);
	        return source;
	    }


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthorizationFilter)
			throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)
//	        		  .accessDeniedHandler(accessDeniedHandler)
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth ->

				auth.requestMatchers("/user/authenticate", "/bam/create", "/actuator/**",BASE+"/**",MANAGEMENT+"/create").permitAll()
//				 .requestMatchers("/api/v1/management/**").hasAnyRole()
						.requestMatchers("/api/test/**").permitAll().anyRequest().authenticated());
		
		http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//	        http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));
		return http.build();
	}
	
	
//	  private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
//	            "/v2/api-docs",
//	            "/v3/api-docs",
//	            "/v3/api-docs/**",
//	            "/swagger-resources",s
//	            "/swagger-resources/**",
//	            "/configuration/ui",
//	            "/configuration/security",
//	            "/swagger-ui/**",
//	            "/webjars/**",
//	            "/swagger-ui.html"};
//	    private final JwtAuthenticationFilter jwtAuthFilter;
//	    private final AuthenticationProvider authenticationProvider;
//	    private final LogoutHandler logoutHandler;
//
//	    @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	                .csrf(AbstractHttpConfigurer::disable)
//	                .authorizeHttpRequests(req ->
//	                        req.requestMatchers(WHITE_LIST_URL)
//	                                .permitAll()
//	                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//	                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//	                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//	                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//	                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
//	                                .anyRequest()
//	                                .authenticated()
//	                )
//	                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
//	                .authenticationProvider(authenticationProvider)
//	                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//	                .logout(logout ->
//	                        logout.logoutUrl("/api/v1/auth/logout")
//	                                .addLogoutHandler(logoutHandler)
//	                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//	                )
//	        ;
//
//	        return http.build();
//	    }

}
