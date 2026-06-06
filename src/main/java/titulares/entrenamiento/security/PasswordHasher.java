package titulares.entrenamiento.security;

import org.mindrot.jbcrypt.BCrypt;
/**
 * Clase utilitaria encargada de la seguridad de contraseñas mediante hashing
 * Proporciona métodos estáticos para encriptar contraseñas en texto plano y para
 * verificar si una contraseña ingresada coincide con un hash previamente guardado,
 * utilizando el algoritmo BCrypt
 */
public class PasswordHasher {
    /**
     * Genera un hash seguro a partir de una contraseña en texto plano utilizando BCrypt
     * garantizando una alta resistencia contra ataques de fuerza bruta y tablas de arcoíris (rainbow tables)
     * plainPassword es la contraseña en texto plano que se desea encriptar
     */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    /**
     * Compara una contraseña en texto plano con un hash almacenado para verificar si coinciden
     * plainPassword la contraseña en texto plano introducida por el usuario
     * hashedPassword el hash seguro guardado contra el cual se desea validar
     */
    public static boolean check(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
