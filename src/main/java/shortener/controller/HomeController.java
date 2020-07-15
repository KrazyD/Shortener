package shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import shortener.entity.*;
import shortener.service.*;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Objects;

// TODO прикрутить валидацию к @ResponseBody
// TODO дописать тесты в Postman
// TODO разделить на 2 контроллера
// TODO сделать возвращемое значение у репозиториев Optional<>
// TODO проверить все методы и их валидацию
// TODO прикрутить security, и чтобы у админов была отдельная страничка с возможностью редактировать все
// TODO починить favicon
// TODO инкремент обращений к ссылке
// TODO метод получения длинной по короткой

@Controller
public class HomeController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IReferenceService referenceService;

    @GetMapping("/")
    public String homeInit(Locale locale, Model model) {
        return "home";
    }

    @ResponseBody
    @PostMapping(value = "/user")
    public ResponseEntity<String> registerUsers(@Valid @RequestBody RegistrationForm form, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessage(errors));
        }

        User user = new User(form.getPassword(), form.getLogin(), form.getUsername(), form.getAdmin());

        User savedUser = (User) handleErrors((service, usr) -> service.save((BaseEntity) usr), userService, user);

        if (savedUser == null || savedUser.getUserId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failure to register user!");
        } else {
            return ResponseEntity.ok("Success! You are registered as " + savedUser.toString());
        }
    }

    @ResponseBody
    @GetMapping(value = "/user")
    public ResponseEntity<String> getUser(@RequestParam(defaultValue = "-1") Long id) {
        if (id == -1) {
            return ResponseEntity.ok(userService.findAll());
        } else {
            User foundUser = (User) handleErrors((service, user) -> service.findById(id), userService, id);
            if (foundUser != null) {
                return ResponseEntity.ok(foundUser.toString());
            } else {
                return ResponseEntity.badRequest().body("User not found!");
            }
        }
    }

    @ResponseBody
    @PutMapping(value = "/user")
    public ResponseEntity<String> modifyUser(@Valid @RequestBody User user, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessage(errors));
        }

        if (user == null) {
            return ResponseEntity.badRequest().body("Bad request!");
        }

        User foundUser = (User) handleErrors((service, id) -> service.findById((Long) id), userService, user.getUserId());

        if (foundUser == null || foundUser.getUserId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        User updatedUser = (User) handleErrors((service, passedUser) -> service.save((BaseEntity) passedUser), userService, user);
        if (updatedUser != null) {
            return ResponseEntity.ok("User successfully updated! Updated user is " + updatedUser);
        } else {
            return ResponseEntity.badRequest().body("Failure to update user.");
        }

    }

    @ResponseBody
    @DeleteMapping(value = "/user")
    public ResponseEntity<String> removeUser(@RequestParam(defaultValue = "-1") Long id) {
        if (id == -1) {
            return ResponseEntity.badRequest().body("Required parameter userId!");
        }

        boolean isError = false;

        User foundUser = (User) handleErrors((service, user) -> service.findById(id), userService, id);
        if (foundUser == null || foundUser.getUserId() == 0) {
             return ResponseEntity.badRequest().body("User not found!");
        }

        try {
            userService.delete(id);
        } catch (Exception ex) {
            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
            isError = true;
        }

        if (isError) {
            return ResponseEntity.badRequest().body("Bad request!");
        } else {
            return ResponseEntity.ok().body("User successfully removed!");
        }
    }

    @ResponseBody
    @PostMapping(value = "/ref")
    public ResponseEntity<String> createReferences(@Valid @RequestBody ReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessage(errors));
        }

        if (refForm.getUserId() == 0) {
            return ResponseEntity.badRequest().body("Bad request!");
        }

        String reducedRef = "smal.link/" + Objects.toString(Objects.hashCode(refForm.getFullRef()));
        Reference ref = new Reference(refForm.getFullRef(), reducedRef, 0, refForm.getUserId());

        Reference newRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (newRef != null) {
            return ResponseEntity.ok("Success! Your reduced reference is " + reducedRef);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failure!");
        }
    }

    @ResponseBody
    @GetMapping(value = "/ref")
    public ResponseEntity<String> getReferences(@RequestParam(defaultValue = "-1") Long userId, @RequestParam(defaultValue = "-1") Long id) {
        try {
            if (id == -1 && userId != -1) {
                return ResponseEntity.ok(referenceService.findByUserId(userId).toString());
            } if (id != -1 && userId == -1) {
                return ResponseEntity.ok(referenceService.findById(id).toString());
            } else {
                return ResponseEntity.ok(referenceService.findAll());
            }
        } catch (Exception ex) {
            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
        }
        return ResponseEntity.badRequest().body("Bad request!");
    }

    @ResponseBody
    @PutMapping(value = "/ref")
    public ResponseEntity<String> updateReferences(@Valid @RequestBody UpdateReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessage(errors));
        }

        if (refForm.getRefId() == 0 ) {
            return ResponseEntity.badRequest().body("Bad request!");
        }

        Reference ref = (Reference) handleErrors((service, id) -> service.findById((Long) id), referenceService, refForm.getRefId());
        if (ref == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reference not found!");
        }

        String reducedRef = "smal.link/" + Objects.toString(Objects.hashCode(refForm.getFullRef()));
        ref.setReducedRef(reducedRef);

        Reference savedRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (savedRef != null) {
            return ResponseEntity.ok("Reference successfully updated! New reference is " + reducedRef);
        } else {
            return ResponseEntity.badRequest().body("Failure to update reference.");
        }

    }

    @ResponseBody
    @DeleteMapping(value = "/ref")
    public ResponseEntity<String> removeReferences(@RequestParam(defaultValue = "") String reducedRef) {

        boolean isError = false;

        if (reducedRef.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required parameter reduced reference!");
        }

        // TODO тут обработку ошибок
        long refId = referenceService.findByReducedRef(reducedRef).getRefId();
        if (refId == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad reduced reference!");
        }

        try {
            referenceService.delete(refId);
        } catch (Exception ex) {
            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
            isError = true;
        }

        // TODO сделать обработку ошибок в DELETE и в GET по такому принципу
        if (isError) {
            return ResponseEntity.badRequest().body("Bad request!");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Reference successfully removed!");
        }
    }

    private String getErrorMessage(Errors errors) {
        StringBuilder errorMessage = new StringBuilder("Some errors were caused: ");
        for (ObjectError err : errors.getAllErrors()) {
            errorMessage.append(err.getDefaultMessage()).append(" ");
        }
        return errorMessage.toString();
    }

    private BaseEntity handleErrors(TwoParamsFunction<BaseService, Object, BaseEntity> func, BaseService service, Object param) {
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
