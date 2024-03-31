package com.online.foodstore.config;

import com.online.foodstore.exception.GeneralApiException;
import com.online.foodstore.model.entity.EUserStatus;
import com.online.foodstore.model.entity.User;
import com.online.foodstore.repository.UserRepository;
import com.online.foodstore.service.AuthService;
import com.online.foodstore.utils.ErrorMessages;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    @Lazy
    private final AuthService authService;

    public AuthProvider(UserRepository userRepository, @Lazy AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        final var user = userRepository.findByUsernameAndStatusNot(username, EUserStatus.BLOCKED).orElseThrow(() ->
                new GeneralApiException(ErrorMessages.LOGIN_PW_ERROR));

        if (Objects.equals(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password, authService.getAuthorities(user.getId()));
        }

        throw new BadCredentialsException(ErrorMessages.LOGIN_PW_ERROR);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.getName().equals(UsernamePasswordAuthenticationToken.class.getName());
    }
}
