package com.voidbank.backend.controller.request;

import com.voidbank.backend.model.enums.DocumentType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateAccountRequest {

    private String ownerName;
    private String document;
    private DocumentType documentType;

}
