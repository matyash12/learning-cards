package com.example.backend.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Autowired
        private MainFilter mainFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors()
                                .and()
                                .csrf()
                                .disable()
                                .addFilterBefore(mainFilter, UsernamePasswordAuthenticationFilter.class)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/user/register").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/user/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/recovery/**").permitAll()
                                                .anyRequest().authenticated());
                // .httpBasic(Customizer.withDefaults());
                // http.cors();
                return http.build();
        }

        @Bean
        UserDetailsService emptyDetailsService() {
                return username -> {
                        throw new UsernameNotFoundException("no local users, only tokens allowed");
                };
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
                configuration.setAllowedOrigins(Collections.singletonList("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "OPTIONS"));

                configuration.addAllowedMethod(HttpMethod.TRACE);

                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}