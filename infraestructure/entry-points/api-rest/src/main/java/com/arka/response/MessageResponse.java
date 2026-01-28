package com.arka.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String message;

    public MessageResponse of(String message) {
        return MessageResponse.builder()
                .message(message)
                .build();
    }

}
