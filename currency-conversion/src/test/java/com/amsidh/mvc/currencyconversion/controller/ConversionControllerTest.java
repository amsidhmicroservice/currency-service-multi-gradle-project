package com.amsidh.mvc.currencyconversion.controller;

import com.amsidh.mvc.currencyconversion.client.response.Exchange;
import com.amsidh.mvc.service.InstanceInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(ConversionController.class)
@ActiveProfiles("test")
class ConversionControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private InstanceInformationService instanceInformationService;

    @Test
    void testHealthCheck() throws Exception {
        log.info("Created the request");
        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-conversion/");
        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("{status:up}", response.getContentAsString());
    }

    @Test
    void testHealthCheck_UnHealthy() throws Exception {
        log.info("Created the request");
        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-conversion/_invalid");
        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(404, response.getStatus());
        Assertions.assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void testConvertCurrency() throws Exception {

        Exchange exchange = getExchange();

        UriComponentsBuilder currencyExchangeURL = UriComponentsBuilder.fromUriString("http://localhost:8181/currency-exchange/{currencyFrom}/to/{currencyTo}");
        Mockito.when(restTemplate.getForEntity(currencyExchangeURL.build("USD", "INR"), Exchange.class))
                .thenReturn(new ResponseEntity<>(exchange, HttpStatus.OK));

        String conversionEnvironmentInfo = "0.0.3-SNAPSHOT : currency-conversion-7f75b4b757-ghcxk";
        Mockito.when(instanceInformationService.retrieveInstanceInfo()).thenReturn(conversionEnvironmentInfo);

        log.info("Created the request");
        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-conversion/from/{currencyFrom}/to/{currencyTo}/quantity/{quantity}",
                "USD", "INR", 100);
        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertNotNull(response.getContentAsString());
    }



    void testConvertCurrencyWithExchangeServiceDown() throws Exception {

        UriComponentsBuilder currencyExchangeURL = UriComponentsBuilder.fromUriString("http://localhost:8181/currency-exchange/{currencyFrom}/to/{currencyTo}");
        Mockito.when(restTemplate.getForEntity(currencyExchangeURL.build("USD", "INR"), Exchange.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        String conversionEnvironmentInfo = "0.0.3-SNAPSHOT : currency-conversion-7f75b4b757-ghcxk";
        Mockito.when(instanceInformationService.retrieveInstanceInfo()).thenReturn(conversionEnvironmentInfo);

        log.info("Created the request");
        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-conversion/from/{currencyFrom}/to/{currencyTo}/quantity/{quantity}",
                "USD", "INR", 100);

        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder)
                .andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(200, response.getStatus());
        //See the response does not contain response from exchange service as it is having internal service issue as set
        Assertions.assertNotNull(response.getContentAsString());


    }

    private Exchange getExchange() {
        Exchange exchange = new Exchange();
        exchange.setCurrencyTo("INR");
        exchange.setCurrencyFrom("USD");
        exchange.setConversionMultiple(new BigDecimal("60.65"));
        exchange.setId(1L);
        exchange.setExchangeEnvironmentInfo("0.0.3-SNAPSHOT : currency-exchange-55db59c984-2dnld");
        return exchange;
    }
}