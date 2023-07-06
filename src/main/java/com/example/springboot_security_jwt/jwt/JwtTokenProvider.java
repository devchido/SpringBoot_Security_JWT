package com.example.springboot_security_jwt.jwt;

import com.example.springboot_security_jwt.security.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${com.example.springboot_security_jwt.jwt.secret}")
    private String JWT_SECRET;
    @Value("${com.example.springboot_security_jwt.jwt.expiration}")
    private int JWT_EXPIRATION;
    // Tạo jwt từ thông tin users
    public String generateToken(CustomUserDetails customUserDetails){
        Date now = new Date();
        Date dateExpired = new Date(now.getTime()+JWT_EXPIRATION);
        // Tạo chuỗi jwt web token từ userName của user
        return Jwts.builder().setSubject(customUserDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(dateExpired)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }
    // Lấy thông tin users từ jwt
    public String geUserNameFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        // Trả lại userName
        return claims.getSubject();
    }
    // Validate thông tin của jwt
    public boolean vailidateToken(String token){
        try {
            Jwts.parser().setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex){
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException ex){
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException ex){
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException ex){
            log.error("JWT claims String is emtry");
        }
        return false;
    }
}
