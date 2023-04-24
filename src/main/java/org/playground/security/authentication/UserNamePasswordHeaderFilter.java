package org.playground.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

/**
 * The Filter builds a token from the request, and sets it in the SecurityContext
 *      if the AuthenticationManager authenticates it.
 * <p>
 * This class demonstrates how to wrestle UsernamePasswordAuthenticationFilter or AbstractAuthenticationProcessingFilter
 *      into using custom credentials without redirecting the URL. In Spring Security 5.2+ consider instantiating
 *      {@link AuthenticationFilter} instead.
 */
public class UserNamePasswordHeaderFilter extends UsernamePasswordAuthenticationFilter {

    public UserNamePasswordHeaderFilter(RequestMatcher matcher, AuthenticationManager manager) {
        super(manager);
        setRequiresAuthenticationRequestMatcher(matcher);
        setPostOnly(false);
        setAuthenticationSuccessHandler(successHandler());
    }

    private AuthenticationSuccessHandler successHandler() {
        var successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((request, response, url) -> {});  // No redirect.
        return successHandler;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getHeader(SPRING_SECURITY_FORM_USERNAME_KEY);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return request.getHeader(SPRING_SECURITY_FORM_PASSWORD_KEY);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);  // Continue the chain instead of redirecting.
    }
}
