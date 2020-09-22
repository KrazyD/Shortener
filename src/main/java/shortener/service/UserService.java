package shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shortener.entity.BaseEntity;
import shortener.entity.Reference;
import shortener.entity.RegistrationForm;
import shortener.entity.User;
import shortener.repository.ReferenceRepository;
import shortener.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReferenceRepository referenceRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<BaseEntity> findAll() {
        Iterator<User> users = userRepository.findAll().iterator();
        List<BaseEntity> userList = new ArrayList<>();

        while(users.hasNext()) {
            userList.add(users.next());
        }

        return userList;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    @Override
    public User updateUser(User modifiedUser) {
        Optional<User> foundUserWrapper = userRepository.findById(modifiedUser.getId());
        if (foundUserWrapper.isEmpty()) {
            return null;
        }

        return userRepository.save(modifiedUser);
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<User> userWrapper = userRepository.findById(id);
        if (userWrapper.isEmpty()) {
            return false;
        }

        userRepository.delete(userWrapper.get());
        return true;
    }

    @Override
    public User getCurrentAuthorizedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            return null;
        }

        String login = authentication.getName();
        Optional<User> userWrapper = userRepository.findByLogin(login);
        if (userWrapper.isEmpty()) {
            return null;
        }
        return userWrapper.get();
    }

    @Override
    public String getLoginById(Long id) {
        Optional<User> userWrapper = userRepository.findById(id);
        if (userWrapper.isEmpty()) {
            return null;
        }

        return userWrapper.get().getLogin();
    }

    @Override
    public String getLoginByRefId(Long refId) {
        Optional<Reference> refWrapper = referenceRepository.findById(refId);
        if (refWrapper.isEmpty()) {
            return null;
        }

        Reference ref = refWrapper.get();
        Long userId = ref.getUserId();
        Optional<User> userWrapper = userRepository.findById(userId);
        if (userWrapper.isEmpty()) {
            return null;
        }

        return userWrapper.get().getLogin();
    }

    @Override
    public String getLoginByReducedRef(String reducedRef) {
        Optional<Reference> refWrapper = referenceRepository.findByReducedRef(reducedRef);
        if (refWrapper.isEmpty()) {
            return null;
        }

        Reference ref = refWrapper.get();
        Long userId = ref.getUserId();
        Optional<User> userWrapper = userRepository.findById(userId);
        if (userWrapper.isEmpty()) {
            return null;
        }

        return userWrapper.get().getLogin();
    }

    @Override
    public User registerUser(RegistrationForm form) {
        User user = new User(encoder.encode(form.getPassword()), form.getLogin(), form.getUsername(), form.getRoles());

        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            return null;
        } else {
            Collection<GrantedAuthority> authorities = rolesToAuthorities(savedUser.getRoles());
            Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getLogin(),
                    savedUser.getPassword(), authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return savedUser;
        }
    }

    @Override
    public User save(BaseEntity user) {
         return userRepository.save((User) user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private Collection<GrantedAuthority> rolesToAuthorities(String[] roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
