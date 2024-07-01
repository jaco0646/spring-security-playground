package org.playground.security.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.playground.security.authentication.UserNamePasswordAuthConverter;
import org.playground.security.authentication.UserNamePasswordAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

import static org.playground.security.controller.SecureController.BASIC;
import static org.playground.security.controller.SecureController.HEADER;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        RequestMatcher headerMatcher = new MvcRequestMatcher(introspector, HEADER);
        Filter filter = authenticationFilter(headerMatcher);
        http
                .addFilterBefore(filter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests().requestMatchers(headerMatcher).authenticated()
                .and()
                .authorizeHttpRequests().requestMatchers(BASIC).authenticated().and().httpBasic()
                .and()
                .authorizeHttpRequests().requestMatchers("/**").permitAll();

        /*  These settings are redundant with default configuration (which denies all cross-origin requests).
               * http.cors().disable();
               * http.cors(AbstractHttpConfigurer::disable);
         */

        http.cors(corsConfigurer -> corsConfigurer.configurationSource(
                httpServletRequest -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of());  // List allowed origins here, e.g. "http://localhost:8080"
                    return config;
                }
        ));

        return http.build();
    }

    private AuthenticationFilter authenticationFilter(RequestMatcher requestMatcher) {
        AuthenticationProvider provider = new UserNamePasswordAuthProvider(userDetailsService(), passwordEncoder());
        AuthenticationManager manager = new ProviderManager(provider);
        AuthenticationFilter filter = new AuthenticationFilter(manager, new UserNamePasswordAuthConverter());
        filter.setRequestMatcher(requestMatcher);
        filter.setSuccessHandler((request, response, authentication) -> {});  // No redirect.
        return filter;
    }

    private UserDetailsService userDetailsService() {
        return userName -> {
            if (userName == null || userName.isBlank() || userName.equals("null")) {
                throw new UsernameNotFoundException("User name is required.");
            }
            return new User(userName, "password", List.of(() -> "read", () -> "write"));
        };
    }

    private PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
