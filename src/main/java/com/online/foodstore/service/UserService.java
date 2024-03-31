package com.online.foodstore.service;

import com.online.foodstore.config.AuthProvider;
import com.online.foodstore.exception.GeneralApiException;
import com.online.foodstore.model.dto.CustomPage;
import com.online.foodstore.model.dto.PaginationRequest;
import com.online.foodstore.model.dto.UserDTO;
import com.online.foodstore.model.entity.ERole;
import com.online.foodstore.model.entity.EUserStatus;
import com.online.foodstore.model.entity.User;
import com.online.foodstore.model.mapper.UserMapper;
import com.online.foodstore.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.online.foodstore.utils.ErrorMessages.*;

@Log4j2
@Service
public class UserService extends GenericService<User, UserDTO, PaginationRequest, UserRepository, UserMapper> {

    private final UserRepository repository;
    private final AuthProvider authProvider;

    public UserService(UserRepository repository, UserMapper mapper, AuthProvider authProvider) {
        super(repository, mapper, User.class);
        this.repository = repository;
        this.authProvider = authProvider;
    }

    public CustomPage<UserDTO> getAll(PaginationRequest paginationRequest, EUserStatus status, ERole role) {
        return super.getAll(paginationRequest, hasStatusAndRoleAndSearch(status, role, paginationRequest.getSearch()));
    }

    public User findByUsername(String username) {
        return repository.findByUsernameAndStatusNot(username, EUserStatus.BLOCKED).orElseThrow(() -> new GeneralApiException(notFound(entityTypeClass.getSimpleName())));
    }

    @Override
    public UserDTO create(UserDTO dto) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new GeneralApiException(LOGIN_SHOULD_BE_UNIQUE);
        }
        return super.create(dto);
    }

    @Override
    public UserDTO edit(UserDTO dto) {
        if (repository.existsByUsernameAndIdNot(dto.getUsername(), dto.getId())) {
            throw new GeneralApiException(LOGIN_SHOULD_BE_UNIQUE);
        }
        return super.edit(dto);
    }

    @Transactional
    public void delete(Long id) {
        // the user can't delete himself/herself
        var authenticatedUser = authProvider.getAuthenticatedUser();

        if (Objects.equals(authenticatedUser.getId(), id)) {
            throw new GeneralApiException(USER_DELETION_CONSTRAINT);
        }

        var user = findById(id);
        user.setStatus(EUserStatus.DELETED);
    }

    @Transactional
    public void changeStatus(Long id, EUserStatus status) {
        var authenticatedUser = authProvider.getAuthenticatedUser();

        if (Objects.equals(authenticatedUser.getId(), id)) {
            throw new GeneralApiException(USER_MODIFICATION_CONSTRAINT);
        }
        var user = findById(id);
        user.setStatus(status);
    }

    public static Specification<User> hasStatusAndRoleAndSearch(EUserStatus status, ERole role, String search) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Initial predicate

            // Add condition for status if not null
            if (Objects.nonNull(status)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
            }

            // Add condition for role if not null
            if (Objects.nonNull(role)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("role"), role));
            }

            // Add condition for search if not empty
            if (Objects.nonNull(search) && !search.isBlank()) {
                String searchPattern = "%" + search.toLowerCase() + "%"; // Case-insensitive search
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchPattern)
                        )
                );
            }

            return predicate;
        };
    }
}


