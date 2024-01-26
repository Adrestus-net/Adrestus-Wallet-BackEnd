package io.Adrestus.Backend.Util;

import io.Adrestus.Backend.payload.response.AuthenticationResponse;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {

    private boolean isTimer;
    private String secret;
    private int jwtExpirationInMs;
    private int refreshExpirationDateInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    public JwtUtil() {
        isTimer = true;
    }

    public AuthenticationResponse generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private AuthenticationResponse doGenerateToken(Map<String, Object> claims, String subject) {
        if (isTimer) {
            isTimer = false;
            Date expiration = new Date(System.currentTimeMillis() + 1500);
            JwtBuilder builder = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS512, secret);
            return new AuthenticationResponse(builder.compact(), jwtExpirationInMs);
        } else {
            Date expiration = new Date(System.currentTimeMillis() + jwtExpirationInMs);
            JwtBuilder builder = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS512, secret);
            return new AuthenticationResponse(builder.compact(), jwtExpirationInMs);
        }
    }

    public AuthenticationResponse doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        if (isTimer) {
            isTimer = false;
            Date expiration = new Date(System.currentTimeMillis() + 1500);
            JwtBuilder builder = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS512, secret);
            return new AuthenticationResponse(builder.compact(), jwtExpirationInMs);
        } else {
            Date expiration = new Date(System.currentTimeMillis() + jwtExpirationInMs);
            JwtBuilder builder = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS512, secret);
            return new AuthenticationResponse(builder.compact(), jwtExpirationInMs);
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }
}
