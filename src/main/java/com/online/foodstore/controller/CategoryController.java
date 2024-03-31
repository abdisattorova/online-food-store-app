package com.online.foodstore.controller;

import com.online.foodstore.model.dto.CategoryDTO;
import com.online.foodstore.model.dto.GenericResponse;
import com.online.foodstore.model.dto.PaginationRequest;
import com.online.foodstore.model.entity.ECategoryStatus;
import com.online.foodstore.service.CategoryService;
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
@RequestMapping(value = "/api/category")
@Tag(name = "Category", description = "CRUD APIs for categories")
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get categories list with pagination",
            description = "This API endpoint retrieves a list of categories with pagination.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0", schema = @Schema(type = "integer", defaultValue = "0"), in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Page size", example = "10", schema = @Schema(type = "integer", defaultValue = "10"), in = ParameterIn.QUERY),
                    @Parameter(name = "search", description = "Search query", example = " ", schema = @Schema(type = "string", defaultValue = " "), in = ParameterIn.QUERY)
            }
    )
    public GenericResponse<?> getListPagination(@Parameter(hidden = true) PaginationRequest request,
                                                @Parameter(name = "status", description = "Category status", schema = @Schema(type = "string", allowableValues = {"ACTIVE", "INACTIVE", "DELETED"}), in = ParameterIn.QUERY)
                                                @RequestParam(required = false) ECategoryStatus status) {

        var page = service.getAll(request, status);
        return GenericResponse.ok(page.getData(), page.getPaginationData());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get category by ID",
            description = "This API endpoint retrieves a category by their ID."
    )
    @Cacheable(value = "category", key = "#id")
    public GenericResponse<?> getById(@Parameter(name = "id", description = "The ID of the category to get", required = true, example = "1", in = ParameterIn.PATH) @PathVariable Long id) {
        var data = service.get(id);
        return GenericResponse.ok(data);
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create a new category",
            description = "This API endpoint creates a new category.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class),
                            examples = @ExampleObject(value = "{\"name\":\"Meat\",\"status\":\"ACTIVE\"}")
                    )
            )
    )
    public GenericResponse<CategoryDTO> create(@RequestBody @Valid CategoryDTO dto) {
        return GenericResponse.ok(service.create(dto));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Edit an existing category",
            description = "This API endpoint edits an existing category.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Category details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Meat\",\"status\":\"ACTIVE\"}")
                    )
            )
    )
    @CachePut(cacheNames = "category", key = "#dto.id")
    public GenericResponse<CategoryDTO> edit(@RequestBody @Valid CategoryDTO dto) {
        return GenericResponse.ok(service.edit(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Delete a category",
            description = "This API endpoint deletes an category user."
    )
    public GenericResponse<?> delete(@Parameter(name = "id", description = "The ID of the category to delete", required = true, example = "1", in = ParameterIn.PATH)
                                     @PathVariable Long id) {
        service.delete(id);
        return GenericResponse.ok();
    }
}
