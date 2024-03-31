package com.online.foodstore.controller;

import com.online.foodstore.model.dto.CustomPage;
import com.online.foodstore.model.dto.GenericResponse;
import com.online.foodstore.model.dto.PaginationRequest;
import com.online.foodstore.model.dto.UserDTO;
import com.online.foodstore.model.entity.ERole;
import com.online.foodstore.model.entity.EUserStatus;
import com.online.foodstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@Tag(name = "User Management", description = "APIs to manage users in the system")
public class UserController {

    private final UserService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
            summary = "Get users list with pagination",
            description = "This API endpoint retrieves a list of users with pagination. Only users with the 'MANAGER' role are authorized to access this endpoint.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0", schema = @Schema(type = "integer", defaultValue = "0"), in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Page size", example = "10", schema = @Schema(type = "integer", defaultValue = "10"), in = ParameterIn.QUERY),
                    @Parameter(name = "search", description = "Search query", example = " ", schema = @Schema(type = "string", defaultValue = " "), in = ParameterIn.QUERY),
            }
    )
    public GenericResponse<?> getListPagination(@Parameter(hidden = true) PaginationRequest request,
                                                @Parameter(name = "status", description = "User status", schema = @Schema(type = "string", allowableValues = {"ACTIVE", "INACTIVE", "DELETED", "BLOCKED"}), in = ParameterIn.QUERY)
                                                @RequestParam(required = false) EUserStatus status,
                                                @Parameter(name = "role", description = "User role", schema = @Schema(type = "string", allowableValues = {"MANAGER", "EMPLOYEE"}), in = ParameterIn.QUERY)
                                                @RequestParam(required = false) ERole role) {

        CustomPage<UserDTO> page = service.getAll(request, status, role);
        return GenericResponse.ok(page.getData(), page.getPaginationData());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
            summary = "Get user by ID",
            description = "This API endpoint retrieves a user by their ID. Only users with the 'MANAGER' role are authorized to access this endpoint."
    )
    @Cacheable(value = "user", key = "#id")
    public GenericResponse<UserDTO> getById(@Parameter(name = "id", description = "ID of the user to retrieve", in = ParameterIn.PATH, required = true, schema = @Schema(type = "integer"))
                                            @PathVariable Long id) {
        return GenericResponse.ok(service.get(id));
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
            summary = "Create a new user",
            description = "This API endpoint creates a new user. Only users with the 'MANAGER' role are authorized to access this endpoint.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User details for creating a new user",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{\"name\":\"John Doe\",\"status\":\"ACTIVE\",\"role\":\"EMPLOYEE\",\"username\":\"johndoe\",\"password\":\"secret\"}")
                    )
            )
    )
    public GenericResponse<UserDTO> create(@RequestBody @Valid UserDTO dto) {
        return GenericResponse.ok(service.create(dto));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
            summary = "Edit an existing user",
            description = "This API endpoint edits an existing user. Only users with the 'MANAGER' role are authorized to access this endpoint.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User details for editing an existing user",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"John Doe\",\"status\":\"ACTIVE\",\"role\":\"EMPLOYEE\",\"username\":\"johndoe\",\"password\":\"secret\"}")
                    )
            )
    )
    @CachePut(cacheNames = "user", key = "#dto.id")
    public GenericResponse<UserDTO> edit(@RequestBody @Valid UserDTO dto) {
        return GenericResponse.ok(service.edit(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
            summary = "Delete a user",
            description = "This API endpoint deletes an existing user. Only users with the 'MANAGER' role are authorized to access this endpoint."
    )
    public GenericResponse<?> delete(@Parameter(name = "id", description = "The ID of the user to delete", required = true, example = "1", in = ParameterIn.PATH)
                                     @PathVariable Long id) {
        service.delete(id);
        return GenericResponse.ok();
    }


    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
            summary = "Change the status of the user",
            description = "This API endpoint changes the status of an existing user. Only users with the 'MANAGER' role are authorized to access this endpoint."
    )
    public GenericResponse<?> changeStatus(@Parameter(name = "id", description = "The ID of the user to change status", required = true, example = "1", in = ParameterIn.PATH)
                                           @PathVariable Long id,
                                           @Parameter(name = "status", description = "User status", schema = @Schema(type = "string", allowableValues = {"ACTIVE", "INACTIVE", "DELETED", "BLOCKED"}), in = ParameterIn.QUERY)
                                           @RequestParam EUserStatus status) {
        service.changeStatus(id, status);
        return GenericResponse.ok();
    }
}
