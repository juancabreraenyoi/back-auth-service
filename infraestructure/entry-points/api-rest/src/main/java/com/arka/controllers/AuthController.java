package com.arka.controllers;

import com.arka.*;
import com.arka.entities.request.ForgotPassword;
import com.arka.entities.request.Login;
import com.arka.entities.request.Register;
import com.arka.request.ForgotPasswordRequest;
import com.arka.request.LoginRequest;
import com.arka.request.RegisterRequest;
import com.arka.request.ResetPasswordRequest;
import com.arka.response.AuthResponse;
import com.arka.response.MessageResponse;
import com.arka.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth",description = "Flujo de autenticacion")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @Value("${jwt-provider.expiration-date}")
    private Long expirationDate;

    /**
     * Creacion de usuarios en el sistema
     * @param request objeto de entrada para ejecucion del servicio
     * @return AuthResponse con el cliente creado
     */

    @PostMapping("/register")
    @Operation(
            summary = "Registrar un cliente nuevo",
            description = "Registor de clientes ARKA"

    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente creado con exito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            )
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        var register = Register.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        var auth = registerUserUseCase.execute(register);

        var response = AuthResponse.of(
                auth.getAccessToken(),
                expirationDate,
                UserResponse.of(auth.getUserResponse())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        var login = Login.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        var auth = loginUserUseCase.execute(login);

        var response = AuthResponse.of(
                auth.getAccessToken(),
                expirationDate,
                UserResponse.of(auth.getUserResponse())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        var forgotPassword = ForgotPassword.builder()
                .email(request.getEmail())
                .build();

        forgotPasswordUseCase.execute(forgotPassword);

        return ResponseEntity.ok(MessageResponse.builder()
                .message("Password reset email sent successfully")
                .build());
    }

    @PostMapping("/password/reset")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request.getToken(), request.getNewPassword());

        return ResponseEntity.ok(MessageResponse.builder()
                .message("Password reset successfully")
                .build());
    }
}
