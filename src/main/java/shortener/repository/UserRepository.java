package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shortener.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Override
    User save(User user);

    Iterable<User> findAll();

    @Override
    void deleteById(Long userId);
}