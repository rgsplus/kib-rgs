package nl.rgs.kib.config;

import nl.rgs.kib.security.SecurityContextUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityContextUtils.getUserName());
    }
}