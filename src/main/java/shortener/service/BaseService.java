package shortener.service;

import shortener.entity.BaseEntity;


public interface BaseService {
    String findAll();
    BaseEntity findById(Long id);
    BaseEntity save(BaseEntity entity);
    void delete(Long id);
}
