package com.example.demo.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfigurator extends WebSecurityConfigurerAdapter {

	@Autowired
	UserRepositoryAuthenticationProvider authenticationProvider;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Database authentication provider
		//auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");
		auth.authenticationProvider(authenticationProvider); //ESTE NO FUNCIONA, NO ENTRA
	 }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests().antMatchers("/login").permitAll();
		http.authorizeRequests().antMatchers("/home").permitAll();
		//http.authorizeRequests().antMatchers("/searchPlatform").permitAll();
		
		http.authorizeRequests().anyRequest().authenticated();
		
		http.authenticationProvider(authenticationProvider);
		http.formLogin().loginPage("/login");
		http.formLogin().usernameParameter("userName");
		http.formLogin().passwordParameter("userPassword");
		http.formLogin().failureUrl("/home");
		http.formLogin().defaultSuccessUrl("/search");
		http.logout().logoutUrl("/logout");
		http.logout().logoutSuccessUrl("/");
		
		http.csrf().disable();
	}
	
	
	
	/*@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}*/
	

}
