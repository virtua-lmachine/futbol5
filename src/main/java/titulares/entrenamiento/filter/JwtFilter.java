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

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Permit basic options and auth endpoints
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

            // Establish Context
            UserContext.setUser(new UserContext.UsuarioInfo(username, rol));

            // Role access check
            // POST /api/entrenamientos requires ENTRENADOR role
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

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String json = String.format("{\"error\": \"%s\", \"status\": %d}", message, status.value());
        response.getWriter().write(json);
    }
}
