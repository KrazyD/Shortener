package shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shortener.entity.BaseEntity;
import shortener.entity.Reference;
import shortener.repository.ReferenceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReferenceService implements IReferenceService {

    @Autowired
    ReferenceRepository referenceRepository;

    @Override
    public String findAll() {
        Iterable<Reference> refs = referenceRepository.findAll();
        StringBuilder refsList = new StringBuilder("{ ");
        refs.forEach(refsList::append);

        return refsList.append(" }").toString();
    }

    public Reference findById(Long id) {
        return referenceRepository.findById(id).orElse(null);
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
        return referenceRepository.findByReducedRef(redRef).orElse(new Reference());
    }

    @Override
    public void delete(Long id) {
        referenceRepository.deleteById(id);
    }

}
