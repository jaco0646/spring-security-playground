package org.playground.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

public class UserNamePasswordAuthConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String userName = request.getHeader(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getHeader(SPRING_SECURITY_FORM_PASSWORD_KEY);
        var token = UsernamePasswordAuthenticationToken.unauthenticated(userName, password);
        token.setDetails(new WebAuthenticationDetails(request));
        return token;
    }
}
