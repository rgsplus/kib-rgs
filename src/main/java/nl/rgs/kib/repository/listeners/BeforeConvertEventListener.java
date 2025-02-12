package nl.rgs.kib.repository.listeners;

import nl.rgs.kib.shared.models.AuditMetadata;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.models.Sortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BeforeConvertEventListener<T extends BaseObject> extends AbstractMongoEventListener<T> {

    @Autowired
    private AuditorAware<String> auditorAware;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<T> event) {
        T object = event.getSource();
        handleSorting(object);
        handleMetadata(object);
    }

    private void handleSorting(T object) {
        if (object instanceof Sortable sortable) {
            sortable.applySort();
        }
    }

    private void handleMetadata(T object) {
        AuditMetadata metadata = object.getMetadata();

        if (metadata == null) {
            metadata = new AuditMetadata();
            object.setMetadata(metadata);
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