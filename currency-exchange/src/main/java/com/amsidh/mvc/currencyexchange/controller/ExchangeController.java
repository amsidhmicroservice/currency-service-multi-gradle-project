package com.amsidh.mvc.currencyexchange.controller;

import com.amsidh.mvc.currencyexchange.entity.Exchange;
import com.amsidh.mvc.currencyexchange.exception.MyCustomException;
import com.amsidh.mvc.currencyexchange.repository.ExchangeRepository;
import com.amsidh.mvc.currencyexchange.service.MyService;
import com.amsidh.mvc.service.InstanceInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/currency-exchange")
public class ExchangeController {
    private final ExchangeRepository exchangeRepository;
    private final InstanceInformationService instanceInformationService;
    private final MyService myService;

    @GetMapping("/")
    public String healthCheck() {
        log.info("=======Start Request=======");
        log.debug("healthCheck method of ExchangeController on host {}", instanceInformationService.retrieveInstanceInfo());
        log.info("=======End Request=======");
        return "{\"status\": \"up\"}";
    }

    //http://34.121.35.177:8181/currency-exchange/USD/to/INR
    @GetMapping(value = "/{currencyFrom}/to/{currencyTo}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Exchange getCurrencyExchange(@PathVariable("currencyFrom") String currencyFrom, @PathVariable("currencyTo") String currencyTo) {
        log.info("=======start request=======");
        log.info("Inside getCurrencyExchange method of ExchangeRepository");
        final Exchange exchange = exchangeRepository.findExchangeByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);
        if (null == exchange) {
            throw new MyCustomException("Currency not found");
        }
        exchange.setExchangeEnvironmentInfo(instanceInformationService.retrieveInstanceInfo());
        log.info("Returning the response from currency-conversion service");
        log.info("=======End Request=======");

        log.info("Before CompletableFuture call");
        CompletableFuture.runAsync(() -> myService.processMyRequest("Amsidh"));
        log.info("After CompletableFuture call");

        return exchange;
    }

}
