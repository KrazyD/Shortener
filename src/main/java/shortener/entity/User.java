package shortener.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Type;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
public class User implements BaseEntity, Serializable {

//    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private static final long serialVersionUID = -7539517893214297867L;

    public User() {}

    public User(String password, String login, String username, String[] roles) {
        this.password = password;
        this.login = login;
        this.username = username;
        this.roles = roles;
    }

    @Id
//    @Min(value = 1, message = "Field 'userId' can`t be empty!")
    @Column(name = "\"id\"", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Field 'password' can`t be empty!")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Field 'login' can`t be empty!")
    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @NotBlank(message = "Field 'username' can`t be empty!")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "roles")
    @Type(type = "shortener.config.StringArrayType")
    private String[] roles;

    @OneToMany(mappedBy = "userId")
    private Set<Reference> referenceSet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
//        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                password.equals(user.password) &&
                login.equals(user.login) &&
                username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, login, username);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Error parse User to JSON!");
            e.printStackTrace();
            return "{}";
        }
    }
}

