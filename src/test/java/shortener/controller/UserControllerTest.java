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
import shortener.entity.BaseEntity;
import shortener.entity.User;
import shortener.service.IUserService;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @InjectMocks
    private UserController controller;

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
    public void getUser() throws Exception {
        Mockito.when(userService.findById(1L)).thenReturn(user);

        this.mockMvc.perform(get("/user").param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data").value(user));
    }

    @Test
    public void getUser_asAdmin() throws Exception {
        List<BaseEntity> foundUsers = Collections.singletonList(user);
        Mockito.when(userService.findAll()).thenReturn(foundUsers);

        this.mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data[0]").value(foundUsers.get(0)));
    }

    @Test
    public void modifyUser() throws Exception {
        User modifiedUser = new User("test", "test", "test", new String[]{"ROLE_WRITER"});
        modifiedUser.setId(1L);
        Mockito.when(userService.updateUser(modifiedUser)).thenReturn(modifiedUser);

        this.mockMvc.perform(put("/user")
                .content("{ \"id\": 1, \"password\": \"test\", \"login\": \"test\", " +
                        "\"username\": \"test\", \"roles\":[\"ROLE_WRITER\"]}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data").value(modifiedUser));
    }

    @Test
    public void removeUser() throws Exception {
        Mockito.when(userService.deleteUser(1L)).thenReturn(true);

        this.mockMvc.perform(delete("/user").param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data").value("User successfully removed!"));
    }

    @Test
    public void getCurrentUser() throws Exception {
        Mockito.when(userService.getCurrentAuthorizedUser()).thenReturn(user);

        this.mockMvc.perform(get("/login")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.username").value("login"))
                .andExpect(jsonPath("$.data.roles").value("ROLE_USER"));
    }
}