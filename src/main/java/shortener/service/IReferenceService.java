package shortener.service;

import shortener.entity.Reference;

import java.util.List;

public interface IReferenceService extends BaseService {
    List<Reference> findByUserId(Long userId);
    Reference findByReducedRef(String redRef);
}
