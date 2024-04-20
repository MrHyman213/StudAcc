package org.example.StudAcc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateToken(UserDetails details){
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = details.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roleList);

        Date issuedDate = new Date();
        Date expireDate = new Date(issuedDate.getTime() + lifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(details.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsername(String token){
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token){
        return getClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .build().
                parseSignedClaims(token).
                getBody();
    }
}
