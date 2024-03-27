package com.amsidh.mvc.currencyexchange.controller;

import com.amsidh.mvc.currencyexchange.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/async")
public class AsyncController {
    private final MyService myService;

    @GetMapping()
    public String async() {
        log.info("Before CompletableFuture call");
        CompletableFuture.runAsync(() -> myService.processMyRequest("Amsidh"));
        log.info("After CompletableFuture call");
        return "Success";
    }
}
