package com.arka;

import com.arka.entities.request.EmailRequest;
import com.arka.gateway.EmailGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesEmailAdapter implements EmailGateway {

    private final SesClient sesClient;

    @Value("${cloud-provider.aws.ses.sender}")
    private String senderEmail;

    @Override
    public void sendEmail(EmailRequest emailRequest) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(Destination.builder()
                            .toAddresses(emailRequest.getRecipient())
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data(emailRequest.getSubject() != null ? emailRequest.getSubject() : "Password Reset")
                                    .build())
                            .body(Body.builder()
                                    .text(Content.builder()
                                            .data(emailRequest.getBody())
                                            .build())
                                    .build())
                            .build())
                    .source(senderEmail)
                    .build();

            sesClient.sendEmail(request);
            log.info("Email sent successfully to: {}", emailRequest.getRecipient());
        } catch (SesException e) {
            log.error("Error sending email to: {}", emailRequest.getRecipient(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
