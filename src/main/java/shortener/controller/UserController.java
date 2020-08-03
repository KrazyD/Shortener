package shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shortener.entity.BaseEntity;
import shortener.entity.LoginForm;
import shortener.entity.RegistrationForm;
import shortener.entity.User;
import shortener.service.IUserService;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static shortener.controller.HomeController.getErrorMessage;
import static shortener.controller.HomeController.handleErrors;

@Controller
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @ResponseBody
    @PostMapping(value = "/user")
    public ResponseEntity<String> registerUsers(@Valid @RequestBody RegistrationForm form, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getErrorMessage(errors) + " }");
        }

        User user = new User(form.getPassword(), form.getLogin(), form.getUsername(), form.getRoles());

        User savedUser = (User) handleErrors((service, usr) -> service.save((BaseEntity) usr), userService, user);

        if (savedUser.getId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Failure to register user!\" }");
        } else {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + savedUser + " }");
        }
    }

    @ResponseBody
    @GetMapping(value = "/user")
    public ResponseEntity<String> getUser(@RequestParam(defaultValue = "-1") Long id) {
        if (id == -1) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " +  userService.findAll() +" }");
        } else {
            User foundUser = (User) handleErrors((service, user) -> service.findById(id), userService, id);
            if (foundUser != null) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " +  foundUser +" }");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
            }
        }
    }

    @ResponseBody
    @PutMapping(value = "/user")
    public ResponseEntity<String> modifyUser(@Valid @RequestBody User user, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getErrorMessage(errors) + " }");
        }

        if (user == null) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"User is not present!\" }");
        }

        User foundUser = (User) handleErrors((service, id) -> service.findById((Long) id), userService, user.getId());

        if (foundUser == null || foundUser.getId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
        }

        User updatedUser = (User) handleErrors((service, passedUser) -> service.save((BaseEntity) passedUser), userService, user);
        if (updatedUser != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + updatedUser + " }");
        } else {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Failure to update user!\" }");
        }

    }

    @ResponseBody
    @DeleteMapping(value = "/user")
    public ResponseEntity<String> removeUser(@RequestParam(defaultValue = "-1") Long id) {
        boolean isError = false;

        if (id == -1) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Required parameter userId!\" }");
        }

        User foundUser = (User) handleErrors((service, user) -> service.findById(id), userService, id);
        if (foundUser == null || foundUser.getId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
        }

        try {
            userService.delete(id);
        } catch (Exception ex) {
            logger.error("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
//            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
            isError = true;
        }

        if (isError) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        } else {
            return ResponseEntity.ok().body("{ \"status\": \"Success\", \"data\": \"User successfully removed!\" }");
        }
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginForm form, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getErrorMessage(errors) + " }");
        }

        if (form == null) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Data is not present!\" }");
        }

        User foundUser = null;

        boolean isError = false;
        try {
            foundUser = userService.getLoggedInUser(form.getLogin(), form.getPassword());
        } catch (Exception ex) {
            logger.error("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
//            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
            isError = true;
        }

        if (isError || foundUser.getId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        } else {
            return ResponseEntity.ok().body("{ \"status\": \"Success\", \"data\":" + foundUser + " }");
        }

    }
}
