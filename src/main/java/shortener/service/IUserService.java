package shortener.service;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import shortener.entity.RegistrationForm;
import shortener.entity.User;

public interface IUserService extends BaseService {

    @Override
    @PreAuthorize("@userService.getLoginById(#id) == authentication.getName() or hasRole('ROLE_ADMIN')")
    User findById(@Param("id") Long id);

    @PreAuthorize("@userService.getLoginById(#modifiedUser.getId()) == authentication.getName() or hasRole('ROLE_ADMIN')")
    User updateUser(@Param("modifiedUser")User modifiedUser);

    @PreAuthorize("@userService.getLoginById(#id) != authentication.getName() and hasRole('ROLE_ADMIN')")
    boolean deleteUser(Long id);

    @PreAuthorize("isAuthenticated()")
    User getCurrentAuthorizedUser();

    User registerUser(RegistrationForm form);

    String getLoginById(Long id);
    String getLoginByRefId(Long refId);
    String getLoginByReducedRef(String reducedRef);
}
