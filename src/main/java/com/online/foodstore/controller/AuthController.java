package com.online.foodstore.controller;

import com.online.foodstore.model.dto.GenericResponse;
import com.online.foodstore.model.dto.UserAuthDTO;
import com.online.foodstore.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/login")
    @Operation(
            summary = "Authenticate user",
            description = "This API endpoint allows user to authenticate using their username and password. Upon successful authentication, an access token (JWT) will be returned.",
            tags = {"Auth"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credentials for administrator authentication",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserAuthDTO.class),
                            examples = @ExampleObject(value = "{\"username\":\"admin\",\"password\":\"admin123\"}")
                    )
            )
    )
    public GenericResponse<?> authenticateAdmin(@RequestBody @Valid UserAuthDTO dto) {
        return GenericResponse.ok(authService.authenticate(dto));
    }
}
