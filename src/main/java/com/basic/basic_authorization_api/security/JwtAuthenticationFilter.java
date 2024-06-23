package com.basic.basic_authorization_api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.basic.basic_authorization_api.services.UserService;
import com.basic.basic_authorization_api.utils.JwtTokenUtils;
import com.basic.basic_authorization_api.utils.TokenType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    /**
     * Метод проверяет наличие токена доступа в request, валидирует его,
     * после чего помешает пользователя в SecurityContextHolder, в котором
     * будет хранить текущий аутентифицированные пользователь.
     * 
     * @param request
     * @param response
     * @param filterChain
     * @return 
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(
            @SuppressWarnings("null") HttpServletRequest request, 
            @SuppressWarnings("null") HttpServletResponse response, 
            @SuppressWarnings("null") FilterChain filterChain)
    throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (StringUtils.hasText(token) && this.jwtTokenUtils.validateToken(token, TokenType.ACCESS)) {
            String username = this.jwtTokenUtils.getUsernameFromAccessToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                username, 
                                null, 
                                userDetails.getAuthorities());
                
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }

}
