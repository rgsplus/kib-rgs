package nl.rgs.kib.config;

import nl.rgs.kib.security.OAuth2ClientSecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityInitializer() {
        super(OAuth2ClientSecurityConfig.class);
    }

}