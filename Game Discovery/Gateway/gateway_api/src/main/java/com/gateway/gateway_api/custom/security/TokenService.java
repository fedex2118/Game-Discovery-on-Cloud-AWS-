package com.gateway.gateway_api.custom.security;

import java.security.Key;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.gateway_api.application.AppProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	
	private static final long EXPIRATION_TIME = 600000; // 10 minutes
	
	@Autowired
	private AppProperties appProperties;

    public String generateToken() {
    	// add the default role user
    	Map<String, Object> extraClaims = new HashMap<>();
    	extraClaims.put("role", "GATEWAY");
        return Jwts.builder()
                .setSubject("gateway")
        		.addClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }
    
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(appProperties.getJwtConfig().getSecretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
