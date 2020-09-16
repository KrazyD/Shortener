package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shortener.entity.Reference;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceRepository extends CrudRepository<Reference, Long> {

    Optional<Reference> findByReducedRef(String reducedRef);
    List<Reference> findByUserId(Long userId);
}
