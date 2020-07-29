package shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shortener.entity.BaseEntity;
import shortener.service.BaseService;

// TODO прикрутить валидацию к @ResponseBody
// TODO сделать возвращемое значение у репозиториев Optional<>
// TODO проверить все методы и их валидацию
// TODO прикрутить security, и чтобы у админов была отдельная страничка с возможностью редактировать все
// TODO починить favicon
// TODO инкремент обращений к ссылке
// TODO метод получения длинной по короткой + добавить ТЕСТ для него

@Controller
public class HomeController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/")
    public String homeInit() {
        return "forward:/resources/templates/index.html";
    }

    @RequestMapping("/favicon.ico")
    public String favicon() {
        return "forward:/resources/favicon.ico";
    }

    public static String getErrorMessage(Errors errors) {
        StringBuilder errorMessage = new StringBuilder("\"Some errors were caused: ");
        for (ObjectError err : errors.getAllErrors()) {
            errorMessage.append(err.getDefaultMessage()).append(" ");
        }
        errorMessage.append("\"");
        return errorMessage.toString();
    }

    public static BaseEntity handleErrors(TwoParamsFunction<BaseService, Object, BaseEntity> func, BaseService service, Object param) {
        BaseEntity resultEntity = null;
        String error = null;

        try {
            resultEntity = func.apply(service, param);
        } catch (Exception ex) {
            error = ex.getCause().getCause().getMessage();
        }

        if (error != null) {
            logger.error("!!!Error while handle request!!!\n" + error);
//            System.err.println("!!!Error while handle request!!!\n" + error);
        }

        return resultEntity;
    }
}
