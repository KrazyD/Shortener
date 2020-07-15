package shortener.entity;

import javax.validation.constraints.NotBlank;

public class RegistrationForm {

    @NotBlank(message = "Field 'password' can`t be empty!")
    private String password;

    @NotBlank(message = "Field 'login' can`t be empty!")
    private String login;

    @NotBlank(message = "Field 'username' can`t be empty!")
    private String username;

    private Boolean isAdmin = false;

    public RegistrationForm() {}

    public RegistrationForm(String password, String login, String username) {
        this.password = password;
        this.login = login;
        this.username = username;
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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
