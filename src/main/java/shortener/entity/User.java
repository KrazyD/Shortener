package shortener.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
public class User implements BaseEntity, Serializable {

    private static final long serialVersionUID = -7539517893214297867L;

    public User() {}

    public User(String password, String login, String username, Boolean isAdmin) {
        this.password = password;
        this.login = login;
        this.username = username;
        this.isAdmin = isAdmin;
    }

    @Id
//    @Min(value = 1, message = "Field 'userId' can`t be empty!")
    @Column(name = "\"userId\"")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @NotBlank(message = "Field 'password' can`t be empty!")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Field 'login' can`t be empty!")
    @Column(name = "login")
    private String login;

    @NotBlank(message = "Field 'username' can`t be empty!")
    @Column(name = "username")
    private String username;

    @Column(name = "\"isAdmin\"")
    private Boolean isAdmin;

    @OneToMany(mappedBy = "userId")
    private Set<Reference> referenceSet;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Boolean getisAdmin() {
        return isAdmin;
    }

    public void setisAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId &&
                password.equals(user.password) &&
                login.equals(user.login) &&
                username.equals(user.username) &&
                isAdmin.equals(user.isAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, login, username, isAdmin);
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\":" + userId +
                ", \"password\":\"" + password + '\"' +
                ", \"login\":\"" + login + '\"' +
                ", \"username\":\"" + username + '\"' +
                ", \"isAdmin\":\"" + isAdmin + '\"' +
                "}";
    }
}

