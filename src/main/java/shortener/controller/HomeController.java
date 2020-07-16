package shortener.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import shortener.entity.BaseEntity;
import shortener.service.BaseService;

import java.util.Locale;

// TODO прикрутить валидацию к @ResponseBody
// TODO сделать возвращемое значение у репозиториев Optional<>
// TODO проверить все методы и их валидацию
// TODO прикрутить security, и чтобы у админов была отдельная страничка с возможностью редактировать все
// TODO починить favicon
// TODO инкремент обращений к ссылке
// TODO метод получения длинной по короткой + добавить ТЕСТ для него

@Controller
public class HomeController {

    @GetMapping("/")
    public String homeInit(Locale locale, Model model) {
        return "home";
    }

    @RequestMapping("/favicon.ico")
    public String favicon() {
        System.out.println("FAVICON");
        return "forward:/resources/favicon.ico";
    }

    public static String getErrorMessage(Errors errors) {
        StringBuilder errorMessage = new StringBuilder("Some errors were caused: ");
        for (ObjectError err : errors.getAllErrors()) {
            errorMessage.append(err.getDefaultMessage()).append(" ");
        }
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
            System.err.println("!!!Error while handle request!!!\n" + error);
        }

        return resultEntity;
    }
}
