package org.playground.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;

/**
 * Each Provider handles a single class of Authentication token.
 * <p>
 * If a Provider is a Bean, Spring will add it to its own ProviderManager (which is not a Bean)
 *      which in turn will override any default handling of this Provider's token class.
 */
@RequiredArgsConstructor
public class UserNamePasswordAuthProvider implements AuthenticationProvider {
    private final UserDetailsService userDetails;
    private final PasswordEncoder encoder;

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    /**
     * This method should return an <i>authenticated</i> token or else throw an exception,
     *      because the upstream Manager and Filter may not check the token's status before applying it.
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        String userName = String.valueOf(authentication.getPrincipal());
        UserDetails user = userDetails.loadUserByUsername(userName);
        String encodedPassword = encoder.encode(String.valueOf(authentication.getCredentials()));
        if (user.getPassword().equals(encodedPassword)) {
            return authenticated(user.getUsername(), user.getPassword(), user.getAuthorities());
        }
        throw new BadCredentialsException("Invalid credentials for user: " + authentication.getPrincipal());
    }
}
