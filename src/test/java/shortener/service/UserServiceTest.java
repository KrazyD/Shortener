package shortener.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shortener.entity.Reference;
import shortener.entity.RegistrationForm;
import shortener.entity.User;
import shortener.repository.ReferenceRepository;
import shortener.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private ReferenceRepository referenceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    private User user;
    private Optional<User> userWrapper;

    private String reducedRef;
    private Optional<Reference> refWrapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User("password", "login", "username", new String[]{"ROLE_USER"});
        user.setId(1L);

        userWrapper = Optional.of(user);

        reducedRef = "small.link/123456789";
        Reference ref = new Reference("http://google.com", "small.link/123456789", 1, 1L);
        ref.setId(1);
        refWrapper = Optional.of(ref);
    }

    @Test
    public void findAll() {
        Iterable<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);

        assertEquals(users, userService.findAll());
    }

    @Test
    public void findById() {
        when(userRepository.findById(1L)).thenReturn(userWrapper);

        assertEquals(user, userService.findById(1L));
    }

    @Test
    public void updateUser() {
        User modifiedUser = new User("test", "test", "test", new String[]{"ROLE_WRITER"});
        modifiedUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(userWrapper);
        when(userRepository.save(modifiedUser)).thenReturn(modifiedUser);

        assertEquals(modifiedUser, userService.updateUser(modifiedUser));
    }

    @Test
    public void deleteUser() {
        when(userRepository.findById(1L)).thenReturn(userWrapper);

        assertTrue(userService.deleteUser(1L));
    }

    @Test
    public void getCurrentAuthorizedUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("username");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userRepository.findByLogin("username")).thenReturn(userWrapper);

        assertEquals(user, userService.getCurrentAuthorizedUser());
    }

    @Test
    public void getLoginById() {
        when(userRepository.findById(1L)).thenReturn(userWrapper);

        assertEquals("login", userService.getLoginById(1L));
    }

    @Test
    public void getLoginByRefId() {
        when(referenceRepository.findById(1L)).thenReturn(refWrapper);
        when(userRepository.findById(1L)).thenReturn(userWrapper);

        assertEquals("login", userService.getLoginByRefId(1L));
    }

    @Test
    public void getLoginByReducedRef() {
        when(referenceRepository.findByReducedRef(reducedRef)).thenReturn(refWrapper);
        when(userRepository.findById(1L)).thenReturn(userWrapper);

        assertEquals("login", userService.getLoginByReducedRef(reducedRef));
    }

    @Test
    public void registerUser() {
        User userWithoutID = new User("password", "login", "username", new String[]{"ROLE_USER"});
        when(userRepository.save(userWithoutID)).thenReturn(user);
        when(encoder.encode("password")).thenReturn("password");

        RegistrationForm form = new RegistrationForm("password", "login", "username", new String[]{"ROLE_USER"});
        assertEquals(user, userService.registerUser(form));
    }
}