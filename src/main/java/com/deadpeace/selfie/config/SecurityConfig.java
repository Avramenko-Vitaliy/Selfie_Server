package com.deadpeace.selfie.config;

import com.deadpeace.selfie.client.SelfieSvcApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Виталий on 07.10.2015.
 */

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    @Qualifier("userService")
    private UserDetailsService service;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(service);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable().authorizeRequests().antMatchers(SelfieSvcApi.SVC_REGISTER).permitAll().and()
                .requestCache().requestCache(new NullRequestCache()).and()
                .formLogin().loginPage(SelfieSvcApi.SVC_LOGIN).permitAll().and()
                .formLogin().loginProcessingUrl(SelfieSvcApi.SVC_LOGIN).successHandler((request, response, authentication)->response.setStatus(HttpServletResponse.SC_OK)).permitAll().and()
                .logout().logoutUrl(SelfieSvcApi.SVC_LOGOUT).logoutSuccessHandler((request, response, authentication)->response.setStatus(HttpServletResponse.SC_OK)).permitAll().and()
                .authorizeRequests().anyRequest().authenticated();
       /* http.csrf().disable();
        http.requestCache().requestCache(new NullRequestCache());
        http.formLogin().loginPage(SelfieSvcApi.SVC_LOGIN).permitAll();
        http.formLogin().loginProcessingUrl(SelfieSvcApi.SVC_LOGIN).successHandler((request, response, authentication)->response.setStatus(HttpServletResponse.SC_OK)).permitAll();
        http.logout().logoutUrl(SelfieSvcApi.SVC_LOGOUT).logoutSuccessHandler((request, response, authentication)->response.setStatus(HttpServletResponse.SC_OK)).permitAll();
        http.authorizeRequests().anyRequest().authenticated();*/
    }
}
