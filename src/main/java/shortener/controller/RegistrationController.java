package shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import shortener.entity.BaseEntity;
import shortener.entity.RegistrationForm;
import shortener.entity.User;
import shortener.service.IUserService;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;

import static shortener.controller.HomeController.getErrorMessage;
import static shortener.controller.HomeController.handleErrors;

@Controller
public class RegistrationController {

//    private static final Logger logger = LogManager.getLogger(RegistrationController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @ResponseBody
    @PostMapping(value = "/register", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationForm form, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getErrorMessage(errors) + " }");
        }

        User user = new User(encoder.encode(form.getPassword()), form.getLogin(), form.getUsername(), form.getRoles());

        User savedUser = (User) handleErrors((service, usr) -> service.save((BaseEntity) usr), userService, user);

        if (savedUser == null || savedUser.getId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Failure to register user!\" }");
        } else {

            Collection<GrantedAuthority> authorities = rolesToAuthorities(savedUser.getRoles());
            Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getLogin(),
                    savedUser.getPassword(), authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + savedUser + " }");
        }
    }

    private Collection<GrantedAuthority> rolesToAuthorities(String[] roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

}
