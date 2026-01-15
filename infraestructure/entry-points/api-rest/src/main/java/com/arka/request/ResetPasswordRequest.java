package com.arka.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Token es obligatorio")
    private String token;

    @NotBlank(message = "el Nuevo password es obligatorio")
    private String newPassword;
}
