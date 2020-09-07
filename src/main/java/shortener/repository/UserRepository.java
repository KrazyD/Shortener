package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import shortener.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    Optional<User> findById(@Param("userId") Long userId);

    Optional<User> findByLogin(@Param("login") String login);

    @Override
    User save(@Param("user") User user);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Iterable<User> findAll();

    @Override
    @PreAuthorize("@userRepository.findById(#userId).get()?.getLogin() != authentication.getName() and hasRole('ROLE_ADMIN')")
    void deleteById(@Param("userId") Long userId);
}