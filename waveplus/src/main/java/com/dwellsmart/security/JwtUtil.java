package com.dwellsmart.security;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

	public String generateToken(UserDetails userDetails) {

		// Extract role from UserDetails
		String role = "USER"; // Default role if none found
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			role = authority.getAuthority(); // Assuming there is only one role
			break; // Exit loop after the first role
		}

		Map<String, Object> claims = Map.of("role", role);

		return createToken(claims, userDetails.getUsername());
	}

	public SecretKey getSecretKey(String key) {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
	}

	private String createToken(Map<String, Object> claims, String sub) {
		String jti = UUID.randomUUID().toString(); // Generate a unique identifier for jti

		return Jwts.builder().claims(claims).id(jti).issuer("DwellSMART pvt. ltd.").subject(sub)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration * 1000))
				.signWith(this.getSecretKey(secretString)).compact();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
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
			if (claims.getIssuer() == null || !claims.getIssuer().equals("DwellSMART pvt. ltd.")) {
				throw new RuntimeException("Invalid issuer");
			}

			return claims;
		} catch (ExpiredJwtException e) {
			// Handle expired token
			throw new RuntimeException("Token has expired", e);
		} catch (PrematureJwtException e) {
			// Handle token used before nbf date
			throw new RuntimeException("Token is not yet valid", e);
		} catch (UnsupportedJwtException e) {
			throw new RuntimeException("Unsupported token.", e);
		} catch (MalformedJwtException e) {
			throw new RuntimeException("Malformed token.", e);
		} catch (SignatureException e) {
			throw new RuntimeException("Invalid token signature.", e);
		} catch (Exception e) {
			// Handle other possible exceptions
			throw new RuntimeException("Invalid token", e);
		}

	}


}
