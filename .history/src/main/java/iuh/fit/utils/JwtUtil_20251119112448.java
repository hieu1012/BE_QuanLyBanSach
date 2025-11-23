package iuh.fit.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret:MySecretKeyForJWTTokenGenerationAndValidationPurposesOnly1234567890!@#$%^&*()")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")  // 24 hours in milliseconds
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}")  // 7 days in milliseconds
    private long refreshTokenExpiration;

    /**
     * Generate JWT token từ User entity
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().toString());
        claims.put("fullName", user.getFullName());
        claims.put("id", user.getId());
        
        return createToken(claims, user.getUsername(), jwtExpiration);
    }

    /**
     * Generate Refresh token
     */
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("id", user.getId());
        
        return createToken(claims, user.getUsername(), refreshTokenExpiration);
    }

    /**
     * Tạo JWT token với claims
     */
    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Lấy tất cả claims từ token
     */
    public Claims getAllClaimsFromToken(String token) {
        return (Claims) Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Lấy username từ token
     */
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Lấy ID user từ token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object idObj = claims.get("id");
        if (idObj instanceof Number) {
            return ((Number) idObj).longValue();
        }
        return null;
    }

    /**
     * Lấy Role từ token
     */
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("role");
    }

    /**
     * Lấy authorities (GrantedAuthority) từ token
     */
    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
        String role = getRoleFromToken(token);
        if (role != null) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return new ArrayList<>();
    }

    /**
     * Kiểm tra token có hợp lệ không
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Kiểm tra token có hết hạn không
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Lấy secret key dùng để sign token
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Lấy thời gian hết hạn của token
     */
    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }
}
