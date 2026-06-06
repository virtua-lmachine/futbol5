package titulares.entrenamiento.security;
/**
 * Contenedor de contexto de seguridad basado en hilos para almacenar la información del usuario autenticado.
 * ThreadLocal para garantizar que los datos del usuario estén aislados por cada hilo de petición,
 * permitiendo acceder a la información de la sesión actual en cualquier capa de la aplicación
 * sin necesidad de pasarla explícitamente como parámetro en los métodos.
 * - Es indispensable invocar el método clear() al finalizar el ciclo de vida de la petición
 *  en un filtro o interceptor para prevenir fugas de memoria (memory leaks) debido al reciclaje de hilos en servidores
 */
public class UserContext {
    // Almacenamiento local del hilo que mantiene la información del usuario actual de manera aislada.
    private static final ThreadLocal<UsuarioInfo> userHolder = new ThreadLocal<>();
    // Vincula la información del usuario autenticado actual
    public static void setUser(UsuarioInfo user) {
        userHolder.set(user);
    }
    // Recupera la información del usuario vinculado actualmente
    public static UsuarioInfo getUser() {
        return userHolder.get();
    }
    /**
     * Limpia y remueve los datos del usuario del hilo actual
     * Este método debe llamarse de forma obligatoria al concluir el procesamiento de una solicitud
     */
    public static void clear() {
        userHolder.remove();
    }
    // Clase interna estática e inmutable que encapsula la información básica y de seguridad del usuario autenticado.
    public static class UsuarioInfo {
        private final String username;
        private final String rol;
        // Construye una nueva instancia inmutable con los datos del usuario.
        public UsuarioInfo(String username, String rol) {
            this.username = username;
            this.rol = rol;
        }
        // Obtiene el nombre de usuario almacenado en este contexto.
        public String getUsername() {
            return username;
        }
        // Obtiene el rol del usuario almacenado en este contexto
        public String getRol() {
            return rol;
        }
    }
}
