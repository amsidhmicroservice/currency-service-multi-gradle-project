package com.amsidh.mvc.currencyexchange.controller;

import com.amsidh.mvc.currencyexchange.entity.Exchange;
import com.amsidh.mvc.currencyexchange.repository.ExchangeRepository;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.org.webcompere.modelassert.json.JsonAssertions;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(ExchangeController.class)
@ActiveProfiles("test")
class ExchangeControllerTest {
    private final MockMvc mockMvc;
    @MockBean
    private InstanceInformationService instanceInformationService;

    @MockBean
    private ExchangeRepository exchangeRepository;

    @Test
    void testHealthCheck() throws Exception {
        log.info("Created the request");
        Mockito.when(instanceInformationService.retrieveInstanceInfo()).thenReturn("NoVersion:localhost");

        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-exchange/");
        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder).andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("{\"status\": \"up\"}", response.getContentAsString());
    }

    @Test
    void testHealthCheck_UnHealthy() throws Exception {
        log.info("Created the request");
        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-exchange/_invalid");
        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder).andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(404, response.getStatus());
        Assertions.assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void testConvertCurrency() throws Exception {
        final Exchange exchange = getExchange();
        Mockito.when(exchangeRepository.findExchangeByCurrencyFromAndCurrencyTo("USD", "INR")).thenReturn(exchange);
        String conversionEnvironmentInfo = "0.0.3-SNAPSHOT : currency-conversion-7f75b4b757-ghcxk";
        Mockito.when(instanceInformationService.retrieveInstanceInfo()).thenReturn(conversionEnvironmentInfo);

        log.info("Created the request");
        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-exchange/{currencyFrom}/to/{currencyTo}", "USD", "INR");
        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder).andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertNotNull(response.getContentAsString());
        JsonAssertions.assertJson(response.getContentAsString()).at("/currencyTo").isText("INR");
        JsonAssertions.assertJson(response.getContentAsString()).at("/currencyFrom").isText("USD");
        JsonAssertions.assertJson(response.getContentAsString()).at("/conversionMultiple").isDouble();
        JsonAssertions.assertJson(response.getContentAsString())
                .isNotNull()
                .isNotNumber()
                .isObject()
                .containsKey("id")
                .containsKeys("id", "currencyFrom", "currencyTo", "exchangeEnvironmentInfo", "conversionMultiple");
    }


    @Test
    void testConvertCurrencyWithCurrencyNotConfigured() throws Exception {
        Mockito.when(exchangeRepository.findExchangeByCurrencyFromAndCurrencyTo("YSD", "PNR")).thenReturn(null);
        log.info("Created the request");
        //Created the request
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/currency-exchange/{currencyFrom}/to/{currencyTo}", "USD", "INR");
        log.info("Call the API and get the response");
        //Call the API and get the response
        MockHttpServletResponse response = mockMvc.perform(mockHttpServletRequestBuilder).andReturn().getResponse();
        log.info("Evaluate the response");
        //Evaluate the response
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

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
