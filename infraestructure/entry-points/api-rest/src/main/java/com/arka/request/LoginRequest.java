package com.arka.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El email es obligartorio")
    private String email;

    @NotBlank(message = "El password es obligartorio")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Solo se permiten caracteres alfanuméricos")
    @Size(min = 8,message = "El password requiere minimo 8 caracteres")
    private String password;

}
