package com.danil.spring.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.danil.spring.model.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        JwtAuthenticationToken resultToken = new JwtAuthenticationToken(authenticationToken.getToken());

        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build();
            Claims claims = (Claims) parser.parse(authenticationToken.getToken()).getBody();
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
