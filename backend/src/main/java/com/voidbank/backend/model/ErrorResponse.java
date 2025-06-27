package com.voidbank.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String errorId;
    private String error;
    private String errorMessage;
    private LocalDateTime moment;
    private int httpStatus;
    private List<FieldErrorDetail> fieldErrors;
}
