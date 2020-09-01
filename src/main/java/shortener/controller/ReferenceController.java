package shortener.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shortener.entity.*;
import shortener.service.IReferenceService;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private IReferenceService referenceService;

    @GetMapping(value = "/smal.link/*")
    public String useShortRef(HttpServletRequest request) {

        String fullRef = null;

        if (request.getRequestURI().length() > 1) {
            Reference ref = referenceService.findByReducedRef(request.getRequestURI().substring(1));
            fullRef = ref.getfullRef();
        }

        if (fullRef != null && fullRef.length() > 7) {
            return "redirect:" + fullRef;
        } else {
            return "redirect:/error?Error_processing_reference";
        }
    }

    @ResponseBody
    @PostMapping(value = "/ref")
    public ResponseEntity<String> createReferences(@Valid @RequestBody ReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getErrorMessage(errors) + " }");
        }

        if (refForm.getUserId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"UserId can not be 0!\" }");
        }

        Pattern pattern = Pattern.compile("^(?:http(s)?:\\/\\/)[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#\\[\\]\\@!\\$\\&\\'\\(\\)\\*\\+,;=.]+$");
        Matcher matcher = pattern.matcher(refForm.getFullRef());

        if (!matcher.matches() || refForm.getFullRef().contains("smal.link")) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Full reference not valid!\" }");
        }

        String reducedRef = "smal.link/" + Objects.toString(Objects.hashCode(refForm.getFullRef()));
        Reference ref = new Reference(refForm.getFullRef(), reducedRef, 0, refForm.getUserId());

        Reference newRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (newRef != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + newRef + " }");
        } else {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Reference is not created!\" }");
        }
    }

    @ResponseBody
    @GetMapping(value = "/ref")
    public ResponseEntity<String> getReferences(@RequestParam(defaultValue = "-1") Long userId,
                                                @RequestParam(defaultValue = "-1") Long id,
                                                @RequestParam(defaultValue = "") String reducedRef) {
        try {
            if (id != -1) {
                Reference foundRef = (Reference) referenceService.findById(id);
                if (foundRef.getId() != 0) {
                    return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + foundRef + " }");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"Reference not found!\" }");
                }
            } if (userId != -1) {
                List<Reference> foundRefs = referenceService.findByUserId(userId);
                if (foundRefs != null) {
                    return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + foundRefs + " }");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"References not found!\" }");
                }
            } else if(!reducedRef.isEmpty()) {
                Reference foundRef = referenceService.findByReducedRef(reducedRef);
                if (foundRef.getId() != 0) {
                    return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + foundRef.getfullRef() + " }");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"Reference not found!\" }");
                }
            } else {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + referenceService.findAll() + " }");
            }
        } catch (Exception ex) {
            logger.error("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
        }
        return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
    }

    @ResponseBody
    @PutMapping(value = "/ref")
    public ResponseEntity<String> updateReferences(@Valid @RequestBody UpdateReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\":" + getErrorMessage(errors) + " }");
        }

        if (refForm.getRefId() == 0 ) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        }

        Reference ref = (Reference) handleErrors((service, id) -> service.findById((Long) id), referenceService, refForm.getRefId());
        if (ref == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"Reference not found!\" }");
        }

        String reducedRef = "smal.link/" + Objects.toString(Objects.hashCode(refForm.getFullRef()));
        ref.setfullRef(refForm.getFullRef());
        ref.setReducedRef(reducedRef);

        Reference savedRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (savedRef != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + savedRef + " }");
        } else {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Failure to update reference!\" }");
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/ref")
    public ResponseEntity<String> removeReferences(@RequestParam(defaultValue = "") String reducedRef) {
        boolean isError = false;

        if (reducedRef.isEmpty()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Required parameter reduced reference!\" }");
        }

        Reference ref = (Reference) handleErrors((service, redRef) -> ((IReferenceService)service).findByReducedRef((String) redRef),
                referenceService, reducedRef);
        if (ref.getId() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"Reference not found!\" }");
        }

        try {
            referenceService.delete(ref.getId());
        } catch (Exception ex) {
            logger.error("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
            isError = true;
        }

        if (isError) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        } else {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"Reference successfully removed!\" }");
        }
    }
}
