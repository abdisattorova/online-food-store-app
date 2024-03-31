package com.online.foodstore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.foodstore.exception.GeneralApiException;
import com.online.foodstore.model.dto.GenericResponse;
import com.online.foodstore.service.AuthService;
import com.online.foodstore.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter implements OpenEndpoints {
    private final UserService service;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            final var token = authorizationHeader.split(" ")[1].trim();
            if (!JWTProvider.isValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            var username = JWTProvider.getUsername(token);

            var user = service.findByUsername(username);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, authService.getAuthorities(user.getId())));
            filterChain.doFilter(request, response);
        } catch (GeneralApiException exception) {
            exception.printStackTrace();
            returnResponse(exception.getMessage(), response, HttpServletResponse.SC_CONFLICT);
        } catch (Exception exception) {
            exception.printStackTrace();
            returnResponse(exception.getMessage(), response, HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return whiteList().stream().anyMatch(endpoint -> new AntPathMatcher().match(endpoint, request.getServletPath()));
    }

    public void returnResponse(String message, HttpServletResponse response, int status) throws IOException {
        var writer = response.getWriter();
        var customResponse = GenericResponse.error(message);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final var content = objectMapper.writeValueAsString(customResponse);
        writer.println(content);
    }
}
