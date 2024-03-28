package com.amsidh.mvc.service.impl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserThread implements Runnable {
    private final String message;

    UserThread(String message) {
        this.message = message;
    }

    @Override
    public void run() {
            for (int i = 0; i <= 10; i++) {
                log.info("Message {} and Current execution count {} and Thread Name {}", this.message, i, Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("Thread exception occurred {}", e.getMessage(), e);
                }
            }
    }
}
