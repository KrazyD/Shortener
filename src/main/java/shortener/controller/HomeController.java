package shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shortener.entity.BaseEntity;
import shortener.entity.User;
import shortener.service.BaseService;
import shortener.service.IUserService;
import shortener.service.UserService;

import java.util.Objects;

// TODO проверить все методы и их валидацию
// TODO прикрутить security, и чтобы у админов была отдельная страничка с возможностью редактировать все
// TODO инкремент обращений к ссылке проверить
// TODO метод получения длинной по короткой + добавить ТЕСТ для него
// TODO настроить logout

@Controller
public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @GetMapping("/")
    public String homeInit() {
        return "forward:/resources/templates/index.html";
    }

    @RequestMapping("/favicon.ico")
    public String favicon() {
        return "forward:/resources/favicon.ico";
    }

    public static String getExceptionMessage(Errors errors) {
        StringBuilder errorMessage = new StringBuilder("\"Some errors were caused: ");
        for (ObjectError err : errors.getAllErrors()) {
            errorMessage.append(err.getDefaultMessage()).append(" ");
        }
        errorMessage.append("\"");
        return errorMessage.toString();
    }

//    сделать в сервисах методы для все CRUD операций и повесить на них PreAuthorize аннотации,
//    поменять метод handleErrors, и добавить этим методы CRUD  в BaseService

    public static BaseEntity handleErrors(TwoParamsFunction<BaseService, Object, BaseEntity> func, BaseService service, Object param) {
        BaseEntity resultEntity = null;

        try {
            resultEntity = func.apply(service, param);
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("!!!Error while handle request!!! - " + error);
        }

        return resultEntity;
    }

    public static String getExceptionMessage(Exception ex) {
        String error;
        if (ex.getCause() == null) {
            error = ex.getMessage();
        } else if(ex.getCause().getCause() == null) {
            error = ex.getCause().getMessage();
        } else {
            error = ex.getCause().getCause().getMessage();
        }
        return error;
    }

    public static boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public static boolean isCurrentUserOwner(Long userId, IUserService userService) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.isAuthenticated()) {
            return false;
        }

        User foundUser = (User) handleErrors((service, id) -> service.findById((Long) id), userService, userId);

        return Objects.equals(auth.getName(), foundUser.getLogin());
    }
}
