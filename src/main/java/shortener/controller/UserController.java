package shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shortener.entity.BaseEntity;
import shortener.entity.RegistrationForm;
import shortener.entity.User;
import shortener.service.IUserService;

import javax.validation.Valid;

import static shortener.controller.HomeController.getErrorMessage;
import static shortener.controller.HomeController.handleErrors;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @ResponseBody
    @PostMapping(value = "/user")
    public ResponseEntity<String> registerUsers(@Valid @RequestBody RegistrationForm form, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getErrorMessage(errors) + " }");
        }

        User user = new User(form.getPassword(), form.getLogin(), form.getUsername(), form.getAdmin());

        User savedUser = (User) handleErrors((service, usr) -> service.save((BaseEntity) usr), userService, user);

        if (savedUser == null || savedUser.getUserId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Failure to register user!\" }");
        } else {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"Success! You are registered as \"" + savedUser.toString() + "}");
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
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " +  foundUser.toString() +" }");
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

        User foundUser = (User) handleErrors((service, id) -> service.findById((Long) id), userService, user.getUserId());

        if (foundUser == null || foundUser.getUserId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
        }

        User updatedUser = (User) handleErrors((service, passedUser) -> service.save((BaseEntity) passedUser), userService, user);
        if (updatedUser != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"User successfully updated! Updated user is \"" + updatedUser + " }");
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
        if (foundUser == null || foundUser.getUserId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
        }

        try {
            userService.delete(id);
        } catch (Exception ex) {
            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
            isError = true;
        }

        if (isError) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        } else {
            return ResponseEntity.ok().body("{ \"status\": \"Success\", \"data\": \"User successfully removed!\" }");
        }
    }
}
