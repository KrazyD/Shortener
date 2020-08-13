package shortener.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shortener.entity.LoginForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    AuthenticationManager authenticationManager;

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginForm loginForm = getLoginForm(request);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginForm.getLogin(), loginForm.getPassword());

        setDetails(request, authRequest);

        return this.authenticationManager.authenticate(authRequest);
    }

    private LoginForm getLoginForm(HttpServletRequest request) {
        LoginForm loginForm = null;
        try {
            loginForm = new ObjectMapper().readValue(request.getReader(), LoginForm.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginForm != null ? loginForm : new LoginForm();
    }

}
