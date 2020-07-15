package shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shortener.entity.BaseEntity;
import shortener.entity.Reference;
import shortener.repository.IReferenceRepository;

import java.util.List;

@Service
public class ReferenceService implements IReferenceService {

    @Autowired
    IReferenceRepository referenceRepository;

    @Override
    public String findAll() {
        Iterable<Reference> refs = referenceRepository.findAll();
        StringBuilder refsList = new StringBuilder("{ ");
        refs.forEach(refsList::append);

        return refsList.append(" }").toString();
    }

    public Reference findById(Long id) {
        return referenceRepository.findById(id).orElse(new Reference());
    }

    @Override
    public List<Reference> findByUserId(Long userId) {
        return referenceRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Reference save(BaseEntity ref) {
        return referenceRepository.save((Reference) ref);
    }

    @Override
    public Reference findByReducedRef(String redRef) {
        return referenceRepository.findByReducedRef(redRef);
    }

    @Override
    public void delete(Long id) {
        referenceRepository.deleteById(id);
    }

}
