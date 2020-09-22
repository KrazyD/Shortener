package shortener.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class UpdateReferenceForm {

    @Min(value = 0, message = "Field 'refId' can`t be empty!")
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

    @Override
    public String toString() {
        return "UpdateReferenceForm{" +
                "refId=" + refId +
                ", fullRef='" + fullRef + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateReferenceForm)) return false;
        UpdateReferenceForm that = (UpdateReferenceForm) o;
        return refId == that.refId &&
                fullRef.equals(that.fullRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refId, fullRef);
    }
}
