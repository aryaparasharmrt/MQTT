package com.dwellsmart.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dwellsmart.constants.Constants;
import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.constants.RoleType;
import com.dwellsmart.entity.User;
import com.dwellsmart.exception.ApplicationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {

	@Value("${app.jwt.secret}")
	private String secretString;

	@Value("${app.jwt.expiration}")
	private long expiration;

	public String generateSecureRefreshToken() {
		SecureRandom secureRandom = new SecureRandom(); // Secure random number generator
		byte[] token = new byte[24]; // 24 bytes = 192 bits
		secureRandom.nextBytes(token);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(token); // URL safe Base64 encoded string
	}

	public String generateToken(UserDetails userDetails, String deviceId) {

		List<String> authorityNames = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		if (authorityNames.isEmpty()) {
			authorityNames = Collections.singletonList(RoleType.USER.name()); // Create a list with default role
		}

		Map<String, Object> claims = Map.ofEntries(Map.entry("role", authorityNames), Map.entry("deviceId", deviceId));

		return createToken(claims, userDetails.getUsername());
	}

	public SecretKey getSecretKey(String key) {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
	}

	private String createToken(Map<String, Object> claims, String username) {
		String jti = UUID.randomUUID().toString(); // Generate a unique identifier for jti

		return Jwts.builder().claims(claims).id(jti).issuer(Constants.COMPANY_NAME).subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration * 1000))
				.signWith(this.getSecretKey(secretString)).compact();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

//	public boolean validateToken(String token, User user) {
//		final String username = extractUsername(token);
//		return (username.equals(user.getUsername()) && !isTokenExpired(token));
//	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String extractDeviceId(String token) {
		final Claims claims = extractAllClaims(token);
		return claims.get("deviceId", String.class);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		try {
			Claims claims = Jwts.parser().verifyWith(this.getSecretKey(secretString)).build().parseSignedClaims(token)
					.getPayload();
			if (claims.getIssuer() == null || !claims.getIssuer().equals(Constants.COMPANY_NAME)) {
				throw new ApplicationException(ErrorCode.INVALID_ISSUER);
			}
			return claims;
		} catch (ExpiredJwtException e) {
			throw new ApplicationException(ErrorCode.TOKEN_EXPIRED);
		} catch (PrematureJwtException e) {
			throw new ApplicationException(ErrorCode.TOKEN_NOT_YET_VALID);
		} catch (UnsupportedJwtException e) {
			throw new ApplicationException(ErrorCode.UNSUPPORTED_TOKEN);
		} catch (MalformedJwtException e) {
			throw new ApplicationException(ErrorCode.MALFORMED_TOKEN);
		} catch (SignatureException e) {
			throw new ApplicationException(ErrorCode.INVALID_SIGNATURE);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.INVALID_TOKEN);
		}
	}

}
