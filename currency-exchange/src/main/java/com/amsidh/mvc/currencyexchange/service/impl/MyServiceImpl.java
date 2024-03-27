package com.amsidh.mvc.currencyexchange.service.impl;

import com.amsidh.mvc.currencyexchange.service.MyService;
import io.opentelemetry.context.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyServiceImpl implements MyService {
    @Override
    public void processMyRequest(String message) {

        log.info("Inside processMyRequest");
        // Retrieve or create the OpenTelemetry context
        Thread myThread = new Thread(new UserThread(message, Context.current()));
        myThread.start();
        log.info("After processMyRequest in MyServiceImpl");
    }
}

