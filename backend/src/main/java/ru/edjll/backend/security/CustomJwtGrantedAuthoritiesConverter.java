package ru.edjll.backend.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String AUTHORITY_PREFIX = "ROLE_";
    private static final String RESOURCE_NAME = "spring-boot";

    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList();

        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
        Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS);

        if (realmAccess != null) {
            grantedAuthorities.addAll(getRoles(realmAccess));
        }

        if (resourceAccess != null) {
            Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(this.RESOURCE_NAME);
            if (resource != null) {
                grantedAuthorities.addAll(getRoles(resource));
            }
        }

        return grantedAuthorities;
    }

    private Collection<GrantedAuthority> getRoles(Map<String, Object> data) {
        Collection<String> roles = (Collection<String>) data.get("roles");
        if (roles != null) {
            return roles.stream()
                            .map(role -> new SimpleGrantedAuthority(this.AUTHORITY_PREFIX + role))
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
