package com.demo.udema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/assets/**", "/coming_soon/**", "/css/**", "/img/**", "/js/**", "/layerslider/**", "/sass/**", "/video/**", "/registration", "/login").permitAll()
                .antMatchers("/adminPage/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                .antMatchers("/userPage/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                .antMatchers("/addCategory/**").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/addLesson/**").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/addLessonTopic/**").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/addCourse/**").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/editCourse/**").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/reviews/**").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/reportedList/**").hasAnyRole("ADMIN")
//   no authentication is required for /register and /confirm URLs as they are the landing pages for a new user
                .antMatchers("/register").permitAll()
                .antMatchers("/confirm").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/404");
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}