package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import shortener.entity.User;

import javax.swing.text.html.Option;
import java.util.Iterator;
import java.util.Optional;

@Repository
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface UserRepository extends CrudRepository<User, Long> {

    @PreAuthorize("authentication?.name != null")
    Iterable<User> findAll();

    @Override
    @PreAuthorize("@userRepository.findById(#userId).get()?.login == authentication?.name")
    Optional<User> findById(@Param("userId") Long userId);

//    @PreAuthorize("@userRepository.findByLogin(#login).get()?.login == authentication?.name")
    Optional<User> findByLogin(@Param("login") String login);

    Optional<User> findByUsername(@Param("username") String username);

    Optional<User> findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    @Override
    @PreAuthorize("#user?.login == authentication?.name")
    User save(@Param("user") User user);

    @Override
    @PreAuthorize("@userRepository.findById(#userId)?.login == authentication?.name")
    void deleteById(@Param("userId") Long userId);
}