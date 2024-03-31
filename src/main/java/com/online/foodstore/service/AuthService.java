package com.online.foodstore.service;

import com.online.foodstore.config.AuthProvider;
import com.online.foodstore.config.JWTProvider;
import com.online.foodstore.exception.GeneralApiException;
import com.online.foodstore.model.dto.UserAuthDTO;
import com.online.foodstore.model.entity.User;
import com.online.foodstore.repository.UserRepository;
import com.online.foodstore.utils.ErrorMessages;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final AuthProvider authenticationProvider;
    private final UserRepository userRepository;

    public AuthService(@Lazy AuthProvider authenticationProvider,
                       UserRepository userRepository) {
        this.authenticationProvider = authenticationProvider;
        this.userRepository = userRepository;
    }

    public Object authenticate(UserAuthDTO dto) {
        try {
            final var authenticate = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            final var principal = (User) authenticate.getPrincipal();
            var authentication = new UsernamePasswordAuthenticationToken(principal, null, getAuthorities(principal.getId()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final var claims = getClaims(principal);
            return JWTProvider.generate(principal.getUsername(), claims);
        } catch (BadCredentialsException exception) {
            throw new GeneralApiException(ErrorMessages.LOGIN_PW_ERROR);
        }
    }

    public Map<String, Object> getClaims(User user) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("user_id", user.getId());
        return claimsMap;
    }

    public List<SimpleGrantedAuthority> getAuthorities(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralApiException(ErrorMessages.notFound(User.class.getSimpleName())));
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}
