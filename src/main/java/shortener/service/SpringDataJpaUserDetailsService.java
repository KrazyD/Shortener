//package shortener.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import shortener.repository.UserRepository;
//
//import java.util.Optional;
//
//@Component
//public class SpringDataJpaUserDetailsService implements UserDetailsService {
//
//    private final UserRepository repository;
//
//    @Autowired
//    public SpringDataJpaUserDetailsService(UserRepository repository) {
//        super();
//        this.repository = repository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        System.out.println("!!!!!!!!!!!!!!!!!SpringDataJpaUserDetailsService.loadUserByUsername username=" + login);
//        Optional<shortener.entity.User> user = this.repository.findByLogin(login);
//        if(user.isEmpty()) {
//            throw new UsernameNotFoundException(login);
//        }
//        return new User(user.get().getLogin(), user.get().getPassword(),
//                AuthorityUtils.createAuthorityList(user.get().getRoles()));
//    }
//
//}