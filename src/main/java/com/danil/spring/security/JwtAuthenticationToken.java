package com.danil.spring.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtAuthenticationToken implements Authentication {
    private String token;
    private String principal;
    private Collection<GrantedAuthority> authorities;
    private boolean authenticated = false;

    public JwtAuthenticationToken(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return this.principal;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    public void setPrincipal(String username) {
        this.principal = username;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    public String getToken() {
        return token;
    }
}
