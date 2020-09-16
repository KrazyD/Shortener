package shortener.service;

import org.springframework.security.access.prepost.PreAuthorize;
import shortener.entity.BaseEntity;

import java.util.List;


public interface BaseService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<BaseEntity> findAll();

    BaseEntity findById(Long id);

    BaseEntity save(BaseEntity entity);

    void delete(Long id);
}
