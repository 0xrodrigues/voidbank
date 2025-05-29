package com.voidbank.transaction_api.transaction.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageResponseApi {

    private String message;
    private LocalDateTime moment;
}
