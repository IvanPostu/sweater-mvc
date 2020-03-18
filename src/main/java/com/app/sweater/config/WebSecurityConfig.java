package com.app.sweater.config;

import com.app.sweater.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserService userService;

  private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
          .authorizeRequests()
          .antMatchers("/",
              "/registration",
              "/static/**",
              "/activate/**",
              "/img/**").permitAll()
          .anyRequest().authenticated()
        .and()
          .formLogin()
          .loginPage("/login")
          .permitAll()
        .and()
          .logout()
          .permitAll();
  }


  public AuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider impl = new DaoAuthenticationProvider();
    impl.setUserDetailsService(userService);
    impl.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
    return impl ;
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }
}