package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import shortener.entity.Reference;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferenceRepository extends CrudRepository<Reference, Long> {

    Optional<Reference> findByReducedRef(String reducedRef);

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userRepository.findById(#userId).get()?.getLogin() == authentication?.getName()")
    List<Reference> findByUserId(@Param("userId") Long userId);
}
