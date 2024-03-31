package com.online.foodstore.service;

import com.online.foodstore.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) {
            return Optional.of(0L);
        }
        var user = (User) authentication.getPrincipal();

        return Optional.of(user.getId());
    }

}