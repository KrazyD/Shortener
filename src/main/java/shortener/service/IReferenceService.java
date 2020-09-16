package shortener.service;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import shortener.entity.Reference;

import java.util.List;

public interface IReferenceService extends BaseService {

    String getFullRef(String reducedRef);

    @PreAuthorize("@userService.getLoginById(#userId) == authentication.getName() or hasRole('ROLE_ADMIN')")
    Reference createRef(@Param("userId") Long userId, String fullRef);

    @PreAuthorize("@userService.getLoginById(#userId) == authentication.getName() or hasRole('ROLE_ADMIN')")
    List<Reference> findByUserId(@Param("userId") Long userId);

    Reference findById(Long id);

    @PreAuthorize("@userService.getLoginByRefId(#refId) == authentication.getName() or hasRole('ROLE_ADMIN')")
    Reference updateReference(@Param("refId")Long refId, String fullRef);

    @PreAuthorize("@userService.getLoginByReducedRef(#reducedRef) == authentication.getName() or hasRole('ROLE_ADMIN')")
    boolean deleteReference(@Param("reducedRef")String reducedRef);
}
