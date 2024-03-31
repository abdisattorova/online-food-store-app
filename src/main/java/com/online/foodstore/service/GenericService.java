package com.online.foodstore.service;

import com.online.foodstore.exception.GeneralApiException;
import com.online.foodstore.model.dto.BaseDTO;
import com.online.foodstore.model.dto.CustomPage;
import com.online.foodstore.model.dto.PaginationRequest;
import com.online.foodstore.model.entity.BaseEntity;
import com.online.foodstore.model.mapper.GenericMapper;
import com.online.foodstore.repository.BaseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.online.foodstore.utils.ErrorMessages.SHOULDNT_BE_NULL;
import static com.online.foodstore.utils.ErrorMessages.notFound;

@Service
@Log4j2
public abstract class GenericService<E extends BaseEntity, DTO extends BaseDTO, REQ extends PaginationRequest, R extends BaseRepository<E>, T extends GenericMapper<E, DTO>> {

    protected final R repository;
    protected final T mapper;
    protected final Class<E> entityTypeClass;

    public GenericService(R repository, T mapper, Class<E> entityTypeClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityTypeClass = entityTypeClass;
    }

    public E findById(Long id) {
        if (Objects.isNull(id)) {
            throw new GeneralApiException("ID" + SHOULDNT_BE_NULL);
        }
        return repository.findById(id).orElseThrow(() -> new GeneralApiException(notFound(entityTypeClass.getSimpleName())));
    }

    public DTO get(Long id) {
        final E entity = findById(id);
        return mapper.toDto(entity);
    }

    public List<DTO> getAll() {
        List<E> all = repository.findAll();
        return mapper.toDtoList(all);
    }

    public CustomPage<DTO> getAll(REQ req) {
        Page<E> page = repository.findAll(req.getPageRequest());
        return CustomPage.of(page, mapper.toDtoList(page.getContent()));
    }

    public CustomPage<DTO> getAll(REQ req, Specification<E> specification) {
        Page<E> page = repository.findAll(specification, req.getPageRequest());
        return CustomPage.of(page, mapper.toDtoList(page.getContent()));
    }

    public DTO create(DTO dto) {
        E entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public DTO edit(DTO dto) {
        E entity = findById(dto.getId());
        mapper.updateEntityFromDto(entity, dto);
        E saved = repository.save(entity);
        return mapper.toDto(saved);
    }
}
