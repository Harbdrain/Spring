package com.danil.spring.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.danil.spring.model.UserRole;
import com.danil.spring.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        JwtAuthenticationToken resultToken = new JwtAuthenticationToken(authenticationToken.getToken());

        try {
            Claims claims = jwtService.getClaims(authenticationToken.getToken());
            resultToken.setPrincipal(claims.getSubject());

            List<GrantedAuthority> authorities = new ArrayList<>();
            UserRole.getRoleByName((String) claims.get("role")).getAuthorities()
                    .forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getAuthority())));
            resultToken.setAuthorities(authorities);

            if (claims.getExpiration().after(new Date())) {
                resultToken.setAuthenticated(true);
            }
        } catch (MalformedJwtException | SignatureException | ExpiredJwtException | IllegalArgumentException e) {
            return resultToken;
        }
        return resultToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }

}
