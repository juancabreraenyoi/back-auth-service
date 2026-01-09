package com.arka.gateway;

import com.arka.entities.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest emailRequest);
}
