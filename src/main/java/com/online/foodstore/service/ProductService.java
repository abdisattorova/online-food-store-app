package com.online.foodstore.service;

import com.online.foodstore.model.dto.CustomPage;
import com.online.foodstore.model.dto.PaginationRequest;
import com.online.foodstore.model.dto.ProductDTO;
import com.online.foodstore.model.entity.EProductStatus;
import com.online.foodstore.model.entity.Product;
import com.online.foodstore.model.mapper.ProductMapper;
import com.online.foodstore.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Log4j2
@Service
public class ProductService extends GenericService<Product, ProductDTO, PaginationRequest, ProductRepository, ProductMapper> {

    private final CategoryService categoryService;

    public ProductService(ProductRepository repository, ProductMapper mapper, @Lazy CategoryService categoryService) {
        super(repository, mapper, Product.class);
        this.categoryService = categoryService;
    }

    public CustomPage<ProductDTO> getAll(PaginationRequest paginationRequest, EProductStatus status, Long categoryId) {
        return super.getAll(paginationRequest, hasStatusAndSearch(status, categoryId, paginationRequest.getSearch()));
    }

    @Transactional
    public void delete(Long id) {
        var user = findById(id);
        user.setStatus(EProductStatus.DELETED);
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        categoryService.findById(dto.getCategoryId());
        return super.create(dto);
    }

    @Override
    public ProductDTO edit(ProductDTO dto) {
        categoryService.findById(dto.getCategoryId());
        return super.edit(dto);
    }

    public int getNumberOfProducts(Long categoryId) {
        return repository.countAllByCategoryId(categoryId);
    }

    private Specification<Product> hasStatusAndSearch(EProductStatus status, Long categoryId, String search) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Initial predicate

            // Add condition for status if not null
            if (Objects.nonNull(status)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
            }

            // Add condition for categoryId if not null
            if (Objects.nonNull(categoryId)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }

            // Add condition for search if not empty
            if (Objects.nonNull(search) && !search.isBlank()) {
                String searchPattern = "%" + search.toLowerCase() + "%"; // Case-insensitive search
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern))
                );
            }

            return predicate;
        };
    }
}


