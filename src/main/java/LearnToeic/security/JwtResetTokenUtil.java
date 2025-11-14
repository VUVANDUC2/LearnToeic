package LearnToeic.security;


import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtResetTokenUtil {
    private final String SECRET = "change_this_to_a_very_secret_key_very_long";

    // Tạo token reset (chứa email, hết hạn)
    public String generateResetToken(String email, long minutes) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + minutes * 60 * 1000);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("type", "RESET_PASSWORD")
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate và lấy email
    public String validateAndGetEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (!"RESET_PASSWORD".equals(claims.get("type"))) {
            throw new RuntimeException("Sai loại token");
        }

        return claims.getSubject(); // email
    }
}
