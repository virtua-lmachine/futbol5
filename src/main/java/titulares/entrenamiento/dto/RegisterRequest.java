package titulares.entrenamiento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import titulares.entrenamiento.model.Rol;

/**
 * utilizado para capturar y validar las
 * credenciales
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}
