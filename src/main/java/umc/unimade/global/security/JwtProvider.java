package umc.unimade.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import umc.unimade.domain.accounts.entity.Role;
import umc.unimade.domain.accounts.repository.BuyerRepository;
import umc.unimade.domain.accounts.repository.SellerRepository;
import umc.unimade.global.common.ErrorCode;
import umc.unimade.global.common.exception.CustomException;
import umc.unimade.global.common.exception.GlobalErrorCode;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider implements InitializingBean {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accessExpiredMs}")
    private Long accessExpiredMs;

    @Value("${jwt.refreshExpiredMs}")
    private Long refreshExpiredMs;
    private Key key;

    @Override
    public void afterPropertiesSet() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createToken(String id, Role role, boolean isAccess) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put("id", id);
        if (isAccess) {
            claims.put("role", role);
        }

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (isAccess ? accessExpiredMs : refreshExpiredMs)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public JwtToken createTotalToken(String id, Role role) {
        return new JwtToken(
                createToken(id, role, true),
                createToken(id, role, false)
        );
    }

    public String reissueToken(HttpServletRequest request, Role role) {
        String refreshToken = refineToken(request);
        String userId = getUserId(refreshToken);
        UserLoginForm user;
        switch (role) {
            case BUYER:
                user = buyerRepository.findBySocialIdAndRefreshToken(userId, refreshToken)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                break;
            case SELLER, ADMIN:
                user = sellerRepository.findByEmailAndPassword(userId, refreshToken)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                break;
            default:
                throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

//        if (user == null) {
//            throw new CustomException(GlobalErrorCode.USER_NOT_FOUND));
//        }

        return createToken(user.getId(), user.getRole(), true);
    }

    public String getUserId(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    public Claims validateToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String refineToken(HttpServletRequest request) throws JwtException {
        String beforeToken = request.getHeader("Authorization");
        if (StringUtils.hasText(beforeToken) && beforeToken.startsWith("Bearer ")) {
            return beforeToken.substring(7);
        } else {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }
    }
}
