package br.com.todolist.aws_lambda_todo.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.todolist.aws_lambda_todo.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        Date now = new Date();

        Date expirationDate = new Date(now.getTime() * 720000);

        return Jwts.builder()
        .setIssuer("Todo API")
        .setSubject(user.getId().toString())
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(getSigninKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public String getSubjectFromToken(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(getSigninKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
