package titulares.entrenamiento.security;

public class UserContext {

    private static final ThreadLocal<UsuarioInfo> userHolder = new ThreadLocal<>();

    public static void setUser(UsuarioInfo user) {
        userHolder.set(user);
    }

    public static UsuarioInfo getUser() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }

    public static class UsuarioInfo {
        private final String username;
        private final String rol;

        public UsuarioInfo(String username, String rol) {
            this.username = username;
            this.rol = rol;
        }

        public String getUsername() {
            return username;
        }

        public String getRol() {
            return rol;
        }
    }
}
