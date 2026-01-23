package com.arka.entities.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPassword {

    @NotBlank(message = "El email es obligartorio")
    @Email(message = "El email ingreso no es valido")
    private String email;
}
