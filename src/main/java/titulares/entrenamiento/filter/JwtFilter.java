package titulares.entrenamiento.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import titulares.entrenamiento.security.JwtUtil;
import titulares.entrenamiento.security.UserContext;

import java.io.IOException;
/**
 * validar la autenticación basada en JWT para las solicitudes que acceden a los recursos protegidos
 * de la aplicación,Extiende OncePerRequestFilter para garantizar que
 * el filtro se ejecute una única vez por cada solicitud HTTP
 * //
 * Su responsabilidad principal es validar la presencia y
 * validez del token JWT, extraer la información del usuario,
 * establecer el contexto de seguridad y verificar los permisos
 * de acceso según el rol autenticado
 */
public class JwtFilter extends OncePerRequestFilter {
    /**
     * Utilidad encargada de generar, validar y extraer información
     * de los tokens JWT.
     */
    private final JwtUtil jwtUtil;
    /**
     * Constructor que inicializa el filtro con la utilidad JWT.
     */
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    /**
     * Procesa cada solicitud HTTP interceptada por el filtro.
     * Este método realiza las siguientes acciones:
     * Valida la existencia del encabezado Authorization
     *  Verifica la validez del token JWT
     *  Extrae la información del usuario autenticado
     *  Valida permisos según el rol asociado al token
     * @param filterChain cadena de filtros que continuará procesando la solicitud.
     * @throws ServletException si ocurre un error durante el procesamiento del servlet.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Permitir opciones básicas y puntos finales de autenticación
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Falta el token de autenticación (Bearer)");
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token de autenticación inválido o expirado");
            return;
        }

        try {
            String username = jwtUtil.extractUsername(token);
            String rol = jwtUtil.extractRol(token);

            // Establecer el contexto
            UserContext.setUser(new UserContext.UsuarioInfo(username, rol));

            // Comprobación de acceso por rol
            // La solicitud POST a /api/entrenamientos requiere el rol ENTRENADOR
            if (path.startsWith("/api/entrenamientos")) {
                if ("POST".equalsIgnoreCase(request.getMethod())) {
                    if (!"ENTRENADOR".equalsIgnoreCase(rol)) {
                        sendErrorResponse(response, HttpStatus.FORBIDDEN, "Acceso denegado: solo el ENTRENADOR puede registrar entrenamientos");
                        return;
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Error procesando el token: " + e.getMessage());
        } finally {
            UserContext.clear();
        }
    }
    /**
     * Construye y envía una respuesta de error en formato JSON Este método centraliza la generación de respuestas
     * para errores de autenticación y autorización
     */
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String json = String.format("{\"error\": \"%s\", \"status\": %d}", message, status.value());
        response.getWriter().write(json);
    }
}
