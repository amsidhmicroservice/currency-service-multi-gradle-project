package com.amsidh.mvc.service.impl;

import brave.propagation.CurrentTraceContext;
import com.amsidh.mvc.service.MyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Slf4j
@Service
public class MyServiceImpl implements MyService {
    private final CurrentTraceContext currentTraceContext;
    @Override
    public void processMyRequest(String message) {

        log.info("Inside processMyRequest");
        Thread myThread = new Thread(new UserThread(message));
        this.currentTraceContext.executor(Executors.newCachedThreadPool()).execute(myThread);
        log.info("After processMyRequest in MyServiceImpl");
    }
}

