package com.twins.clone.common.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, JwtTokenFilter jwtTokenFilter) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(a -> a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//               addFilterBefore(필터, 기준필터.class)-> usernamepassfilter가 실행되기 직전에 jwttokenfilter를 실행해라
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(a -> a.requestMatchers("/author/save", "/author/login").permitAll().anyRequest().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder pwEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
