package com.lhs.documentTextExtraction.config1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);
		firewall.setAllowSemicolon(true);
		return firewall;
	}// End Method

	@Override
	public void configure(WebSecurity web) throws Exception {
		/**
		 * The following paths will be ignored by Spring Security
		 */
		web.ignoring().antMatchers( "/","/api/**","/msDocument/**", "/config/**", "/static/**", "/static/css/**", "/static/images/**",
				"/static/js/**", "/static/sass/**", "/static/stylesheets/**", "/templates/**",
				"/static,/font-awesome/**", "/static/font/**", "/templates/fragments/**", "/templates/pages/**",
				"/error", "/actuator/**",  "/downloadAll/**", "/downloadPage/**", "/downloadAllSeperatePages/**", "/PDFTextExtract/**", "/login/**","/loginAuth/*","/documentUpload/*");
		
		
	}

	/**
	 * This are the request URL mapping with spring security
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.anyRequest().authenticated().and().formLogin()
//				.loginPage("/PDFTextExtract").permitAll();
				.loginPage("/").permitAll();
				
		http.csrf().disable();
		http.headers().disable();
		http.headers().defaultsDisabled().cacheControl();
		http.headers().frameOptions().sameOrigin();
		
		

	}

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
}
