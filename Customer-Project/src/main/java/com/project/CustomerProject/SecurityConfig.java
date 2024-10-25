package com.project.CustomerProject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /* .formLogin(login -> login
                         .loginPage("/login")
                         .defaultSuccessUrl("/index"))
                 .logout(logout -> logout
                         .logout()
                         .logoutSuccessUrl("/login"))*/
                .csrf(csrf -> csrf.disable())
                .formLogin().and()
                .logout().and()
                .authorizeRequests(auth -> auth
                        .antMatchers("/", "/webjars/**","/css", "/login/**", "/images/**",
                                "/register", "/save-user", "/error-page").permitAll()
                        .antMatchers("/customer-view").hasRole("USER")
                        .antMatchers( "/book/**", "/customer/**", "/batch/**").hasRole("ADMIN"))

                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
