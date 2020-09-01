package shortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shortener.entity.User;
import shortener.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> user = this.repository.findByLogin(login);
        if(user.isEmpty() || !encoder.matches(password, user.get().getPassword())) {
            throw new BadCredentialsException("User with the specified credentials was not found!");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : user.get().getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new UsernamePasswordAuthenticationToken(login, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}