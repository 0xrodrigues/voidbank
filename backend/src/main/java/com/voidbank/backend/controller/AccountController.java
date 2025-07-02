package com.voidbank.backend.controller;

import com.voidbank.backend.api.AccountApi;
import com.voidbank.backend.controller.request.CreateAccountRequest;
import com.voidbank.backend.controller.response.MessageResponseApi;
import com.voidbank.backend.model.Account;
import com.voidbank.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.voidbank.backend.util.MapperUtil.map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<MessageResponseApi> createAccount(CreateAccountRequest req) {
        log.info("Method createAccount - request {}", req);
        accountService.createAccount(map(req, Account.class));
        return ResponseEntity.ok(new MessageResponseApi("Account created successfully", LocalDateTime.now()));
    }

}
