package shortener.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shortener.entity.BaseEntity;
import shortener.entity.Reference;
import shortener.entity.User;
import shortener.service.IReferenceService;
import shortener.service.IUserService;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReferenceControllerTest {

    @InjectMocks
    private ReferenceController controller;

    @Mock
    private IReferenceService referenceService;

    @Mock
    private IUserService userService;

    private MockMvc mockMvc;
    private Reference ref;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        ref = new Reference("http://google.com", "small.link/-247021602", 0, 1);
        ref.setId(1L);
    }

    @Test
    public void useShortRef() throws Exception {
        Mockito.when(referenceService.getFullRef("small.link/-247021602")).thenReturn("http://google.com");

        this.mockMvc.perform(get("/small.link/-247021602"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("http://google.com"));
    }

    @Test
    public void createReferences() throws Exception {
        Mockito.when(referenceService.createRef(1L, "http://google.com")).thenReturn(ref);

        this.mockMvc.perform(post("/ref")
                .content("{ \"fullRef\": \"http://google.com\", \"userId\": 1 }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data").value(ref));
    }

    @Test
    public void getReferences() throws Exception {
        List<Reference> foundRefs = Collections.singletonList(ref);
        Mockito.when(referenceService.findByUserId(1L)).thenReturn(foundRefs);

        this.mockMvc.perform(get("/ref").param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data[0]").value(foundRefs.get(0)));
    }

    @Test
    public void getReferences_asAdmin() throws Exception {
        List<BaseEntity> foundRefs = Collections.singletonList(ref);
        Mockito.when(referenceService.findAll()).thenReturn(foundRefs);

        this.mockMvc.perform(get("/ref")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data[0]").value(foundRefs.get(0)));
    }

    @Test
    public void updateReferences() throws Exception {
        Mockito.when(referenceService.updateReference(1L, "http://google.com")).thenReturn(ref);

        this.mockMvc.perform(put("/ref")
                .content("{ \"fullRef\": \"http://google.com\", \"refId\": 1 }")
                .contentType(MediaType.APPLICATION_JSON))/*.andDo(print())*/
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data").value(ref));

    }

    @Test
    public void removeReferences() throws Exception {
        User user = new User("password", "login", "username", new String[] {"ROLE_USER", "ROLE_ADMIN"});
        Mockito.when(userService.findById(1L)).thenReturn(user);

        Mockito.when(referenceService.deleteReference("small.link/-247021602")).thenReturn(true);

        this.mockMvc.perform(delete("/ref").param("reducedRef", "small.link/-247021602")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data").value("Reference successfully removed!"));
    }
}