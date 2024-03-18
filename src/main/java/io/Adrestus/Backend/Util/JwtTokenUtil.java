package io.Adrestus.Backend.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import java.util.function.Function;

@Service
public class JwtTokenUtil {

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

    public JwtTokenUtil() {
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

        return doGenerateToken(userDetails, claims, userDetails.getUsername());
    }

    private AuthenticationResponse doGenerateToken(UserDetails userDetails, Map<String, Object> claims, String subject) {
        Algorithm algorithm = Algorithm.HMAC512(this.secret);
        if (isTimer) {
            isTimer = false;
            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withSubject(subject)
                    .withIssuer(userDetails.getUsername())
                    .withClaim(claims.keySet().stream().findFirst().get(), userDetails.getAuthorities().stream().filter(a -> a.getAuthority().startsWith("ROLE_")).findFirst().orElseThrow().getAuthority())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs));
            return new AuthenticationResponse(jwtBuilder.sign(algorithm), jwtExpirationInMs);
        } else {
            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withSubject(subject)
                    .withIssuer(userDetails.getUsername())
                    .withClaim(claims.keySet().stream().findFirst().get(), userDetails.getAuthorities().stream().filter(a -> a.getAuthority().startsWith("ROLE_")).findFirst().orElseThrow().getAuthority())
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs));
            return new AuthenticationResponse(jwtBuilder.sign(algorithm), jwtExpirationInMs);
        }
    }

    public AuthenticationResponse doGenerateRefreshToken(Map<String, Object> extraClaims, String subject) {
        Algorithm algorithm = Algorithm.HMAC512(this.secret);
        if (isTimer) {
            isTimer = false;
            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withSubject(subject)
                    .withIssuer(subject)
                    .withClaim(extraClaims.entrySet().stream().findFirst().get().getKey(), true)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs));
            // extraClaims.forEach(jwtBuilder::withClaim);
            return new AuthenticationResponse(jwtBuilder.sign(algorithm), jwtExpirationInMs);
        } else {
            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withSubject(subject)
                    .withIssuer(subject)
                    .withClaim(extraClaims.entrySet().stream().findFirst().get().getKey(), true)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs));
            // extraClaims.forEach(jwtBuilder::withClaim);
            return new AuthenticationResponse(jwtBuilder.sign(algorithm), jwtExpirationInMs);
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
        Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    public DecodedJWT decodeJWT(String token) throws TokenExpiredException {
        Algorithm algorithm = Algorithm.HMAC512(this.secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public <T> T getClaim(String token, Function<Map<String, Claim>, T> claimsResolver) {
        Map<String, Claim> claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getUsername(String token) {
        return getClaim(token, claims -> claims.get("sub").asString());
    }

    public Date getExpiration(String token) {
        return getClaim(token, claims -> claims.get("exp").asDate());
    }

    public Map<String, Claim> getAllClaims(String token) {
        return decodeJWT(token).getClaims();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            return decodeJWT(token).getSubject().equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (TokenExpiredException ex) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return decodeJWT(token).getExpiresAt().before(new Date(System.currentTimeMillis()));
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }

    }
}
