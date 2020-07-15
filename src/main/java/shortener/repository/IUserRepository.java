package shortener.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shortener.entity.User;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {

}