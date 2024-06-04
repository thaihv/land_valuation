package com.jdvn.valuation.landpublic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.jdvn.valuation.landpublic.exception.AjaxAccessDeniedHandler;
import com.jdvn.valuation.landpublic.exception.AjaxAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

//	@Autowired
//	private AuthenticationConfiguration authenticationConfiguration;
//  @Bean
//  AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
//      AjaxAuthenticationFilter ajaxAuthenticationFilter = new AjaxAuthenticationFilter();
//      ajaxAuthenticationFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
//      ajaxAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
//      return ajaxAuthenticationFilter;
//  } 	
	@Bean
	AccessDeniedHandler ajaxAccessDeniedHandler() {
		return new AjaxAccessDeniedHandler();
	}      	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }		
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        http.csrf(csrf -> csrf.disable())
        		.authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.GET,"/greeting/**").permitAll()
                    .requestMatchers(HttpMethod.POST,"/greeting/**").permitAll()
                    .requestMatchers(HttpMethod.PATCH,"/greeting/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/api-docs/**").permitAll()
                    .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());
                //.addFilterAfter(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling(ex -> ex.authenticationEntryPoint(new AjaxAuthenticationFailureHandler()));
        return http.build();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}