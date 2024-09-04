package nl.rgs.kib.security;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public static final String REALM_ACCESS = "realm_access";
    public static final String ROLES = "roles";
    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(final Jwt jwt) {
        List<GrantedAuthority> authorityList = new LinkedList<>();

        if (jwt.getClaims().get(REALM_ACCESS) instanceof LinkedTreeMap) {
            @SuppressWarnings("unchecked") final LinkedTreeMap<String, ArrayList<String>> realmAccess = (LinkedTreeMap<String, ArrayList<String>>) jwt.getClaims().get(REALM_ACCESS);
            if (realmAccess != null) {
                List<SimpleGrantedAuthority> realmRoles = realmAccess.get(ROLES).stream()
                        .map(roleName -> ROLE_PREFIX + roleName.toUpperCase())
                        .map(SimpleGrantedAuthority::new).toList();
                authorityList.addAll(realmRoles);
            }
        }

        return authorityList;
    }
}
