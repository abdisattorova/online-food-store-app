package com.online.foodstore.service;

import com.online.foodstore.model.dto.CategoryDTO;
import com.online.foodstore.model.dto.CustomPage;
import com.online.foodstore.model.dto.PaginationRequest;
import com.online.foodstore.model.entity.Category;
import com.online.foodstore.model.entity.ECategoryStatus;
import com.online.foodstore.model.mapper.CategoryMapper;
import com.online.foodstore.repository.CategoryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Log4j2
@Service
public class CategoryService extends GenericService<Category, CategoryDTO, PaginationRequest, CategoryRepository, CategoryMapper> {

    private final ProductService productService;


    public CategoryService(CategoryRepository repository, CategoryMapper mapper, ProductService productService) {
        super(repository, mapper, Category.class);
        this.productService = productService;
    }

    public CustomPage<CategoryDTO> getAll(PaginationRequest paginationRequest, ECategoryStatus status) {
        return super.getAll(paginationRequest, hasStatusAndSearch(status, paginationRequest.getSearch()));
    }

    @Override
    public CategoryDTO get(Long id) {
        CategoryDTO categoryDTO = super.get(id);
        categoryDTO.setNumberOfProducts(productService.getNumberOfProducts(id));
        return categoryDTO;
    }

    @Transactional
    public void delete(Long id) {
        var user = findById(id);
        user.setStatus(ECategoryStatus.DELETED);
    }

    public static Specification<Category> hasStatusAndSearch(ECategoryStatus status, String search) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Initial predicate

            // Add condition for status if not null
            if (Objects.nonNull(status)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
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


