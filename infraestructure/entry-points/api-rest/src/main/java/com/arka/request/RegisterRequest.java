package com.arka.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El email es obligartorio")
    @Email(message = "El email ingreso no es valido")
    private String email;

    @NotBlank(message = "El password es obligartorio")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Solo se permiten caracteres alfanuméricos")
    @Size(min = 8,message = "El password requiere minimo 8 caracteres")
    private String password;

}
