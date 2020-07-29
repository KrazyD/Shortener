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
//@Component
//public class SpringDataJpaUserDetailsService implements UserDetailsService {
//
//    private final UserRepository repository;
//
//    @Autowired
//    public SpringDataJpaUserDetailsService(UserRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        shortener.entity.User user = this.repository.findByLogin(login).orElse(new shortener.entity.User());
//        return new User(user.getLogin(), user.getPassword(),
//                AuthorityUtils.createAuthorityList(user.getRoles()));
//    }
//
//}