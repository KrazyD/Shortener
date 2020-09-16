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
import shortener.entity.Reference;
import shortener.entity.User;
import shortener.service.IUserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        User userWithoutId = new User("password", "login", "username", new String[]{"ROLE_USER"});
        Mockito.when(userService.save(userWithoutId)).thenReturn(user);

//        this.mockMvc.perform(post("/ref")
//                .content("{ \"fullRef\": \"http://google.com\", \"userId\": 1 }")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=utf-8"))
//                .andExpect(jsonPath("$.status").value("Success"))
//                .andExpect(jsonPath("$.data").value(ref));
    }

    @Test
    public void modifyUser() throws Exception {
    }

    @Test
    public void removeUser() throws Exception {
    }

    @Test
    public void getCurrentUser() throws Exception {
    }
}