package shortener.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "reference", schema = "public")
public class Reference implements BaseEntity, Serializable {

    private static final long serialVersionUID = -7539517893214297888L;

    public Reference() {}

    public Reference(String fullRef, String reducedRef, int requestsNumb, long userId) {
        this.fullRef = fullRef;
        this.reducedRef = reducedRef;
        this.requestsNumb = requestsNumb;
        this.userId = userId;
    }

    @Id
//    @Min(value = 1, message = "Field 'refId' can`t be empty!")
    @Column(name = "\"refId\"")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long refId;

    @NotBlank(message = "Field 'fullRef' can`t be empty!")
    @Column(name = "\"fullRef\"")
    private String fullRef;

    @NotBlank(message = "Field 'reducedRef' can`t be empty!")
    @Column(name = "\"reducedRef\"")
    private String reducedRef;

    @Min(value = 0, message = "Field 'requestsNumb' can`t be empty!")
    @Column(name = "\"requestsNumb\"")
    private int requestsNumb;

    @Min(value = 1, message = "Field 'userId' can`t be empty!")
    @Column(name = "\"userId\"")
    private long userId;

    @ManyToOne
    @JoinColumn(name = "\"userId\"", nullable = false, insertable = false, updatable = false)
    private User user;

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }

    public String getfullRef() {
        return fullRef;
    }

    public void setfullRef(String fullRef) {
        this.fullRef = fullRef;
    }

    public String getreducedRef() {
        return reducedRef;
    }

    public void setReducedRef(String reducedRef) {
        this.reducedRef = reducedRef;
    }

    public int getrequestsNumb() {
        return requestsNumb;
    }

    public void setrequestsNumb(int requestsNumb) {
        this.requestsNumb = requestsNumb;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reference)) return false;
        Reference reference = (Reference) o;
        return refId == reference.refId &&
                requestsNumb == reference.requestsNumb &&
                userId == reference.userId &&
                fullRef.equals(reference.fullRef) &&
                reducedRef.equals(reference.reducedRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refId, fullRef, reducedRef, requestsNumb, userId);
    }

    @Override
    public String toString() {
        return "{" +
                "\"refId\":" + refId +
                ", \"fullRef\":\"" + fullRef + '\"' +
                ", \"reducedRef\":\"" + reducedRef + '\"' +
                ", \"requestsNumb\":" + requestsNumb +
                ", \"userId\":" + userId +
                "}";
    }
}
