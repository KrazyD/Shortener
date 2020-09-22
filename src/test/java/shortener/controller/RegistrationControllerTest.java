package shortener.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shortener.entity.RegistrationForm;
import shortener.entity.User;
import shortener.service.IUserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController controller;

    @Mock
    private IUserService userService;

    private MockMvc mockMvc;
    private User user;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = new User("password", "login", "username", new String[]{"ROLE_USER"});
        user.setId(1L);
    }

    @Test
    public void registerUser() throws Exception {
        RegistrationForm form = new RegistrationForm("password", "login",
                "username", new String[]{"ROLE_USER"});
        Mockito.when(userService.registerUser(form)).thenReturn(user);

        this.mockMvc.perform(post("/register").content("{ \"password\":\"password\", " +
                "\"login\":\"login\", \"username\":\"username\", \"roles\":[\"ROLE_USER\"] }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data").value(user));
    }
}