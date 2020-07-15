package shortener.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateReferenceForm {

    @Min(value = 1, message = "Field 'refId' can`t be empty!")
    private long refId;

    @NotBlank(message = "Field 'fullRef' can`t be empty!")
    private String fullRef;

    public UpdateReferenceForm() {
    }

    public UpdateReferenceForm(long refId, String fullRef) {
        this.refId = refId;
        this.fullRef = fullRef;
    }

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }

    public String getFullRef() {
        return fullRef;
    }

    public void setFullRef(String fullRef) {
        this.fullRef = fullRef;
    }
}
