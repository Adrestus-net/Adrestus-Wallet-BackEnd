package io.Adrestus.Backend.Config;

import io.Adrestus.Backend.Service.JwtUserDetailsService;
import io.Adrestus.Backend.Util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            // JWT Token is in the form "Bearer token". Remove Bearer word and
            // get  only the Token
            String jwtToken = extractJwtFromRequest(request);
            if (StringUtils.hasText(jwtToken)) {
                UserDetails userDetails = jwtUserDetailsService.loadUserDetails(jwtTokenUtil.getUsernameFromToken(jwtToken));
                if (!jwtTokenUtil.isTokenValid(jwtToken, userDetails)) {
                    throw new IllegalArgumentException("User of this Token is not valid probably not registered in database");
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                //System.out.println("Cannot set the Security Context");
            }
        } catch (ExpiredJwtException ex) {
            String requestURL = request.getRequestURL().toString();
            if (requestURL.contains("refreshtoken")) {
                allowForRefreshToken(ex, request);
            } else {
                allowForRefreshToken(ex, request);
            }
        } catch (BadCredentialsException ex) {
            request.setAttribute("exception", ex);
        }

        chain.doFilter(request, response);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());

    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}