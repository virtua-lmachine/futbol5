package titulares.entrenamiento.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * Componente utilitario para la gestión de JSON Web Tokens Esta clase se encarga
 * de la generación, estructura, extracción de datos (claims)
 * y validación de la vigencia de los tokens JWT utilizados para la autenticación
 * y autorización de usuarios en la aplicación
 */
@Component
public class JwtUtil {
    /**
     * Clave secreta utilizada para firmar y verificar los tokens JWT.
     * Se inyecta desde las propiedades de la aplicación (application.properties/yml).
     */
    @Value("${app.jwt.secret}")
    private String secret;
    /**
     * Tiempo de expiración del token configurado en milisegundos
     */
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;
    /**
     * Genera la clave criptográfica secreta a partir del texto configurado en las propiedades
     * Utiliza el algoritmo HMAC-SHA
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un nuevo token JWT firmado con los datos del usuario y sus roles de acceso
     */
    public String generateToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }
    /**
     * extraer la totalidad de sus claims
     * Este método realiza la validación criptográfica de la firma antes de permitir el acceso
     * a los datos internos del cuerpo del token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    /**
     * Extrae el nombre de usuario (subject) contenido en el token JWT
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    /**
     * Extrae el rol personalizado almacenado dentro de los claims del token JWT
     */
    public String extractRol(String token) {
        return extractAllClaims(token).get("rol", String.class);
    }
    /**
     * Verifica si la fecha de expiración del token JWT es anterior a la fecha y hora actual
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    /**
     * Valida la estructura y vigencia del token JWT para autorizar una petición en el sistema
     */
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
