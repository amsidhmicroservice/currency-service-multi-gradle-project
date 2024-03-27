package com.amsidh.mvc.currencyexchange.service.impl;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserThread implements Runnable {
    private String message;
    private Context parentContext;

    UserThread(String message, Context parentContext) {
        this.message = message;
        this.parentContext = parentContext;
    }

    @Override
    public void run() {
        // Use the parent context if available, otherwise use the current context
        Context currentContext = parentContext != null ? parentContext : Context.current();

        // Make the current context current in the child thread
        try (Scope scope = currentContext.makeCurrent()) {
            for (int i = 0; i <= 10; i++) {
                log.info("Message {} and Current execution count {}", message, i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("Thread exception occurred {}", e.getMessage(), e);
                }
            }
        }
    }
}
