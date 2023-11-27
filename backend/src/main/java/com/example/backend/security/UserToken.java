package com.example.backend.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.example.backend.security.user.UserEntity;

public class UserToken extends AbstractAuthenticationToken {
    private String username;
    private UserEntity userEntity;

    public UserEntity getUserEntity() {
        return this.userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public UserToken(String username, UserEntity userEntity, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        this.userEntity = userEntity;
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
    public Object getPrincipal() {
        return username;
    }
}