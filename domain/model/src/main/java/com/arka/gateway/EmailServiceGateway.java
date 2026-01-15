package com.arka.gateway;

import com.arka.entities.EmailRequest;

public interface EmailServiceGateway {
    void sendEmail(EmailRequest emailRequest);
}
