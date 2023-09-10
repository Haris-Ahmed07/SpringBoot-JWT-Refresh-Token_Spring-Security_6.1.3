package com.jwt.helper;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jwt.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/*	JWT Helper Class, it provides many services like
 * 1) generate token
 * 2) validate token
 * 3) extract user name from token
 * 4) extract expiration Date from token
 * 5) extract roles from token 
 * 6) check token expiration validity 
*/
@Component
public class JwtUtil {

//    Expiration Validity From Current Time
	@Value("${jwt.token.validity}")
    public long JWT_TOKEN_VALIDITY;
    
//    Secret Encoding Key 
    @Value("${secret.key}")
    private String secret;

//	  Extract user name from token    
    public String getUsernameFromToken(String token) 
    {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    public String getPasswordFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("password");
    }
    
//	  Extract Expiration Date from token 
    public Date getExpirationDateFromToken(String token) 
    {
        return getClaimFromToken(token, Claims::getExpiration);
    }
//	  Extract Specific Claim from token 
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) 
    {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
//	  Extract All Claims from token 
    private Claims getAllClaimsFromToken(String token) 
    {
    	return Jwts
    	        .parserBuilder()
    	        .setSigningKey(getSigningKey())
    	        .build()
    	        .parseClaimsJws(token)
    	        .getBody();

    }
//	  Check the Token Expiration Validity 
    private Boolean isTokenExpired(String token) 
    {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


//	  Generate Token  
    public String generateToken(User user) {
    	
    	Map<String, Object> claims = new HashMap<>();
        claims.put("password", user.getPassword());
        claims.put("role", user.getRole());
    	
        return Jwts
          .builder()
          .setClaims(claims)
          .setSubject(user.getUsername())
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
          .signWith(getSigningKey(), SignatureAlgorithm.HS256)
          .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
//    Return TypeCasted Security Key (String -> java.security.Key )
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}