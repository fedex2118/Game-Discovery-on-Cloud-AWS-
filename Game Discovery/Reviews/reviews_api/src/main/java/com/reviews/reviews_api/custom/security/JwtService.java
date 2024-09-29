package com.reviews.reviews_api.custom.security;

import java.security.Key;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.reviews.reviews_api.application.AppProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Autowired
	private AppProperties appProperties;
	
	public UserDetails parseToken(String token) {
        final Claims claims = extractAllClaims(token);

        String username = claims.getSubject();
        List<SimpleGrantedAuthority> authorities = List.of(
        		new SimpleGrantedAuthority(claims.get("role").toString()));
        
        return new User(username, "", authorities);
	}
	
	private Claims extractAllClaims(String token) {
	    try {
	        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	    } catch (ExpiredJwtException e) {
	    	// the validation already occured in the authorization service 
	    	// so an expiration blocking exception at this point could only lead to database inconsistency problems
	        return e.getClaims();
	    }
	}
	
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(appProperties.getJwtConfig().getSecretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
