package com.martzatech.vdhg.crmprojectback.infrastructure.configs;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.UnauthorizedException;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


@Component
public class Jwt {
    @Value("${app.security.jwt.secret}")
    private String key;

    @Value("${app.security.jwt.issuer}")
    private String issuer;

    @Value("${app.security.jwt.ttlMillis}")
    private long ttlMillis;

    @Autowired
    private UserService userService;

    private final Logger log = LoggerFactory
            .getLogger(Jwt.class);

    /**
     * Create a new token.
     *
     * @param id
     * @param subject
     * @return
     */
    public String create(String id, String subject,String email) {

        // The JWT signature algorithm used to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //  sign JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //  set the JWT Claims
        JwtBuilder builder = Jwts
                .builder().setId(id)
                .setIssuedAt(now
                ).setSubject(subject)
                .setIssuer(issuer)
                .claim("email",email)
                .signWith(signatureAlgorithm, signingKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * Method to validate and read the JWT
     *
     * @param jwt
     * @return
     */
    public String getValue(String jwt) {
        // This line will throw an exception if it is not a signed JWS (as
        // expected)
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key))
                .parseClaimsJws(jwt).getBody();

        return claims.getSubject();
    }

    /*public User findCurrentUser(String jwtToken) {
        Claims claims = null;
        ResponseEntity<String> response ;
        try {
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(jwtToken)
                    .getBody();

        }catch (SecurityException e){
            throw new SecurityException("Error al procesar el token JWT", e);
        }
        String email = claims.get("email", String.class);
        return userService.findByEmail(email);
    }*/
    public User findCurrentUser(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(jwtToken)
                    .getBody();

            String email = claims.get("email", String.class);
            return userService.findByEmail(email);
        } catch (ExpiredJwtException ex) {
            // Token expirado
            throw new UnauthorizedException(ex.getMessage());
        } catch (JwtException ex) {
            // Otro tipo de error JWT
            throw new UnauthorizedException(ex.getMessage());
        } catch (Exception ex) {
            // Otros errores
            throw new UnauthorizedException(ex.getMessage());
        }
    }

    /**
     * Method to validate and read the JWT
     *
     * @param jwt
     * @return
     */
    /*public String getKey(String jwt) {
        Claims claims = null;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(jwt);
            claims  = claimsJws.getBody();

        } catch (SecurityException e) {
            // Captura cualquier excepción relacionada con JWT
            // Esto podría incluir problemas de firma, estructura, etc.
            throw new SecurityException("Error al procesar el token JWT", e);
        }
        return claims.getId();
    }*/

    public ResponseEntity<String> getKey(String jwt) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(jwt).getBody();
            return ResponseEntity.ok(claims.getId());
        } catch (ExpiredJwtException ex) {
            // Token expirado
            throw new UnauthorizedException("Token invalid or expired");
        } catch (JwtException ex) {
            // Otro tipo de error JWT
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación");
        } catch (Exception ex) {
            // Otros errores
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación general");
        }
    }
}
