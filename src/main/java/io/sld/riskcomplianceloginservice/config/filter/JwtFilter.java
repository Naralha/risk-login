package io.sld.riskcomplianceloginservice.config.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.sld.riskcomplianceloginservice.config.jwt.JwtUserDetailsService;
import io.sld.riskcomplianceloginservice.config.jwt.TokenManager;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//@Order(Integer.MIN_VALUE)
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private TokenManager tokenManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        String path = request.getRequestURI();
        if("/login".equals(path)){
            filterChain.doFilter(request, response);
            return;
        }
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            try {
                username = tokenManager.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                response.sendError(HttpStatus.SC_UNAUTHORIZED, "Unable to get JWT Token");
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                response.sendError(HttpStatus.SC_UNAUTHORIZED, "JWT Token has expired");
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("Bearer String not found in token");
            response.sendError(HttpStatus.SC_UNAUTHORIZED, "Bearer String not found in token");
            throw new ServletException("Bearer String not found in token");
        }
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenManager.validateJwtToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken
                        authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
