package com.amsidh.mvc.controller;

import brave.propagation.CurrentTraceContext;
import com.amsidh.mvc.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class AsyncController {
    private final CurrentTraceContext currentTraceContext;
    private final MyService myService;

    @GetMapping("async")
    public String async() {
        log.info("Before CompletableFuture call and Thread Name {}", Thread.currentThread().getName());
        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> myService.processMyRequest("Amsidh Babasha Lokhande"), currentTraceContext.executor(Runnable::run));
        log.info("After CompletableFuture call and Thread Name {}", Thread.currentThread().getName());
        return "Success1";
    }

    @GetMapping("/async2")
    public String async2() {
        log.info("Before CompletableFuture call2");
        CompletableFuture.runAsync(() -> {

            log.info("Before for loop");
            for (int i = 0; i < 10; i++) {
                log.info("Counter {}", i);
            }
            log.info("After for loop");
        }, currentTraceContext.executor(Runnable::run));
        log.info("After CompletableFuture call2");
        return "Success2";
    }
}
