package com.arka.gateway;

import com.arka.entities.request.EmailRequest;

public interface EmailGateway {
    void sendEmail(EmailRequest emailRequest);
}
