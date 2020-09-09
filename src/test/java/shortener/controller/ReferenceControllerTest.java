package shortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shortener.entity.Reference;
import shortener.entity.ReferenceForm;
import shortener.service.IReferenceService;
import shortener.service.IUserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    }

    @Test
    public void useShortRef() throws Exception {
        Mockito.when(referenceService.findByReducedRef("small.link/-247021602")).thenReturn(ref);

        this.mockMvc.perform(get("/small.link/123456"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("http://google.com"));
    }

    @Test
    public void createReferences() throws Exception {
        Mockito.when(referenceService.save(ref)).thenReturn(ref);

        this.mockMvc.perform(post("/ref")
                .content("{ \"fullRef\": \"http://google.com\", \"userId\": 1 }")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
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
        List<Reference> foundRefs = Collections.singletonList(ref);
        Mockito.when(referenceService.findAll()).thenReturn(foundRefs.toString());

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("login", "password", authorities);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mockMvc.perform(get("/ref")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.data[0]").value(foundRefs.get(0)));
    }

    @Test
    public void updateReferences() {
    }

    @Test
    public void removeReferences() {
    }
}