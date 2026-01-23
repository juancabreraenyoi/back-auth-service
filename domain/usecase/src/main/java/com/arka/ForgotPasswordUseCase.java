package com.arka;

import com.arka.entities.PasswordResetToken;
import com.arka.entities.User;
import com.arka.entities.request.EmailRequest;
import com.arka.entities.request.ForgotPassword;
import com.arka.gateway.EmailGateway;
import com.arka.gateway.PasswordResetTokenGateway;
import com.arka.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class ForgotPasswordUseCase {

    private final UserGateway userGateway;
    private final PasswordResetTokenGateway passwordResetTokenGateway;
    private final EmailGateway emailGateway;
    @Value("${jwt-provider.password-rest.token-expiration-minutes}")
    private Long expiredPassword;


    public void execute(ForgotPassword forgotPassword) {
                userGateway.findUserByEmail(forgotPassword.getEmail())
                        .ifPresent(user -> {
                            passwordResetTokenGateway.deleteByUser(user.getEmail());
                            var uuid = UUID.randomUUID().toString();
                            var passwordResetToken = getPasswordResetTokenBuilder(user, uuid);
                            passwordResetTokenGateway.save(passwordResetToken);
                            emailGateway.sendEmail(EmailRequest.builder()
                                            .recipient(forgotPassword.getEmail())
                                            .body(passwordResetToken.getToken())
                                    .build());
                        });
    }

    private PasswordResetToken getPasswordResetTokenBuilder(User user, String uuid) {
        return PasswordResetToken.builder()
                .token(uuid)
                .userId(user.getId().toString())
                .used(false)
                .expiryDate(LocalDateTime.now().minusDays(expiredPassword))
                .build();
    }

}
