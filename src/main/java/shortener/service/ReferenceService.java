package shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shortener.entity.BaseEntity;
import shortener.entity.Reference;
import shortener.repository.ReferenceRepository;

import java.util.*;

@Service
public class ReferenceService implements IReferenceService {

    @Autowired
    ReferenceRepository referenceRepository;

    @Override
    public String getFullRef(String reducedRef) {
        Optional<Reference> refWrapped = referenceRepository.findByReducedRef(reducedRef);
        if (refWrapped.isEmpty()) {
            return null;
        }

        Reference ref = refWrapped.get();
        ref.setRequestsNumb(ref.getRequestsNumb() + 1);
        referenceRepository.save(ref);
        return ref.getFullRef();
    }

    @Override
    public Reference createRef(Long userId, String fullRef) {
        String reducedRef = "small.link/" + Objects.toString(Objects.hashCode(fullRef));
        Reference reference = new Reference(fullRef, reducedRef, 0, userId);
        return referenceRepository.save(reference);
    }

    @Override
    public List<Reference> findByUserId(Long userId) {
        return referenceRepository.findByUserId(userId);
    }


    @Override
    public List<BaseEntity> findAll() {
        Iterator<Reference> refs = referenceRepository.findAll().iterator();
        List<BaseEntity> refsList = new ArrayList<>();

        while(refs.hasNext()) {
            refsList.add(refs.next());
        }

        return refsList;
    }

    @Override
    public Reference updateReference(Long refId, String fullRef) {
        Optional<Reference> refWrapper = referenceRepository.findById(refId);
        if (refWrapper.isEmpty()) {
            return null;
        }
        Reference ref = refWrapper.get();

        String reducedRef = "small.link/" + Objects.toString(Objects.hashCode(fullRef));
        ref.setFullRef(fullRef);
        ref.setReducedRef(reducedRef);

        return referenceRepository.save(ref);
    }

    @Override
    public boolean deleteReference(String reducedRef) {
        Optional<Reference> refWrapper = referenceRepository.findByReducedRef(reducedRef);
        if (refWrapper.isEmpty()) {
            return false;
        }

        Reference ref = refWrapper.get();

        referenceRepository.delete(ref);
        return true;
    }

    @Override
    public void delete(Long id) {
        referenceRepository.deleteById(id);
    }

    @Override
    public Reference save(BaseEntity ref) {
        return referenceRepository.save((Reference) ref);
    }

    @Override
    public Reference findById(Long id) {
        return referenceRepository.findById(id).orElse(null);
    }
}
