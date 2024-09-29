package com.users.users_api.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.users.users_api.application.AppProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Autowired
	private AppProperties appProperties;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public String extractType(String token) {
		final Claims claims = extractAllClaims(token);
		String type = claims.get("type").toString();
		return type;
	}
	
	public boolean isAccessToken(String token) {
		String type = this.extractType(token);
		
		return switch(type) {
		case("access_token") -> true;
		case("refresh_token") -> false;
		default -> throw new IllegalArgumentException("Unexpected value: " + type);		
		};
		
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private HashMap<String, Object> defineExtraClaims(UserDetails userDetails) {
		// by default users will have 'role' as an extra claim for security reasons
		HashMap<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("role", userDetails.getAuthorities().stream().findFirst().get().getAuthority());
		return extraClaims;
	}

	public String generateToken(UserDetails userDetails) {
		HashMap<String, Object> extraClaims = defineExtraClaims(userDetails);
		return generateToken(extraClaims, userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		extraClaims.put("type", "access_token");
		return buildToken(extraClaims, userDetails, appProperties.getJwtConfig().getExpiration());
	}

	public String generateRefreshToken(UserDetails userDetails) {
		HashMap<String, Object> extraClaims = defineExtraClaims(userDetails);
		extraClaims.put("type", "refresh_token");
		return buildToken(extraClaims, userDetails, appProperties.getJwtConfig().getRefreshToken().getExpiration());
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(appProperties.getJwtConfig().getSecretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
