package com.online.foodstore.controller;

import com.online.foodstore.model.dto.GenericResponse;
import com.online.foodstore.model.dto.PaginationRequest;
import com.online.foodstore.model.dto.ProductDTO;
import com.online.foodstore.model.entity.EProductStatus;
import com.online.foodstore.service.ProductService;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/product")
@Tag(name = "Product", description = "CRUD APIs for products")
public class ProductController {

    private final ProductService service;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get products list with pagination",
            description = "This API endpoint retrieves a list of products with pagination.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "page", description = "Page number", example = "0", schema = @Schema(type = "integer", defaultValue = "0"), in = ParameterIn.QUERY),
                    @Parameter(name = "size", description = "Page size", example = "10", schema = @Schema(type = "integer", defaultValue = "10"), in = ParameterIn.QUERY),
                    @Parameter(name = "search", description = "Search query", example = " ", schema = @Schema(type = "string", defaultValue = " "), in = ParameterIn.QUERY)
            }
    )
    public GenericResponse<?> getListPagination(@Parameter(hidden = true) PaginationRequest request,
                                                @Parameter(name = "status", description = "Product status", schema = @Schema(type = "string", allowableValues = {"AVAILABLE", "NOT_AVAILABLE", "DELETED"}), in = ParameterIn.QUERY)
                                                @RequestParam(required = false) EProductStatus status,
                                                @Parameter(name = "categoryId", description = "The id of product category", schema = @Schema(type = "integer"), in = ParameterIn.QUERY)
                                                @RequestParam(required = false) Long categoryId) {

        var page = service.getAll(request, status, categoryId);
        return GenericResponse.ok(page.getData(), page.getPaginationData());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get product by ID",
            description = "This API endpoint retrieves a product by their ID."
    )
    public GenericResponse<?> getById(@Parameter(name = "id", description = "The ID of the product to get", required = true, example = "1", in = ParameterIn.PATH)
                                      @PathVariable Long id) {
        var data = service.get(id);
        return GenericResponse.ok(data);
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create a new product",
            description = "This API endpoint creates a new product.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class),
                            examples = @ExampleObject(value = "{\"name\":\"Beef\",\"categoryId\":1,\"status\":\"AVAILABLE\",\"quantity\":10,\"expireDate\":\"2024-12-31\"}")
                    )
            )
    )
    @CacheEvict(cacheNames = "category", key = "#dto.categoryId")
    public GenericResponse<ProductDTO> create(@RequestBody @Valid ProductDTO dto) {
        return GenericResponse.ok(service.create(dto));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Edit an existing product",
            description = "This API endpoint edits an existing product.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Beef\",\"categoryId\":1,\"status\":\"AVAILABLE\",\"quantity\":10,\"expireDate\":\"2024-12-31\"}")
                    )
            )
    )
    public GenericResponse<ProductDTO> edit(@RequestBody @Valid ProductDTO dto) {
        return GenericResponse.ok(service.edit(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Delete a product",
            description = "This API endpoint deletes an product user."
    )
    public GenericResponse<?> delete(@Parameter(name = "id", description = "The ID of the product to delete", required = true, example = "1", in = ParameterIn.PATH)
                                     @PathVariable Long id) {
        service.delete(id);
        return GenericResponse.ok();
    }
}
