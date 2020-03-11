package com.usmanadio.banka.security;

import com.usmanadio.banka.exceptions.CustomException;
import org.springframework.http.HttpStatus;
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
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsServiceImpl myUserDetails;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl myUserDetails) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myUserDetails = myUserDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        String email = null;
        if (token != null) {
            email = jwtTokenProvider.getEmail(token);
        }
//        Authentication auth = jwtTokenProvider.getAuthentication(token);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetails.loadUserByUsername(email);
                if (jwtTokenProvider.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new CustomException("This token is either expired or does not belong to you", HttpStatus.BAD_REQUEST);
                }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
