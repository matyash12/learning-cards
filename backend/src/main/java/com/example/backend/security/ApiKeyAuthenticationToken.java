package com.example.backend.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private String apiKey;
    private String name;
    public ApiKeyAuthenticationToken(String apiKey,String name, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.name = name;
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}