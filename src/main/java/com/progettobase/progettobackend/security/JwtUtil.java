package com.progettobase.progettobackend.security;

import com.progettobase.progettobackend.entity.AdminDB;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private String secret_key = "la-tua-nuova-chiave-segreta";
    private long accessTokenValidity = 10;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(){
        // Genera una chiave segreta sicura per HS256
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Codifica la chiave segreta in Base64
        String stringaChiaveSegreta = Encoders.BASE64.encode(secretKey.getEncoded());

        secret_key = stringaChiaveSegreta;

        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    // Creo il createToken per l'admin
    public String createTokenAdmin(AdminDB adminDB) {
        Claims claims = Jwts.claims().setSubject(adminDB.getEmail());
        claims.put("username",adminDB.getUsername());
        claims.put("email",adminDB.getEmail());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }



    public Claims decodeToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret_key)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) {
            System.out.println(e);
            // Gestisci eventuali errori di decodifica del token
            throw e;
        }
    }


    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }
}