package com.amsidh.mvc.currencyconversion.controller;

import com.amsidh.mvc.currencyconversion.client.response.Exchange;
import com.amsidh.mvc.currencyconversion.response.CurrencyConversionResponse;
import com.amsidh.mvc.service.InstanceInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

import static net.logstash.logback.argument.StructuredArguments.v;

/**
 * @author amsid
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/currency-conversion")
public class ConversionController {

    private final RestTemplate restTemplate;
    private final InstanceInformationService instanceInformationService;

    @Value("${CURRENCY_EXCHANGE_URL:http://localhost:8181}")
    public String currencyExchangeUrl;

    @GetMapping("/")
    public String healthCheck() {
        log.info("=======================================Start Request================================================");
        log.info("healthCheck method of ConversionController on host {}", instanceInformationService.retrieveInstanceInfo());
        log.info("=======================================End Request================================================");
        return "{status:up}";
    }

    //http://35.222.88.162:8282/currency-conversion/from/USD/to/INR/quantity/100
    @GetMapping(value = {"/from/{currencyFrom}/to/{currencyTo}/quantity/{quantity}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public CurrencyConversionResponse convertCurrency(@PathVariable("currencyFrom") String currencyFrom, @PathVariable("currencyTo") String currencyTo, @PathVariable("quantity") BigDecimal quantity) {
        log.info("=======================================Start Request================================================");
        log.info("Inside convertCurrency method of ConversionController!!!");
        final long requestStartTime = System.currentTimeMillis();
        final UriComponentsBuilder currencyExchangeURL = UriComponentsBuilder.fromUriString(currencyExchangeUrl + "/currency-exchange/{currencyFrom}/to/{currencyTo}");
        final ResponseEntity<Exchange> responseEntity = restTemplate.getForEntity(currencyExchangeURL.build(currencyFrom, currencyTo), Exchange.class);
        final Exchange exchange = responseEntity.getBody();
        log.info("Currency-exchange service url {} is completed in {}ms with status {}", v("RemoteServiceUrl", currencyExchangeURL.build().getPath()), v("TimeTaken", (System.currentTimeMillis() - requestStartTime)), v("status", responseEntity.getStatusCode().value()));

        CurrencyConversionResponse.CurrencyConversionResponseBuilder builder = CurrencyConversionResponse.builder()
                .from(currencyFrom)
                .to(currencyTo)
                .quantity(quantity)
                .conversionEnvironmentInfo(instanceInformationService.retrieveInstanceInfo());


        builder = exchange != null ? builder
                .id(exchange.getId())
                .conversionMultiple(exchange.getConversionMultiple())
                .exchangeEnvironmentInfo(exchange.getExchangeEnvironmentInfo())
                .totalCalculatedAmount(quantity.multiply(exchange.getConversionMultiple())) : builder;
        log.info("=======================================End Request================================================");
        return builder.build();
    }
}
