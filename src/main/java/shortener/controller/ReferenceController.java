package shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shortener.entity.*;
import shortener.service.IReferenceService;

import javax.validation.Valid;

import java.util.Objects;

import static shortener.controller.HomeController.getErrorMessage;
import static shortener.controller.HomeController.handleErrors;

@Controller
public class ReferenceController {

    @Autowired
    private IReferenceService referenceService;

    @ResponseBody
    @PostMapping(value = "/ref")
    public ResponseEntity<String> createReferences(@Valid @RequestBody ReferenceForm refForm, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": " + getErrorMessage(errors) + " }");
        }

        if (refForm.getUserId() == 0) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"UserId can not be 0!\" }");
        }

        String reducedRef = "smal.link/" + Objects.toString(Objects.hashCode(refForm.getFullRef()));
        Reference ref = new Reference(refForm.getFullRef(), reducedRef, 0, refForm.getUserId());

        Reference newRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (newRef != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"Your reduced reference is " + reducedRef + " }");
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
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + referenceService.findById(id).toString() + " }");
            } if (userId == -1) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + referenceService.findByUserId(userId).toString() + " }");
            } else if(!reducedRef.isEmpty()) {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + referenceService.findByReducedRef(reducedRef).getfullRef() + " }");
            } else {
                return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": " + referenceService.findAll() + " }");
            }
        } catch (Exception ex) {
            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
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
        ref.setReducedRef(reducedRef);

        Reference savedRef = (Reference) handleErrors((service, refer) -> service.save((BaseEntity) refer), referenceService, ref);

        if (savedRef != null) {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"Reference successfully updated! New reference is " + reducedRef + " }");
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
        if (ref == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"status\": \"Not found\", \"data\": \"Reference not found!\" }");
        }

        try {
            referenceService.delete(ref.getRefId());
        } catch (Exception ex) {
            System.err.println("!!!Error while handle request!!!\n" + ex.getCause().getCause().getMessage());
            isError = true;
        }

        if (isError) {
            return ResponseEntity.badRequest().body("{ \"status\": \"Bad request\", \"data\": \"Bad request!\" }");
        } else {
            return ResponseEntity.ok("{ \"status\": \"Success\", \"data\": \"Reference successfully removed!\" }");
        }
    }
}
