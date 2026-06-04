package titulares.entrenamiento.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * capturar y validar las
 *  credenciales de acceso
 *  modelo de entrada para los endpoints de autenticación
 */
@Data
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
