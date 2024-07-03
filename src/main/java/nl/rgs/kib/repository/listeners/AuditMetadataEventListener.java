package nl.rgs.kib.repository.listeners;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.shared.models.AuditMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component()
public class AuditMetadataEventListener extends AbstractMongoEventListener<Object> {

    @Autowired()
    private AuditorAware<String> auditorAware;

    @Override()
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        InspectionMethod inspectionMethod = (InspectionMethod) source;
        AuditMetadata metadata = inspectionMethod.getMetadata();

        if (metadata == null) {
            metadata = new AuditMetadata();
            inspectionMethod.setMetadata(metadata);
        }

        String currentAuditor = auditorAware.getCurrentAuditor().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (metadata.getCreated() == null) {
            metadata.setCreated(now);
            metadata.setCreatedBy(currentAuditor);
        }

        metadata.setUpdated(now);
        metadata.setUpdatedBy(currentAuditor);
    }
}