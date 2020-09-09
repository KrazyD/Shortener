package shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shortener.entity.BaseEntity;
import shortener.entity.User;
import shortener.service.IUserService;
import shortener.service.UserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static shortener.controller.HomeController.getErrorMessage;
import static shortener.controller.HomeController.handleErrors;

@Controller
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @ResponseBody
    @GetMapping(value = "/user", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> getUser(@RequestParam(defaultValue = "-1") Long id) {
        if (id == -1) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " +  userService.findAll() +" }");
        } else {
            User foundUser = (User) handleErrors((service, userId) -> service.findById((Long) userId), userService, id);
            if (foundUser != null) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " +  foundUser +" }");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
            }
        }
    }

    @ResponseBody
    @PutMapping(value = "/user", produces = "application/json; charset=utf-8")
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
        if (updatedUser == null) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Failure to update user!\" }");
        } else {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + updatedUser + " }");
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/user", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> removeUser(@RequestParam(defaultValue = "-1") Long id) {
        boolean isError = false;

        if (id == -1) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Required parameter userId!\" }");
        }

        User foundUser = (User) handleErrors((service, userId) -> service.findById((Long) userId), userService, id);
        if (foundUser == null || foundUser.getId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
        }

        try {
            userService.delete(id);
        } catch (Exception ex) {
            String error;
            if (ex.getCause() == null) {
                error = ex.getMessage();
            } else if(ex.getCause().getCause() == null) {
                error = ex.getCause().getMessage();
            } else {
                error = ex.getCause().getCause().getMessage();
            }
            logger.error("!!!Error while handle request!!!\n" + error);
            isError = true;
        }

        if (isError) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"message\": \"Bad request!\" }");
        } else {
            return ResponseEntity.ok().body("{ \"status\": \"Success\", \"data\": \"User successfully removed!\" }");
        }
    }

    @ResponseBody
    @GetMapping(value = "/login", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            String username = authentication.getName();

            String roles = authentication.getAuthorities().stream()
                    .map((auth -> "\"" + auth + "\""))
                    .collect(Collectors.joining(","));

            User foundUser = userService.findByLogin(username);

            return ResponseEntity.ok().body("{ \"status\": \"Success\", \"data\": {" + "\"id\":\"" +
                    foundUser.getId() + "\",\"username\":\"" +
                    username + "\",\"roles\": [" + roles + "] } }");
        } else {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"User is not authenticated!\" }");
        }
    }
}
