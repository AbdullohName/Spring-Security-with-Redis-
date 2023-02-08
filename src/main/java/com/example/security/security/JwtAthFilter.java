package com.example.security.security;

import com.example.security.redis.RedisRepository;
import com.example.security.redis.UsersSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisRepository repository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)) {
                String id = String.valueOf(jwtUtil.getClaim(token,"sub"));
                if(id != null) {
                    Optional<UsersSession> usersSession = repository.findById(id);
                    usersSession.ifPresent(user -> {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        user.getUser(),token,(Collection<? extends GrantedAuthority>) user.getUser().getAuthorities()
                                );
                        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
                        authToken.setDetails(details);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    });

                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
