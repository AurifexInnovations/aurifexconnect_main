package com.erp.Security.JWT;

import com.erp.Config.AppEnv;
import com.erp.Exception.JWTException.InvalidJWTException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class JWTService {

    private final AppEnv env;

    public String generateToken(TokenPayload tokenPayload){
        return Jwts.builder()
                .setClaims(tokenPayload.claims())
                .setIssuedAt(Date.from(tokenPayload.issuedAt()))
                .setExpiration(Date.from(tokenPayload.expiration()))
                .signWith(KeyHolder.getKey(env.getSecurity().getSecret()) ,SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(KeyHolder.getKey(env.getSecurity().getSecret()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException | IllegalArgumentException e){
            throw new InvalidJWTException("Invalid! failed to pass token ,Invalid JWT");
        }
    }

}