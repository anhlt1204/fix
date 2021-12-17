package com.edso.sbfix.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {

    @PostMapping("/send-message")
    String sendMessage(String message, String clientId) {
        return null;
    }
}
