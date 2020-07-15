package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shortener.entity.BaseEntity;
import shortener.entity.Reference;

import java.util.List;

@Repository
public interface IReferenceRepository extends CrudRepository<Reference, Long> {

    List<Reference> findByUserId(Long userId);
    Reference findByReducedRef(String reducedRef);
    Reference findByFullRefAndUserId(String fullRef, Long userId);

}
