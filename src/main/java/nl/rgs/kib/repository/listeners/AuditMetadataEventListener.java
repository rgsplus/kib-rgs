package nl.rgs.kib.repository.listeners;

import nl.rgs.kib.shared.models.AuditMetadata;
import nl.rgs.kib.shared.models.BaseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditMetadataEventListener<T extends BaseObject> extends AbstractMongoEventListener<T> {

    @Autowired
    private AuditorAware<String> auditorAware;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<T> event) {
        AuditMetadata metadata = event.getSource().getMetadata();

        if (metadata == null) {
            metadata = new AuditMetadata();
            event.getSource().setMetadata(metadata);
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