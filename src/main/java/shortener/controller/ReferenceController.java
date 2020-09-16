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
import shortener.entity.BaseEntity;
import shortener.entity.Reference;
import shortener.entity.ReferenceForm;
import shortener.entity.UpdateReferenceForm;
import shortener.service.IReferenceService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static shortener.controller.HomeController.getExceptionMessage;

@Controller
public class ReferenceController {

    private static final Logger logger = LogManager.getLogger(ReferenceController.class);

    private final Pattern pattern = Pattern.compile("^(?:http(s)?:\\/\\/)[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#\\[\\]\\@!\\$\\&\\'\\(\\)\\*\\+,;=.]+$");

    @Autowired
    private IReferenceService referenceService;

    @GetMapping(value = "/small.link/*")
    public String useShortRef(HttpServletRequest request) {

        if (request.getRequestURI().length() <= 1) {
            return "redirect:/error?Error_processing_reference";
        }

        String fullRef = referenceService.getFullRef(request.getRequestURI().substring(1));

        if (fullRef != null && fullRef.length() > 7) {
            return "redirect:" + fullRef;
        } else {
            return "redirect:/error?Full_reference_not_found";
        }
    }

    @ResponseBody
    @PostMapping(value = "/ref", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> createReferences(@Valid @RequestBody ReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": " + getExceptionMessage(errors) + " }");
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

        try {
            Reference newRef = referenceService.createRef(refForm.getUserId(), refForm.getFullRef());
            if (newRef != null) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + newRef + " }");
            } else {
                return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                        "\"data\": \"Reference is not created!\" }");
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle POST \"/ref\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle POST \"/ref\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Error while handle request!\" }");
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
                List<BaseEntity> references = referenceService.findAll();
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + references + " }");
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle GET \"/ref\" with userId==" + userId + " " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle GET \"/ref\" with userId==" + userId + " " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }
    }

    @ResponseBody
    @PutMapping(value = "/ref", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> updateReferences(@Valid @RequestBody UpdateReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\":" + getExceptionMessage(errors) + " }");
        }

        if (refForm.getRefId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }

        Matcher matcher = pattern.matcher(refForm.getFullRef());
        if (!matcher.matches() || refForm.getFullRef().contains("small.link")) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Full reference not valid!\" }");
        }

        try {
            Reference updatedReference = referenceService.updateReference(refForm.getRefId(), refForm.getFullRef());
            if (updatedReference != null) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + updatedReference + " }");
            } else {
                return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                        "\"data\": \"Failure to update reference!\" }");
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle PUT \"/ref\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle PUT \"/ref\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Error while handle request!\" }");
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/ref", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> removeReferences(@RequestParam(defaultValue = "") String reducedRef) {

        if (reducedRef.isEmpty()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Required parameter \"reducedRef\" is empty!\" }");
        }

        try {
            boolean isDeleted = referenceService.deleteReference(reducedRef);
            if (isDeleted) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"Reference successfully removed!\" }");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\"," +
                        " \"data\": \"Reference not found!\" }");
            }
        } catch (AccessDeniedException ex) {
            String error = getExceptionMessage(ex);
            logger.error("Access denied while handle DELETE \"/ref\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Access denied\", \"data\": \"You don`t have permission to perform the operation!\" }");
        } catch (Exception ex) {
            String error = getExceptionMessage(ex);
            logger.error("Error while handle DELETE \"/ref\" " + error);
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", " +
                    "\"data\": \"Error while handle request!\" }");
        }
    }
}
