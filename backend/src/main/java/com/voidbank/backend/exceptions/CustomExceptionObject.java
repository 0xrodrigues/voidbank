package com.voidbank.backend.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomExceptionObject {
    private String error;
    private Integer code;
    private String path;
    private LocalDateTime moment;
}
