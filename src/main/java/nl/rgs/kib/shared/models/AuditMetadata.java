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

Based in the documentation of Spring (https://docs.spring.io/spring-data/jpa/reference/data-commons/auditing.html).
Should be possible to use the @EntityListeners annotation in the embedded document to make it work: `
    Auditing metadata does not necessarily need to live in the root level entity but can be added to an
    embedded one (depending on the actual store in use), as shown in the snippet below.
`.

But it doesn't work on MongoDB as the documentation says as other users have reported in this stack overflow question
(https://stackoverflow.com/questions/37324188/spring-data-mongodb-auditing-doesnt-work-for-embedded-documents)
 */

/**
 * Metadata for auditing purposes.
 * Contains the created and updated date and the user that created and updated the object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditMetadata {
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updated;

    @LastModifiedBy
    private String updatedBy;
    
    @CreatedDate
    private LocalDateTime created;
}