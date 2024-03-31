package com.online.foodstore.repository;

import com.online.foodstore.model.entity.EUserStatus;
import com.online.foodstore.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByUsernameAndStatusNot(String username, EUserStatus status);

    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, Long id);
}
