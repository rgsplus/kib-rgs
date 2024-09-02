package nl.rgs.kib.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityContextUtils {

    private static final String ANONYMOUS = "anonymous";

    private SecurityContextUtils() {
    }

    public static String getUserName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = ANONYMOUS;

        if (null != authentication) {
            if (authentication.getPrincipal() instanceof Jwt jwt) {
                username = jwt.getClaim("preferred_username");

            } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
                username = springSecurityUser.getUsername();

            } else if (authentication.getPrincipal() instanceof String) {
                username = (String) authentication.getPrincipal();

            } else {
                log.debug("User details not found in Security Context");
            }
        } else {
            log.debug("Request not authenticated, hence no user name available");
        }

        return username;
    }
}
