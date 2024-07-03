package nl.rgs.kib.shared.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

/*
All Audit annotations: @CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy
are not working because only work in the root document, not in embedded documents.
In order to make it work there is a AuditMetadataEntityListener class that listens to the
lifecycle events of the entity and sets the audit fields in the embedded document.
 */
@Data()
@AllArgsConstructor()
@NoArgsConstructor()
public class AuditMetadata {
    @CreatedDate()
    public LocalDateTime created;

    @CreatedBy()
    public String createdBy;

    @LastModifiedDate()
    public LocalDateTime updated;

    @LastModifiedBy()
    public String updatedBy;
}