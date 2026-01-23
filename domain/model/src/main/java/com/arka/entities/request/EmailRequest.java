package com.arka.entities.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private String templateHtmlBody;
}
