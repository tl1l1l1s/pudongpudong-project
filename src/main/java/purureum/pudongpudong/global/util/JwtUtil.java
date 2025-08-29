package purureum.pudongpudong.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
	
	private final SecretKey key;
	private final long expirationMs;
	
	public JwtUtil(@Value("${jwt.secret:pudongpudongSecretKeyForHackathon2025}") String secret,
	               @Value("${jwt.expiration:86400000}") long expirationMs) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		this.expirationMs = expirationMs;
	}
	
	public String generateToken(Long userId) {
		return Jwts.builder()
				.subject(userId.toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key)
				.compact();
	}
	
	public Long getUserIdFromToken(String token) {
		try {
			String subject = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getSubject();
			return Long.valueOf(subject);
		} catch (JwtException e) {
			log.error("JWT 토큰 파싱 실패: {}", e.getMessage());
			return null;
		}
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);
			return true;
		} catch (JwtException e) {
			log.error("JWT 토큰 검증 실패: {}", e.getMessage());
			return false;
		}
	}

	public String getSignatureFromToken(String token) {
		String[] chunks = token.split("\\.");
		if (chunks.length != 3) {
			throw new purureum.pudongpudong.global.apiPayload.exception.handler.ApiHandler(ErrorStatus.INVALID_TOKEN_SIGNATURE);
		}
		return chunks[2];
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaims(token).getExpiration();
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}