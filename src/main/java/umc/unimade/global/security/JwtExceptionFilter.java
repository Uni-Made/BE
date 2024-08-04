package umc.unimade.global.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.GlobalErrorCode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtExceptionFilter extends OncePerRequestFilter {

    private final List<String> urls = Arrays.asList(
            "/oauth2/authorization/kakao", "/oauth2/authorization/naver", "/oauth2/authorization/google",
            "/api/v1/auth/naver/callback", "/api/v1/auth/kakao/callback", "/api/v1/auth/google/callback"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        boolean isException = false;
        try {
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            request.setAttribute("exception", ErrorCode.LOGIN_ACCESS_DENIED);
            isException = true;
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_MALFORMED);
            isException = true;
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_TYPE);
            isException = true;
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_EXPIRED);
            isException = true;
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_UNSUPPORTED);
            isException = true;
        } catch (JwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_UNKNOWN);
            isException = true;
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.USER_NOT_FOUND);
            isException = true;
        }

        if (isException) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return urls.contains(request.getRequestURI());
    }
}

