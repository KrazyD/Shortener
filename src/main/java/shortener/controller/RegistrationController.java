package shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import shortener.entity.RegistrationForm;
import shortener.entity.User;
import shortener.service.IUserService;

import javax.validation.Valid;

import static shortener.controller.HomeController.getExceptionMessage;

@Controller
public class RegistrationController {

    private static final Logger logger = LogManager.getLogger(RegistrationController.class);

    @Autowired
    private IUserService userService;

    @ResponseBody
    @PostMapping(value = "/register", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationForm form, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " +
                    getExceptionMessage(errors) + " }");
        }

        try {
            User registeredUser = userService.registerUser(form);
            if (registeredUser != null) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + registeredUser + " }");
            } else {
                return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                        "\"data\": \"Failure to register user!\" }");
            }
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle POST \"/register\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }
    }
}
