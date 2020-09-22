package shortener.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class ReferenceForm {

    @NotBlank(message = "Field 'fullRef' can`t be empty!")
    private String fullRef;

    @Min(value = 0, message = "Field 'userId' can`t be empty!")
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

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Error parse ReferenceFrom to JSON!");
            e.printStackTrace();
            return "{}";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceForm)) return false;
        ReferenceForm form = (ReferenceForm) o;
        return userId == form.userId &&
                fullRef.equals(form.fullRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullRef, userId);
    }
}
