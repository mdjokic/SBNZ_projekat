package ftn.sbnz.banhammer.security;

import ftn.sbnz.banhammer.model.Enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    @Value("X-Auth-Token")
    public String AUTH_TOKEN_PROPERTY_NAME;

    @Value("myXAuthSecret")
    private String SECRET;

    @Value("1800000")
    private Long EXPIRATION_TIME;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(UserDetails userDetails, Role role) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", userDetails.getUsername());
        claims.put("role", role);
        claims.put("created", new Date(System.currentTimeMillis()));
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME * 1000))
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            username = tryToGetUsernameFromToken(token);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private String tryToGetUsernameFromToken(String token){
        Claims claims = this.getClaimsFromToken(token);
        String username = claims.getSubject();
        return username;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = tryToGetClaimsFromToken(token);
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Claims tryToGetClaimsFromToken(String token){
        JwtParser tokenParser = Jwts.parser();
        tokenParser.setSigningKey(this.SECRET);
        Claims claims = tokenParser.parseClaimsJws(token).getBody();
        return claims;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date(System.currentTimeMillis()));
    }



}
