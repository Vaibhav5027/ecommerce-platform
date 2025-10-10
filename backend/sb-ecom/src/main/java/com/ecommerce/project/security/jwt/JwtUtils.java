package com.ecommerce.project.security.jwt;

import com.ecommerce.project.security.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtUtils {
  private final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${spring.app.expirationTime}")
  private long expirationTime;

  @Value("${spring.app.secretKey}")
  private String secretKey;

  @Value("${spring.ecom.app.jwtCookiesName}")
  private String jwtCookie;

  public String getTokenFromHeader(HttpServletRequest request) {
    logger.debug("Authorization header: {}", request.getHeader("Authorization"));
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
      return authorizationHeader.substring(7);
    }
    return null;
  }

  public String getCookieFromHeader(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
    String jwtToken = generateJwtToken(userDetails.getUsername());
    logger.debug("Cookie:{}", jwtToken);
    return ResponseCookie.from(jwtCookie, jwtToken)
        .path("/api")
        .maxAge(24 * 24 * 60)
        .httpOnly(false)
        .build();
  }

  public String generateJwtToken(String username) {
    logger.debug("Username :{}", username);
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + expirationTime))
        .signWith(key())
        .compact();
  }

  public SecretKey key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .verifyWith(key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public Boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
      return true;
    } catch (MalformedJwtException e) {
      logger.debug("Invalid JWT: {}", e.getMessage());
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
      logger.debug("Expired JWT: {}", e.getMessage());
    } catch (io.jsonwebtoken.security.SecurityException e) {
      logger.debug("Invalid signature: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.debug("Empty or null JWT: {}", e.getMessage());
    }
    return false;
  }

  public ResponseCookie getCleanJwtCookies() {
    return ResponseCookie.from(jwtCookie, null).path("/api").build();
  }
}
