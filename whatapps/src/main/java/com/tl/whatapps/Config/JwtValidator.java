package com.tl.whatapps.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Slf4j
public class JwtValidator extends OncePerRequestFilter {

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");

        if (jwt != null && jwt.startsWith("Bearer ")) {
            try {

                if (jwt != null && jwt.startsWith("Bearer ")) {
                    jwt = jwt.substring(7);

                }

                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                System.out.println("JWT Secret Key Hash: " + Arrays.toString(JwtConstant.SECRET_KEY.getBytes()));
                Claims claim = Jwts.parser()
                    .setSigningKey(key) // Use parserBuilder instead
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

                System.out.println("Decoded Claims: " + claim);
                String username = String.valueOf(claim.get("email"));
                String authorities = String.valueOf(claim.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {

                throw new BadCredentialsException("Invalid token received...",e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
