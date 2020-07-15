package shortener.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class ReferenceForm {

    @NotBlank(message = "Field 'fullRef' can`t be empty!")
    private String fullRef;

    @Min(value = 1, message = "Field 'userId' can`t be empty!")
    private long userId;

    public ReferenceForm() {
    }

    public ReferenceForm(String fullRef, long userId) {
        this.fullRef = fullRef;
        this.userId = userId;
    }

    public String getFullRef() {
        return fullRef;
    }

    public void setFullRef(String fullRef) {
        this.fullRef = fullRef;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
