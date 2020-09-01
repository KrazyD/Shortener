package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import shortener.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @PreAuthorize("authentication?.name != null")
    Iterable<User> findAll();

    @Override
    @PreAuthorize("@userRepository.findById(#userId).get()?.login == authentication?.name")
    Optional<User> findById(@Param("userId") Long userId);

    Optional<User> findByLogin(@Param("login") String login);

    Optional<User> findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    @Override
    User save(@Param("user") User user);

    @Override
    @PreAuthorize("@userRepository.findById(#userId)?.login != authentication?.name and hasRole('ROLE_ADMIN')")
    void deleteById(@Param("userId") Long userId);
}