package com.proactivity.rmq.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   

    @Override //Con questa configurazione la validazione del token avviene con AOP su @ReceivesJWT
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/v1/**","/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/*",
                "/webjars/**").permitAll()
                .antMatchers("/testserver").permitAll()
                .anyRequest().authenticated().and().cors().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


	/* Da attivare per utilizzare SecurityRequestFilter (non si raggiunge swagger)
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable()
				.authorizeRequests().antMatchers("/v2/api-docs/**",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui/**",
				"/webjars/**").permitAll()
				.antMatchers("/testserver").permitAll()
				.anyRequest().authenticated().and().cors().and().
				exceptionHandling().authenticationEntryPoint(securityAuthenticationEntryPoint).and().
				sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.addFilterBefore(securityRequestFilter, UsernamePasswordAuthenticationFilter.class);

	} */
}
