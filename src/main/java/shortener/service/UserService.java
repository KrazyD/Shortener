package shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shortener.entity.BaseEntity;
import shortener.entity.User;
import shortener.repository.UserRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public String findAll() {
        Iterator<User> users = userRepository.findAll().iterator();
        List<User> userList = new ArrayList<>();

        while(users.hasNext()) {
            userList.add(users.next());
        }

        return userList.toString();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(new User());
    }

    @Override
    @Transactional
    public User save(BaseEntity user) {
         return userRepository.save((User) user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
