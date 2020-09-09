package shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shortener.entity.*;
import shortener.service.IReferenceService;
import shortener.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static shortener.controller.HomeController.getErrorMessage;
import static shortener.controller.HomeController.handleErrors;

@Controller
public class ReferenceController {

    private static final Logger logger = LogManager.getLogger(ReferenceController.class);

    private final Pattern pattern = Pattern.compile("^(?:http(s)?:\\/\\/)[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#\\[\\]\\@!\\$\\&\\'\\(\\)\\*\\+,;=.]+$");

    @Autowired
    private IReferenceService referenceService;

    @Autowired
    private IUserService userService;

    @GetMapping(value = "/small.link/*")
    public String useShortRef(HttpServletRequest request, HttpServletResponse response) {

        if (request.getRequestURI().length() <= 1) {
            return "redirect:/error?Error_processing_reference.";
        }

        Reference ref = referenceService.findByReducedRef(request.getRequestURI().substring(1));

        if (ref != null && ref.getFullRef() != null && ref.getFullRef().length() > 7) {
            String fullRef = ref.getFullRef();

            ref.setRequestsNumb(ref.getRequestsNumb() + 1);

            referenceService.save(ref);

            return "redirect:" + fullRef;
        } else {
            return "redirect:/error?Error_processing_reference";
        }
    }

    @ResponseBody
    @PostMapping(value = "/ref", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> createReferences(@Valid @RequestBody ReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": " + getErrorMessage(errors) + " }");
        }

        if (refForm.getUserId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"UserId can not be 0!\" }");
        }

        Matcher matcher = pattern.matcher(refForm.getFullRef());
        if (!matcher.matches() || refForm.getFullRef().contains("small.link")) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Full reference not valid!\" }");
        }

        String reducedRef = "small.link/" + Objects.toString(Objects.hashCode(refForm.getFullRef()));
        Reference ref = new Reference(refForm.getFullRef(), reducedRef, 0, refForm.getUserId());

        Reference newRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (newRef != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + newRef + " }");
        } else {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Reference is not created!\" }");
        }
    }

    @ResponseBody
    @GetMapping(value = "/ref", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> getReferences(@RequestParam(defaultValue = "-1") Long userId) {
        try {
            if (userId != -1) {
                List<Reference> foundRefs = referenceService.findByUserId(userId);
                if (foundRefs != null) {
                    return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + foundRefs + " }");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", " +
                            "\"data\": \"References not found!\" }");
                }
            } else {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                logger.info("!!!!!!!!!!!!!!!!!! auth=" + auth);
                if (auth.isAuthenticated() && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + referenceService.findAll() + " }");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", " +
                            "\"data\": \"References not found!\" }");
                }
            }
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
        }
        return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
    }

    @ResponseBody
    @PutMapping(value = "/ref", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> updateReferences(@Valid @RequestBody UpdateReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\":" + getErrorMessage(errors) + " }");
        }

        if (refForm.getRefId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }

        Matcher matcher = pattern.matcher(refForm.getFullRef());
        if (!matcher.matches() || refForm.getFullRef().contains("small.link")) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Full reference not valid!\" }");
        }

        Reference ref = (Reference) handleErrors((service, id) -> service.findById((Long) id), referenceService, refForm.getRefId());
        if (ref == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", " +
                    "\"data\": \"Reference not found!\" }");
        }

        String reducedRef = "small.link/" + Objects.toString(Objects.hashCode(refForm.getFullRef()));
        ref.setFullRef(refForm.getFullRef());
        ref.setReducedRef(reducedRef);

        Reference savedRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (savedRef != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + savedRef + " }");
        } else {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Failure to update reference!\" }");
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/ref", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> removeReferences(@RequestParam(defaultValue = "") String reducedRef) {

        if (reducedRef.isEmpty()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Required parameter reduced reference!\" }");
        }

        Reference ref = (Reference) handleErrors((service, redRef) -> ((IReferenceService) service).findByReducedRef((String) redRef),
                referenceService, reducedRef);
        if (ref.getId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\"," +
                    " \"data\": \"Reference not found!\" }");
        }


        if (isCurrentUserOwner(ref.getUserId()) || isCurrentUserAdmin()) {
            try {
                referenceService.delete(ref.getId());
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
                return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
            }

            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"Reference successfully removed!\" }");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{ \"status\": \"Unauthorized\", " +
                    "\"data\": \"Authorization required!\" }");
        }
    }

    private boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private boolean isCurrentUserOwner(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.isAuthenticated()) {
            return false;
        }

        User foundUser = (User) handleErrors((service, id) -> service.findById((Long) id), userService, userId);

        return Objects.equals(auth.getName(), foundUser.getLogin());
    }
}
