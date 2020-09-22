package shortener.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shortener.entity.Reference;
import shortener.repository.ReferenceRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ReferenceServiceTest {

    @InjectMocks
    private ReferenceService referenceService;

    @Mock
    private ReferenceRepository referenceRepository;

    private String reducedRef;
    private Optional<Reference> refWrapper;
    private Reference ref;
    private String fullRef;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        reducedRef = "small.link/123456789";
        fullRef = "http://google.com";
        ref = new Reference(fullRef, "small.link/123456789", 1, 1L);
        ref.setId(1);
        refWrapper = Optional.of(ref);
    }

    @Test
    public void getFullRef() {
        when(referenceRepository.findByReducedRef(reducedRef)).thenReturn(refWrapper);

        assertEquals(fullRef, referenceService.getFullRef(reducedRef));
    }

    @Test
    public void createRef() {
        Reference savedRef = new Reference(fullRef, "small.link/-247021602", 0, 1);
        when(referenceRepository.save(savedRef)).thenReturn(savedRef);

        assertEquals(savedRef, referenceService.createRef(1L, fullRef));
    }

    @Test
    public void findByUserId() {
        Reference savedRef = new Reference(fullRef, "small.link/-247021602", 0, 1);
        List<Reference> refs = Collections.singletonList(savedRef);
        when(referenceRepository.findByUserId(1L)).thenReturn(refs);

        assertEquals(refs, referenceService.findByUserId(1L));
    }

    @Test
    public void findAll() {
        Reference savedRef = new Reference(fullRef, "small.link/-247021602", 0, 1);
        Iterable<Reference> refs = Collections.singletonList(savedRef);
        when(referenceRepository.findAll()).thenReturn(refs);

        assertEquals(refs, referenceService.findAll());
    }

    @Test
    public void updateReference() {
        Reference updatedRef = new Reference(fullRef, "small.link/-247021602", 1, 1);
        updatedRef.setId(1L);
        when(referenceRepository.save(updatedRef)).thenReturn(updatedRef);

        Reference twitterRef = new Reference("http://twitter.com", "small.link/123456789", 1, 1L);
        twitterRef.setId(1);
        Optional<Reference> twitterRefWrapper = Optional.of(twitterRef);
        when(referenceRepository.findById(1L)).thenReturn(twitterRefWrapper);

        assertEquals(updatedRef, referenceService.updateReference(1L, fullRef));
    }

    @Test
    public void deleteReference() {
        when(referenceRepository.findByReducedRef(reducedRef)).thenReturn(refWrapper);

        assertTrue(referenceService.deleteReference("small.link/123456789"));
    }
}