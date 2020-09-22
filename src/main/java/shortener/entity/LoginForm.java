package shortener.entity;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class LoginForm {

    @NotBlank(message = "Field 'password' can`t be empty!")
    private String password;

    @NotBlank(message = "Field 'login' can`t be empty!")
    private String login;

    public LoginForm() {
    }

    public LoginForm(String password, String login) {
        this.password = password;
        this.login = login;
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

    @Override
    public String toString() {
        return "LoginForm{" +
                "password='" + password + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginForm)) return false;
        LoginForm loginForm = (LoginForm) o;
        return password.equals(loginForm.password) &&
                login.equals(loginForm.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, login);
    }
}
