package umc.unimade.global.security;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailService customUserDetailService;

    private final List<String> urls = Arrays.asList(
            "/oauth2/authorization/kakao", "/oauth2/authorization/naver", "/oauth2/authorization/google",
            "/api/v1/auth/naver/callback", "/api/v1/auth/kakao/callback", "/api/v1/auth/google/callback"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = JwtProvider.refineToken(request);
        Claims claims = jwtProvider.validateToken(token);

        String userId = claims.get("id").toString();
        String role = claims.get("role").toString();

        CustomUserDetail userDetails = customUserDetailService.loadUserByUsernameAndUserRole(userId, role);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return urls.contains(request.getRequestURI());
    }
}


