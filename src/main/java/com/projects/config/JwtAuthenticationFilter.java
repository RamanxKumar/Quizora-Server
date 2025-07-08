package com.projects.config;

import com.projects.service.CustomUserDetailsService;
import com.projects.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        // Step 1️⃣ — Get token from Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim(); // Remove "Bearer " prefix

            // ✅ Only try to extract username if token is non-empty
            if (!token.isEmpty()) {
                try {
                    username = jwtService.extractUsername(token);
                } catch (Exception e) {
                    // ⚠️ Log invalid token
                    System.out.println("Invalid JWT Token: " + e.getMessage());
                    // Optionally, you can reject the request:
                    // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                    // return;
                }
            }
        }

        // Step 2️⃣ — If username exists and user is not yet authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 3️⃣ — Load user details from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Step 4️⃣ — If token is valid, set authentication in security context
            if (jwtService.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 5️⃣ — Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
