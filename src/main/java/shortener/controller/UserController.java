package shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shortener.entity.User;
import shortener.service.IUserService;

import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static shortener.controller.HomeController.getExceptionMessage;

@Controller
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @ResponseBody
    @GetMapping(value = "/user", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> getUser(@RequestParam(defaultValue = "-1") Long id) {
        try {
            if (id == -1) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + userService.findAll() + " }");
            } else {
                User foundUser = userService.findById(id);
                if (foundUser != null) {
                    return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + foundUser + " }");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"User not found!\" }");
                }
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle GET \"/user\" with refId==" + id + " " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle GET \"/user\" with refId==" + id + " " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }
    }

    @ResponseBody
    @PutMapping(value = "/user", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> modifyUser(@Valid @RequestBody User user, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getExceptionMessage(errors) + " }");
        }

        if (user == null) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"User is not present!\" }");
        }

        try {
            User updatedUser = userService.updateUser(user);
            if (updatedUser == null) {
                return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Failure to update user!\" }");
            } else {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + updatedUser + " }");
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle PUT \"/user\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle PUT \"/user\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/user", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> removeUser(@RequestParam(defaultValue = "-1") Long id) {

        if (id == -1) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Required parameter userId!\" }");
        }

        try {
            boolean isUserDeleted = userService.deleteUser(id);
            if (isUserDeleted) {
                return ResponseEntity.ok().body("{ \"status\": \"Success\", \"data\": \"User successfully removed!\" }");
            } else {
                return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"message\": \"User is not deleted!\" }");
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle DELETE \"/user\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle DELETE \"/user\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }
    }

    @ResponseBody
    @GetMapping(value = "/login", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> getCurrentUser() {

        try {
            User currentUser = userService.getCurrentAuthorizedUser();

            if (currentUser == null) {
                return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"User is not authenticated!\" }");
            } else {
                String roles = Stream.of(currentUser.getRoles())
                        .map((auth -> "\"" + auth + "\""))
                        .collect(Collectors.joining(","));
                return ResponseEntity.ok().body("{ \"status\": \"Success\", \"data\": {" + "\"id\":\"" +
                        currentUser.getId() + "\",\"username\":\"" +
                        currentUser.getLogin() + "\",\"roles\": [" + roles + "] } }");
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle GET \"/login\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle GET \"/login\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }
    }
}
