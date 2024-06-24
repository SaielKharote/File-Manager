package org.vcriate.filemanager.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vcriate.filemanager.entities.User;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(JwtSecurityContext.JWT_HEADER);

        if (jwt != null) {
            try {
                jwt = jwt.substring(7); // Remove "Bearer " prefix

                SecretKey key = Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());

                Claims parsedToken = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = String.valueOf(parsedToken.get("email"));
                String authorities = (String) parsedToken.get("authorities");

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Create the User object from the token
                User user = getUserFromToken(parsedToken);

                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, auths);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private User getUserFromToken(Claims parsedToken) {
        String email = String.valueOf(parsedToken.get("email"));
        String username = String.valueOf(parsedToken.get("username"));


        // Create and return a User object
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        // Set other properties as needed

        return user;
    }
}
